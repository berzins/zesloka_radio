package executor.command.utilcommands.recorder

import executor.RemoteCommandExecutor
import executor.command.Command
import executor.command.Command.CommandProcessor
import executor.command.CommandProcessorManager

class RecorderTest extends GroovyTestCase {


    void testExecute() {
        Command.initDefaultCommands()
        Command.CommandProcessor cmdProc = CommandProcessorManager.getInstance()
        cmdProc.add(RemoteCommandExecutor.getInstance())

        fail(processCommand("cmd_recorder_set|cmd", cmdProc))
        fail(processCommand("cmd_recorder_start|cmd", cmdProc))
                fail(processCommand("cmd_recorder_stop|cmd", cmdProc))
                        fail(processCommand("cmd_recorder_store|cmd", cmdProc))

    }
    void processCommand(String cmd, CommandProcessor cmdProc) {
        cmdProc.processCommand(cmd)
        try{
            Thread.sleep(1000)
        } catch(Exception e) {

        }
    }
}
