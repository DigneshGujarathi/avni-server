package org.openchs.web.request.webapp;

import org.joda.time.DateTime;
import org.openchs.domain.OperationalSubjectType;
import org.springframework.hateoas.core.Relation;

/**
 * This class represents a combined entity representing one to one mapping of SubjectType and OperationalSubjectType.
 */
@Relation(collectionRelation = "subjectType")
public class SubjectTypeContractWeb {
    private String name;
    private Long id;
    private Long organisationId;
    private Long subjectTypeOrganisationId;
    private boolean voided;
    private boolean group;
    private String createdBy;
    private String lastModifiedBy;
    private DateTime createdDateTime;
    private DateTime lastModifiedDateTime;

    public static SubjectTypeContractWeb fromOperationalSubjectType(OperationalSubjectType operationalSubjectType) {
        SubjectTypeContractWeb contract = new SubjectTypeContractWeb();
        contract.setId(operationalSubjectType.getId());
        contract.setName(operationalSubjectType.getName());
        contract.setOrganisationId(operationalSubjectType.getOrganisationId());
        contract.setSubjectTypeOrganisationId(operationalSubjectType.getSubjectType().getOrganisationId());
        contract.setVoided(operationalSubjectType.isVoided());
        contract.setCreatedBy(operationalSubjectType.getAudit().getCreatedBy().getUsername());
        contract.setLastModifiedBy(operationalSubjectType.getAudit().getLastModifiedBy().getUsername());
        contract.setCreatedDateTime(operationalSubjectType.getAudit().getCreatedDateTime());
        contract.setModifiedDateTime(operationalSubjectType.getAudit().getLastModifiedDateTime());
        contract.setGroup(operationalSubjectType.isGroup());
        return contract;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
    }

    public boolean isVoided() {
        return voided;
    }

    public void setVoided(boolean voided) {
        this.voided = voided;
    }

    public Long getSubjectTypeOrganisationId() {
        return subjectTypeOrganisationId;
    }

    public void setSubjectTypeOrganisationId(Long subjectTypeOrganisationId) {
        this.subjectTypeOrganisationId = subjectTypeOrganisationId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String username) {
        this.createdBy = username;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String username) {
        this.lastModifiedBy = username;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createDateTime) {
        this.createdDateTime = createDateTime;
    }

    public DateTime getModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setModifiedDateTime(DateTime lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }
}
