package org.openchs.dao;

import org.openchs.domain.Program;
import org.openchs.domain.Program.ProgramProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(collectionResourceRel = "program", path = "program")
public interface ProgramRepository extends ReferenceDataRepository<Program>, FindByLastModifiedDateTime<Program> {
    @Query("select o from Program o where o.operationalPrograms is not empty")
    List<ProgramProjection> findAllOperational();

    List<Program> findByOrganisationId(Long organisationId);
}