package util;

import lombok.Getter;

import javax.ejb.ApplicationException;

@Getter
@ApplicationException
public class QueryParamsException extends RuntimeException {

    private final String message;

    public QueryParamsException(String message) {
        this.message = message;
    }
}