/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.url;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ray
 */
public class URLClusterer {

    public List<URLCluster> clustering(List<URI> setURLs) {
        List<URLCluster> ret = init(setURLs);



        return null;
    }

    private List<URLCluster> init(List<URI> setURLs) {
        List<URLCluster> ret = new ArrayList<URLCluster>();
        for (URI uri : setURLs) {
            URLCluster c = new URLCluster();
            c.append(new URLObj(uri));
            ret.add(c);
        }
        return ret;
    }

    private double membership(URLCluster c1, URLCluster c2) {
        double ret = 0.0D;


        return ret;
    }
}
