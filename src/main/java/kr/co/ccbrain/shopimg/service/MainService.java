package kr.co.ccbrain.shopimg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ccbrain.shopimg.mapper.MainMapper;

@Transactional
@Service
@SuppressWarnings("rawtypes")
public class MainService {
	@Autowired
	MainMapper mainMapper;

	public List init() throws Exception {
		return mainMapper.get();
	}
}
