package com.db.camunda.service;

public interface DmnService {
  void initDmnEngine();

  Long decideQuarter(Integer month);
}
