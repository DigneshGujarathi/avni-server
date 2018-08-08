select *
from organisation;

-- ITEMS FOR TRANSLATION
select distinct name
from
  (
    select form_element_group_name as name
    from all_form_element_groups
    where organisation_id = :organisation_id
    union
    select form_element_name as name
    from all_form_elements
    where organisation_id = :organisation_id
    union
    select concept_name as name
    from all_concepts
    where organisation_id = :organisation_id
    union
    select answer_concept_name as name
    from all_concept_answers
    where organisation_id = :organisation_id
    union
    select operational_encounter_type_name as name
    from all_operational_encounter_types
    where organisation_id = :organisation_id
    union
    select encounter_type_name as name
    from all_encounter_types
    where organisation_id = :organisation_id
    union
    select operational_program_name as name
    from all_operational_programs
    where organisation_id = :organisation_id
    union
    select program_name as name
    from all_programs
    where organisation_id = :organisation_id

    union

    select concept.name as name
    from concept
    where
      concept.id not in (select concept.id
                         from concept
                           inner join form_element element2 on concept.id = element2.concept_id
                         where concept.organisation_id = :organisation_id
                         union
                         select concept.id
                         from concept
                           inner join concept_answer ca on concept.id = ca.answer_concept_id
                         where concept.organisation_id = :organisation_id) and
      concept.organisation_id = :organisation_id and not concept.is_voided

    union

    select concept.name concept_name
    from concept
    where concept.id not in (select concept.id
                             from concept
                               inner join form_element element2 on concept.id = element2.concept_id
                             where concept.organisation_id = 1
                             union
                             select concept.id
                             from concept
                               inner join concept_answer ca on concept.id = ca.answer_concept_id
                             where concept.organisation_id = 1
    ) and concept.organisation_id = 1 and not concept.is_voided
  ) as X order by name;

-- VIEW CONCEPT WITH ANSWERS
select
  concept.name,
  a.uuid  AS "Concept Answer UUID",
  c2.uuid as "Answer UUID",
  c2.name as "Answer",
  a.answer_order,
  concept.organisation_id concept_organisation_id,
  a.organisation_id answer_organisation_id,
  c2.organisation_id answer_concept_organisation_id
from concept
  inner join concept_answer a on concept.id = a.concept_id
  inner join concept c2 on a.answer_concept_id = c2.id
where concept.name = :concept_name
order by a.answer_order;


-- ADDRESS LEVELS (Required for translations, do not change this one)
select
  distinct a.title
from address_level a
where a.organisation_id = :org_id
order by a.title;

-- CATCHMENT TYPE (Required for translations, do not change this one)
select distinct type
from catchment
where organisation_id = :org_id;

-- Encounter types
select
  et.name  "EncounterType",
  oet.name "OrgEncounterType"
from operational_encounter_type oet
  inner join encounter_type et on oet.encounter_type_id = et.id;

-- Cancel Forms
select
  f2.id               as FormMappingId,
  program.name        as Program,
  encounter_type.name as EncounterType
from form f
  inner join form_mapping f2 on f.id = f2.form_id
  inner join encounter_type on encounter_type.id = f2.observations_type_entity_id
  inner join program on program.id = f2.entity_id
where f2.organisation_id = 2 and f.form_type = 'ProgramEncounterCancellation'
order by
  Program;