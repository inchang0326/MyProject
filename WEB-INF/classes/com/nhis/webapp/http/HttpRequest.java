package com.nhis.webapp.http;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nhis.webapp.common.ResultCode;

public class HttpRequest {

	private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

	// HTTP Client POST 통신 - NH Developers API(개발계) 사용
	public String sendPost(String url, HashMap<String, Object> input) throws HttpException, IOException {
		logger.info(this.getClass().getName().toString());
		String rtCd = "";
		CloseableHttpResponse response = null;
		
		try {
	        CloseableHttpClient httpclient = HttpClients.createDefault();
	        HttpPost httpPost = new HttpPost(url);

	        // API 데이터 설정
	        httpPost.addHeader("Content-Type", "application/json");	        
	        StringEntity params = new StringEntity("  {\r\n" + 
	        		" \"Header\": {\r\n" + 
	        		" 				\"ApiNm\": " + "\"" + input.get("apiNm").toString() + "\"" + ",\r\n" + 
	        		"	        	\"Tsymd\": " + "\"" + input.get("tsymd").toString() + "\"" + ",\r\n" + 
	        		"	        	\"Trtm\": " + "\"" + input.get("trtm").toString() + "\"" + ",\r\n" + 
	        		"	        	\"Iscd\": \"000379\",\r\n" + 
	        		"	        	\"FintechApsno\": \"001\",\r\n" + 
	        		"	        	\"ApiSvcCd\": "+ "\"" + input.get("apiSvcCd").toString() + "\"" + ",\r\n" + 
	        		"	        	\"IsTuno\": "+ "\"" + input.get("sqnoOfApi").toString() + "\"" + ",\r\n" + 
	        		"	        	\"AccessToken\": \"8c2e98806773300fddca0b2ea2e03f2289a6f085ace3ddb79d0f3f299a0ed34d\"\r\n" + 
	        		"	       	  },\r\n" + 
	        		" \"Bncd\": " + "\"" + input.get("bkNm").toString() + "\"" + ",\r\n" + 
	        		" \"Acno\": " + "\"" + input.get("accNo").toString() + "\"" + ",\r\n" + 
	        		" \"Tram\": \"1\",\r\n" + 
	        		" \"DractOtlt\": " + "\"" + (String) input.get("dractOtlt") + "\"" + ",\r\n" + 
	        		" \"MractOtlt\": " + "\"" + (String) input.get("mractOtlt") + "\"" + "\r\n" + 
	        		" } ");	        

	        httpPost.setEntity(params);
	        response = httpclient.execute(httpPost);
	        HttpEntity entity = response.getEntity();
	        
	        // 응답 결과코드 파싱
	        if (entity != null) {
	           String jsonStr = EntityUtils.toString(entity); 
	           logger.info("API Response Data >> : " + jsonStr);
	           rtCd = jsonStr.substring(jsonStr.indexOf("Rpcd") + 7, jsonStr.indexOf("Rpcd") + 7 + 5);
	        }
	    } catch (Exception e) {
			logger.error(e.getMessage());
			throw new HttpException(ResultCode.ERR_0008, ResultCode.ERR_0008_MSG);
	    } finally {
	    	if(response != null) {
	            response.close();	    		
	    	}
	    }
		
        return rtCd;
	}	
}