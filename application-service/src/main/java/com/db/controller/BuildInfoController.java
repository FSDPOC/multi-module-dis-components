package com.db.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuildInfoController {

    @Autowired BuildProperties buildProperties;

    @GetMapping("/build-info")
    public ResponseEntity getBuildInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n[Multi Module Application project details : multi-module-dis-components]");
        sb.append("\nApplication name : " + buildProperties.getName());
        sb.append("\nBuild version    : " + buildProperties.getVersion());
        sb.append("\nBuild timestamp  : " + buildProperties.getTime());
        sb.append("\nMaven version  : " + buildProperties.get("maven.version"));
        sb.append("\nRaghu Machine OS  : " + buildProperties.get("raghu.machine"));
        return ResponseEntity.ok(sb);
    }

    @Autowired private GitProperties gitProperties;

    @GetMapping("/git-info")
    public ResponseEntity gitInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n[Multi Module Application project details : multi-module-dis-components]");
        sb.append("\nBranch name : " + this.gitProperties.getBranch());
        sb.append("\nBuild host    : " + this.gitProperties.get("build.host"));
        sb.append("\ngit Commit id  : " + this.gitProperties.getCommitId());
        sb.append("\ngit Commit Time : " + this.gitProperties.getCommitTime());
        return ResponseEntity.ok(sb);
    }

    @Autowired private Environment environment;

    @GetMapping("/profile-info")
    public Map<String, Object> profileInfo() {
        return Map.of(
                "activeProfiles",
                String.join(",", this.environment.getActiveProfiles()),
                "defaultProfiles",
                String.join(",", this.environment.getDefaultProfiles()));
    }
}
