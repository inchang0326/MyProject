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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nhis.webapp.common.EnumResultCode;
import com.nhis.webapp.exception.HttpException;

public class HttpRequest {

	private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

	// HTTP Client POST 통신 - NH Developers API(테스트용) 사용
	@SuppressWarnings("unchecked")
	public String sendPost(String url, HashMap<String, Object> input) throws HttpException, IOException {
		logger.debug(this.getClass().getName().toString());
		
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		String rtCd = "";
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;

		try {
			HttpPost httpPost = new HttpPost(url);
			
			// POST 헤더 설정
			httpPost.addHeader("Content-Type", "application/json");

			// POST 바디 설정
			// (2020.09.13) 수정 >> JSONObject로 바디 데이터 설정
			JSONObject jobjOuter = new JSONObject();
			JSONObject jobjInner = new JSONObject();
			
			jobjInner.put("ApiNm", input.get("apiNm").toString());
			jobjInner.put("Tsymd", input.get("tsymd").toString());
			jobjInner.put("Trtm", input.get("trtm").toString());
			jobjInner.put("Iscd", "000379");
			jobjInner.put("FintechApsno", "001");
			jobjInner.put("ApiSvcCd", input.get("apiSvcCd").toString());
			jobjInner.put("IsTuno", input.get("sqnoOfApi").toString());
			jobjInner.put("AccessToken", "8c2e98806773300fddca0b2ea2e03f2289a6f085ace3ddb79d0f3f299a0ed34d");
			
			jobjOuter.put("Header", jobjInner);

			jobjOuter.put("Bncd", input.get("bkNm").toString());
			jobjOuter.put("Acno", input.get("accNo").toString());
			jobjOuter.put("Tram", "1");
			jobjOuter.put("DractOtlt", (String) input.get("dractOtlt"));
			jobjOuter.put("MractOtlt", (String) input.get("mractOtlt"));

			StringEntity params = new StringEntity(jobjOuter.toString());
			httpPost.setEntity(params);
			response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			
			// 결과코드 파싱
			if (entity != null) {
				// (2020.09.13) 수정 >> JSON String to JSON Object to HashMap 변환 후 결과코드 파싱
				String jsonStr = EntityUtils.toString(entity);
				// (2020.09.13) 수정 >> HttpEntity 자원 해제 코드 추가
				EntityUtils.consume(response.getEntity());

				JSONParser jparser = new JSONParser();
				JSONObject resJobj = new JSONObject();
				resJobj = (JSONObject) jparser.parse(jsonStr);
				
				logger.debug("API Response Data >> : " + resJobj.toString());

				retMap.putAll(resJobj);
				
				// 데이터 한 번 더 변환
				resJobj = (JSONObject) retMap.get("Header");
				retMap.putAll(resJobj);

				rtCd = (String) retMap.get("Rpcd");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new HttpException(EnumResultCode.E0008.toString(), EnumResultCode.E0008.getMsg());
		} finally {
			if (response != null) {
				response.close();
			}
			// (2020.09.13) 수정 >> ClosableHttpClient 연결 종료
			httpclient.close();
		}

		return rtCd;
	}
}