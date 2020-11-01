package kr.co.azurepassion.common.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.azurepassion.common.service.CommonService;

@Service(value = "commonService")
public class CommonServiceImpl implements CommonService {
    private static Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

}
