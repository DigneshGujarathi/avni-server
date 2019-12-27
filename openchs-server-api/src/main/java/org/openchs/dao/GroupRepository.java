package org.openchs.dao;

import org.openchs.domain.Group;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "group", path = "group", exported = false)
@PreAuthorize("hasAnyAuthority('user','admin','organisation_admin')")
public interface GroupRepository extends ReferenceDataRepository<Group>, FindByLastModifiedDateTime<Group> {

}
