package ec.gob.mag.api.controller;

import java.io.IOException;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ec.gob.mag.api.util.Consumer;
import ec.gob.mag.api.util.ConvertEntityUtil;
import ec.gob.mag.api.util.Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/ubicacion")
@Api(value = "API CARACTERIZACION AFC", tags = "UBICACION")
@ApiResponses(value = { @ApiResponse(code = 200, message = "SUCESS"),
		@ApiResponse(code = 404, message = "RESOURCE NOT FOUND"), @ApiResponse(code = 400, message = "BAD REQUEST"),
		@ApiResponse(code = 201, message = "CREATED"), @ApiResponse(code = 401, message = "UNAUTHORIZED"),
		@ApiResponse(code = 415, message = "UNSUPPORTED TYPE - Representation not supported for the resource"),
		@ApiResponse(code = 500, message = "SERVER ERROR") })
public class UbicacionController implements ErrorController {
	private static final String PATH = "/error";
	public static final Logger LOGGER = LoggerFactory.getLogger(UbicacionController.class);

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

	@Value("${url.ubicacion}")
	private String urlMicroUbicacion;

	/***************************************
	 * SECCION - END-POINTS
	 ***************************************/

	@GetMapping(value = "/findOnlyFirstLevelByUbiId/{ubiId}")
	@ApiOperation(value = "Get Ubicacion by ubiId", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> findOnlyFirstLevelByUbiId(@PathVariable Long ubiId,
			@RequestHeader(name = "Authorization") String token) {
		String pathMicro = urlServidor + urlMicroUbicacion + "api/ubicacion/findOnlyFirstLevelByUbiId/" + ubiId;
		System.out.println("===>> " + pathMicro);
		Object response = consumer.doGet(pathMicro, token);
		LOGGER.info("ubicacion/findOnlyFirstLevelByUbiId/" + response + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/findChildrenByUbiId/{ubiId}")
	@ApiOperation(value = "Get Ubicacion by ubiId", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> findChildrenByUbiId(@PathVariable Long ubiId,
			@RequestHeader(name = "Authorization") String token) throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException, IOException {

		String pathMicro = urlServidor + urlMicroUbicacion + "api/ubicacion/findChildrenByUbiId/" + ubiId;
		List<?> lista = (List<?>) convertEntityUtil.ConvertListEntity(pathMicro, token, Object.class);
		LOGGER.info("ubicacion/findChildrenByUbiId" + lista + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(lista);
	}

	@GetMapping(value = "/procedure/coordenada/findValidateUbicationParroquia/{ubiId}/{xLong}/{yLat}")
	@ApiOperation(value = "Obtener y validar la ubicacion por ubiId en las parroquias", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> findValidateUbicationParroquia(@PathVariable Long ubiId, @PathVariable Double xLong,
			@PathVariable Double yLat, @RequestHeader(name = "Authorization") String token) {

		String pathMicro = urlServidor + urlMicroUbicacion + "procedure/coordenada/findValidateUbicationParroquia/"
				+ ubiId + "/" + xLong + "/" + yLat;
		Object response = consumer.doGet(pathMicro, token);
		LOGGER.info("ubicacion/coordenada/procedure/findValidateUbicationParroquia/" + ubiId + "/" + xLong + "/" + yLat
				+ response + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/procedure/coordenada/findValidateUbicationCanton/{ubiId}/{xLong}/{yLat}")
	@ApiOperation(value = "Obtener y validar la ubicacion por ubiId en los cantones", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> findValidateUbicationCanton(@PathVariable Long ubiId, @PathVariable Double xLong,
			@PathVariable Double yLat, @RequestHeader(name = "Authorization") String token) {
		String pathMicro = urlServidor + urlMicroUbicacion + "procedure/coordenada/findValidateUbicationCanton/" + ubiId
				+ "/" + xLong + "/" + yLat;
		Object response = consumer.doGet(pathMicro, token);
		LOGGER.info("ubicacion/coordenada/procedure/findValidateUbicationCanton/" + ubiId + "/" + xLong + "/" + yLat
				+ response + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/procedure/coordenada/findValidateUbicationParroquiaUtm/{ubiId}/{ubiX}/{ubiY}")
	@ApiOperation(value = "Obtener y validar la ubicacion por ubiId en los cantones", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> validateCoordenadasUTM(@PathVariable Long ubiId, @PathVariable Double ubiX,
			@PathVariable Double ubiY, @RequestHeader(name = "Authorization") String token) {
		String pathMicro = urlServidor + urlMicroUbicacion + "procedure/coordenada/findValidateUbicationParroquiaUtm/"
				+ ubiId + "/" + ubiX + "/" + ubiY;
		Object response = consumer.doGet(pathMicro, token);
		LOGGER.info("ubicacion/coordenada/procedure/findValidateUbicationCanton/" + ubiId + "/" + ubiX + "/" + ubiY
				+ response + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/findByUbiId/{ubiId}")
	@ApiOperation(value = "Get Ubicacion by ubiId", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> findUbicacionByUbiId(@PathVariable Long ubiId,
			@RequestHeader(name = "Authorization") String token) {
		String pathMicro = urlServidor + urlMicroUbicacion + "api/ubicacion/findByUbiId/" + ubiId;
		Object response = consumer.doGet(pathMicro, token);
		LOGGER.info("ubicacion/findByUbiId/" + ubiId + response + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(response);
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}

}
