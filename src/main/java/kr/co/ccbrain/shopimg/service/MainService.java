package kr.co.ccbrain.shopimg.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ccbrain.shopimg.mapper.MainMapper;

@Transactional
@Service
@SuppressWarnings("rawtypes")
public class MainService {
	
	@Autowired
	private MainMapper mainMapper;

	public List init() throws Exception {
		return mainMapper.get();
	}

	public List getServiceInfo() throws Exception {
		return mainMapper.selectServiceInfo();
	}
	
	public String getLatestDir(Map param) throws Exception {
		return mainMapper.selectLatestDir(param);
	}
}
