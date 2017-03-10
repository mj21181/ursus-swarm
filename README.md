# ursus-swarm

A Java library for robot-based path planning and particle swarm optimization.

This library was created as part of the work for my Master's Thesis. If you are interested, it is freely available for download here:
http://search.proquest.com/docview/1868839549

Why name the library ursus-swarm? I could have named it JSwarm, Java Swarm, or some Swarm-Java combination.  While functionally descriptive I think those names are boring. A swarm of bears though? That's a terrifying prospect.

Project Structure
------------------

**lib/** - Folder containing dependent libraries and jars. In the future, this will hopefully be replaced by Maven <br>
**resources/** - Folder containing project resources like images <br>
**src/** - Folder containing source files <br>
<br>
**src/main/java/** - Main source files <br>
**src/test/java/** - Unit Tests <br>

src/main/java:

**event/** - Source files associated with transmission, reception, and processing of events and messages passed between robots <br>
**gui/** - Source files associated with various GUI components like the display of plots <br>
**log/** - Source files related to the library's logging mechanism. Interface exists for attaching log messages to an application using this library <br>
**path/** - Source files related to robot path planning for single or multiple robots <br>
**pso/** - Source files related to particle swarm optimization robots can use to search for some goal <br>
**render/** - Source files associated with a renderer used to render simulations or logs <br>
**task/** - Source files associated with multitasking and Thread synchronization between robots <br>

Examples
===================

Path Planning
---------------

The main class of the Path Planning library is called GraphSearchEngine. This object is responsible for determining which search needs to be performed based on the EngineConfig object that is passed to it. This could be a single agent path like A* or Jump Point Search, or it could be a multiple agent search like Conflict Based Search. The search being performed depends on the parameters of EngineConfig object passed to it. After being configured the search can be performed using the plan() method. <br>
<br>
The type of object returned by the plan method depends on which search type was configured. The performance of the search is stored in a SearchPerformance object and can be queried after the search by calling getPerformanceStats() on the GraphSearchEngine. <br>

**A* Example**

	// Create a 5x5 cartesian grid, indexed from 0
	ObstacleMap map = new ObstacleMap(5, 5);
	
	/* Create our EngineConfig object
	 * A* is a single agent search, so we make a SingleAgentConfig
	 * The parameters are as follows:
	 * id number - 0
	 * start x position     - 2
	 * start y position     - 1
	 * start time index     - 0
	 * target x position    - 4
	 * target y position    - 4
	 * target time          - -1 (we don't care when the search ends)
	 * is Jump Point Search - false (using A*)
	 * obstacle map         - map
	 * constraints          - empty list, no constraints imposed by other robots (only 1)
	 */
	SingleAgentConfig cfg = new SingleAgentConfig(0, 2, 1, 0, 4, 4,
					-1, false, map, new LinkedList<Constraint>());
	
	// construct our search engine				
	GraphSearchEngine eng = new GraphSearchEngine(cfg);
	
	// plan our path
	AgentPath p = (AgentPath) eng.plan();
			
	System.out.println("Path: " + p.toString());
			
	SearchPerformance stats = eng.getPerformanceStats();
	
	System.out.println("Run Time (ms): " + stats.getTimeToRunInMilliseconds());
	System.out.println("Number of Nodes in Grid expanded: " + stats.getNumberOfNodesExpanded());
	System.out.println("Path Length: " + stats.getSolutionLength());
	
**Jump Point Search Example**

This example is identical to the A* example except the parameter "is Jump Point Search" should be set to true. Jump Point Search is a much faster path planning algorithm than A* on uniform cost grids, which is the type of graph being searched here.  There is also much less memory overhead. In addition, the paths produced are straighter.