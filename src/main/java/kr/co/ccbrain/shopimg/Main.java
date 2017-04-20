package kr.co.ccbrain.shopimg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.ccbrain.shopimg.service.MainService;

@Component
public class Main {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	MainService mainService;

	public void init() {
		logger.info("init");
		try {
			System.out.println(mainService.init());
			;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
