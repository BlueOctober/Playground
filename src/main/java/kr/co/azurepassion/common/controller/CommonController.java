package kr.co.azurepassion.common.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.azurepassion.common.cipher.Crypto;
import kr.co.azurepassion.common.service.CommonService;
import kr.co.azurepassion.util.TotalPage;

@Controller
public class CommonController {
    private static Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    CommonService commonService;

    @Autowired
    private TotalPage totalPage;

    @Autowired @Qualifier("aes256Crypto")
    Crypto crypto;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main(Locale locale, Model model) {
        return "playground_main";
    }
    
    @RequestMapping(value = "/getPredictationNumber", method = RequestMethod.POST)
    @ResponseBody
    public Object getPredictationNumber(Locale locale, Model model) {
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	logger.debug("JH : "+getClass().getName()+" - getPredictationNumber execute..!!");
        resultMap = commonService.getPredictationNumber();
        return resultMap;
    }
    
    

}
