/*
 *
 *  Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package io.openshift.booster.http;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openshift.booster.model.Project;
import io.openshift.booster.service.ProjectService;
import io.openshift.booster.service.ProjectServiceImpl;

@Path("/gateway/projects")
@ApplicationScoped
public class ProjectsEndpoint {

	Logger log = LoggerFactory.getLogger(ProjectsEndpoint.class);

	
	private ProjectService service  = new ProjectServiceImpl();

	@GET
	@Path("/")
	@Produces("application/json")
	public Response getProjects() {
		log.info("get projects.");
		List<Project> projects = service.getProjects();
		return Response.ok(projects).build();
	}

	@GET
	@Path("/{projectId}")
	@Produces("application/json")
	public Response getProjectById(@PathParam("projectId") Integer id) {
		log.info("get project by id:", id);
		Project project = service.getProjectById(id);
		return Response.ok(project).build();
	}

	@GET
	@Path("/status/{theStatus}")
	@Produces("application/json")
	public Response getProjectByStatus(@PathParam("theStatus") String status) {
		log.info("get project by status:", status);
		List<Project> projects = service.getProjectsByStatus(status);
		return Response.ok(projects).build();
	}
}