package ec.gob.mag.api.dto;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(of = "imgId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
public class ImagenCatalogoDTO {

	@Id
	@ApiModelProperty(value = "Este campo es  la clave primaria de la tabla Imagen")
	@Column(name = "img_id", unique = true, nullable = false)
	@JsonProperty("imgId")
	private Long imgId;

	@ApiModelProperty(value = "Id de la tabla catalogos")
	@Column(name = "cat_id", nullable = false)
	@JsonProperty("catId")
	private Integer catId;

	@ApiModelProperty(value = "Nombre de la carpeta de la imagen")
	@Column(name = "img_carpeta", nullable = false)
	@JsonProperty("imgCarpeta")
	private String imgCarpeta;

	@ApiModelProperty(value = "Nombre del archivo")
	@Column(name = "img_nombre_archivo", nullable = false)
	@JsonProperty("imgNombreArchivo")
	private String imgNombreArchivo;

}
