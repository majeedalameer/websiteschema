/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.List;
import java.util.Map;
import websiteschema.model.domain.Brand;

/**
 *
 * @author mupeng
 */
public interface BrandMapper {
   public long getTotalResults(Map map);

    public List<Brand> getBrands(Map map);

    public List<Brand> getAllBrands();

    public Brand getById(long id);
    
    public List<Brand> getByCid(long cid);

    public void insert(Brand brand);

    public void update(Brand brand);

    public void delete(Brand brand);

    public void deleteById(long id);  
}
