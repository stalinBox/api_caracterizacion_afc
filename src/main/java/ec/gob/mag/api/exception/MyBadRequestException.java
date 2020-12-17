package ec.gob.mag.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MyBadRequestException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MyBadRequestException(String message) {
		super(message);
	}
}
