package ec.gob.mag.api.dto;

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
	private Long ciop_id;
	private Integer cia_id;
	private String cia_nombre;
	private Integer ciop_cat_id_oferta;
	private Integer ciop_estado;
	private Long ciop_reg_usu;
	private Long ciop_act_usu;
	private Boolean ciop_eliminado;
	private String nombre_ciop_cat_id_oferta;
	private Integer totalRecords;
}
