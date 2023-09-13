# Database API (NukkitX API 1.0.0)

The ultimate API for managing and connecting to your database

Please see the [documentation](documentation.md)

## What can I do with the API?

You can add the .jar to the server, and to work with the API, simply add it to your dependency.

You can also compile it together with the plugin, but well, that's up to you.

<br/>

### Configure the connection and start making your connections

##### Example, Java

```java
package dev.zomarrd;

import cn.nukkit.plugin.PluginBase;
import dev.zomarrd.dbapi.Database;
import dev.zomarrd.dbapi.DatabaseLib;
import dev.zomarrd.dbapi.SQLType;
import dev.zomarrd.dbapi.mariadb.ConnectionInfo;

public class connectExample extends PluginBase {
    @Override
    public void onEnable() {
        // I'm importing the ConnectionInfo from MariaDB
        // You can import the one you need or create your own methods.
        ConnectionInfo mariadbInfo = new ConnectionInfo("ip", "user", "pass", "scheme", "optional_port");
        Database database = DatabaseLib.INSTANCE.init(SQLType.MariaDB, mariadbInfo);

        // Connect to the database
        if (database.connect()) {
            getLogger().info("Working");
        } else {
            getLogger().info("No connection");
        }
    }
}
```

##### Example, Kotlin

```kotlin
package dev.zomarrd

import cn.nukkit.plugin.PluginBase
import dev.zomarrd.dbapi.DatabaseLib.init
import dev.zomarrd.dbapi.SQLType
import dev.zomarrd.dbapi.mariadb.ConnectionInfo

class connectExample : PluginBase() {
    override fun onEnable() {
        // I'm importing the ConnectionInfo from MariaDB
        // You can import the one you need or create your own methods.
        val mariadbInfo = ConnectionInfo("ip", "user", "pass", "scheme", "optional_port")
        val database = init(SQLType.MariaDB, mariadbInfo)

        // Connect to the database
        if (database.connect()) {
            logger.info("Working")
        } else {
            logger.info("No connection")
        }
    }
}
```

<br/>

### Making the first query

##### Example, Java

```java
package dev.zomarrd;

import cn.nukkit.plugin.PluginBase;
import dev.zomarrd.dbapi.Database;
import dev.zomarrd.dbapi.DatabaseLib;
import dev.zomarrd.dbapi.SQLType;
import dev.zomarrd.dbapi.mariadb.ConnectionInfo;

class firstQueryExample extends PluginBase {
    @Override
    public void onEnable() {
        ConnectionInfo mariadbInfo = new ConnectionInfo("ip", "user", "pass", "scheme", "optional_port");
        Database database = DatabaseLib.INSTANCE.init(SQLType.MariaDB, mariadbInfo);

        // Connect to the database
        if (!database.connect()) {
            getServer().shutdown();
        }

        database.query("CREATE TABLE players (\n" +
                "player_id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                "player_name VARCHAR(50) NOT NULL,\n" +
                "player_score INT,\n" +
                "player_team VARCHAR(50)\n" +
                ");\n"
        );
    }
}
```

##### Example, Kotlin

```kotlin
package dev.zomarrd

import cn.nukkit.plugin.PluginBase
import dev.zomarrd.dbapi.DatabaseLib.init
import dev.zomarrd.dbapi.SQLType
import dev.zomarrd.dbapi.mariadb.ConnectionInfo

internal class firstQueryExample : PluginBase() {
    override fun onEnable() { .
        val mariadbInfo = ConnectionInfo("ip", "user", "pass", "scheme", "optional_port")
        val database = init(SQLType.MariaDB, mariadbInfo)

        // Connect to the database
        if (!database.connect()) {
            server.shutdown()
        }
        database.query("QUERY HERE")
    }
}
```

<br/>

### Yes, there is callback 😁

I have added two ways to handle it.

Which one to use? That's up to you, here I'm going to show you different examples of the versatility of this.

##### Example, Java

```java
package dev.zomarrd;

import cn.nukkit.plugin.PluginBase;
import dev.zomarrd.dbapi.Database;
import dev.zomarrd.dbapi.DatabaseLib;
import dev.zomarrd.dbapi.SQLType;
import dev.zomarrd.dbapi.mariadb.ConnectionInfo;
import dev.zomarrd.dbapi.utils.ResultCallback;
import org.jetbrains.annotations.NotNull;

class handlingCallback extends PluginBase {
    @Override
    public void onEnable() {
        ConnectionInfo mariadbInfo = new ConnectionInfo("ip", "user", "pass", "scheme");
        Database database = DatabaseLib.INSTANCE.init(SQLType.MariaDB, mariadbInfo);

        if (!database.connect()) {
            getServer().shutdown();
        }


        /////////////////////
        ///// METHOD 1 /////
        ///////////////////

        // Method with the callback as a lambda function
        database.query("QUERY...", result -> {
            if (result.getError() != null) {
                // Oops, an error has occurred.
                // The query was not executed.
            } else {
                // The query was executed successfully.
                // The only thing is that we don't have anything in result.getData().
                // We just make sure that the query was executed correctly.
            }

            return null;
        });


        /////////////////////
        ///// METHOD 2 /////
        ///////////////////

        // Method with ResultCallback object
        database.query("QUERY...", new ResultCallback<>() {
            @Override
            public void success(Object value) {
                // The query was executed successfully.
                // The only thing is that we don't have anything in the value
                // We just make sure that the query was executed correctly.
            }

            @Override
            public void error(@NotNull Exception exception) {
                // Oops, an error has occurred.
                // The query was not executed.
            }
        });


        /////////////////////
        ///// METHOD 3 /////
        ///////////////////

        // Method with ResultCallback object
        ResultCallback<Object> myCallback = new ResultCallback<>();
        database.query("QUERY...", myCallback);

        myCallback.setOnComplete(result -> {
            if (result.getError() != null) {
                // Oops, an error has occurred.
                // The query was not executed.
            } else {
                // The query was executed successfully.
                // The only thing is that we don't have anything in result.getData().
                // We just make sure that the query was executed correctly.
            }
            
            return null;
        });
    }
}
```

##### Example, Kotlin

```kotlin
package dev.zomarrd

import cn.nukkit.plugin.PluginBase
import dev.zomarrd.dbapi.DatabaseLib.init
import dev.zomarrd.dbapi.SQLType
import dev.zomarrd.dbapi.mariadb.ConnectionInfo
import dev.zomarrd.dbapi.utils.ResultCallback
import dev.zomarrd.dbapi.utils.ResultOrError

class handlingCallback : PluginBase() {
    override fun onEnable() {
        val mariadbInfo = ConnectionInfo("ip", "user", "pass", "scheme")
        val database = init(SQLType.MariaDB, mariadbInfo)
        
        if (!database.connect()) {
            server.shutdown()
        }


        /////////////////////
        ///// METHOD 1 /////
        ///////////////////

        // Method with the callback as a lambda function
        database.query("QUERY...") { result: ResultOrError<Any?> ->
            if (result.error != null) {
                // Oops, an error has occurred.
                // The query was not executed.
            } else {
                // The query was executed successfully.
                // The only thing is that we don't have anything in result.getData().
                // We just make sure that the query was executed correctly.
            }
        }


        /////////////////////
        ///// METHOD 2 /////
        ///////////////////

        // Method with ResultCallback object
        database.query("QUERY...", object : ResultCallback<Any?>() {
            override fun success(value: Any?) {
                // The query was executed successfully.
                // The only thing is that we don't have anything in the value
                // We just make sure that the query was executed correctly.
            }

            override fun error(exception: Exception) {
                // Oops, an error has occurred.
                // The query was not executed.
            }
        })


        /////////////////////
        ///// METHOD 3 /////
        ///////////////////

        // Method with ResultCallback object
        val myCallback = ResultCallback<Any?>()
        database.query("QUERY...", myCallback)
        myCallback.onComplete = { result: ResultOrError<Any?> ->
            if (result.error != null) {
                // Oops, an error has occurred.
                // The query was not executed.
            } else {
                // The query was executed successfully.
                // The only thing is that we don't have anything in result.getData().
                // We just make sure that the query was executed correctly.
            }
        }
    }
}
```

### And that's all? No...

Here I will be leaving examples similar to something real, so you can understand the API and its functions.

##### Example, Java

```java
package dev.zomarrd;

import cn.nukkit.plugin.PluginBase;
import dev.zomarrd.dbapi.Database;
import dev.zomarrd.dbapi.DatabaseLib;
import dev.zomarrd.dbapi.SQLType;
import dev.zomarrd.dbapi.mariadb.ConnectionInfo;
import dev.zomarrd.dbapi.utils.Pair;
import dev.zomarrd.dbapi.utils.ResultCallback;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

class handlingCallback extends PluginBase {
    @Override
    public void onEnable() {
        ConnectionInfo mariadbInfo = new ConnectionInfo("ip", "user", "pass", "scheme");
        Database database = DatabaseLib.INSTANCE.init(SQLType.MariaDB, mariadbInfo);

        if (!database.connect()) {
            getServer().shutdown();
        }

        database.query("CREATE TABLE IF NOT EXIST players(...) values (...)", result -> {
            if (result.getError() != null) {
                getServer().getLogger().error("[Database] Error: " + result.getError().getMessage());
                getServer().shutdown();
            } else {

                // INSERT #1
                database.insert("players", new Pair[]{new Pair("name", "zOmArRD"), new Pair("hasSettings", true), new Pair("money", 500.00)}, insertResult -> {
                    // Your code here...
                    return null;
                });

                // INSERT #2
                database.insert("players", new Pair[]{new Pair("name", "zOmArRD"), new Pair("hasSettings", true), new Pair("money", 500.00)}, new ResultCallback<>() {
                    @Override
                    public void success(Object value) {
                        // Your code here...
                        database.select("players", "name", "zomarrd", selectResult -> {
                            if (selectResult.getData() instanceof HashMap) {
                                HashMap<String, Object> rows = (HashMap<String, Object>) selectResult.getData();
                                for (String key : rows.keySet()) {
                                    getServer().getLogger().info(key + " = " + rows.get(key));
                                }
                            }
                            return null;
                        });


                        database.update("players", "name", "zOmArRD",
                                new Pair[]{
                                        new Pair("hasSettings", false),
                                        new Pair("name", "OmarDev"),
                                        new Pair("money", 0)
                                }, updateResult -> {
                                    if (updateResult.getError() != null) {
                                        // Your code here...
                                    } else {
                                        // Your code here...
                                    }

                                    return null;
                                }
                        );
                    }

                    @Override
                    public void error(@NotNull Exception exception) {
                        // Your code here...
                    }
                });
            }
            return null;
        });
    }
}
```

##### Example, Kotlin

```kotlin
package dev.zomarrd

import cn.nukkit.plugin.PluginBase
import dev.zomarrd.dbapi.DatabaseLib.init
import dev.zomarrd.dbapi.SQLType
import dev.zomarrd.dbapi.mariadb.ConnectionInfo
import dev.zomarrd.dbapi.utils.Pair
import dev.zomarrd.dbapi.utils.ResultCallback
import dev.zomarrd.dbapi.utils.ResultOrError

class handlingCallback : PluginBase() {
    override fun onEnable() {
        val mariadbInfo = ConnectionInfo("ip", "user", "pass", "scheme")
        val database = init(SQLType.MariaDB, mariadbInfo)
        
        if (!database.connect()) {
            server.shutdown()
        }
        
        database.query("CREATE TABLE IF NOT EXIST players(...) values (...)") { result: ResultOrError<Any?> ->
            if (result.error != null) {
                server.logger.error("[Database] Error: " + result.error!!.message)
                server.shutdown()
            } else {

                // INSERT #1
                database.insert(
                    "players", Pair("name", "zOmArRD"), Pair("hasSettings", true), Pair("money", 500.00)
                ) { insertResult: ResultOrError<Any?> ->
                    if (insertResult.error != null) {
                        // bla bla bla
                    }
                }

                // INSERT #2
                database.insert("players",
                    Pair("name", "zOmArRD"),
                    Pair("hasSettings", true),
                    Pair("money", 500.00),
                    callback = object : ResultCallback<Any?>() {
                        override fun success(value: Any?) {
                            // Your code here...
                            database.select("players", "name", "zomarrd") { (data): ResultOrError<Any?> ->
                                if (data is HashMap<*, *>) {
                                    val rows = data as HashMap<String, Any>?
                                    for (key in rows!!.keys) {
                                        server.logger.info(key + " = " + rows[key])
                                    }
                                }
                            }

                            database.update(
                                "players",
                                "name",
                                "zOmArRD",
                                Pair("hasSettings", false),
                                Pair("name", "OmarDev"),
                                Pair("money", 0)
                            ) { updateResult: ResultOrError<Any?> ->
                                if (updateResult.error != null) {
                                    // Your code here...
                                } else {
                                    // Your code here...
                                }

                            }
                        }

                        override fun error(exception: Exception) {
                            // Your code here...
                        }
                    })
            }
        }
    }
}
```

## Credits

Inspired by [sqlNukkitLib](https://github.com/Ragnok123/sqlNukkitLib)