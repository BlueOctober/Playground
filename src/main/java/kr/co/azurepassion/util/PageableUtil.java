package kr.co.azurepassion.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PageableUtil {
	
	public Map<String, Object> getRowBounds(int startPage, int visiblePages){
		
		if(startPage==1){
			startPage = 0;
		}else{
			startPage = (startPage-1)*visiblePages;
		}
		Map<String, Object> pageable = new HashMap<String, Object>();
		
		pageable.put("start", startPage);
		pageable.put("end", visiblePages);
		
		return pageable;
	}

}