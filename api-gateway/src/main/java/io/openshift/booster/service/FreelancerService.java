package io.openshift.booster.service;

import java.util.List;

import io.openshift.booster.model.Freelancer;


public interface FreelancerService {

	List<Freelancer> getFreelancers();

	Freelancer getFreelancerById(Integer freelancerId);

}
