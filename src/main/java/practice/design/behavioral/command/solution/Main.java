package practice.design.behavioral.command.solution;

import practice.design.behavioral.command.solution.enums.RemoteButton;
import practice.design.behavioral.command.solution.service.Remote;

public class Main {
    public static void main(String[] args) {
        Remote remote = new Remote();

        remote.pressButton(RemoteButton.BUTTON_1);
        remote.pressButton(RemoteButton.BUTTON_1);
        remote.pressButton(RemoteButton.BUTTON_1);

        remote.pressButton(RemoteButton.BUTTON_2);
        remote.pressButton(RemoteButton.BUTTON_2);
        remote.pressButton(RemoteButton.BUTTON_2);

        remote.pressButton(RemoteButton.BUTTON_3);
        remote.pressButton(RemoteButton.BUTTON_3);
        remote.pressButton(RemoteButton.BUTTON_3);
        remote.pressButton(RemoteButton.BUTTON_3);
        remote.pressButton(RemoteButton.BUTTON_3);
        remote.pressButton(RemoteButton.BUTTON_3);

        remote.pressButton(RemoteButton.UNDO);
        remote.pressButton(RemoteButton.UNDO);
        remote.pressButton(RemoteButton.UNDO);

        remote.pressButton(RemoteButton.REDO);
        remote.pressButton(RemoteButton.REDO);
    }
}
