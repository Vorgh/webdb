package rest.config;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import rest.exception.DatabaseConnectionException;
import rest.exception.MissingAuthInfoException;
import rest.model.error.GeneralError;
import rest.model.error.SQLError;

import java.sql.SQLException;


@ControllerAdvice
public class ControllerExceptionHandler
{
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(MissingAuthInfoException.class)
    public ResponseEntity<GeneralError> missingAuthInfoException(final MissingAuthInfoException e) //Wrong parameters
    {
        logger.error(e.getMessage());
        return new ResponseEntity<>(createErrorDTO("Missing parameters", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DatabaseConnectionException.class)
    public ResponseEntity<GeneralError> databaseConnectionException(final DatabaseConnectionException e) //DB is unreachable
    {
        logger.error(e.getMessage());
        return new ResponseEntity<>(createErrorDTO("Database connection error", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GeneralError> badCredentialsException(final BadCredentialsException e) //Wrong credentials or user not found
    {
        logger.error(e.getMessage());
        return new ResponseEntity<>(createErrorDTO("Bad Credentials", e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CommunicationsException.class)
    public ResponseEntity<GeneralError> communicationsException(final CommunicationsException e) //Lost connection to database
    {
        logger.error(e.getMessage());
        return new ResponseEntity<>(createErrorDTO("Lost connection", "Lost connection to the database. Please check your connection or try again later."), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<GeneralError> sqlException(final SQLException e) //Lost connection to database
    {
        logger.error(e.getErrorCode() + ":" + e.getMessage());
        return new ResponseEntity<>(createErrorDTO("SQL exception", "Something is wrong with the query."), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<GeneralError> sqlException(final DataAccessException e) //Lost connection to database
    {
        Throwable cause = e.getMostSpecificCause();

        if (cause instanceof SQLException)
        {
            SQLException sqlException = (SQLException)cause;
            String errorMessage = sqlException.getErrorCode() + " : " + sqlException.getMessage();

            logger.error(errorMessage);
            return new ResponseEntity<>(createErrorDTO("SQL Exception", errorMessage), HttpStatus.CONFLICT);
        }
        else
        {
            logger.error(cause.getMessage());
            return new ResponseEntity<>(createErrorDTO("Data Access Exception", "An error occurred during the transaction."), HttpStatus.CONFLICT);
        }
    }

    private GeneralError createErrorDTO(String name, String message)
    {
        return new GeneralError(name, message);
    }
}
