package db

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

open class DBConnection() {
    companion object {
        fun newInstance(dbConProp: DBConnectionData) : DBConnection?{
            var dbCon : DBConnection? =  null
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance()
                val con = DriverManager.getConnection(dbConProp.url, dbConProp.login, dbConProp.password)
                dbCon =  DBConnection(con)
            } catch (ex: SQLException) {
                println("SQL EXCEPTION")
                ex.printStackTrace()
            } catch (ex: Exception) {
                println("EXCEPTION")
                ex.printStackTrace()
            }
            dbCon?.selectDatabase("radiodj")
            return dbCon
        }
    }

    constructor(sqlConnection : Connection) : this() {
        this.sqlConnection = sqlConnection
    }

    lateinit var sqlConnection : Connection
        private set

    class DBConnectionData(login: String, password:String, host : String, port : Int) : Properties() {

        companion object {
            val KEY_LOGIN = "login"
            val KEY_PASSWORD = "password"
            val KEY_URL = "url"
            val KEY_PORT = "port"
            val KEY_HOST = "host"
        }

        init {
            this.put(db.DBConnection.DBConnectionData.KEY_LOGIN, login)
            this.put(db.DBConnection.DBConnectionData.KEY_PASSWORD, password)
            this.put(db.DBConnection.DBConnectionData.KEY_HOST, host)
            this.put(db.DBConnection.DBConnectionData.KEY_PORT, port)
        }

        val login : String
            get() = this[db.DBConnection.DBConnectionData.KEY_LOGIN] as String
        val password : String
            get() = this[db.DBConnection.DBConnectionData.KEY_PASSWORD] as String
        private val host : String
            get() = this[db.DBConnection.DBConnectionData.KEY_HOST] as String
        private val port : Int
            get() = this[db.DBConnection.DBConnectionData.KEY_PORT] as Int
        val url : String
        get() = "jdbc:mysql://$host:$port/"
    }

    open fun executeStatement(sql: String) : ResultSet {
        val stmt = sqlConnection.createStatement()
        return stmt.executeQuery(sql);
    }

    fun selectDatabase(name: String) {
        executeStatement("use $name")
    }

    fun findSongsWhatIsLike(name: String) : ResultSet {
        val sql = "select * from songs where " +
                "artist like '%$name%' or " +
                "title like '%$name%' or " +
                "album like '%$name%'"
        return executeStatement(sql)
    }
}

