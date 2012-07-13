package websiteschema.mpsegment.conf;

import java.io.IOException;
import websiteschema.mpsegment.util.JarResources;

public class ReadDataFile {

    public byte[] getData(String s) throws IOException {
        String dictFile = Configure.getInstance().getDictFile();
        JarResources jarResources = new JarResources(dictFile);  //giantseg.dat
        byte abyte0[] = jarResources.getResource(s);
        jarResources = null;
        return abyte0;
    }
}
