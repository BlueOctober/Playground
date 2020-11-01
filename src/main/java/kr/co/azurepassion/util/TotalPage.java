package kr.co.azurepassion.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TotalPage {
	
	public BigDecimal getTotalPage(int totalCnt, int visiblePages){
		BigDecimal decimal1 = new BigDecimal(totalCnt);
		BigDecimal decimal2 = new BigDecimal(visiblePages);
		BigDecimal tatalPage = decimal1.divide(decimal2, 0, BigDecimal.ROUND_UP);
		
		return tatalPage;
	}

}
