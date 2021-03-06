package org.openchs.dao.individualRelationship;

import org.openchs.dao.FindByLastModifiedDateTime;
import org.openchs.dao.ReferenceDataRepository;
import org.openchs.domain.individualRelationship.IndividualRelationGenderMapping;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "individualRelationGenderMapping", path = "individualRelationGenderMapping")
public interface IndividualRelationGenderMappingRepository extends ReferenceDataRepository<IndividualRelationGenderMapping>, FindByLastModifiedDateTime<IndividualRelationGenderMapping> {
    default IndividualRelationGenderMapping findByName(String name) {
        throw new UnsupportedOperationException("No field 'name' in IndividualRelationGenderMapping");
    }

    default IndividualRelationGenderMapping findByNameIgnoreCase(String name) {
        throw new UnsupportedOperationException("No field 'name' in IndividualRelationGenderMapping");
    }
}