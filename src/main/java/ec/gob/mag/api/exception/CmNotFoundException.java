package ec.gob.mag.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CmNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CmNotFoundException(String message) {
		super(message);
	}
}
