/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import websiteschema.fb.annotation.*;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.model.domain.Websiteschema;
import websiteschema.persistence.hbase.WebsiteschemaMapper;

/**
 *
 * @author ray
 */
@EO(name = {"EO", "FAIL"})
@EI(name = {"EI:INIT"})
public class FBWebsiteschema extends FunctionBlock {

    @DI(name = "SITE")
    public String siteId = "";
    @DO(name = "OUT", relativeEvents = {"EO"})
    public Websiteschema out = null;

    @Algorithm(name = "INIT")
    public void create() {
        WebsiteschemaMapper mapper = getContext().getSpringBeanFactory().getBean("websiteschemaMapper", WebsiteschemaMapper.class);
//        WebsiteschemaMapper mapper = new WebsiteschemaMapper();
        out = mapper.get(siteId);
        if (null != out) {
            triggerEvent("EO");
        } else {
            triggerEvent("FAIL");
        }
    }
}
