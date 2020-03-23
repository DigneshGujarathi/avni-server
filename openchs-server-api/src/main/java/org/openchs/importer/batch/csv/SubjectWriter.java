package org.openchs.importer.batch.csv;

import org.joda.time.LocalDate;
import org.openchs.application.Form;
import org.openchs.application.FormElement;
import org.openchs.application.FormElementType;
import org.openchs.application.FormType;
import org.openchs.dao.*;
import org.openchs.dao.application.FormRepository;
import org.openchs.domain.*;
import org.openchs.geo.Point;
import org.openchs.importer.batch.model.Row;
import org.openchs.service.ObservationService;
import org.openchs.web.request.ObservationRequest;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class SubjectWriter implements ItemWriter<Row>, Serializable {
    private enum FixedHeaders {
        id("Id"),
        subjectType("Subject Type"),
        firstName("First Name"),
        lastName("Last Name"),
        dateOfBirth("Date Of Birth"),
        dobVerified("Date Of Birth Verified"),
        registrationDate("Date Of Registration"),
        registrationLocation("Registration Location");

        private final String headerValue;

        FixedHeaders(String headerValue) {
            this.headerValue = headerValue;
        }

        public String getHeader() {
            return headerValue;
        }

        public static String[] getAllHeaders() {
            return Arrays.stream(FixedHeaders.values()).map(FixedHeaders::getHeader).toArray(String[]::new);
        }
    }

    private final OperationalSubjectTypeRepository operationalSubjectTypeRepository;
    private final AddressLevelTypeRepository addressLevelTypeRepository;
    private final LocationRepository locationRepository;
    private final ConceptRepository conceptRepository;
    private final ObservationService observationService;
    private final FormRepository formRepository;
    private final IndividualRepository individualRepository;
    private static final String legacyIdUuid = "a503b679-e73f-4852-8c1f-dddef0de975f";


    @Autowired
    public SubjectWriter(OperationalSubjectTypeRepository operationalSubjectTypeRepository,
                         AddressLevelTypeRepository addressLevelTypeRepository,
                         LocationRepository locationRepository,
                         ConceptRepository conceptRepository,
                         ObservationService observationService,
                         FormRepository formRepository,
                         IndividualRepository individualRepository) {
        this.operationalSubjectTypeRepository = operationalSubjectTypeRepository;
        this.addressLevelTypeRepository = addressLevelTypeRepository;
        this.locationRepository = locationRepository;
        this.conceptRepository = conceptRepository;
        this.observationService = observationService;
        this.formRepository = formRepository;
        this.individualRepository = individualRepository;
    }

    @Override
    public void write(List<? extends Row> rows) throws Exception {
        for (Row row : rows) write(row);
    }

    private void write(Row row) throws Exception {
        List<AddressLevelType> locationTypes = addressLevelTypeRepository.findAll();
        locationTypes.sort(Comparator.comparingDouble(AddressLevelType::getLevel).reversed());

        List<AddressLevel> locations = locationRepository.findAll();

        Set<String> nonConceptHeaders = Stream.concat(
                locationTypes.stream().map(AddressLevelType::getName),
                Stream.of(FixedHeaders.getAllHeaders())).collect(Collectors.toSet());

        List<Concept> obsConcepts = getConceptHeaders(row.getHeaders(), nonConceptHeaders)
                .stream()
                .map(conceptRepository::findByName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Individual individual = new Individual();
        List<String> allErrorMsgs = new ArrayList<>();

        setSubjectType(individual, row, allErrorMsgs);
        individual.setFirstName(row.get(FixedHeaders.firstName.getHeader()));
        individual.setLastName(row.get(FixedHeaders.lastName.getHeader()));
        setDateOfBirth(individual, row, allErrorMsgs);
        individual.setDateOfBirthVerified(row.getBool(FixedHeaders.dobVerified.getHeader()));
        setRegistrationDate(individual, row, allErrorMsgs);
        setRegistrationLocation(individual, row, allErrorMsgs);
        setAddressLevel(individual, row, locationTypes, locations, allErrorMsgs);
        setObservations(individual, row, obsConcepts, allErrorMsgs);

        if (allErrorMsgs.size() > 0) {
            throw new Exception(String.join(", ", allErrorMsgs));
        }

        individual.setVoided(false);
        individual.setUuid(UUID.randomUUID().toString());

        individualRepository.save(individual);
    }

    private Set<String> getConceptHeaders(String[] allHeaders, Set<String> nonConceptHeaders) {
        return Arrays
                .stream(allHeaders)
                .filter(header -> !nonConceptHeaders.contains(header))
                .collect(Collectors.toSet());
    }

    private void setSubjectType(Individual individual,
                                Row row,
                                List<String> errorMsgs) {
        String subjectTypeValue = row.get(FixedHeaders.subjectType.getHeader());
        SubjectType subjectType = operationalSubjectTypeRepository.findByNameIgnoreCase(subjectTypeValue).getSubjectType();
        if (subjectType == null)
            errorMsgs.add(String.format("'%s' not found", FixedHeaders.subjectType.getHeader()));
        else
            individual.setSubjectType(subjectType);
    }

    private void setDateOfBirth(Individual individual, Row row, List<String> errorMsgs) {
        try {
            String dob = row.get(FixedHeaders.dateOfBirth.getHeader());
            if (dob != null)
                individual.setDateOfBirth(LocalDate.parse(dob));
        } catch (Exception ex) {
            errorMsgs.add(String.format("Invalid '%s'", FixedHeaders.dateOfBirth.getHeader()));
        }
    }

    private void setRegistrationDate(Individual individual, Row row, List<String> errorMsgs) {
        try {
            String registrationDate = row.get(FixedHeaders.registrationDate.getHeader());
            individual.setRegistrationDate(registrationDate != null ? LocalDate.parse(registrationDate) : LocalDate.now());
        } catch (Exception ex) {
            errorMsgs.add(String.format("Invalid '%s'", FixedHeaders.registrationDate.getHeader()));
        }
    }

    private void setRegistrationLocation(Individual individual, Row row, List<String> errorMsgs) {
        try {
            String registrationLocation = row.get(FixedHeaders.registrationLocation.getHeader());
            if (registrationLocation != null) {
                String[] points = registrationLocation.split(",");
                individual.setRegistrationLocation(
                        new Point(Double.parseDouble(points[0]), Double.parseDouble(points[1])));
            }
        } catch (Exception ex) {
            errorMsgs.add(String.format("Invalid '%s'", FixedHeaders.registrationDate.getHeader()));
        }
    }

    private void setAddressLevel(Individual individual,
                                 Row row,
                                 List<AddressLevelType> locationTypes,
                                 List<AddressLevel> locations,
                                 List<String> errorMsgs) {
        try {
            AddressLevel addressLevel;
            AddressLevelType lowestAddressLevelType = locationTypes.get(locationTypes.size() - 1);

            String lowestInputAddressLevel = row.get(lowestAddressLevelType.getName());
            if (lowestInputAddressLevel == null)
                throw new Exception(String.format("Missing '%s'", lowestAddressLevelType.getName()));

            Supplier<Stream<AddressLevel>> addressMatches = () ->
                    locations.stream()
                            .filter(location ->
                                    location.getTitle()
                                            .toLowerCase()
                                            .equals(lowestInputAddressLevel.toLowerCase()));

            if (addressMatches.get().count() > 1) {
                // filter by lineage if more than one location with same name present
                addressLevel = getAddressLevelByLineage(row, locationTypes, locations);
            } else {
                // exactly 1 or no match
                addressLevel = addressMatches.get().findFirst().orElseThrow(() -> new Exception("'Address' not found"));
            }
            individual.setAddressLevel(addressLevel);
        } catch (Exception ex) {
            errorMsgs.add(ex.getMessage());
        }
    }

    private AddressLevel getAddressLevelByLineage(Row row,
                                                  List<AddressLevelType> locationTypes,
                                                  List<AddressLevel> locations) throws Exception {
        List<String> inputLocations = new ArrayList<>();
        for (AddressLevelType addressLevelType : locationTypes) {
            String _location = row.get(addressLevelType.getName());
            if (_location != null)
                inputLocations.add(_location);
        }

        if (inputLocations.size() == 0)
            throw new Exception("Invalid address");

        String lineage = String.join(", ", inputLocations);
        return locations.stream()
                .filter(location ->
                        location.getTitleLineage()
                                .toLowerCase()
                                .endsWith(lineage.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new Exception("'Address' not found"));
    }

    private ObservationRequest createLegacyIdObservationRequest(String legacyId) throws Exception {
        if (legacyId == null)
            throw new Exception("'Id' not present");
        ObservationRequest observationRequest = new ObservationRequest();
        observationRequest.setConceptUUID(legacyIdUuid);
        observationRequest.setValue(legacyId);
        return observationRequest;
    }

    private void setObservations(Individual individual,
                                 Row row,
                                 List<Concept> obsConcepts,
                                 List<String> errorMsgs) {
        List<ObservationRequest> observationRequests = new ArrayList<>();
        for (Concept concept : obsConcepts) {
            String rowValue = row.get(concept.getName());
            if (rowValue == null || rowValue.trim().equals(""))
                continue;
            ObservationRequest observationRequest = new ObservationRequest();
            observationRequest.setConceptName(concept.getName());
            observationRequest.setConceptUUID(concept.getUuid());
            try {
                observationRequest.setValue(getObservationValue(concept, rowValue));
            } catch (Exception ex) {
                errorMsgs.add(String.format("Invalid answer '%s' for '%s'", rowValue, concept.getName()));
            }
            observationRequests.add(observationRequest);
        }
        try {
            observationRequests.add(createLegacyIdObservationRequest(row.get(FixedHeaders.id.getHeader())));
        } catch (Exception ex) {
            errorMsgs.add(ex.getMessage());
        }
        ObservationCollection observations = observationService.createObservations(observationRequests);
        individual.setObservations(observations);
    }

    private Object getObservationValue(Concept concept, String answerValue) throws Exception {
        switch (ConceptDataType.valueOf(concept.getDataType())) {
            case Coded:
                List<Form> individualProfileForms = formRepository.findAllByFormType(FormType.IndividualProfile);
                if (individualProfileForms.size() == 0)
                    throw new Exception("No forms of type IndividualProfile found");

                FormElement formElement = individualProfileForms.stream()
                        .map(Form::getAllFormElements)
                        .flatMap(List::stream)
                        .filter(fel -> fel.getConcept().equals(concept))
                        .findFirst()
                        .orElseThrow(() -> new Exception("No form element linked to concept found"));

                if (formElement.getType().equals(FormElementType.MultiSelect.name())) {
                    /* For multi-select answers, expected input format would be:
                       1. Answer 1, Answer 2, ...
                       2. Answer 1, "Answer2, has, commas", Answer 3, ...
                       ... etc.
                    */
                    String[] providedAnswers = Stream.of(answerValue.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"))
                            .map(value -> value.trim().replaceAll("\"", ""))
                            .toArray(String[]::new);
                    return Stream.of(providedAnswers)
                            .map(answer -> concept.findAnswerConcept(answer).getUuid())
                            .collect(Collectors.toList());
                } else {
                    return concept.findAnswerConcept(answerValue).getUuid();
                }
            case Numeric:
                return Double.parseDouble(answerValue);
            case Date:
            case DateTime:
                return (answerValue.trim().equals("")) ? null : toISODateFormat(answerValue);
            default:
                return answerValue;
        }
    }

    private String toISODateFormat(String dateStr) {
        DateTimeFormatter outputFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        DateTimeFormatter parseFmt = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd[ HH:mm:ss]")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();
        TemporalAccessor parsed = parseFmt.parseBest(dateStr, ZonedDateTime::from, java.time.LocalDate::from);
        ZonedDateTime dt = null;
        if (parsed instanceof ZonedDateTime) {
            dt = (ZonedDateTime) parsed;
        } else if (parsed instanceof java.time.LocalDate) {
            dt = ((java.time.LocalDate) parsed).atStartOfDay(ZoneId.systemDefault());
        }
        return dt.format(outputFmt);
    }
}
