package executor.command.robotcommands;

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
