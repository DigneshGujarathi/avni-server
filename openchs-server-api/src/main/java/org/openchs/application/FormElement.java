package org.openchs.application;

import org.hibernate.annotations.Type;
import org.openchs.domain.Concept;
import org.openchs.domain.Organisation;
import org.openchs.domain.OrganisationAwareEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "form_element")
public class FormElement extends OrganisationAwareEntity {
    @NotNull
    private String name;

    @NotNull
    private Double displayOrder;

    @NotNull
    private boolean isMandatory;

    @Column
    @Type(type = "keyValues")
    private KeyValues keyValues;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "concept_id")
    private Concept concept;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_element_group_id")
    private FormElementGroup formElementGroup;

    @Column(name = "type", nullable = true)
    private String type;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "non_applicable_form_element",
            joinColumns = @JoinColumn(name = "form_element_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "organisation_id", referencedColumnName = "id"))
    private Set<NonApplicableFormElement> nonApplicableFormElements = null;

    @Embedded
    private Format validFormat;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Double displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    public KeyValues getKeyValues() {
        return keyValues;
    }

    public void setKeyValues(KeyValues newKeyValues) {
        this.keyValues = newKeyValues;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        isMandatory = mandatory;
    }

    public FormElementGroup getFormElementGroup() {
        return formElementGroup;
    }

    public void setFormElementGroup(FormElementGroup formElementGroup) {
        this.formElementGroup = formElementGroup;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Format getValidFormat() {
        return validFormat;
    }

    public void setValidFormat(Format validFormat) {
        this.validFormat = validFormat;
    }

    public boolean isSingleSelect() {
        return "SingleSelect".equals(this.type);
    }

    public void addNonApplicableOrganisations(Organisation organisation) {
        if (nonApplicableFormElements == null) {
            nonApplicableFormElements = new HashSet<>();
        }
        this.nonApplicableFormElements.add(organisation);
    }

    public boolean isNonApplicableForOrganisation(Organisation organisation) {
      return this.nonApplicableFormElements.contains(organisation);
    }
}