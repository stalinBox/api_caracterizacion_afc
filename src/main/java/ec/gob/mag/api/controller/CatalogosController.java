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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ec.gob.mag.api.dto.CatalogoDTO;
import ec.gob.mag.api.util.Consumer;
import ec.gob.mag.api.util.ConvertEntityUtil;
import ec.gob.mag.api.util.Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/catalogos")
@Api(value = "API CARACTERIZACION AFC", tags = "CATALOGOS")
@ApiResponses(value = { @ApiResponse(code = 200, message = "SUCESS"),
		@ApiResponse(code = 404, message = "RESOURCE NOT FOUND"), @ApiResponse(code = 400, message = "BAD REQUEST"),
		@ApiResponse(code = 201, message = "CREATED"), @ApiResponse(code = 401, message = "UNAUTHORIZED"),
		@ApiResponse(code = 415, message = "UNSUPPORTED TYPE - Representation not supported for the resource"),
		@ApiResponse(code = 500, message = "SERVER ERROR") })
public class CatalogosController implements ErrorController {
	private static final String PATH = "/error";
	public static final Logger LOGGER = LoggerFactory.getLogger(CatalogosController.class);

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

	@Value("${url.catalogos}")
	private String urlMicroCatalogos;

	/***************************************
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 ***************************************/

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/findByIdTipoCatalogo/{id}")
	@ApiOperation(value = "Busca una organizacion por id", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> findTipoCatalogosId(@PathVariable Long id,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroCatalogos + "api/catalogo/findCatalogosByTipo/" + id;
		List<CatalogoDTO> catalogos = (List<CatalogoDTO>) convertEntityUtil.ConvertListEntity(pathMicro, token,
				CatalogoDTO.class);

		LOGGER.info("catalogo/findByIdTipoCatalogo/" + id + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(catalogos);
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/findByCatIdHijo/{catIdHijo}")
	@ApiOperation(value = "Busca un catalogo por el catCodigo y el tipo de catalogo", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> findByCatCodigo(@PathVariable Long catIdHijo,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroCatalogos + "api/catalogo/findByCatIdHijo/" + catIdHijo;
		List<CatalogoDTO> catalogos = (List<CatalogoDTO>) convertEntityUtil.ConvertListEntity(pathMicro, token,
				CatalogoDTO.class);
		LOGGER.info("api/catalogo/findByCatIdHijo/" + catIdHijo + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(catalogos);
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}

}
