package dev.angelcruzl.springboot.testing.webflux.service;

import dev.angelcruzl.springboot.testing.webflux.dto.EmployeeDto;
import reactor.core.publisher.Mono;

public interface EmployeeService {

    Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto);

    Mono<EmployeeDto> getEmployeeById(String id);

}
