package ec.gob.mag.api.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import javax.persistence.Entity;
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

	@Column(name = "org_id", unique = true)
	@ApiModelProperty(value = " Clave primaria de la tabla organizacion", notes = "ID de la tabla", position = 1)
	@JsonProperty("orgId")
	private Long id;

	@Transient
	@ApiModelProperty(value = "Razon Social de la Organización", notes = "Razon Social de la Organización", position = 8)
	@JsonProperty("institucionRectora")
	private String institucionRectora;

	@Column(name = "cat_tipo_org")
	@ApiModelProperty(value = " Id tipo de organización", notes = "65=Institución Pública, 66=Empresa Privada, 67=Empresa Pública, 361=Organizacion Social", position = 2)
	@JsonProperty("catTipoOrg")
	private Integer catTipoOrg;

	@Column(name = "cat_tipo_org_per")
	@ApiModelProperty(value = " Id tipo de organización - persona", notes = "69 - Persona Jurídica, 68 - Persona Natural", position = 3)
	@JsonProperty("catTipoOrgPer")
	private Integer catTipoOrgPer;

	@Column(name = "cat_tipo_identificacion")
	@ApiModelProperty(value = " Id tipo de identificación - persona", notes = "19=Ruc, 18=Cédula, 20=Pasaporte, 345=Sin Identificacion", position = 4)
	@JsonProperty("catTipoIdentificacion")
	private Integer catTipoIdentificacion;

	@Column(name = "ubi_id")
	@ApiModelProperty(value = " Id de ubicación de la Organización", notes = "Id de la tabla ubicacion", position = 5)
	@JsonProperty("ubiId")
	private Long ubiId;

	@Column(name = "org_identificacion")
	@ApiModelProperty(value = " Identificación de la Organización", notes = "RUC de la Organización", position = 6)
	@JsonProperty("orgIdentificacion")
	private String orgIdentificacion;

	@Column(name = "org_sigla")
	@ApiModelProperty(value = "Siglas de la Organización", notes = "Siglas de la Organización", position = 7)
	@JsonProperty("orgSigla")
	private String orgSigla;

	@Column(name = "org_razon_social")
	@ApiModelProperty(value = "Razon Social de la Organización", notes = "Razon Social de la Organización", position = 8)
	@JsonProperty("orgRazonSocial")
	private String orgRazonSocial;

	@Column(name = "org_nombre_comercial")
	@ApiModelProperty(value = "Nombre Comercial de la Organización", notes = "Nombre Comercial de la Organización", position = 9)
	@JsonProperty("orgNombreComercial")
	private String orgNombreComercial;

	@Column(name = "org_act_economica")
	@ApiModelProperty(value = "Actividad económica de la Organización", notes = "Actividad económica de la Organización", position = 10)
	@JsonProperty("orgActEconomica")
	private String orgActEconomica;

	@Column(name = "org_direccion")
	@ApiModelProperty(value = "Dirección de la Organización", notes = "Dirección de la Organización", position = 11)
	@JsonProperty("orgDireccion")
	private String orgDireccion;

	@Column(name = "org_telefono")
	@ApiModelProperty(value = "Teléfono de la Organización", notes = "Teléfono de la Organización", position = 12)
	@JsonProperty("orgTelefono")
	private String orgTelefono;

	@Column(name = "org_celular")
	@ApiModelProperty(value = "Teléfono Celular de la Organización", notes = "Teléfono Celular de la Organización", position = 13)
	@JsonProperty("orgCelular")
	private String orgCelular;

	@Column(name = "org_correo")
	@ApiModelProperty(value = "Correo de la Organización", notes = "Correo de la Organización", position = 14)
	@JsonProperty("orgCorreo")
	private String orgCorreo;

	@Column(name = "org_acuerdo_ministerial")
	@ApiModelProperty(value = "Acuerdo Ministerial de la Organización", notes = "Acuerdo Ministerial de la Organización", position = 15)
	@JsonProperty("orgAcuerdoMinisterial")
	private String orgAcuerdoMinisterial;

	@Column(name = "org_codigo_suios")
	@ApiModelProperty(value = "Código Suios de la Organización", notes = "Código Suios de la Organización", position = 16)
	@JsonProperty("orgCodigoSuios")
	private String orgCodigoSuios;

	@Column(name = "org_dependencia")
	@ApiModelProperty(value = "Entidad de la que depende de la Organización", notes = "Entidad de la que depende de la Organización", position = 17)
	@JsonProperty("orgDependencia")
	private String orgDependencia;

	@Column(name = "org_tipo_contribuyente")
	@ApiModelProperty(value = "Tipo Contribuyente de la Organización", notes = "Tipo Contribuyente de la Organización", position = 18)
	@JsonProperty("orgTipoContribuyente")
	private String orgTipoContribuyente;

	@Column(name = "org_fuente")
	@ApiModelProperty(value = "Fuente de la Organización", notes = "Fuente de la Organización: SRI/romproc", position = 19)
	@JsonProperty("orgFuente")
	private String orgFuente;

	@Column(name = "org_fuente_id")
	@ApiModelProperty(value = "Id de Fuente de la Organización", notes = "Id de Fuente de la Organización", position = 20)
	@JsonProperty("orgFuenteId")
	private Integer orgFuenteId;

	@Column(name = "org_fuente_fecha")
	@ApiModelProperty(value = "Fecha de la Fuente de la Organización", notes = "Fecha de la Fuente de la Organización", position = 21)
	@JsonProperty("orgFuenteFecha")
	private Date orgFuenteFecha;

	@Column(name = "org_estado_persona_sri")
	@ApiModelProperty(value = "Estado SRI de la Persona", notes = "Estado SRI de la Persona", position = 22)
	@JsonProperty("orgEstadoPersonaSri")
	private String orgEstadoPersonaSri;

	@Column(name = "org_estado_sociedad_sri")
	@ApiModelProperty(value = "Estado SRI de la Sociedad", notes = "Estado SRI de la Sociedad", position = 23)
	@JsonProperty("orgEstadoSociedadSri")
	private String orgEstadoSociedadSri;

	@Column(name = "org_pagina_web")
	@ApiModelProperty(value = "Página Web de la Organización", notes = "Página Web de la Organización", position = 30)
	@JsonProperty("orgPaginaWeb")
	private String orgPaginaWeb;

	@Column(name = "cat_grado")
	@ApiModelProperty(value = "Se selecciona el grado o nivel de la organizacion, TIPO CATALOGO: ", position = 3)
	@JsonProperty("catGrado")
	private Integer catGrado;

	@Column(name = "cat_figura_org")
	@ApiModelProperty(value = "Se selecciona una figura organizativa de un listado de catalogos, TIPO CATALOGO:77 ", position = 3)
	@JsonProperty("catFiguraOrg")
	private Integer catFiguraOrg;

	@Column(name = "org_dependencia_id")
	@ApiModelProperty(value = "Se selecciona la institucion publica que otorgo el acuerdo de personalidad juridica", position = 3)
	@JsonProperty("orgDependenciaId")
	private Long orgDependenciaId;

	@Column(name = "org_acuerdo_fecha")
	@ApiModelProperty(value = "Fecha del acuerdo ", notes = "Fecha del acuerdo de la Organización")
	@JsonProperty("orgAcuerdoFecha")
	private Date orgAcuerdoFecha;

	@Column(name = "org_correo_matriz")
	@ApiModelProperty(value = "Correo de la matriz", notes = "Correo Web de la Organización")
	@JsonProperty("orgCorreoMatriz")
	@Email(message = "_error.validation_valid_mail.message")
	private String orgCorreoMatriz;

	@Column(name = "org_cord_x")
	@ApiModelProperty(value = "Coordenada x", notes = "Coordenada x")
	@JsonProperty("orgCordX")
	private String orgCordX;

	@Column(name = "org_cord_y")
	@ApiModelProperty(value = "Coordenada y", notes = "Coordenada y")
	@JsonProperty("orgCordY")
	private String orgCordY;

	@Column(name = "org_cord_z")
	@ApiModelProperty(value = "Coordenada z", notes = "Coordenada z")
	@JsonProperty("orgCordZ")
	private String orgCordZ;

	@Column(name = "org_cord_hemisferio")
	@ApiModelProperty(value = "Hemisferio", notes = "Hemisferio")
	@JsonProperty("orgCordHemisferio")
	private Integer orgCordHemisferio;

	@Column(name = "org_cord_zona")
	@ApiModelProperty(value = "Zona", notes = "Zona")
	@JsonProperty("orgCordZona")
	private Integer orgCordZona;

	@Column(name = "org_fecha_carga")
	@ApiModelProperty(value = "Fecha de carga de la Organización", notes = "Fecha de carga de la Organización")
	@JsonProperty("orgFechaCarga")
	private Date orgFechaCarga;

	@Column(name = "org_cord_latitud")
	@ApiModelProperty(value = "Coordenada z", notes = "Latitud")
	@JsonProperty("orgCordLatitud")
	private String orgCordLatitud;

	@Column(name = "org_cord_longitud")
	@ApiModelProperty(value = "Coordenada z", notes = "Latitud")
	@JsonProperty("orgCordLongitud")
	private String orgCordLongitud;

	@Transient
	@JsonProperty("ubicacion")
	private UbicacionDTO ubicacion;
}
//ok
