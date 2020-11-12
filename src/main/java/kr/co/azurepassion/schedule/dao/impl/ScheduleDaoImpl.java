package kr.co.azurepassion.schedule.dao.impl;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.azurepassion.schedule.dao.ScheduleDao;

@Repository("scheduleDao")
public class ScheduleDaoImpl implements ScheduleDao {
	private static Logger logger = LoggerFactory.getLogger(ScheduleDaoImpl.class);
    @Autowired
    private SqlSession sqlSession;

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
    
    public int saveLottoInfo(Map<String, Object> paramMap) {
    	logger.debug("JH : "+getClass().getName()+" - saveLottoInfo - paramMap = "+paramMap);
    	return sqlSession.insert("insertLottoInfo", paramMap);
    }
    
    public int savePredictionNumber(Map<String, Object> paramMap) {
    	logger.debug("JH : "+getClass().getName()+" - savePredictionNumber - paramMap = "+paramMap);
    	return sqlSession.insert("insertPredictionNumber", paramMap);
    }
    
    	
}
