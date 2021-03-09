package com.db.camunda.controller;

import com.db.camunda.response.BasicResponseMessage;
import com.db.camunda.service.DmnTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping(value = "/camunda")
@Slf4j
//@Api(value = "Camunda Admin Controller")
public class RulesController {

  private DmnTemplateService dmnTemplateService;

  public RulesController(DmnTemplateService dmnTemplateService) {
    this.dmnTemplateService = dmnTemplateService;
  }

  @PostMapping(value = "/rules/import")
  public ResponseEntity<BasicResponseMessage> importDecisionRules(
      @RequestParam("file") MultipartFile file) {
    try {
      log.info("Importing new excel files");
      dmnTemplateService.convertDmnTemplate(file.getInputStream());
      return ResponseEntity.ok(new BasicResponseMessage("Rules are imported successfully"));
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new BasicResponseMessage("Failed to convert DMN template file"));
    }
  }

  @GetMapping("/rules")
  public ResponseEntity getCamunda() {

    String filename = null;
    try {
      filename = ResourceUtils.getURL("classpath:").getPath() + "testTemplate.dmn";
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    System.out.println("filename path ::::: " + filename);
    return ResponseEntity.ok(
        new BasicResponseMessage("Rules service module is active" + filename));
  }
}
