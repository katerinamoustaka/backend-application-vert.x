package com.moustaka.katerina.backend_application_moustaka.Models;

public class People {
  private String id;
  private String name;
  private String lastName;
  private String occupation;

  public People(String id, String name, String lastName, String occupation) {
    this.id = id;
    this.name = name;
    this.lastName = lastName;
    this.occupation = occupation;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getOccupation() {
    return occupation;
  }

  public void setOccupation(String occupation) {
    this.occupation = occupation;
  }
}
