package ec.gob.mag.api.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import ec.gob.mag.api.exception.CmBadRequestException;
import ec.gob.mag.api.exception.CmInternalErrorException;
import ec.gob.mag.api.exception.CmNotFoundException;
import ec.gob.mag.api.exception.CmUnauthorizedRequestException;
import ec.gob.mag.api.exception.CmUnsupportedTypeRequestException;
import ec.gob.mag.api.exception.EnumCodeExceptions;
import ec.gob.mag.api.exception.EnumTypeExceptions;
import ec.gob.mag.api.exception.ExceptionResponse;
import ec.gob.mag.api.exception.MyExceptionUtility;
import ec.gob.mag.api.exception.MyNotFoundException;

@Service("consumer")
public class Consumer {

	@Autowired
	private MessageSource messageSource;

	public String readerInputStream(InputStream input) throws IOException {
		String output = "", res = "";
		BufferedReader br = new BufferedReader(new InputStreamReader((input)));
		try {
			while ((output = br.readLine()) != null)
				res = output;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		br.close();
		return res;
	}

	private Object convertStringToObject(String data) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Object responseObj;
		try {
			responseObj = mapper.readValue(data, Object.class);
		} catch (IOException e) {
			String msg = MyExceptionUtility.buildExceptionJsonString("error.body_response.message", null,
					this.getClass(), "convertStringToObject", EnumTypeExceptions.CRITICAL,
					EnumCodeExceptions.ERROR_MICRO_SERVICE_OFF, messageSource);
			throw new MyNotFoundException(msg);
		}
		return responseObj;
	}

	private ExceptionResponse convertStringToErrorObject(String data) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ExceptionResponse responseObj;
		try {
			responseObj = mapper.readValue(data, ExceptionResponse.class);
		} catch (IOException e) {
			String msg = MyExceptionUtility.buildExceptionJsonString("error.body_response.message", null,
					this.getClass(), "convertStringToObject", EnumTypeExceptions.CRITICAL,
					EnumCodeExceptions.ERROR_MICRO_SERVICE_OFF, messageSource);
			throw new MyNotFoundException(msg);

		}
		return responseObj;
	}

	private void launchException(ExceptionResponse ex) {
		// Transformar a json
		String msg = MyExceptionUtility.buildExceptionMicroJsonString(ex, this.getClass(), "launchException",
				EnumTypeExceptions.WARN, EnumCodeExceptions.ERROR_IN_MICRO_SERVICE, messageSource);

		switch (ex.getStatus().value()) {
		case 404:
			throw new CmNotFoundException(msg);
		case 400:
			throw new CmBadRequestException(msg);
		case 401:
			throw new CmUnauthorizedRequestException(msg);
		case 415:
			throw new CmUnsupportedTypeRequestException(msg);
		case 500:
			throw new CmInternalErrorException(msg);
		default:
			throw new CmInternalErrorException(msg);
		}
	}

	// FUNCIONES PARA CONSUMO DOGET - DOPOST ...
	public Object doGet(String urlString, String accessToken) {
		// TODO Auto-generated method stub
		Object objectResponse;
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Authorization", accessToken);
			if (conn.getResponseCode() != 200) {
				String responseError = readerInputStream(conn.getErrorStream());
				ExceptionResponse objectErrorResponse = convertStringToErrorObject(responseError);
				launchException(objectErrorResponse);
			}
			String responseError = readerInputStream(conn.getInputStream());
			objectResponse = convertStringToObject(responseError);
			conn.disconnect();
			return objectResponse;

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object doPost(String urlString, String data, String accessToken) throws IOException {
		URL object = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) object.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", accessToken);
		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		wr.write(data.toString());
		wr.flush();
		int HttpResult = con.getResponseCode();
		if (HttpResult == HttpURLConnection.HTTP_OK || HttpResult == HttpURLConnection.HTTP_CREATED) {
			String responseError = readerInputStream(con.getInputStream());
			Object objResponse = convertStringToObject(responseError);
			return objResponse;
		} else {
			String responseError = readerInputStream(con.getErrorStream());
			ExceptionResponse objectErrorResponse = convertStringToErrorObject(responseError);
			launchException(objectErrorResponse);
		}
		return null;
	}

	public Object doPut(String urlString, String data, String accessToken) throws IOException {
		URL object = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) object.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestMethod("PUT");
		con.setRequestProperty("Authorization", accessToken);
		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		wr.write(data.toString());
		wr.flush();
		int HttpResult = con.getResponseCode();
		if (HttpResult == HttpURLConnection.HTTP_OK || HttpResult == HttpURLConnection.HTTP_CREATED) {
			String responseError = readerInputStream(con.getInputStream());
			Object objResponse = convertStringToObject(responseError);
			return objResponse;
		} else {
			String responseError = readerInputStream(con.getErrorStream());
			ExceptionResponse objectErrorResponse = convertStringToErrorObject(responseError);
			launchException(objectErrorResponse);
		}
		return null;
	}

	public Object doDelete(String urlString, String accessToken) {
		// TODO Auto-generated method stub
		Object objectResponse;
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("DELETE");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Authorization", accessToken);
			if (conn.getResponseCode() != 200) {
				String responseError = readerInputStream(conn.getErrorStream());
				ExceptionResponse objectErrorResponse = convertStringToErrorObject(responseError);
				launchException(objectErrorResponse);
			}
			String responseError = readerInputStream(conn.getInputStream());
			objectResponse = convertStringToObject(responseError);
			conn.disconnect();
			return objectResponse;

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object doPatch(String urlString, String data, String accessToken) throws IOException {
		URL object = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) object.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestMethod("PATCH");
		con.setRequestProperty("X-HTTP-Method-Override", "PATCH");
		con.setRequestProperty("Authorization", accessToken);
		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		wr.write(data.toString());
		wr.flush();
		int HttpResult = con.getResponseCode();
		if (HttpResult == HttpURLConnection.HTTP_OK || HttpResult == HttpURLConnection.HTTP_CREATED) {
			String responseError = readerInputStream(con.getInputStream());
			Object objResponse = convertStringToObject(responseError);
			return objResponse;
		} else {
			String responseError = readerInputStream(con.getErrorStream());
			ExceptionResponse objectErrorResponse = convertStringToErrorObject(responseError);
			launchException(objectErrorResponse);
		}
		return null;
	}
}
