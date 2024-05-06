package dev.angelcruzl.springboot.testing.webflux.controller;

import dev.angelcruzl.springboot.testing.webflux.dto.EmployeeDto;
import dev.angelcruzl.springboot.testing.webflux.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
public class EmployeeController {

    private EmployeeService service;

    @GetMapping
    public Flux<EmployeeDto> getAllEmployees() {
        return service.getAllEmployees();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EmployeeDto> saveEmployee(@RequestBody EmployeeDto employeeDto) {
        return service.saveEmployee(employeeDto);
    }

    @GetMapping("/{id}")
    public Mono<EmployeeDto> getEmployeeById(@PathVariable String id) {
        return service.getEmployeeById(id);
    }

    @PutMapping("/{id}")
    public Mono<EmployeeDto> updateEmployee(@PathVariable("id") String employeeId,
                                            @RequestBody EmployeeDto employeeDto) {
        return service.updateEmployee(employeeId, employeeDto);
    }

}
