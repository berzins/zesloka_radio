package db

import java.sql.*
import java.util.*

open class DBConnection() {
    companion object {
        fun newInstance(dbConProp: DBConnectionData) : DBConnection?{
            var dbCon : DBConnection? =  null
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance()

                val url = "mysql://localhost"

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

    fun isClosed() : Boolean {
        this.sqlConnection
        if(this.sqlConnection.isClosed) {
            return true
        }
        return false
    }



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
            get() = this[KEY_LOGIN] as String
        val password : String
            get() = this[KEY_PASSWORD] as String
        private val host : String
            get() = this[KEY_HOST] as String
        private val port : Int
            get() = this[KEY_PORT] as Int
        val url : String
        get() = "jdbc:mysql://$host:$port/?autoReconnect=true&useUnicode=yes"
    }

    open fun executeStatement(sql: String) : ResultSet {
        val stmt = sqlConnection.createStatement()
        return stmt.executeQuery(sql)
    }

    open fun getPreparedStatement(sql:String) : PreparedStatement {
        return sqlConnection.prepareStatement(sql)
    }

    fun selectDatabase(name: String) {
        executeStatement("use $name")
    }

    fun getRowCountOfTable(tableName:String) : ResultSet {
        val sql = "select count(*) from $tableName"
        return executeStatement(sql)
    }

    fun getSongsByIndecies(indecies: Array<Int> ) : ResultSet {

        var ids: String = ""
        var index = 1

        // generate ids sequence
        for(i: Int in indecies) {
            var end:String
            if(index != indecies.size) end = "," else end = ""
            ids = "$ids$i$end"
            index++
        }

        // create sql statement
        val sql = "select * from songs where " +
                "id in ($ids)"

        return executeStatement(sql)
    }

    fun findSongs(value: String, cols: Array<String>) : ResultSet {
        var sql = "select * from songs where"
        var index = 1
        for(col: String in cols) {
            var end:String
            if(index != cols.size) end = " or" else end = ""
            sql = "$sql $col like '%$value%'$end"
            index++
        }
        return executeStatement(sql)
    }

    fun close() {
        if(this.sqlConnection != null) { this.sqlConnection.close() }
    }
}

