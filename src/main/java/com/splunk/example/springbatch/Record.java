package com.splunk.example.springbatch;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Record {
  int userId;
  @Id int id;
  String title;
  boolean completed;
}
