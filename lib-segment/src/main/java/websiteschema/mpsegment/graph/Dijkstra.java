package websiteschema.mpsegment.graph;

import java.util.*;

public class Dijkstra {

    public class PriorityQueue {

        class QueueElement implements Comparable {

            public QueueElement(Dijkstra.PriorityQueue priorityqueue, int i) {
                id = i;
                distance = 0;  //starting vertex
            }

            public QueueElement(Dijkstra.PriorityQueue priorityqueue, int i, int j) {
                id = i;
                distance = j;
            }

            @Override
            public int compareTo(Object obj) {
                QueueElement b1 = (QueueElement) obj;
                int i = b1.distance;
                if (distance == i) {
                    return id != b1.id ? -1 : 0;
                }
                return distance >= i ? 1 : -1;
            }
            public int id;
            public int distance;
        }

        public void clear() {
            treeSet.clear();
        }

        public boolean isEmpty() {
            return treeSet.isEmpty();
        }

        public int getSize() {
            return treeSet.size();
        }

        public void insert(int qe, int distance) {
            if (qe == 0) {
                throw new IllegalArgumentException("element must be not null");
            }
            if (distance < 0) {
                throw new IllegalArgumentException((new StringBuilder()).append("Illegal distance: ").append(distance).toString());
            } else {
                QueueElement element = new QueueElement(this, qe, distance);
                treeSet.add(element);
                return;
            }
        }

        public int dequeueLowestPriorityElement() {
            if (!isEmpty()) {
                QueueElement element = (QueueElement) treeSet.first();
                int i = element.id;
                treeSet.remove(element);
                return i;
            } else {
                return -1;
            }
        }
        private TreeSet<QueueElement> treeSet;

        public PriorityQueue() {
            treeSet = new TreeSet<QueueElement>();
        }
    }

    public class ArraySet {

        public void clear() {
            numElement = 0;
            Arrays.fill(data, -1);
        }

        public void add(int i) {
            data[i] = numElement;
            numElement++;
        }

        public boolean contains(int i) {
            return data[i] > 0;
        }
        private int data[];
        private int numElement;
        private int size;

        public ArraySet() {
            numElement = 0;
            size = 1;
            data = new int[size];
        }

        public ArraySet(int i) {
            numElement = 0;
            size = 1;
            size = i + 1;
            data = new int[size];
        }
    }

    public Dijkstra(int i) {
        list = new ArrayList();
        maxGraphSize = i;
        resolvedVertexSet = new ArraySet(maxGraphSize);
        reachedVertexQueue = new PriorityQueue();
        verticesToDistanceMap = new ArrayMap(maxGraphSize);
        backTracelMap = new ArrayMap(maxGraphSize);
    }

    public void setGraph(IGraph iigraph) {
        graph = iigraph;
    }

    /**
     * startVertex = 1; endVertex = sentence.length + 1;
     * @param startVertex
     * @param endVertex
     */
    private void traversalAllPath(int startVertex, int endVertex) {
        initialize(startVertex);
        do {
            if (reachedVertexQueue.isEmpty()) {
                break;
            }
            int currentVertex = reachedVertexQueue.dequeueLowestPriorityElement();//第一次循环：k = i;
            if (currentVertex == endVertex) {
                break;
            }
            //第一次循环：arraySet[1] = 0;
            resolvedVertexSet.add(currentVertex);
            traversalAdjacentVertices(currentVertex);
        } while (true);
    }

    private void initialize(int startVertex) {
        verticesToDistanceMap.clear();
        backTracelMap.clear();
        resolvedVertexSet.clear();
        reachedVertexQueue.clear();
        verticesToDistanceMap.put(startVertex, 0);
        reachedVertexQueue.insert(startVertex, 0);
    }

    private void traversalAdjacentVertices(int currentVertex) {
        int adjacentVertices[] = graph.getAdjacentVertices(currentVertex);
        int i = 0;
        do {
            if (i >= adjacentVertices.length) {
                break;
            }
            int adjacentVertex = adjacentVertices[i];
            i++;
            if (!resolvedVertexSet.contains(adjacentVertex)) {
                int newDistance = getKnownDistanceOfVertex(currentVertex) + graph.getEdgeWeight(currentVertex, adjacentVertex);
                if (getKnownDistanceOfVertex(adjacentVertex) > newDistance) {
                    setMinimumDistance(adjacentVertex, newDistance);
                    backTracelMap.put(adjacentVertex, currentVertex);
                    reachedVertexQueue.insert(adjacentVertex, newDistance);
                }
            }
        } while (true);
    }

    private int getKnownDistanceOfVertex(int i) {
        if (verticesToDistanceMap.containsKey(i)) {
            return verticesToDistanceMap.get(i);
        } else {
            return infinity;
        }
    }

    private void setMinimumDistance(int i, int j) {
        verticesToDistanceMap.put(i, j);
    }

    /**
     * i = 1; j = sentence.length + 1;
     * @param i
     * @param j
     * @return
     */
    public Path getShortestPath(int start, int end) {
        checkIllegalParams(start);
        checkIllegalParams(end);
        traversalAllPath(start, end);
        if (start != end) {
            return backTraceShortestPath(start, end);
        } else {
            return null;
        }
    }

    private Path backTraceShortestPath(int i, int j) {
        if (getKnownDistanceOfVertex(j) != infinity) {
            list.clear();
            int k = j;
            do {
                list.add(new Integer(k));
                k = backTracelMap.get(k);
            } while (k > 0 && k != i);
            list.add(new Integer(i));
            Collections.reverse(list);
            return new Path(list);
        } else {
            Path path = new Path();
            return path;
        }
    }

    private void checkIllegalParams(int vertex) {
        if (vertex < 0 || vertex > maxGraphSize) {
            throw new IllegalArgumentException((new StringBuilder()).append("The  vertex ! ").append(vertex).append(" does not exist in the graph.").toString());
        } else {
            return;
        }
    }
    private static final int infinity = Integer.MAX_VALUE;
    private IGraph graph;
    private int maxGraphSize;
    private ArraySet resolvedVertexSet;
    private PriorityQueue reachedVertexQueue;
    private ArrayMap verticesToDistanceMap;
    private ArrayMap backTracelMap;
    private ArrayList list;
}
