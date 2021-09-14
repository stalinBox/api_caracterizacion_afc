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

	// *** PAGINADOS
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

			// SPLIT CIOP_CAT_IDS_RUTA

			System.out.println("***** ids catalogos para Split: " + mpr.getCiop_cat_ids_ruta());
			String[] parts = SplitUsingTokenizer(mpr.getCiop_cat_ids_ruta(), ",");

			System.out.println("/////// PARTES: " + parts);

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

			try {
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

//		try {
//			pathMicroCatalogos = urlServidor + urlMicroCatalogos + "api/catalogo/findById/"
//					+ cialco.getCiop_cat_id_oferta();
//			catalogosDTO = convertEntityUtil.ConvertSingleEntityGET(pathMicroCatalogos, token, CatalogoDTO.class);
//			mpr.setNombre_cio_oferta(catalogosDTO.getCatNombre());
//		} catch (Exception e) {
//			catalogosDTO = null;
//		}

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

	@Override
	public String getErrorPath() {
		return PATH;
	}

}
