package ec.gob.mag.api.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	/*****************************************************
	 * SECCION - CAMPOS POR DEFECTO EN TODAS LAS ENTIDADES
	 *****************************************************/
//	@ApiModelProperty(value = "11=activo  12=inactivo", required = true, allowableValues = "11=>activo, 12=>inactivo", example = "11")
//	@Column(name = "img_estado", columnDefinition = "Integer default 11")
//	@JsonProperty("imgEstado")
//	@JsonInclude(Include.NON_NULL)
//	private Integer imgEstado;
//
//	@ApiModelProperty(value = "Fecha de registro del campo", example = "")
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "img_reg_fecha", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
//	@JsonProperty("imgRegFecha")
//	@JsonInclude(Include.NON_NULL)
//	private Date imgRegFecha;
//
//	@ApiModelProperty(value = "Id de usuario que creó el regristro", example = "")
//	@Column(name = "img_reg_usu", nullable = false)
//	@JsonProperty("imgRegUsu")
//	@JsonInclude(Include.NON_NULL)
//	@NotNull(message = "_error.validation_blank.message")
//	private Integer imgRegUsu;
//
//	@ApiModelProperty(value = "Fecha en la que hizo la actualización del registro", example = "")
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "img_act_fecha")
//	@JsonProperty("imgActFecha")
//	@JsonInclude(Include.NON_NULL)
//	private Date imgActFecha;
//
//	@ApiModelProperty(value = "Id de usuario que actualizacio del campo", example = "")
//	@Column(name = "img_act_usu")
//	@JsonProperty("imgActUsu")
//	private Integer imgActUsu;
//
//	@ApiModelProperty(value = "Este campo almacena los valores f =false para eliminado logico  y t= true para indicar que está activo", required = true, allowableValues = "false=>no eliminado lógico, true=> eliminado lógico", example = "")
//	@Column(name = "img_eliminado", columnDefinition = "boolean default false")
//	@JsonProperty("imgEliminado")
//	@JsonInclude(Include.NON_NULL)
//	private Boolean imgEliminado;

}
