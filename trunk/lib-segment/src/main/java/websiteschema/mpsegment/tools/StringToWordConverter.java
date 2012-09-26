package websiteschema.mpsegment.tools;

import websiteschema.mpsegment.dict.IWord;
import websiteschema.mpsegment.dict.POS;
import websiteschema.mpsegment.dict.POSArray;
import websiteschema.mpsegment.dict.WordImpl;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringToWordConverter {

    private static Pattern patternWord = Pattern.compile("\"(.*)\"\\s*=\\s*(\\{.*\\})");
    private static Pattern patternPair = Pattern.compile("([\\w\\d]+)\\s*:\\s*((\\{[^\\}]+?\\})|([\\w\\d]+))\\s*(,|$)");
    WordImpl word;
    private static String domainTypeKey = "domainType";
    private static String posTableKey = "POSTable";

    public IWord convert(String str) {
        Matcher m = patternWord.matcher(str);
        if (m.matches()) {
            String wordName = m.group(1);
            Map<String, String> properties = convertToMap(m.group(2));
            try {
                wordName = java.net.URLDecoder.decode(wordName, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            word = new WordImpl(wordName);
            setDomainType(properties.get(domainTypeKey));
            setPOSTable(properties.get(posTableKey));
            return word;
        }
        return null;
    }

    private void setPOSTable(String posTableString) {
        if(null != posTableString) {
            Map<String,String> posTable = convertToMap(posTableString);
            POSArray posArray = new POSArray();
            for(String pos : posTable.keySet()) {
                posArray.add(new POS(pos, Integer.parseInt(posTable.get(pos))));
            }
            word.setPosArray(posArray);
        }
    }

    private void setDomainType(String domainType) {
        if(null != domainType) {
            word.setDomainType(Integer.parseInt(domainType));
        }
    }

    private Map<String, String> convertToMap(String str) {
        if(str.matches("\\{.*\\}")) {
            String content = str.substring(1, str.length() - 1);
            Matcher m = patternPair.matcher(content);
            Map<String, String> map = new LinkedHashMap<String, String>();
            int start = 0;
            while(m.find(start)) {
                String pair = m.group();
                map.put(m.group(1), m.group(2));
                start += pair.length();
            }
            return map;
        }
        return null;
    }

}
