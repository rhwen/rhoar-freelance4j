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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import io.openshift.booster.model.Freelancer;


public class FreelancerServiceImpl implements FreelancerService {

	Logger log = LoggerFactory.getLogger(FreelancerServiceImpl.class);

	@Inject
	@ConfigurationValue("freelancer.service.url")
	private String freelancerUrl;

	private WebTarget freelancerService;

	@PostConstruct
	public void init() {
		log.info("init backend url");
		freelancerService = ((ResteasyClientBuilder) ClientBuilder.newBuilder()).connectionPoolSize(10).build()
				.target(freelancerUrl).path("freelancers");
	}

	@Override
	public List<Freelancer> getFreelancers() {
		log.info("get freelancers.");
		Response response = freelancerService.request(MediaType.APPLICATION_JSON).get();
		if (response.getStatus() == 200) {
			GenericType<List<Freelancer>> genericType = new GenericType<List<Freelancer>>() {
			};
			List<Freelancer> freelancers = response.readEntity(genericType);
			log.info("get freelancers:", freelancers);
			return freelancers;
		} else if (response.getStatus() == 404) {
			return null;
		} else {
			throw new ServiceUnavailableException();
		}
	}

	@Override
	public Freelancer getFreelancerById(Integer freelancerId) {
		log.info("get freelancer by id:", freelancerId);
		Response response = freelancerService.path(String.valueOf(freelancerId)).request(MediaType.APPLICATION_JSON)
				.get();
		if (response.getStatus() == 200) {
			Freelancer freelancer = response.readEntity(Freelancer.class);
			log.info("get freelancer:", freelancer);
			return freelancer;
		} else if (response.getStatus() == 404) {
			return null;
		} else {
			throw new ServiceUnavailableException();
		}
	}

}
