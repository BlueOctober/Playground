package kr.co.azurepassion.common.dao.impl;

import kr.co.azurepassion.common.dao.CommonDao;
import kr.co.azurepassion.util.PageableUtil;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("commonDao")
public class CommonDaoImpl implements CommonDao {
    private static Logger logger = LoggerFactory.getLogger(CommonDaoImpl.class);

    @Autowired
    private SqlSession sqlSession;

    public void setSqlSession(SqlSession sqlSession){
        this.sqlSession = sqlSession;
    }

    @Autowired
    private PageableUtil pageableUtil;
}
