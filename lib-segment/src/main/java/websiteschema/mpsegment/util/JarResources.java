package websiteschema.mpsegment.util;

import java.io.*;
import java.util.Hashtable;
import java.util.zip.*;

public final class JarResources {

    public JarResources(String resource) throws FileNotFoundException {
        debugOn = true;
        zipEntries_content = new Hashtable();
        dictFile = resource;
        File file = new File(dictFile);
        if (file.exists()) {
            loadZipEntries(new FileInputStream(file));
        } else {
            loadZipEntries(this.getClass().getClassLoader().getResourceAsStream(dictFile));
        }
    }

    public byte[] getResource(String s) {
        return (byte[]) zipEntries_content.get(s);
    }

    private void loadZipEntries(InputStream is) {
        try {

            BufferedInputStream bufferedinputstream = new BufferedInputStream(is);
            ZipInputStream zipinputstream = new ZipInputStream(bufferedinputstream);
            for (ZipEntry zipentry1 = null; (zipentry1 = zipinputstream.getNextEntry()) != null;) {
                if (!zipentry1.isDirectory()) {
                    int i = (int) zipentry1.getSize();
                    if (i == -1) {
                        throw new NullPointerException();
                    }
                    byte abyte0[] = new byte[i];
                    int j = 0;
                    boolean flag = false;
                    int k;
                    for (; i - j > 0; j += k) {
                        k = zipinputstream.read(abyte0, j, i - j);
                        if (k == -1) {
                            break;
                        }
                    }

                    zipEntries_content.put(zipentry1.getName(), abyte0);
                }
            }

        } catch (NullPointerException nullpointerexception) {
            System.out.println("[System] something wrong about dict.");
        } catch (FileNotFoundException filenotfoundexception) {
            System.out.println((new StringBuilder()).append("[System] dictionary file is not found !").toString());
        } catch (IOException ioexception) {
            System.out.println((new StringBuilder()).append("[System] failed to read dictionary file !").toString());
        }
    }
    public boolean debugOn;
    private Hashtable zipEntries_content;
    private String dictFile;
}
