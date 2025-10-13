package practice.design.behavioral.command.solution.service;

import java.util.EnumMap;
import java.util.Map;

import practice.design.behavioral.command.solution.command.Command;
import practice.design.behavioral.command.solution.command.DecrementThermostatCommand;
import practice.design.behavioral.command.solution.command.IncrementThermostatCommand;
import practice.design.behavioral.command.solution.command.ToggleGarageDoorCommand;
import practice.design.behavioral.command.solution.command.ToggleLightCommand;
import practice.design.behavioral.command.solution.enums.RemoteButton;
import practice.design.behavioral.command.solution.provider.GarageDoor;
import practice.design.behavioral.command.solution.provider.Light;
import practice.design.behavioral.command.solution.provider.Thermostat;
import practice.design.behavioral.command.solution.util.Action;

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
