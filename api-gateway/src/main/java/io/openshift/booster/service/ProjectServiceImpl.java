package io.openshift.booster.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import io.openshift.booster.model.Project;


public class ProjectServiceImpl implements ProjectService {

	@Inject
	@ConfigurationValue("project.service.url")
	private String projectUrl;

	private WebTarget projectService;

	@PostConstruct
	public void init() {
		projectService = ((ResteasyClientBuilder) ClientBuilder.newBuilder()).connectionPoolSize(10).build()
				.target(projectUrl).path("projects");
	}

	@Override
	public List<Project> getProjects() {
		Response response = projectService.request(MediaType.APPLICATION_JSON).get();
		if (response.getStatus() == 200) {
			GenericType<List<Project>> genericType = new GenericType<List<Project>>() {
			};
			List<Project> projects = response.readEntity(genericType);
			return projects;
		} else if (response.getStatus() == 404) {
			return null;
		} else {
			throw new ServiceUnavailableException();
		}
	}

	@Override
	public Project getProjectById(Integer id) {
		Response response = projectService.path(String.valueOf(id)).request(MediaType.APPLICATION_JSON).get();
		if (response.getStatus() == 200) {
			Project project = response.readEntity(Project.class);
			return project;
		} else if (response.getStatus() == 404) {
			return null;
		} else {
			throw new ServiceUnavailableException();
		}
	}

	@Override
	public List<Project> getProjectsByStatus(String status) {
		Response response = projectService.path("status").path(status).request(MediaType.APPLICATION_JSON).get();
		if (response.getStatus() == 200) {
			GenericType<List<Project>> genericType = new GenericType<List<Project>>() {
			};
			List<Project> projects = response.readEntity(genericType);
			return projects;
		} else if (response.getStatus() == 404) {
			return null;
		} else {
			throw new ServiceUnavailableException();
		}
	}

}
