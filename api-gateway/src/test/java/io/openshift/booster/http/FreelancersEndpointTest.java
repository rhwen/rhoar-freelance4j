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
import io.openshift.booster.model.Freelancer;

@RunWith(Arquillian.class)
public class FreelancersEndpointTest {

	private static String port = "18080";

	private Client client;

	@Rule
	public WireMockRule freelancerServiceMock = new WireMockRule(8089);

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "api-gateway.war")
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
	public void testGetFreelancers() throws Exception {

		freelancerServiceMock.stubFor(get(urlEqualTo("/freelancers")).willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "application/json").withBody(buildFreelancersResponse())));

		WebTarget target = client.target("http://localhost:" + port).path("gateway").path("freelancers");

		Response response = target.request(MediaType.APPLICATION_JSON).get();

		assertThat(response.getStatus(), equalTo(new Integer(200)));

		GenericType<List<Freelancer>> genericType = new GenericType<List<Freelancer>>() {
		};
		List<Freelancer> freelancers = response.readEntity(genericType);
		assertThat(freelancers.size(), equalTo(2));
	}

	private String buildFreelancersResponse() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		List<Freelancer> freelancers = new ArrayList<Freelancer>();

		Freelancer f1 = new Freelancer();
		f1.setFreelancerId(1);
		f1.setFirstName("Hello");

		Freelancer f2 = new Freelancer();
		f2.setFreelancerId(1);
		f2.setFirstName("Hello");

		freelancers.add(f1);
		freelancers.add(f2);

		return mapper.writeValueAsString(freelancers);
	}

	@Test
	@RunAsClient
	public void testGetFreelancerById() throws Exception {

		freelancerServiceMock.stubFor(get(urlEqualTo("/freelancers/1")).willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "application/json").withBody(buildFreelancerResponse())));

		Integer id = 1;
		WebTarget target = client.target("http://localhost:" + port).path("gateway").path("freelancers")
				.path(String.valueOf(id));
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		assertThat(response.getStatus(), equalTo(new Integer(200)));
		Freelancer freelancer = response.readEntity(Freelancer.class);
		assertThat(freelancer.getFreelancerId(), equalTo(1));
	}

	private String buildFreelancerResponse() throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		Freelancer f1 = new Freelancer();
		f1.setFreelancerId(1);
		f1.setFirstName("Hello");

		return mapper.writeValueAsString(f1);
	}

}
