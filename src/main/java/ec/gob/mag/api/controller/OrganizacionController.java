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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ec.gob.mag.api.dto.OrganizacionDTO;
import ec.gob.mag.api.dto.UbicacionDTO;
import ec.gob.mag.api.util.Consumer;
import ec.gob.mag.api.util.ConvertEntityUtil;
import ec.gob.mag.api.util.Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/organizacion")
@Api(value = "API CARACTERIZACION AFC", tags = "ORGANIZACION")
@ApiResponses(value = { @ApiResponse(code = 200, message = "SUCESS"),
		@ApiResponse(code = 404, message = "RESOURCE NOT FOUND"), @ApiResponse(code = 400, message = "BAD REQUEST"),
		@ApiResponse(code = 201, message = "CREATED"), @ApiResponse(code = 401, message = "UNAUTHORIZED"),
		@ApiResponse(code = 415, message = "UNSUPPORTED TYPE - Representation not supported for the resource"),
		@ApiResponse(code = 500, message = "SERVER ERROR") })
public class OrganizacionController implements ErrorController {
	private static final String PATH = "/error";
	public static final Logger LOGGER = LoggerFactory.getLogger(OrganizacionController.class);

	/***************************************
	 * SECCION - INYECCION DE DEPENDENCIAS
	 ***************************************/
	@Autowired
	@Qualifier("consumer")
	private Consumer consumer;

	@Autowired
	@Qualifier("convertEntityUtil")
	private ConvertEntityUtil convertEntityUtil;

	@Autowired
	@Qualifier("util")
	private Util util;

	/***************************************
	 * SECCION - MICROSERVICIOS
	 ***************************************/
	@Value("${url.servidor_micro}")
	private String urlServidor;

	@Value("${url.organizacion}")
	private String urlMicroOrganizacion;

	@Value("${url.ubicacion}")
	private String urlMicroUbicacion;

	/***************************************
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 ***************************************/

	@GetMapping(value = "/findById/{idOrg}")
	@ApiOperation(value = "Busca una organizacion por id", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> findPersonaById(@PathVariable Long idOrg,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroOrganizacion + "organizacion/findById/" + idOrg;

		OrganizacionDTO organizacion = convertEntityUtil.ConvertSingleEntityGET(pathMicro, token,
				OrganizacionDTO.class);

		String pathMicroUbicacion = null;
		pathMicroUbicacion = urlServidor + urlMicroUbicacion + "api/ubicacion/findByUbiId/" + organizacion.getUbiId();

		System.out.println("--->" + pathMicroUbicacion);

		UbicacionDTO ubicacionDTO = null;
		try {
			ubicacionDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroUbicacion, token, UbicacionDTO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		organizacion.setUbicacion(ubicacionDTO);

		LOGGER.info("/api/persona/findById/" + idOrg + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(organizacion);
	}

	@PostMapping(value = "/centroAcopio/create/")
	@ApiOperation(value = "Guarda una tipologia", response = Object.class)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> saveOfertaProductivaCialco(@RequestBody String data,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroOrganizacion + "centroAcopio/create";
		Object res = consumer.doPost(pathMicro, data, token);
		LOGGER.info("/api/organizacion/centroAcopio/create/" + data + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}

}
