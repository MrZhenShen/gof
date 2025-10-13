package practice.design.behavioral.command.solution.command;

import practice.design.behavioral.command.solution.service.Remote;

public abstract class ThermostatCommand extends Command<Integer> {

    public ThermostatCommand(Remote remote) {
        super(remote);
    }

    @Override
    public void undo() {
        if (backup == null)
            return;
        remote.thermostat.setTemperature(backup);
    }
}