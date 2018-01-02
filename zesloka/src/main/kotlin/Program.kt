import db.DataBase
import eventservice.ClientConnectionManager
import eventservice.consoleclient.ConsoleClient
import eventservice.remotecontrolserver.RemoteControlServer
import executor.*
import executor.command.Command
import executor.command.CommandProcessorManager


val sesion_id = "123"
val x = 100f
val y = 100f
val x2 = 1500f
val y2 = 600f


fun main(args: Array<String>) {
    val cmdProcessor = getCommandProcessor()
    //val dbConnection = DataBase.getConnection()
    val ccl = ClientConnectionManager.getInstance()
    val server = RemoteControlServer(8743)
    server.addClientConnectionListener(ccl)
    server.start()
    val console = ConsoleClient();
    console.addClientConnectionListener(ccl)
    console.start();
}

fun getCommandProcessor() :  Command.CommandProcessor {
    Command.initDefaultCommands()
    Command.initUserCommands()
    val processor = CommandProcessorManager.getInstance()
    processor.add(RemoteCommandExecutor.getInstance())
    return processor
}