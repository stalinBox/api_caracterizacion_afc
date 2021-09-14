package ec.gob.mag.api.dto;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
public class FuncionamientoCialcoDTO {

	@Id
	private Integer nro;
	private Long fcia_id;
	private Long cia_id;
	private String cia_nombre;
	private Integer fcia_id_cat_dia_funcionamiento;
	private Integer fcia_id_cat_hora_inicio;
	private Integer fcia_id_cat_hora_fin;
	private Integer fcia_estado;
	private Integer fcia_reg_usu;
	private Boolean fcia_eliminado;
	private Integer totalRecords;
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
}
