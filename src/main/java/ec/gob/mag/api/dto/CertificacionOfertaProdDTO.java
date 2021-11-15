package ec.gob.mag.api.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class CertificacionOfertaProdDTO {

	@Id
	@ApiModelProperty(value = "Este campo es la clave primaria de la tabla", required = true, readOnly = true)
	@JsonProperty("copId")
	private Long copId;

	@ApiModelProperty(value = "Aqui se digita el id de la categoria de la Certificación", example = "0")
	@JsonProperty("idcatcertificacion")
	private Integer idCatCertificacion;

	@ApiModelProperty(value = "Aqui se digita el id de la categoria de la Certificación")
	@JsonProperty("nombre_idCatCertificacion")
	private String nombre_idCatCertificacion;

	/*****************************************************
	 * SECCION - RELACIONES JPA
	 *****************************************************/
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "oopd_id")
//	@JsonBackReference
//	private OfertaDetalle ofertaDetalle;

	/*****************************************************
	 * SECCION - CAMPOS POR DEFECTO EN TODAS LAS ENTIDADES
	 *****************************************************/

	@ApiModelProperty(value = "11=activo  12=inactivo", required = true, allowableValues = "11=>activo, 12=>inactivo", example = "11")
	@JsonProperty("copEstado")
	private Integer copEstado;

	@ApiModelProperty(value = "Fecha de registro del campo", example = "")
	@JsonProperty("copRegFecha")
	private Date copRegFecha;

	@ApiModelProperty(value = "Id de usuario que creó el regristro", example = "")
	@JsonProperty("copRegUsu")
	private Long copRegUsu;

	@ApiModelProperty(value = "Fecha en la que hizo la actualización del registro", example = "")
	@JsonProperty("copActFecha")
	private Date copActFecha;

	@ApiModelProperty(value = "Id de usuario que actualizacio del qi", example = "")
	@JsonProperty("copActUsu")
	private Long copActUsu;

	@ApiModelProperty(value = "Este campo almacena los valores f =false para eliminado logico  y t= true para indicar que está activo", required = true, allowableValues = "false=>no eliminado lógico, true=> eliminado lógico", example = "")
	@JsonProperty("copEliminado")
	private Boolean copEliminado;

}
