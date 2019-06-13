package io.openshift.booster.http;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import io.openshift.booster.RestApplication;
import io.openshift.booster.model.Project;

@RunWith(Arquillian.class)
public class ProjectsEndpointTest {

	private static String port = "18080";

	private Client client;

	@Rule
	public WireMockRule projectServiceMock = new WireMockRule(8089);

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "freelancer-service.war")
				.addPackages(true, RestApplication.class.getPackage())
				.addAsResource("project-local.yml", "project-defaults.yml");
	}

	@Before
	public void before() throws Exception {
		client = ClientBuilder.newClient();
	}

	@After
	public void after() throws Exception {
		client.close();
	}

	@Test
	@RunAsClient
	public void testGetProjects() throws Exception {
		projectServiceMock.stubFor(get(urlEqualTo("/projects")).willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "application/json").withBody(buildProjectsResponse())));

		WebTarget target = client.target("http://localhost:" + port).path("gateway").path("projects");

		Response response = target.request(MediaType.APPLICATION_JSON).get();

		assertThat(response.getStatus(), equalTo(new Integer(200)));

		GenericType<List<Project>> genericType = new GenericType<List<Project>>() {
		};
		List<Project> projects = response.readEntity(genericType);
		assertThat(projects.size(), equalTo(2));
	}

	private String buildProjectsResponse() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		List<Project> projects = new ArrayList<Project>();

		Project p1 = new Project();
		p1.setProjectId(1);
		p1.setProjectTitle("Hello");

		Project p2 = new Project();
		p2.setProjectId(1);
		p2.setProjectTitle("Hello");

		projects.add(p1);
		projects.add(p2);

		return mapper.writeValueAsString(projects);
	}

	@Test
	@RunAsClient
	public void testGetFreelancerById() throws Exception {

		projectServiceMock.stubFor(get(urlEqualTo("/projects/1")).willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "application/json").withBody(buildProjectResponse())));

		Integer id = 1;
		WebTarget target = client.target("http://localhost:" + port).path("gateway").path("projects")
				.path(String.valueOf(id));
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		assertThat(response.getStatus(), equalTo(new Integer(200)));
		Project project = response.readEntity(Project.class);
		assertThat(project.getProjectId(), equalTo(1));
	}

	private String buildProjectResponse() throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		Project p1 = new Project();
		p1.setProjectId(1);
		p1.setProjectTitle("Hello");

		return mapper.writeValueAsString(p1);
	}

	@Test
	@RunAsClient
	public void testGetProjectsByStatus() throws Exception {

		projectServiceMock.stubFor(get(urlEqualTo("/projects/status/open")).willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "application/json").withBody(buildProjectStatusOpenResponse())));

		String status = "open";
		WebTarget target = client.target("http://localhost:" + port).path("gateway").path("projects").path("status")
				.path(status);

		Response response = target.request(MediaType.APPLICATION_JSON).get();

		assertThat(response.getStatus(), equalTo(new Integer(200)));

		GenericType<List<Project>> genericType = new GenericType<List<Project>>() {
		};
		List<Project> projects = response.readEntity(genericType);
		assertThat(projects.size(), equalTo(1));
	}

	private String buildProjectStatusOpenResponse() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		List<Project> projects = new ArrayList<Project>();

		Project p1 = new Project();
		p1.setProjectId(1);
		p1.setProjectTitle("Hello");
		p1.setProjectStatus("open");

		projects.add(p1);

		return mapper.writeValueAsString(projects);
	}

}
