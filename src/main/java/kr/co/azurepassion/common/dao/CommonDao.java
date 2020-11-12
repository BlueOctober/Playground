package kr.co.azurepassion.common.dao;

import java.util.List;
import java.util.Map;

public interface CommonDao {

	List<Map<String, Object>> getPredictationNumber(Map<String, Object> paramMap);

	Map<String, Object> getCurrentHistoryNumber(Map<String, Object> paramMap);

}
