package br.com.erudio.integrationstests.controller.withjson;


import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.erudio.data.vo.v1.security.TokenVO;
import br.com.erudio.integrationstests.vo.AccountCredentialsVO;
import br.com.erudio.integrationstests.vo.wrappers.WrapperPersonVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.*;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.erudio.configs.TestConfigs;
import br.com.erudio.integrationstests.testcontainers.AbstractIntegrationsTest;
import br.com.erudio.integrationstests.vo.PersonVO;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationsTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;

	private static PersonVO person;

	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		person = new PersonVO();
	}

	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {

		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin234");

		var accessToken = given()
				.basePath("/auth/signin")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(user)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenVO.class)
				.getAccessToken();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockPerson();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(person)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());

		assertTrue(persistedPerson.getId() > 0);

		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet", persistedPerson.getLastName());
		assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		person.setLastName("Piquet Souto Maior");

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(person)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());

		assertEquals(person.getId(), persistedPerson.getId());

		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
		assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

	@Test
	@Order(3)
	public void testDisablePersonById() throws JsonMappingException, JsonProcessingException {

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("id", person.getId())
				.when()
				.patch("{id}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertFalse(persistedPerson.getEnabled());

		assertEquals(person.getId(), persistedPerson.getId());

		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
		assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

	@Test
	@Order(4)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockPerson();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("id", person.getId())
				.when()
				.get("{id}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());

		assertEquals(person.getId(), persistedPerson.getId());

		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
		assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

	@Test
	@Order(5)
	public void testDelete() throws JsonMappingException, JsonProcessingException {

		given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("id", person.getId())
				.when()
				.delete("{id}")
				.then()
				.statusCode(204);
	}

	@Test
	@Order(6)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.queryParam("page", 0, "size", 10, "direction", "asc")
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
		var people = wrapper.getEmbedded().getPersons();
		PersonVO foundPersonOne = people.getFirst();

		assertNotNull(foundPersonOne.getId());
		assertNotNull(foundPersonOne.getFirstName());
		assertNotNull(foundPersonOne.getLastName());
		assertNotNull(foundPersonOne.getAddress());
		assertNotNull(foundPersonOne.getGender());

		assertTrue(foundPersonOne.getEnabled());

		assertEquals(11, foundPersonOne.getId());

		assertEquals("Abbe", foundPersonOne.getFirstName());
		assertEquals("Henrique", foundPersonOne.getLastName());
		assertEquals("23438 Bluejay Place", foundPersonOne.getAddress());
		assertEquals("Male", foundPersonOne.getGender());

		PersonVO foundPersonTre = people.get(2);

		assertNotNull(foundPersonTre.getId());
		assertNotNull(foundPersonTre.getFirstName());
		assertNotNull(foundPersonTre.getLastName());
		assertNotNull(foundPersonTre.getAddress());
		assertNotNull(foundPersonTre.getGender());

		assertFalse(foundPersonTre.getEnabled());

		assertEquals(895, foundPersonTre.getId());

		assertEquals("Adams", foundPersonTre.getFirstName());
		assertEquals("Thurley", foundPersonTre.getLastName());
		assertEquals("3 Fieldstone Center", foundPersonTre.getAddress());
		assertEquals("Male", foundPersonTre.getGender());
	}


	@Test
	@Order(7)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {

		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		given().spec(specificationWithoutToken)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.when()
				.get()
				.then()
				.statusCode(403);
	}

	@Test
	@Order(8)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("firstName", "Jo")
				.queryParam("page", 1, "size", 10, "direction", "asc")
					.when()
					.get("findPersonByName/{firstName}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
		var people = wrapper.getEmbedded().getPersons();
		PersonVO foundPersonOne = people.getFirst();

		assertNotNull(foundPersonOne.getId());
		assertNotNull(foundPersonOne.getFirstName());
		assertNotNull(foundPersonOne.getLastName());
		assertNotNull(foundPersonOne.getAddress());
		assertNotNull(foundPersonOne.getGender());

		assertTrue(foundPersonOne.getEnabled());

		assertEquals(26, foundPersonOne.getId());

		assertEquals("Josh", foundPersonOne.getFirstName());
		assertEquals("Djuricic", foundPersonOne.getLastName());
		assertEquals("16 Steensland Circle", foundPersonOne.getAddress());
		assertEquals("Male", foundPersonOne.getGender());
	}

	@Test
	@Order(9)
	public void testHATEOAS() throws JsonMappingException, JsonProcessingException {

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.accept(TestConfigs.CONTENT_TYPE_JSON)
				.queryParams("page", 3, "size", 10, "direction", "asc")
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/800\"}}}"));
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/818\"}}}"));
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/793\"}}}"));

		assertTrue(content.contains("{\"first\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=0&size=10&sort=firstName,asc\"}"));
		assertTrue(content.contains("\"prev\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=2&size=10&sort=firstName,asc\"}"));
		assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/person/v1?page=3&size=10&direction=asc\"}"));
		assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=4&size=10&sort=firstName,asc\"}"));
		assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=100&size=10&sort=firstName,asc\"}}"));

		assertTrue(content.contains("\"page\":{\"size\":10,\"totalElements\":1003,\"totalPages\":101,\"number\":3}}"));
	}

	private void mockPerson() {
		person.setFirstName("Nelson");
		person.setLastName("Piquet");
		person.setAddress("Brasília - DF - Brasil");
		person.setGender("Male");
		person.setEnabled(true);
	}
}