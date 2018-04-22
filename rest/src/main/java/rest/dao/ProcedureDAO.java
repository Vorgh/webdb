package rest.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rest.mapper.ParameterMapper;
import rest.mapper.ProcedureMapper;
import rest.model.connection.UserConnection;
import rest.model.database.Parameter;
import rest.model.database.Procedure;
import rest.model.request.Change;
import rest.sql.executor.ProcedureQueryExecutor;

import java.util.List;

public class ProcedureDAO extends AbstractDatabaseDAO
{
    private static final Logger logger = LoggerFactory.getLogger(ProcedureDAO.class);

    public ProcedureDAO(UserConnection connection)
    {
        super(connection);
    }

    public List<Procedure> getAllProcedures(String schemaName)
    {
        String procedureQuery = "SELECT routine_schema, routine_name, routine_type, dtd_identifier, routine_definition, last_altered " +
                "FROM information_schema.routines " +
                "WHERE routine_schema=?;";

        String paramQuery = "SELECT specific_schema, specific_name, parameter_mode, parameter_name, dtd_identifier " +
                "FROM information_schema.parameters " +
                "WHERE specific_schema=? AND parameter_name IS NOT NULL";

        List<Parameter> paramList = jdbcTemplate.query(paramQuery, new Object[]{schemaName}, new ParameterMapper());

        logger.info("Query executed: {},\n{}", paramQuery, procedureQuery);
        return jdbcTemplate.query(procedureQuery, new Object[]{schemaName}, new ProcedureMapper(paramList));
    }

    public Procedure getProcedure(String schemaName, String procedureName, boolean isFunction)
    {
        String query;
        if (isFunction)
        {
            query = "SELECT routine_schema, routine_name, routine_type, dtd_identifier, routine_definition, last_altered " +
                    "FROM information_schema.routines " +
                    "WHERE routine_schema=? AND routine_name=? AND routine_type='FUNCTION';";
        }
        else
        {
            query = "SELECT routine_schema, routine_name, routine_type, dtd_identifier, routine_definition, last_altered " +
                    "FROM information_schema.routines " +
                    "WHERE routine_schema=? AND routine_name=? AND routine_type='PROCEDURE';";
        }

        String paramQuery = "SELECT specific_schema, specific_name, parameter_mode, parameter_name, dtd_identifier " +
                "FROM information_schema.parameters " +
                "WHERE specific_schema=? AND specific_name=? AND parameter_name IS NOT NULL";

        List<Parameter> paramList = jdbcTemplate.query(paramQuery, new Object[]{schemaName, procedureName}, new ParameterMapper());

        logger.info("Query executed: {}", query);
        return jdbcTemplate.queryForObject(query, new Object[]{schemaName, procedureName}, new ProcedureMapper(paramList));
    }

    public void createProcedure(Procedure requestProcedure)
    {
        boolean isFunction = requestProcedure.getType().equals("FUNCTION");
        ProcedureQueryExecutor queryExecutor = new ProcedureQueryExecutor(jdbcTemplate, isFunction);

        queryExecutor.create(requestProcedure);
    }

    public void modifyProcedure(Change<Procedure> change)
    {
        boolean isFunction = change.from.getType().equals("FUNCTION");
        ProcedureQueryExecutor queryExecutor = new ProcedureQueryExecutor(jdbcTemplate, isFunction);

        queryExecutor.modify(change);
    }

    public void dropProcedure(String schemaName, String procedureName, boolean isFunction)
    {
        Procedure procedure = getProcedure(schemaName, procedureName, isFunction);
        if (procedure != null)
        {
            ProcedureQueryExecutor queryExecutor = new ProcedureQueryExecutor(jdbcTemplate, isFunction);
            queryExecutor.delete(schemaName, procedureName);
        }

    }
}
