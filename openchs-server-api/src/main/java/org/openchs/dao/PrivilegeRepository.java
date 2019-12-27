package org.openchs.dao;

import org.joda.time.DateTime;
import org.openchs.domain.Privilege;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "privilege", path = "privilege", exported = false)
@PreAuthorize("hasAnyAuthority('user','admin','organisation_admin')")
public interface PrivilegeRepository extends PagingAndSortingRepository<Privilege, Long> {

    @PreAuthorize("hasAnyAuthority('admin' ,'organisation_admin')")
    <S extends Privilege> S save(S entity);

    @RestResource(path = "lastModified", rel = "lastModified")
    Page<Privilege> findByLastModifiedDateTimeIsBetweenOrderByLastModifiedDateTimeAscIdAsc(
            @Param("lastModifiedDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime lastModifiedDateTime,
            @Param("now") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime now,
            Pageable pageable);
}
