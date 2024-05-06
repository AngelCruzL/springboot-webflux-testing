package dev.angelcruzl.springboot.testing.webflux.integration;

import dev.angelcruzl.springboot.testing.webflux.dto.EmployeeDto;
import dev.angelcruzl.springboot.testing.webflux.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerITests {

    @Autowired
    private EmployeeService service;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testSaveEmployee() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Angel");
        employeeDto.setLastName("Cruz");
        employeeDto.setEmail("me@angelcruzl.dev");

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
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Angel");
        employeeDto.setLastName("Cruz");
        employeeDto.setEmail("test@angelcruzl.dev");

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

}
