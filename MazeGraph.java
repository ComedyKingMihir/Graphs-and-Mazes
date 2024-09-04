package graph;
import graph.WeightedGraph;
import maze.Juncture;
import maze.Maze;

/** 
 * <P>The MazeGraph is an extension of WeightedGraph.  
 * The constructor converts a Maze into a graph.</P>
 */
public class MazeGraph extends WeightedGraph<Juncture> {

	/* STUDENTS:  SEE THE PROJECT DESCRIPTION FOR A MUCH
	 * MORE DETAILED EXPLANATION ABOUT HOW TO WRITE
	 * THIS CONSTRUCTOR
	 */
	
	/** 
	 * <P>Construct the MazeGraph using the "maze" contained
	 * in the parameter to specify the vertices (Junctures)
	 * and weighted edges.</P>
	 * 
	 * <P>The Maze is a rectangular grid of "junctures", each
	 * defined by its X and Y coordinates, using the usual
	 * convention of (0, 0) being the upper left corner.</P>
	 * 
	 * <P>Each juncture in the maze should be added as a
	 * vertex to this graph.</P>
	 * 
	 * <P>For every pair of adjacent junctures (A and B) which
	 * are not blocked by a wall, two edges should be added:  
	 * One from A to B, and another from B to A.  The weight
	 * to be used for these edges is provided by the Maze. 
	 * (The Maze methods getMazeWidth and getMazeHeight can
	 * be used to determine the number of Junctures in the
	 * maze. The Maze methods called "isWallAbove", "isWallToRight",
	 * etc. can be used to detect whether or not there
	 * is a wall between any two adjacent junctures.  The 
	 * Maze methods called "getWeightAbove", "getWeightToRight",
	 * etc. should be used to obtain the weights.)</P>
	 * 
	 * @param maze to be used as the source of information for
	 * adding vertices and edges to this MazeGraph.
	 */
	public MazeGraph(Maze maze) {
		for (int x = 0; x < maze.getMazeWidth(); x++) {
			for (int y = 0; y < maze.getMazeHeight(); y++) {
				this.addVertex(new Juncture(x, y));
			}
		}
		
		for (int x = 0; x < maze.getMazeWidth(); x++) {
			for (int y = 0; y < maze.getMazeHeight(); y++) {
				Juncture currJuncture = new Juncture(x, y); //current junction 
				Juncture junctAbove = new Juncture(x, y - 1); //junction above current
				Juncture junctBelow = new Juncture(x, y + 1); //junction below current
				Juncture junctLeft = new Juncture(x - 1, y); //junction left of current
				Juncture junctRight = new Juncture(x + 1, y); //junction right of current
				
				//Edge with desired weight is added if there is no wall above current juncture
				//Also checks to make sure there is a juncture above the current juncture
				if (!(maze.isWallAbove(currJuncture)) && this.containsVertex(junctAbove)) {
					this.addEdge(currJuncture, junctAbove, maze.getWeightAbove(currJuncture));
				}
				
				//Edge with desired weight is added if there is no wall below current juncture
				//Also checks to make sure there is a juncture below the current juncture
				if (!(maze.isWallBelow(currJuncture)) && this.containsVertex(junctBelow)) {
					this.addEdge(currJuncture, junctBelow, maze.getWeightBelow(currJuncture));
				}
				
				//Edge with desired weight is added if there is no wall left of current juncture
				//Also checks to make sure there is a juncture left of the current juncture
				if (!(maze.isWallToLeft(currJuncture)) && this.containsVertex(junctLeft)) {
					this.addEdge(currJuncture, junctLeft, maze.getWeightToLeft(currJuncture));
				}
				
				//Edge with desired weight is added if there is no wall right of current juncture
				//Also checks to make sure there is a juncture right of the current juncture
				if (!(maze.isWallToRight(currJuncture)) && this.containsVertex(junctRight)) {
					this.addEdge(currJuncture, junctRight, maze.getWeightToRight(currJuncture));
				}
			}
		}
	}
}
