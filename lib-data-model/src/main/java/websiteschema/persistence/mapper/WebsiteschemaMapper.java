/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.persistence.mapper;

import java.util.List;
import org.springframework.stereotype.Service;
import websiteschema.common.base.Function;
import websiteschema.model.domain.Websiteschema;
import websiteschema.persistence.Mapper;

/**
 *
 * @author ray
 */
@Service
public class WebsiteschemaMapper implements Mapper<Websiteschema>{

    @Override
    public boolean exists(String rowKey) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Websiteschema get(String rowKey) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Websiteschema> getList(String start, String end) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Websiteschema> getList(String start, String end, String family) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Websiteschema> getList(String start, String end, String family, int maxResults) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void put(Websiteschema obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void put(List<Websiteschema> lst) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(String rowKey) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void scan(String start, String end, Function<Websiteschema> func) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void batchScan(String start, String end, int batchSize, Function<List<Websiteschema>> func) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
