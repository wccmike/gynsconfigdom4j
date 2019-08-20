package directorynametraverseget;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;

public class Main {

	/**
	 * 读指定文件夹的所有工程名，输出一个excel可读文件
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		/* 要处理哪个文件夹 */
		String dir = "D:/workpla/ee/24thMay";
		/* 生成在哪,绝对路径加文件名.csv */
		String outpath = "D:/workpla/ee/24thMay/1.csv";
		List<String> result = new LinkedList<String>();
		File parent = new File(dir);
		File[] fs = parent.listFiles();
		String[] exclude = { ".metadata", ".recommenders", "RemoteSystemsTempFiles" };
		for (File f : fs) {
			if (f.isDirectory()) {
				String fname = f.getName();
				boolean isExclude = false;
				for (int i = 0; i < exclude.length; i++) {
					if (exclude[i].equalsIgnoreCase(fname)) {
						isExclude = true;
						break;
					}
				}
				if (!isExclude)
					result.add(fname);
			}

		}
		File newcsv = new File(outpath);
		newcsv.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(newcsv));
		StringBuffer sb = new StringBuffer();
		for (String s : result) {
			sb.append(s + "\r\n");
		}
		bw.write(sb.toString());
		bw.flush();
		bw.close();
	}

}
