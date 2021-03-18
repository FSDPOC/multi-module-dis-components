package com.db.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @Value("${application.name}")
    private String applicationName;

    @Value("${build.version}")
    private String buildVersion;

    @Value("${build.timestamp}")
    private String buildTimestamp;

    @Value("${maven.version}")
    private String mavenVersion;

    @GetMapping("/health-info")
    public ResponseEntity getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n[Multi Module Application project details : multi-module-dis-components]");
        sb.append("\nApplication name : " + applicationName);
        sb.append("\nBuild version    : " + buildVersion);
        sb.append("\nBuild timestamp  : " + buildTimestamp);
        sb.append("\nMaven version  : " + mavenVersion);
        return ResponseEntity.ok(sb);
    }
}
