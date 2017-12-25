package rest.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rest.dao.TableDAO;
import rest.mapper.TableDetailMapper;
import rest.mapper.TableMapper;
import rest.model.database.Table;

import java.sql.SQLException;
import java.util.List;

@Transactional
@Repository
public class TableDAOImpl implements TableDAO
{
    private JdbcTemplate jdbcTemplate;

    @Autowired
    TableDAOImpl(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Table> getAllTables(String schemaName)
    {
        return jdbcTemplate.query(
            "SELECT table_name FROM information_schema.tables WHERE table_schema=? AND table_type='BASE TABLE';",
            new Object[] {schemaName},
            new TableMapper());
    }

    @Override
    public List<Table> getAllTablesDetail(String schemaName)
    {
        /*Connection c = jdbcTemplate.getDataSource().getConnection();
        DatabaseMetaData md = c.getMetaData();
        ResultSet rs = md.getTables(null, schemaName, "%", );
        while (rs.next())-
        {
            Table table = new Table();
            table.setName(rs.getString(3));
            tables.add(table);
        }*/

        return jdbcTemplate.query(
            "SELECT table_schema, table_name, table_type, engine, create_time, table_collation " +
                    "FROM information_schema.tables " +
                    "WHERE table_schema=? AND table_type='BASE TABLE';",
            new Object[] {schemaName},
            new TableDetailMapper());
    }

    @Override
    public List<Table> getAllViews(String schemaName)
    {
        return jdbcTemplate.query(
                "SELECT table_name FROM information_schema.tables WHERE table_schema=? AND table_type='VIEW';",
                new Object[] {schemaName},
                new TableMapper());
    }

    @Override
    public List<Table> getAllViewsDetail(String schemaName)
    {
        return jdbcTemplate.query(
                "SELECT table_schema, table_name, table_type, engine, create_time, table_collation " +
                        "FROM information_schema.tables " +
                        "WHERE table_schema=? AND table_type='VIEW';",
                new Object[] {schemaName},
                new TableDetailMapper());
    }
}
