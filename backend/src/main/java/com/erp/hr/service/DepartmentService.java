package com.erp.hr.service;

import com.erp.hr.dto.DepartmentRequest;
import com.erp.hr.entity.Department;
import com.erp.hr.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(
            DepartmentRepository departmentRepository) {

        this.departmentRepository = departmentRepository;
    }

    public Department createDepartment(
            DepartmentRequest request) {

        if (departmentRepository.existsByName(request.getName())) {
            throw new RuntimeException(
                    "Department already exists"
            );
        }

        Department department = new Department();

        department.setName(request.getName());
        department.setDescription(request.getDescription());

        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {

        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {

        return departmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Department not found"
                        )
                );
    }

    public Department updateDepartment(
            Long id,
            DepartmentRequest request) {

        Department department =
                getDepartmentById(id);

        if (!department.getName()
                .equals(request.getName())
                && departmentRepository
                    .existsByName(request.getName())) {

            throw new RuntimeException(
                    "Department name already exists"
            );
        }

        department.setName(request.getName());
        department.setDescription(
                request.getDescription()
        );

        return departmentRepository.save(department);
    }

    public void deleteDepartment(Long id) {

        Department department =
                getDepartmentById(id);

        departmentRepository.delete(department);
    }
}