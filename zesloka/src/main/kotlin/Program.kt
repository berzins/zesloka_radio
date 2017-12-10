import db.DataBase
import remotecontrolserver.ClientConnection
import remotecontrolserver.RemoteControlServer
import executor.*

fun main(args: Array<String>) {
    val rce = RemoteCommandExecutor.getInstance()
    class SuperDuperMsgListener : ClientConnection.MessageListener {
        override fun onMessage(cmd: RemoteCommandExecutor.Command?) {
            rce.processCommand(cmd)
        }
    }
    val dbConnection = DataBase.getConnection()
    val clientConnection = ClientConnection()
    clientConnection.addMsgListener(SuperDuperMsgListener())
    val server = RemoteControlServer(123)
    server.addClientConnectionListener(clientConnection)
    server.start()
}