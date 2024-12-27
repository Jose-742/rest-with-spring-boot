package br.com.erudio.integrationstests.repositories;

import br.com.erudio.integrationstests.testcontainers.AbstractIntegrationsTest;

import br.com.erudio.model.Person;
import br.com.erudio.repositories.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationsTest {

    @Autowired
    public PersonRepository personRepository;

    private static Person person;

    @BeforeAll
    public static void setup(){
        person = new Person();
    }

    @Test
    @Order(1)
    public void testFindByName() throws JsonMappingException, JsonProcessingException {


        Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Direction.ASC, "firstName"));
        person = personRepository.findPersonByName("Jo", pageable).getContent().getFirst();

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());

        assertTrue(person.getEnabled());

        assertEquals(704, person.getId());

        assertEquals("Jory", person.getFirstName());
        assertEquals("Dumper", person.getLastName());
        assertEquals("73991 Northland Place", person.getAddress());
        assertEquals("Male", person.getGender());
    }

    @Test
    @Order(2)
    public void testDisablePerson() throws JsonMappingException, JsonProcessingException {

        personRepository.disablePerson(person.getId());

        Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Direction.ASC, "firstName"));
        person = personRepository.findPersonByName("Jo", pageable).getContent().getFirst();

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());

        assertFalse(person.getEnabled());

        assertEquals(704, person.getId());

        assertEquals("Jory", person.getFirstName());
        assertEquals("Dumper", person.getLastName());
        assertEquals("73991 Northland Place", person.getAddress());
        assertEquals("Male", person.getGender());
    }
}
