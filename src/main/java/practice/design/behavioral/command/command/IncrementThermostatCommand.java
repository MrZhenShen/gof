package practice.design.behavioral.command.command;

import practice.design.behavioral.command.service.Remote;

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
