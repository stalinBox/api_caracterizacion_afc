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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ec.gob.mag.api.util.Consumer;
import ec.gob.mag.api.util.ConvertEntityUtil;
import ec.gob.mag.api.util.Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/gopagro")
@Api(value = "API CARACTERIZACION AFC", tags = "GOP AGRO")
@ApiResponses(value = { @ApiResponse(code = 200, message = "SUCESS"),
		@ApiResponse(code = 404, message = "RESOURCE NOT FOUND"), @ApiResponse(code = 400, message = "BAD REQUEST"),
		@ApiResponse(code = 201, message = "CREATED"), @ApiResponse(code = 401, message = "UNAUTHORIZED"),
		@ApiResponse(code = 415, message = "UNSUPPORTED TYPE - Representation not supported for the resource"),
		@ApiResponse(code = 500, message = "SERVER ERROR") })
public class GestionGopAgroController implements ErrorController {
	private static final String PATH = "/error";
	public static final Logger LOGGER = LoggerFactory.getLogger(GestionGopAgroController.class);

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

	@Value("${url.gopagro}")
	private String urlMicroGopAgro;

	/***************************************
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 ***************************************/

	@PostMapping(value = "/tipologiaNivel/create/")
	@ApiOperation(value = "Guarda una tipologia", response = Object.class)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> saveTipologiaNivel(@RequestBody String data,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "tipologiaNivel/create/";
		Object res = consumer.doPost(pathMicro, data, token);
		LOGGER.info("/api/gopagro/tipologiaNivel/create/" + data + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@PostMapping(value = "/cialcofertaprod/create/")
	@ApiOperation(value = "Guarda una tipologia", response = Object.class)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> saveOfertaProductivaCialco(@RequestBody String data,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "cialcofertaprod/create/";
		Object res = consumer.doPost(pathMicro, data, token);
		LOGGER.info("/api/gopagro/cialcofertaprod/create/" + data + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}

}
