/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.w3c.dom.Document;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.element.DocumentUtil;
import websiteschema.fb.annotation.Description;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.model.domain.UrlLink;
import websiteschema.persistence.hbase.UrlLinkMapper;
import websiteschema.utils.UrlLinkUtil;

/**
 *
 * @author ray
 */
@EI(name = {"ADD:ADD", "SAVE:SAVE"})
@EO(name = {"ADD", "SAVE", "FATAL"})
@Description(desc = "保存链接和抽取的结果")
public class FBURLStorage extends FunctionBlock {

    @DI(name = "DOC", desc = "抽取后的内容")
    public Doc in;
    @DI(name = "URL", desc = "抽取内容的目标链接")
    public String url;
    @DI(name = "STATUS", desc = "采集时的HTTP Status")
    public int status;
    @DO(name = "DOC", relativeEvents = "SAVE")
    public Document out;
    @DI(name = "PARENT", desc = "翻页时的父URL")
    public String parent;
    @DI(name = "LINKS", desc = "需要保存的链接列表")
    public List<String> links;
    @DI(name = "JOBNAME", desc = "起始URL的jobname")
    public String jobname;
    @DI(name = "DEPTH", desc = "URL深度")
    public int depth;
    @DO(name = "ADDED", relativeEvents = "ADD")
    public List<String> added = new ArrayList<String>();

    @Algorithm(name = "ADD", desc = "将添加链接保存至HBase存储")
    public void add() {
        try {
            added.clear();
            UrlLinkMapper mapper = getContext().getSpringBean("urlLinkMapper", UrlLinkMapper.class);
            for (String u : links) {
                URL link = new URL(u);
                String rowKey = UrlLinkUtil.getInstance().convertUrlToRowKey(link);
                if (!mapper.exists(rowKey)) {
                    UrlLink newUrlLink = new UrlLink();
                    newUrlLink.setRowKey(rowKey);
                    newUrlLink.setUrl(u);
                    newUrlLink.setDepth(depth);
                    newUrlLink.setJobname(jobname);
                    newUrlLink.setParent(parent);
                    newUrlLink.setHttpStatus(0);
                    newUrlLink.setCreateTime(new Date());
                    mapper.put(newUrlLink);
                    added.add(u);
                } else {
                    UrlLink old = mapper.get(rowKey);
                    int d = old.getDepth();
                    if (depth < d) {
                        old.setDepth(depth);
                        mapper.put(old);
                    }
                }
            }
            l.info("saved " + added.size() + " links.");
            triggerEvent("ADD");
        } catch (Exception ex) {
            triggerEvent("FATAL");
        }
    }

    @Algorithm(name = "SAVE", desc = "将采集到的内容保存至HBase存储")
    public void save() {
        try {
            if (null != in && null != url) {
                UrlLinkMapper mapper = getContext().getSpringBean("urlLinkMapper", UrlLinkMapper.class);
                URL link = new URL(url);
                String rowKey = UrlLinkUtil.getInstance().convertUrlToRowKey(link);
                if (mapper.exists(rowKey)) {
                    UrlLink old = mapper.get(rowKey);
                    old.setStatus(status);
                    old.setContent(DocumentUtil.getXMLString(in.toW3CDocument()));
                    mapper.put(old);
                } else {
                    UrlLink newUrlLink = new UrlLink();
                    newUrlLink.setRowKey(rowKey);
                    newUrlLink.setUrl(url);
                    newUrlLink.setContent(DocumentUtil.getXMLString(in.toW3CDocument()));
                    newUrlLink.setCreateTime(new Date());
                    newUrlLink.setDepth(1000);
                    mapper.put(newUrlLink);
                }
            }
            out = in.toW3CDocument();
            triggerEvent("SAVE");
        } catch (Exception ex) {
            triggerEvent("FATAL");
        }
    }
}
