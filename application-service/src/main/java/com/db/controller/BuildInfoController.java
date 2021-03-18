package com.db.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuildInfoController {

    @Autowired BuildProperties buildProperties;

    @GetMapping("/build-info")
    public Map<String, Object> getBuildInfo() {
        return Map.of(
                "Multi Module Application project details",
                String.join(",", "multi-module-dis-components"),
                "Application name",
                String.join(",", buildProperties.getName()),
                "Build version",
                String.join(",", buildProperties.getVersion()),
                "Build timestamp",
                String.join(",", buildProperties.getTime().toString()),
                "Maven version",
                String.join(",", buildProperties.get("maven.version")),
                "Raghu Machine OS",
                String.join(",", buildProperties.get("raghu.machine")));
    }

    @Autowired private GitProperties gitProperties;

    @GetMapping("/git-info")
    public Map<String, Object> gitInfo() {
        return Map.of(
                "Multi Module Application project details",
                String.join(",", "multi-module-dis-components"),
                "Branch name",
                String.join(",", gitProperties.getBranch()),
                "Build host",
                String.join(",", gitProperties.get("build.host")),
                "git Commit id",
                String.join(",", gitProperties.getCommitId()),
                "git Commit Time",
                String.join(",", gitProperties.getCommitTime().toString()));
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
