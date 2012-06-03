/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase;

import org.springframework.stereotype.Service;
import websiteschema.model.domain.Commodity;
import websiteschema.persistence.hbase.core.HBaseMapper;


/**
 *
 * @author mupeng
 */
@Service
public class CommodityMapper extends HBaseMapper<Commodity>{
    public CommodityMapper() {
        super(Commodity.class);
    }
}
