package rest.config;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import rest.exception.*;
import rest.model.error.GeneralError;

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

    @ExceptionHandler(InvalidJdbcUrlException.class)
    public ResponseEntity<GeneralError> invalidJdbcUrlException(final InvalidJdbcUrlException e)
    {
        logger.error(e.getMessage());
        return new ResponseEntity<>(createErrorDTO("Invalid jdbc url", e.getMessage()), HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<GeneralError> accessDeniedException(final AccessDeniedException e)
    {
        logger.error(e.getMessage());
        return new ResponseEntity<>(createErrorDTO("Access denied", "Access denied"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<GeneralError> sqlException(final SQLException e)
    {
        logger.error(e.getErrorCode() + ":" + e.getMessage());
        return new ResponseEntity<>(createErrorDTO("SQL exception", "Something is wrong with the query."), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<GeneralError> dataAccessException(final DataAccessException e)
    {
        Throwable cause = e.getMostSpecificCause();

        if (cause instanceof SQLException)
        {
            SQLException sqlException = (SQLException)cause;
            String errorMessage = sqlException.getErrorCode() + " : " + sqlException.getMessage();

            logger.error(errorMessage);
            if (sqlException.getErrorCode() == 1044)
            {
                return new ResponseEntity<>(createErrorDTO("Access denied", e.getMessage()), HttpStatus.FORBIDDEN);
            }

            return new ResponseEntity<>(createErrorDTO("SQL Exception", errorMessage), HttpStatus.CONFLICT);
        }
        else
        {
            logger.error(cause.getMessage());
            return new ResponseEntity<>(createErrorDTO("Data Access Exception", "An error occurred during the transaction."), HttpStatus.CONFLICT);
        }
    }

    @ExceptionHandler(SchemaNotFoundException.class)
    public ResponseEntity<GeneralError> schemaNotFoundException(final SchemaNotFoundException e)
    {
        logger.error(e.getMessage());
        return new ResponseEntity<>(createErrorDTO("Schema not found", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<GeneralError> objectNotFoundException(final ObjectNotFoundException e)
    {
        logger.error(e.getMessage());
        return new ResponseEntity<>(createErrorDTO("Object not found", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GeneralError> illegalArgumentException(final IllegalArgumentException e)
    {
        logger.error(e.getMessage());
        return new ResponseEntity<>(createErrorDTO("Illegal argument", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private GeneralError createErrorDTO(String name, String message)
    {
        return new GeneralError(name, message);
    }
}
