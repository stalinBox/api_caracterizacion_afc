package ec.gob.mag.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//============== LOMBOK =============
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonaDTO {

	@ApiModelProperty(value = "Este campo es  la clave primaria de la tabla Persona", required = false, readOnly = true)
	@JsonProperty("perId")
	private Long id;

	@ApiModelProperty(value = "21=masculino 22=femenino", required = true)
	@JsonProperty("catGenero")
	@JsonInclude(Include.NON_NULL)
	private Integer catGenero;

	@ApiModelProperty(value = "23=soltero 24=casado 25=divorciado.", required = true)
	@JsonProperty("catEstadoCivil")
	@JsonInclude(Include.NON_NULL)
	private Integer catEstadoCivil;

	@ApiModelProperty(value = "Campo que almacena los nombres de la persona", required = false)
	@JsonProperty("perNombres")
	@JsonInclude(Include.NON_NULL)
	private String perNombres;

	@ApiModelProperty(value = "Campo que almacena el numero de cedula de la persona", required = true)
	@JsonProperty("perIdentificacion")
	@JsonInclude(Include.NON_NULL)
	private String perIdentificacion;

}
