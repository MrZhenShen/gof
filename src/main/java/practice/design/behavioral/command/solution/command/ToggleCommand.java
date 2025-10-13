package practice.design.behavioral.command.solution.command;

import practice.design.behavioral.command.solution.util.Action;

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
