/*
 * Created by IntelliJ Idea
 *
 * User: zOmArRD
 * Date: 12/9/2023
 *
 * Copyright (c) 2023. OMY TECHNOLOGY by <dev@zomarrd.me>
 */
package dev.zomarrd.dbapi

import dev.zomarrd.dbapi.utils.Pair
import dev.zomarrd.dbapi.utils.ResultCallback
import dev.zomarrd.dbapi.utils.ResultOrError
import java.sql.Connection
import java.sql.SQLException
import java.util.*
import java.util.concurrent.Executors

/**
 * An abstract class that provides an interface for interacting with a database.
 *
 * @property connectInfo Connection information for the database.
 */
abstract class Database(@JvmField protected var connectInfo: SQLConnectionInfo) {
    private val asyncQuery = Executors.newCachedThreadPool()

    @JvmField
    protected var connection: Connection? = null

    /**
     * Connects to the database using the information provided in [connectInfo].
     *
     * @return `true` if the connection was successful, `false` otherwise.
     */
    abstract fun connect(): Boolean

    /**
     * Asynchronously closes the current connection to the database.
     */
    fun close() {
        asyncQuery.execute {
            try {
                connection!!.close()
            } catch (ignored: SQLException) {
            }
        }
    }

    private fun executeQuery(query: String) {
        val statement = connection!!.prepareStatement(query)
        statement.executeUpdate()
        statement.close()
    }

    /**
     * Asynchronously executes an SQL query in the database.
     *
     * @param query SQL query to execute.
     */
    fun query(query: String) {
        asyncQuery.execute {
            try {
                executeQuery(query)
            } catch (_: SQLException) {
            }
        }
    }

    /**
     * Asynchronously executes an SQL query in the database and provides a callback to handle the result.
     *
     * @param query SQL query to execute.
     * @param callback Callback called with the result of the query.
     */
    fun query(query: String, callback: ((ResultOrError<Any?>) -> Unit)?) {
        asyncQuery.execute {
            try {
                executeQuery(query)
                callback?.invoke(ResultOrError(null))
            } catch (ex: SQLException) {
                callback?.invoke(ResultOrError(error = ex))
            }
        }
    }

    /**
     * Asynchronously executes an SQL query in the database and provides a callback to handle the result.
     *
     * @param query SQL query to execute.
     * @param callback Callback called with the result of the query.
     */
    fun query(query: String, callback: ResultCallback<Any?>?) {
        asyncQuery.execute {
            try {
                executeQuery(query)
                callback?.success(null)
            } catch (ex: SQLException) {
                callback?.error(ex)
            }
        }
    }

    private fun executeSelect(table: String, where: String, selector: String): HashMap<String, Any> {
        val query = "SELECT * FROM `$table` WHERE `$where` = ?"

        val statement = connection!!.prepareStatement(query)
        statement.setString(1, selector)

        val result = statement.executeQuery()
        val md = result.metaData
        val columns = md.columnCount
        val row = HashMap<String, Any>()

        while (result.next()) {
            for (i in 1..columns) {
                val columnName = md.getColumnName(i)
                val columnValue = result.getObject(i)
                row[columnName] = columnValue
            }
        }

        statement.close()
        result.close()

        return row
    }

    /**
     * Performs a SELECT query on the database asynchronously.
     *
     * @param table Name of the table to query.
     * @param where WHERE clause of the query.
     * @param selector Value to search for in the WHERE clause.
     */
    fun select(table: String, where: String, selector: String) {
        asyncQuery.execute {
            try {
                executeSelect(table, where, selector)
            } catch (_: SQLException) {
            }
        }
    }

    /**
     * Performs a SELECT query on the database asynchronously and provides a callback to handle the result.
     *
     * @param table Name of the table to query.
     * @param where WHERE clause of the query.
     * @param selector Value to search for in the WHERE clause.
     * @param callback Callback called with the result of the query.
     */
    fun select(table: String, where: String, selector: String, callback: ((ResultOrError<Any?>) -> Unit)?) {
        asyncQuery.execute {
            try {
                val rows: HashMap<String, Any> = executeSelect(table, where, selector)
                callback?.invoke(ResultOrError(rows))
            } catch (ex: SQLException) {
                callback?.invoke(ResultOrError(error = ex))
            }
        }
    }

    /**
     * Performs a SELECT query on the database asynchronously and provides a callback to handle the result.
     *
     * @param table Name of the table to query.
     * @param where WHERE clause of the query.
     * @param selector Value to search for in the WHERE clause.
     * @param callback Callback called with the result of the query.
     */
    fun select(table: String, where: String, selector: String, callback: ResultCallback<Any?>?) {
        asyncQuery.execute {
            try {
                val rows = executeSelect(table, where, selector)
                callback?.success(rows)
            } catch (ex: SQLException) {
                callback?.error(ex)
            }
        }
    }

    private fun executeInsert(table: String, vararg pairs: Pair) {
        val keysJoiner = StringJoiner(",")
        val valuesJoiner = StringJoiner(",")

        for (pair in pairs) {
            keysJoiner.add(pair.key)
            valuesJoiner.add("?")
        }

        val query = "INSERT INTO `$table` ($keysJoiner) VALUES ($valuesJoiner)"
        val statement = connection!!.prepareStatement(query)
        var paramIndex = 1

        for (pair in pairs) {
            statement.setObject(paramIndex++, pair.value)
        }

        statement.executeUpdate()
        statement.close()
    }

    /**
     * Asynchronously inserts a new record into the specified table with the provided column-value pairs.
     *
     * @param table Name of the table to insert the record into.
     * @param pairs Pairs of column names and corresponding values for the new record.
     */
    fun insert(table: String, vararg pairs: Pair) {
        asyncQuery.execute {
            try {
                executeInsert(table, *pairs)
            } catch (_: SQLException) {
            }
        }
    }

    /**
     * Asynchronously inserts a new record into the specified table with the provided column-value pairs and provides a callback to handle the result.
     *
     * @param table Name of the table to insert the record into.
     * @param pairs Pairs of column names and corresponding values for the new record.
     * @param callback Callback called upon the completion of the insertion operation.
     */
    fun insert(table: String, vararg pairs: Pair, callback: ((ResultOrError<Any?>) -> Unit)?) {
        asyncQuery.execute {
            try {
                executeInsert(table, *pairs)
                callback?.invoke(ResultOrError(null))
            } catch (ex: SQLException) {
                callback?.invoke(ResultOrError(error = ex))
            }
        }
    }

    /**
     * Asynchronously inserts a new record into the specified table with the provided column-value pairs and provides a callback to handle the result.
     *
     * @param table Name of the table to insert the record into.
     * @param pairs Pairs of column names and corresponding values for the new record.
     * @param callback Callback called upon the completion of the insertion operation.
     */
    fun insert(table: String, vararg pairs: Pair, callback: ResultCallback<Any?>?) {
        asyncQuery.execute {
            try {
                executeInsert(table, *pairs)
                callback?.success(null)
            } catch (ex: SQLException) {
                callback?.error(ex)
            }
        }
    }

    private fun executeUpdate(table: String, where: String, selector: String, vararg pairs: Pair) {
        val queryBuilder = StringBuilder()

        queryBuilder.run {
            append("UPDATE `").append(table).append("` SET ")

            for (i in pairs.indices) {
                append("`").append(pairs[i].key).append("` = ?")
                if (i < pairs.size - 1) {
                    append(", ")
                }
            }

            append(" WHERE `").append(where).append("` = ?")
        }

        val query = queryBuilder.toString()
        val statement = connection!!.prepareStatement(query)

        statement.run {
            var paramIndex = 1

            for (pair in pairs) {
                setObject(paramIndex++, pair.value)
            }

            setObject(paramIndex, selector)
            executeUpdate()
            close()
        }
    }

    /**
     * Asynchronously updates records in the specified table based on a WHERE clause with a given selector.
     *
     * @param table Name of the table to update.
     * @param where WHERE clause for filtering records to update.
     * @param selector Value used in the WHERE clause as a filter.
     * @param pairs Pairs of column names and values to update in the matching records.
     */
    fun update(table: String, where: String, selector: String, vararg pairs: Pair) {
        asyncQuery.execute {
            try {
                executeUpdate(table, where, selector, *pairs)
            } catch (_: SQLException) {
            }
        }
    }

    /**
     * Asynchronously updates records in the specified table based on a WHERE clause with a given selector and provides a callback to handle the result.
     *
     * @param table Name of the table to update.
     * @param where WHERE clause for filtering records to update.
     * @param selector Value used in the WHERE clause as a filter.
     * @param pairs Pairs of column names and values to update in the matching records.
     * @param callback Callback called upon the completion of the update operation.
     */
    fun update(
        table: String,
        where: String,
        selector: String,
        vararg pairs: Pair,
        callback: ((ResultOrError<Any?>) -> Unit)?,
    ) {
        asyncQuery.execute {
            try {
                executeUpdate(table, where, selector, *pairs)
                callback?.invoke(ResultOrError(null))
            } catch (ex: SQLException) {
                callback?.invoke(ResultOrError(error = ex))
            }
        }
    }

    /**
     * Asynchronously updates records in the specified table based on a WHERE clause with a given selector and provides a callback to handle the result.
     *
     * @param table Name of the table to update.
     * @param where WHERE clause for filtering records to update.
     * @param selector Value used in the WHERE clause as a filter.
     * @param pairs Pairs of column names and values to update in the matching records.
     * @param callback Callback called upon the completion of the update operation.
     */
    fun update(table: String, where: String, selector: String, vararg pairs: Pair, callback: ResultCallback<Any?>?) {
        asyncQuery.execute {
            try {
                executeUpdate(table, where, selector, *pairs)
                callback?.success(null)
            } catch (ex: SQLException) {
                callback?.error(ex)
            }
        }
    }
}