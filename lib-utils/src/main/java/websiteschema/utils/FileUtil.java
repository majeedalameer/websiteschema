/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author ray
 */
public class FileUtil {

    public static String read(String file) {
        File f = new File(file);
        if (f.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
                return readInputStream(fis, "utf-8");
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (null != fis) {
                    try {
                        fis.close();
                    } catch (Exception ex) {
                    }
                }

            }
        }
        return null;
    }

    public static String readResource(String resource) {
        InputStream is = FileUtil.class.getClassLoader().getResourceAsStream(resource);
        if (null != is) {
            try {
                return readInputStream(is, "utf-8");
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (null != is) {
                    try {
                        is.close();
                    } catch (Exception ex) {
                    }
                }

            }
        }
        return null;
    }

    public static String readInputStream(InputStream is, String encoding) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, encoding));
            String line = br.readLine();
            while(null != line) {
                sb.append(line).append("\n");
                line = br.readLine();
            }
        } catch (Exception ex) {
        }
        return sb.toString();
    }
}
