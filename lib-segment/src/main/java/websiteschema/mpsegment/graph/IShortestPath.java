/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.graph;

/**
 *
 * @author twer
 */
public interface IShortestPath {

    /**
     * i = 1; j = sentence.length + 1;
     * @param i
     * @param j
     * @return
     */
    Path getShortestPath(int start, int end);

    void setGraph(IGraph graph);
    
}
