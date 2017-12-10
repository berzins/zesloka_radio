import db.DataBase
import remotecontrolserver.RemoteControlServer

fun main(args: Array<String>) {
    val dbConnection = DataBase.getConnection()
    println(dbConnection.toString())
    val server = RemoteControlServer(123)
    server.start()


}