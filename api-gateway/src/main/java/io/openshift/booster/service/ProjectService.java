package io.openshift.booster.service;

import java.util.List;

import io.openshift.booster.model.Project;


public interface ProjectService {

	public List<Project> getProjects();

	public Project getProjectById(Integer id);

	public List<Project> getProjectsByStatus(String status);

}
