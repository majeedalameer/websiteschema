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
     * 抽取数据，结果可能是一个集合
     * @param doc
     * @return
     */
    public Set<String> extract(Document doc);

}
