package dev.angelcruzl.springboot.testing.webflux.controller;

import dev.angelcruzl.springboot.testing.webflux.dto.EmployeeDto;
import dev.angelcruzl.springboot.testing.webflux.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = EmployeeController.class)
public class EmployeeControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EmployeeService service;

    @DisplayName("Test to save a new employee")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() {
        // given - precondition or setup
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Angel");
        employeeDto.setLastName("Cruz");
        employeeDto.setEmail("me@angelcruzl.dev");

        BDDMockito.given(service.saveEmployee(any(EmployeeDto.class)))
                .willReturn(Mono.just(employeeDto));

        // when - action or the behaviour that we are going test
        WebTestClient.ResponseSpec response = webTestClient.post()
                .uri("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();

        // then - verify the output
        response.expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo("Angel")
                .jsonPath("$.lastName").isEqualTo("Cruz")
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @DisplayName("Test to get an employee by id")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() {
        // given - precondition or setup
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId("1");
        employeeDto.setFirstName("Angel");
        employeeDto.setLastName("Cruz");
        employeeDto.setEmail("me@angelcruzl.dev");

        BDDMockito.given(service.getEmployeeById("1"))
                .willReturn(Mono.just(employeeDto));

        // when - action or the behaviour that we are going test
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/v1/employees/{id}", Collections.singletonMap("id", "1"))
                .exchange();

        // then - verify the output
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.firstName").isEqualTo("Angel")
                .jsonPath("$.lastName").isEqualTo("Cruz")
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

}
