import db.DataBase
import remotecontrolserver.ClientConnection
import remotecontrolserver.RemoteControlServer
import executor.*
import executor.command.Command
import executor.command.CommandProcessorManager
import utilities.TimeUtils


val sesion_id = "123"
val x = 100f
val y = 100f
val x2 = 1500f
val y2 = 600f
val testCommands = arrayOf(
        "cmd_test",
        "cmd_recorder_set?cmd_recorder_set__session_id=$sesion_id&cmd_recorder_set__cmd_key=cmd_user_1&cmd_recorder_set__cmd_name=first user defined command",
        "cmd_recorder_start?cmd_recorder_start__session_id=$sesion_id",
//        "cmd_mouse_move_to?robot_cmd_mouse_move_to__x=$x&robot_cmd_mouse_move_to__y=$y",
//        "cmd_mouse_click_3",
        "cmd_mouse_move_to?robot_cmd_mouse_move_to__x=$x2&robot_cmd_mouse_move_to__y=$y2",
        "cmd_mouse_click_3",
        "cmd_recorder_stop?cmd_recorder_stop__session_id=$sesion_id",
        "cmd_recorder_store?cmd_recorder_store__session_id=$sesion_id"
)


fun executeCommands(timeout: Long, cmdProcessor: Command.CommandProcessor) {
    testCommands.forEach { str ->
        Thread.sleep(timeout)
        cmdProcessor.processCommand(str)
    }
}

fun main(args: Array<String>) {
    val cmdProcessor = getCommandProcessor()
    class SuperDuperMsgListener : ClientConnection.MessageListener {
        override fun onMessage(cmd: String) {
            println("msg received : " + cmd)
            cmdProcessor.processCommand(cmd)
        }
    }
    val dbConnection = DataBase.getConnection()
    val clientConnection = ClientConnection()
    clientConnection.addMsgListener(SuperDuperMsgListener())
    val server = RemoteControlServer(8743)
    server.addClientConnectionListener(clientConnection)
    server.start()



    //executeCommands(1000, cmdProcessor)

    while(true) {
        cmdProcessor.processCommand(readLine())
        println(TimeUtils.getCurrentTimeString())
    }
}

fun getCommandProcessor() :  Command.CommandProcessor {
    Command.initDefaultCommands()
    Command.initUserCommands()
    val processor = CommandProcessorManager.getInstance()
    processor.add(RemoteCommandExecutor.getInstance())
    return processor
}