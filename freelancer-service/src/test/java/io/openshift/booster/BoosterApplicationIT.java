/*
 * Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.openshift.booster;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.openshift.booster.service.Freelancer;
import io.openshift.booster.service.FreelancerRepository;
import io.restassured.RestAssured;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BoosterApplicationIT {

	private static final String FREELANCERS_PATH = "freelancers";

	@Value("${local.server.port}")
	private int port;

	@Autowired
	private FreelancerRepository repository;

	@Before
	public void beforeTest() {
		repository.deleteAll();
		RestAssured.baseURI = String.format("http://localhost:%d/" + FREELANCERS_PATH, port);
	}

	@Test
	public void testGetAll() {
		Freelancer f1 = new Freelancer();
		f1.setFreelancerId(1);
		f1.setFirstName("Hello");
		repository.save(f1);

		Freelancer f2 = new Freelancer();
		f2.setFreelancerId(2);
		f2.setFirstName("World");
		repository.save(f2);

		given().baseUri(String.format("http://localhost:%d/%s", port, FREELANCERS_PATH)).get().then().statusCode(200)
				.body("freelancerId", hasItems(f1.getFreelancerId(), f2.getFreelancerId()))
				.body("firstName", hasItems(f1.getFirstName(), f2.getFirstName()));
	}

	@Test
	public void testGetOne() {
		Freelancer f1 = new Freelancer();
		f1.setFreelancerId(1);
		f1.setFirstName("Hello");
		repository.save(f1);
		given().baseUri(String.format("http://localhost:%d/%s/%d", port, FREELANCERS_PATH, f1.getFreelancerId())).get()
				.then().statusCode(200).body("freelancerId", is(f1.getFreelancerId()))
				.body("firstName", is(f1.getFirstName()));
	}

}
