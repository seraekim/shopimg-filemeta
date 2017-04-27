package kr.co.ccbrain.shopimg.mapper;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface MainMapper {
	List<Map> get();
	List<Map> selectServiceInfo();
	String selectLatestDir(Map param);
	List<String> selectCateId(Map param);
	int insertTbColResult(Map param);
	int insertTbErrFileInfo(Map param);
	int updateTbColResult(Map param);
	
	int insertStaticTbColResult(Map param);
}
