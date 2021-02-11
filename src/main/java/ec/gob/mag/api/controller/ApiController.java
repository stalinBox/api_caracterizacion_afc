package ec.gob.mag.api.controller;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;




@RestController
@RequestMapping("/api")
@ApiResponses(value = { @ApiResponse(code = 200, message = "SUCESS"),
		@ApiResponse(code = 404, message = "RESOURCE NOT FOUND"), @ApiResponse(code = 400, message = "BAD REQUEST"),
		@ApiResponse(code = 201, message = "CREATED"), @ApiResponse(code = 401, message = "UNAUTHORIZED"),
		@ApiResponse(code = 415, message = "UNSUPPORTED TYPE - Representation not supported for the resource"),
		@ApiResponse(code = 500, message = "SERVER ERROR") })
public class ApiController implements ErrorController {

	private static final String PATH = "/error";
	public static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ApiController.class);

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

	@Value("${url.micro.cialco}")
	private String urlMicroCialco;

	/***************************************
	 * SECCION - END-POINTS
	 ***************************************/

	@RequestMapping(value = "/cialco/findall", method = RequestMethod.GET)
	@ApiOperation(value = "consumo de micro cialco", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> getCialco(
			@RequestHeader(name = "Authorization") String auth) {
		String pathMicro = urlServidor + urlMicroCialco + "/findAll";
		Object response = consumer.doGet(pathMicro, auth);
		LOGGER.info("cialco/findall");
		return ResponseEntity.ok(response);
	}
	
	@Override
	public String getErrorPath() {
		return PATH;
	}
}