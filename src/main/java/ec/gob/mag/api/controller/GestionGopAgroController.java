package ec.gob.mag.api.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ec.gob.mag.api.dto.CatalogoDTO;
import ec.gob.mag.api.dto.CertificacionOfertaProdDTO;
import ec.gob.mag.api.dto.Cialco;
import ec.gob.mag.api.dto.CialcoDTO;
import ec.gob.mag.api.dto.CialcoOfertaProductiva;
import ec.gob.mag.api.dto.CialcoOfertaProductivaDTO;
import ec.gob.mag.api.dto.FuncionamientoCialco;
import ec.gob.mag.api.dto.FuncionamientoCialcoDTO;
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

	/****************************************************
	 * 
	 * ****** CERTIFICACION OFERTA PRODUCTIVA ***********
	 * 
	 ****************************************************/

	/**
	 * Realiza un mantenimiento de estados del registro
	 * 
	 * @param auditCialco: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@PatchMapping(value = "/certofertaprod/state-record/")
	@ApiOperation(value = "Gestionar estado del registro ciaEstado={11 ACTIVO,12 INACTIVO}, ciaEliminado={false, true}, state: {disable, delete, activate}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> auditCertificacionOfertaProductiva(@RequestBody String audit,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "certofertaprod/state-record/";
		Object res = consumer.doPut(pathMicro, audit, token);
		LOGGER.info("/api/gopagro/cialco/update/" + audit + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	/**
	 * Devuelve todos los registros de la tabla certificacion oferta productiva
	 * 
	 * @param auditCialco: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/certofertaprod/findAll")
	@ApiOperation(value = "Gestionar estado del registro ciaEstado={11 ACTIVO,12 INACTIVO}, ciaEliminado={false, true}, state: {disable, delete, activate}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> allCertificacionOfertaProductiva(@RequestHeader(name = "Authorization") String token)
			throws JsonParseException, JsonMappingException, IOException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;

		pathMicro = urlServidor + urlMicroGopAgro + "certofertaprod/findAll";
		System.out.println("url: " + pathMicro);
		List<CertificacionOfertaProdDTO> cop = (List<CertificacionOfertaProdDTO>) convertEntityUtil
				.ConvertListEntity(pathMicro, token, CertificacionOfertaProdDTO.class);

		cop.stream().forEach(mpr -> {
			CatalogoDTO catalogosDTO = null;
			try {
				String pathMicroCatalogos = null;
				pathMicroCatalogos = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
						+ mpr.getIdCatCertificacion();

				catalogosDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogos, token, CatalogoDTO.class);
				mpr.setNombre_idCatCertificacion(catalogosDTO.getCatNombre());
			} catch (Exception e) {
				catalogosDTO = null;
			}
		});

		LOGGER.info("/api/gopagro/certofertaprod/findAll" + "usuario" + util.filterUsuId(token));
		return ResponseEntity.ok(cop);
	}

	/**
	 * Devuelve un registro por id de certificacion oferta productiva
	 * 
	 * @param auditCialco: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@GetMapping(value = "/certofertaprod/findById/{id}")
	@ApiOperation(value = "Devuelve un registro por id de CERTIFICACION OFERTA PRODUCTIVA")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> findByIdCertificacionOfertaProductiva(@RequestHeader(name = "Authorization") String token,
			@PathVariable Long id) throws JsonParseException, JsonMappingException, IOException, NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "certofertaprod/findById/" + id;
		CatalogoDTO catalogosDTO = null;
		CertificacionOfertaProdDTO cop = convertEntityUtil.ConvertSingleEntityGET(pathMicro, token,
				CertificacionOfertaProdDTO.class);
		try {
			String pathMicroCatalogos = null;
			pathMicroCatalogos = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
					+ cop.getIdCatCertificacion();
			catalogosDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogos, token, CatalogoDTO.class);
			cop.setNombre_idCatCertificacion(catalogosDTO.getCatNombre());
		} catch (Exception e) {
			catalogosDTO = null;
		}

		LOGGER.info("/api/gopagro/certofertaprod/findById" + id + " usuario" + util.filterUsuId(token));
		return ResponseEntity.ok(cop);
	}

	/**
	 * Actualiza un registro de la tabla certificacion oferta productiva
	 * 
	 * @param certOferProd: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@PutMapping(value = "/certofertaprod/update/")
	@ApiOperation(value = "Actualiza un registro de la tabla certificacion oferta productiva")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> updateCertificacionOfertaProductiva(@RequestBody String data,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "certofertaprod/update/";
		System.out.println("URL-> " + pathMicro);
		Object res = consumer.doPut(pathMicro, data, token);
		LOGGER.info("/api/gopagro/certofertaprod/update/" + data + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	/**
	 * Crea un registro de la tabla certificacion oferta productiva
	 * 
	 * @param certOferProd: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@PostMapping(value = "/certofertaprod/create/")
	@ApiOperation(value = "Crea un registro de la tabla certificacion oferta productiva", response = Object.class)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> saveCertifciacionOfertaProd(@RequestBody String data,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "certofertaprod/create/";
		Object res = consumer.doPost(pathMicro, data, token);
		LOGGER.info("/api/gopagro/certofertaprod/create/" + data + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	/****************************************************
	 * 
	 * ******************* CIALCO ***********************
	 * 
	 ****************************************************/

	/**
	 * Realiza un mantenimiento de estados del registro
	 * 
	 * @param auditCialco: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@PatchMapping(value = "/cialco/state-record/")
	@ApiOperation(value = "Gestionar estado del registro ciaEstado={11 ACTIVO,12 INACTIVO}, ciaEliminado={false, true}, state: {disable, delete, activate}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> auditCialco(@RequestBody String auditCialco,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "/cialco/state-record/";
		Object res = consumer.doPut(pathMicro, auditCialco, token);
		LOGGER.info("/api/gopagro/cialco/update/" + auditCialco + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@PutMapping(value = "/cialco/update/")
	@ApiOperation(value = "Actualiza un registro en la tabla cialco", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> updateCialco(@RequestBody String updateCialco,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "cialco/update/";
		Object res = consumer.doPut(pathMicro, updateCialco, token);
		LOGGER.info("/api/gopagro/cialco/update/" + updateCialco + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@PostMapping(value = "/cialco/create/")
	@ApiOperation(value = "Guarda una cialco", response = Object.class)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> saveCialco(@RequestBody String data, @RequestHeader(name = "Authorization") String token)
			throws JsonParseException, JsonMappingException, IOException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "cialco/create/";
		Object res = consumer.doPost(pathMicro, data, token);
		LOGGER.info("/api/gopagro/cialco/create/" + data + " usuario: " + util.filterUsuId(token));
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

		List<CialcoDTO> cialco = (List<CialcoDTO>) convertEntityUtil.ConvertListEntity(CialcoDTO.class,
				map.get("data"));

		cialco = cialco.stream().map(mpr -> {
			String pathMicroUbicacion = null;
			String pathMicroCatalogos = null;
			CatalogoDTO catalogosDTO = null;
			UbicacionDTO ubicacionDTO = null;

			// SETTEAR NOMBRE_TIP_CAT_ID
			try {
				pathMicroCatalogos = null;
				System.out.println("SETTEAR NOMBRE_TIP_CAT_ID ");
				pathMicroCatalogos = urlServidor + urlMicroCatalogos + "api/catalogo/findById/" + mpr.getTip_cat_id();
				catalogosDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogos, token, CatalogoDTO.class);
				mpr.setNombre_tip_cat_id(catalogosDTO.getCatNombre());
				System.out.println("NOMBRE CATEGORIA: " + catalogosDTO.getCatNombre());
				System.out.println("FIN");
			} catch (Exception e) {
				catalogosDTO = null;
			}

			// SETTEAR NOMBRE_CIALCO_OFERTA
			try {
				pathMicroCatalogos = null;
				pathMicroCatalogos = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
						+ mpr.getCiop_cat_id_oferta();
				catalogosDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogos, token, CatalogoDTO.class);
				mpr.setNombre_cio_oferta(catalogosDTO.getCatNombre());
			} catch (Exception e) {
				catalogosDTO = null;
			}
			// SETTEAR NOMBRE PARROQUIA
			try {
				ubicacionDTO = null;
				pathMicroUbicacion = urlServidor + urlMicroUbicacion + "api/ubicacion/findByUbiId/"
						+ mpr.getUbi_id_parroquia();
				ubicacionDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroUbicacion, token, UbicacionDTO.class);
				mpr.setParroquia(ubicacionDTO.getUbiNombre());
			} catch (Exception e) {
				ubicacionDTO = null;
			}
			// SETTEAR NOMBRE CANTON
			try {
				ubicacionDTO = null;
				pathMicroUbicacion = urlServidor + urlMicroUbicacion + "api/ubicacion/findByUbiId/"
						+ mpr.getUbi_id_canton();
				ubicacionDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroUbicacion, token, UbicacionDTO.class);
				mpr.setCanton(ubicacionDTO.getUbiNombre());
			} catch (Exception e) {
				ubicacionDTO = null;
			}
			// SETTEAR NOMBRE PROVINCIA
			try {
				ubicacionDTO = null;
				pathMicroUbicacion = urlServidor + urlMicroUbicacion + "api/ubicacion/findByUbiId/"
						+ mpr.getUbi_id_provincia();
				ubicacionDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroUbicacion, token, UbicacionDTO.class);
				mpr.setProvincia(ubicacionDTO.getUbiNombre());
			} catch (Exception e) {
				ubicacionDTO = null;
			}

			return mpr;
		}).collect(Collectors.toList());

		map.replace("data", cialco);
		Object map2 = mapper.convertValue(map, Object.class);
		LOGGER.info("/cialco/findAllPaginated/" + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(map2);
	}

	@GetMapping(value = "/cialco/findById/{id}")
	@ApiOperation(value = "Busca una cialco por id", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> cialcoFindById(@PathVariable Long id, @RequestHeader(name = "Authorization") String token)
			throws JsonParseException, JsonMappingException, IOException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "cialco/findById/" + id;
		String pathMicroUbicacion = null;
		UbicacionDTO ubicacionDTO = null;
		Cialco cialco = convertEntityUtil.ConvertSingleEntityGET(pathMicro, token, Cialco.class);

		cialco.getFuncionamientoCialco().stream().map(mpr -> {
			String pathMicroCatalogos = null;
			CatalogoDTO catalogosDTO = null;
			try {
				pathMicroCatalogos = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
						+ mpr.getFciaIdCatdiaFuncionamiento();
				catalogosDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogos, token, CatalogoDTO.class);
				mpr.setNombre_cat_dia_funcionamiento(catalogosDTO.getCatNombre());
			} catch (Exception e) {
				catalogosDTO = null;
			}

			try {
				pathMicroCatalogos = null;
				catalogosDTO = null;
				pathMicroCatalogos = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
						+ mpr.getFciaIdCatHoraInicio();
				catalogosDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogos, token, CatalogoDTO.class);
				mpr.setNombre_fcia_id_cat_hora_fin(catalogosDTO.getCatNombre());
			} catch (Exception e) {
				catalogosDTO = null;
			}

			try {
				pathMicroCatalogos = null;
				catalogosDTO = null;
				pathMicroCatalogos = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
						+ mpr.getFciaIdCatHoraFin();
				catalogosDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogos, token, CatalogoDTO.class);
				mpr.setNombre_fcia_id_cat_hora_inicio(catalogosDTO.getCatNombre());
			} catch (Exception e) {
				catalogosDTO = null;
			}

			return mpr;
		}).collect(Collectors.toList());

		// SETTEAR NOMBRE PARROQUIA
		try {
			ubicacionDTO = null;
			pathMicroUbicacion = urlServidor + urlMicroUbicacion + "api/ubicacion/findByUbiId/"
					+ cialco.getUbiIdParroquia();
			ubicacionDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroUbicacion, token, UbicacionDTO.class);
			cialco.setParroquia(ubicacionDTO.getUbiNombre());
		} catch (Exception e) {
			ubicacionDTO = null;
		}
		// SETTEAR NOMBRE CANTON
		try {
			ubicacionDTO = null;
			pathMicroUbicacion = urlServidor + urlMicroUbicacion + "api/ubicacion/findByUbiId/"
					+ cialco.getUbiIdCanton();
			ubicacionDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroUbicacion, token, UbicacionDTO.class);
			cialco.setCanton(ubicacionDTO.getUbiNombre());
		} catch (Exception e) {
			ubicacionDTO = null;
		}
		// SETTEAR NOMBRE PROVINCIA
		try {
			ubicacionDTO = null;
			pathMicroUbicacion = urlServidor + urlMicroUbicacion + "api/ubicacion/findByUbiId/"
					+ cialco.getUbiIdProvincia();
			ubicacionDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroUbicacion, token, UbicacionDTO.class);
			cialco.setProvincia(ubicacionDTO.getUbiNombre());
		} catch (Exception e) {
			ubicacionDTO = null;
		}

		LOGGER.info("/cialco/findById/" + id + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(cialco);
	}

	/****************************************************
	 * 
	 * ********** CIALCO OFERTA PRODUCTIVA **************
	 * 
	 ****************************************************/

	/**
	 * Actualiza un registro de la tabla certificacion oferta productiva
	 * 
	 * @param certOferProd: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@PutMapping(value = "/cialcofertaprod/update/")
	@ApiOperation(value = "Actualiza un registro de la tabla CIALCO OFERTA PRODUCTIVA")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> updateCialcoOfertaProductiva(@RequestBody String data,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "cialcofertaprod/update/";
		Object res = consumer.doPut(pathMicro, data, token);
		LOGGER.info("/api/gopagro/cialcofertaprod/update/" + data + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	/**
	 * Devuelve todos los registros de la tabla cialco oferta productiva
	 * 
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@GetMapping(value = "/cialcofertaprod/findAll")
	@ApiOperation(value = "Devuelve todos los registros de la tabla cialco oferta productiva")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> allCialcoOfertaProductiva(@RequestHeader(name = "Authorization") String token)
			throws JsonParseException, JsonMappingException, IOException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "/cialcofertaprod/findAll";
		Object res = consumer.doGet(pathMicro, token);
		LOGGER.info("/api/gopagro/cialcofertaprod/findAll" + "usuario" + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	/**
	 * Realiza un mantenimiento de estados del registro
	 * 
	 * @param auditCialco: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@PatchMapping(value = "/cialcofertaprod/state-record/")
	@ApiOperation(value = "Gestionar estado del registro ciaEstado={11 ACTIVO,12 INACTIVO}, ciaEliminado={false, true}, state: {disable, delete, activate}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> auditCialcoOfertaProductiva(@RequestBody String audit,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "/cialcofertaprod/state-record/";
		Object res = consumer.doPut(pathMicro, audit, token);
		LOGGER.info("/api/gopagro/cialcofertaprod/state-record/" + audit + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@GetMapping(value = "/cialcofertaprod/findById/{id}")
	@ApiOperation(value = "Busca una cialcofertaprod por id", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> cialcofertaprodFindById(@PathVariable Long id,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		String pathMicroCatalogos = null;

		pathMicro = urlServidor + urlMicroGopAgro + "cialcofertaprod/findById/" + id;
		CatalogoDTO catalogosDTO = null;
		CialcoOfertaProductiva ciofprod = convertEntityUtil.ConvertSingleEntityGET(pathMicro, token,
				CialcoOfertaProductiva.class);

		try {
			pathMicroCatalogos = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
					+ ciofprod.getCiopCatIdOferta();

			catalogosDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogos, token, CatalogoDTO.class);
			ciofprod.setNombre_cio_oferta(catalogosDTO.getCatNombre());
		} catch (Exception e) {
			catalogosDTO = null;
		}

		LOGGER.info("/cialcofertaprod/findById/" + id + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(ciofprod);
	}

	@PostMapping(value = "/cialcofertaprod/create/")
	@ApiOperation(value = "Guarda una CIALCO OFERTA PRODUCTIVA", response = Object.class)
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
	@GetMapping(value = "/cialcofertaprod/findAllPaginated/{ciaId}")
	@ApiOperation(value = "Busca una cialco por id", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> cialcoOfertaProductivafindAllPaginated(@PathVariable Long ciaId,
			HttpServletRequest request, @RequestHeader(name = "Authorization") String token)
			throws JsonParseException, JsonMappingException, IOException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "cialcofertaprod/findAllPaginated/" + ciaId + "/?"
				+ request.getQueryString();
		Object res = consumer.doGet(pathMicro, token);

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = mapper.convertValue(res, Map.class);

		List<CialcoOfertaProductivaDTO> cialco_oferta_p = (List<CialcoOfertaProductivaDTO>) convertEntityUtil
				.ConvertListEntity(CialcoOfertaProductivaDTO.class, map.get("data"));

		cialco_oferta_p = cialco_oferta_p.stream().map(mpr -> {
			String pathMicroCatalogos = null;
			CatalogoDTO catalogosDTO = null;
			try {
				pathMicroCatalogos = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
						+ mpr.getCiop_cat_id_oferta();
				catalogosDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogos, token, CatalogoDTO.class);
				mpr.setNombre_ciop_cat_id_oferta(catalogosDTO.getCatNombre());
			} catch (Exception e) {
				catalogosDTO = null;
			}

			List<FuncionamientoCialco> funcionamientoDTO = null;
			String pathMicroFuncionamiento = null;
			try {
				pathMicroFuncionamiento = urlServidor + urlMicroGopAgro + "funcionamientocialco/findByCiaId/"
						+ mpr.getCia_id();
				funcionamientoDTO = (List<FuncionamientoCialco>) convertEntityUtil
						.ConvertListEntity(pathMicroFuncionamiento, token, FuncionamientoCialco.class);
				mpr.setFuncionamientoCialco(funcionamientoDTO);
			} catch (Exception e) {
				funcionamientoDTO = null;
			}

			// ADD FUNCIONAMIENTO ENTITY
			funcionamientoDTO.stream().map(mprFuncionamiento -> {
				String pathMicroCatalogosFun = null;
				String pathMicroCatalogosHInicio = null;
				String pathMicroCatalogosHFin = null;
				CatalogoDTO catalogosFunDTO = null;
				CatalogoDTO catalogosDTOHoraInicio = null;
				CatalogoDTO catalogosDTOHoraFin = null;

				try {
					pathMicroCatalogosFun = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
							+ mprFuncionamiento.getFciaIdCatdiaFuncionamiento();
					catalogosFunDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogosFun, token,
							CatalogoDTO.class);

					mprFuncionamiento.setNombre_cat_dia_funcionamiento(catalogosFunDTO.getCatNombre());
				} catch (Exception e) {
					catalogosFunDTO = null;
				}

				try {
					pathMicroCatalogosHInicio = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
							+ mprFuncionamiento.getFciaIdCatHoraInicio();
					catalogosDTOHoraInicio = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogosHInicio, token,
							CatalogoDTO.class);
					mprFuncionamiento.setNombre_fcia_id_cat_hora_inicio(catalogosDTOHoraInicio.getCatNombre());
				} catch (Exception e) {
					catalogosDTOHoraInicio = null;
				}

				try {
					pathMicroCatalogosHFin = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
							+ mprFuncionamiento.getFciaIdCatHoraFin();
					catalogosDTOHoraFin = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogosHFin, token,
							CatalogoDTO.class);
					mprFuncionamiento.setNombre_fcia_id_cat_hora_fin(catalogosDTOHoraFin.getCatNombre());
				} catch (Exception e) {
					catalogosDTOHoraFin = null;
				}
				return mprFuncionamiento;
			}).collect(Collectors.toList());
			String[] parts = SplitUsingTokenizer(mpr.getCiop_cat_ids_ruta(), ",");
			for (String catIdsRuta : parts) {
				try {
					pathMicroCatalogos = urlServidor + urlMicroCatalogos + "api/catalogo/findById/" + catIdsRuta;
					catalogosDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogos, token,
							CatalogoDTO.class);
					String aux = mpr.getNombres_ciop_cat_ids_ruta();
					if (aux != null) {
						mpr.setNombres_ciop_cat_ids_ruta(aux + "/" + catalogosDTO.getCatNombre());
					} else {
						mpr.setNombres_ciop_cat_ids_ruta(catalogosDTO.getCatNombre());
					}

				} catch (Exception e) {
					catalogosDTO = null;
				}
			}
			return mpr;
		}).collect(Collectors.toList());

		map.replace("data", cialco_oferta_p);
		Object map2 = mapper.convertValue(map, Object.class);
		LOGGER.info("/cialcofertaprod/findAllPaginated/{ciaId}" + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(map2);
	}

	public static String[] SplitUsingTokenizer(String subject, String delimiters) {
		StringTokenizer strTkn = new StringTokenizer(subject, delimiters);
		ArrayList<String> arrLis = new ArrayList<String>(subject.length());
		while (strTkn.hasMoreTokens())
			arrLis.add(strTkn.nextToken());
		return arrLis.toArray(new String[0]);
	}

	/****************************************************
	 * 
	 * ************ FUNCIONAMIENTO CIALCO ***************
	 * 
	 ****************************************************/

	/**
	 * Actualiza un registro de la tabla certificacion oferta productiva
	 * 
	 * @param certOferProd: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@PutMapping(value = "/funcionamientocialco/update/")
	@ApiOperation(value = "Actualiza un registro de la tabla Funcionamiento Cialco")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> updateFuncionamientoCialco(@RequestBody String data,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "funcionamientocialco/update/";
		Object res = consumer.doPut(pathMicro, data, token);
		LOGGER.info("/api/gopagro/funcionamientocialco/update/" + data + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	/**
	 * Devuelve todos los registros de la tabla cialco oferta productiva
	 * 
	 * @param auditCialco: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@GetMapping(value = "/funcionamientocialco/findAll")
	@ApiOperation(value = "Devuelve todos los registros de la tabla funcionamiento cialco")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> allFuncionamientoCialco(@RequestHeader(name = "Authorization") String token)
			throws JsonParseException, JsonMappingException, IOException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "/funcionamientocialco/findAll";
		Object res = consumer.doGet(pathMicro, token);
		LOGGER.info("/api/gopagro/funcionamientocialco/findAll" + "usuario" + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	/**
	 * Realiza un mantenimiento de estados del registro
	 * 
	 * @param auditCialco: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@PatchMapping(value = "/funcionamientocialco/state-record/")
	@ApiOperation(value = "Gestionar estado del registro ciaEstado={11 ACTIVO,12 INACTIVO}, ciaEliminado={false, true}, state: {disable, delete, activate}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> auditFuncionamientoCialco(@RequestBody String audit,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "/funcionamientocialco/state-record/";
		Object res = consumer.doPut(pathMicro, audit, token);
		LOGGER.info("/api/gopagro/funcionamientocialco/state-record/" + audit + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@GetMapping(value = "/funcionamientocialco/findById/{id}")
	@ApiOperation(value = "Busca un registro de la tabla funcionamiento cialco por id", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> funcionamientocialcoFindById(@PathVariable Long id,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "funcionamientocialco/findById/" + id;
		System.out.println("url: " + pathMicro);
		Object resp = consumer.doGet(pathMicro, token);
//		CatalogoDTO catalogosDTO = null;
//		CialcoOfertaProductiva ciofprod = convertEntityUtil.ConvertSingleEntityGET(pathMicro, token,
//				CialcoOfertaProductiva.class);
//
//		try {
//			pathMicroCatalogos = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
//					+ ciofprod.getCiopCatIdOferta();
//
//			catalogosDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogos, token, CatalogoDTO.class);
//			ciofprod.setNombre_cio_oferta(catalogosDTO.getCatNombre());
//		} catch (Exception e) {
//			catalogosDTO = null;
//		}
		LOGGER.info("/funcionamientocialco/findById/" + id + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(resp);
	}

	@GetMapping(value = "/funcionamientocialco/findByCiaId/{id}")
	@ApiOperation(value = "Busca un registro de la tabla funcionamiento cialco por id", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> funcionamientocialcoFindByCiaId(@PathVariable Long id,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "funcionamientocialco/findByCiaId/" + id;
		System.out.println("url: " + pathMicro);
		Object resp = consumer.doGet(pathMicro, token);
		LOGGER.info("/funcionamientocialco/findById/" + id + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(resp);
	}

	@PostMapping(value = "/funcionamientocialco/create/")
	@ApiOperation(value = "Guarda una funcionamiento cialco", response = Object.class)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> saveFuncionamientoCialco(@RequestBody String data,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "funcionamientocialco/create/";
		Object res = consumer.doPost(pathMicro, data, token);
		LOGGER.info("/api/gopagro/funcionamientocialco/create/" + data + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/funcionamientocialco/findAllPaginated/{ciaId}")
	@ApiOperation(value = "Busca una cialco por id", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> funcionamientoCialcoPaginated(@PathVariable Long ciaId, HttpServletRequest request,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "funcionamientocialco/findAllPaginated/" + ciaId + "/?"
				+ request.getQueryString();
		Object res = consumer.doGet(pathMicro, token);

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = mapper.convertValue(res, Map.class);

		List<FuncionamientoCialcoDTO> f_cialco = (List<FuncionamientoCialcoDTO>) convertEntityUtil
				.ConvertListEntity(FuncionamientoCialcoDTO.class, map.get("data"));

		f_cialco = f_cialco.stream().map(mpr -> {
			String pathMicroCatalogos = null;
			String pathMicroCatalogosHInicio = null;
			String pathMicroCatalogosHFin = null;
			CatalogoDTO catalogosDTO = null;
			CatalogoDTO catalogosDTOHoraInicio = null;
			CatalogoDTO catalogosDTOHoraFin = null;

			try {
				pathMicroCatalogos = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
						+ mpr.getFcia_id_cat_dia_funcionamiento();
				catalogosDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogos, token, CatalogoDTO.class);

				mpr.setNombre_cat_dia_funcionamiento(catalogosDTO.getCatNombre());
			} catch (Exception e) {
				catalogosDTO = null;
			}

			try {
				pathMicroCatalogosHInicio = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
						+ mpr.getFcia_id_cat_hora_inicio();
				catalogosDTOHoraInicio = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogosHInicio, token,
						CatalogoDTO.class);
				mpr.setNombre_fcia_id_cat_hora_inicio(catalogosDTOHoraInicio.getCatNombre());
			} catch (Exception e) {
				catalogosDTOHoraInicio = null;
			}

			try {
				pathMicroCatalogosHFin = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
						+ mpr.getFcia_id_cat_hora_fin();
				catalogosDTOHoraFin = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogosHFin, token,
						CatalogoDTO.class);
				mpr.setNombre_fcia_id_cat_hora_fin(catalogosDTOHoraFin.getCatNombre());
			} catch (Exception e) {
				catalogosDTOHoraFin = null;
			}
			return mpr;
		}).collect(Collectors.toList());

		map.replace("data", f_cialco);
		Object map2 = mapper.convertValue(map, Object.class);
		LOGGER.info("/funcionamientocialco/findAllPaginated/" + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(map2);
	}

	/****************************************************
	 * 
	 * ****************** MES COSECHA *******************
	 * 
	 ****************************************************/

	/**
	 * Realiza un mantenimiento de estados del registro
	 * 
	 * @param auditCialco: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@PatchMapping(value = "/mescosecha/state-record/")
	@ApiOperation(value = "Gestionar estado del registro ciaEstado={11 ACTIVO,12 INACTIVO}, ciaEliminado={false, true}, state: {disable, delete, activate}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> auditMescosecha(@RequestBody String audit,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "/mescosecha/state-record/";
		Object res = consumer.doPut(pathMicro, audit, token);
		LOGGER.info("/api/gopagro/mescosecha/state-record/" + audit + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	/**
	 * Devuelve todos los registros de la tabla cialco oferta productiva
	 * 
	 * @param auditCialco: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@GetMapping(value = "/mescosecha/findAll")
	@ApiOperation(value = "Devuelve todos los registros de la tabla MES COSECHA")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> allMesCosecha(@RequestHeader(name = "Authorization") String token)
			throws JsonParseException, JsonMappingException, IOException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "/mescosecha/findAll";
		Object res = consumer.doGet(pathMicro, token);
		LOGGER.info("/api/gopagro/mescosecha/findAll" + "usuario" + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@GetMapping(value = "/mescosecha/findById/{id}")
	@ApiOperation(value = "Busca un registro de la tabla mescosecha por id", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> findByIdMescosecha(@PathVariable Long id,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "mescosecha/findById/" + id;
		Object resp = consumer.doGet(pathMicro, token);
//		CatalogoDTO catalogosDTO = null;
//		CialcoOfertaProductiva ciofprod = convertEntityUtil.ConvertSingleEntityGET(pathMicro, token,
//				CialcoOfertaProductiva.class);
//
//		try {
//			pathMicroCatalogos = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
//					+ ciofprod.getCiopCatIdOferta();
//
//			catalogosDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogos, token, CatalogoDTO.class);
//			ciofprod.setNombre_cio_oferta(catalogosDTO.getCatNombre());
//		} catch (Exception e) {
//			catalogosDTO = null;
//		}
		LOGGER.info("/mescosecha/findById/" + id + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(resp);
	}

	/**
	 * Actualiza un registro de la tabla certificacion oferta productiva
	 * 
	 * @param certOferProd: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@PutMapping(value = "/mescosecha/update/")
	@ApiOperation(value = "Actualiza un registro de la tabla mescosecha")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> updateMescosecha(@RequestBody String data,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "mescosecha/update/";
		Object res = consumer.doPut(pathMicro, data, token);
		LOGGER.info("/api/gopagro/mescosecha/update/" + data + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@PostMapping(value = "/mescosecha/create/")
	@ApiOperation(value = "Guarda una mescosecha", response = Object.class)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> saveMescosecha(@RequestBody String data,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "mescosecha/create/";
		Object res = consumer.doPost(pathMicro, data, token);
		LOGGER.info("/api/gopagro/mescosecha/create/" + data + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	/****************************************************
	 * 
	 * *************** OFERTA DETALLE *******************
	 * 
	 ****************************************************/

	/**
	 * Realiza un mantenimiento de estados del registro
	 * 
	 * @param auditCialco: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@PatchMapping(value = "/ofertadetalle/state-record/")
	@ApiOperation(value = "Gestionar estado del registro ciaEstado={11 ACTIVO,12 INACTIVO}, ciaEliminado={false, true}, state: {disable, delete, activate}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> auditofertadetalle(@RequestBody String audit,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "/ofertadetalle/state-record/";
		Object res = consumer.doPut(pathMicro, audit, token);
		LOGGER.info("/api/gopagro/ofertadetalle/state-record/" + audit + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	/**
	 * Devuelve todos los registros de la tabla cialco oferta productiva
	 * 
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@GetMapping(value = "/ofertadetalle/findAll")
	@ApiOperation(value = "Devuelve todos los registros de la tabla OFERTA DETALLE")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> allofertadetalle(@RequestHeader(name = "Authorization") String token)
			throws JsonParseException, JsonMappingException, IOException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "/ofertadetalle/findAll";
		Object res = consumer.doGet(pathMicro, token);
		LOGGER.info("/api/gopagro/ofertadetalle/findAll" + "usuario" + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@GetMapping(value = "/ofertadetalle/findById/{id}")
	@ApiOperation(value = "Busca un registro de la tabla OFERTA DETALLE por id", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> findByIdofertadetalle(@PathVariable Long id,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "ofertadetalle/findById/" + id;
		Object resp = consumer.doGet(pathMicro, token);
//		CatalogoDTO catalogosDTO = null;
//		CialcoOfertaProductiva ciofprod = convertEntityUtil.ConvertSingleEntityGET(pathMicro, token,
//				CialcoOfertaProductiva.class);
//
//		try {
//			pathMicroCatalogos = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
//					+ ciofprod.getCiopCatIdOferta();
//
//			catalogosDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogos, token, CatalogoDTO.class);
//			ciofprod.setNombre_cio_oferta(catalogosDTO.getCatNombre());
//		} catch (Exception e) {
//			catalogosDTO = null;
//		}
		LOGGER.info("/ofertadetalle/findById/" + id + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(resp);
	}

	/**
	 * Actualiza un registro de la tabla Organizacion oferta productiva
	 * 
	 * @param certOferProd: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@PutMapping(value = "/ofertadetalle/update/")
	@ApiOperation(value = "Actualiza un registro de la tabla OFERTA DETALLE")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> updateOfertadetalle(@RequestBody String data,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "ofertadetalle/update/";
		Object res = consumer.doPut(pathMicro, data, token);
		LOGGER.info("/api/gopagro/ofertadetalle/update/" + data + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@PostMapping(value = "/ofertadetalle/create/")
	@ApiOperation(value = "Guarda un registro en la tabla OFERTA DETALLE", response = Object.class)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> saveofertadetalle(@RequestBody String data,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "ofertadetalle/create/";
		Object res = consumer.doPost(pathMicro, data, token);
		LOGGER.info("/api/gopagro/ofertadetalle/create/" + data + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	/****************************************************
	 * 
	 * ************ ORGANIZACION CIALCO *****************
	 * 
	 ****************************************************/

	/**
	 * Realiza un mantenimiento de estados del registro
	 * 
	 * @param auditCialco: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@PatchMapping(value = "/orgcialco/state-record/")
	@ApiOperation(value = "Gestionar estado del registro ciaEstado={11 ACTIVO,12 INACTIVO}, ciaEliminado={false, true}, state: {disable, delete, activate}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> auditoorgcialco(@RequestBody String audit,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "/orgcialco/state-record/";
		Object res = consumer.doPut(pathMicro, audit, token);
		LOGGER.info("/api/gopagro/ofertadetalle/state-record/" + audit + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	/**
	 * Devuelve todos los registros de la tabla cialco oferta productiva
	 * 
	 * @param auditCialco: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@GetMapping(value = "/orgcialco/findAll")
	@ApiOperation(value = "Devuelve todos los registros de la tabla ORGANIZACION CIALCO")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> allorgcialco(@RequestHeader(name = "Authorization") String token)
			throws JsonParseException, JsonMappingException, IOException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "/orgcialco/findAll";
		Object res = consumer.doGet(pathMicro, token);
		LOGGER.info("/api/gopagro/orgcialco/findAll" + "usuario" + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@GetMapping(value = "/orgcialco/findById/{id}")
	@ApiOperation(value = "Busca un registro de la tabla ORGANIZACION CIALCO por id", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> findByIdOrgcialco(@PathVariable Long id,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "orgcialco/findById/" + id;
		Object resp = consumer.doGet(pathMicro, token);
//		CatalogoDTO catalogosDTO = null;
//		CialcoOfertaProductiva ciofprod = convertEntityUtil.ConvertSingleEntityGET(pathMicro, token,
//				CialcoOfertaProductiva.class);
//
//		try {
//			pathMicroCatalogos = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
//					+ ciofprod.getCiopCatIdOferta();
//
//			catalogosDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogos, token, CatalogoDTO.class);
//			ciofprod.setNombre_cio_oferta(catalogosDTO.getCatNombre());
//		} catch (Exception e) {
//			catalogosDTO = null;
//		}
		LOGGER.info("/findById/findById/" + id + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(resp);
	}

	/**
	 * Actualiza un registro de la tabla Organizacion oferta productiva
	 * 
	 * @param certOferProd: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@PutMapping(value = "/orgcialco/update/")
	@ApiOperation(value = "Actualiza un registro de la tabla ORGANIZACION CIALCO")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> updateOrgcialco(@RequestBody String data,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "orgcialco/update/";
		Object res = consumer.doPut(pathMicro, data, token);
		LOGGER.info("/api/gopagro/orgcialco/update/" + data + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@PostMapping(value = "/orgcialco/create/")
	@ApiOperation(value = "Guarda un registro en la tabla ORGANIZACION CIALCO", response = Object.class)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> saveOrgcialco(@RequestBody String data,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "orgcialco/create/";
		Object res = consumer.doPost(pathMicro, data, token);
		LOGGER.info("/api/gopagro/orgcialco/create/" + data + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	/****************************************************
	 * 
	 * ******* ORGANIZACION OFERTA PRODUCTIVA ***********
	 * 
	 ****************************************************/

	/**
	 * Realiza un mantenimiento de estados del registro
	 * 
	 * @param auditCialco: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@PatchMapping(value = "/orgofertaproductiva/state-record/")
	@ApiOperation(value = "Gestionar estado del registro ciaEstado={11 ACTIVO,12 INACTIVO}, ciaEliminado={false, true}, state: {disable, delete, activate}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> auditorgofertaproductiva(@RequestBody String audit,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "/orgofertaproductiva/state-record/";
		Object res = consumer.doPut(pathMicro, audit, token);
		LOGGER.info("/api/gopagro/ofertadetalle/state-record/" + audit + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	/**
	 * Devuelve todos los registros de la tabla ORGANIZACION OFERTA PRODUCTIVA
	 * 
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@GetMapping(value = "/orgofertaproductiva/findAll")
	@ApiOperation(value = "Devuelve todos los registros de la tabla ORGANIZACION OFERTA PRODUCTIVA")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> allorgofertaproductiva(@RequestHeader(name = "Authorization") String token)
			throws JsonParseException, JsonMappingException, IOException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "/orgofertaproductiva/findAll";
		Object res = consumer.doGet(pathMicro, token);
		LOGGER.info("/api/gopagro/orgofertaproductiva/findAll" + "usuario" + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@GetMapping(value = "/orgofertaproductiva/findById/{id}")
	@ApiOperation(value = "Busca un registro de la tabla ORGANIZACION OFERTA PRODUCTIVA por id", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> findByIdorgofertaproductiva(@PathVariable Long id,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "orgofertaproductiva/findById/" + id;
		Object resp = consumer.doGet(pathMicro, token);
//		CatalogoDTO catalogosDTO = null;
//		CialcoOfertaProductiva ciofprod = convertEntityUtil.ConvertSingleEntityGET(pathMicro, token,
//				CialcoOfertaProductiva.class);
//
//		try {
//			pathMicroCatalogos = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
//					+ ciofprod.getCiopCatIdOferta();
//
//			catalogosDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogos, token, CatalogoDTO.class);
//			ciofprod.setNombre_cio_oferta(catalogosDTO.getCatNombre());
//		} catch (Exception e) {
//			catalogosDTO = null;
//		}
		LOGGER.info("/orgofertaproductiva/findById/" + id + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(resp);
	}

	/**
	 * Actualiza un registro de la tabla Organizacion oferta productiva
	 * 
	 * @param certOferProd: json de entrada
	 * @return ResponseController: Retorna el estado del registro
	 * @throws IOException
	 */
	@PutMapping(value = "/orgofertaproductiva/update/")
	@ApiOperation(value = "Actualiza un registro de la tabla ORGANIZACION OFERTA PRODUCTIVA")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> updateorgofertaproductiva(@RequestBody String data,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "orgofertaproductiva/update/";
		Object res = consumer.doPut(pathMicro, data, token);
		LOGGER.info("/api/gopagro/orgcialco/update/" + data + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	@PostMapping(value = "/orgofertaproductiva/create/")
	@ApiOperation(value = "Guarda un registro en la tabla ORGANIZACION OFERTA PRODUCTIVA", response = Object.class)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> saveorgofertaproductiva(@RequestBody String data,
			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String pathMicro = null;
		pathMicro = urlServidor + urlMicroGopAgro + "orgofertaproductiva/create/";
		Object res = consumer.doPost(pathMicro, data, token);
		LOGGER.info("/api/gopagro/orgofertaproductiva/create/" + data + " usuario: " + util.filterUsuId(token));
		return ResponseEntity.ok(res);
	}

	/****************************************************
	 * 
	 * **************** TIPOLOGIA NIVEL *****************
	 * 
	 ****************************************************/

//	/**
//	 * Realiza un mantenimiento de estados del registro
//	 * 
//	 * @param auditCialco: json de entrada
//	 * @return ResponseController: Retorna el estado del registro
//	 * @throws IOException
//	 */
//	@PatchMapping(value = "/tipologiaNivel/state-record/")
//	@ApiOperation(value = "Gestionar estado del registro ciaEstado={11 ACTIVO,12 INACTIVO}, ciaEliminado={false, true}, state: {disable, delete, activate}")
//	@ResponseStatus(HttpStatus.OK)
//	public ResponseEntity<?> auditTipologiaNivel(@RequestBody String audit,
//			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
//			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
//		String pathMicro = null;
//		pathMicro = urlServidor + urlMicroGopAgro + "/tipologiaNivel/state-record/";
//		Object res = consumer.doPut(pathMicro, audit, token);
//		LOGGER.info("/api/gopagro/ofertadetalle/state-record/" + audit + " usuario: " + util.filterUsuId(token));
//		return ResponseEntity.ok(res);
//	}
//
//	/**
//	 * Devuelve todos los registros de la tabla TIPOLOGIA NIVEL
//	 * 
//	 * @param auditCialco: json de entrada
//	 * @return ResponseController: Retorna el estado del registro
//	 * @throws IOException
//	 */
//	@GetMapping(value = "/tipologiaNivel/findAll")
//	@ApiOperation(value = "Devuelve todos los registros de la tabla TIPOLOGIA NIVEL")
//	@ResponseStatus(HttpStatus.OK)
//	public ResponseEntity<?> alltipologiaNivel(@RequestHeader(name = "Authorization") String token)
//			throws JsonParseException, JsonMappingException, IOException, NoSuchFieldException, SecurityException,
//			IllegalArgumentException, IllegalAccessException {
//		String pathMicro = null;
//		pathMicro = urlServidor + urlMicroGopAgro + "/tipologiaNivel/findAll";
//		Object res = consumer.doGet(pathMicro, token);
//		LOGGER.info("/api/gopagro/tipologiaNivel/findAll" + "usuario" + util.filterUsuId(token));
//		return ResponseEntity.ok(res);
//	}
//
//	@GetMapping(value = "/tipologiaNivel/findById/{id}")
//	@ApiOperation(value = "Busca un registro de la tabla TIPOLOGIA NIVEL por id", response = Object.class)
//	@ResponseStatus(HttpStatus.OK)
//	public ResponseEntity<?> findByIdtipologiaNivel(@PathVariable Long id,
//			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
//			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
//		String pathMicro = null;
//		pathMicro = urlServidor + urlMicroGopAgro + "tipologiaNivel/findById/" + id;
//		Object resp = consumer.doGet(pathMicro, token);
////		CatalogoDTO catalogosDTO = null;
////		CialcoOfertaProductiva ciofprod = convertEntityUtil.ConvertSingleEntityGET(pathMicro, token,
////				CialcoOfertaProductiva.class);
////
////		try {
////			pathMicroCatalogos = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
////					+ ciofprod.getCiopCatIdOferta();
////
////			catalogosDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogos, token, CatalogoDTO.class);
////			ciofprod.setNombre_cio_oferta(catalogosDTO.getCatNombre());
////		} catch (Exception e) {
////			catalogosDTO = null;
////		}
//		LOGGER.info("/tipologiaNivel/findById/" + id + " usuario: " + util.filterUsuId(token));
//		return ResponseEntity.ok(resp);
//	}

//	/**
//	 * Actualiza un registro de la tabla Topologia nivel
//	 * 
//	 * @param certOferProd: json de entrada
//	 * @return ResponseController: Retorna el estado del registro
//	 * @throws IOException
//	 */
//	@PutMapping(value = "/tipologiaNivel/update/")
//	@ApiOperation(value = "Actualiza un registro de la tabla TIPOLOGIA NIVEL")
//	@ResponseStatus(HttpStatus.OK)
//	public ResponseEntity<?> updatetipologiaNivel(@RequestBody String data,
//			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
//			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
//		String pathMicro = null;
//		pathMicro = urlServidor + urlMicroGopAgro + "tipologiaNivel/update/";
//		Object res = consumer.doPut(pathMicro, data, token);
//		LOGGER.info("/api/gopagro/tipologiaNivel/update/" + data + " usuario: " + util.filterUsuId(token));
//		return ResponseEntity.ok(res);
//	}
//
//	/***************************************
//	 * @throws IllegalAccessException
//	 * @throws IllegalArgumentException
//	 * @throws SecurityException
//	 * @throws NoSuchFieldException
//	 ***************************************/
//
//	@PostMapping(value = "/tipologiaNivel/create/")
//	@ApiOperation(value = "Guarda un registro en la tabla TIPOLOGIA NIVEL", response = Object.class)
//	@ResponseStatus(HttpStatus.CREATED)
//	public ResponseEntity<?> saveTipologiaNivel(@RequestBody String data,
//			@RequestHeader(name = "Authorization") String token) throws JsonParseException, JsonMappingException,
//			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
//		String pathMicro = null;
//		pathMicro = urlServidor + urlMicroGopAgro + "tipologiaNivel/create/";
//		Object res = consumer.doPost(pathMicro, data, token);
//		LOGGER.info("/api/gopagro/tipologiaNivel/create/" + data + " usuario: " + util.filterUsuId(token));
//		return ResponseEntity.ok(res);
//	}

	@Override
	public String getErrorPath() {
		return PATH;
	}

}
