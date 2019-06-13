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
package io.openshift.booster.model;

import java.io.Serializable;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class Project implements Serializable {

	// The Project service provides information about the projects. A project has
	// the following fields:
	// projectId
	// owner first name
	// owner last name
	// owner email address
	// project title
	// project description
	// project status: open, in_progress, completed, cancelled

	private static final long serialVersionUID = 4372860366872253707L;

	private Integer projectId;
	private String ownerFirstName;
	private String ownerLastName;
	private String ownerEmailAddress;
	private String projectTitle;
	private String projectDescription;
	private String projectStatus;

	public Project() {
	}

	public Project(JsonObject json) {
		this.projectId = json.getInteger("projectId");
		this.ownerFirstName = json.getString("ownerFirstName");
		this.ownerLastName = json.getString("ownerLastName");
		this.ownerEmailAddress = json.getString("ownerEmailAddress");
		this.projectTitle = json.getString("projectTitle");
		this.projectDescription = json.getString("projectDescription");
		this.projectStatus = json.getString("projectStatus");
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getOwnerFirstName() {
		return ownerFirstName;
	}

	public void setOwnerFirstName(String ownerFirstName) {
		this.ownerFirstName = ownerFirstName;
	}

	public String getOwnerLastName() {
		return ownerLastName;
	}

	public void setOwnerLastName(String ownerLastName) {
		this.ownerLastName = ownerLastName;
	}

	public String getOwnerEmailAddress() {
		return ownerEmailAddress;
	}

	public void setOwnerEmailAddress(String ownerEmailAddress) {
		this.ownerEmailAddress = ownerEmailAddress;
	}

	public String getProjectTitle() {
		return projectTitle;
	}

	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public JsonObject toJson() {

		final JsonObject json = new JsonObject();
		json.put("projectId", this.projectId);
		json.put("ownerFirstName", this.ownerFirstName);
		json.put("ownerLastName", this.ownerLastName);
		json.put("ownerEmailAddress", this.ownerEmailAddress);
		json.put("projectTitle", this.projectTitle);
		json.put("projectDescription", this.projectDescription);
		json.put("projectStatus", this.projectStatus);
		return json;
	}

}
