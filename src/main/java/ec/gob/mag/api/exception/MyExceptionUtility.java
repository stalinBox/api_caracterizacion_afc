package ec.gob.mag.api.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyExceptionUtility {

	@SuppressWarnings("rawtypes")
	public static String buildExceptionJsonString(String msgProperty, String infAditional, Class myClass, String method,
			EnumTypeExceptions enumEx, EnumCodeExceptions enumCod, MessageSource messageSource)
			throws JsonProcessingException {

		ExceptionResponse er = new ExceptionResponse();
		if (infAditional == null)
			er.setMessage(messageSource.getMessage(msgProperty, null, LocaleContextHolder.getLocale()));
		else
			er.setMessage(String.format(messageSource.getMessage(msgProperty, null, LocaleContextHolder.getLocale()),
					infAditional));
		er.setProyect(messageSource.getMessage("name.proyect", null, LocaleContextHolder.getLocale()));
		er.setClasse(myClass.getName());
		er.setMethod(method);
		er.setType(enumEx);
		er.setNumberCode(enumCod);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.writeValueAsString(er);
	}

	@SuppressWarnings("rawtypes")
	public static String buildExceptionMicroJsonString(ExceptionResponse ex, Class myClass, String method,
			EnumTypeExceptions enumEx, EnumCodeExceptions enumCod, MessageSource messageSource) {

		ExceptionResponse er = new ExceptionResponse();
		er.setMessage(ex.getMessage());
		er.setStatusCode(ex.getStatusCode());
		er.setDetails(ex.getDetails());
		er.setProyect(messageSource.getMessage("name.proyect", null, LocaleContextHolder.getLocale()));
		er.setClasse(myClass.getName());
		er.setMethod(method);
		er.setType(enumEx);
		er.setNumberCode(enumCod);
		er.setDetailsJson(ex.getDetailsJson());
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			return mapper.writeValueAsString(er);
		} catch (JsonProcessingException e) {
			return "";
		}
	}

	public static String getExceptionDump(Exception ex) {
		StringBuilder result = new StringBuilder();
		for (Throwable cause = ex; cause != null; cause = cause.getCause()) {
			if (result.length() > 0)
				result.append("Caused by: ");
			result.append(cause.getClass().getName());
			result.append(": ");
			result.append(cause.getMessage());
			result.append("\n");
			for (StackTraceElement element : cause.getStackTrace()) {
				result.append("\tat ");
				result.append(element.getMethodName());
				result.append("(");
				result.append(element.getFileName());
				result.append(":");
				result.append(element.getLineNumber());
				result.append(")\n");
			}
		}
		System.out.println("###########################################################################");
		System.out.println("#################       DUMP                                   ############");
		System.out.println("###########################################################################");

		System.out.println(result.toString());
		return result.toString();
	}

}
