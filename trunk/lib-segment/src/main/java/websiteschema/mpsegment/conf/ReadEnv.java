package websiteschema.mpsegment.conf;

import java.io.*;
import java.util.Properties;

public class ReadEnv {

    public ReadEnv() {
    }

    public static Properties getEnvVars()
            throws Throwable {
        Process process = null;
        Properties properties = new Properties();
        Runtime runtime = Runtime.getRuntime();
        String s = System.getProperty("os.name").toLowerCase();
        System.out.println((new StringBuilder()).append("[System] ").append(s).toString());


        System.out.println(System.getProperty("zhida_home"));

        if (s.indexOf("windows 9") > -1) {
            process = runtime.exec("command.com /c set");
        } else if (s.indexOf("nt") > -1 || s.indexOf("windows") > -1) {
            process = runtime.exec("cmd.exe /c set");
        } else {
            process = runtime.exec("env");
        }
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String s1;
        while ((s1 = bufferedreader.readLine()) != null) {
            int i = s1.indexOf('=');
            if (i >= 0) {
                String s2 = s1.substring(0, i);
                String s3 = s1.substring(i + 1);
                properties.setProperty(s2, s3);
            }
        }
        process = null;
        return properties;
    }

    public String getCnnlpHome() {
        String s = null;
        Properties properties = new Properties();
        InputStream inputstream = null;
        try {
            inputstream = getClass().getResourceAsStream("/cnnlp_init.properties");
            if (inputstream != null) {
                properties.load(inputstream);
            }
        } catch (Exception exception) {
            System.err.println("Error reading Cnnlp properties in CnnlpGlobals");
            exception.printStackTrace();
        } finally {
            try {
                if (inputstream != null) {
                    inputstream.close();
                }
            } catch (Exception exception2) {
            }
        }
        if (properties != null) {
            s = properties.getProperty("cnnlpHome");
            if (s != null) {
                for (s = s.trim(); s.endsWith("/") || s.endsWith("\\"); s = s.substring(0, s.length() - 1));
            }
        }
        return s;
    }
}
