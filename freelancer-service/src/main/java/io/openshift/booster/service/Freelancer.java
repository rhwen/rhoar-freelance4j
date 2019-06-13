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
package io.openshift.booster.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Freelancer implements Serializable {

	// The Freelancer service provides information about freelancers. A freelancer
	// has the following fields:
	// freelancerId
	// first name
	// last name
	// email address
	// list of skills (for example: Java, Thorntail, Vert.x, Spring Boot, JPA etc …)

	private static final long serialVersionUID = -8236877253728366461L;

	@Id
	private Integer freelancerId;
	private String firstName;
	private String lastName;
	private String email;
	private String skills;

	public Freelancer() {
	}

	public Integer getFreelancerId() {
		return freelancerId;
	}

	public void setFreelancerId(Integer freelancerId) {
		this.freelancerId = freelancerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

}
