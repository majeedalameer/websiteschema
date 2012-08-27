package websiteschema.mpsegment.graph;

import java.util.Arrays;
import websiteschema.mpsegment.dict.IWord;

public class SingleMatrixGraph implements IGraph {

    private final int maxEdgesPerVertex = 3;

    public SingleMatrixGraph(int vertexNumber) {
        tmpTails = new int[maxEdgesPerVertex];
        passedRvn = new int[256];
        if (vertexNumber < 1) {
            throw new IllegalArgumentException("Can't create graph. The verticesNumber must be > 0");
        } else {
            maxVertices = vertexNumber;
            vertices = new int[vertexNumber][maxEdgesPerVertex];
            edges = new int[vertexNumber * (maxEdgesPerVertex - 1)][3];
            edgeTop = 1;
            objects = new Object[vertexNumber];
            wordSet = new ObjectArraySet(vertexNumber * (maxEdgesPerVertex - 1));
            existingVertex = 0;
        }
    }

    @Override
    public void clear() {
        int k = existingVertex + 1;
        if (k > maxVertices) {
            k = maxVertices;
        }
        edgeTop = 1;
        existingVertex = 0;
        for (int l = 0; l < k; l++) {
            objects[l] = null;
            for (int j1 = 0; j1 < maxEdgesPerVertex; j1++) {
                vertices[l][j1] = 0;
            }
        }

        for (int i1 = 0; i1 < k * (maxEdgesPerVertex - 1); i1++) {
            edges[i1][0] = 0;
            edges[i1][1] = 0;
        }

        wordSet.clear();
    }

    public int getVerticesNumber() {
        return maxVertices;
    }

    @Override
    public void addVertex() {
        if (existingVertex >= maxVertices) {
            System.out.println("[SingleMatrixGraph] Can't add the vertex because all the vertices have been alreay added!");
        } else {
            existingVertex++;
        }
    }

    @Override
    public void addEdge(int head, int tail, int weight, IWord obj) {
        int edgeOfVertex = 0;
        if (weight <= 0) {
            throw new IllegalArgumentException((new StringBuilder()).append("Invalid weight ! ").append(weight).append(". Must be > 0").toString());
        }
        if (weight == 0x7fffffff) {
            throw new IllegalArgumentException((new StringBuilder()).append("Invalid weight ! ").append(weight).append(". Must be < ").append(0x7fffffff).toString());
        }
        //检查节点的边的数量是否超过了限制
        while (edgeOfVertex < maxEdgesPerVertex && vertices[head][edgeOfVertex] != 0) {
            edgeOfVertex++;
        }

        if (edgeOfVertex < maxEdgesPerVertex && vertices[head][edgeOfVertex] == 0) {
            vertices[head][edgeOfVertex] = edgeTop;
            edges[edgeTop][0] = tail;
            edges[edgeTop][1] = weight;
            edges[edgeTop][2] = wordSet.top;
            wordSet.add(obj);
            edgeTop++;
            checkArrayBound();
        }
    }

    private void checkArrayBound() {
        if (edgeTop >= edges.length) {
            int edgesNew[][] = new int[edges.length + 1024][3];
            for (int i = 0; i < edges.length; i++) {
                System.arraycopy(edges[i], 0, edgesNew[i], 0, edges[i].length);
            }

            edges = null;
            edges = edgesNew;
        }
    }

    @Override
    public int getEdgeWeight(int head, int tail) {
        int edgeOfVertex = 0;
        int weight = 0;
        while (edgeOfVertex < maxEdgesPerVertex && vertices[head][edgeOfVertex] != 0 && edges[vertices[head][edgeOfVertex]][0] != tail) {
            edgeOfVertex++;
        }
        if (edgeOfVertex < maxEdgesPerVertex && vertices[head][edgeOfVertex] != 0 && edges[vertices[head][edgeOfVertex]][0] == tail) {
            weight = edges[vertices[head][edgeOfVertex]][1];
        }
        return weight;
    }

    @Override
    public IWord getEdgeObject(int head, int tail) {
        int edgeOfVertex = 0;
        int wordIndex = 0;
        while (edgeOfVertex < maxEdgesPerVertex && vertices[head][edgeOfVertex] != 0 && edges[vertices[head][edgeOfVertex]][0] != tail) {
            edgeOfVertex++;
        }
        if (edgeOfVertex < maxEdgesPerVertex && vertices[head][edgeOfVertex] != 0 && edges[vertices[head][edgeOfVertex]][0] == tail) {
            wordIndex = edges[vertices[head][edgeOfVertex]][2];
        }
        return wordSet.get(wordIndex);
    }

    @Override
    public int[] getAdjacentVertices(int head) {
        int l = 0;
        for (int i1 = 0; i1 < maxEdgesPerVertex; i1++) {
            if (vertices[head][i1] != 0) {
                tmpTails[l] = edges[vertices[head][i1]][0];
                l++;
            }
        }

        if (l < maxEdgesPerVertex) {
            int ai[] = new int[l];
            System.arraycopy(tmpTails, 0, ai, 0, l);
            return ai;
        } else {
            return tmpTails;
        }
    }

    public int getStopVertex2(int start, int end) {
        int gap = (end - start) + 1;
        Arrays.fill(passedRvn, 0);
        if (gap > passedRvn.length) {
            start = end - passedRvn.length;
            if (start < 0) {
                start = 0;
            }
        }
        gap = (end - start) + 1;
        for (int vn = start; vn <= end; vn++) {
            int maxTail = 0;
            for (int edgeNUmber = 0; edgeNUmber < maxEdgesPerVertex; edgeNUmber++) {
                if (vertices[vn][edgeNUmber] == 0) {
                    continue;  // no edge, continue to find next edge
                }
                int tail = edges[vertices[vn][edgeNUmber]][0];
                if (tail > maxTail) {
                    maxTail = tail;
                }
            }

            if (maxTail <= vn + 1) {
                continue;  //this edge's tail does not stretch beyond vn, so discard
            }

            for (int distance = 1; distance < maxTail - vn; distance++) {
                if ((vn - start) + distance < passedRvn.length) {  //ensure no outofbound of passedRvn[]
                    passedRvn[(vn - start) + distance] = 1;
                }
            }
        }

        int delta = end - start;
        while (delta > 0 && passedRvn[delta] >= 1) {
            delta--;
        }
        return delta + start;
    }

    @Override
    public int getStopVertex(int start, int end) {
        int stopVertex = start;
        int gap = (end - start) + 1;
        if (start <= 0 || end <= 0) {
            return -1;
        }
        int maxDistance[] = new int[gap];  // all Zeros at initialization
        for (int rvn = 0; rvn < gap; rvn++) {  //rvn: relative vertex number
            int adjacentVertices[] = getAdjacentVertices(start + rvn);

            //find out the Length of the longest edges of one vertex, and calculate its distance to start vertex, and fill it in maxDistance[]
            for (int adjacent = 0; adjacent < adjacentVertices.length; adjacent++) {
                if (adjacentVertices[adjacent] - start > maxDistance[rvn]) {
                    maxDistance[rvn] = adjacentVertices[adjacent] - start;
                }
            }
        }

        for (int rvn = gap - 1; rvn > 0;) {
            int l1 = rvn;
            for (int k2 = rvn - 1; k2 >= 0; k2--) {
                if (maxDistance[k2] > rvn) {  //vertex's edge before rvn  reach beyond rvn, so rvn is not a stopvertex.
                    l1 = -1;
                    rvn = k2;
                    break;
                }
            }

            if (l1 > 0) {
                stopVertex = l1 + start;
                break;

            }
        }

        return stopVertex;


    }
    private int maxVertices;
    private Object objects[];
    private ObjectArraySet wordSet;
    private int existingVertex;
    private int edgeTop;
    private int vertices[][];
    private int edges[][];
    int tmpTails[];
    int passedRvn[];
}
