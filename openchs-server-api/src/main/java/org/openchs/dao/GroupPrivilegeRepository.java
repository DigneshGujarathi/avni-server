package org.openchs.dao;

import org.openchs.domain.GroupPrivilege;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "groupPrivilege", path = "groupPrivilege", exported = false)
@PreAuthorize("hasAnyAuthority('user','admin','organisation_admin')")
public interface GroupPrivilegeRepository extends ReferenceDataRepository<GroupPrivilege>, FindByLastModifiedDateTime<GroupPrivilege> {
    default GroupPrivilege findByName(String name) {
        throw new UnsupportedOperationException("No field 'name' in GroupPrivilege.");
    }

    default GroupPrivilege findByNameIgnoreCase(String name) {
        throw new UnsupportedOperationException("No field 'name' in GroupPrivilege.");
    }
}
