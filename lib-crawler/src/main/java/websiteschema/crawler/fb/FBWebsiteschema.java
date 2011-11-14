/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import websiteschema.crawler.SpringContext;
import websiteschema.fb.annotation.*;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.model.domain.Websiteschema;
import websiteschema.persistence.hbase.WebsiteschemaMapper;

/**
 *
 * @author ray
 */
@EO(name = {"SUCCESS", "FAILURE"})
@EI(name = {"INIT:INIT"})
public class FBWebsiteschema extends FunctionBlock {

    @DI(name = "IN")
    public String siteId = "";
    @DO(name = "OUT", relativeEvents = {"SUCCESS"})
    public Websiteschema out = null;

    @Algorithm(name = "INIT")
    public void create() {
        WebsiteschemaMapper mapper = SpringContext.getContext().getBean("websiteschemaMapper", WebsiteschemaMapper.class);
        out = mapper.get(siteId);
        if (null != out) {

            triggerEvent("SUCCESS");
        } else {
            triggerEvent("FAILURE");
        }
    }
}
