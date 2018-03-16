package rest.sql.builder;

import org.springframework.stereotype.Component;

@Component
public class ProcedureQueryBuilder
{
    /*private String createTriggerQuery(Trigger requestTrigger)
    {
        StringBuilder query = new StringBuilder();
        String delimiter = ";";
        String tempDelimiter = "$$";

        query.append("DELIMITER ").append(tempDelimiter).append("\n");
        query.append("CREATE TRIGGER ").append(quote(requestTrigger.schema)).append(".").append(quote(requestTrigger.name)).append(" ");
        query.append(requestTrigger.timing).append(" ").append(requestTrigger.eventType).append(" \n");
        query.append("ON ").append(quote(requestTrigger.eventSchema)).append(".").append(quote(requestTrigger.eventTable)).append(" \n");
        query.append("FOR EACH ROW \n");
        query.append(requestTrigger.triggerBody).append("\n");
        query.replace(query.lastIndexOf(delimiter), query.lastIndexOf(delimiter)+1, tempDelimiter);
        query.append("DELIMITER ").append(delimiter).append("\n");

        return query.toString();
    }*/
}
