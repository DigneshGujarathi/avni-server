package org.openchs.dao;

import org.openchs.domain.GroupRole;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "groupRole", path = "groupRole")
@PreAuthorize("hasAnyAuthority('user','admin','organisation_admin')")
public interface GroupRoleRepository extends ReferenceDataRepository<GroupRole>, FindByLastModifiedDateTime<GroupRole> {

    default GroupRole findByNameIgnoreCase(String name) {
        throw new UnsupportedOperationException("No field 'name' in GroupRole");
    }

    default GroupRole findByName(String name) {
        throw new UnsupportedOperationException("No field 'name' in GroupRole");
    }

    GroupRole findByRole(String role);
}
