package org.openchs.dao;

import org.openchs.domain.Concept;
import org.openchs.domain.ConceptAnswer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(collectionResourceRel = "conceptAnswer", path = "conceptAnswer")
public interface ConceptAnswerRepository extends ReferenceDataRepository<ConceptAnswer>, FindByLastModifiedDateTime<ConceptAnswer> {
    ConceptAnswer findByConceptAndAnswerConcept(Concept concept, Concept answerConcept);

    default ConceptAnswer findByName(String name) {
        throw new UnsupportedOperationException("No field 'name' in ConceptAnswer");
    }

    default ConceptAnswer findByNameIgnoreCase(String name) {
        throw new UnsupportedOperationException("No field 'name' in ConceptAnswer");
    }

    @Query("select a.name from ConceptAnswer ca join ca.answerConcept a where a.isVoided = false and ca.isVoided = false")
    List<String> getAllConceptNames();

    @Query("select c.name from ConceptAnswer ca join ca.concept c where c.isVoided = false and ca.isVoided = false")
    List<String> getAllNames();
}