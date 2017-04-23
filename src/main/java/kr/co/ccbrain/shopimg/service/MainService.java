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
	
	public List<String> getCateId(Map param) throws Exception {
		return mainMapper.selectCateId(param);
	}
	
	public int initTbColResult(Map param) throws Exception {
		return mainMapper.insertTbColResult(param);
	}
	
	public int addTbErrFileInfo(Map param) throws Exception {
		return mainMapper.insertTbErrFileInfo(param);
	}
	
	public int updTbColResult(Map param) throws Exception {
		return mainMapper.updateTbColResult(param);
	}
}
