package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

class Employee {
    private final String employeeId;
    private final String name;
    private final int age;
    private final String department;
    private final int securityClearanceLevel;
    private final boolean isIdValid;
    private final boolean isActive;

    public Employee(String employeeId, String name, int age, String department, 
                    int securityClearanceLevel, boolean isIdValid, boolean isActive) {
        // Replaced .blank() with standard .trim().isEmpty() to support older JRE versions
        if (employeeId == null || employeeId.trim().isEmpty()) throw new IllegalArgumentException("Employee ID cannot be empty.");
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be empty.");
        if (age < 0) throw new IllegalArgumentException("Age cannot be negative.");
        if (department == null || department.trim().isEmpty()) throw new IllegalArgumentException("Department cannot be empty.");
        if (securityClearanceLevel < 0) throw new IllegalArgumentException("Security clearance level cannot be negative.");

        this.employeeId = employeeId;
        this.name = name;
        this.age = age;
        this.department = department;
        this.securityClearanceLevel = securityClearanceLevel;
        this.isIdValid = isIdValid;
        this.isActive = isActive;
    }

    public String getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getDepartment() { return department; }
    public int getSecurityClearanceLevel() { return securityClearanceLevel; }
    public boolean isIdValid() { return isIdValid; }
    public boolean isActive() { return isActive; }
}

class EvaluationResult {
    public enum Status { ELIGIBLE, CONDITIONALLY_ELIGIBLE, NOT_ELIGIBLE }
    
    private final Status status;
    private final List<String> reasons;

    public EvaluationResult(Status status, List<String> reasons) {
        this.status = status;
        this.reasons = new ArrayList<String>(reasons);
    }

    public Status getStatus() { return status; }
    public List<String> getReasons() { return reasons; }

    @Override
    public String toString() {
        return "Status: " + status + (reasons.isEmpty() ? "" : " | Reasons: " + reasons);
    }
}

class EligibilityEvaluator {
    // Standard initialization safe across basic Java compilers
    private static final Set<String> AUTH_DEPTS = new HashSet<String>(Arrays.asList("IT", "HR", "FINANCE", "ADMINISTRATION"));

    public static EvaluationResult evaluateAccess(Employee employee, int requiredLevel) {
        if (employee == null) throw new IllegalArgumentException("Employee cannot be null");
        
        List<String> reasons = new ArrayList<String>();

        if (employee.getAge() < 21) reasons.add("Age is below 21.");
        if (!AUTH_DEPTS.contains(employee.getDepartment().toUpperCase())) reasons.add("Department is not authorized.");
        if (!employee.isActive()) reasons.add("Employment status is inactive.");
        if (!employee.isIdValid()) reasons.add("Employee ID is invalid.");

        if (!reasons.isEmpty()) {
            return new EvaluationResult(EvaluationResult.Status.NOT_ELIGIBLE, reasons);
        }

        if (employee.getSecurityClearanceLevel() < requiredLevel) {
            return new EvaluationResult(EvaluationResult.Status.CONDITIONALLY_ELIGIBLE, 
                Arrays.asList("Insufficient clearance. Required: " + requiredLevel + ", Actual: " + employee.getSecurityClearanceLevel()));
        }

        return new EvaluationResult(EvaluationResult.Status.ELIGIBLE, new ArrayList<String>());
    }
}

public class AccessSystem {
    public static void main(String[] args) {
        System.out.println("System operational.");
    }
}
