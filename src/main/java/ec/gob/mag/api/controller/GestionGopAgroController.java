package ec.gob.mag.api.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
import com.fasterxml.jackson.databind.ObjectMapper;

import ec.gob.mag.api.dto.CatalogoDTO;
import ec.gob.mag.api.dto.CialcoDTO;
import ec.gob.mag.api.dto.UbicacionDTO;
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

	@Value("${url.ubicacion}")
	private String urlMicroUbicacion;

	@Value("${url.catalogos}")
	private String urlMicroCatalogos;

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

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/cialco/findAllPaged/")
	@ApiOperation(value = "Busca una cialco por id", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> cialcofindAllPaginated(HttpServletRequest request,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "cialco/findAllPaginated/?" + request.getQueryString();
		Object res = consumer.doGet(pathMicro, token);

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = mapper.convertValue(res, Map.class);

//		List<CialcoDTO> cialco = new ArrayList<CialcoDTO>();
//		System.out.println("2 solo data: " + map.containsKey("data"));
//		System.out.println("3 solo data: " + map.get("data"));

		List<CialcoDTO> cialco = (List<CialcoDTO>) convertEntityUtil.ConvertListEntity(CialcoDTO.class,
				map.get("data"));

		cialco = cialco.stream().map(mpr -> {
			String pathMicroUbicacion = null;
			String pathMicroCatalogos = null;
			pathMicroUbicacion = urlServidor + urlMicroUbicacion + "api/ubicacion/findByUbiId/"
					+ mpr.getUbi_id_parroquia();
			UbicacionDTO ubicacionDTO = null;
			CatalogoDTO catalogosDTO = null;
			try {
				pathMicroCatalogos = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
						+ mpr.getCiop_cat_id_oferta();
				catalogosDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogos, token, CatalogoDTO.class);
				mpr.setNombre_cio_oferta(catalogosDTO.getCatNombre());
			} catch (Exception e) {
				catalogosDTO = null;
			}

			try {
				ubicacionDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroUbicacion, token, UbicacionDTO.class);
				mpr.setNombre_parroquia(ubicacionDTO.getUbiNombre());
				mpr.setNombre_canton(ubicacionDTO.getUbicacionDTO().getUbiNombre());
				mpr.setNombre_provincia(ubicacionDTO.getUbicacionDTO().getUbicacionDTO().getUbiNombre());
			} catch (Exception e) {
				ubicacionDTO = null;
			}
			return mpr;
		}).collect(Collectors.toList());

		map.replace("data", cialco);
		Object map2 = mapper.convertValue(map, Object.class);
//		cialco.stream().forEach(mpr -> System.out.println("parroquia: " + mpr.getNombre_parroquia()));
//		cialco.stream().forEach(mpr -> System.out.println("canton: " + mpr.getNombre_canton()));
//		cialco.stream().forEach(mpr -> System.out.println("provincia: " + mpr.getNombre_provincia()));
//		cialco.stream().forEach(mpr -> System.out.println(mpr.getNombre_cio_oferta()));

		LOGGER.info("/cialco/findAllPaginated/" + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(map2);
	}

	@GetMapping(value = "/cialco/findAll")
	@ApiOperation(value = "Busca una cialco por id", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> cialcofindAll(@RequestHeader(name = "Authorization") String token)
			throws JsonParseException, JsonMappingException, IOException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "cialco/findAll";
		Object res = consumer.doGet(pathMicro, token);
		LOGGER.info("/cialco/findAll/" + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@GetMapping(value = "/cialco/findById/{id}")
	@ApiOperation(value = "Busca una cialco por id", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> cialcoFindById(@PathVariable Long id, @RequestHeader(name = "Authorization") String token)
			throws JsonParseException, JsonMappingException, IOException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "cialco/findById/" + id;
		Object res = consumer.doGet(pathMicro, token);
		LOGGER.info("/cialco/findById/" + id + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@GetMapping(value = "/cialcofertaprod/findAll")
	@ApiOperation(value = "Busca una cialcofertaprod por id", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> cialcofertaprodfindAll(@RequestHeader(name = "Authorization") String token)
			throws JsonParseException, JsonMappingException, IOException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "cialcofertaprod/findAll";
		Object res = consumer.doGet(pathMicro, token);
		LOGGER.info("/cialcofertaprod/findAll/" + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@GetMapping(value = "/cialcofertaprod/findById/{id}")
	@ApiOperation(value = "Busca una cialcofertaprod por id", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> cialcofertaprodFindById(@PathVariable Long id,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "cialcofertaprod/findById/" + id;
		Object res = consumer.doGet(pathMicro, token);
		LOGGER.info("/cialcofertaprod/findById/" + id + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}

}
