package ec.gob.mag.api.dto;

import java.io.Serializable;

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
public class UbicacionDTO implements Serializable {

	private static final long serialVersionUID = -4017650183258693515L;

	@ApiModelProperty(value = "Este campo es  la clave primaria de la tabla agrupacion")
	@JsonProperty("ubiId")
	private Long ubiId;

	@ApiModelProperty(value = " ***", position = 6)
	@JsonInclude(Include.NON_NULL)
//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "ubi_id_padre", insertable = false, updatable = false)
	@JsonProperty("ubicacion")
	private UbicacionDTO ubicacionDTO;

	@ApiModelProperty(value = " ***", position = 9)
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("ubiNombre")
	private String ubiNombre;

}
