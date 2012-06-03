/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.dwr.response.ListRange;
import websiteschema.model.domain.Brand;
import websiteschema.persistence.rdbms.BrandMapper;
import static websiteschema.persistence.rdbms.utils.ParameterUtil.*;

/**
 *
 * @author mupeng
 */
@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(brandMapper.getBrands(params).toArray());
        listRange.setTotalSize(brandMapper.getTotalResults(params));
        return listRange;
    }

    public Brand getById(long id) {
        return brandMapper.getById(id);
    }
    
    public ListRange getByCid(Map map){
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(brandMapper.getBrands(params).toArray());
        listRange.setTotalSize(brandMapper.getTotalResults(params));
        return listRange;
    }

    @Transactional
    public void insert(Brand cate) {
        cate.setCreateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cate.setCreateUser(userDetails.getUsername());
        cate.setCreateTime(new Date());

        brandMapper.insert(cate);
    }

    @Transactional
    public void update(Brand cate) {
        cate.setUpdateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cate.setLastUpdateUser(userDetails.getUsername());
        cate.setUpdateTime(new Date());
        brandMapper.update(cate);
    }

    @Transactional
    public void deleteRecord(Brand cate) {
        brandMapper.delete(cate);
    }

    @Transactional
    public void deleteById(long id) {
        if (id > 0) {
            brandMapper.deleteById(id);
        }
    }
}
