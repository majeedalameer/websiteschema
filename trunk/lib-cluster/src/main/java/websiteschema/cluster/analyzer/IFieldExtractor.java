/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.util.Set;
import org.w3c.dom.Document;
import websiteschema.common.base.JObject;

/**
 *
 * @author ray
 */
public interface IFieldExtractor extends JObject {

    /**
     * 指明分析的字段类型，例如分析时间字段就返回PUBLISHDATE。
     * @return
     */
    public String getFieldName();

    public void setFieldName(String fieldName);

    /**
     * 抽取数据，结果可能是一个集合
     * @param doc
     * @return
     */
    public Set<String> extract(Document doc);
}
