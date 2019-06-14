package io.openshift.booster.api;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import io.openshift.booster.model.Project;
import io.openshift.booster.service.ProjectService;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class ApiVerticleTest {

	private Vertx vertx;
	private Integer port;
	private ProjectService projectService;

	@Before
	public void setUp(TestContext context) throws IOException {
		vertx = Vertx.vertx();
		vertx.exceptionHandler(context.exceptionHandler());
		ServerSocket socket = new ServerSocket(0);
		port = socket.getLocalPort();
		socket.close();
		DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("project.http.port", port));
		projectService = mock(ProjectService.class);
		vertx.deployVerticle(new ApiVerticle(projectService), options, context.asyncAssertSuccess());
	}

	@After
	public void tearDown(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}

	@Test
	public void testGetProjects(TestContext context) throws Exception {
		Integer projectId1 = 1;
		JsonObject json1 = new JsonObject().put("projectId", projectId1);

		Integer projectId2 = 2;
		JsonObject json2 = new JsonObject().put("projectId", projectId2);

		List<Project> projects = new ArrayList<>();
		projects.add(new Project(json1));
		projects.add(new Project(json2));

		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Handler<AsyncResult<List<Project>>> handler = invocation.getArgument(0);
				handler.handle(Future.succeededFuture(projects));
				return null;
			}
		}).when(projectService).getProjects(any());

		Async async = context.async();
		vertx.createHttpClient().get(port, "localhost", "/projects", response -> {
			assertThat(response.statusCode(), equalTo(200));
			assertThat(response.headers().get("Content-type"), equalTo("application/json"));
			response.bodyHandler(body -> {
				JsonArray json = body.toJsonArray();
				Set<Integer> projectIds = json.stream().map(j -> new Project((JsonObject) j)).map(p -> p.getProjectId())
						.collect(Collectors.toSet());
				assertThat(projectIds.size(), equalTo(2));
				verify(projectService).getProjects(any());
				async.complete();
			}).exceptionHandler(context.exceptionHandler());
		}).exceptionHandler(context.exceptionHandler()).end();
	}

}
