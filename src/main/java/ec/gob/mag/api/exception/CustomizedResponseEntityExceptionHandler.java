package ec.gob.mag.api.exception;

import java.util.Date;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@SuppressWarnings("unused")
	private final static Logger LOGGER = LoggerFactory.getLogger(ResponseEntityExceptionHandler.class);

	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final ResponseEntity buildException(Exception ex, HttpStatus status, String msgProperty, String msgDetail,
			String msgException, EnumCodeExceptions myTypeCod) {
		String proyecto = messageSource.getMessage("name.proyect", null, LocaleContextHolder.getLocale());
		ExceptionResponse exResponse;
		try {
			String jsonStringU = ex.getMessage();
			ObjectMapper mapper = new ObjectMapper();
			ExceptionResponse myEx = mapper.readValue(jsonStringU, ExceptionResponse.class);
			myEx.setStatus(status);
			myEx.setStatusCode(status.value());
			myEx.setTimestamp(new Date());
			exResponse = myEx;
		} catch (Exception e) {
			String msgEx = null;
			if (msgException == null) {
				if (msgProperty != null)
					msgEx = messageSource.getMessage(msgProperty, null, LocaleContextHolder.getLocale());
				else
					msgEx = ex.getMessage();
			} else
				msgEx = msgException;
			myTypeCod = (myTypeCod == null ? EnumCodeExceptions.ERROR_UNRECOGNIZABLE : myTypeCod);
			EnumTypeExceptions myTypeEx = (myTypeCod == EnumCodeExceptions.ERROR_UNRECOGNIZABLE
					? EnumTypeExceptions.CRITICAL
					: EnumTypeExceptions.WARN);
			exResponse = new ExceptionResponse(status, null, new Date(), msgEx, null, msgDetail, proyecto, null, null,
					null, myTypeEx);
		}

		return new ResponseEntity(exResponse, status);
	}

	@SuppressWarnings("unchecked")
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
		return buildException(ex, HttpStatus.INTERNAL_SERVER_ERROR, null, null, null, null);
	}

	@SuppressWarnings("unchecked")
	@ExceptionHandler(MyNotFoundException.class)
	public final ResponseEntity<Object> handleMyNotFoundException(Exception ex, WebRequest request) {
		return buildException(ex, HttpStatus.NOT_FOUND, null, null, null, null);
	}

	@SuppressWarnings("unchecked")
	@ExceptionHandler(MyInternalException.class)
	public final ResponseEntity<Object> handleMyInternalException(Exception ex, WebRequest request) {
		return buildException(ex, HttpStatus.INTERNAL_SERVER_ERROR, null, null, null, null);
	}

	@SuppressWarnings("unchecked")
	@ExceptionHandler(CmNotFoundException.class)
	public final ResponseEntity<Object> handleNotFoundException(Exception ex, WebRequest request) {
		return buildException(ex, HttpStatus.NOT_FOUND, null, null, null, null);
	}

	@SuppressWarnings("unchecked")
	@ExceptionHandler(MyBadRequestException.class)
	public final ResponseEntity<Object> handleBadRequestException(Exception ex, WebRequest request) {
		return buildException(ex, HttpStatus.BAD_REQUEST, null, null, null, null);
	}

	// ERRORES INTERNOS

	@SuppressWarnings("unchecked")
	@ExceptionHandler(IllegalArgumentException.class)
	public final ResponseEntity<Object> handleIllegalArgumentException(Exception ex, WebRequest request) {
		return buildException(ex, HttpStatus.INTERNAL_SERVER_ERROR, null, null, null, null);

	}

	@SuppressWarnings("unchecked")
	@ExceptionHandler(NoSuchElementException.class)
	public final ResponseEntity<Object> handleNoSuchElementException(Exception ex, WebRequest request) {
		return buildException(ex, HttpStatus.INTERNAL_SERVER_ERROR, "error.entity_not_exist_find_array.message", null,
				null, null);
	}

	@SuppressWarnings("unchecked")
	@ExceptionHandler(StackOverflowError.class)
	public final ResponseEntity<Object> handleStackOverFlowError(Exception ex, WebRequest request) {
		return buildException(ex, HttpStatus.INTERNAL_SERVER_ERROR, null, null, null, null);
	}

	@SuppressWarnings("unchecked")
	@ExceptionHandler(InternalError.class)
	public final ResponseEntity<Object> handleInternalException(Exception ex, WebRequest request) {
		return buildException(ex, HttpStatus.INTERNAL_SERVER_ERROR, null, null, null, null);
	}

}
