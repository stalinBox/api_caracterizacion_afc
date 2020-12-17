package ec.gob.mag.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class CmUnsupportedTypeRequestException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CmUnsupportedTypeRequestException(String message) {
		super(message);
	}
}
