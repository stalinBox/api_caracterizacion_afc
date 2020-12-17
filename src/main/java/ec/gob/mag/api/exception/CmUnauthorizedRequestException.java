package ec.gob.mag.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class CmUnauthorizedRequestException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CmUnauthorizedRequestException(String message) {
		super(message);
	}
}
