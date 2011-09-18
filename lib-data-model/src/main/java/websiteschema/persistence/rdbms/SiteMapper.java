/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.List;
import websiteschema.model.domain.PageInfo;
import websiteschema.model.domain.Site;

/**
 *
 * @author ray
 */
public interface SiteMapper {

    public long getTotalResults();

    public List<Site> getSites(PageInfo pageInfo);

    public Site getSiteBySiteId(String siteId);

    public Site getSiteById(long id);

    public Site getSiteByName(String siteName);

    public void update(Site site);

    public void insert(Site site);

    public void delete(Site site);

    public void deleteBySiteId(String siteId);
}
