package db

class DataBase  {



    companion object {

        var conn : DBConnection? =  null

        fun getConnection() : DBConnection? {
            if(conn == null) {
                val conData = DBConnection.DBConnectionData(
                        "user",
                        "parolee",
                        "zesloka.tk",
                        3306
                )
                conn = DBConnection.newInstance(conData)
            }
            return conn
        }
    }
}