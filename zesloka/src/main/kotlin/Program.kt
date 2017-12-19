import db.DataBase
import remotecontrolserver.ClientConnection
import remotecontrolserver.RemoteControlServer
import executor.*
import executor.command.Command
import executor.command.CommandProcessorManager
import executor.command.CommandRecorderManager
import utilities.TimeUtils

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

    while(true) {
        cmdProcessor.processCommand(readLine())
        println(TimeUtils.getCurrentTimeString())
    }
}

fun getCommandProcessor() :  Command.CommandProcessor {
    Command.initDefaultCommands()
    val processor = CommandProcessorManager.getInstance()
    processor.add(RemoteCommandExecutor.getInstance())
    return processor
}