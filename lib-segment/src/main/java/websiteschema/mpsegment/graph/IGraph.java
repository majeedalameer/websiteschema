package websiteschema.mpsegment.graph;

public interface IGraph {

    public int getVerticesNumber();

    public void addVertex();

    public void addEdgeObject(int head, int tail, int weight, Object obj);

    public int getEdgeWeight(int head, int tail);

    public Object getEdgeObject(int head, int tail);

    public int[] getAdjacentVertices(int vertex);

    public int getStopVertex(int i, int j);

    public void clear();
}
