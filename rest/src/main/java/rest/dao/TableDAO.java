package rest.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import rest.sql.executor.BatchExecutor;
import rest.sql.builder.DeleteQueryBuilder;
import rest.sql.builder.InsertQueryBuilder;
import rest.sql.builder.SelectQueryBuilder;
import rest.sql.builder.UpdateQueryBuilder;
import rest.mapper.ColumnDetailMapper;
import rest.mapper.ConstraintMapper;
import rest.mapper.IndexMapper;
import rest.mapper.TableDetailMapper;
import rest.model.connection.UserConnection;
import rest.model.database.Column;
import rest.model.database.Constraint;
import rest.model.database.Index;
import rest.model.database.Table;
import rest.model.request.table.AlterTableRequest;
import rest.model.request.Change;
import rest.model.request.table.CreateTableRequest;
import rest.model.request.table.RowModifyRequest;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
public class TableDAO extends AbstractDatabaseDAO
{
    private static final Logger logger = LoggerFactory.getLogger(TableDAO.class);

    public TableDAO(UserConnection connection)
    {
        super(connection);
    }

    public List<Table> getAllTablesMetadata(String schemaName)
    {
        return jdbcTemplate.query(
                "SELECT table_schema, table_name, table_type, engine, create_time, table_collation " +
                        "FROM information_schema.tables " +
                        "WHERE table_schema=?;",
                new Object[]{schemaName},
                new TableDetailMapper());
    }

    public Table getTableMetadata(String schemaName, String tableName)
    {
        return jdbcTemplate.queryForObject(
                "SELECT table_schema, table_name, table_type, engine, create_time, table_collation " +
                        "FROM information_schema.tables " +
                        "WHERE table_schema=? AND table_name=?;",
                new Object[]{schemaName, tableName},
                new TableDetailMapper());
    }

    public List<Column> getAllColumnsMetadata(String schemaName, String tableName)
    {
        String selectCols = "cols.table_schema, cols.table_name, cols.column_name, cols.ordinal_position, cols.column_default, " +
                "cols.is_nullable, cols.data_type, cols.character_maximum_length, cols.character_octet_length, " +
                "cols.numeric_precision, cols.numeric_scale, cols.datetime_precision, cols.character_set_name, " +
                "cols.column_type, cols.column_key, cols.extra ";

        return jdbcTemplate.query(
                "SELECT " + selectCols +
                        "FROM information_schema.columns cols " +
                        "WHERE cols.table_schema=? AND cols.table_name=? " +
                        "ORDER BY ordinal_position ASC;",
                new Object[]{schemaName, tableName},
                new ColumnDetailMapper());
    }

    public List<Constraint> getForeignKeys(String schemaName, String tableName)
    {
        return jdbcTemplate.query(
                "SELECT key_col.constraint_name, key_col.table_schema, key_col.table_name, key_col.column_name, " +
                        "key_col.referenced_table_schema, key_col.referenced_table_name, key_col.referenced_column_name, ref_const.update_rule, ref_const.delete_rule " +
                        "FROM information_schema.KEY_COLUMN_USAGE key_col " +
                        "JOIN information_schema.REFERENTIAL_CONSTRAINTS ref_const ON key_col.constraint_name=ref_const.constraint_name " +
                        "WHERE key_col.table_schema=? AND key_col.table_name=? AND key_col.referenced_column_name IS NOT NULL;",
                new Object[]{schemaName, tableName},
                new ConstraintMapper());
    }

    public List<Index> getTableIndexes(String schemaName, String tableName)
    {
        return jdbcTemplate.query(
                "SELECT index_schema, index_name, table_schema, table_name, column_name, non_unique, nullable " +
                        "FROM information_schema.STATISTICS " +
                        "WHERE table_schema=? AND table_name=?;",
                new Object[]{schemaName, tableName},
                new IndexMapper());
    }

    public List<Map<String, Object>> getRowData(String schemaName, String tableName, String column)
    {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder();
        String query = queryBuilder
                .select(column)
                .from(schemaName, tableName)
                .build();

        return jdbcTemplate.queryForList(query);
    }

    public List<Map<String, Object>> getRowData(String schemaName, String tableName, String[] columnNames)
    {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder();
        String query = queryBuilder
                .select(Arrays.asList(columnNames))
                .from(schemaName, tableName)
                .build();

        return jdbcTemplate.queryForList(query);
    }

    public void modifyRows(String schemaName, String tableName, RowModifyRequest request)
    {
        StringBuilder query = new StringBuilder();
        BatchExecutor batchExecutor = new BatchExecutor(jdbcTemplate);
        List<String> insertQueries = new ArrayList<>();
        List<String> updateQueries = new ArrayList<>();
        List<String> deleteQueries = new ArrayList<>();

        List<String> columnList = new ArrayList<>();
        List<String> primaryKeys = getPrimaryKeyColumns(schemaName, tableName);

        if (request.changes.get(0).from != null)
        {
            columnList.addAll(request.changes.get(0).from.keySet());
        }
        else
        {
            columnList.addAll(request.changes.get(0).to.keySet());
        }

        for (Change<Map<String, Object>> change : request.changes)
        {
            List<String> inserts = new ArrayList<>();
            //List<String> updates = new ArrayList<>();
            //List<String> deletes = new ArrayList<>();
            Map<String, String> updates = new HashMap<>();
            Map<String, String> deletes = new HashMap<>();

            if (change.from == null)
            {
                /*List<String> insertObjectStringList = new ArrayList<>();
                for (Object item : change.to.values())
                {
                    if (!"".equals(item.toString()))
                        insertObjectStringList.add(quoteValue(item.toString()));
                    else
                        insertObjectStringList.add("NULL");
                }
                inserts.add("(" + String.join(",", insertObjectStringList) + ")");*/

                for (Object item : change.to.values())
                {
                    if (!"".equals(item.toString()))
                    {
                        inserts.add(item.toString());
                    }
                    else
                    {
                        inserts.add(null);
                    }
                }
            }

            if (change.from != null && change.to != null)
            {
                for (String col : columnList)
                {
                    updates.put(col, change.to.get(col).toString());
                    //updates.add(col + "=" + change.to.get(col));
                }
            }

            if (change.to == null)
            {
                /*List<String> deleteCondition = new ArrayList<>();
                for (String key : primaryKeys)
                {
                    if (change.from.get(key) == null || change.from.get(key) instanceof Number)
                        deleteCondition.add(key + "=" + change.from.get(key));
                    else
                        deleteCondition.add(key + "=" + quoteValue(change.from.get(key).toString()));
                }
                deletes.add(String.join(" AND ", deleteCondition));*/

                for (String key : primaryKeys)
                {
                    if (change.from.get(key) == null)
                        deletes.put(key, null);
                    else
                        deletes.put(key, change.from.get(key).toString());
                }
            }

            if (!inserts.isEmpty())
            {
                InsertQueryBuilder insertQueryBuilder = new InsertQueryBuilder();
                String q = insertQueryBuilder
                        .insertInto(schemaName, tableName)
                        .columns(columnList)
                        .values(inserts)
                        .build();

                //logger.info("Query executed: {}", q);
                //jdbcTemplate.update(q);
                insertQueries.add(q);

                //query.append("INSERT INTO ").append(quote(schemaName)).append(".").append(quote(tableName)).append(" (");
                //query.append(String.join(",", columnList)).append(") VALUES ").append(String.join(",", inserts)).append(";\n");
            }

            if (!updates.isEmpty())
            {
                Map<String, String> conditions = new HashMap<>();
                //List<String> conditions = new ArrayList<>();

                /*query.append("UPDATE ").append(quote(schemaName)).append(".").append(quote(tableName)).append(" ");
                for (String key : primaryKeys)
                {
                    if (change.from.get(key) == null || change.from.get(key) instanceof Number)
                        conditions.add(key + "=" + change.from.get(key));
                    else
                        conditions.add(key + "=" + quoteValue(change.from.get(key).toString()));
                }*/

                for (String key : primaryKeys)
                {
                    if (change.from.get(key) == null)
                        conditions.put(key, null);
                    else
                        conditions.put(key, change.from.get(key).toString());
                }

                UpdateQueryBuilder updateQueryBuilder = new UpdateQueryBuilder();
                String q = updateQueryBuilder
                        .update(schemaName, tableName)
                        .set(updates)
                        .where(conditions)
                        .build();

                //logger.info("Query executed: {}", q);
                //jdbcTemplate.update(q);
                updateQueries.add(q);

                /*query.append("SET ").append(String.join(",", updates)).append(" \n");
                query.append("WHERE ").append(String.join(" AND ", conditions)).append(";\n");*/
            }

            if (!deletes.isEmpty())
            {
                //query.append("DELETE FROM ").append(quote(schemaName)).append(".").append(quote(tableName)).append(" \n");
                //query.append("WHERE ").append(String.join(" OR ", deletes)).append(";\n");

                DeleteQueryBuilder deleteQueryBuilder = new DeleteQueryBuilder();
                String q = deleteQueryBuilder
                        .from(schemaName, tableName)
                        .where(deletes)
                        .build();

                //logger.info("Query executed: {}", q);
                //jdbcTemplate.update(q);
                deleteQueries.add(q);
            }
        }

        //logger.info("Query executed: {}", query.toString());
        //jdbcTemplate.execute(query.toString());

        if (!insertQueries.isEmpty())
        {
            batchExecutor.batchInsert(insertQueries.toArray(new String[insertQueries.size()]));
        }
        if (!updateQueries.isEmpty())
        {
            batchExecutor.batchUpdate(updateQueries.toArray(new String[updateQueries.size()]));
        }
        if (!deleteQueries.isEmpty())
        {
            batchExecutor.batchDelete(deleteQueries.toArray(new String[deleteQueries.size()]));
        }
    }

    public void createTable(String schemaName, CreateTableRequest request)
    {
        StringBuilder query = new StringBuilder();
        List<String> primaryKeys = new ArrayList<>();

        query.append("CREATE TABLE IF NOT EXISTS ").append(quote(schemaName)).append(".").append(quote(request.tableName)).append("(");
        for (Column col : request.columns)
        {
            if (col.isPrimaryKey())
            {
                primaryKeys.add(col.getName());
            }

            query.append(createColumnDefinition(col)).append(", ");
        }

        if (!primaryKeys.isEmpty())
            query.append("PRIMARY KEY (").append(String.join(",", primaryKeys)).append("), \n");

        for (Constraint key: request.foreignKeys)
        {
            query.append("INDEX ").append(quote(key.constraintName + "_idx")).append(" (").append(quote(key.column)).append("), \n");
            query.append("FOREIGN KEY (").append(quote(key.column)).append(") ");
            query.append("REFERENCES ").append(quote(key.refSchema)).append(".").append(quote(key.refTable)).append("(").append(quote(key.refColumn)).append(") ");
            query.append("ON UPDATE ").append(key.updateRule).append(" ");
            query.append("ON DELETE ").append(key.deleteRule).append(", \n");
        }

        query.replace(query.lastIndexOf(","), query.lastIndexOf(",")+1, ");");

        logger.info("Query executed: {}", query.toString());
        jdbcTemplate.execute(query.toString());
    }

    public void dropTable(String schemaName, String tableName)
    {
        String query = "DROP TABLE IF EXISTS " + quote(schemaName) + "." + quote(tableName) + ";";
        logger.info("Query executed: {}", query);
        jdbcTemplate.execute(query);
    }

    public void alterTable(String schemaName, String tableName, AlterTableRequest request)
    {
        StringBuilder query = new StringBuilder();
        StringBuilder postAlterQuery = new StringBuilder();
        Table thisTable = getTableMetadata(schemaName, tableName);
        List<Index> indexList = getTableIndexes(schemaName, tableName);
        List<String> primaryKeys = getPrimaryKeyColumns(schemaName, tableName);
        boolean primaryKeysChanged = false;

        if (request.isNullOrEmpty()) return;

        if (request.nameChange != null && !request.nameChange.isEmpty())
            query.append("RENAME TO ").append(quote(request.nameChange)).append(", \n");

        for (Change<Column> change : request.columnChange)
        {
            if (change.to == null) //Drop
            {
                if (change.from.isPrimaryKey())
                {
                    primaryKeys.remove(change.from.getName());
                    primaryKeysChanged = true;
                }
                query.append("DROP COLUMN ").append(quote(change.from.getName())).append(", \n");
                continue;
            }

            if (change.from == null) //Add
            {
                if (change.to.isPrimaryKey())
                {
                    primaryKeys.add(change.to.getName());
                    primaryKeysChanged = true;
                }
                query.append("ADD COLUMN ").append(createColumnDefinition(change.to)).append(", \n");
                continue;
            }

            if (change.from.isPrimaryKey() && !change.to.isPrimaryKey())
            {
                primaryKeys.remove(change.from.getName());
                primaryKeysChanged = true;
            }
            if (!change.from.isPrimaryKey() && change.to.isPrimaryKey())
            {
                primaryKeys.add(change.to.getName());
                primaryKeysChanged = true;
            }

            query.append("CHANGE COLUMN ").append(quote(change.from.getName())).append(" ");
            query.append(createColumnDefinition(change.to)).append(", ");

            if (change.from.isUnique() && !change.to.isUnique() && !change.from.isPrimaryKey() && !change.to.isPrimaryKey())
            {
                indexList
                        .stream()
                        .filter(index -> index.unique && index.column.equals(change.from.getName()))
                        .forEach(index -> query.append("DROP INDEX ").append(quote(index.indexName)).append(", "));
            }
        }

        if (primaryKeysChanged)
        {
            query.append("DROP PRIMARY KEY, ");
            query.append("ADD PRIMARY KEY (").append(primaryKeys
                    .stream()
                    .map(this::quote)
                    .collect(Collectors.joining(",")))
                    .append("), ");
        }

        for (Change<Constraint> change : request.constraintChange)
        {
            if (change.to == null) //Drop
            {
                if (thisTable.getEngine().equals("InnoDB"))
                {
                    List<Index> tmpList = indexList.stream()
                            .filter(index -> index.indexName.contains(change.from.constraintName) && index.column.equals(change.from.column))
                            .collect(Collectors.toList());
                    if (tmpList.size() > 0)
                    {
                        query.insert(0, "DROP FOREIGN KEY " + quote(change.from.constraintName) + ", \n");
                        postAlterQuery.append("DROP INDEX ").append(quote(tmpList.get(0).indexName)).append(", \n");
                        continue;
                    }

                    tmpList = indexList.stream()
                            .filter(index -> index.column.equals(change.from.column))
                            .collect(Collectors.toList());
                    if (tmpList.size() > 0)
                    {
                        query.insert(0, "DROP FOREIGN KEY " + quote(change.from.constraintName) + ", \n");
                        postAlterQuery.append("DROP INDEX ").append(quote(indexList.get(0).indexName)).append(", \n");
                        continue;
                    }
                }
            }

            if (change.from == null) //Add
            {
                if (thisTable.getEngine().equals("InnoDB"))
                {
                    List<Index> tmpList = indexList.stream()
                            .filter(index -> index.indexName.contains(change.to.constraintName))
                            .collect(Collectors.toList());

                    if (tmpList.isEmpty())
                    {
                        query.append("ADD INDEX ").append(quote(change.to.constraintName + "_idx")).append(" (").append(quote(change.to.column)).append("), \n");
                    }
                }

                query.append("ADD CONSTRAINT ").append(quote(change.to.constraintName)).append(" ");
                query.append("FOREIGN KEY (").append(quote(change.to.column)).append(") ");
                query.append("REFERENCES ").append(quote(change.to.refSchema)).append(".").append(quote(change.to.refTable)).append("(").append(quote(change.to.refColumn)).append(") ");
                query.append("ON UPDATE ").append(change.to.updateRule).append(" ");
                query.append("ON DELETE ").append(change.to.deleteRule).append(", \n");
            }
        }

        for (Change<Index> change : request.indexChange)
        {
            if (change.to == null) //Drop
            {
                String drop = "DROP INDEX " + quote(change.from.indexName) + ", \n";
                if (postAlterQuery.indexOf(drop) == -1)
                    query.append(drop);
                continue;
            }

            if (change.from == null) //Add
            {
                query.append("ADD ");
                if (change.from.unique) query.append("UNIQUE ");
                query.append("INDEX ").append(quote(change.from.indexName)).append("(").append(quote(change.to.column)).append("), \n");
            }
        }

        query.insert(0, "ALTER TABLE " + quote(schemaName) + "." + quote(tableName) + " ");
        query.setCharAt(query.lastIndexOf(","), ';');
        if (postAlterQuery.length() != 0)
        {
            postAlterQuery.insert(0, "ALTER TABLE " + quote(schemaName) + "." + quote(tableName) + " ");
            postAlterQuery.setCharAt(postAlterQuery.lastIndexOf(","), ';');
            query.append(postAlterQuery);
        }

        logger.info("Query executed: {}", query.toString());
        jdbcTemplate.execute(query.toString());
    }

    private List<String> getPrimaryKeyColumns(String schemaName, String tableName)
    {
        return jdbcTemplate.query(
                "SELECT k.column_name " +
                        "FROM information_schema.table_constraints t " +
                        "LEFT JOIN information_schema.key_column_usage k " +
                        "USING(constraint_name,table_schema,table_name) " +
                        "WHERE t.constraint_type='PRIMARY KEY' AND t.table_schema=? AND t.table_name=?;",
                new Object[]{schemaName, tableName},
                (resultSet, i) -> resultSet.getString("column_name"));
    }

    private String createColumnDefinition(Column col)
    {
        StringBuilder query = new StringBuilder("");

        query.append(quote(col.getName())).append(" ").append(col.getColumnType()).append(" ");

        if (!col.isNullable())
            query.append("NOT NULL ");

        if (col.getDefaultValue() != null && !col.getDefaultValue().isEmpty())
        {
            query.append("DEFAULT \'").append(col.getDefaultValue()).append("\' ");
        }

        if (col.isAutoIncrement())
        {
            query.append("AUTO_INCREMENT ");
        }

        if (col.isUnique())
        {
            query.append("UNIQUE ");
        }

        return query.toString();
    }
}
