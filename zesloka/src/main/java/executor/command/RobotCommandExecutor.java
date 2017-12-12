package executor.command;

import executor.RemoteCommandExecutorManager;
import executor.command.robotcommands.RobotCommand;

import java.util.ArrayList;
import java.util.List;

public class RobotCommandExecutor {

    RobotCommandExecutor() {
        commands = new ArrayList<>();
    }

    private List<RobotCommand> commands;

    public void addRobotCommand(RobotCommand command) {
        this.commands.add(command);
    }

    public void execute() {
        for(RobotCommand cmd : commands) {
            cmd.execute();
        }
    }












}
