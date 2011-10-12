package websiteschema.utils;

import java.io.*;

public class Escape {
	public static void main(String args[]){
		System.out.println(Escape.escape("股权转让慢半拍","utf-8"));
//		System.out.println("Startup...");
//		try {
//			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("title.dat"),"UTF-8"));
//			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("title.escaped.txt")));
//			try {
//				String line = br.readLine();
//				while(line!=null){
//					String l = escape(line,"UTF-8");
////					System.out.println(l);
//					bw.write(l);
//					bw.write("\r\n");
//					line = br.readLine();
//				}
//				
//				bw.close();
//				br.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("Exiting...");
	}
	
	public static String escape (String src,String encoding) {
		if(!(src instanceof String)){
			return null;
		}
		
		byte[] b = null;
		if(encoding != null && !encoding.equals("")){
			try {
				b = src.getBytes(encoding);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			b = src.getBytes();
		}
		StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length()*9);
		for(int i = 0; i < b.length; i++){
			if(Character.isDigit(b[i]) || Character.isLowerCase(b[i]) || Character.isUpperCase(b[i]))
				tmp.append((char)b[i]);
			else
				tmp.append("%"+getHexString(b[i]));
        }
        return tmp.toString();
    }
	
	private static String getHexString(byte b) {   
        String hexStr = Integer.toHexString(b).toUpperCase();   
        int m = hexStr.length();   
        if (m < 2) {   
            hexStr = "0" + hexStr;   
        } else {   
            hexStr = hexStr.substring(m - 2);   
        }
        return hexStr;
    }
}
