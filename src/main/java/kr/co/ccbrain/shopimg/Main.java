package kr.co.ccbrain.shopimg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.ccbrain.shopimg.service.MainService;

@Component
public class Main {
	
	@Autowired
	MainService mainService;
	public void init() {
		System.out.println("init");
		try {
			System.out.println(mainService.init());;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
