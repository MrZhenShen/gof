package practice.design.behavioral.command.solution.command;

import practice.design.behavioral.command.solution.service.Remote;

public class IncrementThermostatCommand extends ThermostatCommand {

    public IncrementThermostatCommand(Remote remote) {
        super(remote);
    }

    @Override
    public void execute() {
        int currentValue = remote.thermostat.getTemperature();

        backup(currentValue);

        remote.thermostat.setTemperature(++currentValue);
    }
}
