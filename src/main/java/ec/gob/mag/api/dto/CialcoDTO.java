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
public class CialcoDTO {

	@Id
	private Long cia_id;
	private Integer ubi_id_parroquia;
	private Integer org_id;
	private Integer soc_id;
	private Integer tip_id;
	private String per_identificacion;
	private String per_nombres;
	private String cia_nombre;
	private String cia_descripcion;
	private String cia_sect_referencia;
	private Integer cia_id_cat_frecuencia;
	private String cia_direccion;
	private String cia_telefono;
	private String cia_celular;
	private String cia_correo;
	private String cia_cord_x;
	private String cia_cord_y;
	private String cia_cord_z;
	private String cia_cord_hemisferio;
	private String cia_cord_zona;
	private String cia_cord_latitud;
	private String cia_cord_longitud;
	private String cia_estado_negocio;
	private String cia_negocio_observacion;
	private Integer ciop_cat_id_oferta;

	private String nombre_parroquia;
	private String nombre_canton;
	private String nombre_provincia;
	private String nombre_cio_oferta;
	private Boolean cia_eliminado;
	private Integer cia_estado;
	private Integer totalRecords;

}
