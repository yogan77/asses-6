package com.access;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AccessSystemTest {

    @Test
    public void testPositive_FullyEligibleEmployee() {
        Employee emp = new Employee("E001", "John Doe", 25, "IT", 3, true, true);
        EvaluationResult result = EligibilityEvaluator.evaluateAccess(emp, 2);
        assertEquals(EvaluationResult.Status.ELIGIBLE, result.getStatus());
        assertTrue(result.getReasons().isEmpty());
    }

    @Test
    public void testPositive_BoundaryAgeExactly21() {
        Employee emp = new Employee("E002", "Jane Smith", 21, "HR", 2, true, true);
        EvaluationResult result = EligibilityEvaluator.evaluateAccess(emp, 2);
        assertEquals(EvaluationResult.Status.ELIGIBLE, result.getStatus());
    }

    @Test
    public void testPositive_CaseInsensitiveDepartment() {
        Employee emp = new Employee("E003", "Bob Ross", 30, "finance", 2, true, true);
        EvaluationResult result = EligibilityEvaluator.evaluateAccess(emp, 2);
        assertEquals(EvaluationResult.Status.ELIGIBLE, result.getStatus());
    }

    @Test
    public void testConditional_InsufficientClearance() {
        Employee emp = new Employee("E004", "Alice Paul", 28, "Administration", 1, true, true);
        EvaluationResult result = EligibilityEvaluator.evaluateAccess(emp, 3);
        assertEquals(EvaluationResult.Status.CONDITIONALLY_ELIGIBLE, result.getStatus());
        assertEquals(1, result.getReasons().size());
    }

    @Test
    public void testNegative_UnderageRejection() {
        Employee emp = new Employee("E005", "Billy Kid", 20, "IT", 2, true, true);
        EvaluationResult result = EligibilityEvaluator.evaluateAccess(emp, 2);
        assertEquals(EvaluationResult.Status.NOT_ELIGIBLE, result.getStatus());
        assertTrue(result.getReasons().contains("Age is below 21."));
    }

    @Test
    public void testNegative_UnauthorizedDepartment() {
        Employee emp = new Employee("E006", "Mark Davis", 35, "Marketing", 2, true, true);
        EvaluationResult result = EligibilityEvaluator.evaluateAccess(emp, 2);
        assertEquals(EvaluationResult.Status.NOT_ELIGIBLE, result.getStatus());
        assertTrue(result.getReasons().contains("Department is not authorized."));
    }

    @Test
    public void testNegative_InactiveOrInvalidId() {
        Employee emp = new Employee("E007", "Terminated User", 40, "HR", 2, false, false);
        EvaluationResult result = EligibilityEvaluator.evaluateAccess(emp, 2);
        assertEquals(EvaluationResult.Status.NOT_ELIGIBLE, result.getStatus());
        assertEquals(2, result.getReasons().size()); 
    }

    @Test
    public void testNegative_MultipleSimultaneousFailures() {
        Employee emp = new Employee("E008", "Ghost", 17, "Sales", 0, false, false);
        EvaluationResult result = EligibilityEvaluator.evaluateAccess(emp, 3);
        assertEquals(EvaluationResult.Status.NOT_ELIGIBLE, result.getStatus());
        assertEquals(4, result.getReasons().size()); 
    }

    @Test
    public void testNegative_ConstructorInvalidData() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Employee("", "Valid Name", 25, "IT", 1, true, true);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Employee("E009", "Valid Name", -1, "IT", 1, true, true);
        });
    }

    @Test
    public void testNegative_NullEmployeeEvaluation() {
        assertThrows(IllegalArgumentException.class, () -> {
            EligibilityEvaluator.evaluateAccess(null, 2);
        });
    }
}
