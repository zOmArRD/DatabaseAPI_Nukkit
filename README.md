# Documentation for `Database` Class

EXAMPLES IN THIS LINK

[CODE EXAMPLES](examples.md)

## Overview

The `Database` class is an abstract class that provides an interface for interacting with a database. It allows developers to connect to a database, perform asynchronous SQL queries and operations, and handle the results with ease. This documentation provides an in-depth understanding of the `Database` class and its available methods.

## Class Declaration

```kotlin
package dev.zomarrd.dbapi

import dev.zomarrd.dbapi.utils.Pair
import dev.zomarrd.dbapi.utils.ResultCallback
import dev.zomarrd.dbapi.utils.ResultOrError
import java.sql.Connection
import java.sql.SQLException
import java.util.*
import java.util.concurrent.Executors

abstract class Database(@JvmField protected var connectInfo: SQLConnectionInfo) 
// ...

```
### Constructor

- `Database(connectInfo: SQLConnectionInfo)`: Initializes a `Database` object with the provided connection information.

## Properties

- `connectInfo`: Connection information for the database.

## Methods

### `connect()`

```kotlin
abstract fun connect(): Boolean
```
- Connects to the database using the information provided in `connectInfo`.

### `close()`

```kotlin
fun close()
```
- Asynchronously closes the current connection to the database.

### `query(query: String)`

```kotlin
fun query(query: String)
```
- Asynchronously executes an SQL query in the database.

### `query(query: String, callback: ((ResultOrError<Any?>) -> Unit)?)`

```kotlin
fun query(query: String, callback: ((ResultOrError<Any?>) -> Unit)?)
```
- Asynchronously executes an SQL query in the database and provides a callback to handle the result.

### `query(query: String, callback: ResultCallback<Any?>?)`

```kotlin
fun query(query: String, callback: ResultCallback<Any?>?)
```
- Asynchronously executes an SQL query in the database and provides a callback to handle the result.

### `select(table: String, where: String, selector: String)`

```kotlin
fun select(table: String, where: String, selector: String)
```
- Performs a SELECT query on the database asynchronously.

### `select(table: String, where: String, selector: String, callback: ((ResultOrError<Any?>) -> Unit)?)`

```kotlin
fun select(table: String, where: String, selector: String, callback: ((ResultOrError<Any?>) -> Unit)?)
```
- Performs a SELECT query on the database asynchronously and provides a callback to handle the result.

### `select(table: String, where: String, selector: String, callback: ResultCallback<Any?>?)`

```kotlin
fun select(table: String, where: String, selector: String, callback: ResultCallback<Any?>?)
```
- Performs a SELECT query on the database asynchronously and provides a callback to handle the result.

### `insert(table: String, vararg pairs: Pair)`

```kotlin
fun insert(table: String, vararg pairs: Pair)
```
- Asynchronously inserts a new record into the specified table with the provided column-value pairs.

### `insert(table: String, vararg pairs: Pair, callback: ((ResultOrError<Any?>) -> Unit)?)`

```kotlin
fun insert(table: String, vararg pairs: Pair, callback: ((ResultOrError<Any?>) -> Unit)?)
```
- Asynchronously inserts a new record into the specified table with the provided column-value pairs and provides a callback to handle the result.

### `insert(table: String, vararg pairs: Pair, callback: ResultCallback<Any?>?)`

```kotlin
fun insert(table: String, vararg pairs: Pair, callback: ResultCallback<Any?>?)
```
- Asynchronously inserts a new record into the specified table with the provided column-value pairs and provides a callback to handle the result.

### `update(table: String, where: String, selector: String, vararg pairs: Pair)`

```kotlin
fun update(table: String, where: String, selector: String, vararg pairs: Pair)
```
- Asynchronously updates records in the specified table based on a WHERE clause with a given selector.

### `update(table: String, where: String, selector: String, vararg pairs: Pair, callback: ((ResultOrError<Any?>) -> Unit)?)`

```kotlin
fun update(
table: String,
where: String,
selector: String,
vararg pairs: Pair,
callback: ((ResultOrError<Any?>) -> Unit)?
)
```
- Asynchronously updates records in the specified table based on a WHERE clause with a given selector and provides a callback to handle the result.

### `update(table: String, where: String, selector: String, vararg pairs: Pair, callback: ResultCallback<Any?>?)`

```kotlin
fun update(
table: String,
where: String,
selector: String,
vararg pairs: Pair,
callback: ResultCallback<Any?>?
)
```
- Asynchronously updates records in the specified table based on a WHERE clause with a given selector and provides a callback to handle the result.

## Usage

EXAMPLES IN THIS LINK

[CODE EXAMPLES](examples.md)

Developers can utilize the `Database` class to interact with their database systems efficiently. It allows for asynchronous execution of queries and provides callbacks for handling results or errors, making it a versatile tool for database operations in their applications.

Please note that this documentation provides an overview of the `Database` class and its methods. Developers should refer to the class implementation and their specific database documentation for more details on usage and configuration.

## Credits

Inspired by [sqlNukkitLib](https://github.com/Ragnok123/sqlNukkitLib)