package com.db.camunda.controller;

import com.db.camunda.response.BasicResponseMessage;
import com.db.camunda.service.DmnService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/decision")
@Slf4j
@Api(value = "Camunda Decision Controller")
public class DecisionController {

  private DmnService dmnService;

  public DecisionController(DmnService dmnService) {
    this.dmnService = dmnService;
  }

  @GetMapping(value = "/quarter/{month}")
  public ResponseEntity<BasicResponseMessage> decideQuarter(@PathVariable("month") Integer month) {
    Long quarter = dmnService.decideQuarter(month);
    return ResponseEntity.ok(
        new BasicResponseMessage("Month " + month + " is in quarter " + quarter));
  }
}
