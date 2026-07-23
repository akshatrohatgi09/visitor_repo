package com.police.evisitor.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ClientAESGCMAdvice implements RequestBodyAdvice, ResponseBodyAdvice {

	@Autowired
	private MasterCryptoAESGCM masterCrypto;

	@Value("${encrypted.key}")
	private String encryptedKey;

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		Method method = methodParameter.getMethod();
		return addAdviceAnnotation(method);
		// return true;
	}

	private boolean addAdviceAnnotation(Method method) {
		return method.isAnnotationPresent(AddAESGCMAdvice.class)
				|| method.getDeclaringClass().isAnnotationPresent(AddAESGCMAdvice.class);
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {

		StringBuilder resultStringBuilder = new StringBuilder();

		try (InputStreamReader isr = new InputStreamReader(inputMessage.getBody()); // , StandardCharsets.UTF_8
				BufferedReader bufferedReader = new BufferedReader(isr)) {

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}

			String inputData = resultStringBuilder.toString();
			log.info(" RequestRespModifyAdvice beforeBodyRead inputData {} " + inputData);
			JSONObject jsonObject = new JSONObject(inputData);
			String encryptedData = jsonObject.optString("v1", "");
			String encodeBase64IV = jsonObject.optString("v2", "");
			RequestContextHolder.getRequestAttributes().setAttribute("encryptedKey", encryptedKey,
					RequestAttributes.SCOPE_REQUEST);
			String decryptedData = masterCrypto.decrypt(encryptedData,encryptedKey, encodeBase64IV );

			log.info(" RequestRespModifyAdvice beforeBodyRead decryptedData {} ", decryptedData);
			// adding encryptionKey so we can resuse while encrypting the response

			return new HttpInputMessage() {
				@Override
				public InputStream getBody() throws IOException {
					return new ByteArrayInputStream(decryptedData.getBytes(StandardCharsets.UTF_8));
				}

				@Override
				public HttpHeaders getHeaders() {
					return inputMessage.getHeaders();
				}
			};
		}
	}

	@Override
	public String afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		StringBuilder resultStringBuilder = new StringBuilder();
		String inputData = "";
		try (InputStreamReader isr = new InputStreamReader(inputMessage.getBody()); // , StandardCharsets.UTF_8
				BufferedReader bufferedReader = new BufferedReader(isr)) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
			inputData = resultStringBuilder.toString();
		} catch (IOException e) {
			log.error(" Error in RequestRespModifyAdvice afterBodyRead ", e);
		}
		return inputData;
	}

	@Override
	public HttpInputMessage handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return inputMessage;
	}

	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		Method method = returnType.getMethod();
		return addAdviceAnnotation(method);
		// return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType,
			org.springframework.http.MediaType selectedContentType, Class selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		JSONObject apiResponse = new JSONObject();
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		String encryptionKey = requestAttributes.getAttribute("encryptedKey", RequestAttributes.SCOPE_REQUEST)
				.toString();
		try {
			String encodeBase64IV = masterCrypto.generateBase64IV();
			String jsonObject = "";
			if (body != null) {
				jsonObject = body.toString();
				// initVector should be encoded while decrypting
				log.info("RequestRespModifyAdvice beforeBodyWrite initVector {} ", encodeBase64IV);
				apiResponse.put("v1", masterCrypto.encrypt(jsonObject, encryptionKey, encodeBase64IV));
				apiResponse.put("v2", encodeBase64IV);
			}
		} catch (Exception e) {
			log.error("Error in RequestRespModifyAdvice beforeBodyWriteex ", e);
		}
		log.info("RequestRespModifyAdvice beforeBodyWrite beatResponseWrapper {} ", apiResponse.toString());
		return apiResponse.toString();
	}

}
