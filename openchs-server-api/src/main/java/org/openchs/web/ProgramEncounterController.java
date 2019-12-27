package org.openchs.web;

import com.bugsnag.Bugsnag;
import org.joda.time.DateTime;
import org.openchs.dao.*;
import org.openchs.domain.*;
import org.openchs.geo.Point;
import org.openchs.service.ObservationService;
import org.openchs.service.UserService;
import org.openchs.web.request.PointRequest;
import org.openchs.web.request.ProgramEncounterRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@RestController
public class ProgramEncounterController extends AbstractController<ProgramEncounter> implements RestControllerResourceProcessor<ProgramEncounter>, OperatingIndividualScopeAwareController<ProgramEncounter>, OperatingIndividualScopeAwareFilterController<ProgramEncounter> {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(IndividualController.class);
    @Autowired
    Bugsnag bugsnag;
    private EncounterTypeRepository encounterTypeRepository;
    private ProgramEncounterRepository programEncounterRepository;
    private ProgramEnrolmentRepository programEnrolmentRepository;
    private ObservationService observationService;
    private UserService userService;

    @Autowired
    public ProgramEncounterController(EncounterTypeRepository encounterTypeRepository, ProgramEncounterRepository programEncounterRepository, ProgramEnrolmentRepository programEnrolmentRepository, ObservationService observationService, UserService userService) {
        this.encounterTypeRepository = encounterTypeRepository;
        this.programEncounterRepository = programEncounterRepository;
        this.programEnrolmentRepository = programEnrolmentRepository;
        this.observationService = observationService;
        this.userService = userService;
    }

    private void checkForSchedulingCompleteConstraintViolation(ProgramEncounterRequest request) {
        if ((request.getEarliestVisitDateTime() != null || request.getMaxVisitDateTime() != null)
                && (request.getEarliestVisitDateTime() == null || request.getMaxVisitDateTime() == null)
        ) {
            //violating constraint so notify bugsnag
            bugsnag.notify(new Exception(String.format("ProgramEncounter violating scheduling constraint uuid %s earliest %s max %s", request.getUuid(), request.getEarliestVisitDateTime(), request.getMaxVisitDateTime())));
        }

    }

    @RequestMapping(value = "/programEncounters", method = RequestMethod.POST)
    @Transactional
    @PreAuthorize(value = "hasAnyAuthority('user')")
    public void save(@RequestBody ProgramEncounterRequest request) {
        logger.info(String.format("Saving programEncounter with uuid %s", request.getUuid()));
        checkForSchedulingCompleteConstraintViolation(request);
        EncounterType encounterType = encounterTypeRepository.findByUuidOrName(request.getEncounterType(), request.getEncounterTypeUUID());
        ProgramEncounter encounter = newOrExistingEntity(programEncounterRepository, request, new ProgramEncounter());
        //Planned visit can not overwrite completed encounter
        if (encounter.isCompleted() && request.isPlanned())
            return;

        encounter.setEncounterDateTime(request.getEncounterDateTime());
        encounter.setProgramEnrolment(programEnrolmentRepository.findByUuid(request.getProgramEnrolmentUUID()));
        encounter.setEncounterType(encounterType);
        encounter.setObservations(observationService.createObservations(request.getObservations()));
        encounter.setName(request.getName());
        encounter.setEarliestVisitDateTime(request.getEarliestVisitDateTime());
        encounter.setMaxVisitDateTime(request.getMaxVisitDateTime());
        encounter.setCancelDateTime(request.getCancelDateTime());
        encounter.setCancelObservations(observationService.createObservations(request.getCancelObservations()));
        PointRequest encounterLocation = request.getEncounterLocation();
        if (encounterLocation != null)
            encounter.setEncounterLocation(new Point(encounterLocation.getX(), encounterLocation.getY()));
        PointRequest cancelLocation = request.getCancelLocation();
        if (cancelLocation != null)
            encounter.setCancelLocation(new Point(cancelLocation.getX(), cancelLocation.getY()));

        programEncounterRepository.save(encounter);
        logger.info(String.format("Saved programEncounter with uuid %s", request.getUuid()));
    }

    @RequestMapping(value = "/programEncounter/search/byIndividualsOfCatchmentAndLastModified", method = RequestMethod.GET)
    @PreAuthorize(value = "hasAnyAuthority('user')")
    @Deprecated()
    public PagedResources<Resource<ProgramEncounter>> getByIndividualsOfCatchmentAndLastModified(
            @RequestParam("catchmentId") long catchmentId,
            @RequestParam("lastModifiedDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime lastModifiedDateTime,
            @RequestParam("now") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime now,
            Pageable pageable) {
        return wrap(programEncounterRepository.findByProgramEnrolmentIndividualAddressLevelVirtualCatchmentsIdAndAuditLastModifiedDateTimeIsBetweenOrderByAuditLastModifiedDateTimeAscIdAsc(catchmentId, lastModifiedDateTime, now, pageable));
    }

    @RequestMapping(value = "/programEncounter/search/lastModified", method = RequestMethod.GET)
    @PreAuthorize(value = "hasAnyAuthority('user')")
    public PagedResources<Resource<ProgramEncounter>> getByLastModified(
            @RequestParam("lastModifiedDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime lastModifiedDateTime,
            @RequestParam("now") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime now,
            Pageable pageable) {
        return wrap(programEncounterRepository.findByAuditLastModifiedDateTimeIsBetweenOrderByAuditLastModifiedDateTimeAscIdAsc(lastModifiedDateTime, now, pageable));
    }

    @RequestMapping(value = "/programEncounter", method = RequestMethod.GET)
    @PreAuthorize(value = "hasAnyAuthority('user')")
    public PagedResources<Resource<ProgramEncounter>> getProgramEncountersByOperatingIndividualScope(
            @RequestParam("lastModifiedDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime lastModifiedDateTime,
            @RequestParam("now") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime now,
            @RequestParam(value = "encounterTypeUuid", required = false) List<String> encounterTypeUuid,
            Pageable pageable) {
        if (encounterTypeUuid == null) {
            return wrap(getCHSEntitiesForUserByLastModifiedDateTime(userService.getCurrentUser(), lastModifiedDateTime, now, pageable));
        } else {
            return encounterTypeUuid.isEmpty() ? wrap(new PageImpl<>(Collections.emptyList())) :
                    wrap(getCHSEntitiesForUserByLastModifiedDateTimeAndFilterByType(userService.getCurrentUser(), lastModifiedDateTime, now, encounterTypeUuid, pageable));
        }
    }

    @Override
    public Resource<ProgramEncounter> process(Resource<ProgramEncounter> resource) {
        ProgramEncounter programEncounter = resource.getContent();
        resource.removeLinks();
        resource.add(new Link(programEncounter.getEncounterType().getUuid(), "encounterTypeUUID"));
        resource.add(new Link(programEncounter.getProgramEnrolment().getUuid(), "programEnrolmentUUID"));
        return resource;
    }

    @Override
    public OperatingIndividualScopeAwareRepository<ProgramEncounter> resourceRepository() {
        return programEncounterRepository;
    }

    @Override
    public OperatingIndividualScopeAwareRepositoryWithTypeFilter<ProgramEncounter> repository() {
        return programEncounterRepository;
    }
}