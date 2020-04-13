package org.openchs.dao;

import org.openchs.domain.ReportAdminForm;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.openchs.domain.ReportType.SubjectTypeProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


@Repository
public interface ReportAdminFormRepository extends JpaRepository<ReportAdminForm, Long> {
}