package io.openshift.booster.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FreelancerServiceImpl implements FreelancerService {

	@Autowired
	private FreelancerRepository repository;

	@Override
	public List<Freelancer> getFreelancers() {
		ArrayList<Freelancer> freelancers = new ArrayList<Freelancer>();
		repository.findAll().forEach(freelancers::add);
		return freelancers;
	}

	@Override
	public Freelancer getFreelancerById(Integer freelancerId) {
		return repository.findOne(freelancerId);
	}

}
