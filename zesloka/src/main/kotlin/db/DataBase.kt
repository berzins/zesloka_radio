package db

class DataBase {
    companion object {
        fun getConnection() : DBConecton? {
            val conData = DBConecton.DBConnectionData(
                    "user",
                    "parolee",
                    "zesloka.tk",
                    3306
            )
            return DBConecton.newInstance(conData)
        }
    }
}