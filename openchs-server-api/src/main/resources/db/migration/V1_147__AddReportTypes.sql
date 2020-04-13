create table report_type (
  id              serial primary key,
  uuid            varchar(255),
  name           varchar(255) not null,
  organisation_id bigint       not null,
  is_voided       boolean      not null default false,
  audit_id        bigint       not null,
  version         integer               default 1
);