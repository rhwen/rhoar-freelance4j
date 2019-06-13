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
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.openshift.booster.model.Freelancer;
import io.openshift.booster.service.FreelancerService;

@Path("/freelancers")
@ApplicationScoped
public class FreelancersEndpoint {

	@Inject
	private FreelancerService service;

	@GET
	@Path("/")
	@Produces("application/json")
	public Response getFreelancers() {
		List<Freelancer> freelancers = service.getFreelancers();
		return Response.ok(freelancers).build();
	}

	@GET
	@Path("/{freelancerId}")
	@Produces("application/json")
	public Response getFreelancers(@PathParam("freelancerId") Integer id) {
		Freelancer freelancer = service.getFreelancerById(id);
		return Response.ok(freelancer).build();
	}
}