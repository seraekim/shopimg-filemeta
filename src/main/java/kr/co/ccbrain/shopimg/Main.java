package kr.co.ccbrain.shopimg;

import java.io.File;
import java.text.SimpleDateFormat;
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
		try {
			List<Map> serviceList = mainService.getServiceInfo();
			for (Map s : serviceList) {
				listFilesForDir(s);
			}
			// listFilesForFolder(new File(rootDir));
			// System.out.println(mainService.init());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	private void listFilesForDir(Map map) throws Exception {

		String dir = rootDir + "/" + map.get("colPath");
		String latestDirOftb = mainService.getLatestDir(map);
		System.out.println("dir : " + latestDirOftb);
		String id = (String) map.get("sId");
		File[] list = new File(dir).listFiles();
		if (list != null) {
			for (File entry : list) {
				if (entry.isDirectory()) {
					String realSubDir = entry.getName();
					System.out.println("realSubDir : "+realSubDir);
					if (isValidDir(realSubDir)) {
						if(latestDirOftb == null ||Integer.parseInt(realSubDir) > Integer.parseInt(latestDirOftb)) {
							File[] fs = new File(dir+"/"+realSubDir).listFiles();
							for (File f : fs) {
								String nm = f.getName();
								System.out.println(nm);
							}
						}
					}
				} else {
					System.out.println("f : " + entry.getName());
				}
			}
		}
	}

	private boolean isValidDir(String str) {
		return str.matches("^[\\d]{10}$");
	}
}
