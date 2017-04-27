package kr.co.ccbrain.shopimg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
			/*List<Map> serviceList = mainService.getServiceInfo();
			for (Map s : serviceList) {
				ss = s;
				listFilesForDir(s);
			}*/
			insertStaticFileInfo();
		} catch (Exception e) {
			logger.error(ss.toString());
			logger.error(e.getMessage(), e);
		}

	}

	public void insertStaticFileInfo() throws Exception {
		int cnt = 0;
		System.out.println(rootDir);
		File[] timeDirList = new File(rootDir).listFiles();
		System.out.println("start");
		Map param = new HashMap();
		
		
		for (File timeDir : restDir()) { // timeDirList
			String timeDirNm = timeDir.getName();
			String absPathNm = timeDir.getAbsolutePath();
			param.put("dd", timeDirNm.substring(0,8));
			param.put("hh", timeDirNm.substring(8));
			if(isValidDir(timeDirNm)) {
				File[] fileList = new File(rootDir+"/"+timeDirNm).listFiles();
				for(File f : fileList) {
					String fNm = f.getName();
					
					if(fNm.matches("\\w+.\\d{10}.html")) {
						String cId = fNm.substring(0,fNm.indexOf("."));
						param.put("cId", cId);
						String prefix = fNm.substring(0, fNm.length() - 5);
						File brof = new File(absPathNm+"/"+prefix+".browser.jpg");
						File winf = new File(absPathNm+"/"+prefix+".win.jpg");
						if(brof.exists() && winf.exists()) {
							param.put("rgSt", "0");
							System.out.println(cnt);
						} else {
							param.put("rgSt", "9");
							System.out.println(cnt+" 없음 "+fNm);
						}
						//System.out.println(param);
						mainService.initStaticTbColResult(param);
					}
					cnt++;
				}
			}
		}
	}
	
	
	public void collectTargetTxt() throws FileNotFoundException, IOException{
		String rooDir = "\\\\fileserver/working/project_logs/robot/result";
		File[] timeDirList = new File(rooDir).listFiles();
		PrintWriter pw = new PrintWriter(new FileWriter("D:/shopimg/target.txt"));
		int cnt = 0;
		System.out.println("start");
		for (File timeDir : timeDirList) {
			String timeDirNm = timeDir.getName();
			if(isValidDir(timeDirNm)) {
				File[] targetTxtList = new File(rooDir+"/"+timeDirNm).listFiles();
				for(File txt : targetTxtList) {
					String txtNm = txt.getName();
					if(txtNm.endsWith(".txt")) {
						System.out.println(cnt+" "+txtNm);
						cnt++;
						FileInputStream fis = new FileInputStream(txt);
						BufferedReader br = new BufferedReader(new InputStreamReader(fis));
						String line;
						while((line=br.readLine())!=null) {
							pw.write(line+"\n");
						}
					}
				}
			}
		}
		
		pw.close();
		System.out.println("finish");
	}
	
	public void distinctServiceId() throws FileNotFoundException, IOException{
		File f = new File("D:/shopimg/distinctTarget.txt");
		FileInputStream fis = new FileInputStream(f);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String line;
		String prevId = "";
		while((line=br.readLine())!=null) {
			String sId = line.split(",")[0];
			sId = sId.substring(0, sId.length()-8);
			if(!sId.equals(prevId)) {
				prevId = sId;
				System.out.println(prevId);
			}
		}
	}
	
	public void printInsertCateId() throws FileNotFoundException, IOException{
		File f = new File("D:/shopimg/distinctTarget.txt");
		FileInputStream fis = new FileInputStream(f);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String line;
		while((line=br.readLine())!=null) {
			String[] tokens = line.split(",");
			String cateId = tokens[0];
			String url = tokens[1];
			String sId = cateId.substring(0, cateId.length()-8);
			System.out.println("insert into tb_category_info values ('"+cateId+"','','"+url+"','"+sId+"',1);");
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
	
	private File[] restDir() {
		File[] files = new File[dirList.length];
		
		for (int i = 0; i < files.length; i++) {
			files[i] = new File(rootDir+"/"+dirList[i]);
		}
		return files;
	}
	// 2016072209 가 문제
	private int[] dirList = {2016052215,
	                         2016052312,
	                         2016052419,
	                         2016052510,
	                         2016062409,
	                         2016062412,
	                         2016062419,
	                         2016062519,
	                         2016062619,
	                         2016062715,
	                         2016062719,
	                         2016062909,
	                         2016070119,
	                         2016070219,
	                         2016070221,
	                         2016070309,
	                         2016070312,
	                         2016070315,
	                         2016070319,
	                         2016070415,
	                         2016070419,
	                         2016070815,
	                         2016070912,
	                         2016071021,
	                         2016071121,
	                         2016071209,
	                         2016071212,
	                         2016071312,
	                         2016071315,
	                         2016071321,
	                         2016071509,
	                         2016071519,
	                         2016071521,
	                         2016071612,
	                         2016071615,
	                         2016071815,
	                         2016072209,
	                         2016072419,
	                         2016072421,
	                         2016072509,
	                         2016080521,
	                         2016080609,
	                         2016080612,
	                         2016080621,
	                         2016081209,
	                         2016081212,
	                         2016081919,
	                         2016081921,
	                         2016082112,
	                         2016082115};
}
