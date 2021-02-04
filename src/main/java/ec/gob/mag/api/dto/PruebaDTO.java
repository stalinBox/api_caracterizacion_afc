package ec.gob.mag.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//============== LOMBOK =============
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PruebaDTO {

	private Long id;
	private String prueNom2;

	private String prueNom;
	private String estado;

}
