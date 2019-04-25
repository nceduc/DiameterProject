package com.company.CassandraAPI.exceptionhandler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Customer already exist. Please, check your request and try again.")
public class CustomerAlreadyExistException extends RuntimeException {
}
