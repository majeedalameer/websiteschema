/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ray
 */
public class BasicAnalysisResult {

    Set<String> validNodes = new HashSet<String>();
    Set<String> invalidNodes = new HashSet<String>();
    Set<String> titlePrefix = new HashSet<String>();
    Set<String> titleSuffix = new HashSet<String>();

    /**
     * 所有的无效节点。
     * @return
     */
    public Set<String> getInvalidNodes() {
        return invalidNodes;
    }

    /**
     * 设置所有的无效节点。
     * @param invalidNodes
     */
    public void setInvalidNodes(Set<String> invalidNodes) {
        this.invalidNodes = invalidNodes;
    }

    /**
     * 返回所有的标题前缀
     * @return
     */
    public Set<String> getTitlePrefix() {
        return titlePrefix;
    }

    /**
     * 设置所有的标题前缀
     * @param titlePrefix
     */
    public void setTitlePrefix(Set<String> titlePrefix) {
        this.titlePrefix = titlePrefix;
    }

    /**
     * 返回所有的标题后缀
     * @return
     */
    public Set<String> getTitleSuffix() {
        return titleSuffix;
    }

    /**
     * 设置所有的标题前缀
     * @param titleSuffix
     */
    public void setTitleSuffix(Set<String> titleSuffix) {
        this.titleSuffix = titleSuffix;
    }

    /**
     * 返回所有的有效节点
     * @return
     */
    public Set<String> getValidNodes() {
        return validNodes;
    }

    /**
     * 设置所有的有效节点
     * @param validNodes
     */
    public void setValidNodes(Set<String> validNodes) {
        this.validNodes = validNodes;
    }
}
