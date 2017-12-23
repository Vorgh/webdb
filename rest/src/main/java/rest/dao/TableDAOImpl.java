package rest.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rest.model.Table;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public List<Table> getAllTableNames() throws SQLException
    {
        List<Table> tables = new ArrayList<Table>();
        Connection c = jdbcTemplate.getDataSource().getConnection();
        DatabaseMetaData md = c.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);
        while (rs.next())
        {
            Table table = new Table();
            table.setName(rs.getString(3));
            tables.add(table);
        }

        return tables;
    }
}
