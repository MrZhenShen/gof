package practice.design.behavioral.command.service;

import java.util.EnumMap;
import java.util.Map;

import practice.design.behavioral.command.command.Command;
import practice.design.behavioral.command.command.DecrementThermostatCommand;
import practice.design.behavioral.command.command.IncrementThermostatCommand;
import practice.design.behavioral.command.command.ToggleGarageDoorCommand;
import practice.design.behavioral.command.command.ToggleLightCommand;
import practice.design.behavioral.command.enums.RemoteButton;
import practice.design.behavioral.command.provider.GarageDoor;
import practice.design.behavioral.command.provider.Light;
import practice.design.behavioral.command.provider.Thermostat;
import practice.design.behavioral.command.util.Action;

public class Remote {
    private final Map<RemoteButton, Action> buttons;
    private final CommandHistory history;

    public final Light light;
    public final Thermostat thermostat;
    public final GarageDoor garage;

    public Remote() {
        light = new Light();
        thermostat = new Thermostat();
        garage = new GarageDoor();
        history = new CommandHistory();
        buttons = new EnumMap<>(Map.of(
                RemoteButton.BUTTON_1, () -> executeCommand(new ToggleGarageDoorCommand(this)),
                RemoteButton.BUTTON_2, () -> executeCommand(new ToggleLightCommand(this)),
                RemoteButton.BUTTON_3, () -> executeCommand(new IncrementThermostatCommand(this)),
                RemoteButton.BUTTON_4, () -> executeCommand(new DecrementThermostatCommand(this)),
                RemoteButton.UNDO, () -> undo(),
                RemoteButton.REDO, () -> redo()));
    }

    public void pressButton(RemoteButton button) {
        Action action = buttons.get(button);
        if (action != null)
            action.execute();
    }

    private void executeCommand(Command<?> command) {
        command.execute();
        history.push(command);
    }

    private void undo() {
        if (history.isEmpty())
            return;

        Command<?> command = history.pop();
        if (command != null) {
            command.undo();
        }
    }

    private void redo() {
        if (history.noFuture())
            return;

        Command<?> command = history.popFuture();
        if (command != null) {
            command.execute();
        }
    }
}
