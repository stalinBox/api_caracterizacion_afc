package ec.gob.mag.api.dto;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(of = "tipcatIdHijo")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class CatalogoRecursiveDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Integer catIdHijo;
	private Integer tipcatIdHijo;
	private Integer tipCatId;
	private String tipCatDescripcion;
	private Integer catIdPadre;
	private String catNombrePadre;
	private String catNombreHijo;
}