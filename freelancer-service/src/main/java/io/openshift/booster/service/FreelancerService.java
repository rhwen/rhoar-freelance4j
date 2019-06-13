package io.openshift.booster.service;

import java.util.List;

public interface FreelancerService {

	List<Freelancer> getFreelancers();

	Freelancer getFreelancerById(Integer freelancerId);

}
