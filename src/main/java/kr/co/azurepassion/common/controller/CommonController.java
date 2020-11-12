package kr.co.azurepassion.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

@Controller
public class CommonController {
    private static Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    CommonService commonService;

    @Autowired @Qualifier("aes256Crypto")
    Crypto crypto;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main(Locale locale, Model model) {
        return "playground_main";
    }
    
    @RequestMapping(value = "/getPredictationNumber", method = RequestMethod.POST)
    @ResponseBody
    public Object getPredictationNumber(Locale locale, Model model) {
    	List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();
    	logger.debug("JH : "+getClass().getName()+" - getPredictationNumber execute..!!");
    	resultMapList = commonService.getPredictationNumber();
        return resultMapList;
    }
    
    
    @RequestMapping(value = "/lottoHistory", method = RequestMethod.GET)
    public String lottoHistory(Locale locale, Model model) {
        return "lotto_history";
    }
    
    @RequestMapping(value = "/getCurrentHistoryNumber", method = RequestMethod.POST)
    @ResponseBody
    public Object getCurrentHistoryNumber(Locale locale, Model model) {
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	logger.debug("JH : "+getClass().getName()+" - getCurrentHistoryNumber execute..!!");
        resultMap = commonService.getCurrentHistoryNumber();
//    	resultMap = commonService.getTestLogicResult2();
    	logger.debug("JH : getCurrentHistoryNumber - resultMap = "+resultMap);
        return resultMap;
    }
    
    
    		
//    @RequestMapping(value = "/getTestResult", method = RequestMethod.POST)
//    @ResponseBody
//    public Object getTestResult(Locale locale, Model model) {
//    	List<Integer> testList = new ArrayList<Integer>();
//    	testList.add(44);
//    	testList.add(32);
//    	testList.add(31);
//    	testList.add(25);
//    	testList.add(33);
//    	testList.add(19);
//    	
//    	logger.debug("JH : befroe - testList = "+testList);
//    	testList = selectionSort2(testList);
//    	logger.debug("JH : after - testList = "+testList);
//    	
//        return testList;
//    }
    
//	public List<Integer> selectionSort2(List<Integer> numList) {
//		logger.debug("JH : "+getClass().getName()+" - selectionSort2 - numList - "+numList);
//		int size = numList.size();
//        int min; //최소값을 가진 데이터의 인덱스 저장 변수
//        int temp;
//        for(int i=0; i<size-1; i++){ // size-1 : 마지막 요소는 자연스럽게 정렬됨
//            min = i;
//            for(int j=i+1; j<size; j++){
//                if(numList.get(min) > numList.get(j)){
//                    min = j;
//                }
//            }
//            temp = numList.get(min);
//            numList.set(min, numList.get(i));
//            numList.set(i, temp);
//        }
//		return numList;
//	}
    
    

}
