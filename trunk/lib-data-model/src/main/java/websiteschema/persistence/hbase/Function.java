/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.persistence.hbase;

/**
 *
 * @author ray
 */
public interface Function<T> {

    public void invoke(T arg);

}
