package rest.service.impl;

import org.springframework.stereotype.Service;
import rest.dao.CustomDAO;
import rest.model.connection.UserConnection;
import rest.service.CustomService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class CustomServiceImpl implements CustomService
{
    @Override
    public List<List<Map<String, Object>>> execute(String sql, UserConnection connection)
    {
        CustomDAO customDAO = new CustomDAO(connection);
        String[] statements;

        String randomDelimiter = String.valueOf(new Random().nextInt(10000));
        while (sql.contains(randomDelimiter))
        {
            randomDelimiter += randomDelimiter;
        }

        String tempSql = sql.toUpperCase();
        int beginIndex = tempSql.indexOf("BEGIN");
        while (beginIndex >= 0)
        {
            int endIndex = tempSql.indexOf("END", beginIndex + 5);
            int semicolonIndex = beginIndex;

            while (semicolonIndex < endIndex && semicolonIndex != -1)
            {
                semicolonIndex = tempSql.indexOf(';', semicolonIndex + 1);
                if (semicolonIndex != -1)
                {
                    tempSql = tempSql.substring(0, semicolonIndex) + randomDelimiter + tempSql.substring(semicolonIndex + 1, tempSql.length());
                }
            }

            beginIndex = tempSql.indexOf("BEGIN", beginIndex + 1);
        }

        statements = tempSql.split("(;[\n]*)|(\n{2,})");
        for (int i=0; i<statements.length; i++)
        {
            statements[i] = statements[i].replace(randomDelimiter, ";");
        }

        return customDAO.execute(statements);
    }
}
