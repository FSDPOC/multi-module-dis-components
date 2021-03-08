package com.db.camunda.service.impl;

import com.db.camunda.config.Configuration;
import com.db.camunda.service.DmnService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Paths;

@Service
@Slf4j
public class DmnServiceImpl implements DmnService {

  private DmnEngine dmnEngine;
  private DmnModelInstance dmnModelInstance;
  private DmnDecision decision;

  @Autowired
  private Configuration configuration;

  @Override
  @PostConstruct
  public void initDmnEngine() {
    dmnEngine = DmnEngineConfiguration.createDefaultDmnEngineConfiguration().buildEngine();

    dmnModelInstance = Dmn.readModelFromFile(Paths.get(configuration.getDmnFilePath()).toFile());
    decision = dmnEngine.parseDecision("test_rules", dmnModelInstance);
  }

  @Override
  public Long decideQuarter(Integer month) {
    VariableMap variables = Variables.createVariables().putValue("month", month);
    Long quarter =
        dmnEngine.evaluateDecision(decision, variables).getSingleResult().<Long>getEntry("quarter");
    log.info("quarter is {}", quarter);
    return quarter;
  }
}
