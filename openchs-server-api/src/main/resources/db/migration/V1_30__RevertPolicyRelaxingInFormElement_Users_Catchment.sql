-- Previously modified by V1_19
-- Put back from V1_15
DROP POLICY form_element_orgs on form_element;
CREATE POLICY form_element_orgs ON form_element USING (organisation_id IN (WITH RECURSIVE list_of_orgs(id, parent_organisation_id) AS ( SELECT id, parent_organisation_id FROM organisation WHERE db_user = current_user UNION ALL SELECT o.id, o.parent_organisation_id FROM organisation o, list_of_orgs log WHERE o.id = log.parent_organisation_id ) SELECT id FROM list_of_orgs)) WITH CHECK ((organisation_id = (select id from organisation where db_user = current_user)));

-- Previously modified by V1_29
-- Put back from V0_42
ALTER TABLE users ENABLE ROW LEVEL SECURITY;
DROP POLICY IF EXISTS users_user ON users;
CREATE POLICY users_user ON users USING (organisation_id in (select id from organisation where db_user in ('openchs', current_user)))WITH CHECK (organisation_id in (select id from organisation where db_user in ('openchs', current_user)));
