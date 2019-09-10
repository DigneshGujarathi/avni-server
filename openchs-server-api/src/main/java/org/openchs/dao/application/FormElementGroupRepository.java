package org.openchs.dao.application;

import org.openchs.application.FormElementGroup;
import org.openchs.dao.FindByLastModifiedDateTime;
import org.openchs.dao.ReferenceDataRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(collectionResourceRel = "formElementGroup", path = "formElementGroup")
public interface FormElementGroupRepository extends ReferenceDataRepository<FormElementGroup>, FindByLastModifiedDateTime<FormElementGroup> {
    List<FormElementGroup> findByOrganisationId(Long organisationId);
}