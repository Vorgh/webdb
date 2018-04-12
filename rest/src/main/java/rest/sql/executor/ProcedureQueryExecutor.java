package rest.sql.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import rest.model.database.Procedure;
import rest.model.request.Change;
import static rest.sql.util.SqlStringUtils.bquote;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
public class ProcedureQueryExecutor implements BaseExecutor<Procedure>
{
    private static final Logger logger = LoggerFactory.getLogger(ProcedureQueryExecutor.class);

    private JdbcTemplate jdbcTemplate;
    private boolean isFunction;

    public ProcedureQueryExecutor(JdbcTemplate jdbcTemplate, boolean isFunction)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.isFunction = isFunction;
    }

    @Override
    public void create(Procedure procedure)
    {
        String sql = createProcedureQuery(procedure);

        logger.info("Query executed: {}", sql);
        jdbcTemplate.execute(sql);
    }

    @Override
    public void modify(Change<Procedure> change)
    {
        delete(change.from.getSchema(), change.from.getName());

        isFunction = change.to.getType().equals("FUNCTION"); //Change type if return type was added or removed.

        //If the creation is not successful, try to restore the dropped procedure
        try
        {
            create(change.to);
        }
        catch (Exception e)
        {
            create(change.from);
            throw e;
        }
    }

    @Override
    public void delete(String schema, String name)
    {
        StringBuilder sb = new StringBuilder();

        if (!isFunction)
        {
            sb.append("DROP PROCEDURE IF EXISTS ");
        }
        else
        {
            sb.append("DROP FUNCTION IF EXISTS ");
        }

        sb.append(bquote(schema)).append(".").append(bquote(name)).append(";");

        String sql = sb.toString();
        logger.info("Query executed: {}", sql);
        jdbcTemplate.execute(sql);
    }

    private String createProcedureQuery(final Procedure procedure)
    {
        StringBuilder sb = new StringBuilder();

        if (!isFunction)
        {
            sb.append("CREATE PROCEDURE ");
        }
        else
        {
            sb.append("CREATE FUNCTION ");
        }

        sb.append(bquote(procedure.getSchema())).append(".").append(bquote(procedure.getName()));

        sb.append("(");
        if (procedure.getParamList() != null)
        {
            List<String> paramTriplets = procedure.getParamList().stream()
                    .map(param ->
                    {
                        List<String> paramParts = new ArrayList<>();
                        if (procedure.getType().equals("PROCEDURE"))
                        {
                            paramParts.add(param.getMode());
                        }
                        paramParts.add(param.getName());
                        paramParts.add(param.getType());

                        return String.join(" ", paramParts);
                    })
                    .collect(Collectors.toList());
            sb.append(String.join(", ", paramTriplets));
        }
        sb.append(")\n");

        if (isFunction)
        {
            sb.append("RETURNS ").append(procedure.getReturnType()).append(" \n");
        }

        sb.append(procedure.getBody());

        return sb.toString();
    }
}
