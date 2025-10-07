package practice.design.behavioral.command.command;

import practice.design.behavioral.command.provider.Light;
import practice.design.behavioral.command.service.Remote;

public class ToggleLightCommand extends Command<Boolean> implements ToggleCommand {

    public ToggleLightCommand(Remote remote) {
        super(remote);
    }

    @Override
    public void execute() {
        Light device = remote.light;
        backup(device.isOn());
        toggle(device.isOn(), () -> device.turnOff(), () -> device.turnOn());
    }

    @Override
    public void undo() {
        if (backup == null)
            return;

        Light device = remote.light;
        toggle(backup, () -> device.turnOn(), () -> device.turnOff());
    }

}
