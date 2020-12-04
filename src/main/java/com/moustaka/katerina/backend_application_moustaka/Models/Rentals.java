package com.moustaka.katerina.backend_application_moustaka.Models;

public class Rentals {
  private String title;
  private String author;
  private String name;
  private String lastName;
  private String bookId;
  private String peopleId;

  public Rentals(String title, String author, String name, String lastName, String bookId, String peopleId) {
    this.title = title;
    this.author = author;
    this.name = name;
    this.lastName = lastName;
    this.bookId = bookId;
    this.peopleId = peopleId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
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

  public String getBookId() {
    return bookId;
  }

  public void setBookId(String bookId) {
    this.bookId = bookId;
  }

  public String getPeopleId() {
    return peopleId;
  }

  public void setPeopleId(String peopleId) {
    this.peopleId = peopleId;
  }
}
