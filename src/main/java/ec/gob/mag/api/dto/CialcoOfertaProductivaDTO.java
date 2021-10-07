package ec.gob.mag.api.dto;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

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
public class CialcoOfertaProductivaDTO {

	@Id
	private Integer nro;
	private String ciop_id;
	private String cia_id;
	private String cia_nombre;
	private String ciop_cat_id_oferta;
	private String ciop_estado;
	private String ciop_reg_usu;
	private String ciop_act_usu;
	private String ciop_eliminado;
	private String nombre_ciop_cat_id_oferta;
	private Integer totalRecords;
	private String ciop_cat_ids_ruta;
	private String nombres_ciop_cat_ids_ruta;
	private List<FuncionamientoCialco> funcionamientoCialco;
}
