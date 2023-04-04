package io.ylab.intensive.lesson05.eventsourcing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Person {
  private Long id;
  private String name;
  private String lastName;
  private String middleName;

  public Person() {
  }

  public Person(Long id, String name, String lastName, String middleName) {
    this.id = id;
    this.name = name;
    this.lastName = lastName;
    this.middleName = middleName;
  }

  public String toJson() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(this);
  }

  public static io.ylab.intensive.lesson04.eventsourcing.Person fromJson(String json) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(json, io.ylab.intensive.lesson04.eventsourcing.Person.class);
  }
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }
}
