package kr.co.azurepassion.common.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.azurepassion.common.service.CommonService;

@Service(value = "commonService")
public class CommonServiceImpl implements CommonService {
	private static Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

	public Map<String, Object> getPredictationNumber() {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		
		try {
			JSONParser jsonParse = new JSONParser();
			JSONObject jsonObject = new JSONObject();
			
			String API_URL = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=903";
			URL url = new URL(API_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			//conn.setRequestProperty("X-Auth-Token", API_KEY);
			conn.setRequestProperty("X-Data-Type", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);


			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			if (conn.getResponseCode() != 200) {
				logger.error("Failed: HTTP error code : " + conn.getResponseCode());
				throw new RuntimeException("Failed: HTTP error code : " + conn.getResponseCode());
			}

			String readStr = null;
			StringBuilder sb = new StringBuilder();
			while ((readStr = br.readLine()) != null) {
				sb.append(readStr);
			}
			br.close();
			logger.debug("JH : sb = "+sb.toString());
			jsonObject = (JSONObject) jsonParse.parse(sb.toString());
			resultMap.putAll(jsonObject);
			logger.debug("JH : resultMap = "+resultMap);
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}

}
