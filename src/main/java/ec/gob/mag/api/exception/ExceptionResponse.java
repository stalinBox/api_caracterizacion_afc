package ec.gob.mag.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;

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
	private EnumCodeExceptions numberCode;
	@JsonInclude(Include.NON_NULL)
	private EnumTypeExceptions type;
}
