DELETE FROM form_element;
DELETE FROM form_element_group;
DELETE FROM form_mapping;
DELETE FROM form;
DELETE FROM encounter;
DELETE FROM program_encounter;
DELETE FROM program_enrolment;
DELETE FROM individual;
DELETE FROM program;
DELETE FROM encounter_type;
DELETE FROM program_outcome;
DELETE FROM concept_answer;
DELETE FROM concept;
DELETE FROM gender;
DELETE FROM catchment_address_mapping;
DELETE FROM address_level;
DELETE FROM catchment;
DELETE FROM users;
DELETE FROM organisation;

INSERT INTO organisation (id, name, db_user, uuid)
VALUES (1, 'OpenCHS', 'openchs', '3539a906-dfae-4ec3-8fbb-1b08f35c3884');
INSERT INTO organisation (id, name, db_user, uuid)
VALUES (2, 'demo', 'demo', 'ae0e4ac4-681d-45f2-8bdd-2b09a5a1a6e5');

INSERT INTO users (id, name, uuid, created_date_time, last_modified_date_time, version, organisation_id) VALUES (1, 'openchs', '5fed2907-df3a-4867-aef5-c87f4c78a31a', current_timestamp, current_timestamp, 1, 1);

INSERT INTO catchment(id, name, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time, organisation_id, type)
VALUES(1, 'CatchmentX', '1722e2d4-3ef3-4ea0-a4c8-72090504ec7f', 0, 1, 1, current_timestamp, current_timestamp, 1, 'TypeX');

INSERT INTO gender (id, name, uuid, created_date_time, last_modified_date_time, created_by_id, last_modified_by_id, version) VALUES (1, 'Female', 'ad7d1d14-54fd-45a2-86b7-ea329b744484', current_timestamp, current_timestamp, 1, 1, 1);
INSERT INTO gender (id, name, uuid, created_date_time, last_modified_date_time, created_by_id, last_modified_by_id, version) VALUES (2, 'Male', '840de9fb-e565-4d7d-b751-90335ba20490', current_timestamp, current_timestamp, 1, 1, 1);
INSERT INTO gender (id, name, uuid, created_date_time, last_modified_date_time, created_by_id, last_modified_by_id, version) VALUES (3, 'Other', '188ad77e-fe46-4328-b0e2-98f3a05c554c', current_timestamp, current_timestamp, 1, 1, 1);

ALTER SEQUENCE concept_id_seq RESTART WITH 1;
INSERT INTO concept (name, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Temperature', 'numeric', '95c4b174-6ce6-4d9a-b223-1f9000b60006', 1, 1, 1, current_timestamp, current_timestamp);

/* muliselect */
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Complaint', 'multiselect', '9daa0b8a-985a-464d-a5ab-8a4f90e8a26b', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Fever', 'N/A', 'd2e35080-ec9b-46f6-bf8c-7087fcf0ecfd', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Vomiting', 'N/A', '627c8cbc-a03d-4e3f-9e4d-7059e60f3225', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Chloroquine Resistant', 'N/A', '9d9e3cab-3a45-4f85-bc19-2d2d736bb17a', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Cough', 'N/A', '002173d4-2f59-4a6c-b315-049ecdb7cf68', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Bodyache', 'N/A', '6c9cd213-0822-42d5-8ef3-47f0da1738f9', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Cold', 'N/A', '7eae07be-a340-4ced-ac8d-c910cf91a672', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Headache', 'N/A', '5f14ae60-1ae4-4d1e-ae10-d312d47e529a', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Acidity', 'N/A', '637c8ae1-f6a8-45f7-bd8f-10964632c05a', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Giddyness', 'N/A', 'f9498304-50a8-4725-8071-04f623ddacf4', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Abdominal Pain', 'N/A', 'db848b92-dda9-4510-988e-a06b71acbaf5', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Diarrhoea', 'N/A', 'e491b590-2f34-4fb7-8b46-7e533a9903f1', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Pregnancy', 'N/A', '74a4ed09-f9a9-4647-8e48-a00432a65c35', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Wound', 'N/A', '18e30591-41e0-4da0-9e78-ee52c6f6c4fe', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Scabies', 'N/A', '57e2e29f-9691-4f2d-a4db-4df4a17a9255', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Ringworm', 'N/A', '122cb9cb-3fdc-48e7-a68f-682c5e744c22', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Boils', 'N/A', 'aee32344-0ea0-4833-9387-2cb21586f1a9', 1, 1, 1, current_timestamp, current_timestamp);

INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (2, 3, 1, '00828291-c2fe-415f-a51e-ba8a02607da0', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (2, 4, 2, '85841889-9676-40e7-a587-9da9d05bb89b', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (2, 5, 3, '3cd01910-bd15-45ff-aec8-392ec11f2357', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (2, 6, 4, '3ec9b9e6-355b-4db1-bb4e-92db6d14edc5', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (2, 7, 5, '6a29ed8e-d5aa-4581-b5da-d650fa1b51ff', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (2, 8, 6, '8cab0a2a-d47f-44f7-9727-f7cf52687b8d', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (2, 9, 7, '5c2a8604-1abf-4091-9023-b7b18db54a60', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (2, 10, 8, '180d05cf-35ea-42ca-a1c0-9345424cf14c', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (2, 11, 9, 'e94fa8a5-1f12-40b9-9b95-075a83c45901', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (2, 12, 10, '2a394f0e-03b0-44cf-9199-29e31da84e3b', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (2, 13, 11, '539d046c-3381-4068-a853-1915f5270d78', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (2, 14, 12, '2a0e7e1c-620a-4ca0-b46e-21db36a5ff4d', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (2, 15, 13, '7c0fd5bd-c8ef-4f09-a5df-dc0bf4aaba67', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (2, 16, 14, '81bf9fb9-a19b-415e-a9fe-fbef9d17ada0', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (2, 17, 15, 'eb592ded-2467-4d32-a48d-deb4916cda31', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (2, 18, 16, 'a9ca3096-6f4d-4af2-8b91-9cf87f5d4d13', 1, 1, 1, current_timestamp, current_timestamp);

/* single select */
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Paracheck', 'singleselect', '405f25bb-4553-4b7c-b6bc-a44082ef576f', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Negative', 'N/A', '782d6227-b815-4fed-aef1-52354e1dcf77', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Positive for PV', 'N/A', '3bdd6dfe-1113-4930-90df-a20cff9ea0f4', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Positive for PF', 'N/A', '6c71b496-7df2-4ee2-afa7-248d622b9760', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept (NAME, data_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Positive for PV and PF', 'N/A', '0d6f3dbd-c758-4b03-aa45-fd40699d6138', 1, 1, 1, current_timestamp, current_timestamp);

INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (19, 20, 1, '2e91c2ea-ca5f-4674-b98b-9c0f8cb48069', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (19, 21, 2, '0a4d1804-7404-4f29-be18-eaff80c3d503', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (19, 22, 3, '676c358d-0d21-46cf-bb38-c3a5bfb5ead1', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO concept_answer (concept_id, answer_concept_id, answer_order, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (19, 23, 4, '7dc1c6db-419b-483d-8b47-0d2b89d9919b', 1, 1, 1, current_timestamp, current_timestamp);


ALTER SEQUENCE program_outcome_id_seq RESTART WITH 1;
-- insert

ALTER SEQUENCE encounter_type_id_seq RESTART WITH 1;
INSERT INTO encounter_type (name, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Sample Encounter Type', '3a1535d0-81fd-48fc-85b5-dc9da81064a3', 1, 1, 1, current_timestamp, current_timestamp);

ALTER SEQUENCE program_id_seq RESTART WITH 1;
INSERT INTO program (name, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Diabetes', 'db62a322-0ec2-4bb0-ac24-296dc7216c9a', 1, 1, 1, current_timestamp, current_timestamp);

ALTER SEQUENCE address_level_id_seq RESTART WITH 1;
INSERT INTO address_level (title, level, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Nijhma', 1, 'ae35fe6d-910e-47bd-a0c7-0c10182a4085', 1, 1, 1, current_timestamp, current_timestamp);
INSERT INTO address_level (title, level, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Naya Gaon', 1, 'a62d5ff9-4480-44f8-ab9f-9fe12e2e1a91', 1, 1, 1, current_timestamp, current_timestamp);

INSERT INTO catchment_address_mapping(catchment_id, addresslevel_id, organisation_id)
VALUES(1,1,1);

ALTER SEQUENCE individual_id_seq RESTART WITH 1;
INSERT INTO individual (uuid, address_id, version, date_of_birth, date_of_birth_verified, first_name, last_name, gender_id, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('4378dce3-247e-4393-8dd5-032c6eb0a655', 1, 1, current_timestamp, FALSE, 'Prabhu', 'Kumar', 2, 1, 1, current_timestamp, current_timestamp);

ALTER SEQUENCE program_enrolment_id_seq RESTART WITH 1;
INSERT INTO program_enrolment (individual_id, program_id, enrolment_date_time, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (1, 1, current_timestamp, 'ba0a3b91-2d4d-446b-a3ee-d56e7edaf3d3', 1, 1, 1, current_timestamp, current_timestamp);

ALTER SEQUENCE program_encounter_id_seq RESTART WITH 1;
INSERT INTO program_encounter (program_enrolment_id, encounter_type_id, observations, encounter_date_time, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (1, 1, '[
  {
    "conceptUUID": "95c4b174-6ce6-4d9a-b223-1f9000b60006",
    "valuePrimitive": 98.9
  }
]' :: JSONB, current_timestamp, 'f5c3d56c-3d69-41bd-9e6a-52963adb6e76', 1, 1, 1, current_timestamp, current_timestamp);

ALTER SEQUENCE encounter_id_seq RESTART WITH 1;
INSERT INTO encounter (individual_id, encounter_type_id, observations, encounter_date_time, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (1, 1, '
  {
    "conceptUUID": "95c4b174-6ce6-4d9a-b223-1f9000b60006",
    "valuePrimitive": 98.9
  }
' :: JSONB, current_timestamp, '63a7d615-b965-4830-9dd2-e8f533d9a4e9', 1, 1, 1, current_timestamp, current_timestamp);

INSERT INTO program (name, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Nutrition', 'ac8cfbcb-39d2-4fcb-b02f-4ef80335f553', 1, 1, 1, current_timestamp, current_timestamp);

INSERT INTO individual (address_id, date_of_birth, date_of_birth_verified, first_name, last_name, gender_id, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (1, '1950-09-17', FALSE, 'Ramesh', 'Kumar', 2, '8d3d49af-f776-4cca-8413-ee571d9042fd', 1, 1, 1, current_timestamp, current_timestamp);

INSERT INTO program_enrolment (individual_id, program_id, enrolment_date_time, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (2, 1, current_timestamp, '0ae41288-78c5-4ed4-af60-68d4ad2af1d0', 1, 1, 1, current_timestamp, current_timestamp);

INSERT INTO program_enrolment (individual_id, program_id, enrolment_date_time, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (2, 2, current_timestamp, '529aa9ed-46bc-4530-9768-6ec941c0e2e0', 1, 1, 1, current_timestamp, current_timestamp);

INSERT INTO individual (address_id, date_of_birth, date_of_birth_verified, first_name, last_name, gender_id, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (1, '1955-01-05', FALSE, 'Ram', 'Kumari', 1, 'c415ef96-8ff9-4cbb-8407-e7618c90a055', 1, 1, 1, current_timestamp, current_timestamp);

ALTER SEQUENCE form_id_seq RESTART WITH 1;
INSERT INTO form (NAME, form_type, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('encounter_form', 'Encounter', '2c32a184-6d27-4c51-841d-551ca94594a5', 1, 1, 1, current_timestamp, current_timestamp);

ALTER SEQUENCE form_element_group_id_seq RESTART WITH 1;
INSERT INTO form_element_group (NAME, form_id, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('default_group', 1, '4b317705-4372-4405-a628-6c8bb8da8671', 1, 1, 1, current_timestamp, current_timestamp);

INSERT INTO form_element (name, display_order, is_mandatory, concept_id, is_used_in_summary, is_generated, form_element_group_id, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Complaint', 2, TRUE, 2, FALSE, FALSE, 1, '2f256e95-3011-4f42-8ebe-1c1af5e6b8d2', 1, 1, 1, current_timestamp, current_timestamp);

INSERT INTO form_element (name, display_order, is_mandatory, concept_id, is_used_in_summary, is_generated, form_element_group_id, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Temperature', 1, TRUE, 1, FALSE, FALSE, 1, '2b2e9964-d942-4f83-a296-1096db2c2f0b', 1, 1, 1, current_timestamp, current_timestamp);

INSERT INTO form_element (name, display_order, is_mandatory, concept_id, is_used_in_summary, is_generated, form_element_group_id, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES ('Paracheck', 3, TRUE, 19, FALSE, FALSE, 1, 'b6edbb87-22d8-4265-9231-aad499475d0c', 1, 1, 1, current_timestamp, current_timestamp);

INSERT INTO form_mapping (form_id, entity_id, uuid, version, created_by_id, last_modified_by_id, created_date_time, last_modified_date_time)
VALUES (1, 1, '741cbb1f-f1bf-42f2-87f7-f5258aa91647', 0, 1, 1, current_timestamp, current_timestamp);

SELECT setval('catchment_id_seq', COALESCE((SELECT MAX(id)+1 FROM catchment), 1), false);