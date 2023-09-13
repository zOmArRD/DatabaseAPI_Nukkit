/*
 * Created by IntelliJ Idea
 *
 * User: zOmArRD
 * Date: 12/9/2023
 *
 * Copyright (c) 2023. OMY TECHNOLOGY by <dev@zomarrd.me>
 */
package dev.zomarrd.dbapi.mysql

import cn.nukkit.Server
import dev.zomarrd.dbapi.Database
import dev.zomarrd.dbapi.SQLConnectionInfo

import java.sql.DriverManager
import java.sql.SQLException

class MySQL(info: SQLConnectionInfo) : Database(info) {
    override fun connect(): Boolean {
        return try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            connection = DriverManager.getConnection(connectInfo.connectionString)
            connection?.createStatement()
            true
        } catch (ex: SQLException) {
            Server.getInstance().logger.error("[DATABASE]" + ex.message)
            false
        } catch (ex: ClassNotFoundException) {
            Server.getInstance().logger.error("[DATABASE]$ex")
            false
        }
    }
}
