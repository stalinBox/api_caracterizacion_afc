package ec.gob.mag.api.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(of = "perId")
@EqualsAndHashCode(of = "perId")
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class SocioDTO   {
	
	@Id
	@JsonProperty("perId")
	private Long perId;

	@JsonProperty("perNombres")
	@JsonInclude(Include.NON_NULL)
	private String perNombres;

	@JsonProperty("perIdentificacion")
	@JsonInclude(Include.NON_NULL)
	private String perIdentificacion;
}

