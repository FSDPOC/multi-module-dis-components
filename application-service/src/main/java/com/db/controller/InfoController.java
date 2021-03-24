package com.db.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
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
  public Map<String, Object> getInfo() {
    //        StringBuilder sb = new StringBuilder();
    //        sb.append("\n[Multi Module Application project details :
    // multi-module-dis-components]");
    //        sb.append("\nApplication name : " + applicationName);
    //        sb.append("\nBuild version    : " + buildVersion);
    //        sb.append("\nBuild timestamp  : " + buildTimestamp);
    //        sb.append("\nMaven version  : " + mavenVersion);
    //        return ResponseEntity.ok(sb);

    return Map.of(
        "Multi Module Application project details",
        String.join(",", "multi-module-dis-components"),
        "Application name",
        String.join(",", applicationName),
        "Build version",
        String.join(",", buildVersion),
        "Build timestamp",
        String.join(",", buildTimestamp),
        "Maven version",
        String.join(",", mavenVersion));
  }
}
