package org.aztec.autumn.common.utils.ws;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

public class RestApiProxy implements InvocationHandler {

	private String baseUrl;

	public RestApiProxy(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		if (method.getAnnotation(GET.class) != null)
			return ApiInvocationUtils.invokeGet(getRequestUrl(method), method.getReturnType(), method.getReturnType(),
					getRequestParam(args, method, PathParam.class), getRequestParam(args, method, HeaderParam.class),
					getRequestParam(args, method, QueryParam.class));
		else if (method.getAnnotation(POST.class) != null)
			return ApiInvocationUtils.invokePost(getRequestUrl(method), method.getReturnType(), method.getReturnType(),
					getRequestParam(args, method, PathParam.class), getRequestParam(args, method, HeaderParam.class),
					getRequestParam(args, method, FormParam.class), null);
		else if (method.getAnnotation(PUT.class) != null)
			return ApiInvocationUtils.invokePut(getRequestUrl(method), method.getReturnType(), method.getReturnType(),
					getRequestParam(args, method, PathParam.class), getRequestParam(args, method, HeaderParam.class),
					getRequestParam(args, method, FormParam.class));
		else if (method.getAnnotation(DELETE.class) != null)
			return ApiInvocationUtils.invokeDelete(getRequestUrl(method), method.getReturnType(),
					method.getReturnType(), getRequestParam(args, method, PathParam.class),
					getRequestParam(args, method, HeaderParam.class), getRequestParam(args, method, FormParam.class));
		return null;
	}

	private String getRequestUrl(Method method) throws NoSuchMethodException, SecurityException {
		Path pathAnno = method.getAnnotation(Path.class);
		return baseUrl + pathAnno.value();
	}

	private Map<String, String> getRequestParam(Object[] invokeParams, Method method, Class httpParamType)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Map<String, String> retParams = new HashMap<String, String>();
		Parameter[] params = method.getParameters();
		for (int i = 0; i < params.length; i++) {
			if (invokeParams[i] == null)
				continue;
			Parameter param = params[i];
			Annotation paramAnno = param.getAnnotation(httpParamType);
			if (paramAnno != null) {
				String paramName = (String) httpParamType.getDeclaredMethod("value").invoke(paramAnno);
				retParams.put(paramName, (String) invokeParams[i]);
			}
		}
		return retParams;
	}

}
