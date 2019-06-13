package io.openshift.booster.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import io.openshift.booster.model.Freelancer;

@ApplicationScoped
public interface FreelancerService {

	List<Freelancer> getFreelancers();

	Freelancer getFreelancerById(Integer freelancerId);

}
