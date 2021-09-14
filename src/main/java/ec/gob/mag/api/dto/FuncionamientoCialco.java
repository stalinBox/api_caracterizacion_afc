package ec.gob.mag.api.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "funcionamiento_cialco", schema = "sc_gopagro")
public class FuncionamientoCialco {

	@Id
	@ApiModelProperty(value = "Este campo es la clave primaria de la tabla", required = true, readOnly = true)
	@Column(name = "fcia_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("fciaId")
	private Long fciaId;

	@ApiModelProperty(value = "ID de la categoria del dia de funcionamiento", example = "4")
	@Column(name = "fcia_id_cat_dia_funcionamiento", nullable = false)
	@NotEmpty(message = "_error.validation_blank.message")
	@JsonProperty("fciaIdCatdiaFuncionamiento")
	private Integer fciaIdCatdiaFuncionamiento;

	@ApiModelProperty(value = "Hora de Inicio de Actividades", example = "8")
	@Column(name = "fcia_id_cat_hora_inicio", nullable = false)
	@NotEmpty(message = "_error.validation_blank.message")
	@JsonProperty("fciaIdCatHoraInicio")
	private Integer fciaIdCatHoraInicio;

	@ApiModelProperty(value = "Hora de Fin de Actividades", example = "17")
	@Column(name = "fcia_id_cat_hora_fin", nullable = false)
	@NotEmpty(message = "_error.validation_blank.message")
	@JsonProperty("fciaIdCatHoraFin")
	private Integer fciaIdCatHoraFin;

	private String nombre_cat_dia_funcionamiento;
	private String nombre_fcia_id_cat_hora_inicio;
	private String nombre_fcia_id_cat_hora_fin;
	/*****************************************************
	 * SECCION - RELACIONES JPA
	 *****************************************************/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cia_id")
	@ApiModelProperty(value = " Clave foranea de la tabla CIALCO", notes = "***")
	@JsonBackReference
	private Cialco cialco;

	/*****************************************************
	 * SECCION - CAMPOS POR DEFECTO EN TODAS LAS ENTIDADES
	 *****************************************************/
	@ApiModelProperty(value = "11=activo  12=inactivo", required = true, allowableValues = "11=>activo, 12=>inactivo", example = "11")
	@Column(name = "fcia_estado", columnDefinition = "Integer default 11")
	@JsonProperty("fciaEstado")
	private Integer fciaEstado;

	@ApiModelProperty(value = "Fecha de registro del campo", example = "")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fcia_reg_fecha", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@JsonProperty("fciaRegFecha")
	private Date fciaRegFecha;

	@ApiModelProperty(value = "Id de usuario que creó el regristro", example = "")
	@Column(name = "fcia_reg_usu", nullable = false)
	@JsonProperty("fciaRegUsu")
	private Integer fciaRegUsu;

	@ApiModelProperty(value = "Fecha en la que hizo la actualización del registro", example = "")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fcia_act_fecha")
	@JsonProperty("fciaActFecha")
	private Date fciaActFecha;

	@ApiModelProperty(value = "Id de usuario que actualizacio del qi", example = "")
	@Column(name = "fcia_act_usu")
	@JsonProperty("fciaActUsu")
	private Integer fciaActUsu;

	@ApiModelProperty(value = "Este campo almacena los valores f =false para eliminado logico  y t= true para indicar que está activo", required = true, allowableValues = "false=>no eliminado lógico, true=> eliminado lógico", example = "")
	@Column(name = "fcia_eliminado", columnDefinition = "boolean default false")
	@JsonProperty("fciaEliminado")
	private Boolean fciaEliminado;

}
