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
**render/** - Source files associated with a renderer used to render simulations <br>
**task/** - Source files associated with multitasking and Thread synchronization between robots <br>

Examples
===================

Path Planning
---------------

The main class of the Path Planning library is called GraphSearchEngine. This object is responsible for determining which search needs to be performed based on the EngineConfig object that is passed to it. This could be a single agent path like A* or Jump Point Search, or it could be a multiple agent search like Conflict Based Search. The search being performed depends on the parameters of EngineConfig object passed to it. After being configured the search can be performed using the plan() method. <br>
<br>
The type of object returned by the plan method depends on which search type was configured. The performance of the search is stored in a SearchPerformance object and can be queried after the search by calling getPerformanceStats() on the GraphSearchEngine. <br>

**A* Example**

A* is a path planning algorithm which will bring a single robot (or agent, like a character in a video game) from one location in a graph or map to another. To create this path we need to create the map being explored and specify the locations of any stationary obstacles. We also need to create a SingleAgentConfig object to configure the GraphSearchEngine.

	// Create a 5x5 cartesian grid, indexed from 0
	ObstacleMap map = new ObstacleMap(5, 5);
	
	// here is where we could specify any obstacles with a method call
	// or load the information from a file
	// using map.blockXY(int x, int y)
	
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
	
	// this object is a List of LocationVertex objects which
	// are the x,y,t elements of the path the agent should follow
			
	System.out.println("Path: " + p.toString());
			
	SearchPerformance stats = eng.getPerformanceStats();
	
	System.out.println("Run Time (ms): " + stats.getTimeToRunInMilliseconds());
	System.out.println("Number of Nodes in Grid expanded: " + stats.getNumberOfNodesExpanded());
	System.out.println("Path Length: " + stats.getSolutionLength());
	
**Jump Point Search Example**

This example is identical to the A* example except the parameter "is Jump Point Search" should be set to true. Jump Point Search is a much faster path planning algorithm than A* on uniform cost grids, which is the type of graph being searched here.  There is also much less memory overhead. In addition, the paths produced tend to be straighter across open spaces.

**Conflict Based Search Example**

Unlike A* or Jump Point Search, Conflict Based Search is a path planning algorithm for multiple robots or agents. Its goal is to plan a path from point A to B for every agent so that they are both free of conflicts and collisions. No conflicts exist between the agents and obstacles or agents and other agents. These paths are also optimal, so the sum of all path lengths is guaranteed to be the shortest possible.

	ObstacleMap map = new ObstacleMap(5, 5);
	
	// create the start and target locations for each agent in x, y, t
	// -1 for the target time t means we don't care when it finds
	// the target location
	LocationVertex start0 = new LocationVertex(2, 0, 0);
	LocationVertex target0 = new LocationVertex(2, 4, -1);
	LocationVertex start1 = new LocationVertex(2, 4, 0);
	LocationVertex target1 = new LocationVertex(2, 0, -1);
	
	Agent a0 = new Agent(0, start0, target0);
	Agent a1 = new Agent(1, start1, target1);
	
	// add the Agents to the list
	ArrayList<Agent> agents = new ArrayList<Agent>();
	
	agents.add(a0);
	agents.add(a1);
	
	// create our EngineConfig object
	CBSConfig conf = new CBSConfig(agents, map);
	
	System.out.println("Config: " + conf.toString());
	
	GraphSearchEngine eng = new GraphSearchEngine(conf);
	
	// searches the constraint tree for the solution
	ConstraintTreeNode goal = (ConstraintTreeNode) eng.plan();
	
	if (goal == null)
	{
		System.err.println("Error: Solution is null. This means the " +
		"algorithm could find no valid solution. For example, this " +
		"happens when a target location is completely blocked " +
		"by obstacles.");
	}
	
	// print our solution
	for (Agent a : goal.getSolution().keySet())
	{
		AgentPath p = goal.getSolution().get(a);
		
		System.out.println("For Agent " + a.getId() + " path is: ");
		System.out.println(p.toString());
	}
	
**Rendering Paths**

The render package is used to render simulations in a window using the graphics card. This package could use some work, and would ideally read from a log file. Its cool to use it to visualize the path planning results. 