package org.example.service;


import org.example.model.Department;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class DepartmentService {

    public List<Department> listDepartments() {
        return Department.listAll();
    }

    @Transactional
    public void addDepartment(Department department) {
        department.persist();
    }

    public Department findById(Long id) {
        return Department.findById(id);
    }

    @Transactional
    public void updateDepartment(Department department) {
        department.persist();
    }

    @Transactional
    public void deleteDepartment(Long id) {
        Department.deleteById(id);
    }
}
