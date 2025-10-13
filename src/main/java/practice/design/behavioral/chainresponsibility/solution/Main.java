package practice.design.behavioral.chainresponsibility.solution;

import practice.design.behavioral.chainresponsibility.solution.model.Category;
import practice.design.behavioral.chainresponsibility.solution.model.ExpenseRequest;
import practice.design.behavioral.chainresponsibility.solution.service.ExpenseService;

public class Main {
    public static void main(String[] args) {
        var svc = new ExpenseService();

        System.out.println(svc.approve(new ExpenseRequest("e-1", 35_000, Category.GENERAL, "Team offsite snacks")));
        System.out.println(svc.approve(new ExpenseRequest("e-2", 120_000, Category.TRAVEL, "Flights")));
        System.out.println(svc.approve(new ExpenseRequest("e-3", 450_000, Category.HARDWARE, "New laptops")));
        System.out.println(svc.approve(new ExpenseRequest("e-4", 2_500_000, Category.GENERAL, "Booth")));
    }
}
