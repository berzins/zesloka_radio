//package executor.command;
//
//import utilities.Util;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class RecordableCommand extends Command{
//
//
//    Map<Command, String> commandParams = new HashMap<>();
//
//    public RecordableCommand(String name, String key) {
//        super(name, key);
//    }
//
//    public void record(Command cmd, String params) {
//        Command c = Util.serializedCopy(cmd);
//        this.commands.add(c);
//        this.commandParams.put(c, params);
//    }
//
//    @Override
//    public void execute(String params) {
//        for(Command cmd : this.commands) {
//            cmd.executeAsync(this.commandParams.get(cmd));
//        }
//    }
//
//
//
//}
