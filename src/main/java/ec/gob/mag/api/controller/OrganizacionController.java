package ec.gob.mag.api.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import ec.gob.mag.api.dto.SocioDTO;
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

	@Value("${url.persona}")
	private String urlMicroPersona;

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
		UbicacionDTO ubicacionDTO = null;
		try {
			ubicacionDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroUbicacion, token, UbicacionDTO.class);
			organizacion.setParroquia(ubicacionDTO.getUbiNombre());
			organizacion.setCanton(ubicacionDTO.getUbicacionDTO().getUbiNombre());
			organizacion.setProvincia(ubicacionDTO.getUbicacionDTO().getUbicacionDTO().getUbiNombre());
			organizacion.setPais(ubicacionDTO.getUbicacionDTO().getUbicacionDTO().getUbicacionDTO().getUbiNombre());
		} catch (Exception e) {
			ubicacionDTO = null;
		}
		LOGGER.info("/api/persona/findById/" + idOrg + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(organizacion);
	}

	@GetMapping(value = "/afc/findAllPaged/")
	@ApiOperation(value = "Busca una organizacion afc", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> findOrganizacionAFCPaged(HttpServletRequest request,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroOrganizacion + "/organizacionAFC/findAllPaginated/?"
				+ request.getQueryString();
		Object resp = consumer.doGet(pathMicro, token);
		LOGGER.info("/api/organizacionAFC/findByAFCPaged/ " + "usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(resp);
	}

	@GetMapping(value = "/centroAcopio/findAllByOrgIdPaged/{orgid}/")
	@ApiOperation(value = "Busca una cialco por id", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> cialcofindAllPaginated(@PathVariable Long orgid, HttpServletRequest request,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroOrganizacion + "centroAcopio/findAllByOrgIdPaged/" + orgid + "/?"
				+ request.getQueryString();
		Object res = consumer.doGet(pathMicro, token);
		LOGGER.info("/centroAcopio/findAllByOrgIdPaged/" + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
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

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/findSociosbyOrgId/{orgId}")
	@ApiOperation(value = "Busca todos los socios de una organizacion", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> findSociosbyOrgId(@PathVariable Long orgId,
			@RequestHeader(name = "Authorization") String token) throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException, IOException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroPersona + "organizacion/findSocios/" + orgId;
		List<SocioDTO> socios = (List<SocioDTO>) convertEntityUtil.ConvertListEntity(pathMicro, token, SocioDTO.class);
		LOGGER.info("/api/organizacion/findSociosbyOrgId/" + orgId.toString() + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(socios);
	}

	@GetMapping(value = "/findByRepresentanteLegal/{perId}")
	@ApiOperation(value = "Busca todas las organizaciones que tienen de representante legal perId", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> findByRepresentanteLegal(@PathVariable Long perId,
			@RequestHeader(name = "Authorization") String token) {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroOrganizacion + "/organizacion/findByRepresentanteLegal/" + perId;
		Object organizaciones = consumer.doGet(pathMicro, token);
		LOGGER.info("/api/organizacion/findByRepresentanteLegal/" + perId.toString() + " usuario: "
				+ util.filterUsuId(token));
		return ResponseEntity.ok(organizaciones);
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}

}
