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
package path.search.multi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import path.GraphSearch;
import path.config.CBSConfig;
import path.elements.Agent;
import path.elements.Conflict;
import path.elements.Constraint;
import path.elements.VertexTypeException;
import path.elements.edge.GraphEdge;
import path.elements.heuristic.GraphHeuristic;
import path.elements.heuristic.SumInCostsHeuristic;
import path.elements.map.ObstacleMap;
import path.elements.vertices.ConstraintTreeNode;
import path.elements.vertices.GraphVertex;
import path.elements.vertices.LocationVertex;
import log.ApplicationLogger;

public class CBS implements GraphSearch
{
	/**
	 * The Goal {@link ConstraintTreeNode} being searched for
	 */
	protected ConstraintTreeNode			goalNode	= null;
	
	/**
	 * The {@link GraphHeuristic} being used to calculate the cost for each node
	 */
	protected GraphHeuristic				heuristic	= null;
	
	/**
	 * The {@link Agent}s searching the {@link ObstacleMap}
	 */
	protected ArrayList<Agent>				agents		= null;
	
	/**
	 * The {@link ObstacleMap} containing the locations of obstacles in the
	 * search space
	 */
	protected ObstacleMap					map			= null;
	
	/**
	 * The {@link SolutionValidator} used to check whether a given solution is
	 * valid
	 */
	protected SolutionValidator				validator	=
			new SolutionValidator();
	
	/**
	 * The Closed List used to store {@link ConstraintTreeNode} objects that we
	 * have already searched. HashSet is used because contains() operation is
	 * O(1)
	 */
	protected HashSet<ConstraintTreeNode>	closedList	=
			new HashSet<ConstraintTreeNode>();
	
	protected boolean						debug		= false;
	
	/**
	 * Constructs a {@link CBS} object from the specified {@link CBSConfig}
	 * object
	 * 
	 * @param cfg
	 */
	public CBS (CBSConfig cfg)
	{
		this.agents = cfg.getAgents();
		this.map = cfg.getObstacles();
		
		if (cfg.getDebug())
		{
			debug = true;
		}
		
		heuristic = new SumInCostsHeuristic(debug);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.GraphSearch#initialize()
	 */
	@Override
	public GraphVertex initialize ()
	{
		// initialize root node
		
		ConstraintTreeNode root = new ConstraintTreeNode();
		
		// add obstacle constraints optionally
		
		root.setNodeConstraint(null);
		
		// run Single Agent searches for all agents
		
		root.generateSolution(agents, map, debug);
		
		// calculate Sum In Costs for root node
		
		// ApplicationLogger.getInstance().logDebug("Calculating Cost for Root
		// Node");
		
		try
		{
			root.setG(heuristic.calculateCost(root, null));
			root.setCost();
		}
		catch (VertexTypeException e)
		{
			// should not ever happen
			ApplicationLogger.getInstance().logException(e);
		}
		
		// return the root node
		return root;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.GraphSearch#getSolution()
	 */
	@Override
	public Object getSolution ()
	{
		return goalNode;
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
		// check node is of correct type
		if (! (current instanceof ConstraintTreeNode)) { throw new VertexTypeException(
				"Graph Vertex needs to be of type ConstraintTreeNode"); }
		
		ConstraintTreeNode node = (ConstraintTreeNode) current;
		
		closedList.add(node);
		
		if (debug)
		{
			ApplicationLogger.getInstance()
					.log("Current Node: " + node.toString());
		}
		
		// validate paths
		
		Queue<Conflict> conflicts = validator.validateSolution(node);
		
		if (debug)
		{
			for (Conflict c : conflicts)
			{
				ApplicationLogger.getInstance()
						.log("Found Conflict: " + c.toString());
			}
		}
		
		// if there were no conflicts then we found the goal node and should
		// save the solution
		if (conflicts.isEmpty())
		{
			goalNode = node;
			
			return true;
		}
		
		Conflict conflict = conflicts.poll();
		
		// there was a conflict found, now we need to expand the child nodes
		
		for (Integer agent : conflict.getAgents())
		{
			// create the new node
			ConstraintTreeNode child = new ConstraintTreeNode();
			
			// set its parent
			child.setParent(node);
			
			// set the constraint for this node
			
			Constraint c = null;
			
			if (conflict.getGraphObject() instanceof LocationVertex)
			{
				LocationVertex v = (LocationVertex) conflict.getGraphObject();
				
				if (debug)
				{
					ApplicationLogger.getInstance()
							.log("Vertex Conflict: " + v.toString());
				}
				
				ArrayList<LocationVertex> nextVertices =
						new ArrayList<LocationVertex>(
								conflict.getAgents().size() - 1);
				
				for (Integer otherAgent : conflict.getAgents())
				{
					if (otherAgent.intValue() != agent.intValue())
					{
						nextVertices.add(conflict.getNextLocation(otherAgent));
					}
				}
				
				c = new Constraint(agent, v);
				
				c.addNextVertices(nextVertices);
			}
			else if (conflict.getGraphObject() instanceof GraphEdge)
			{
				
				GraphEdge e = (GraphEdge) conflict.getGraphObject();
				
				if (debug)
				{
					ApplicationLogger.getInstance()
							.log("Edge Conflict: " + e.toString());
				}
				
				// there are only two agents for an edge conflict, otherwise it
				// is a vertex conflict
				Integer otherAgent = null;
				
				for (Integer test : conflict.getAgents())
				{
					if (test.intValue() != agent.intValue())
					{
						otherAgent = test;
					}
				}
				
				LocationVertex otherNext = conflict.getNextLocation(otherAgent);
				
				LocationVertex otherCurrent = null;
				
				LocationVertex v1 = (LocationVertex) e.getV1();
				LocationVertex v2 = (LocationVertex) e.getV2();
				
				if(otherNext.equals(v1))
				{
					otherCurrent = v2;
				}
				else
				{
					otherCurrent = v1;
				}
				
				c = new Constraint(agent, otherCurrent);
				
				// there is only one
				ArrayList<LocationVertex> nextLocations = new ArrayList<LocationVertex>();
				nextLocations.add(otherNext);
				
				c.addNextVertices(nextLocations);
			}
			
			ArrayList<Constraint> constraints = node.buildConstraintList(node);
			
			if (!constraints.contains(c))
			{
				if (debug)
				{
					ApplicationLogger.getInstance()
							.log("Adding constraint: " + c.toString());
				}
				
				child.setNodeConstraint(c);
				
				// set the solution for this node
				child.setSolution(node.getSolution());
				
				// copy the jump point constraints
				child.setJumpPointConstraints(node.getJumpPointConstraints());
				
				// copy the performance stats
				child.setPerformanceStats(node.getPerformance());
				
				// update this node's solution for the new constraint
				boolean success = child.updateSolution(map, debug);
				
				// if we found a solution, otherwise we can't add this node to
				// the constraint tree
				if (success)
				{
					// calculate the Sum In Costs value
					child.setG(heuristic.calculateCost(child, null));
					child.setCost();
					
					if (debug)
					{
						ApplicationLogger.getInstance()
								.log("New Node: " + child.toString());
					}
					
					// for (Agent a : child.getSolution().keySet())
					// {
					// ApplicationLogger.getInstance()
					// .log("Agent: " + a.getId());
					// ApplicationLogger.getInstance()
					// .log(child.getSolution().get(a).toString());
					// }
					// TODO:
					// if a solution was found, all the child node to the open
					// list
					openList.add(child);
				}
			}
		}
		
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.GraphSearch#needsToRun()
	 */
	@Override
	public boolean needsToRun ()
	{
		// validate the root node, if no conflict is found then return
		// optionally, just return true and have that happen first time
		// expandSuccessorNodes is called
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
	
	public Set<ConstraintTreeNode> getClosedList ()
	{
		return closedList;
	}
	
}
