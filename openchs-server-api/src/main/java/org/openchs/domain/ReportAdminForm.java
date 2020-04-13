package org.openchs.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
 
@Entity
@Table(name = "report_admin_form")
public class ReportAdminForm {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @SequenceGenerator(name = "auto_gen", sequenceName = "A")
  private long id;

  @Column(name = "report_name")
  private String report_name;

  @Column(name = "corres_report_name")
  private String corres_report_name;

  @Column(name = "user_role")
  private int user_role;

  @Column(name = "description")
  private String description;

  @Column(name = "organisation_id")
  private int organisation_id;

  @Column(name = "is_active")
  private boolean is_active;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getReport_name() {
    return report_name;
  }

  public void setReport_name(String report_name) {
    this.report_name = report_name;
  }

  public String getCorres_report_name() {
    return corres_report_name;
  }

  public void setCorres_report_name(String corres_report_name) {
    this.corres_report_name = corres_report_name;
  }

  public int getUser_role() {
    return user_role;
  }

  public void setUser_role(int user_role) {
    this.user_role = user_role;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getOrganisation_id() {
    return organisation_id;
  }

  public void setOrganisation_id(int organisation_id) {
    this.organisation_id = organisation_id;
  }

  public boolean isIs_active() {
    return is_active;
  }

  public void setIs_active(boolean is_active) {
    this.is_active = is_active;
  }

}