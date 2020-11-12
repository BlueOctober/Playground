package kr.co.azurepassion.common.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.azurepassion.common.dao.CommonDao;
import kr.co.azurepassion.common.service.CommonService;

@Service(value = "commonService")
public class CommonServiceImpl implements CommonService {
	private static Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

	@Resource(name = "commonDao")
    private CommonDao commonDao;
	
	public List<Map<String, Object>> getPredictationNumber() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		logger.debug("JH : "+getClass().getName()+" - getPredictationNumber execute..!!");
		
		return commonDao.getPredictationNumber(paramMap);
		
	}
	
	public Map<String, Object> getCurrentHistoryNumber() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		logger.debug("JH : "+getClass().getName()+" - getCurrentHistoryNumber execute..!!");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = commonDao.getCurrentHistoryNumber(paramMap);
		
		logger.debug("JH : "+getClass().getName()+" - getCurrentHistoryNumber - resultMap = "+resultMap);
		return resultMap;
	}
	
	
	public Map<String, Object> getTestLogicResult2() {
		logger.debug("JH : "+getClass().getName()+" - getTestLogicResult2 execute..!!");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			JSONParser jsonParse = new JSONParser();
			JSONObject jsonObject = new JSONObject();
			// BASE = 935회차. 2020-10-31
			
			int baseNum = 935;
			String sBaseDate = "2020-10-31";
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String sTodayDate = sdf.format(date);
			
			Date baseDate = sdf.parse(sBaseDate);
			Date todayDate = sdf.parse(sTodayDate);
			
			long calDate = todayDate.getTime() - baseDate.getTime();
			long dateGap = calDate / ( 24*60*60*1000);
			dateGap = Math.abs(dateGap);
			
			
			int iDateGap = Long.valueOf(dateGap).intValue()/7;
			
			int targetNum = baseNum+iDateGap;
			
			String API_URL_BASE = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=";
			String API_URL = API_URL_BASE+targetNum;
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
			jsonObject = (JSONObject) jsonParse.parse(sb.toString());
			conn.disconnect();
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("drwNo", Integer.parseInt(String.valueOf(jsonObject.get("drwNo"))));
			tempMap.put("drwtNo1", Integer.parseInt(String.valueOf(jsonObject.get("drwtNo1"))));
			tempMap.put("drwtNo2", Integer.parseInt(String.valueOf(jsonObject.get("drwtNo2"))));
			tempMap.put("drwtNo3", Integer.parseInt(String.valueOf(jsonObject.get("drwtNo3"))));
			tempMap.put("drwtNo4", Integer.parseInt(String.valueOf(jsonObject.get("drwtNo4"))));
			tempMap.put("drwtNo5", Integer.parseInt(String.valueOf(jsonObject.get("drwtNo5"))));
			tempMap.put("drwtNo6", Integer.parseInt(String.valueOf(jsonObject.get("drwtNo6"))));
			tempMap.put("bnusNo", Integer.parseInt(String.valueOf(jsonObject.get("bnusNo"))));
			tempMap.put("drwNoDate", String.valueOf(jsonObject.get("drwNoDate")));
			resultMap.putAll(tempMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	public List<String> selectionSort(List<Map<String, Object>> numCntMapList) {
		List<String> sortResult = new ArrayList<String>();
		for(int i=0; i<numCntMapList.size(); i++) {
			sortResult.add((String)numCntMapList.get(i).get("id"));
		}
		String tempId;
		for(int i=0; i<numCntMapList.size()-1; i++) {
			for(int j=i+1; j<numCntMapList.size(); j++) {
				if( (Integer) numCntMapList.get(i).get("cnt") >(Integer) numCntMapList.get(j).get("cnt")) {
					tempId = sortResult.get(j);
					sortResult.set(j, sortResult.get(i));
					sortResult.set(i, tempId);
				}
			}
		}
		return sortResult;
	}
	
	

}
