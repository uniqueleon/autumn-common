package org.aztec.autumn.common.utils.ws;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiInvocationUtils {

	private static final long DATA_ACCEPT_INTERVAL = 1000;
	private static Logger logger = LoggerFactory.getLogger(ApiInvocationUtils.class);
	private static final String pathParamRegex = "\\{[\\w+|_|-]+\\}";

	public static void main(String[] args) {
		String url = "add/{param1}/sub/{param2}";
		Map<String, String> params = new HashMap<String, String>();
		params.put("param1", "1");
		params.put("param2", "2");
		System.out.println(getParameterizedUrl(params, url));
	}

	private static String getParameterizedUrl(Map<String, String> pathParams, String originUrl) {
		String retUrl = originUrl;
		if (pathParams == null || pathParams.size() == 0)
			return retUrl;
		for (String key : pathParams.keySet()) {
			retUrl = retUrl.replaceAll("\\{" + key + "\\}", pathParams.get(key));
		}
		return retUrl;
	}


	/**
	 * 调用Get方法类型的远程Web service
	 * 
	 * @param theUrl
	 *            web service的URL(统一资源定位符)
	 * @param retDataCls
	 *            返回值的数据类型
	 * @param entityCls
	 *            实体类型
	 * @param pathParams
	 *            路径参数，以字符串键，值对的Map对象形式传入，支持多个对象，以英文逗号隔开。
	 * @param headers
	 *            头信息参数
	 * @param parameters
	 *            一般参数
	 * @return 调用结果
	 * @throws IOException
	 *             出现网络异常时抛出
	 * @throws HttpException
	 *             与Http协议相关的异常抛出
	 * @throws UnsupportedEncodingException
	 *             当使用不存在的编码方式时抛出，因为默认使用的是UTF-8编码，理论上不会出现这个异常
	 * @throws ApiException
	 *             调用API接口产生的异常，通常都会把所有可以截获的异常归类为这一异常。
	 */
	public static <T, E> T invokeGet(String theUrl,Class<T> retDataCls, Class<E> entityCls,
			Map<String, String> pathParams, Map<String, String> headers, Map<String, String> parameters) throws ApiException{
		return invokeGet(theUrl, retDataCls, entityCls, pathParams, headers, parameters,null,null);
	}
	
	public static <T, E> T invokeGet(String theUrl, Class<T> retDataCls, Class<E> entityCls,
			Map<String, String> pathParams, Map<String, String> headers, Map<String, String> parameters,String proxyHost,Integer proxyPort)
			throws ApiException {
		try {
			GetMethod getMethod = new GetMethod(getParameterizedUrl(pathParams, theUrl));
			if (headers != null) {
				for (String headerName : headers.keySet()) {
					getMethod.setRequestHeader(headerName, headers.get(headerName));
				}
			}
			HttpClient httpClient = new HttpClient();
			getMethod.setRequestHeader("content-type", "application/x-www-form-urlencoded;charset=UTF-8");
			// getMethod.setRequestHeader("charset", "utf-8");
			getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			HttpMethodParams params = new HttpMethodParams();
			if (parameters != null) {
				for (String paramName : parameters.keySet()) {
					params.setParameter(paramName, parameters.get(paramName));
				}
			}
			getMethod.setParams(new HttpMethodParams());
			List<NameValuePair> paires = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> param : parameters.entrySet()) {
				paires.add(new NameValuePair(param.getKey(), param.getValue()));
			}
			getMethod.setQueryString(paires.toArray(new NameValuePair[paires.size()]));
			HttpClient httpclient = new HttpClient();
			if(proxyHost != null && proxyPort != null)
				setProxy(httpclient, proxyHost, proxyPort);
			int result = httpclient.executeMethod(getMethod);
			return getResultData(getMethod, retDataCls, entityCls);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApiException(e.getMessage(), e);
		}
	}
	
	
	private static void setProxy(HttpClient client,String ip,int port){
		HostConfiguration hostConfiguration = new HostConfiguration();;
		hostConfiguration.setHost(ip, port);
		client.setHostConfiguration(hostConfiguration);
	}

	private static <T, E> T getJsonData(String jsonData, Class<T> retType, Class<E> entityCls) throws Exception {
		List datas = new ArrayList();
		JsonUtils util = UtilsFactory.getInstance().getJsonUtils();
		if (jsonData.startsWith("{")) {
			return util.json2Object(jsonData, retType);
		} else if (jsonData.startsWith("[")) {
			E[] beans = (E[]) util.json2Array(jsonData, entityCls);
			for (E bean : beans) {
				datas.add(bean);
			}
		}
		return (T) datas;
	}
	
	private static <T, E> T getXmlData(String xmlData,Class<T> retType, Class<E> entityCls){
		return null;
	}

	private static String parseParamsMap2String(Map<String, String> paramMap) {
		if (paramMap == null)
			return null;
		StringBuilder paramString = new StringBuilder();
		for (String paramName : paramMap.keySet()) {
			if (paramMap.get(paramName) == null)
				continue;
			if (!paramString.toString().isEmpty()) {
				paramString.append("&");
			}
			paramString.append(paramName + "=" + paramMap.get(paramName));
		}
		return paramString.toString();
	}

	/**
	 * 调用Get方法类型的远程Web service
	 * 
	 * @param theUrl
	 *            web service的URL(统一资源定位符)
	 * @param retDataCls
	 *            返回值的数据类型
	 * @param entityCls
	 *            实体类型
	 * @param pathParams
	 *            路径参数，以字符串键，值对的Map对象形式传入，支持多个对象，以英文逗号隔开。
	 * @param headers
	 *            头信息参数
	 * @param parameters
	 *            一般参数
	 * @return 调用结果
	 * @throws IOException
	 *             出现网络异常时抛出
	 * @throws HttpException
	 *             与Http协议相关的异常抛出
	 * @throws UnsupportedEncodingException
	 *             当使用不存在的编码方式时抛出，因为默认使用的是UTF-8编码，理论上不会出现这个异常
	 * @throws ApiException
	 *             调用API接口产生的异常，通常都会把所有可以截获的异常归类为这一异常。
	 */
	public static <T, E> T invokePut(String theUrl, Class<T> retDataCls, Class<E> entityCls,
			Map<String, String> pathParams, Map<String, String> headers, Map<String, String> parameters)
			throws ApiException {
		try {
			PutMethod put = new PutMethod(getParameterizedUrl(pathParams, theUrl));
			put.setRequestEntity(new StringRequestEntity(parseParamsMap2String(parameters),
					"application/x-www-form-urlencoded", "UTF-8"));
			if (headers != null) {
				for (String headerName : headers.keySet()) {
					put.setRequestHeader(headerName, headers.get(headerName));
				}
			}
			HttpClient httpclient = new HttpClient();
			int result = httpclient.executeMethod(put);
			return getResultData(put, retDataCls, entityCls);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApiException(e.getMessage(), e);
		}
	}
	

	private static <T, E> T getResultData(HttpMethod method, Class<T> retDataCls, Class<E> entityCls) throws Exception {
		
		if(retDataCls.equals(InputStream.class))
			return (T) method.getResponseBodyAsStream();
		String jsonStr = new String(method.getResponseBody(), "UTF-8");
		if (jsonStr == null || jsonStr.equals(""))
			throw new ApiException("No data has been receive from remote API Server!");
		if(retDataCls.equals(String.class)){
			return (T) jsonStr;
		}
		T resultData = null;
		try {
			resultData = (T) getJsonData(jsonStr, retDataCls, entityCls);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultData;
	}

	/**
	 * 调用Post方法类型的远程Web service
	 * 
	 * @param theUrl
	 *            web service的URL(统一资源定位符)
	 * @param retDataCls
	 *            返回值的数据类型
	 * @param entityCls
	 *            实体类型
	 * @param pathParams
	 *            路径参数，以字符串键，值对的Map对象形式传入，支持多个对象，以英文逗号隔开。
	 * @param headers
	 *            头信息参数
	 * @param parameters
	 *            一般参数
	 * @return 调用结果
	 * @throws IOException
	 *             出现网络异常时抛出
	 * @throws HttpException
	 *             与Http协议相关的异常抛出
	 * @throws UnsupportedEncodingException
	 *             当使用不存在的编码方式时抛出，因为默认使用的是UTF-8编码，理论上不会出现这个异常
	 * @throws ApiException
	 *             调用API接口产生的异常，通常都会把所有可以截获的异常归类为这一异常。
	 */
	public static <T, E> T invokePost(String theUrl, Class<T> retDataCls, Class<E> entityCls,
			Map<String, String> pathParams, Map<String, String> headers, Map<String, String> parameters
			,String requestBody)
			throws ApiException {
		try {
			PostMethod post = new PostMethod(getParameterizedUrl(pathParams, theUrl));
			post.setRequestHeader("content-type", "application/x-www-form-urlencoded;charset=UTF-8");
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				post.setRequestHeader(entry.getKey(), entry.getValue());
			}
			if(requestBody != null && !requestBody.isEmpty()) {
				post.setRequestEntity(new StringRequestEntity(requestBody,
					"application/x-www-form-urlencoded", "UTF-8"));
			}
			/*else
				post.setRequestEntity(new StringRequestEntity(parseParamsMap2String(parameters),
						"application/x-www-form-urlencoded", "UTF-8"));*/
			for(String paramName : parameters.keySet()){
				post.addParameter(paramName, parameters.get(paramName));
			}
			HttpClient httpclient = new HttpClient();
			HttpMethodParams hmp = post.getParams();
			int result = httpclient.executeMethod(post);
			return getResultData(post, retDataCls, entityCls);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApiException(e.getMessage(), e);
		}
	}
	
	public static <T, E> T uploadFile(String theUrl, Class<T> retDataCls, Class<E> entityCls,
			Map<String, String> pathParams, Map<String, String> headers, Map<String, String> parameters
			,Map<String,File> files)
			throws ApiException {
		try {
			PostMethod post = new PostMethod(getParameterizedUrl(pathParams, theUrl));
			/*post.setRequestHeader("content-type", "multipart/form-data;boundary=" + HttpMethodParams.MULTIPART_BOUNDARY);*/
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				post.setRequestHeader(entry.getKey(), entry.getValue());
			}

			HttpMethodParams hmp = post.getParams();
			List<Part> parts = new ArrayList<>();
			for(String paramKey : parameters.keySet()){
				parts.add(new StringPart(paramKey, parameters.get(paramKey)));
			}
			for(String fileKey : files.keySet()){
				parts.add(new FilePart(fileKey, files.get(fileKey)));
			}
			MultipartRequestEntity mpr = new MultipartRequestEntity(parts.toArray(new Part[parts.size()]), hmp);
			post.setRequestEntity(mpr);
			HttpClient httpclient = new HttpClient();
			int result = httpclient.executeMethod(post);
			return getResultData(post, retDataCls, entityCls);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApiException(e.getMessage(), e);
		}
	}

	/**
	 * 调用Delete方法类型的远程Web service
	 * 
	 * @param theUrl
	 *            web service的URL(统一资源定位符)
	 * @param retDataCls
	 *            返回值的数据类型
	 * @param entityCls
	 *            实体类型
	 * @param pathParams
	 *            路径参数，以字符串键，值对的Map对象形式传入，支持多个对象，以英文逗号隔开。
	 * @param headers
	 *            头信息参数
	 * @param parameters
	 *            一般参数
	 * @return 调用结果
	 * @throws IOException
	 *             出现网络异常时抛出
	 * @throws HttpException
	 *             与Http协议相关的异常抛出
	 * @throws UnsupportedEncodingException
	 *             当使用不存在的编码方式时抛出，因为默认使用的是UTF-8编码，理论上不会出现这个异常
	 * @throws ApiException
	 *             调用API接口产生的异常，通常都会把所有可以截获的异常归类为这一异常。
	 */
	public static <T, E> T invokeDelete(String theUrl, Class<T> retDataCls, Class<E> entityCls,
			Map<String, String> pathParams, Map<String, String> headers, Map<String, String> parameters)
			throws ApiException {
		try {
			DeleteMethod delete = new DeleteMethod(getParameterizedUrl(pathParams, theUrl));
			delete.setRequestHeader("content-type", "application/x-www-form-urlencoded;charset=UTF-8");
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				delete.setRequestHeader(entry.getKey(), entry.getValue());
			}
			delete.setRequestHeader("content-type", "application/x-www-form-urlencoded;charset=UTF-8");
			// getMethod.setRequestHeader("charset", "utf-8");
			delete.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			HttpMethodParams params = new HttpMethodParams();
			if (parameters != null) {
				for (String paramName : parameters.keySet()) {
					params.setParameter(paramName, parameters.get(paramName));
				}
			}
			delete.setParams(new HttpMethodParams());
			List<NameValuePair> paires = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> param : parameters.entrySet()) {
				paires.add(new NameValuePair(param.getKey(), param.getValue()));
			}
			delete.setQueryString(paires.toArray(new NameValuePair[paires.size()]));
			HttpClient httpclient = new HttpClient();
			int result = httpclient.executeMethod(delete);
			return getResultData(delete, retDataCls, entityCls);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ApiException(e.getMessage(), e);
		}
	}
	
	

}
