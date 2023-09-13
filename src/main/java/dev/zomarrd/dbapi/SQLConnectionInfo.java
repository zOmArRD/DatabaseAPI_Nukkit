/*
 * Created by IntelliJ Idea
 *
 * User: zOmArRD
 * Date: 12/9/2023
 *
 * Copyright (c) 2023. OMY TECHNOLOGY by <dev@zomarrd.me>
 */

package dev.zomarrd.dbapi;

public interface SQLConnectionInfo {
    /**
     * @return url for the database connection
     */
    String getConnectionString();
}