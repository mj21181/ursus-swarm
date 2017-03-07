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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.math3.geometry.spherical.twod.Edge;

import path.elements.Agent;
import path.elements.AgentPath;
import path.elements.Conflict;
import path.elements.edge.GraphEdge;
import path.elements.vertices.ConstraintTreeNode;
import path.elements.vertices.GraphVertex;
import path.elements.vertices.LocationVertex;

/**
 * This class is used to validate solutions to the Multi Agent Path Finding
 * problem. The class is a library of method calls related to this process.
 * Returned is a {@link Queue} of {@link Conflict}s found during the validation
 * process.
 * 
 * @author Mike Johnson
 *
 */
public class SolutionValidator
{
	/**
	 * When the solution is validated, if this node is not a goal node then a
	 * conflict will occur. This means that the node has child
	 * {@link ConstraintTreeNode}s which can be expanded.
	 * 
	 * @return The {@link Queue} of {@link Conflict}s generated while validating
	 *         the solution of this node, or queue is empty if it is a goal.
	 */
	public Queue<Conflict> validateSolution (ConstraintTreeNode node)
	{
		int timestep = 0;
		
		Map<Integer, AgentPath> sol = new HashMap<Integer, AgentPath>();
		
		for (Agent a : node.getSolution().keySet())
		{
			sol.put(a.getId(), node.getSolution().get(a));
		}
		
		// ApplicationLogger.getInstance().logDebug(
		// "Beginning Solution Validation");
		
		// get the last timestep in the solution so that we can iterate over all
		// of the timesteps
		int lastTime = node.getLastTimestep();
		
		// ApplicationLogger.getInstance().logDebug("Last Timestep %d",
		// lastTime);
		
		LinkedList<Conflict> conflicts = new LinkedList<Conflict>();
		
		while (timestep < lastTime)
		{
			// ApplicationLogger.getInstance().logDebug("Current Timestep: %d",
			// timestep);
			
			// get the mapping of agents to location at this timestep
			Map<Integer, GraphVertex> currentTimestep =
					getVerticesAtTimestep(timestep, sol);
			
			// get the mapping of agents to location at the next timestep
			Map<Integer, GraphVertex> nextTimestep =
					getVerticesAtTimestep(timestep + 1, sol);
			
			// get all of the vertex conflicts that exist
			conflicts.addAll(generateVertexConflicts(timestep, nextTimestep,
					currentTimestep));
			
			// get all of the edge conflicts that exist
			conflicts.addAll(generateEdgeConflicts(timestep, nextTimestep,
					currentTimestep));
			
			// increment our timestep
			timestep++ ;
		}
		
		Collections.sort(conflicts);
		
		return conflicts;
	}
	
	/**
	 * For a given timestep, it returns all of the {@link GraphVertex} objects
	 * in each {@link Agent}'s {@link AgentPath}.
	 * 
	 * Returns a {@link Map} between ID number and {@link GraphVertex} for that
	 * agent. If an {@link AgentPath} is not as long as the timestep specified,
	 * no mapping is added for that agent.
	 * 
	 * @param timestep
	 * @param sol
	 * @return
	 */
	protected Map<Integer, GraphVertex> getVerticesAtTimestep (int timestep,
			Map<Integer, AgentPath> sol)
	{
		Map<Integer, GraphVertex> vertices =
				new HashMap<Integer, GraphVertex>();
		
		// check vertices at this timestep
		for (Integer agent : sol.keySet())
		{
			AgentPath path = sol.get(agent);
			
			GraphVertex v = null;
			
			// search backward from the current timestep to find the last vertex
			// this agent was located at (if the timestep is longer than the
			// path length, otherwise it just gets the current vertex)
			int count = timestep;
			
			while (v == null)
			{
				v = path.vertexAtTime(count);
				
				count-- ;
			}
			
			vertices.put(agent, v);
		}
		
		return vertices;
	}
	
	/**
	 * Scans through the given set of {@link GraphVertex} objects and identifies
	 * any {@link Conflict}s between them, where an {@link Agent} is occupying
	 * the same vertex at the same time.
	 * 
	 * @param time
	 * @param vertices
	 * @return
	 */
	protected List<Conflict> generateVertexConflicts (int time,
			Map<Integer, GraphVertex> nextTimestep,
			Map<Integer, GraphVertex> vertices)
	{
		// create mapping for all of the vertex objects
		Map<Integer, GraphVertex> vers = new HashMap<Integer, GraphVertex>();
		
		// create conflict list that we will return
		ArrayList<Conflict> conflicts = new ArrayList<Conflict>();
		
		// TODO: better method
		// iterate through all of the vertices at this timestep
		for (Integer agent : vertices.keySet())
		{
			// the vertex for this agent
			GraphVertex v = vertices.get(agent);
			
			// the next vertex for this agent
			GraphVertex nv = nextTimestep.get(agent);
			
			// something went wrong and we should stop
			if (v == null) { throw new IllegalStateException(
					"Error: No Vertex for Agent: " + agent + " at timestep: "
							+ time); }
			
			// if we reached the end
			if (nv == null)
			{
				nv = v;
			}
			
			boolean found = false;
			
			// check if we found this vertex before at this timestep
			for (Integer other : vers.keySet())
			{
				// if there is a conflict
				if (vers.get(other).equals(v))
				{
					Conflict c = new Conflict(time, v);
					
					// this conflict has been found before
					if (conflicts.contains(c))
					{
						// get the object from before, and add this id to the
						// list of ids conflicting at this vertex
						conflicts.get(conflicts.indexOf(c)).addAgent(agent, (LocationVertex) nv);
					}
					else
					{
						LocationVertex onv = (LocationVertex) nextTimestep.get(other);
						
						if(onv == null)
						{
							onv = (LocationVertex) vers.get(other);
						}
						
						// it's a new conflict, add the two ids to the list
						c.addAgent(agent, (LocationVertex) nv);
						c.addAgent(other, onv);
						
						conflicts.add(c);
					}
					
					found = true;
					break;
				}
			}
			
			// no conflict, add it to the vertex mapping for when we check
			// future agents
			if (!found)
			{
				vers.put(agent, v);
			}
		}
		
		return conflicts;
	}
	
	/**
	 * Checks the Vertex Mapping at the current timestep and the Vertex Mapping
	 * at the last timestep for any conflicts on the graph {@link Edge} objects.
	 * 
	 * @param time
	 * @param nextTimestep
	 * @param currentTimestep
	 * @return
	 */
	protected List<Conflict> generateEdgeConflicts (int time,
			Map<Integer, GraphVertex> nextTimestep,
			Map<Integer, GraphVertex> currentTimestep)
	{
		// if (currentTimestep.keySet().size() != lastTimestep.keySet().size())
		// { throw new IllegalArgumentException(
		// "Vertex Maps must have the same size"); }
		
		// create mapping for all of the edge objects we have already found
		Map<Integer, GraphEdge> edges = new HashMap<Integer, GraphEdge>();
		
		ArrayList<Conflict> conflicts = new ArrayList<Conflict>();
		
		// TODO: better method
		// iterate through all of the edges at this timestep
		for (Integer agent : currentTimestep.keySet())
		{
			GraphVertex current = currentTimestep.get(agent);
			GraphVertex next = nextTimestep.get(agent);
			
			// this means the agent reached the goal at this timestep and no
			// edge exists here
			if (next == null)
			{
				continue;
			}
			
			// we know this edge exists now, we don't need to check current
			// because it exists in the keySet
			GraphEdge edge = new GraphEdge(current, next);
			
			boolean found = false;
			
			// check if we found this one before
			for (Integer other : edges.keySet())
			{
				GraphEdge otherEdge = edges.get(other);
				
				// if there is a conflict because we already found this (equals
				// checks both edge directions)
				if (edge.equals(otherEdge))
				{
					// create the conflict object
					Conflict c = new Conflict(time, edge);
					
					// check if we had a conflict at this edge before
					if (conflicts.contains(c))
					{
						Conflict earlierConflict =
								conflicts.get(conflicts.indexOf(c));
						
						// add to the list of the agents who have a conflict at
						// this edge
						earlierConflict.addAgent(agent, (LocationVertex) next);
					}
					else
					{
						// add these two ids to the list of agents who have a
						// conflict at this edige
						c.addAgent(agent, (LocationVertex) next);
						c.addAgent(other, (LocationVertex) otherEdge.getV2());
						
						// add the conflict to the list
						conflicts.add(c);
					}
					
					found = true;
					break;
				}
			}
			
			// no conflict, add it to the edge mapping
			if (!found)
			{
				edges.put(agent, edge);
			}
		}
		
		return conflicts;
	}
	
}
