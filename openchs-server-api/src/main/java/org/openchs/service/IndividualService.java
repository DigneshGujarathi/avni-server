package org.openchs.service;

import org.openchs.dao.ConceptRepository;
import org.openchs.dao.IndividualRepository;
import org.openchs.domain.*;
import org.openchs.web.request.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;


@Service
public class IndividualService {
    private final Logger logger;
    private final IndividualRepository individualRepository;
    private final ConceptRepository conceptRepository;
    private final ProjectionFactory projectionFactory;

    @Autowired
    public IndividualService(ConceptRepository conceptRepository, IndividualRepository individualRepository, ProjectionFactory projectionFactory) {
        this.projectionFactory = projectionFactory;
        logger = LoggerFactory.getLogger(this.getClass());
        this.conceptRepository = conceptRepository;
        this.individualRepository = individualRepository;
    }

    public  IndividualContract getSubjectEncounters(String individualUuid){
        Individual individual = individualRepository.findByUuid(individualUuid);
        if (!Objects.nonNull(individual)) {
            return null;
        }
        Set<EncounterContract> encountersContractList = constructEncounters(individual.getEncounters());
        IndividualContract individualContract = new IndividualContract();
        individualContract.setEncounters(encountersContractList);
        return individualContract;
    }

    public  IndividualContract getSubjectProgramEnrollment(String individualUuid){
        Individual individual = individualRepository.findByUuid(individualUuid);
        if (!Objects.nonNull(individual)) {
            return null;
        }
        List<EnrolmentContract> enrolmentContractList = constructEnrolmentsMetadata(individual);
        IndividualContract individualContract = new IndividualContract();
        individualContract.setUuid(individual.getUuid());
        individualContract.setEnrolments(enrolmentContractList);
        return individualContract;
    }

    public IndividualContract getSubjectInfo(String individualUuid) {
        Individual individual = individualRepository.findByUuid(individualUuid);
        IndividualContract individualContract = new IndividualContract();
        if (!Objects.nonNull(individual)) {
            return null;
        }

        List<ObservationContract> observationContractsList = constructObservations(individual.getObservations());
        List<RelationshipContract> relationshipContractList = constructRelationships(individual);
        List<EnrolmentContract> enrolmentContractList = constructEnrolments(individual);
        individualContract.setObservations(observationContractsList);
        individualContract.setRelationships(relationshipContractList);
        individualContract.setEnrolments(enrolmentContractList);
        individualContract.setUuid(individual.getUuid());
        individualContract.setFirstName(individual.getFirstName());
        individualContract.setLastName(individual.getLastName());
        individualContract.setDateOfBirth(individual.getDateOfBirth());
        individualContract.setGender(individual.getGender().getName());
        individualContract.setAddressLevel(individual.getAddressLevel().getTitle());
        individualContract.setRegistrationDate(individual.getRegistrationDate());
        individualContract.setFullAddress(individual.getAddressLevel().getTitleLineage());
        return individualContract;
    }

    public List<EnrolmentContract> constructEnrolmentsMetadata(Individual individual) {
        return individual.getProgramEnrolments().stream().filter(x -> !x.isVoided()).map(programEnrolment -> {
            EnrolmentContract enrolmentContract = new EnrolmentContract();
            enrolmentContract.setUuid(programEnrolment.getUuid());
            enrolmentContract.setOperationalProgramName(programEnrolment.getProgram().getName());
            enrolmentContract.setEnrolmentDateTime(programEnrolment.getEnrolmentDateTime());
            enrolmentContract.setProgramExitDateTime(programEnrolment.getProgramExitDateTime());
            enrolmentContract.setProgramEncounters(constructProgramEncounters(programEnrolment.getProgramEncounters()));
            List<ObservationContract> observationContractsList = constructObservations(programEnrolment.getObservations());
            enrolmentContract.setObservations(observationContractsList);
            if (programEnrolment.getProgramExitObservations() != null) {
                enrolmentContract.setExitObservations(constructObservations(programEnrolment.getProgramExitObservations()));
            }
            return enrolmentContract;
        }).collect(Collectors.toList());
    }

    public Set<EncounterContract> constructEncounters(Set<Encounter> encounters) {
        return encounters.stream().map(programEncounter -> {
            EncounterContract encountersContract = new EncounterContract();
            encountersContract.setUuid(programEncounter.getUuid());
            encountersContract.setName(programEncounter.getName());
            encountersContract.setOperationalEncounterTypeName(programEncounter.getEncounterType().getOperationalEncounterTypeName());
            encountersContract.setEncounterDateTime(programEncounter.getEncounterDateTime());
            encountersContract.setEarliestVisitDateTime(programEncounter.getEarliestVisitDateTime());
            encountersContract.setMaxVisitDateTime(programEncounter.getMaxVisitDateTime());
            return encountersContract;
        }).collect(Collectors.toSet());
    }


    public Set <ProgramEncountersContract> constructProgramEncounters(Set<ProgramEncounter> programEncounters) {
        return programEncounters.stream().map(programEncounter -> {
            ProgramEncountersContract programEncountersContract = new ProgramEncountersContract();
            programEncountersContract.setUuid(programEncounter.getUuid());
            programEncountersContract.setName(programEncounter.getName());
            programEncountersContract.setOperationalEncounterTypeName(programEncounter.getEncounterType().getName());
            programEncountersContract.setEncounterDateTime(programEncounter.getEncounterDateTime());
            programEncountersContract.setCancelDateTime(programEncounter.getCancelDateTime());
            programEncountersContract.setEarliestVisitDateTime(programEncounter.getEarliestVisitDateTime());
            programEncountersContract.setMaxVisitDateTime(programEncounter.getMaxVisitDateTime());
            return  programEncountersContract;
        }).collect(Collectors.toSet());
    }

    public List<EnrolmentContract> constructEnrolments(Individual individual) {

        return individual.getProgramEnrolments().stream().map(programEnrolment -> {
            EnrolmentContract enrolmentContract = new EnrolmentContract();
            enrolmentContract.setUuid(programEnrolment.getUuid());
            enrolmentContract.setOperationalProgramName(programEnrolment.getProgram().getName());
            enrolmentContract.setEnrolmentDateTime(programEnrolment.getEnrolmentDateTime());
            enrolmentContract.setProgramExitDateTime(programEnrolment.getProgramExitDateTime());
            return enrolmentContract;
        }).collect(Collectors.toList());
    }

    public List<RelationshipContract> constructRelationships(Individual individual) {
        return individual.getRelationships().stream().map(individualRelationship -> {
            RelationshipContract relationshipContract = new RelationshipContract();
            relationshipContract.setUuid(individualRelationship.getUuid());
            relationshipContract.setIndividualBUuid(individualRelationship.getIndividualB().getUuid());
            relationshipContract.setIndividualBIsToARelation(individualRelationship.getRelationship().getIndividualBIsToA().getName());
            relationshipContract.setRelationshipTypeUuid(individualRelationship.getRelationship().getUuid());
            relationshipContract.setEnterDateTime(individualRelationship.getEnterDateTime());
            relationshipContract.setExitDateTime(individualRelationship.getExitDateTime());
            relationshipContract.setFirstName(individualRelationship.getIndividualB().getFirstName());
            relationshipContract.setLastName(individualRelationship.getIndividualB().getLastName());
            relationshipContract.setDateOfBirth(individualRelationship.getIndividualB().getDateOfBirth());
            if (individualRelationship.getExitObservations() != null) {
                relationshipContract.setExitObservations(constructObservations(individualRelationship.getExitObservations()));
            }
            return relationshipContract;
        }).collect(Collectors.toList());
    }

    public List<ObservationContract> constructObservations(@NotNull ObservationCollection observationCollection) {
        return observationCollection.entrySet().stream().map(entry -> {
            ObservationContract observationContract = new ObservationContract();
            Concept questionConcept = conceptRepository.findByUuid(entry.getKey());
            ConceptContract conceptContract = ConceptContract.create(questionConcept);
            observationContract.setConcept(conceptContract);
            Object value = entry.getValue();
            if (questionConcept.getDataType().equalsIgnoreCase(ConceptDataType.Coded.toString())) {
                List<String> answers = value instanceof List ? (List<String>) value : singletonList(value.toString());
                List<ConceptContract> answerConceptList = questionConcept.getConceptAnswers().stream()
                        .filter(it ->
                                answers.contains(it.getAnswerConcept().getUuid())
                        ).map(it -> {
                            ConceptContract cc = ConceptContract.create(it.getAnswerConcept());
                            cc.setAbnormal(it.isAbnormal());
                            return cc;
                        }).collect(Collectors.toList());
                observationContract.setValue(answerConceptList);
            } else {
                observationContract.setValue(value);
            }
            return observationContract;
        }).collect(Collectors.toList());
    }
}
