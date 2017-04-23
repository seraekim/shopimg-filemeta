package kr.co.ccbrain.shopimg;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import kr.co.ccbrain.shopimg.service.MainService;

@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Main {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddhh");

	@Autowired
	private MainService mainService;

	@Value("${root_dir}")
	private String rootDir;

	public void init() {
		logger.info(rootDir);
		Map ss = null;
		try {
			List<Map> serviceList = mainService.getServiceInfo();
			for (Map s : serviceList) {
				ss = s;
				listFilesForDir(s);
			}
		} catch (Exception e) {
			logger.error(ss.toString());
			logger.error(e.getMessage(), e);
		}

	}

	private void listFilesForDir(Map param) throws Exception {

		String dir = rootDir + "/" + param.get("colPath");
		String latestDirOftb = mainService.getLatestDir(param);
		System.out.println("dir : " + latestDirOftb);
		File[] list = new File(dir).listFiles();
		if (list != null) {
			for (File entry : list) {
				if (entry.isDirectory()) {
					String realSubDir = entry.getName();
					System.out.println("realSubDir : " + realSubDir);
					if (isValidDir(realSubDir)) {
						if (latestDirOftb == null || Integer.parseInt(realSubDir) > Integer.parseInt(latestDirOftb)) {
							List<String> cateIdList = mainService.getCateId(param);

							String dd = realSubDir.substring(0, 8);
							String hh = realSubDir.substring(8);
							String rgSt = "2"; // 등록중
							param.put("dd", dd);
							param.put("hh", hh);
							param.put("rgSt", rgSt);

							// tb_col_result 등록중 상태 insert
							mainService.initTbColResult(param);

							// tb_err_file_info 파일 존재여부와 함께 insert
							for (String cId : cateIdList) {
								String prefix = cId + "." + realSubDir;
								File browser = new File(dir + "/" + realSubDir + "/" + prefix + ".browser.jpg");
								File html = new File(dir + "/" + realSubDir + "/" + prefix + ".html");
								File win = new File(dir + "/" + realSubDir + "/" + prefix + ".win.jpg");

								boolean isAllGood = true;

								param.put("cId", cId);
								if (!browser.isFile()) {
									param.put("fk", "0");
									param.put("fSt", "01");
									isAllGood = false;
									mainService.addTbErrFileInfo(param);
								}
								// throw new Exception();

								if (!win.isFile()) {
									param.put("fk", "1");
									param.put("fSt", "01");
									isAllGood = false;
									mainService.addTbErrFileInfo(param);
								}

								if (!html.isFile()) {
									param.put("fk", "2");
									param.put("fSt", "01");
									isAllGood = false;
									mainService.addTbErrFileInfo(param);
								}

								// tb_col_result 파일 등록 상태 update
								if (isAllGood) {
									param.put("rgSt", "0");
								} else {
									param.put("rgSt", "9");
								}
								mainService.updTbColResult(param);
							}
						}
					}
				}
			}
		}
	}

	private boolean isValidDir(String str) {
		return str.matches("^[\\d]{10}$");
	}
}
