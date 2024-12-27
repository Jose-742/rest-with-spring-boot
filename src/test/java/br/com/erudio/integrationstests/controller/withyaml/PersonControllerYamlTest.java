package br.com.erudio.integrationstests.controller.withyaml;


import br.com.erudio.configs.TestConfigs;
import br.com.erudio.data.vo.v1.security.TokenVO;
import br.com.erudio.integrationstests.controller.withyaml.mapper.YMLMapper;
import br.com.erudio.integrationstests.testcontainers.AbstractIntegrationsTest;
import br.com.erudio.integrationstests.vo.AccountCredentialsVO;
import br.com.erudio.integrationstests.vo.PersonVO;
import br.com.erudio.integrationstests.vo.pagedmodels.PagedModelPerson;
import br.com.erudio.integrationstests.vo.wrappers.WrapperPersonVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerYamlTest extends AbstractIntegrationsTest {

	private static RequestSpecification specification;
	private static YMLMapper objectMapper;

	private static PersonVO person;

	@BeforeAll
	public static void setup() {
		objectMapper = new YMLMapper();

		person = new PersonVO();
	}

	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {

		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin234");

		var accessToken = given()
				.config(
						RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(
												TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.basePath("/auth/signin")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(user, objectMapper)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenVO.class, objectMapper)
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

		var persistedPerson = given().spec(specification)
				.config(
						RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(
												TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(person, objectMapper)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(PersonVO.class, objectMapper);

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

		var persistedPerson = given().spec(specification)
				.config(
						RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(
												TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(person, objectMapper)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(PersonVO.class, objectMapper);

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

		var persistedPerson = given().spec(specification)
				.config(
						RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(
												TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", person.getId())
				.when()
				.patch("{id}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(PersonVO.class, objectMapper);

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

		var persistedPerson = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", person.getId())
				.when()
				.get("{id}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(PersonVO.class, objectMapper);

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
				.config(
						RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(
												TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", person.getId())
				.when()
				.delete("{id}")
				.then()
				.statusCode(204);
	}

	@Test
	@Order(6)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {

		var wrapper = given().spec(specification)
				.config(
						RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(
												TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.queryParam("page", 0, "size", 10, "direction", "asc")
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(PagedModelPerson.class, objectMapper);


		var people = wrapper.getContent();

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
				.config(
						RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(
												TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.when()
				.get()
				.then()
				.statusCode(403);
	}

	private void mockPerson() {
		person.setFirstName("Nelson");
		person.setLastName("Piquet");
		person.setAddress("Brasília - DF - Brasil");
		person.setGender("Male");
		person.setEnabled(true);
	}
}