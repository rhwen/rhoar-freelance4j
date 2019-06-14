package io.openshift.booster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openshift.booster.api.ApiVerticle;
import io.openshift.booster.service.ProjectService;
import io.openshift.booster.service.ProjectVerticle;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.config.ConfigRetriever;
import io.vertx.rxjava.core.AbstractVerticle;

public class MainApplication extends AbstractVerticle {

	Logger log = LoggerFactory.getLogger(MainApplication.class);

	@Override
	public void start(final Future<Void> future) {

		ConfigStoreOptions jsonConfigStore = new ConfigStoreOptions().setType("json");
		ConfigStoreOptions appStore = new ConfigStoreOptions().setType("configmap").setFormat("yaml")
				.setConfig(new JsonObject().put("name", System.getenv("APP_CONFIGMAP_NAME")).put("key",
						System.getenv("APP_CONFIGMAP_KEY")));

		ConfigRetrieverOptions options = new ConfigRetrieverOptions();
		if (System.getenv("KUBERNETES_NAMESPACE") != null) {
			// we're running in Kubernetes
			options.addStore(appStore);
		} else {
			// default to json based config
			jsonConfigStore.setConfig(config());
			options.addStore(jsonConfigStore);
		}

		ConfigRetriever.create(vertx, options).getConfig(ar -> {
			if (ar.succeeded()) {
				deployVerticles(ar.result(), future);
			} else {
				log.error("Failed to retrieve the configuration.");
				future.fail(ar.cause());
			}
		});
	}

	private void deployVerticles(JsonObject config, Future<Void> startFuture) {

		Future<String> apiVerticleFuture = Future.future();
		Future<String> projectVerticleFuture = Future.future();

		ProjectService projectService = ProjectService.createProxy(vertx.getDelegate());
		DeploymentOptions options = new DeploymentOptions();
		options.setConfig(config);
		vertx.deployVerticle(new ProjectVerticle(), options, projectVerticleFuture.completer());
		vertx.deployVerticle(new ApiVerticle(projectService), options, apiVerticleFuture.completer());

		CompositeFuture.all(apiVerticleFuture, projectVerticleFuture).setHandler(ar -> {
			if (ar.succeeded()) {
				log.info("Verticles deployed successfully.");
				startFuture.complete();
			} else {
				log.warn("WARNINIG: Verticles NOT deployed successfully.");
				startFuture.fail(ar.cause());
			}
		});

	}

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}

}
