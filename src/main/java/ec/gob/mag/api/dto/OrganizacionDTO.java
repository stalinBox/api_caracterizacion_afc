package ec.gob.mag.api.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(of = "id")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class OrganizacionDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer nro;

	@JsonProperty("orgId")
	private Long id;

	@JsonProperty("institucionRectora")
	private String institucionRectora;

	@JsonProperty("catTipoOrg")
	private Integer catTipoOrg;

	@JsonProperty("catTipoOrgPer")
	private Integer catTipoOrgPer;

	@JsonProperty("catTipoIdentificacion")
	private Integer catTipoIdentificacion;

	@JsonProperty("ubiId")
	private Long ubiId;

	@JsonProperty("orgIdentificacion")
	private String orgIdentificacion;

	@JsonProperty("orgSigla")
	private String orgSigla;

	@JsonProperty("orgRazonSocial")
	private String orgRazonSocial;

	@JsonProperty("orgNombreComercial")
	private String orgNombreComercial;

	@JsonProperty("orgActEconomica")
	private String orgActEconomica;

	@JsonProperty("orgDireccion")
	private String orgDireccion;

	@JsonProperty("orgTelefono")
	private String orgTelefono;

	@JsonProperty("orgCelular")
	private String orgCelular;

	@JsonProperty("orgCorreo")
	private String orgCorreo;

	@JsonProperty("orgAcuerdoMinisterial")
	private String orgAcuerdoMinisterial;

	@JsonProperty("orgCodigoSuios")
	private String orgCodigoSuios;

	@JsonProperty("orgDependencia")
	private String orgDependencia;

	@JsonProperty("orgTipoContribuyente")
	private String orgTipoContribuyente;

	@JsonProperty("orgFuente")
	private String orgFuente;

	@JsonProperty("orgFuenteId")
	private Integer orgFuenteId;

	@JsonProperty("orgFuenteFecha")
	private Date orgFuenteFecha;

	@JsonProperty("orgEstadoPersonaSri")
	private String orgEstadoPersonaSri;

	@JsonProperty("orgEstadoSociedadSri")
	private String orgEstadoSociedadSri;

	@JsonProperty("orgPaginaWeb")
	private String orgPaginaWeb;

	@JsonProperty("catGrado")
	private Integer catGrado;

	@JsonProperty("catFiguraOrg")
	private Integer catFiguraOrg;

	@JsonProperty("orgDependenciaId")
	private Long orgDependenciaId;

	@JsonProperty("orgAcuerdoFecha")
	private Date orgAcuerdoFecha;

	@JsonProperty("orgCorreoMatriz")
	@Email(message = "_error.validation_valid_mail.message")
	private String orgCorreoMatriz;

	@JsonProperty("orgCordX")
	private String orgCordX;

	@JsonProperty("orgCordY")
	private String orgCordY;

	@JsonProperty("orgCordZ")
	private String orgCordZ;

	@JsonProperty("orgCordHemisferio")
	private Integer orgCordHemisferio;

	@JsonProperty("orgCordZona")
	private Integer orgCordZona;

	@JsonProperty("orgFechaCarga")
	private Date orgFechaCarga;

	@JsonProperty("orgCordLatitud")
	private String orgCordLatitud;

	@JsonProperty("orgCordLongitud")
	private String orgCordLongitud;

	@Transient
	@ApiModelProperty(value = "pais de la organizacion", position = 40)
	private String pais;

	@Transient
	@ApiModelProperty(value = "provincia de la organizacion", position = 41)
	private String provincia;

	@Transient
	@ApiModelProperty(value = "canton de la organizacion", position = 42)
	private String canton;

	@Transient
	@ApiModelProperty(value = "parroquia de la organizacion", position = 43)
	private String parroquia;
}
//ok
