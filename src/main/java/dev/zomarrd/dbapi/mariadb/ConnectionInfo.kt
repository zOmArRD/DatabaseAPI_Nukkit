/*
 * Created by IntelliJ Idea
 *
 * User: zOmArRD
 * Date: 12/9/2023
 *
 * Copyright (c) 2023. OMY TECHNOLOGY by <dev@zomarrd.me>
 */
package dev.zomarrd.dbapi.mariadb

import dev.zomarrd.dbapi.SQLConnectionInfo

class ConnectionInfo @JvmOverloads constructor(
    private val ip: String,
    private val user: String,
    private val password: String,
    private val database: String,
    private val port: String = "3306",
) : SQLConnectionInfo {
    override fun getConnectionString(): String {
        return "jdbc:mariadb://$ip:$port/$database?user=$user&password=$password"
    }
}
