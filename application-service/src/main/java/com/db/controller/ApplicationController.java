package com.db.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class ApplicationController {

  @Autowired MessageSource messageSource;

  @GetMapping("/admin")
  public ResponseEntity getAdmin() {
    return ResponseEntity.ok("Application service module is active");
  }
}
