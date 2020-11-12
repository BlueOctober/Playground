package kr.co.azurepassion.schedule;

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
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.azurepassion.schedule.dao.ScheduleDao;


@Component
public class Schedule  {
	private static final Logger logger = LoggerFactory.getLogger(Schedule.class);

	@Autowired
	public ScheduleDao scheduleDao;
	
	
	
	public void getTotalHistory() throws Exception {
		logger.info("***************** [Batch Job] - getTotalHistory ***************** ");
		try {
			JSONParser jsonParse = new JSONParser();
			JSONObject jsonObject = new JSONObject();
			String API_URL_BASE = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=";
			for(int i=1; i<=935; i++) {
				String API_URL = API_URL_BASE+i;
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
				scheduleDao.saveLottoInfo(tempMap);
			} // End Of For
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // End Of getTotalHistory
	
	public void getLatestHistory() throws Exception {
		logger.debug("JH : "+getClass().getName()+" - getLatestHistory execute..!!");
		
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
			scheduleDao.saveLottoInfo(tempMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // End Of getCurrentHistory
	
	
	
	
	
	
	public void executeAlgorithm() throws Exception {
		List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();
		int num1=0,num2=0,num3=0,num4=0,num5=0,num6=0,num7=0,num8=0,num9=0,num10=0,num11=0,num12=0,num13=0,num14=0,num15=0;
		int num16=0,num17=0,num18=0,num19=0,num20=0,num21=0,num22=0,num23=0,num24=0,num25=0,num26=0,num27=0,num28=0,num29=0,num30=0;
		int num31=0,num32=0,num33=0,num34=0,num35=0,num36=0,num37=0,num38=0,num39=0,num40=0,num41=0,num42=0,num43=0,num44=0,num45=0;
		
		Map<String, Object> prediction1Map = new HashMap<String, Object>();
		Map<String, Object> prediction2Map = new HashMap<String, Object>();
		Map<String, Object> prediction3Map = new HashMap<String, Object>();
		Map<String, Object> prediction4Map = new HashMap<String, Object>();
		Map<String, Object> prediction5Map = new HashMap<String, Object>();
		
		try {
			JSONParser jsonParse = new JSONParser();
			JSONObject jsonObject = new JSONObject();
			// BASE = 935회차. 2020-10-31
			String API_URL_BASE = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=";
			int baseNum = 935; //개발시점인 935회차 2020/10/31 기준으로 계산
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
			int latestNum = baseNum+iDateGap; // LatestNum
			
			prediction1Map.put("drw_no", latestNum+1);
			prediction2Map.put("drw_no", latestNum+1);
			prediction3Map.put("drw_no", latestNum+1);
			prediction4Map.put("drw_no", latestNum+1);
			prediction5Map.put("drw_no", latestNum+1);
			
			List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
			for(int i=(latestNum-333); i<latestNum; i++) {
				String API_URL = API_URL_BASE+i;
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
				
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("drwtNo1", Integer.parseInt(String.valueOf(jsonObject.get("drwtNo1"))));
				tempMap.put("drwtNo2", Integer.parseInt(String.valueOf(jsonObject.get("drwtNo2"))));
				tempMap.put("drwtNo3", Integer.parseInt(String.valueOf(jsonObject.get("drwtNo3"))));
				tempMap.put("drwtNo4", Integer.parseInt(String.valueOf(jsonObject.get("drwtNo4"))));
				tempMap.put("drwtNo5", Integer.parseInt(String.valueOf(jsonObject.get("drwtNo5"))));
				tempMap.put("drwtNo6", Integer.parseInt(String.valueOf(jsonObject.get("drwtNo6"))));
				//tempMap.put("bonusNum", Integer.parseInt(String.valueOf(jsonObject.get("bonusNum"))));
				
				listMap.add(tempMap);
				
				switch(Integer.parseInt(String.valueOf(jsonObject.get("drwtNo1")))) {
					case 1: num1++;break;
					case 2: num2++;break;
					case 3: num3++;break;
					case 4: num4++;break;
					case 5: num5++;break;
					case 6: num6++;break;
					case 7: num7++;break;
					case 8: num8++;break;
					case 9: num9++;break;
					case 10: num10++;break;
					case 11: num11++;break;
					case 12: num12++;break;
					case 13: num13++;break;
					case 14: num14++;break;
					case 15: num15++;break;
					case 16: num16++;break;
					case 17: num17++;break;
					case 18: num18++;break;
					case 19: num19++;break;
					case 20: num20++;break;
					case 21: num21++;break;
					case 22: num22++;break;
					case 23: num23++;break;
					case 24: num24++;break;
					case 25: num25++;break;
					case 26: num26++;break;
					case 27: num27++;break;
					case 28: num28++;break;
					case 29: num29++;break;
					case 30: num30++;break;
					case 31: num31++;break;
					case 32: num32++;break;
					case 33: num33++;break;
					case 34: num34++;break;
					case 35: num35++;break;
					case 36: num36++;break;
					case 37: num37++;break;
					case 38: num38++;break;
					case 39: num39++;break;
					case 40: num40++;break;
					case 41: num41++;break;
					case 42: num42++;break;
					case 43: num43++;break;
					case 44: num44++;break;
					case 45: num45++;break;
					default: break;
				}
				
				switch(Integer.parseInt(String.valueOf(jsonObject.get("drwtNo2")))) {
					case 1: num1++;break;
					case 2: num2++;break;
					case 3: num3++;break;
					case 4: num4++;break;
					case 5: num5++;break;
					case 6: num6++;break;
					case 7: num7++;break;
					case 8: num8++;break;
					case 9: num9++;break;
					case 10: num10++;break;
					case 11: num11++;break;
					case 12: num12++;break;
					case 13: num13++;break;
					case 14: num14++;break;
					case 15: num15++;break;
					case 16: num16++;break;
					case 17: num17++;break;
					case 18: num18++;break;
					case 19: num19++;break;
					case 20: num20++;break;
					case 21: num21++;break;
					case 22: num22++;break;
					case 23: num23++;break;
					case 24: num24++;break;
					case 25: num25++;break;
					case 26: num26++;break;
					case 27: num27++;break;
					case 28: num28++;break;
					case 29: num29++;break;
					case 30: num30++;break;
					case 31: num31++;break;
					case 32: num32++;break;
					case 33: num33++;break;
					case 34: num34++;break;
					case 35: num35++;break;
					case 36: num36++;break;
					case 37: num37++;break;
					case 38: num38++;break;
					case 39: num39++;break;
					case 40: num40++;break;
					case 41: num41++;break;
					case 42: num42++;break;
					case 43: num43++;break;
					case 44: num44++;break;
					case 45: num45++;break;
					default: break;
				}
				
				switch(Integer.parseInt(String.valueOf(jsonObject.get("drwtNo3")))) {
					case 1: num1++;break;
					case 2: num2++;break;
					case 3: num3++;break;
					case 4: num4++;break;
					case 5: num5++;break;
					case 6: num6++;break;
					case 7: num7++;break;
					case 8: num8++;break;
					case 9: num9++;break;
					case 10: num10++;break;
					case 11: num11++;break;
					case 12: num12++;break;
					case 13: num13++;break;
					case 14: num14++;break;
					case 15: num15++;break;
					case 16: num16++;break;
					case 17: num17++;break;
					case 18: num18++;break;
					case 19: num19++;break;
					case 20: num20++;break;
					case 21: num21++;break;
					case 22: num22++;break;
					case 23: num23++;break;
					case 24: num24++;break;
					case 25: num25++;break;
					case 26: num26++;break;
					case 27: num27++;break;
					case 28: num28++;break;
					case 29: num29++;break;
					case 30: num30++;break;
					case 31: num31++;break;
					case 32: num32++;break;
					case 33: num33++;break;
					case 34: num34++;break;
					case 35: num35++;break;
					case 36: num36++;break;
					case 37: num37++;break;
					case 38: num38++;break;
					case 39: num39++;break;
					case 40: num40++;break;
					case 41: num41++;break;
					case 42: num42++;break;
					case 43: num43++;break;
					case 44: num44++;break;
					case 45: num45++;break;
					default: break;
				}
				
				switch(Integer.parseInt(String.valueOf(jsonObject.get("drwtNo4")))) {
					case 1: num1++;break;
					case 2: num2++;break;
					case 3: num3++;break;
					case 4: num4++;break;
					case 5: num5++;break;
					case 6: num6++;break;
					case 7: num7++;break;
					case 8: num8++;break;
					case 9: num9++;break;
					case 10: num10++;break;
					case 11: num11++;break;
					case 12: num12++;break;
					case 13: num13++;break;
					case 14: num14++;break;
					case 15: num15++;break;
					case 16: num16++;break;
					case 17: num17++;break;
					case 18: num18++;break;
					case 19: num19++;break;
					case 20: num20++;break;
					case 21: num21++;break;
					case 22: num22++;break;
					case 23: num23++;break;
					case 24: num24++;break;
					case 25: num25++;break;
					case 26: num26++;break;
					case 27: num27++;break;
					case 28: num28++;break;
					case 29: num29++;break;
					case 30: num30++;break;
					case 31: num31++;break;
					case 32: num32++;break;
					case 33: num33++;break;
					case 34: num34++;break;
					case 35: num35++;break;
					case 36: num36++;break;
					case 37: num37++;break;
					case 38: num38++;break;
					case 39: num39++;break;
					case 40: num40++;break;
					case 41: num41++;break;
					case 42: num42++;break;
					case 43: num43++;break;
					case 44: num44++;break;
					case 45: num45++;break;
					default: break;
				}
				
				switch(Integer.parseInt(String.valueOf(jsonObject.get("drwtNo5")))) {
					case 1: num1++;break;
					case 2: num2++;break;
					case 3: num3++;break;
					case 4: num4++;break;
					case 5: num5++;break;
					case 6: num6++;break;
					case 7: num7++;break;
					case 8: num8++;break;
					case 9: num9++;break;
					case 10: num10++;break;
					case 11: num11++;break;
					case 12: num12++;break;
					case 13: num13++;break;
					case 14: num14++;break;
					case 15: num15++;break;
					case 16: num16++;break;
					case 17: num17++;break;
					case 18: num18++;break;
					case 19: num19++;break;
					case 20: num20++;break;
					case 21: num21++;break;
					case 22: num22++;break;
					case 23: num23++;break;
					case 24: num24++;break;
					case 25: num25++;break;
					case 26: num26++;break;
					case 27: num27++;break;
					case 28: num28++;break;
					case 29: num29++;break;
					case 30: num30++;break;
					case 31: num31++;break;
					case 32: num32++;break;
					case 33: num33++;break;
					case 34: num34++;break;
					case 35: num35++;break;
					case 36: num36++;break;
					case 37: num37++;break;
					case 38: num38++;break;
					case 39: num39++;break;
					case 40: num40++;break;
					case 41: num41++;break;
					case 42: num42++;break;
					case 43: num43++;break;
					case 44: num44++;break;
					case 45: num45++;break;
					default: break;
				}
				
				switch(Integer.parseInt(String.valueOf(jsonObject.get("drwtNo6")))) {
					case 1: num1++;break;
					case 2: num2++;break;
					case 3: num3++;break;
					case 4: num4++;break;
					case 5: num5++;break;
					case 6: num6++;break;
					case 7: num7++;break;
					case 8: num8++;break;
					case 9: num9++;break;
					case 10: num10++;break;
					case 11: num11++;break;
					case 12: num12++;break;
					case 13: num13++;break;
					case 14: num14++;break;
					case 15: num15++;break;
					case 16: num16++;break;
					case 17: num17++;break;
					case 18: num18++;break;
					case 19: num19++;break;
					case 20: num20++;break;
					case 21: num21++;break;
					case 22: num22++;break;
					case 23: num23++;break;
					case 24: num24++;break;
					case 25: num25++;break;
					case 26: num26++;break;
					case 27: num27++;break;
					case 28: num28++;break;
					case 29: num29++;break;
					case 30: num30++;break;
					case 31: num31++;break;
					case 32: num32++;break;
					case 33: num33++;break;
					case 34: num34++;break;
					case 35: num35++;break;
					case 36: num36++;break;
					case 37: num37++;break;
					case 38: num38++;break;
					case 39: num39++;break;
					case 40: num40++;break;
					case 41: num41++;break;
					case 42: num42++;break;
					case 43: num43++;break;
					case 44: num44++;break;
					case 45: num45++;break;
					default: break;
				}
				conn.disconnect();
			} // End Of For
			
			/** Counting End / Prediction Logic Start **/
			
			
			for(int i=1; i<=5; i++) {
				Map<String, Object> latest1Map = listMap.get(listMap.size()-1); //1주전 당첨번호
				Map<String, Object> latest2Map = listMap.get(listMap.size()-1); //2주전 당첨번호
				Map<String, Object> latest3Map = listMap.get(listMap.size()-2); //3주전 당첨번호
				int overlapNum = 0; //첫 번째 당첨 예상 번호 변수
				
				/**
				 * [첫 번째 당첨 예상 번호 추출]
				 * 
				 * 1. 2주 전 당첨번호를 기준으로 1주전,3주전 당첨번호화의 중독여부를 분석. 
				 * 2. 해당번호들 중 중복되지 않는 번호를 추출한다.
				 * 2-1. 중복되지 않는 번호가 없었다면 2주전 당첨번호 중 1개를 무작위로 추출한다. 
				 * 3. 중복되지 않는 번호가 있었다면 해당 번호를 대상으로한다.
				 * 3-1. 중복되지 않는 번호가 1개라면 해당 번호를 예상번호로 채택한다.
				 * 3-2. 중복되지 않는 번호가 여러개라면 그 중 1개의 번호를 무작위로 추출한다.
				 * 
				 *  Key는 2주전 당첨번호 중 하나는 무조건 예상번호에 들어가며, 기왕이면 1주전, 3주전에 당첨번호로 나오지 않았던 번호를 선택하는 방법이다.
				 *  
				 */
				List<Integer> overlapNumList = new ArrayList<Integer>();  
				if(!(latest1Map.containsValue(latest2Map.get("drwtNo1"))) && !(latest3Map.containsValue(latest2Map.get("drwtNo1")))) {
					overlapNumList.add((Integer)latest2Map.get("drwtNo1"));
				}
				if(!(latest1Map.containsValue(latest2Map.get("drwtNo2"))) && !(latest3Map.containsValue(latest2Map.get("drwtNo2")))) {
					overlapNumList.add((Integer)latest2Map.get("drwtNo2"));
				}
				if(!(latest1Map.containsValue(latest2Map.get("drwtNo3"))) && !(latest3Map.containsValue(latest2Map.get("drwtNo3")))) {
					overlapNumList.add((Integer)latest2Map.get("drwtNo3"));
				}
				if(!(latest1Map.containsValue(latest2Map.get("drwtNo4"))) && !(latest3Map.containsValue(latest2Map.get("drwtNo4")))) {
					overlapNumList.add((Integer)latest2Map.get("drwtNo4"));
				}
				if(!(latest1Map.containsValue(latest2Map.get("drwtNo5"))) && !(latest3Map.containsValue(latest2Map.get("drwtNo5")))) {
					overlapNumList.add((Integer)latest2Map.get("drwtNo5"));
				}
				if(!(latest1Map.containsValue(latest2Map.get("drwtNo6"))) && !(latest3Map.containsValue(latest2Map.get("drwtNo6")))) {
					overlapNumList.add((Integer)latest2Map.get("drwtNo6"));
				}
				if(overlapNumList.size()>0) {
					List<Integer> workOverlapNumList = new ArrayList<Integer>();
					int overlapNumListSize = overlapNumList.size();
					for(int j=0; j<overlapNumListSize; j++) {
						if(overlapNumListSize==1) {
							overlapNum = overlapNumList.get(0);		
						} else {
							Random random = new Random();
							overlapNum = random.nextInt(overlapNumListSize);
						}
					}
				} else {
					int randomNum = (int) (Math.random() * 6) +1;
					String tempStr = "drwtNo"+randomNum;
					overlapNum = (Integer) latest2Map.get(tempStr);
				}
				
				/**
				 * [2~6 번째 당첨 예상 번호 추출]
				 * 
				 * 1. 지난주 당첨번호를 기준으로 -333회차까지 데이터를 베이스로 한다. 
				 * 2. 지난 당첨번호들을 카운팅하여 1~35 번호들 중 당첨횟수를 카운팅한다.
				 * 3. 카운팅한 번호들을 당첨 횟수를 기준으로 정렬한다.  
				 * 
				 *  Key는 지난 300회차분의 당첨번호를 바탕으로 낮았던 번호들 중 일부, 많이 나왔던 번호들 중 일부를 조합하는 것이다.
				 *  
				 */
				List<Map<String, Object>> numCntMapList = new ArrayList<Map<String, Object>>();
				Map<String, Object> num1Map = new HashMap<String, Object>();num1Map.put("cnt", num1);num1Map.put("id", "num1");
				Map<String, Object> num2Map = new HashMap<String, Object>();num2Map.put("cnt", num2);num2Map.put("id", "num2");
				Map<String, Object> num3Map = new HashMap<String, Object>();num3Map.put("cnt", num3);num3Map.put("id", "num3");
				Map<String, Object> num4Map = new HashMap<String, Object>();num4Map.put("cnt", num4);num4Map.put("id", "num4");
				Map<String, Object> num5Map = new HashMap<String, Object>();num5Map.put("cnt", num5);num5Map.put("id", "num5");
				Map<String, Object> num6Map = new HashMap<String, Object>();num6Map.put("cnt", num6);num6Map.put("id", "num6");
				Map<String, Object> num7Map = new HashMap<String, Object>();num7Map.put("cnt", num7);num7Map.put("id", "num7");
				Map<String, Object> num8Map = new HashMap<String, Object>();num8Map.put("cnt", num8);num8Map.put("id", "num8");
				Map<String, Object> num9Map = new HashMap<String, Object>();num9Map.put("cnt", num9);num9Map.put("id", "num9");
				Map<String, Object> num10Map = new HashMap<String, Object>();num10Map.put("cnt", num10);num10Map.put("id", "num10");
				Map<String, Object> num11Map = new HashMap<String, Object>();num11Map.put("cnt", num11);num11Map.put("id", "num11");
				Map<String, Object> num12Map = new HashMap<String, Object>();num12Map.put("cnt", num12);num12Map.put("id", "num12");
				Map<String, Object> num13Map = new HashMap<String, Object>();num13Map.put("cnt", num13);num13Map.put("id", "num13");
				Map<String, Object> num14Map = new HashMap<String, Object>();num14Map.put("cnt", num14);num14Map.put("id", "num14");
				Map<String, Object> num15Map = new HashMap<String, Object>();num15Map.put("cnt", num15);num15Map.put("id", "num15");
				Map<String, Object> num16Map = new HashMap<String, Object>();num16Map.put("cnt", num16);num16Map.put("id", "num16");
				Map<String, Object> num17Map = new HashMap<String, Object>();num17Map.put("cnt", num17);num17Map.put("id", "num17");
				Map<String, Object> num18Map = new HashMap<String, Object>();num18Map.put("cnt", num18);num18Map.put("id", "num18");
				Map<String, Object> num19Map = new HashMap<String, Object>();num19Map.put("cnt", num19);num19Map.put("id", "num19");
				Map<String, Object> num20Map = new HashMap<String, Object>();num20Map.put("cnt", num20);num20Map.put("id", "num20");
				Map<String, Object> num21Map = new HashMap<String, Object>();num21Map.put("cnt", num21);num21Map.put("id", "num21");
				Map<String, Object> num22Map = new HashMap<String, Object>();num22Map.put("cnt", num22);num22Map.put("id", "num22");
				Map<String, Object> num23Map = new HashMap<String, Object>();num23Map.put("cnt", num23);num23Map.put("id", "num23");
				Map<String, Object> num24Map = new HashMap<String, Object>();num24Map.put("cnt", num24);num24Map.put("id", "num24");
				Map<String, Object> num25Map = new HashMap<String, Object>();num25Map.put("cnt", num25);num25Map.put("id", "num25");
				Map<String, Object> num26Map = new HashMap<String, Object>();num26Map.put("cnt", num26);num26Map.put("id", "num26");
				Map<String, Object> num27Map = new HashMap<String, Object>();num27Map.put("cnt", num27);num27Map.put("id", "num27");
				Map<String, Object> num28Map = new HashMap<String, Object>();num28Map.put("cnt", num28);num28Map.put("id", "num28");
				Map<String, Object> num29Map = new HashMap<String, Object>();num29Map.put("cnt", num29);num29Map.put("id", "num29");
				Map<String, Object> num30Map = new HashMap<String, Object>();num30Map.put("cnt", num30);num30Map.put("id", "num30");
				Map<String, Object> num31Map = new HashMap<String, Object>();num31Map.put("cnt", num31);num31Map.put("id", "num31");
				Map<String, Object> num32Map = new HashMap<String, Object>();num32Map.put("cnt", num32);num32Map.put("id", "num32");
				Map<String, Object> num33Map = new HashMap<String, Object>();num33Map.put("cnt", num33);num33Map.put("id", "num33");
				Map<String, Object> num34Map = new HashMap<String, Object>();num34Map.put("cnt", num34);num34Map.put("id", "num34");
				Map<String, Object> num35Map = new HashMap<String, Object>();num35Map.put("cnt", num35);num35Map.put("id", "num35");
				numCntMapList.add(num1Map);numCntMapList.add(num2Map);numCntMapList.add(num3Map);numCntMapList.add(num4Map);numCntMapList.add(num5Map);
				numCntMapList.add(num6Map);numCntMapList.add(num7Map);numCntMapList.add(num8Map);numCntMapList.add(num9Map);numCntMapList.add(num10Map);
				numCntMapList.add(num11Map);numCntMapList.add(num12Map);numCntMapList.add(num13Map);numCntMapList.add(num14Map);numCntMapList.add(num15Map);
				numCntMapList.add(num16Map);numCntMapList.add(num17Map);numCntMapList.add(num18Map);numCntMapList.add(num19Map);numCntMapList.add(num20Map);
				numCntMapList.add(num21Map);numCntMapList.add(num22Map);numCntMapList.add(num23Map);numCntMapList.add(num24Map);numCntMapList.add(num25Map);
				numCntMapList.add(num26Map);numCntMapList.add(num27Map);numCntMapList.add(num28Map);numCntMapList.add(num29Map);numCntMapList.add(num30Map);
				numCntMapList.add(num31Map);numCntMapList.add(num32Map);numCntMapList.add(num33Map);numCntMapList.add(num34Map);numCntMapList.add(num35Map);
				
				List<String> numIdList = selectionSort(numCntMapList);
				
				if(i==1) {
					prediction1Map.put("drwt_no1", overlapNum);
					prediction1Map.put("no_desc", 1);
					
					String minTargetId1 = numIdList.get(1);
					String minTargetId2 = numIdList.get(3);
					String minTargetId3 = numIdList.get(5);
					
					String bigTargetId1 = numIdList.get(numIdList.size()-2);
					String bigTargetId2 = numIdList.get(numIdList.size()-4);
					
					String candidateMin1 = numIdList.get(2);
					String candidateMin2 = numIdList.get(4);
					String candidateMin3 = numIdList.get(6);
					String candidateMin4 = numIdList.get(0);
					String candidateMin5 = numIdList.get(7);
					String candidateMin6 = numIdList.get(8);
					
					String candidateMax1 = numIdList.get(numIdList.size()-3);
					String candidateMax2 = numIdList.get(numIdList.size()-5);
					String candidateMax3 = numIdList.get(numIdList.size()-6);
					String candidateMax4 = numIdList.get(numIdList.size()-1);
					String candidateMax5 = numIdList.get(numIdList.size()-7);
					String candidateMax6 = numIdList.get(numIdList.size()-8);
					
					// 2번째로 가장 당첨되지 않은 횟수가 높은 번호
					if(!prediction1Map.containsValue(Integer.parseInt( minTargetId1.substring(3, minTargetId1.length())))) {
						prediction1Map.put("drwt_no2", Integer.parseInt( minTargetId1.substring(3, minTargetId1.length())));
					}
					// 4번째로 가장 당첨되지 않은 횟수가 높은 번호
					if(!prediction1Map.containsValue(Integer.parseInt( minTargetId2.substring(3, minTargetId2.length())))) {
						prediction1Map.put("drwt_no3", Integer.parseInt( minTargetId2.substring(3, minTargetId2.length())));
					}
					// 6번째로 가장 당첨되지 않은 횟수가 높은 번호
					if(!prediction1Map.containsValue(Integer.parseInt( minTargetId3.substring(3, minTargetId3.length())))) {
						prediction1Map.put("drwt_no4", Integer.parseInt( minTargetId3.substring(3, minTargetId3.length())));
					}
					
					// 3번째로 가장 당첨횟수가 높은 번호
					if(!prediction1Map.containsValue(Integer.parseInt( bigTargetId1.substring(3, bigTargetId1.length())))) {
						prediction1Map.put("drwt_no5", Integer.parseInt( bigTargetId1.substring(3, bigTargetId1.length())));
					}
					// 5번째로 가장 당첨횟수가 높은 번호
					if(!prediction1Map.containsValue(Integer.parseInt( bigTargetId2.substring(3, bigTargetId2.length())))) {
						prediction1Map.put("drwt_no6", Integer.parseInt( bigTargetId2.substring(3, bigTargetId2.length())));
					}

					
					if(!prediction1Map.containsKey("drwt_no2")) {
						if(!prediction1Map.containsValue(Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())))) {
							prediction1Map.put("drwt_no2", Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())));	
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())))) {
							prediction1Map.put("drwt_no2", Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())))) {
							prediction1Map.put("drwt_no2", Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())))) {
							prediction1Map.put("drwt_no2", Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())))) {
							prediction1Map.put("drwt_no2", Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())))) {
							prediction1Map.put("drwt_no2", Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())));
						} else {
							prediction1Map.put("drwt_no2", 0);
						}
					}
					
					if(!prediction1Map.containsKey("drwt_no3")) {
						if(!prediction1Map.containsValue(Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())))) {
							prediction1Map.put("drwt_no3", Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())));	
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())))) {
							prediction1Map.put("drwt_no3", Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())))) {
							prediction1Map.put("drwt_no3", Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())))) {
							prediction1Map.put("drwt_no3", Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())))) {
							prediction1Map.put("drwt_no3", Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())))) {
							prediction1Map.put("drwt_no3", Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())));
						} else {
							prediction1Map.put("drwt_no3", 0);
						}
					}
					
					if(!prediction1Map.containsKey("drwt_no4")) {
						if(!prediction1Map.containsValue(Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())))) {
							prediction1Map.put("drwt_no4", Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())));	
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())))) {
							prediction1Map.put("drwt_no4", Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())))) {
							prediction1Map.put("drwt_no4", Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())))) {
							prediction1Map.put("drwt_no4", Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())))) {
							prediction1Map.put("drwt_no4", Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())))) {
							prediction1Map.put("drwt_no4", Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())));
						} else {
							prediction1Map.put("drwt_no4", 0);
						}
					}
					
					if(!prediction1Map.containsKey("drwt_no5")) {
						if(!prediction1Map.containsValue(Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())))) {
							prediction1Map.put("drwt_no5", Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())));	
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())))) {
							prediction1Map.put("drwt_no5", Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())))) {
							prediction1Map.put("drwt_no5", Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())))) {
							prediction1Map.put("drwt_no5", Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())))) {
							prediction1Map.put("drwt_no5", Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())))) {
							prediction1Map.put("drwt_no5", Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())));
						} else {
							prediction1Map.put("drwt_no5", 0);
						}
					}
					
					if(!prediction1Map.containsKey("drwt_no6")) {
						if(!prediction1Map.containsValue(Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())))) {
							prediction1Map.put("drwt_no6", Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())));	
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())))) {
							prediction1Map.put("drwt_no6", Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())))) {
							prediction1Map.put("drwt_no6", Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())))) {
							prediction1Map.put("drwt_no6", Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())))) {
							prediction1Map.put("drwt_no6", Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())));
						} else if(!prediction1Map.containsValue(Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())))) {
							prediction1Map.put("drwt_no6", Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())));
						} else {
							prediction1Map.put("drwt_no6", 0);
						}
					}
					
					List<Integer> numList = new ArrayList<Integer>();
					numList.add((Integer)prediction1Map.get("drwt_no1"));
					numList.add((Integer)prediction1Map.get("drwt_no2"));
					numList.add((Integer)prediction1Map.get("drwt_no3"));
					numList.add((Integer)prediction1Map.get("drwt_no4"));
					numList.add((Integer)prediction1Map.get("drwt_no5"));
					numList.add((Integer)prediction1Map.get("drwt_no6"));
					numList = selectionSort2(numList);
					prediction1Map.put("drwt_no1", numList.get(0));
					prediction1Map.put("drwt_no2", numList.get(1));
					prediction1Map.put("drwt_no3", numList.get(2));
					prediction1Map.put("drwt_no4", numList.get(3));
					prediction1Map.put("drwt_no5", numList.get(4));
					prediction1Map.put("drwt_no6", numList.get(5));
					scheduleDao.savePredictionNumber(prediction1Map);
					
				} else if(i==2) {
					prediction2Map.put("drwt_no1", overlapNum);
					prediction2Map.put("no_desc", 2);

					String minTargetId1 = numIdList.get(1);
					String minTargetId2 = numIdList.get(2);
					String minTargetId3 = numIdList.get(3);
					String bigTargetId1 = numIdList.get(4);
					String bigTargetId2 = numIdList.get(5);
					
					String candidateMin1 = numIdList.get(0);
					String candidateMin2 = numIdList.get(11);
					String candidateMin3 = numIdList.get(12);
					String candidateMin4 = numIdList.get(8);
					String candidateMin5 = numIdList.get(9);
					String candidateMin6 = numIdList.get(10);
					
					String candidateMax1 = numIdList.get(6);
					String candidateMax2 = numIdList.get(7);
					String candidateMax3 = numIdList.get(13);
					String candidateMax4 = numIdList.get(14);
					String candidateMax5 = numIdList.get(15);
					String candidateMax6 = numIdList.get(16);
					
					// 2번째로 가장 당첨되지 않은 횟수가 높은 번호
					if(!prediction2Map.containsValue(Integer.parseInt( minTargetId1.substring(3, minTargetId1.length())))) {
						prediction2Map.put("drwt_no2", Integer.parseInt( minTargetId1.substring(3, minTargetId1.length())));
					}
					// 4번째로 가장 당첨되지 않은 횟수가 높은 번호
					if(!prediction2Map.containsValue(Integer.parseInt( minTargetId2.substring(3, minTargetId2.length())))) {
						prediction2Map.put("drwt_no3", Integer.parseInt( minTargetId2.substring(3, minTargetId2.length())));
					}
					// 6번째로 가장 당첨되지 않은 횟수가 높은 번호
					if(!prediction2Map.containsValue(Integer.parseInt( minTargetId3.substring(3, minTargetId3.length())))) {
						prediction2Map.put("drwt_no4", Integer.parseInt( minTargetId3.substring(3, minTargetId3.length())));
					}
					
					// 3번째로 가장 당첨횟수가 높은 번호
					if(!prediction2Map.containsValue(Integer.parseInt( bigTargetId1.substring(3, bigTargetId1.length())))) {
						prediction2Map.put("drwt_no5", Integer.parseInt( bigTargetId1.substring(3, bigTargetId1.length())));
					}
					// 5번째로 가장 당첨횟수가 높은 번호
					if(!prediction2Map.containsValue(Integer.parseInt( bigTargetId2.substring(3, bigTargetId2.length())))) {
						prediction2Map.put("drwt_no6", Integer.parseInt( bigTargetId2.substring(3, bigTargetId2.length())));
					}

					
					if(!prediction2Map.containsKey("drwt_no2")) {
						if(!prediction2Map.containsValue(Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())))) {
							prediction2Map.put("drwt_no2", Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())));	
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())))) {
							prediction2Map.put("drwt_no2", Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())))) {
							prediction2Map.put("drwt_no2", Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())))) {
							prediction2Map.put("drwt_no2", Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())))) {
							prediction2Map.put("drwt_no2", Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())))) {
							prediction2Map.put("drwt_no2", Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())));
						} else {
							prediction2Map.put("drwt_no2", 0);
						}
					}
					
					if(!prediction2Map.containsKey("drwt_no3")) {
						if(!prediction2Map.containsValue(Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())))) {
							prediction2Map.put("drwt_no3", Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())));	
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())))) {
							prediction2Map.put("drwt_no3", Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())))) {
							prediction2Map.put("drwt_no3", Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())))) {
							prediction2Map.put("drwt_no3", Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())))) {
							prediction2Map.put("drwt_no3", Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())))) {
							prediction2Map.put("drwt_no3", Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())));
						} else {
							prediction2Map.put("drwt_no3", 0);
						}
					}
					
					if(!prediction2Map.containsKey("drwt_no4")) {
						if(!prediction2Map.containsValue(Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())))) {
							prediction2Map.put("drwt_no4", Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())));	
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())))) {
							prediction2Map.put("drwt_no4", Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())))) {
							prediction2Map.put("drwt_no4", Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())))) {
							prediction2Map.put("drwt_no4", Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())))) {
							prediction2Map.put("drwt_no4", Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())))) {
							prediction2Map.put("drwt_no4", Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())));
						} else {
							prediction2Map.put("drwt_no4", 0);
						}
					}
					
					if(!prediction2Map.containsKey("drwt_no5")) {
						if(!prediction2Map.containsValue(Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())))) {
							prediction2Map.put("drwt_no5", Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())));	
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())))) {
							prediction2Map.put("drwt_no5", Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())))) {
							prediction2Map.put("drwt_no5", Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())))) {
							prediction2Map.put("drwt_no5", Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())))) {
							prediction2Map.put("drwt_no5", Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())))) {
							prediction2Map.put("drwt_no5", Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())));
						} else {
							prediction2Map.put("drwt_no5", 0);
						}
					}
					
					if(!prediction2Map.containsKey("drwt_no6")) {
						if(!prediction2Map.containsValue(Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())))) {
							prediction2Map.put("drwt_no6", Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())));	
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())))) {
							prediction2Map.put("drwt_no6", Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())))) {
							prediction2Map.put("drwt_no6", Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())))) {
							prediction2Map.put("drwt_no6", Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())))) {
							prediction2Map.put("drwt_no6", Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())));
						} else if(!prediction2Map.containsValue(Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())))) {
							prediction2Map.put("drwt_no6", Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())));
						} else {
							prediction2Map.put("drwt_no6", 0);
						}
					}
					
					List<Integer> numList = new ArrayList<Integer>();
					numList.add((Integer)prediction2Map.get("drwt_no1"));
					numList.add((Integer)prediction2Map.get("drwt_no2"));
					numList.add((Integer)prediction2Map.get("drwt_no3"));
					numList.add((Integer)prediction2Map.get("drwt_no4"));
					numList.add((Integer)prediction2Map.get("drwt_no5"));
					numList.add((Integer)prediction2Map.get("drwt_no6"));
					numList = selectionSort2(numList);
					prediction2Map.put("drwt_no1", numList.get(0));
					prediction2Map.put("drwt_no2", numList.get(1));
					prediction2Map.put("drwt_no3", numList.get(2));
					prediction2Map.put("drwt_no4", numList.get(3));
					prediction2Map.put("drwt_no5", numList.get(4));
					prediction2Map.put("drwt_no6", numList.get(5));
					scheduleDao.savePredictionNumber(prediction2Map);
				} else if(i==3) {
					prediction3Map.put("drwt_no1", overlapNum);
					prediction3Map.put("no_desc", 3);
					
					String minTargetId1 = numIdList.get(numIdList.size()-1);
					String minTargetId2 = numIdList.get(numIdList.size()-2);
					String minTargetId3 = numIdList.get(numIdList.size()-5);
					
					String bigTargetId1 = numIdList.get(numIdList.size()-3);
					String bigTargetId2 = numIdList.get(numIdList.size()-4);
					
					String candidateMin1 = numIdList.get(numIdList.size()-6);
					String candidateMin2 = numIdList.get(numIdList.size()-7);
					String candidateMin3 = numIdList.get(numIdList.size()-10);
					String candidateMin4 = numIdList.get(numIdList.size()-11);
					String candidateMin5 = numIdList.get(numIdList.size()-15);
					String candidateMin6 = numIdList.get(numIdList.size()-16);
					
					String candidateMax1 = numIdList.get(numIdList.size()-8);
					String candidateMax2 = numIdList.get(numIdList.size()-9);
					String candidateMax3 = numIdList.get(numIdList.size()-12);
					String candidateMax4 = numIdList.get(numIdList.size()-13);
					String candidateMax5 = numIdList.get(numIdList.size()-14);
					String candidateMax6 = numIdList.get(numIdList.size()-17);
					
					// 2번째로 가장 당첨되지 않은 횟수가 높은 번호
					if(!prediction3Map.containsValue(Integer.parseInt( minTargetId1.substring(3, minTargetId1.length())))) {
						prediction3Map.put("drwt_no2", Integer.parseInt( minTargetId1.substring(3, minTargetId1.length())));
					}
					// 4번째로 가장 당첨되지 않은 횟수가 높은 번호
					if(!prediction3Map.containsValue(Integer.parseInt( minTargetId2.substring(3, minTargetId2.length())))) {
						prediction3Map.put("drwt_no3", Integer.parseInt( minTargetId2.substring(3, minTargetId2.length())));
					}
					// 6번째로 가장 당첨되지 않은 횟수가 높은 번호
					if(!prediction3Map.containsValue(Integer.parseInt( minTargetId3.substring(3, minTargetId3.length())))) {
						prediction3Map.put("drwt_no4", Integer.parseInt( minTargetId3.substring(3, minTargetId3.length())));
					}
					
					// 3번째로 가장 당첨횟수가 높은 번호
					if(!prediction3Map.containsValue(Integer.parseInt( bigTargetId1.substring(3, bigTargetId1.length())))) {
						prediction3Map.put("drwt_no5", Integer.parseInt( bigTargetId1.substring(3, bigTargetId1.length())));
					}
					// 5번째로 가장 당첨횟수가 높은 번호
					if(!prediction3Map.containsValue(Integer.parseInt( bigTargetId2.substring(3, bigTargetId2.length())))) {
						prediction3Map.put("drwt_no6", Integer.parseInt( bigTargetId2.substring(3, bigTargetId2.length())));
					}
					
					if(!prediction3Map.containsKey("drwt_no2")) {
						if(!prediction3Map.containsValue(Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())))) {
							prediction3Map.put("drwt_no2", Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())));	
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())))) {
							prediction3Map.put("drwt_no2", Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())))) {
							prediction3Map.put("drwt_no2", Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())))) {
							prediction3Map.put("drwt_no2", Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())))) {
							prediction3Map.put("drwt_no2", Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())))) {
							prediction3Map.put("drwt_no2", Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())));
						} else {
							prediction3Map.put("drwt_no2", 0);
						}
					}
					
					if(!prediction3Map.containsKey("drwt_no3")) {
						if(!prediction3Map.containsValue(Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())))) {
							prediction3Map.put("drwt_no3", Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())));	
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())))) {
							prediction3Map.put("drwt_no3", Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())))) {
							prediction3Map.put("drwt_no3", Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())))) {
							prediction3Map.put("drwt_no3", Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())))) {
							prediction3Map.put("drwt_no3", Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())))) {
							prediction3Map.put("drwt_no3", Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())));
						} else {
							prediction3Map.put("drwt_no3", 0);
						}
					}
					
					if(!prediction3Map.containsKey("drwt_no4")) {
						if(!prediction3Map.containsValue(Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())))) {
							prediction3Map.put("drwt_no4", Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())));	
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())))) {
							prediction3Map.put("drwt_no4", Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())))) {
							prediction3Map.put("drwt_no4", Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())))) {
							prediction3Map.put("drwt_no4", Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())))) {
							prediction3Map.put("drwt_no4", Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())))) {
							prediction3Map.put("drwt_no4", Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())));
						} else {
							prediction3Map.put("drwt_no4", 0);
						}
					}
					
					if(!prediction3Map.containsKey("drwt_no5")) {
						if(!prediction3Map.containsValue(Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())))) {
							prediction3Map.put("drwt_no5", Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())));	
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())))) {
							prediction3Map.put("drwt_no5", Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())))) {
							prediction3Map.put("drwt_no5", Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())))) {
							prediction3Map.put("drwt_no5", Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())))) {
							prediction3Map.put("drwt_no5", Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())))) {
							prediction3Map.put("drwt_no5", Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())));
						} else {
							prediction3Map.put("drwt_no5", 0);
						}
					}
					
					if(!prediction3Map.containsKey("drwt_no6")) {
						if(!prediction3Map.containsValue(Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())))) {
							prediction3Map.put("drwt_no6", Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())));	
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())))) {
							prediction3Map.put("drwt_no6", Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())))) {
							prediction3Map.put("drwt_no6", Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())))) {
							prediction3Map.put("drwt_no6", Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())))) {
							prediction3Map.put("drwt_no6", Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())));
						} else if(!prediction3Map.containsValue(Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())))) {
							prediction3Map.put("drwt_no6", Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())));
						} else {
							prediction3Map.put("drwt_no6", 0);
						}
					}
					
					List<Integer> numList = new ArrayList<Integer>();
					numList.add((Integer)prediction3Map.get("drwt_no1"));
					numList.add((Integer)prediction3Map.get("drwt_no2"));
					numList.add((Integer)prediction3Map.get("drwt_no3"));
					numList.add((Integer)prediction3Map.get("drwt_no4"));
					numList.add((Integer)prediction3Map.get("drwt_no5"));
					numList.add((Integer)prediction3Map.get("drwt_no6"));
					numList = selectionSort2(numList);
					prediction3Map.put("drwt_no1", numList.get(0));
					prediction3Map.put("drwt_no2", numList.get(1));
					prediction3Map.put("drwt_no3", numList.get(2));
					prediction3Map.put("drwt_no4", numList.get(3));
					prediction3Map.put("drwt_no5", numList.get(4));
					prediction3Map.put("drwt_no6", numList.get(5));
					scheduleDao.savePredictionNumber(prediction3Map);
					
				} else if(i==4) {
					prediction4Map.put("drwt_no1", overlapNum);
					prediction4Map.put("no_desc", 4);
					
					
					String minTargetId1 = numIdList.get(2);
					String minTargetId2 = numIdList.get(4);
					String minTargetId3 = numIdList.get(6);
					
					String bigTargetId1 = numIdList.get(numIdList.size()-1);
					String bigTargetId2 = numIdList.get(numIdList.size()-3);
					
					String candidateMin1 = numIdList.get(3);
					String candidateMin2 = numIdList.get(5);
					String candidateMin3 = numIdList.get(1);
					String candidateMin4 = numIdList.get(7);
					String candidateMin5 = numIdList.get(8);
					String candidateMin6 = numIdList.get(9);
					
					String candidateMax1 = numIdList.get(numIdList.size()-5);
					String candidateMax2 = numIdList.get(numIdList.size()-2);
					String candidateMax3 = numIdList.get(numIdList.size()-4);
					String candidateMax4 = numIdList.get(numIdList.size()-6);
					String candidateMax5 = numIdList.get(numIdList.size()-7);
					String candidateMax6 = numIdList.get(numIdList.size()-8);
					
					logger.debug("JH : [ CHECK 4 ] -- START");
					logger.debug("JH : minTargetId1 = "+minTargetId1);
					logger.debug("JH : minTargetId2 = "+minTargetId2);
					logger.debug("JH : minTargetId3 = "+minTargetId3);
					logger.debug("JH : ");
					logger.debug("JH : bigTargetId1 = "+bigTargetId1);
					logger.debug("JH : bigTargetId2 = "+bigTargetId2);
					logger.debug("JH : ");
					logger.debug("JH : candidateMin1 = "+candidateMin1);
					logger.debug("JH : candidateMin2 = "+candidateMin2);
					logger.debug("JH : candidateMin3 = "+candidateMin3);
					logger.debug("JH : candidateMin4 = "+candidateMin4);
					logger.debug("JH : candidateMin5 = "+candidateMin5);
					logger.debug("JH : candidateMin6 = "+candidateMin6);
					logger.debug("JH : ");
					logger.debug("JH : candidateMax1 = "+candidateMax1);
					logger.debug("JH : candidateMax2 = "+candidateMax2);
					logger.debug("JH : candidateMax3 = "+candidateMax3);
					logger.debug("JH : candidateMax4 = "+candidateMax4);
					logger.debug("JH : candidateMax5 = "+candidateMax5);
					logger.debug("JH : candidateMax6 = "+candidateMax6);
					logger.debug("JH : ");
					logger.debug("JH : replaceCheck1"+Integer.parseInt( minTargetId1.substring(3, minTargetId1.length())));
					logger.debug("JH : replaceCheck2"+Integer.parseInt( minTargetId1.substring(3, minTargetId2.length())));
					logger.debug("JH : replaceCheck3"+Integer.parseInt( minTargetId1.substring(3, minTargetId3.length())));
					logger.debug("JH : replaceCheck4"+Integer.parseInt( minTargetId1.substring(3, bigTargetId1.length())));
					logger.debug("JH : replaceCheck5"+Integer.parseInt( minTargetId1.substring(3, bigTargetId2.length())));
					logger.debug("JH : [ CHECK 4 ] -- END");
					
					// 2번째로 가장 당첨되지 않은 횟수가 높은 번호
					if(!prediction4Map.containsValue(Integer.parseInt( minTargetId1.substring(3, minTargetId1.length())))) {
						prediction4Map.put("drwt_no2", Integer.parseInt( minTargetId1.substring(3, minTargetId1.length())));
					}
					// 4번째로 가장 당첨되지 않은 횟수가 높은 번호
					if(!prediction4Map.containsValue(Integer.parseInt( minTargetId2.substring(3, minTargetId2.length())))) {
						prediction4Map.put("drwt_no3", Integer.parseInt( minTargetId2.substring(3, minTargetId2.length())));
					}
					// 6번째로 가장 당첨되지 않은 횟수가 높은 번호
					if(!prediction4Map.containsValue(Integer.parseInt( minTargetId3.substring(3, minTargetId3.length())))) {
						prediction4Map.put("drwt_no4", Integer.parseInt( minTargetId3.substring(3, minTargetId3.length())));
					}
					
					// 3번째로 가장 당첨횟수가 높은 번호
					if(!prediction4Map.containsValue(Integer.parseInt( bigTargetId1.substring(3, bigTargetId1.length())))) {
						prediction4Map.put("drwt_no5", Integer.parseInt( bigTargetId1.substring(3, bigTargetId1.length())));
					}
					// 5번째로 가장 당첨횟수가 높은 번호
					if(!prediction4Map.containsValue(Integer.parseInt( bigTargetId2.substring(3, bigTargetId2.length())))) {
						prediction4Map.put("drwt_no6", Integer.parseInt( bigTargetId2.substring(3, bigTargetId2.length())));
					}

					
					if(!prediction4Map.containsKey("drwt_no2")) {
						if(!prediction4Map.containsValue(Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())))) {
							prediction4Map.put("drwt_no2", Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())));	
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())))) {
							prediction4Map.put("drwt_no2", Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())))) {
							prediction4Map.put("drwt_no2", Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())))) {
							prediction4Map.put("drwt_no2", Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())))) {
							prediction4Map.put("drwt_no2", Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())))) {
							prediction4Map.put("drwt_no2", Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())));
						} else {
							prediction4Map.put("drwt_no2", 0);
						}
					}
					
					if(!prediction4Map.containsKey("drwt_no3")) {
						if(!prediction4Map.containsValue(Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())))) {
							prediction4Map.put("drwt_no3", Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())));	
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())))) {
							prediction4Map.put("drwt_no3", Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())))) {
							prediction4Map.put("drwt_no3", Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())))) {
							prediction4Map.put("drwt_no3", Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())))) {
							prediction4Map.put("drwt_no3", Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())))) {
							prediction4Map.put("drwt_no3", Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())));
						} else {
							prediction4Map.put("drwt_no3", 0);
						}
					}
					
					if(!prediction4Map.containsKey("drwt_no4")) {
						if(!prediction4Map.containsValue(Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())))) {
							prediction4Map.put("drwt_no4", Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())));	
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())))) {
							prediction4Map.put("drwt_no4", Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())))) {
							prediction4Map.put("drwt_no4", Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())))) {
							prediction4Map.put("drwt_no4", Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())))) {
							prediction4Map.put("drwt_no4", Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())))) {
							prediction4Map.put("drwt_no4", Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())));
						} else {
							prediction4Map.put("drwt_no4", 0);
						}
					}
					
					if(!prediction4Map.containsKey("drwt_no5")) {
						if(!prediction4Map.containsValue(Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())))) {
							prediction4Map.put("drwt_no5", Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())));	
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())))) {
							prediction4Map.put("drwt_no5", Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())))) {
							prediction4Map.put("drwt_no5", Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())))) {
							prediction4Map.put("drwt_no5", Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())))) {
							prediction4Map.put("drwt_no5", Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())))) {
							prediction4Map.put("drwt_no5", Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())));
						} else {
							prediction4Map.put("drwt_no5", 0);
						}
					}
					
					if(!prediction4Map.containsKey("drwt_no6")) {
						if(!prediction4Map.containsValue(Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())))) {
							prediction4Map.put("drwt_no6", Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())));	
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())))) {
							prediction4Map.put("drwt_no6", Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())))) {
							prediction4Map.put("drwt_no6", Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())))) {
							prediction4Map.put("drwt_no6", Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())))) {
							prediction4Map.put("drwt_no6", Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())));
						} else if(!prediction4Map.containsValue(Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())))) {
							prediction4Map.put("drwt_no6", Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())));
						} else {
							prediction4Map.put("drwt_no6", 0);
						}
					}
					
					List<Integer> numList = new ArrayList<Integer>();
					numList.add((Integer)prediction4Map.get("drwt_no1"));
					numList.add((Integer)prediction4Map.get("drwt_no2"));
					numList.add((Integer)prediction4Map.get("drwt_no3"));
					numList.add((Integer)prediction4Map.get("drwt_no4"));
					numList.add((Integer)prediction4Map.get("drwt_no5"));
					numList.add((Integer)prediction4Map.get("drwt_no6"));
					numList = selectionSort2(numList);
					prediction4Map.put("drwt_no1", numList.get(0));
					prediction4Map.put("drwt_no2", numList.get(1));
					prediction4Map.put("drwt_no3", numList.get(2));
					prediction4Map.put("drwt_no4", numList.get(3));
					prediction4Map.put("drwt_no5", numList.get(4));
					prediction4Map.put("drwt_no6", numList.get(5));
					scheduleDao.savePredictionNumber(prediction4Map);
				} else if(i==5) {
					prediction5Map.put("drwt_no1", overlapNum);
					prediction5Map.put("no_desc", 5);
					
					String minTargetId1 = numIdList.get(5);
					String minTargetId2 = numIdList.get(7);
					String minTargetId3 = numIdList.get(9);
					
					String bigTargetId1 = numIdList.get(numIdList.size()-4);
					String bigTargetId2 = numIdList.get(numIdList.size()-6);
					
					String candidateMin1 = numIdList.get(8);
					String candidateMin2 = numIdList.get(6);
					String candidateMin3 = numIdList.get(4);
					String candidateMin4 = numIdList.get(10);
					String candidateMin5 = numIdList.get(11);
					String candidateMin6 = numIdList.get(12);
					
					String candidateMax1 = numIdList.get(numIdList.size()-8);
					String candidateMax2 = numIdList.get(numIdList.size()-5);
					String candidateMax3 = numIdList.get(numIdList.size()-7);
					String candidateMax4 = numIdList.get(numIdList.size()-9);
					String candidateMax5 = numIdList.get(numIdList.size()-10);
					String candidateMax6 = numIdList.get(numIdList.size()-11);
					
					logger.debug("JH : [ CHECK 5 ] -- START");
					logger.debug("JH : minTargetId1 = "+minTargetId1);
					logger.debug("JH : minTargetId2 = "+minTargetId2);
					logger.debug("JH : minTargetId3 = "+minTargetId3);
					logger.debug("JH : ");
					logger.debug("JH : bigTargetId1 = "+bigTargetId1);
					logger.debug("JH : bigTargetId2 = "+bigTargetId2);
					logger.debug("JH : ");
					logger.debug("JH : candidateMin1 = "+candidateMin1);
					logger.debug("JH : candidateMin2 = "+candidateMin2);
					logger.debug("JH : candidateMin3 = "+candidateMin3);
					logger.debug("JH : candidateMin4 = "+candidateMin4);
					logger.debug("JH : candidateMin5 = "+candidateMin5);
					logger.debug("JH : candidateMin6 = "+candidateMin6);
					logger.debug("JH : ");
					logger.debug("JH : candidateMax1 = "+candidateMax1);
					logger.debug("JH : candidateMax2 = "+candidateMax2);
					logger.debug("JH : candidateMax3 = "+candidateMax3);
					logger.debug("JH : candidateMax4 = "+candidateMax4);
					logger.debug("JH : candidateMax5 = "+candidateMax5);
					logger.debug("JH : candidateMax6 = "+candidateMax6);
					logger.debug("JH : ");
					logger.debug("JH : replaceCheck1"+Integer.parseInt( minTargetId1.substring(3, minTargetId1.length())));
					logger.debug("JH : replaceCheck2"+Integer.parseInt( minTargetId1.substring(3, minTargetId2.length())));
					logger.debug("JH : replaceCheck3"+Integer.parseInt( minTargetId1.substring(3, minTargetId3.length())));
					logger.debug("JH : replaceCheck4"+Integer.parseInt( minTargetId1.substring(3, bigTargetId1.length())));
					logger.debug("JH : replaceCheck5"+Integer.parseInt( minTargetId1.substring(3, bigTargetId2.length())));
					logger.debug("JH : [ CHECK 5 ] -- END");
					
					// 2번째로 가장 당첨되지 않은 횟수가 높은 번호
					if(!prediction5Map.containsValue(Integer.parseInt( minTargetId1.substring(3, minTargetId1.length())))) {
						prediction5Map.put("drwt_no2", Integer.parseInt( minTargetId1.substring(3, minTargetId1.length())));
					}
					// 4번째로 가장 당첨되지 않은 횟수가 높은 번호
					if(!prediction5Map.containsValue(Integer.parseInt( minTargetId2.substring(3, minTargetId2.length())))) {
						prediction5Map.put("drwt_no3", Integer.parseInt( minTargetId2.substring(3, minTargetId2.length())));
					}
					// 6번째로 가장 당첨되지 않은 횟수가 높은 번호
					if(!prediction5Map.containsValue(Integer.parseInt( minTargetId3.substring(3, minTargetId3.length())))) {
						prediction5Map.put("drwt_no4", Integer.parseInt( minTargetId3.substring(3, minTargetId3.length())));
					}
					
					// 3번째로 가장 당첨횟수가 높은 번호
					if(!prediction5Map.containsValue(Integer.parseInt( bigTargetId1.substring(3, bigTargetId1.length())))) {
						prediction5Map.put("drwt_no5", Integer.parseInt( bigTargetId1.substring(3, bigTargetId1.length())));
					}
					// 5번째로 가장 당첨횟수가 높은 번호
					if(!prediction5Map.containsValue(Integer.parseInt( bigTargetId2.substring(3, bigTargetId2.length())))) {
						prediction5Map.put("drwt_no6", Integer.parseInt( bigTargetId2.substring(3, bigTargetId2.length())));
					}
					
					if(!prediction5Map.containsKey("drwt_no2")) {
						if(!prediction5Map.containsValue(Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())))) {
							prediction5Map.put("drwt_no2", Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())));	
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())))) {
							prediction5Map.put("drwt_no2", Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())))) {
							prediction5Map.put("drwt_no2", Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())))) {
							prediction5Map.put("drwt_no2", Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())))) {
							prediction5Map.put("drwt_no2", Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())))) {
							prediction5Map.put("drwt_no2", Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())));
						} else {
							prediction5Map.put("drwt_no2", 0);
						}
					}
					
					if(!prediction5Map.containsKey("drwt_no3")) {
						if(!prediction5Map.containsValue(Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())))) {
							prediction5Map.put("drwt_no3", Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())));	
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())))) {
							prediction5Map.put("drwt_no3", Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())))) {
							prediction5Map.put("drwt_no3", Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())))) {
							prediction5Map.put("drwt_no3", Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())))) {
							prediction5Map.put("drwt_no3", Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())))) {
							prediction5Map.put("drwt_no3", Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())));
						} else {
							prediction5Map.put("drwt_no3", 0);
						}
					}
					
					if(!prediction5Map.containsKey("drwt_no4")) {
						if(!prediction5Map.containsValue(Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())))) {
							prediction5Map.put("drwt_no4", Integer.parseInt( candidateMin1.substring(3, candidateMin1.length())));	
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())))) {
							prediction5Map.put("drwt_no4", Integer.parseInt( candidateMin2.substring(3, candidateMin2.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())))) {
							prediction5Map.put("drwt_no4", Integer.parseInt( candidateMin3.substring(3, candidateMin3.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())))) {
							prediction5Map.put("drwt_no4", Integer.parseInt( candidateMin4.substring(3, candidateMin4.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())))) {
							prediction5Map.put("drwt_no4", Integer.parseInt( candidateMin5.substring(3, candidateMin5.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())))) {
							prediction5Map.put("drwt_no4", Integer.parseInt( candidateMin6.substring(3, candidateMin6.length())));
						} else {
							prediction5Map.put("drwt_no4", 0);
						}
					}
					
					if(!prediction5Map.containsKey("drwt_no5")) {
						if(!prediction5Map.containsValue(Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())))) {
							prediction5Map.put("drwt_no5", Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())));	
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())))) {
							prediction5Map.put("drwt_no5", Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())))) {
							prediction5Map.put("drwt_no5", Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())))) {
							prediction5Map.put("drwt_no5", Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())))) {
							prediction5Map.put("drwt_no5", Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())))) {
							prediction5Map.put("drwt_no5", Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())));
						} else {
							prediction5Map.put("drwt_no5", 0);
						}
					}
					
					if(!prediction5Map.containsKey("drwt_no6")) {
						if(!prediction5Map.containsValue(Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())))) {
							prediction5Map.put("drwt_no6", Integer.parseInt( candidateMax1.substring(3, candidateMax1.length())));	
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())))) {
							prediction5Map.put("drwt_no6", Integer.parseInt( candidateMax2.substring(3, candidateMax2.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())))) {
							prediction5Map.put("drwt_no6", Integer.parseInt( candidateMax3.substring(3, candidateMax3.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())))) {
							prediction5Map.put("drwt_no6", Integer.parseInt( candidateMax4.substring(3, candidateMax4.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())))) {
							prediction5Map.put("drwt_no6", Integer.parseInt( candidateMax5.substring(3, candidateMax5.length())));
						} else if(!prediction5Map.containsValue(Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())))) {
							prediction5Map.put("drwt_no6", Integer.parseInt( candidateMax6.substring(3, candidateMax6.length())));
						} else {
							prediction5Map.put("drwt_no6", 0);
						}
					}
					
					List<Integer> numList = new ArrayList<Integer>();
					numList.add((Integer)prediction5Map.get("drwt_no1"));
					numList.add((Integer)prediction5Map.get("drwt_no2"));
					numList.add((Integer)prediction5Map.get("drwt_no3"));
					numList.add((Integer)prediction5Map.get("drwt_no4"));
					numList.add((Integer)prediction5Map.get("drwt_no5"));
					numList.add((Integer)prediction5Map.get("drwt_no6"));
					numList = selectionSort2(numList);
					prediction5Map.put("drwt_no1", numList.get(0));
					prediction5Map.put("drwt_no2", numList.get(1));
					prediction5Map.put("drwt_no3", numList.get(2));
					prediction5Map.put("drwt_no4", numList.get(3));
					prediction5Map.put("drwt_no5", numList.get(4));
					prediction5Map.put("drwt_no6", numList.get(5));
					scheduleDao.savePredictionNumber(prediction5Map);
				}
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	public List<Integer> selectionSort2(List<Integer> numList) {
		logger.debug("JH : "+getClass().getName()+" - selectionSort2 - numList - "+numList);
		int size = numList.size();
        int min; //최소값을 가진 데이터의 인덱스 저장 변수
        int temp;
        for(int i=0; i<size-1; i++){ // size-1 : 마지막 요소는 자연스럽게 정렬됨
            min = i;
            for(int j=i+1; j<size; j++){
                if(numList.get(min) > numList.get(j)){
                    min = j;
                }
            }
            temp = numList.get(min);
            numList.set(min, numList.get(i));
            numList.set(i, temp);
        }
		return numList;
	}
	
	
	
}
