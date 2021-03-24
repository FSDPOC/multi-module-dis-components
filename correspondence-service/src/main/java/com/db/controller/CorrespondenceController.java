package com.db.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/corres")
// @Api(value = "Correspondence Admin Controller")
public class CorrespondenceController {

  @GetMapping("/admin")
  public ResponseEntity getCorrespondence() {

    return ResponseEntity.ok("Correspondence service module is active");
  }
}
