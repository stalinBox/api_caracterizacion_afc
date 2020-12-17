package ec.gob.mag.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CmInternalErrorException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CmInternalErrorException(String message) {
		super(message);
	}
}
