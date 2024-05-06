package dev.angelcruzl.springboot.testing.webflux.integration;

import dev.angelcruzl.springboot.testing.webflux.dto.EmployeeDto;
import dev.angelcruzl.springboot.testing.webflux.repository.EmployeeRepository;
import dev.angelcruzl.springboot.testing.webflux.service.EmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerITests {

    @Autowired
    private EmployeeService service;

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private WebTestClient webTestClient;

    private EmployeeDto employeeDto;

    @BeforeEach
    public void setup() {
        employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Angel");
        employeeDto.setLastName("Cruz");
        employeeDto.setEmail("me@angelcruzl.dev");
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll().subscribe();
    }

    @Test
    public void testSaveEmployee() {
        webTestClient.post().uri("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo("Angel")
                .jsonPath("$.lastName").isEqualTo("Cruz")
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void testGetEmployeeById() {
        EmployeeDto savedEmployee = service.saveEmployee(employeeDto).block();

        webTestClient.get().uri("/api/v1/employees/" + savedEmployee.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(savedEmployee.getId())
                .jsonPath("$.firstName").isEqualTo("Angel")
                .jsonPath("$.lastName").isEqualTo("Cruz")
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void testGetAllEmployees() {
        service.saveEmployee(employeeDto).block();

        EmployeeDto employeeDto2 = new EmployeeDto();
        employeeDto2.setFirstName("John");
        employeeDto2.setLastName("Doe");
        employeeDto2.setEmail("test@mail.com");
        service.saveEmployee(employeeDto2).block();

        webTestClient.get().uri("/api/v1/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .hasSize(2);
    }

    @Test
    public void testUpdateEmployee() {
        EmployeeDto savedEmployee = service.saveEmployee(employeeDto).block();

        EmployeeDto updatedEmployee = new EmployeeDto();
        updatedEmployee.setFirstName("Luis");
        updatedEmployee.setLastName("Lara");
        updatedEmployee.setEmail("test@mail.com");

        webTestClient.put().uri("/api/v1/employees/{id}", Collections.singletonMap("id", savedEmployee.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedEmployee), EmployeeDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(savedEmployee.getId())
                .jsonPath("$.firstName").isEqualTo("Luis")
                .jsonPath("$.lastName").isEqualTo("Lara")
                .jsonPath("$.email").isEqualTo(updatedEmployee.getEmail());
    }

}
