package practice.design.behavioral.command.command;

import practice.design.behavioral.command.util.Action;

public interface ToggleCommand {
    default void toggle(
            boolean state,
            Action trufyAction,
            Action falsyAction) {
        if (state)
            trufyAction.execute();
        else
            falsyAction.execute();
    }
}
