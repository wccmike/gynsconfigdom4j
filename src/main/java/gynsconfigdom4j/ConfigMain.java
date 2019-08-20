package gynsconfigdom4j;

import java.io.File;

public class ConfigMain {

	public static void main(String[] args) throws Exception {
		//way1 large range
//		String dir = "D:/1/11ni/pe-gyns-parent";
//		String dir = "D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-wxbank";
//		ConvertUtil util = new ConvertUtil();
//		util.dealDir(dir);
		
		//way2 small range
		String dir2 = "D:/1/11ni/pe-gyns-parent/pe-gyns-router/pe-gyns-router-direct";
//		String dir2 = "D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-pmobile/pe-gyns-fsg-pmobile/pe-gyns-fsg-mbs-pweb-pquery";
		ConvertUtil util2 = new ConvertUtil();
		util2.innerRecurse(new File(dir2));
		
		System.out.println("app finished without error");
	}

}
