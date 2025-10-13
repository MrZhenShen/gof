package practice.design.behavioral.command.solution.command;

import practice.design.behavioral.command.solution.provider.GarageDoor;
import practice.design.behavioral.command.solution.service.Remote;

public class ToggleGarageDoorCommand extends Command<Boolean> implements ToggleCommand {

    public ToggleGarageDoorCommand(Remote remote) {
        super(remote);
    }

    @Override
    public void execute() {
        GarageDoor device = remote.garage;
        backup(device.isOpen());
        toggle(device.isOpen(), () -> device.close(), () -> device.open());
    }

    @Override
    public void undo() {
        if (backup == null)
            return;

        GarageDoor device = remote.garage;
        toggle(backup, () -> device.open(), () -> device.close());
    }
}
