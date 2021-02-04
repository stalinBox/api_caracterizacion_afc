package ec.gob.mag.api.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ec.gob.mag.api.dto.PersonaDTO;
import ec.gob.mag.api.dto.PruebaDTO;
import ec.gob.mag.api.util.Consumer;
import ec.gob.mag.api.util.ConvertEntityUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;

import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping("/api")
@ApiResponses(value = { @ApiResponse(code = 200, message = "SUCESS"),
		@ApiResponse(code = 404, message = "RESOURCE NOT FOUND"), @ApiResponse(code = 400, message = "BAD REQUEST"),
		@ApiResponse(code = 201, message = "CREATED"), @ApiResponse(code = 401, message = "UNAUTHORIZED"),
		@ApiResponse(code = 415, message = "UNSUPPORTED TYPE - Representation not supported for the resource"),
		@ApiResponse(code = 500, message = "SERVER ERROR") })
public class ApiController implements ErrorController {

	private static final String PATH = "/error";
	public static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

	/***************************************
	 * SECCION - INYECCION DE DEPENDENCIAS
	 ***************************************/
	@Autowired
	@Qualifier("consumer")
	private Consumer consumer;

	@Autowired
	@Qualifier("convertEntityUtil")
	private ConvertEntityUtil convertEntityUtil;

	/***************************************
	 * SECCION - MICROSERVICIOS
	 ***************************************/
	@Value("${url.servidor_micro}")
	private String urlServidor;

	@Value("${url.persona}")
	private String urlMicroPersona;

	@Value("${url.prueba}")
	private String urlMicroPrueba;

	/***************************************
	 * SECCION - END-POINTS
	 ***************************************/
	@GetMapping(value = "/persona/findByCedula1/{cedula}")
	@ApiOperation(value = "Ejemplo de llamada directa al microservicio", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> getPersonaEjemplo1(@Validated @PathVariable String cedula,
			@RequestHeader(name = "Authorization") String auth) {
		String pathMicro = urlServidor + urlMicroPersona + "persona/findByCedula/" + cedula;
		Object response = consumer.doGet(pathMicro, auth);
		LOGGER.info("persona/findByCedula1");
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/persona/findByCedula2/{cedula}")
	@ApiOperation(value = "Ejemplo de conversion DTO desde un microservicio", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> getPersonaEjemplo2(@Validated @PathVariable String cedula,
			@RequestHeader(name = "Authorization") String auth) throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException, IOException {
		String pathMicro = urlServidor + urlMicroPersona + "persona/findByCedula/" + cedula;
		PersonaDTO personaDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicro, auth, PersonaDTO.class);
		LOGGER.info("persona/findByCedula2");
		return ResponseEntity.ok(personaDTO);
	}

	@PostMapping(value = "/prueba/create")
	@ApiOperation(value = "Guarda un registro en la tabla prueba del esquema publico", response = Object.class)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> savePruebaPost(@Validated @RequestBody String prueba,
			@RequestHeader(name = "Authorization") String auth) throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException, IOException {
		String pathMicro = urlServidor + urlMicroPrueba + "prueba/create/";
		PruebaDTO pruebaDTO = convertEntityUtil.ConvertSingleEntityPOST(pathMicro, prueba, auth, PruebaDTO.class);
		LOGGER.info("prueba/create");
		return ResponseEntity.ok(pruebaDTO);
	}

	@PutMapping(value = "/prueba/update/{usuId}")
	@ApiOperation(value = "Actualiza un registro en la tabla prueba del esquema publico", response = Object.class)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> updatePruebaPost(@Validated @PathVariable Long usuId, @RequestBody String prueba,
			@RequestHeader(name = "Authorization") String auth) throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException, IOException {
		String pathMicro = urlServidor + urlMicroPrueba + "prueba/update/" + usuId;
		PruebaDTO pruebaDTO = convertEntityUtil.ConvertSingleEntityPOST(pathMicro, prueba, auth, PruebaDTO.class);
		LOGGER.info("prueba/update: entidad:" + prueba + " update by: " + usuId);
		return ResponseEntity.ok(pruebaDTO);
	}

	@DeleteMapping(value = "/prueba/delete/{id}/{usuId}")
	@ApiOperation(value = "Elimina un registro en la tabla prueba del esquema publico", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> deletePruebaPost(@Validated @PathVariable Long id, @PathVariable Long usuId,
			@RequestHeader(name = "Authorization") String auth) throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException, IOException {
		String pathMicro = urlServidor + urlMicroPrueba + "prueba/delete/" + id + "/" + usuId;
		Object pruebaDTO = consumer.doGet(pathMicro, auth);
		LOGGER.info("prueba/delete: id:" + id + " delete by:" + usuId);
		return ResponseEntity.ok(pruebaDTO);
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}
}
