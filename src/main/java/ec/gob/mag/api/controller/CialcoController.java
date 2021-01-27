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

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import ec.gob.mag.api.util.Consumer;
import ec.gob.mag.api.util.ConvertEntityUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/apicialco")
@ApiResponses(value = { @ApiResponse(code = 200, message = "SUCESS"),
		@ApiResponse(code = 404, message = "RESOURCE NOT FOUND"), @ApiResponse(code = 400, message = "BAD REQUEST"),
		@ApiResponse(code = 201, message = "CREATED"), @ApiResponse(code = 401, message = "UNAUTHORIZED"),
		@ApiResponse(code = 415, message = "UNSUPPORTED TYPE - Representation not supported for the resource"),
		@ApiResponse(code = 500, message = "SERVER ERROR") })

public class CialcoController implements ErrorController{
	
	private static final String PATH = "/error";
	public static final Logger LOGGER = LoggerFactory.getLogger(CialcoController.class);

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
	@Value("${url.micro.afc}")
	private String urlServidor;

	@Value("${url.cialco}")
	private String urlmicrocialco;

	/***************************************
	 * SECCION - END-POINTS
	 ***************************************/
	@RequestMapping(value = "/findAll", method = RequestMethod.GET)
	@ApiOperation(value = "Api de Consumo Microservicio Cialco", response = Object.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> getCialco() throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException, IOException {
		String pathMicro = urlServidor + urlmicrocialco +"cialco/findAll";
		String auth = "";
		Object response = consumer.doGet(pathMicro, auth);
		LOGGER.info("/findAll");
		return ResponseEntity.ok(response);
	}
	
	
	
	
	@Override
	public String getErrorPath() {
		return PATH;
	}

}
