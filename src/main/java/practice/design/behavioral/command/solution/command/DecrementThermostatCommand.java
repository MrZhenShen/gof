package practice.design.behavioral.command.solution.command;

import practice.design.behavioral.command.solution.service.Remote;

public class DecrementThermostatCommand extends ThermostatCommand {

    public DecrementThermostatCommand(Remote remote) {
        super(remote);
    }

    @Override
    public void execute() {
        int currentValue = remote.thermostat.getTemperature();

        backup(currentValue);

        if (currentValue > 0)
            remote.thermostat.setTemperature(--currentValue);
    }
}
