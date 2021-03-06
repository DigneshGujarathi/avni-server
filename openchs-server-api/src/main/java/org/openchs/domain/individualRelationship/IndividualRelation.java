package org.openchs.domain.individualRelationship;

import org.openchs.domain.OrganisationAwareEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "individual_relation")
public class IndividualRelation extends OrganisationAwareEntity {
    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public static IndividualRelation create(String name) {
        IndividualRelation relation = new IndividualRelation();
        relation.name = name;
        return relation;
    }
}