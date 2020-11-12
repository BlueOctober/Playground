package kr.co.azurepassion.common.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.azurepassion.common.dao.CommonDao;

@Repository("commonDao")
public class CommonDaoImpl implements CommonDao {
    private static Logger logger = LoggerFactory.getLogger(CommonDaoImpl.class);

    @Autowired
    private SqlSession sqlSession;

    public void setSqlSession(SqlSession sqlSession){
        this.sqlSession = sqlSession;
    }


       
    public List<Map<String, Object>> getPredictationNumber(Map<String, Object> paramMap) {
    	logger.debug("JH : "+getClass().getName()+" - getPredictationNumber execute..!!");
		List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();
		resultMapList = sqlSession.selectList("selectPredictationNumber", paramMap);
		return resultMapList;
    }
    
    
    public Map<String, Object> getCurrentHistoryNumber(Map<String, Object> paramMap) {
    	logger.debug("JH : "+getClass().getName()+" - getCurrentHistoryNumber execute..!!");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = sqlSession.selectOne("selectCurrentHistoryNumber", paramMap);
		return resultMap;
    }
    
    
}
