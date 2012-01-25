/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.util.ArrayList;
import java.util.Collection;
import websiteschema.cluster.analyzer.IFieldExtractor;
import websiteschema.cluster.analyzer.AnalysisResult;
import java.util.List;
import websiteschema.model.domain.Websiteschema;
import websiteschema.element.XPathAttributes;
import websiteschema.fb.annotation.DO;
import java.util.Map;
import org.w3c.dom.Document;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author ray
 */
@EO(name = {"EO", "FATAL"})
@EI(name = {"EI:EXT"})
public class FBDOMExtractor extends FunctionBlock {

    private Map<String, String> prop;
    private XPathAttributes xpathAttr;
    private AnalysisResult analysisResult = new AnalysisResult();
    @DI(name = "IN")
    public Document in;
    @DI(name = "SCHEMA")
    public Websiteschema schema;
    @DI(name = "CLS")
    public String clusterName = AnalysisResult.DefaultClusterName;
    @DO(name = "OUT", relativeEvents = {"EO"})
    public Doc out;

    @Algorithm(name = "EXT")
    public void extract() {
        try {
            prop = schema.getProperties();
            xpathAttr = schema.getXpathAttr();
            analysisResult.init(prop);
            //初始化Document out
            createDocument();
            //抽取其他标签
            extractFields(in, out, analysisResult.getFieldAnalyzers(), analysisResult.getFieldExtractors());
            this.triggerEvent("EO");
        } catch (Exception ex) {
            ex.printStackTrace();
            l.error(ex);
            this.triggerEvent("FATAL");
        }
    }

    /**
     * 根据配置抽取每一个字段
     * @param in
     * @param fieldAnalyzerNames
     */
    private void extractFields(Document in, Doc out, Map<String, String> fieldAnalyzerNames, Map<String, String> fieldExtractorNames) {
        List<IFieldExtractor> list = new ArrayList<IFieldExtractor>();
        for (String fieldName : fieldAnalyzerNames.keySet()) {
            String clazzName = fieldAnalyzerNames.get(fieldName);
            IFieldExtractor extractor = createFieldExtractor(fieldName, clazzName);//创建字段抽取器
            if (null != extractor) {
                list.add(extractor);
            }
        }
        for (String fieldName : fieldExtractorNames.keySet()) {
            String clazzName = fieldExtractorNames.get(fieldName);
            IFieldExtractor extractor = createFieldExtractor(fieldName, clazzName);//创建字段抽取器
            if (null != extractor) {
                list.add(extractor);
            }
        }
        extractFields(in, out, list);
    }

    private void extractFields(Document in, Doc doc, List<IFieldExtractor> fields) {
        for (IFieldExtractor extractor : fields) {
            //创建字段抽取器
            extractor.setXPathAttr(xpathAttr);
            extractor.setBasicAnalysisResult(analysisResult.getBasicAnalysisResult());
            try {
                //读取字段抽取器的配置，这是一个List<Map>的配置
                List<Map<String, String>> listConfig = analysisResult.getListByField(clusterName, extractor.getFieldName());
                if (null != listConfig) {
                    for (Map<String, String> config : listConfig) {
                        List<String> properClusters = analysisResult.getProperCluster(config);
                        //对每一个配置都尝试抽取，如果配置和类的名称相符
                        if (properClusters.contains(clusterName)) {
                            extractor.init(config);
                            //开始抽取
                            Collection<String> result = extractor.extract(in);
                            if (null != result && !result.isEmpty()) {
                                //添加抽取结果到doc中
                                for (String res : result) {
                                    doc.addField(extractor.getFieldName(), res);
                                }
                                //如果抽取到结果，就结束抽取
                                break;
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private IFieldExtractor createFieldExtractor(String fieldName, String clazzName) {
        try {
            Class clazz = Class.forName(clazzName);
            IFieldExtractor extractor = (IFieldExtractor) clazz.newInstance();
            extractor.setFieldName(fieldName);
            extractor.setXPathAttr(xpathAttr);
            extractor.setBasicAnalysisResult(analysisResult.getBasicAnalysisResult());
            return extractor;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Doc createDocument() {
        out = new Doc();
        return out;
    }
}
