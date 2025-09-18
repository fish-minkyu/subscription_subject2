package com.subject2.subscription.project.controller;

import com.subject2.subscription.project.entity.Project;
import com.subject2.subscription.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public void createProject(@RequestBody Project project) {
        projectService.createProject(project);
    }

    @GetMapping("/{id}")
    public Project readProject(@PathVariable("id") Long projectId) {
        return projectService.readProject(projectId);
    }

    @PatchMapping("/{id}")
    public void updateProject(@PathVariable("id") Long projectId, @RequestBody Project project) {
        projectService.updateProject(projectId, project);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable("id") Long projectId) {
        projectService.deleteProject(projectId);
    }
}
