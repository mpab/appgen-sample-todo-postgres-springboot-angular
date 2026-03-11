package com.api.services;

import java.util.List;

public interface RepositoryPostgres {
    Long resetIndex(String tableName, String index);

    <T> List<T> getPagingDataByCursor(int pageStartCursor, int pageEndCursor, Class<?> type, String tableName,
            String index);

    <T> List<T> getPagingDataByPageSize(int pageStartCursor, int pageEndCursor, Class<?> type, String tableName,
            String index);

    <T> List<T> getPagingDataByPageSizePrevCursor(int pageSize, int cursor, Class<?> type, String tableName,
            String index);

    <T> List<T> getPagingDataByPageSizeFirstCursor(int pageSize, int cursor, Class<?> type, String tableName,
            String index);

    <T> List<T> getPagingDataByPageSizeLastCursor(int pageSize, int cursor, Class<?> type, String tableName,
            String index);

    <T> T getFirstEntity(Class<?> type, String tableName, String indexName);

    <T> T getLastEntity(Class<?> type, String tableName, String indexName);

    <T> List<T> getAll(Class<?> type, String tableName);

    Long getEntityCount(String tableName);
}
