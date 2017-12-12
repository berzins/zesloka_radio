package db

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

class DBConecton() {
    companion object {
        fun newInstance(dbConProp: DBConnectionData) : DBConecton ?{
            var dbCon : DBConecton? =  null
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance()
                val con = DriverManager.getConnection(dbConProp.url, dbConProp.login, dbConProp.password)
                dbCon =  DBConecton(con)            } catch (ex: SQLException) {
                println("SQL EXCEPTION")
                ex.printStackTrace()
            } catch (ex: Exception) {
                println("EXCEPTION")
                ex.printStackTrace()
            }
            return dbCon
        }
    }

    constructor(sqlConnection : Connection) : this() {
        this.sqlConnection = sqlConnection
    }

    lateinit var sqlConnection : Connection
        public get
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
            this.put(db.DBConecton.DBConnectionData.KEY_LOGIN, login)
            this.put(db.DBConecton.DBConnectionData.KEY_PASSWORD, password)
            this.put(db.DBConecton.DBConnectionData.KEY_HOST, host)
            this.put(db.DBConecton.DBConnectionData.KEY_PORT, port)
        }

        val login : String
            get() = this[db.DBConecton.DBConnectionData.KEY_LOGIN] as String
        val password : String
            get() = this[db.DBConecton.DBConnectionData.KEY_PASSWORD] as String
        private val host : String
            get() = this[db.DBConecton.DBConnectionData.KEY_HOST] as String
        private val port : Int
            get() = this[db.DBConecton.DBConnectionData.KEY_PORT] as Int
        val url : String
        get() = "jdbc:mysql://$host:$port/"
    }
}

