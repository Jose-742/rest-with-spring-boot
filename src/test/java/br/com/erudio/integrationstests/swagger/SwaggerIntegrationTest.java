package br.com.erudio.integrationstests.swagger;


import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import br.com.erudio.configs.TestConfigs;
import br.com.erudio.integrationstests.testcontainers.AbstractIntegrationsTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationsTest {

	@Test
	public void shouldDisplaySwaggerUiPage() {
		var content =
			given()
					.basePath("/swagger-ui/index.html")
					.port(TestConfigs.SERVER_PORT)
					.when()
						.get()
					.then()
						 .statusCode(200)
					.extract()
						.body()
							.asString();
		assertTrue(content.contains("Swagger UI"));
	}

}
