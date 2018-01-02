import eventservice.ClientConnectionManager
import eventservice.EventServiceFacade
import eventservice.client.ConsoleClient
import eventservice.client.RemoteControlServer
import eventservice.client.TestCaseClient
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

    val clients = arrayOf(
            RemoteControlServer(8743),
            ConsoleClient(),
            TestCaseClient()
    )

    for((i, value) in clients.withIndex()) {
        value.addClientConnectionListener(ccl)
        value.start()
    }
}

fun getCommandProcessor() :  Command.CommandProcessor {
    Command.initDefaultCommands()
    Command.initUserCommands()
    val processor = CommandProcessorManager.getInstance()
    processor.add(RemoteCommandExecutor.getInstance())
    return processor
}