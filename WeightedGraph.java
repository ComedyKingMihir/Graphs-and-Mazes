package graph;

import java.util.*;

/**
 * <P>
 * This class represents a general "directed graph", which could be used for any
 * purpose. The graph is viewed as a collection of vertices, which are sometimes
 * connected by weighted, directed edges.
 * </P>
 * 
 * <P>
 * This graph will never store duplicate vertices.
 * </P>
 * 
 * <P>
 * The weights will always be non-negative integers.
 * </P>
 * 
 * <P>
 * The WeightedGraph will be capable of performing three algorithms:
 * Depth-First-Search, Breadth-First-Search, and Djikatra's.
 * </P>
 * 
 * <P>
 * The Weighted Graph will maintain a collection of "GraphAlgorithmObservers",
 * which will be notified during the performance of the graph algorithms to
 * update the observers on how the algorithms are progressing.
 * </P>
 */
public class WeightedGraph<V> {

	private Map<V, Map<V, Integer>> wGraph;

	/*
	 * Collection of observers. Be sure to initialize this list in the constructor.
	 * The method "addObserver" will be called to populate this collection. Your
	 * graph algorithms (DFS, BFS, and Dijkstra) will notify these observers to let
	 * them know how the algorithms are progressing.
	 */
	private Collection<GraphAlgorithmObserver<V>> observerList;

	/**
	 * Initialize the data structures to "empty", including the collection of
	 * GraphAlgorithmObservers (observerList).
	 */
	public WeightedGraph() {
		wGraph = new HashMap<V, Map<V, Integer>>();
		observerList = new HashSet<GraphAlgorithmObserver<V>>();
	}

	/**
	 * Add a GraphAlgorithmObserver to the collection maintained by this graph
	 * (observerList).
	 * 
	 * @param observer
	 */
	public void addObserver(GraphAlgorithmObserver<V> observer) {
		observerList.add(observer);
	}

	/**
	 * Add a vertex to the graph. If the vertex is already in the graph, throw an
	 * IllegalArgumentException.
	 * 
	 * @param vertex vertex to be added to the graph
	 * @throws IllegalArgumentException if the vertex is already in the graph
	 */
	public void addVertex(V vertex) {
		if (!(wGraph.containsKey(vertex))) {
			wGraph.put(vertex, new HashMap<V, Integer>());
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Searches for a given vertex.
	 * 
	 * @param vertex the vertex we are looking for
	 * @return true if the vertex is in the graph, false otherwise.
	 */
	public boolean containsVertex(V vertex) {
		return wGraph.containsKey(vertex);
	}

	/**
	 * <P>
	 * Add an edge from one vertex of the graph to another, with the weight
	 * specified.
	 * </P>
	 * 
	 * <P>
	 * The two vertices must already be present in the graph.
	 * </P>
	 * 
	 * <P>
	 * This method throws an IllegalArgumentExeption in three cases:
	 * </P>
	 * <P>
	 * 1. The "from" vertex is not already in the graph.
	 * </P>
	 * <P>
	 * 2. The "to" vertex is not already in the graph.
	 * </P>
	 * <P>
	 * 3. The weight is less than 0.
	 * </P>
	 * 
	 * @param from   the vertex the edge leads from
	 * @param to     the vertex the edge leads to
	 * @param weight the (non-negative) weight of this edge
	 * @throws IllegalArgumentException when either vertex is not in the graph, or
	 *                                  the weight is negative.
	 */
	public void addEdge(V from, V to, Integer weight) {
		/*
		 * If the vertices from and to exist in the graph, and the weight is 0 or
		 * greater An edge is added with the desired weight between from and to
		 */
		if (this.containsVertex(from) && this.containsVertex(to) && weight >= 0) {
			wGraph.get(from).put(to, weight);
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * <P>
	 * Returns weight of the edge connecting one vertex to another. Returns null if
	 * the edge does not exist.
	 * </P>
	 * 
	 * <P>
	 * Throws an IllegalArgumentException if either of the vertices specified are
	 * not in the graph.
	 * </P>
	 * 
	 * @param from vertex where edge begins
	 * @param to   vertex where edge terminates
	 * @return weight of the edge, or null if there is no edge connecting these
	 *         vertices
	 * @throws IllegalArgumentException if either of the vertices specified are not
	 *                                  in the graph.
	 */
	public Integer getWeight(V from, V to) {
		if (this.containsVertex(from) && this.containsVertex(to)) { // If the vertex from and to exist
			return wGraph.get(from).get(to); // the weight of the edge is returned
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * <P>
	 * This method will perform a Breadth-First-Search on the graph. The search will
	 * begin at the "start" vertex and conclude once the "end" vertex has been
	 * reached.
	 * </P>
	 * 
	 * <P>
	 * Before the search begins, this method will go through the collection of
	 * Observers, calling notifyBFSHasBegun on each one.
	 * </P>
	 * 
	 * <P>
	 * Just after a particular vertex is visited, this method will go through the
	 * collection of observers calling notifyVisit on each one (passing in the
	 * vertex being visited as the argument.)
	 * </P>
	 * 
	 * <P>
	 * After the "end" vertex has been visited, this method will go through the
	 * collection of observers calling notifySearchIsOver on each one, after which
	 * the method should terminate immediately, without processing further vertices.
	 * </P>
	 * 
	 * @param start vertex where search begins
	 * @param end   the algorithm terminates just after this vertex is visited
	 */
	public void DoBFS(V start, V end) {
		Queue<V> discovered = new LinkedList<V>(); // discovered vertices
		List<V> vSet = new ArrayList<V>(); // list of visited vertices

		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyBFSHasBegun(); // notify each Observer that BFS has begun
		}

		discovered.add(start); // add starting vertex to queue

		while (discovered.size() != 0) { // confirms that queue is not empty
			V val = discovered.poll(); // takes first element in queue

			if (!(vSet.contains(val))) { // checks if value has been visited
				for (GraphAlgorithmObserver<V> observer : observerList) {
					observer.notifyVisit(val); // notifies observers that value is being visited
				}

				vSet.add(val); // adds value to visited list

				for (V adjacency : wGraph.get(val).keySet()) {
					if (!vSet.contains(adjacency)) { // Checks if adjacency has been visited
						discovered.add(adjacency); // adds adjacency to queue
					}
				}
			}

			if (vSet.contains(end)) { // checks if the end value has been visited
				for (GraphAlgorithmObserver<V> observer : observerList) {
					observer.notifySearchIsOver(); // notifies observers that search is over
				}
				break; // ends method
			}
		}
	}

	/**
	 * <P>
	 * This method will perform a Depth-First-Search on the graph. The search will
	 * begin at the "start" vertex and conclude once the "end" vertex has been
	 * reached.
	 * </P>
	 * 
	 * <P>
	 * Before the search begins, this method will go through the collection of
	 * Observers, calling notifyDFSHasBegun on each one.
	 * </P>
	 * 
	 * <P>
	 * Just after a particular vertex is visited, this method will go through the
	 * collection of observers calling notifyVisit on each one (passing in the
	 * vertex being visited as the argument.)
	 * </P>
	 * 
	 * <P>
	 * After the "end" vertex has been visited, this method will go through the
	 * collection of observers calling notifySearchIsOver on each one, after which
	 * the method should terminate immediately, without visiting further vertices.
	 * </P>
	 * 
	 * @param start vertex where search begins
	 * @param end   the algorithm terminates just after this vertex is visited
	 */
	public void DoDFS(V start, V end) {
		List<V> vSet = new ArrayList<V>(); // list of visited vertices

		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyDFSHasBegun(); // notifies each Observer that DFS has begun
		}

		DFSHelper(start, end, vSet); // Call to helper
	}

	private void DFSHelper(V start, V end, List<V> visitedSet) {

		if (!visitedSet.contains(end)) { // checks if end has been visited
			for (GraphAlgorithmObserver<V> observer : observerList) {
				observer.notifyVisit(start); // notifies observers value is being visited
			}

			visitedSet.add(start); // add start vertex to vSet

			for (V adjacency : wGraph.get(start).keySet()) { // loops through adjacency(s)
				if (!visitedSet.contains(adjacency)) { // Checks if adjacency has been visited
					DFSHelper(adjacency, end, visitedSet); // Call to helper
					if (visitedSet.contains(end)) { // Checks if end has been visited
						for (GraphAlgorithmObserver<V> observer : observerList) {
							observer.notifySearchIsOver(); // notifies observers that search is over
						}
						break; // ends method
					}
				}
			}
		}
	}

	/**
	 * <P>
	 * Perform Dijkstra's algorithm, beginning at the "start" vertex.
	 * </P>
	 * 
	 * <P>
	 * The algorithm DOES NOT terminate when the "end" vertex is reached. It will
	 * continue until EVERY vertex in the graph has been added to the finished set.
	 * </P>
	 * 
	 * <P>
	 * Before the algorithm begins, this method goes through the collection of
	 * Observers, calling notifyDijkstraHasBegun on each Observer.
	 * </P>
	 * 
	 * <P>
	 * Each time a vertex is added to the "finished set", this method goes through
	 * the collection of Observers, calling notifyDijkstraVertexFinished on each one
	 * (passing the vertex that was just added to the finished set as the first
	 * argument, and the optimal "cost" of the path leading to that vertex as the
	 * second argument.)
	 * </P>
	 * 
	 * <P>
	 * After all of the vertices have been added to the finished set, the algorithm
	 * will calculate the "least cost" path of vertices leading from the starting
	 * vertex to the ending vertex. Next, it will go through the collection of
	 * observers, calling notifyDijkstraIsOver on each one, passing in as the
	 * argument the "lowest cost" sequence of vertices that leads from start to end
	 * (I.e. the first vertex in the list will be the "start" vertex, and the last
	 * vertex in the list will be the "end" vertex.)
	 * </P>
	 * 
	 * @param start vertex where algorithm will start
	 * @param end   special vertex used as the end of the path reported to observers
	 *              via the notifyDijkstraIsOver method.
	 */
	public void DoDijsktra(V start, V end) {
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyDijkstraHasBegun();
		}

		Set<V> vSet = new HashSet<V>(); // set of visited vertices
		Map<V, V> pred = new HashMap<V, V>(); // Map of predecessors
		Map<V, Integer> cost = new HashMap<V, Integer>(); // Map of costs

		for (V currVertex : wGraph.keySet()) { // loops through each vertex
			cost.put(currVertex, 1000000); // puts vertex into cost with a large cost
			pred.put(currVertex, null); // null predecessor
		}

		cost.put(start, 0); // start cost is 0

		while (!vSet.containsAll(wGraph.keySet())) { // checks if all vertex(es) have been visited
			int sCost = 1000000; // smallest cost variable
			V sVertex = null; // vertex with the smallest cost

			for (V currVertex : wGraph.keySet()) {
				if (!(vSet.contains(currVertex))) { // checks if vertex hasn't been visited
					if (cost.get(currVertex) < sCost) { // checks if current cost is less than smallest cost
						sCost = cost.get(currVertex); // replaces smallest cost with current cost
						sVertex = currVertex; // replaces smallest vertex with current vertex
					}
				}
			}

			vSet.add(sVertex); // smallest vertex visited

			for (GraphAlgorithmObserver<V> observer : observerList) {
				observer.notifyDijkstraVertexFinished(sVertex, cost.get(sVertex)); // notifies observers that that value
																					// is being visited
			}

			for (V currVertex : wGraph.get(sVertex).keySet()) {
				if (!vSet.contains(currVertex)) { // Checks if vertex has been visited
					// Checks if cost to get adjacency is less than current cost
					if (cost.get(sVertex) + getWeight(sVertex, currVertex) < cost.get(currVertex)) {
						cost.put(currVertex, cost.get(sVertex) + getWeight(sVertex, currVertex)); // set cost to new
																									// cost
						pred.put(currVertex, sVertex); // sets predecessor
					}
				}
			}
		}

		ArrayList<V> fastPath = new ArrayList<V>(); // list stores fastest path

		V ending = end;
		while (ending != null) { // checks if end has reached the start
			fastPath.add(0, ending); // adds vertex to path
			ending = pred.get(fastPath.get(0)); // sets end to predecessor
		}

		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyDijkstraIsOver(fastPath); // notifies observers that Dijkstra is done
		}

	}

}
