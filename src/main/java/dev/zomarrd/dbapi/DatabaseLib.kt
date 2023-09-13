/*
 * Created by IntelliJ Idea
 *
 * User: zOmArRD
 * Date: 12/9/2023
 *
 * Copyright (c) 2023. OMY TECHNOLOGY by <dev@zomarrd.me>
 */
package dev.zomarrd.dbapi

import dev.zomarrd.dbapi.mariadb.MariaDB
import dev.zomarrd.dbapi.mysql.MySQL

object DatabaseLib {
    /**
     * Initializes and returns an instance of a database based on the specified [type] and [info].
     *
     * @param type The type of SQL database to initialize (e.g., SQLType.MariaDB, SQLType.MySQL).
     * @param info The connection information required to connect to the database.
     * @return An instance of the specified database type.
     * @throws IllegalArgumentException if the [type] is not recognized or supported.
     */
    fun init(type: SQLType, info: SQLConnectionInfo): Database {
        return when (type) {
            SQLType.MariaDB -> MariaDB(info)
            SQLType.MySQL -> MySQL(info)
            else -> throw IllegalArgumentException("Unsupported database type: $type")
        }
    }
}