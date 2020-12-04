package com.moustaka.katerina.backend_application_moustaka.Models;

public class Lending {
  private String bookId;
  private String peopleId;

  public Lending(String bookId, String peopleId) {
    this.bookId = bookId;
    this.peopleId = peopleId;
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
