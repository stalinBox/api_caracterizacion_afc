package ec.gob.mag.api.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
public class Cialco {

	@Id
	@ApiModelProperty(value = "Este campo es la clave primaria de la tabla", required = true, readOnly = true)
	@Column(name = "cia_id", nullable = true, updatable = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("ciaId")
	private Long ciaId;

	@ApiModelProperty(value = "Este campo es la clave foranea de la ubicación")
	@Column(name = "ubi_id_provincia")
	@JsonProperty("ubiIdProvincia")
	private Integer ubiIdProvincia;

	@ApiModelProperty(value = "Este campo es la clave foranea de la ubicación")
	@Column(name = "ubi_id_canton")
	@JsonProperty("ubiIdCanton")
	private Integer ubiIdCanton;

	@ApiModelProperty(value = "Este campo es la clave foranea de la ubicación")
	@Column(name = "ubi_id_parroquia")
	@JsonProperty("ubiIdParroquia")
	private Integer ubiIdParroquia;

	@ApiModelProperty(value = "Este campo es la clave foranea de la tabla organización")
	@Column(name = "org_id")
	@JsonProperty("orgId")
	private Integer orgId;

	@ApiModelProperty(value = "Este campo es la clave foranea de la tabla Sociedades")
	@Column(name = "soc_id")
	@JsonProperty("socId")
	private Integer socId;

	@ApiModelProperty(value = "Valor texto de la identificación de la persona", example = "etiqueta")
	@Column(name = "per_identificacion", nullable = false)
	@JsonProperty("perIdentificacion")
	private String perIdentificacion;

	@ApiModelProperty(value = "Valor texto del nombre de la persona", example = "etiqueta")
	@Column(name = "per_nombres")
	@JsonProperty("perNombres")
	private String perNombres;

	@ApiModelProperty(value = "Valor texto del nombre del cialco", example = "etiqueta")
	@Column(name = "cia_nombre", nullable = false)
	@JsonProperty("ciaNombre")
	private String ciaNombre;

	@ApiModelProperty(value = "Valor texto de la descripcion del cialco", example = "etiqueta")
	@Column(name = "cia_descripcion", nullable = false)
	@JsonProperty("ciaDescripcion")
	private String ciaDescripcion;

	@ApiModelProperty(value = "Valor texto del sector de referencia del cialco", example = "etiqueta")
	@Column(name = "cia_sect_referencia", nullable = false)
	@JsonProperty("ciaSectReferencia")
	private String ciaSectReferencia;

	@ApiModelProperty(value = "Aqui se digita el id de la categoria de la funcionalidad", example = "5")
	@Column(name = "cia_id_cat_frecuencia", nullable = false)
	@JsonProperty("ciaIdCatFrecuencia")
	private Integer ciaIdCatFrecuencia;

	@ApiModelProperty(value = "Valor texto de la direccion del cialco", example = "etiqueta")
	@Column(name = "cia_direccion", nullable = false)
	@JsonProperty("ciaDireccion")
	private String ciaDireccion;

	@ApiModelProperty(value = "Valor texto de la hora de inicio", example = "etiqueta")
	@Column(name = "cia_telefono", nullable = false)
	@JsonProperty("ciaTelefono")
	private String ciaTelefono;

	@ApiModelProperty(value = "Valor texto de la hora de inicio", example = "etiqueta")
	@Column(name = "cia_celular", nullable = false)
	@JsonProperty("ciaCelular")
	private String ciaCelular;

	@ApiModelProperty(value = "Correo de la persona cialco ", example = "usuario@domino.com", required = true)
	@Column(name = "cia_correo", nullable = false)
	@JsonProperty("ciaCorreo")
	private String ciaCorreo;

	@ApiModelProperty(value = "Coordenada X", example = "etiqueta")
	@Column(name = "cia_cord_x", nullable = false)
	@JsonProperty("ciaCordX")
	private String ciaCordX;

	@ApiModelProperty(value = "Coordenada Y", example = "etiqueta")
	@Column(name = "cia_cord_y", nullable = false)
	@JsonProperty("ciaCordY")
	private String ciaCordY;

	@ApiModelProperty(value = "Valor texto de la hora de inicio", example = "etiqueta")
	@Column(name = "cia_cord_Z", nullable = false)
	@JsonProperty("ciaCordZ")
	private String ciaCordZ;

	@ApiModelProperty(value = "Coordenada Hemisferio", example = "etiqueta")
	@Column(name = "cia_cord_hemisferio", nullable = false)
	@JsonProperty("ciaCordHemisferio")
	private String ciaCordHemisferio;

	@ApiModelProperty(value = "Coordenada ZONA", example = "etiqueta")
	@Column(name = "cia_cord_zona", nullable = false)
	@JsonProperty("ciaCordZona")
	private String ciaCordZona;

	@ApiModelProperty(value = "Coordenada Latitud", example = "etiqueta")
	@Column(name = "cia_cord_latitud", nullable = false)
	@JsonProperty("ciaCordLatitud")
	private String ciaCordLatitud;

	@ApiModelProperty(value = "Valor texto de la hora de inicio", example = "etiqueta")
	@Column(name = "cia_cord_longitud", nullable = false)
	@JsonProperty("ciaCordLongitud")
	private String ciaCordLongitud;

	@ApiModelProperty(value = "Estado Negocio", example = "etiqueta")
	@Column(name = "cia_estado_negocio", nullable = false)
	@JsonProperty("ciaEstadoNegocio")
	private Integer ciaEstadoNegocio;

	@ApiModelProperty(value = "Valor texto de la hora de inicio", example = "etiqueta")
	@Column(name = "cia_negocio_observacion", nullable = false)
	@JsonProperty("ciaNegocioObservacion")
	private String ciaNegocioObservacion;

	@ApiModelProperty(value = "Estado Negocio", example = "id")
	@Column(name = "tip_cat_id", nullable = false)
	@JsonProperty("tipCatId")
	private Integer tipCatId;
	/*****************************************************
	 * SECCION - RELACIONES JPA
	 *****************************************************/
//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinColumn(name = "ciop_id")
//	@JsonProperty("cialcoOfertaProductiva")
//	private List<CialcoOfertaProductiva> cialcoOfertaProductiva;
//
//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinColumn(name = "fcia_id")
//	@JsonProperty("funcionamientoCialco")
//	private List<FuncionamientoCialco> funcionamientoCialco;
//
//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinColumn(name = "oci_id")
//	@JsonProperty("organizacionCialco")
//	private List<OrganizacionCialco> organizacionCialco;

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cialco", cascade = { CascadeType.ALL })
//	@JsonProperty("cialcoOfertaProductiva")
//	private List<CialcoOfertaProductiva> cialcoOfertaProductiva;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cialco", cascade = { CascadeType.ALL })
	@JsonProperty("funcionamientoCialco")
	private List<FuncionamientoCialco> funcionamientoCialco;
//
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cialco", cascade = { CascadeType.ALL })
//	@JsonProperty("organizacionCialco")
//	private List<OrganizacionCialco> organizacionCialco;

	private String provincia;
	private String canton;
	private String parroquia;
	/*****************************************************
	 * SECCION - CAMPOS POR DEFECTO EN TODAS LAS ENTIDADES
	 *****************************************************/

	@ApiModelProperty(value = "11=activo  12=inactivo", required = true, allowableValues = "11=>activo, 12=>inactivo", example = "11")
	@Column(name = "cia_estado", columnDefinition = "Integer default 11")
	@JsonProperty("ciaEstado")
	private Integer ciaEstado;

	@ApiModelProperty(value = "Fecha de registro del campo", example = "")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cia_reg_fecha", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@JsonProperty("ciaRegFecha")
	private Date ciaRegFecha;

	@ApiModelProperty(value = "Id de usuario que creó el regristro", example = "")
	@Column(name = "cia_reg_usu", nullable = false)
	@JsonProperty("ciaRegUsu")
	private Integer ciaRegUsu;

	@ApiModelProperty(value = "Fecha en la que hizo la actualización del registro", example = "")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cia_act_fecha")
	@JsonProperty("ciaActFecha")
	private Date ciaActFecha;

	@ApiModelProperty(value = "Id de usuario que actualizacio del qi", example = "")
	@Column(name = "cia_act_usu")
	@JsonProperty("ciaActUsu")
	@JsonInclude(Include.NON_NULL)
	private Integer ciaActUsu;

	@ApiModelProperty(value = "Este campo almacena los valores f =false para eliminado logico  y t= true para indicar que está activo", required = true, allowableValues = "false=>no eliminado lógico, true=> eliminado lógico", example = "")
	@Column(name = "cia_eliminado", columnDefinition = "boolean default false")
	@JsonProperty("ciaEliminado")
	private Boolean ciaEliminado;

}
