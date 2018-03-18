package rest.sql.executor;

import rest.model.request.Change;

public interface BaseExecutor<T>
{
    void create(T obj);
    void modify(Change<T> change);
    void delete(String schema, String name);
}
