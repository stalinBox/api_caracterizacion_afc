package ec.gob.mag.api.dto;

import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(of = "catId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CatalogoDTO {

	@ApiModelProperty(value = "Nombre del Catalogo")
	@JsonProperty("catId")
	private Long catId;

	@ApiModelProperty(value = "Nombre del Catalogo")
	@JsonProperty("catNombre")
	private String catNombre;

	@ApiModelProperty(value = "Descripcion del Catalogo")
	@JsonProperty("catDescripcion")
	private String catDescripcion;

	@ApiModelProperty(value = "Abreviatura del Catalogo")
	@JsonProperty("catAbreviatura")
	private String catAbreviatura;

	@ApiModelProperty(value = "Identificativo del Catalogo")
	@JsonProperty("catIdentificativo")
	private String catIdentificativo;

}
