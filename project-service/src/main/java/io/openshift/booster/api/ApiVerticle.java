package io.openshift.booster.api;

import java.util.List;

import io.openshift.booster.model.Project;
import io.openshift.booster.service.ProjectService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class ApiVerticle extends AbstractVerticle {

	private ProjectService service;

	public ApiVerticle(ProjectService service) {
		this.service = service;
	}

	@Override
	public void start(Future<Void> startFuture) throws Exception {

		// expose our REST API
		Router router = Router.router(vertx);

		router.get("/projects").handler(this::getProjects);
		router.get("/projects/:projectId").handler(this::getProjectById);
		router.get("/projects/:projectStatus").handler(this::getProjectsByStatus);

		// Health Checks
		router.get("/health/readiness").handler(rc -> rc.response().end("OK"));
		HealthCheckHandler healthCheckHandler = HealthCheckHandler.create(vertx).register("health", f -> health(f));
		router.get("/health/liveness").handler(healthCheckHandler);

		vertx.createHttpServer().requestHandler(router::accept).listen(config().getInteger("catalog.http.port", 8080),
				result -> {
					if (result.succeeded()) {
						startFuture.complete();
					} else {
						startFuture.fail(result.cause());
					}
				});
	}

	private void getProjects(RoutingContext rc) {

		service.getProjects(ar -> {
			if (ar.succeeded()) {
				List<Project> products = ar.result();
				// convert List<Product> to JSON array
				JsonArray json = new JsonArray();
				products.stream().map(p -> p.toJson()).forEach(p -> json.add(p));
				rc.response().putHeader("Content-type", "application/json").end(json.encodePrettily());
			} else {
				rc.fail(ar.cause());
			}
		});
	}

	private void getProjectById(RoutingContext rc) {
		Integer projectId = Integer.valueOf(rc.request().getParam("projectId"));
		service.getProjectById(projectId, ar -> {
			if (ar.succeeded()) {
				Project project = ar.result();
				JsonObject json;
				if (project != null) {
					json = project.toJson();
					rc.response().putHeader("Content-type", "application/json").end(json.encodePrettily());
				} else {
					rc.fail(404);
				}
			} else {
				rc.fail(ar.cause());
			}
		});
	}

	private void getProjectsByStatus(RoutingContext rc) {
		String projectStatus = rc.request().getParam("projectStatus");
		service.getProjectsByStatus(projectStatus, ar -> {
			if (ar.succeeded()) {
				List<Project> products = ar.result();
				// convert List<Product> to JSON array
				JsonArray json = new JsonArray();
				products.stream().map(p -> p.toJson()).forEach(p -> json.add(p));
				rc.response().putHeader("Content-type", "application/json").end(json.encodePrettily());
			} else {
				rc.fail(ar.cause());
			}
		});
	}

	private void health(Future<Status> future) {
		service.ping(ar -> {
			if (ar.succeeded()) {
				// HealthCheckHandler has a timeout of 1000s. If timeout is exceeded, the future
				// will be failed
				if (!future.isComplete()) {
					future.complete(Status.OK());
				}
			} else {
				if (!future.isComplete()) {
					future.complete(Status.KO());
				}
			}
		});
	}

}
