package dev.angelcruzl.springboot.testing.webflux.repository;

import dev.angelcruzl.springboot.testing.webflux.entity.Employee;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EmployeeRepository extends ReactiveCrudRepository<Employee, String> {
}
