package ec.gob.mag.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
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
public class CialcoDTO {
	
	
	@ApiModelProperty(value = "Este campo es la clave foranea de la ubicación", required = true, readOnly = true)	
	@JsonProperty("ubiidparroquia")
	private Long ubiidparroquia;
	
	@ApiModelProperty(value = "Este campo es la clave foranea de la tabla organización", required = true, readOnly = true)	
	@JsonProperty("orgid")
	private Long orgid;
	
	
	@ApiModelProperty(value = "Este campo es la clave foranea de la tabla Sociedades", required = true, readOnly = true)	
	@JsonProperty("socid")
	private Long socid;
	
	@ApiModelProperty(value = "Este campo es la clave foranea de la tabla tipifación", required = true, readOnly = true)	
	@JsonProperty("tipid")
	private Long tipid;
	
	@ApiModelProperty(value = "Valor texto de la identificación de la persona", example = "etiqueta")
	@JsonProperty("peridentificacion")
	@JsonInclude(Include.NON_NULL)
	private String peridentificacion;
	
	@ApiModelProperty(value = "Valor texto del nombre de la persona", example = "etiqueta")
	@JsonProperty("pernombres")
	@JsonInclude(Include.NON_NULL)
	private String pernombres;
	
	@ApiModelProperty(value = "Valor texto del nombre del cialco", example = "etiqueta")
	@JsonProperty("cianombre")
	@JsonInclude(Include.NON_NULL)
	private String cianombre;

}
