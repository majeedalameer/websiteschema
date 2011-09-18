/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.model.domain;

/**
 *
 * @author mupeng
 */
public class PageInfo implements java.io.Serializable{
    private Integer pageSize;
    private Integer pageNum;

    public PageInfo(Integer start,Integer pageSize){
        this.pageNum = start;
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
}
