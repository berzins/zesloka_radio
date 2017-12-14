package executor.command.robotcommands;

import executor.RemoteCommandExecutor;

import java.awt.*;

public class RobotMouseMoveCommand extends RobotCommand{

    private Float [] pos = new Float [2];

    public RobotMouseMoveCommand() {
        super("mouse move", RobotCommand.ROBOT_CMD_MOUSE_MOVE, 0); // key identifier dummy value.. not needed for move command
    }

    @Override
    public void execute(String params) {
        super.execute(params);
        pos[0] = parseX(params);
        pos[1] = parseY(params);
        if(validateParams(this.pos)) {
            int x = MouseInfo.getPointerInfo().getLocation().x + Math.round(pos[0]);
            int y = MouseInfo.getPointerInfo().getLocation().y + Math.round(pos[1]);
            RemoteCommandExecutor.getInstance().getRobot().mouseMove(x, y);
        }
    }

    public static Float parseX(String params) {
        return parseParam(params, 0);
    }

    public static Float parseY(String params) {
        return parseParam(params, 1);
    }

    private static Float parseParam(String params, int index) {
        if(params.contains(",")) {
            String [] vals = params.split(",");
            if(vals.length == 2) {
                return Float.valueOf(vals[index]);
            }
        }
        return null;
    }

    private static boolean validateParams(Float [] pos){
        for(Float i : pos) {
            if(i == null) {
                return false;
            }
        }
        return true;
    }
}
