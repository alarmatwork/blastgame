package com.fitness.blast;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ControllerAdvice
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
class BlasterExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex) {
		return renderExceptionAsError(HttpStatus.BAD_REQUEST, ex, ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(Exception ex) {
		return renderExceptionAsError(HttpStatus.INTERNAL_SERVER_ERROR, ex, ex.getMessage());
	}

	private ResponseEntity renderExceptionAsError(HttpStatus status, Exception ex, String msg) {
		UUID uuid = UUID.randomUUID();
		log.error("Exception description for request: " + uuid + "  " + ex.getMessage(), ex);
		Map<String, Object> error = new HashMap<>();
		error.put("error", msg);
		error.put("error_description", uuid);

		return ResponseEntity.status(status).body(error);
	}

}
