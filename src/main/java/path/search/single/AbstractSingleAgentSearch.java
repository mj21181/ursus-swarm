/**
 * Copyright (C) 2016 Michael Johnson
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package path.search.single;

import java.util.HashSet;
import java.util.List;
import java.util.Queue;

import path.GraphSearch;
import path.config.SingleAgentConfig;
import path.elements.Agent;
import path.elements.AgentPath;
import path.elements.Constraint;
import path.elements.VertexTypeException;
import path.elements.heuristic.EuclidianDistanceGraphHeuristic;
import path.elements.heuristic.GraphHeuristic;
import path.elements.map.ObstacleMap;
import path.elements.vertices.GraphVertex;
import path.elements.vertices.LocationVertex;
import log.ApplicationLogger;

/**
 * This class is the parent class which implements all methods common to a
 * single agent search of a uniform cost grid.
 * 
 * @author Mike Johnson
 * 
 */
public abstract class AbstractSingleAgentSearch implements GraphSearch
{
	/**
	 * The {@link GraphHeuristic} being used by the search algorithm to judge
	 * the H score between any two vertices in the graph.
	 */
	protected GraphHeuristic			heuristic	=
			new EuclidianDistanceGraphHeuristic();
	
	/**
	 * The Closed List used to store {@link LocationVertex} objects that we have
	 * already searched. HashSet is used because contains() operation is O(1)
	 */
	protected HashSet<LocationVertex>	closedList	=
			new HashSet<LocationVertex>();
	
	/**
	 * The Root {@link LocationVertex} which is our start location
	 */
	protected LocationVertex			root		= null;
	
	/**
	 * The Goal {@link LocationVertex} which is our target location
	 */
	protected LocationVertex			goal		= null;
	
	/**
	 * The dynamics {@link Constraint}s on this search which prevent this agent
	 * from occupying a given {@link LocationVertex} at a given time
	 */
	protected List<Constraint>			constraints	= null;
	
	/**
	 * Our map of obstacle locations within the search space
	 */
	protected ObstacleMap				obstacles	= null;
	
	/**
	 * The ID number of the {@link Agent} performing the search
	 */
	protected int						id			= -1;
	
	protected boolean					debug		= false;
	
	/**
	 * A shortcut reference to the logger
	 */
	protected ApplicationLogger			log			=
			ApplicationLogger.getInstance();
	
	/**
	 * The parent constructor for implementations of a single agent search to
	 * use
	 * 
	 * @param cfg
	 */
	protected AbstractSingleAgentSearch (SingleAgentConfig cfg)
	{
		id = cfg.getAgent().getId();
		
		// get start and target location
		root = cfg.getAgent().getStart();
		goal = cfg.getAgent().getTarget();
		
		// set constraints
		obstacles = cfg.getObstacles();
		
		if (!obstacles.vertexExists(root.getY(), root.getX())
				|| !obstacles.vertexExists(goal.getY(),
						goal.getX())) { throw new IllegalArgumentException(
								"Bad Configuration: " + root.toString()
										+ " and " + goal.toString()); }
		
		if (cfg.getDebug())
		{
			debug = true;
		}
		
		// log.logDebug("Setting Constraints: %s",
		// Arrays.toString(cfg.getConstraints().toArray()));
		
		constraints = cfg.getConstraints();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.GraphSearch#initialize()
	 */
	@Override
	public GraphVertex initialize ()
	{
		root.setG(0);
		
		try
		{
			root.setH(heuristic.calculateCost(root, goal));
		}
		catch (VertexTypeException e)
		{
			log.logException(e);
		}
		
		// calculate f score
		root.setCost();
		root.setParent(null); // we are the root
		
		// return start location
		return root;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.GraphSearch#expandSuccessorNodes(edu.unh.acl
	 * .app.model.path.GraphVertex, java.util.List)
	 */
	@Override
	public boolean expandSuccessorNodes (GraphVertex current,
			Queue<GraphVertex> openList) throws VertexTypeException
	{
		if (! (current instanceof LocationVertex)) { throw new VertexTypeException(
				"Input vertex not of type "
						+ LocationVertex.class.getSimpleName()); }
		
		LocationVertex currentLocation = (LocationVertex) current;
		
		if (currentLocation.getT() < 10)
		{
			log.logDebug("Expanding Successor Nodes: Current: %s",
					currentLocation.toString());
			
		}
		
		// so that we do not search again in future
		closedList.add(currentLocation);
		
		for (LocationVertex successor : findSuccessors(currentLocation))
		{
			if (debug)
			{
				log.log("Found successor node: " + successor.toString());
			}
			
			if (checkIfInClosedList(successor))
			{
				continue;
			}
			
			if (currentLocation.getT() < 10)
			{
				log.logDebug("Found Successor: " + successor.toString());
			}
			
			// set parent location
			successor.setParent(current);
			
			// if (currentLocation.getT() < 10)
			// {
			// log.logDebug("Set Parent Node: " + current.toString());
			// }
			
			// if we found the target location, break from the search loop
			if (checkIfFoundTarget(successor))
			{
				// save successor for the parent vertex
				goal = successor;
				
				if (debug)
				{
					log.log("Goal Node Found %s", successor.toString());
				}
				
				// log.logDebug("Goal Node Found %s", goal.toString());
				
				return true;
			}
			
			// now we need to calculate the neighbor node's g score
			double nextCost = calculateGScore(currentLocation, successor);
			
			// log.logDebug(
			// "Calculated Successor G score %f", nextCost);
			
			successor.setG(nextCost);
			
			// calculate the h score
			successor.setH(heuristic.calculateCost(successor, goal));
			
			// log.logDebug(
			// "Calculated Successor H score %f", successor.getH());
			
			// calculate the f score (add g + h)
			successor.setCost();
			
			// log.logDebug(
			// "Calculated Successor F score %f", successor.getCost());
			
			openList.add(successor);
		}
		
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.GraphSearch#getSolution()
	 */
	@Override
	public Object getSolution ()
	{
		AgentPath p = new AgentPath();
		
		if (debug)
		{
			log.log("Reconstructing path from " + goal.toString());
		}
		
		// get the path from parent locations
		p.reconstruct(goal);
		
		if (debug)
		{
			log.log("Padding path");
		}
		
		optionallyPadPath(p);
		
		return p;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.GraphSearch#needsToRun()
	 */
	@Override
	public boolean needsToRun ()
	{
		// check if the target location is blocked
		
		if (obstacles.isBlockedXY(goal.getX(), goal.getY()))
		{
			goal.setParent(root);
			goal.setT(1);
			
			return false;
		}
		
		// check if start location is target location
		
		if (root.equals(goal)) { return false; }
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.GraphSearch#getClosedListSize()
	 */
	@Override
	public int getClosedListSize ()
	{
		return closedList.size();
	}
	
	/* Methods used in expandSuccessorNodes ******************************* */
	
	protected boolean checkIfFoundTarget (LocationVertex successor)
	{
		// we are searching for the goal tile, not caring about when
		
		// System.out.println("Checking goal: " + goal.toString());
		
		if (goal.getT() == -1)
		{
			if (successor.equals(goal)) { return true; }
		}
		else
		{
			if (successor.equals(goal))
			{
				if (successor.getT() >= goal.getT()) { return true; }
			}
		}
		
		return false;
	}
	
	/**
	 * Returns true if a given location vertex is already present in the closed
	 * list. False otherwise. This check includes the time dimension
	 * 
	 * @param l The {@link LocationVertex} to check
	 * @return
	 */
	protected boolean checkIfInClosedList (LocationVertex l)
	{
		for (LocationVertex v : closedList)
		{
			if (v.equalsWithTime(l)) { return true; }
		}
		
		return false;
	}
	
	/**
	 * Calculates the G Score for the successor {@link LocationVertex} given the
	 * current {@link LocationVertex}
	 * 
	 * @param current
	 * @param successor
	 * @return
	 */
	protected abstract double calculateGScore (LocationVertex current,
			LocationVertex successor);
	
	/**
	 * Finds successor nodes to this current node which will be expanded and
	 * added to the open list
	 * 
	 * @param current the current {@link LocationVertex}
	 * @return a {@link List} of successor nodes
	 */
	protected abstract List<LocationVertex>
			findSuccessors (LocationVertex current);
	
	/**
	 * If the single agent search implementation skips over some nodes in the
	 * path, it may need to pad locations so that it properly is constructed.
	 * 
	 * @param p
	 */
	protected abstract void optionallyPadPath (AgentPath p);
	
	/* General utility methods ******************************************** */
	
	/**
	 * Checks whether two nodes of the graph are diagonal from each other in the
	 * space dimension with the time dimension ignored
	 * 
	 * @param current The current node
	 * @param successor The child node
	 * @return true if they are spatially diagonal
	 */
	protected boolean checkIfDiagonal (LocationVertex current,
			LocationVertex successor)
	{
		int sX = successor.getX();
		int sY = successor.getY();
		int cX = current.getX();
		int cY = current.getY();
		
		if ( (sX == (cX - 1) && sY == (cY - 1))
				|| (sX == (cX + 1) && sY == (cY - 1))
				|| (sX == (cX + 1) && sY == (cY + 1))
				|| (sX == (cX - 1) && sY == (cY + 1))) { return true; }
		return false;
	}
	
	/**
	 * Checks whether a given node is present in the list of {@link Constraint}s
	 * or is an obstacle, or is outside the bounds of the search space.
	 * 
	 * @param x The x location of the node
	 * @param y The y location of the node
	 * @param t The t location of the node
	 * @return True if the node is blocked, false otherwise
	 */
	protected boolean checkIfBlocked (int x, int y, int t)
	{
		LocationVertex test = new LocationVertex(x, y, t);
		
		// log.logDebug("Checking if vertex exists
		// %s",
		// test.toString());
		
		if (!obstacles.vertexExists(y, x)) { return true; }
		
		// log.logDebug("Checking if vertex is
		// blocked");
		
		if (obstacles.isBlockedXY(x, y))
		{
			// log.logDebug("Blocked: %d, %d", x,
			// y);
			return true;
		}
		
		// log.logDebug("Checking if constraints
		// apply");
		
//		for (Constraint c : constraints)
//		{
//			if (c.getAgent() == id)
//			{
//				if (c.applies(test))
//				{
//					
//					if (debug)
//					{
//						log.log("found constraint: " + c.toString());
//					}
//					
//					return true;
//				}
//			}
//		}
		
		return false;
	}
	
}
