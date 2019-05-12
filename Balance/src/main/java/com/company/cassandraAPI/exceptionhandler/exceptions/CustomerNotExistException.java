package com.company.cassandraAPI.exceptionhandler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Customer is not exist. Please, check your request and try again.")
public class CustomerNotExistException extends RuntimeException {
}
