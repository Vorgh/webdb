package rest.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rest.dao.FunctionDAO;
import rest.model.database.Function;

@Transactional
@Repository
public class FunctionDAOImpl implements FunctionDAO
{
    private JdbcTemplate jdbcTemplate;

    @Autowired
    FunctionDAOImpl(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

/*    public Function getFunction(String name)
    {

    }*/
}
