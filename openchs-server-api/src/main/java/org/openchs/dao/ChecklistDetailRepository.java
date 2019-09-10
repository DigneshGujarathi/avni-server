package org.openchs.dao;

import org.openchs.domain.ChecklistDetail;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(collectionResourceRel = "checklistDetail", path = "checklistDetail")
public interface ChecklistDetailRepository extends ImplReferenceDataRepository<ChecklistDetail>, FindByLastModifiedDateTime<ChecklistDetail> {
    List<ChecklistDetail> findByOrganisationId(Long organisationId);
}
