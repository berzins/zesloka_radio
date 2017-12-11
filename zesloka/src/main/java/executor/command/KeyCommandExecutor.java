package executor.command;

import executor.RemoteCommandExecutorManager;

import java.util.ArrayList;
import java.util.List;

public class CommandExecutor {

    CommandExecutor() {
        commands = new ArrayList<>();
    }

    private List<RobotCommand> commands;

    public void addRobotCommand(RobotCommand command) {
        this.commands.add(command);
    }

    public void execute() throws InterruptedException {
        for(RobotCommand cmd : commands) {
            cmd.execute();
        }
    }



    public abstract class RobotCommand {
        Integer event;
        Integer timeout = 0;

        public RobotCommand(Integer event) {
            this.event = event;
        }

        public void execute() {
            if(timeout > 0) {
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class RobotKeyPressCommand extends RobotCommand {

        public RobotKeyPressCommand(Integer event) {
            super(event);
        }

        @Override
        public void execute() {
            super.execute();
            RemoteCommandExecutorManager.getInstance().getRobot().keyPress(this.event);
        }
    }

    public class RobotKeyReleaseCommand extends RobotCommand {

        public RobotKeyReleaseCommand(Integer event) {
            super(event);
        }

        @Override
        public void execute() {
            super.execute();
            RemoteCommandExecutorManager.getInstance().getRobot().keyRelease(this.event);
        }
    }

    public class RobotMousePressCommand extends  RobotCommand {

        public RobotMousePressCommand(Integer event) {
            super(event);
        }

        @Override
        public void execute() {
            super.execute();
            RemoteCommandExecutorManager.getInstance().getRobot().mousePress(this.event);
        }
    }

    public class RobotMouseReleaseCommand extends RobotCommand {

        public RobotMouseReleaseCommand(Integer event) {
            super(event);
        }

        @Override
        public void execute() {
            super.execute();
            RemoteCommandExecutorManager.getInstance().getRobot().mouseRelease(this.event);
        }
    }

    public class RobotMouseMoveCommand extends RobotCommand{

        private Integer x, y;

        private RobotMouseMoveCommand(Integer event) {
            super(event);
        }

        public RobotMouseMoveCommand(int x, int y) {
            this(0); // dummy value.. not needed for this case
            this.x = x;
            this.y = y;
        }

        @Override
        public void execute() {
            super.execute();
            RemoteCommandExecutorManager.getInstance().getRobot().mouseMove(x, y);
        }
    }
}
