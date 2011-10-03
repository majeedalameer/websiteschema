/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain.cluster;

import java.util.List;

/**
 *
 * @author ray
 */
public class DocVector {

    Dimension[] dims;
    String name;

    public void append(List<Dimension> array) {
        int pos = this.dims.length;
        Dimension[] tmp = new Dimension[pos + array.size()];
        System.arraycopy(this.dims, 0, tmp, 0, pos);
        for (int i = 0; i < array.size(); i++) {
            tmp[pos + i] = array.get(i);
        }
        this.dims = tmp;
    }

    public Dimension[] getDims() {
        return dims;
    }

    public void setDims(Dimension[] dims) {
        this.dims = dims;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
