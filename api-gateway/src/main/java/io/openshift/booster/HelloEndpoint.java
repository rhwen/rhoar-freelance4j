package io.openshift.booster;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/hello")
public class HelloEndpoint {

	@GET
	@Produces("text/plain")
	public Response hello() {
		return Response.ok("Hello World").build();
	}
}
