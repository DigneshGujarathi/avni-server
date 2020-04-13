package org.openchs.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.openchs.application.projections.BaseProjection;
import org.springframework.data.rest.core.config.Projection;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "report_type")
// @JsonIgnoreProperties({"operationalSubjectTypes"})
public class ReportType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "desc")
    private String description;

    // @OneToMany(fetch = FetchType.LAZY, mappedBy = "ReportType")
    // private Set<OperationalSubjectType> operationalSubjectTypes = new HashSet<>();

    public ReportType(String string, String string2) {
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // public Set<OperationalSubjectType> getOperationalSubjectTypes() {
    //     return operationalSubjectTypes;
    // }

    // public void setOperationalSubjectTypes(Set<OperationalSubjectType> operationalSubjectTypes) {
    //     this.operationalSubjectTypes = operationalSubjectTypes;
    // }

    // @JsonIgnore
    // public String getOperationalSubjectTypeName() {
    //     return operationalSubjectTypes.stream()
    //             .map(OperationalSubjectType::getName)
    //             .filter(Objects::nonNull)
    //             .findFirst()
    //             .orElse(null);
    // }

    @Projection(name = "SubjectTypeProjection", types = {ReportType.class})
    public interface SubjectTypeProjection extends BaseProjection {
        String getName();

        String getOperationalSubjectTypeName();
    }
}
