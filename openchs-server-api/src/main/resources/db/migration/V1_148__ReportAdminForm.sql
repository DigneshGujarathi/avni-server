create table report_admin_form (
  id              serial primary key,
  report_name     varchar(255) not null,
  corres_report_name varchar(255) not null,
  user_role       integer      not null  default 1,
  description     varchar(255) not null,
  organisation_id bigint       not null,
  is_active       boolean      not null default false,
  version         integer               default 1
);