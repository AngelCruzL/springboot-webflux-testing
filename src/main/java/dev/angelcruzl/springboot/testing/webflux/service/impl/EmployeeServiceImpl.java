package dev.angelcruzl.springboot.testing.webflux.service.impl;

import dev.angelcruzl.springboot.testing.webflux.dto.EmployeeDto;
import dev.angelcruzl.springboot.testing.webflux.entity.Employee;
import dev.angelcruzl.springboot.testing.webflux.exception.NotFoundException;
import dev.angelcruzl.springboot.testing.webflux.mapper.EmployeeMapper;
import dev.angelcruzl.springboot.testing.webflux.repository.EmployeeRepository;
import dev.angelcruzl.springboot.testing.webflux.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository repository;

    @Override
    public Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.toEmployee(employeeDto);
        Mono<Employee> savedEmployee = repository.save(employee);

        return savedEmployee.map(EmployeeMapper::toEmployeeDto);
    }

    @Override
    public Mono<EmployeeDto> getEmployeeById(String id) {
        Mono<Employee> employee = repository.findById(id);

        return employee.map(EmployeeMapper::toEmployeeDto)
                .switchIfEmpty(Mono.error(
                        new NotFoundException("Employee with id " + id + " not found")));
    }

    @Override
    public Flux<EmployeeDto> getAllEmployees() {
        Flux<Employee> employeeFlux = repository.findAll();

        return employeeFlux.map(EmployeeMapper::toEmployeeDto).
                switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<EmployeeDto> updateEmployee(String id, EmployeeDto employeeDto) {
        Mono<Employee> employeeMono = repository.findById(id);

        Mono<Employee> updatedEmployee = employeeMono.flatMap(existingEmployee -> {
            existingEmployee.setFirstName(employeeDto.getFirstName());
            existingEmployee.setLastName(employeeDto.getLastName());
            existingEmployee.setEmail(employeeDto.getEmail());

            return repository.save(existingEmployee);
        });

        return updatedEmployee.map(EmployeeMapper::toEmployeeDto);
    }

    @Override
    public Mono<Void> deleteEmployee(String id) {
        return repository.deleteById(id);
    }
}
