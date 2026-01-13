package com.example.teamtree.controller;

import com.example.teamtree.model.*;
import com.example.teamtree.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeViewController {

    private final EmployeeService employeeService;
    private final ContractService contractService;
    private final DepartmentService departmentService;
    private final PositionService positionService;
    private final RoleService roleService;
    private final SalaryService salaryService;

    public EmployeeViewController(EmployeeService employeeService,
                                  ContractService contractService,
                                  DepartmentService departmentService,
                                  PositionService positionService,
                                  RoleService roleService,
                                  SalaryService salaryService) {
        this.employeeService = employeeService;
        this.contractService = contractService;
        this.departmentService = departmentService;
        this.positionService = positionService;
        this.roleService = roleService;
        this.salaryService = salaryService;
    }

    // LIST EMPLOYEES
    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAll());
        return "employees";
    }

    // ADD EMPLOYEE FORM
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("contracts", contractService.getAll());
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("positions", positionService.getAll());
        model.addAttribute("roles", roleService.getAll());
        model.addAttribute("employees", employeeService.getAll()); // manager dropdown
        model.addAttribute("pageTitle", "Add Employee");
        return "employee_form";
    }

    // SAVE NEW EMPLOYEE
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute Employee employee,
                               @RequestParam BigDecimal salaryAmount) {

        // Logica simplă de salvare la creare
        Employee saved = employeeService.create(employee);

        Salary salary = new Salary();
        salary.setAmount(salaryAmount);
        salary.setStartDate(LocalDate.now());
        salary.setEndDate(null);
        salary.setEmployee(saved);

        salaryService.save(salary);

        return "redirect:/employees";
    }

    // SHOW EDIT FORM
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id: " + id));

        model.addAttribute("employee", employee);
        model.addAttribute("contracts", contractService.getAll());
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("positions", positionService.getAll());
        model.addAttribute("roles", roleService.getAll());
        model.addAttribute("employees", employeeService.getAll()); // manager dropdown

        // Salariul actual
        BigDecimal currentSalary = salaryService.getCurrentSalary(employee.getId())
                .map(Salary::getAmount)
                .orElse(BigDecimal.ZERO);
        model.addAttribute("currentSalary", currentSalary);

        model.addAttribute("pageTitle", "Update Employee");

        return "employee_update";
    }


    // UPDATE EMPLOYEE - VARIANTA CORECTATĂ (FIX PENTRU EROAREA 500)
    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute Employee employeeFromForm,
                                 @RequestParam(required = false) BigDecimal salaryAmount) {

        // 1. Încărcăm angajatul real din baza de date
        Employee existingEmployee = employeeService.getById(employeeFromForm.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id"));

        // 2. Copiem manual datele simple (Text)
        existingEmployee.setFirstName(employeeFromForm.getFirstName());
        existingEmployee.setLastName(employeeFromForm.getLastName());
        existingEmployee.setEmail(employeeFromForm.getEmail());

        // 3. FIX RELAȚII: Încărcăm entitățile reale din DB pe baza ID-urilor din form
        // Aceasta rezolvă eroarea "TransientPropertyValueException"

        // --- Manager ---
        if (employeeFromForm.getManager() != null && employeeFromForm.getManager().getId() != null) {
            Employee realManager = employeeService.getById(employeeFromForm.getManager().getId()).orElse(null);
            existingEmployee.setManager(realManager);
        } else {
            existingEmployee.setManager(null);
        }

        // --- Department ---
        if (employeeFromForm.getDepartment() != null && employeeFromForm.getDepartment().getId() != null) {
            Department realDept = departmentService.getById(employeeFromForm.getDepartment().getId()).orElse(null);
            existingEmployee.setDepartment(realDept);
        } else {
            existingEmployee.setDepartment(null);
        }

        // --- Position ---
        if (employeeFromForm.getPosition() != null && employeeFromForm.getPosition().getId() != null) {
            Position realPos = positionService.getById(employeeFromForm.getPosition().getId()).orElse(null);
            existingEmployee.setPosition(realPos);
        } else {
            existingEmployee.setPosition(null);
        }

        // --- Role ---
        if (employeeFromForm.getRole() != null && employeeFromForm.getRole().getId() != null) {
            Role realRole = roleService.getById(employeeFromForm.getRole().getId()).orElse(null);
            existingEmployee.setRole(realRole);
        } else {
            existingEmployee.setRole(null);
        }

        // --- Contract ---
        if (employeeFromForm.getContract() != null && employeeFromForm.getContract().getId() != null) {
            Contract realContract = contractService.getById(employeeFromForm.getContract().getId()).orElse(null);
            existingEmployee.setContract(realContract);
        } else {
            existingEmployee.setContract(null);
        }

        // 4. Salvăm angajatul cu relațiile corecte
        Employee saved = employeeService.update(existingEmployee);

        // 5. Logica de Salariu (Verificăm dacă salaryAmount nu e null)
        if (salaryAmount != null) {
            Optional<Salary> currentSalaryOpt = salaryService.getCurrentSalary(saved.getId());

            if (currentSalaryOpt.isPresent()) {
                Salary oldSalary = currentSalaryOpt.get();

                // Verificăm dacă salariul s-a schimbat
                if (oldSalary.getAmount().compareTo(salaryAmount) != 0) {
                    // A. Închidem salariul vechi
                    oldSalary.setEndDate(LocalDate.now());
                    salaryService.save(oldSalary);

                    // B. Creăm salariul nou
                    Salary newSalary = new Salary();
                    newSalary.setAmount(salaryAmount);
                    newSalary.setStartDate(LocalDate.now());
                    newSalary.setEndDate(null); // Activ
                    newSalary.setEmployee(saved);
                    salaryService.save(newSalary);
                }
            } else {
                // Nu avea salariu deloc, creăm unul nou
                Salary newSalary = new Salary();
                newSalary.setAmount(salaryAmount);
                newSalary.setStartDate(LocalDate.now());
                newSalary.setEmployee(saved);
                salaryService.save(newSalary);
            }
        }

        return "redirect:/employees";
    }


    // VIEW EMPLOYEE DETAILS
    @GetMapping("/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {

        Employee employee = employeeService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));

        BigDecimal currentSalary = salaryService.getCurrentSalary(employee.getId())
                .map(Salary::getAmount)
                .orElse(BigDecimal.ZERO);

        List<Salary> salaryHistory = salaryService.getByEmployeeId(employee.getId());

        model.addAttribute("employee", employee);
        model.addAttribute("currentSalary", currentSalary);
        model.addAttribute("salaryHistory", salaryHistory);
        model.addAttribute("pageTitle", employee.getFirstName() + " " + employee.getLastName());

        return "employee_view";
    }

    // ȘTERGERE ANGAJAT
    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
        return "redirect:/employees";
    }
}