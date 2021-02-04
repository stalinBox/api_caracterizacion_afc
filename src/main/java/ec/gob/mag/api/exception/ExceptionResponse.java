package ec.gob.mag.api.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionResponse {
	@JsonInclude(Include.NON_NULL)
	private HttpStatus status;
	@JsonInclude(Include.NON_NULL)
	private Integer statusCode;
	@JsonInclude(Include.NON_NULL)
	private Date timestamp;
	@JsonInclude(Include.NON_NULL)
	private String message;
	@JsonInclude(Include.NON_NULL)
	private String details;
	@JsonInclude(Include.NON_NULL)
	private String proyect;
	@JsonInclude(Include.NON_NULL)
	private String classe;
	@JsonInclude(Include.NON_NULL)
	private String method;
	@JsonInclude(Include.NON_NULL)
	private String detailsJson;
	@JsonInclude(Include.NON_NULL)
	private EnumCodeExceptions numberCode;
	@JsonInclude(Include.NON_NULL)
	private EnumTypeExceptions type;
}
