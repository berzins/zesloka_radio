import db.DataBase
import remotecontrolserver.ClientConnection
import remotecontrolserver.RemoteControlServer
import executor.*

fun main(args: Array<String>) {
    val rce = RemoteCommandExecutorManager.getInstance()
    class SuperDuperMsgListener : ClientConnection.MessageListener {
        override fun onMessage(cmd: String) {
            println("msg received : " + cmd.toString())
            rce.processCommand(cmd)
        }
    }
    val dbConnection = DataBase.getConnection()
    val clientConnection = ClientConnection()
    clientConnection.addMsgListener(SuperDuperMsgListener())
    val server = RemoteControlServer(8743)
    server.addClientConnectionListener(clientConnection)
    server.start()
}