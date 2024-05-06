package dev.angelcruzl.springboot.testing.webflux.service.impl;

import dev.angelcruzl.springboot.testing.webflux.dto.EmployeeDto;
import dev.angelcruzl.springboot.testing.webflux.entity.Employee;
import dev.angelcruzl.springboot.testing.webflux.mapper.EmployeeMapper;
import dev.angelcruzl.springboot.testing.webflux.repository.EmployeeRepository;
import dev.angelcruzl.springboot.testing.webflux.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
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
}
