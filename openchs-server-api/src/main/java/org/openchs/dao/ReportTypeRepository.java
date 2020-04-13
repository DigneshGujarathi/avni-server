package org.openchs.dao;

import org.openchs.domain.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.openchs.domain.ReportType.SubjectTypeProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

// @Repository
// @RepositoryRestResource(collectionResourceRel = "ReportType", path = "ReportType")
// public interface ReportTypeRepository extends ReferenceDataRepository<ReportType>, FindByLastModifiedDateTime<ReportType> {

//     @Query("select rt from ReportType rt where name = 'Individual'")
//     ReportType individualSubjectType();

//     // @Query("select st from ReportType st where st.operationalSubjectTypes is not empty and st.isVoided = false")
//     // List<SubjectTypeProjection> findAllOperational();
// }


@Repository
public interface ReportTypeRepository extends JpaRepository<ReportType, Long>{
// List<ReportType> findName(String name);
List<ReportType> findAll();
}