package com.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityManager;

public class RepositoryPostgresImpl implements RepositoryPostgres {

    @Autowired
    private EntityManager entityManager;

    public Long resetIndex(String tableName, String index) {
        var query = entityManager
                .createNativeQuery("select pg_get_serial_sequence('%s', '%s')".formatted(tableName, index));
        var index_name = query.getSingleResult();
        query = entityManager.createNativeQuery(
                "select setval('%s',(select max(%s) from %s))".formatted(index_name, index, tableName));
        return (Long) query.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getPagingDataByCursor(int pageStartCursor, int pageEndCursor, Class<?> type,
            String tableName, String index) {
        var queryString = "select * from %s where \"%s\" >= %s and \"%s\" <= %s order by %s;".formatted(
                tableName, index,
                pageStartCursor, index, pageEndCursor, index);
        var query = entityManager.createNativeQuery(queryString, type);
        var result = query.getResultList();
        return (List<T>) result;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getPagingDataByPageSize(int pageSize, int pageCursor, Class<?> type, String tableName,
            String index) {
        var queryString = "select * from %s where \"%s\" > %d order by %s limit %d;".formatted(tableName, index,
                pageCursor, index, pageSize, index);
        var query = entityManager.createNativeQuery(queryString, type);
        var result = query.getResultList();
        return (List<T>) result;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getPagingDataByPageSizePrevCursor(int pageSize, int pageCursor, Class<?> type,
            String tableName,
            String index) {
        var queryString = "select * from (select * from %s where \"%s\" < %d order by %s desc limit %d) order by %s;"
                .formatted(tableName, index, pageCursor, index, pageSize, index);
        var query = entityManager.createNativeQuery(queryString, type);
        var result = query.getResultList();
        return (List<T>) result;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getPagingDataByPageSizeFirstCursor(int pageSize, int pageCursor, Class<?> type,
            String tableName,
            String index) {
        var queryString = "select * from %s where \"%s\" >= %d order by %s limit %d;"
                .formatted(tableName, index, pageCursor, index, pageSize);
        var query = entityManager.createNativeQuery(queryString, type);
        var result = query.getResultList();
        return (List<T>) result;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getPagingDataByPageSizeLastCursor(int pageSize, int pageCursor, Class<?> type,
            String tableName,
            String index) {
        var entityCount = getEntityCount(tableName);
        var window = entityCount % pageSize;
        if (window == 0)
            window = pageSize;
        var queryString = "select * from (select * from %s where \"%s\" <= %d order by %s desc limit %d) order by %s;"
                .formatted(tableName, index, pageCursor, index, window, index);
        var query = entityManager.createNativeQuery(queryString, type);
        var result = query.getResultList();
        return (List<T>) result;
    }

    @SuppressWarnings("unchecked")
    public <T> T getFirstEntity(Class<?> type, String tableName, String indexName) {
        var queryString = "select * from %s order by \"%s\" asc limit 1;".formatted(tableName, indexName);
        var query = entityManager.createNativeQuery(queryString, type);
        var result = (T) query.getSingleResult();
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> T getLastEntity(Class<?> type, String tableName, String indexName) {
        var queryString = "select * from %s order by \"%s\" desc limit 1;".formatted(tableName, indexName);
        var query = entityManager.createNativeQuery(queryString, type);
        var result = (T) query.getSingleResult();
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getAll(Class<?> type, String tableName) {
        var queryString = "select * from %s;".formatted(tableName);
        var query = entityManager.createNativeQuery(queryString, type);
        var result = query.getResultList();
        return (List<T>) result;
    }

    public Long getEntityCount(String tableName) {
        var queryString = "select count(*) from %s;".formatted(tableName);
        var query = entityManager.createNativeQuery(queryString);
        return (Long) query.getSingleResult();
    }
}