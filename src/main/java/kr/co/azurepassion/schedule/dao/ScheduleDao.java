package kr.co.azurepassion.schedule.dao;

import java.util.Map;

public interface ScheduleDao {

	int saveLottoInfo(Map<String, Object> paramMap);
	
	int savePredictionNumber(Map<String, Object> paramMap);
	

}
