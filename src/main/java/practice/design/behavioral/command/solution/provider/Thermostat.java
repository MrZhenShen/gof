package practice.design.behavioral.command.solution.provider;

public class Thermostat {
    private int temp = 22;

    public void setTemperature(int t) {
        temp = t;
        System.out.println("[Thermostat] temp=" + temp);
    }

    public int getTemperature() {
        return temp;
    }
}
