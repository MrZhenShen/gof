package practice.design.structural.facade.solution.service;

public class Billing {
    void charge(String accountId, long cents) {
        System.out.println("[billing] " + accountId + " +" + cents);
    }
}