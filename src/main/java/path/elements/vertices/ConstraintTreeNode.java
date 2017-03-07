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
package path.elements.vertices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import path.GraphSearchEngine;
import path.SearchPerformance;
import path.config.SingleAgentConfig;
import path.elements.Agent;
import path.elements.AgentPath;
import path.elements.Conflict;
import path.elements.Constraint;
import path.elements.JumpPointConstraint;
import path.elements.map.ObstacleMap;
import path.search.multi.CBS;
import path.search.multi.SolutionValidator;
import log.ApplicationLogger;

/**
 * This class is an implementation of a {@link GraphVertex} in a Constraint Tree
 * for {@link CBS}.
 * 
 * @author Mike Johnson
 * 
 */
public class ConstraintTreeNode extends GraphVertex
{
	// TODO: configurable?
	private final boolean								JPS_ON					=
			true;
	
	/**
	 * Each node has one {@link Constraint} in the Constraint Tree which belongs
	 * to one agent. The root of the tree has an empty set of constraints or the
	 * set of constraints from obstacles. A child {@link ConstraintTreeNode}
	 * inherits the constraints of the parent and adds one new constraint for
	 * one agent.
	 */
	protected Constraint								nodeConstraint			=
			null;
	
	/**
	 * Each node has a solution to the MAPF instance which consists of K number
	 * of {@link AgentPath}s. The Path in this set for a given agent i must be
	 * consistent with all of the {@link Constraint}s in the set of constraints
	 * for agent i. These {@link AgentPath}s will be found using a Single Agent
	 * Search.
	 */
	protected Map<Agent, AgentPath>						solution				=
			null;
	
	protected Map<Agent, SearchPerformance>				performance				=
			null;
	
	protected Map<Integer, List<JumpPointConstraint>>	jumpPointConstraints	=
			null;
	
	/**
	 * A shortcut reference to the logger
	 */
	protected ApplicationLogger							log						=
			ApplicationLogger.getInstance();
	
	/**
	 * Default Constructor
	 */
	public ConstraintTreeNode ()
	{
		super();
	}
	
	/**
	 * Gets this {@link ConstraintTreeNode}'s {@link Constraint} so that a child
	 * node can build its set of {@link Constraint}s by traversing the
	 * Constraint Tree and calling this method on its parent.
	 * 
	 * @return
	 */
	public Constraint getNodeConstraint ()
	{
		return nodeConstraint;
	}
	
	/**
	 * Sets the constraint for this node
	 * 
	 * @param c
	 */
	public void setNodeConstraint (Constraint c)
	{
		nodeConstraint = c;
	}
	
	/**
	 * Gets this {@link ConstraintTreeNode}'s solution.
	 * 
	 * @return
	 */
	public Map<Agent, AgentPath> getSolution ()
	{
		return solution;
	}
	
	/**
	 * Gets this {@link ConstraintTreeNode}'s performance stats.
	 * 
	 * @return
	 */
	public Map<Agent, SearchPerformance> getPerformance ()
	{
		return performance;
	}
	
	public Map<Integer, List<JumpPointConstraint>> getJumpPointConstraints ()
	{
		return jumpPointConstraints;
	}
	
	/**
	 * Sets the solution for this {@link ConstraintTreeNode}. This method copies
	 * the solution passed in so that solutions between nodes are independent
	 * and not modified when they are updated.
	 * 
	 * @param sol
	 */
	public void setSolution (Map<Agent, AgentPath> sol)
	{
		// for .equals()
		if (sol == null)
		{
			solution = null;
			
			return;
		}
		
		solution = new HashMap<Agent, AgentPath>(sol.size());
		
		for (Agent a : sol.keySet())
		{
			solution.put(new Agent(a), sol.get(a).copy());
		}
	}
	
	public void setJumpPointConstraints (
			Map<Integer, List<JumpPointConstraint>> cons)
	{
		if (cons == null)
		{
			jumpPointConstraints = null;
			
			return;
		}
		
		jumpPointConstraints =
				new HashMap<Integer, List<JumpPointConstraint>>();
		
		for (Integer a : cons.keySet())
		{
			List<JumpPointConstraint> current = cons.get(a);
			
			ArrayList<JumpPointConstraint> copy =
					new ArrayList<JumpPointConstraint>(current.size());
			
			for (JumpPointConstraint c : current)
			{
				copy.add(c.copy());
			}
			
			jumpPointConstraints.put(new Integer(a), copy);
		}
	}
	
	/**
	 * Sets the performance stats for this {@link ConstraintTreeNode}
	 * 
	 * @param perf
	 */
	public void setPerformanceStats (Map<Agent, SearchPerformance> perf)
	{
		if (perf == null)
		{
			performance = null;
			
			return;
		}
		
		performance = new HashMap<Agent, SearchPerformance>(perf.size());
		
		log.logDebug("Copying Stats:");
		for (Agent a : perf.keySet())
		{
			SearchPerformance sp = new SearchPerformance();
			
			SearchPerformance tmp = perf.get(a);
			
			sp.setHighLevel(tmp.isHighLevel());
			sp.setNumberOfNodesExpanded(tmp.getNumberOfNodesExpanded());
			sp.setSolutionLength(tmp.getSolutionLength());
			sp.setTimeToRunInMilliseconds(tmp.getTimeToRunInMilliseconds());
			
			log.logDebug(sp.toString());
			
			performance.put(new Agent(a), sp);
		}
	}
	
	/**
	 * Used to initialize the solution of the Root {@link ConstraintTreeNode}.
	 * For each {@link Agent} in the provided {@link List} and the given
	 * {@link ObstacleMap}, a set of paths are generated using the low level
	 * search algorithm.
	 * 
	 * Throws an {@link IllegalStateException} if a path does not exist for one
	 * of the agents.
	 * 
	 * @param agents
	 * @param obs
	 * @throws IllegalStateException
	 */
	public void generateSolution (ArrayList<Agent> agents, ObstacleMap obs,
			boolean debug) throws IllegalStateException
	{
		solution = new HashMap<Agent, AgentPath>();
		performance = new HashMap<Agent, SearchPerformance>();
		jumpPointConstraints =
				new HashMap<Integer, List<JumpPointConstraint>>();
		
		log.logDebug("Generating Solutions for %d Agents.", agents.size());
		
		for (Agent a : agents)
		{
			// since this is the first time we are running this search, we do
			// not want to care about the final timestep for each unit.
			a.getTarget().setT(-1);
			
			AgentPath pth = runLowLevelSearch(a, obs, debug);
			
			solution.put(a, pth);
		}
		
		log.logDebug("All Solutions Generated for %d Agents.", agents.size());
		
		int lastTime = getLastTimestep();
		
		padSolutionToTimestep(lastTime);
		
		for (Agent a : solution.keySet())
		{
			if (debug)
			{
				log.log("Generating jump point constraints for Agent "
						+ a.getId());
			}
			
			AgentPath pth = solution.get(a);
			
			List<JumpPointConstraint> cons =
					buildJumpPointConstraintsFromPath(pth);
			
			if (debug)
			{
				StringBuilder sb = new StringBuilder();
				
				sb.append("Constraints for Agent " + a.getId() + ": "
						+ System.lineSeparator());
				for (JumpPointConstraint c : cons)
				{
					sb.append(c.toString());
					sb.append(System.lineSeparator());
				}
				
				log.log(sb.toString());
			}
			
			jumpPointConstraints.put(a.getId(), cons);
		}
		
		if (debug)
		{
			log.log("Constraints Size: " + jumpPointConstraints.size());
		}
	}
	
	/**
	 * Except for the root {@link ConstraintTreeNode}, only the Single Agent
	 * Search needs to be performed for the agent associated with the new
	 * {@link Constraint}. The {@link AgentPath}s of the other agents remain the
	 * same.
	 * 
	 * @param obs
	 * @throws IllegalStateException if there is no agent in the solution for
	 *             this node's agent, or if the path doesn't exist for this
	 *             agent
	 */
	public boolean updateSolution (ObstacleMap obs, boolean debug)
			throws IllegalStateException
	{
		int id = nodeConstraint.getAgent();
		
		Agent toUpdate = null;
		
		for (Agent a : solution.keySet())
		{
			if (a.getId() == id)
			{
				toUpdate = a;
			}
		}
		
		if (toUpdate == null) { throw new IllegalStateException(
				"No agent exists for id: " + id); }
		
		if (debug)
		{
			log.log("Replanning for " + toUpdate.toString());
		}
		
		int lastTimestep = getLastTimestep();
		
		// we care about when the search is finished
		toUpdate.getTarget().setT(lastTimestep);
		
		AgentPath pth = runLowLevelSearch(toUpdate, obs, debug);
		
		if (pth == null) { return false; }
		
		// log.logDebug("Planned Path: %s", pth.toString());
		
		// remove the old entry
		
		Agent toRemove = null;
		for (Agent a : solution.keySet())
		{
			if (a.getId() == id)
			{
				toRemove = a;
			}
		}
		
		if (toRemove != null)
		{
			// System.out.println("1sol size: " + solution.keySet().size());
			// System.out.println("removing agent: " + toRemove.toString());
			
			solution.remove(toRemove);
			jumpPointConstraints.remove(toRemove);
			// System.out.println("2sol size: " + solution.keySet().size());
		}
		
		if (debug)
		{
			log.log("Putting agent " + toUpdate.toString());
		}
		
		solution.put(toUpdate, pth);
		
		// System.out.println("3sol size: " + solution.keySet().size());
		
		lastTimestep = getLastTimestep();
		
		padSolutionToTimestep(lastTimestep);
		
		if (debug)
		{
			log.log("Updating jump point constraints for Agent "
					+ toUpdate.getId());
		}
		
		List<JumpPointConstraint> cons =
				buildJumpPointConstraintsFromPath(solution.get(toUpdate));
		
		jumpPointConstraints.put(toUpdate.getId(), cons);
		
		return true;
	}
	
	/**
	 * Runs the low level search algorithm for the given {@link Agent} and
	 * {@link ObstacleMap}. Returns the {@link AgentPath} which was found by the
	 * algorithm.
	 * 
	 * @param a
	 * @param obs
	 * @return
	 */
	protected AgentPath runLowLevelSearch (Agent a, ObstacleMap obs,
			boolean debug)
	{
		LocationVertex s = a.getStart();
		LocationVertex t = a.getTarget();
		
		log.logDebug(
				"Running Low Level search for Agent: %d Start: %s Target %s",
				a.getId(), s, t);
		
		ArrayList<Constraint> constraints = buildConstraintList(this);
		ArrayList<JumpPointConstraint> jmpConstraints =
				buildJumpPointConstraintList(a.getId());
		
		if (debug)
		{
			log.log("Constraints: %s", Arrays.toString(constraints.toArray()));
		}
		
		if (debug)
		{
			StringBuilder sb = new StringBuilder();
			
			sb.append("Jump Point Constraints for Agent " + a.getId() + ": "
					+ System.lineSeparator());
			for (JumpPointConstraint c : jmpConstraints)
			{
				sb.append(c.toString());
				sb.append(System.lineSeparator());
			}
			
			log.log(sb.toString());
		}
		
		SingleAgentConfig conf =
				new SingleAgentConfig(a.getId(), s.getX(), s.getY(), s.getT(),
						t.getX(), t.getY(), t.getT(), JPS_ON, obs, constraints);
		
		conf.setDebug(debug);
		conf.setJumpPointConstraints(jmpConstraints);
		
		if (debug)
		{
			log.log("Configuration: %s", conf.toString());
		}
		
		GraphSearchEngine engine = new GraphSearchEngine(conf);
		
		AgentPath path = (AgentPath) engine.plan();
		
		if (path != null)
		{
			log.logDebug(engine.getPerformanceStats().toString());
			
			performance.put(a, engine.getPerformanceStats());
			
			if (debug)
			{
				log.log("Path Planned for : " + a.getId());
				log.log(path.toString());
			}
		}
		
		// if (path == null) { throw new IllegalStateException(
		// "Error: Agent " + a.toString() + " has no AgentPath"); }
		
		return path;
	}
	
	public ArrayList<Constraint> buildConstraintList (ConstraintTreeNode node)
	{
		ArrayList<Constraint> constraints = new ArrayList<Constraint>();
		
		ConstraintTreeNode tmp = node;
		
		while (tmp != null)
		{
			if (tmp.getNodeConstraint() != null)
			{
				constraints.add(tmp.getNodeConstraint());
			}
			
			tmp = (ConstraintTreeNode) tmp.getParent();
		}
		
		return constraints;
	}
	
	public ArrayList<JumpPointConstraint> buildJumpPointConstraintList (int id)
	{
		ArrayList<JumpPointConstraint> cons =
				new ArrayList<JumpPointConstraint>();
		
		for (Integer a : jumpPointConstraints.keySet())
		{
			if (a.intValue() != id)
			{
				cons.addAll(jumpPointConstraints.get(a));
			}
		}
		
		return cons;
	}
	
	/**
	 * Iterates through each path in the solution checking the length of each.
	 * Returns the size of the largest path.
	 * 
	 * @param sol
	 * @return
	 */
	public int getLastTimestep ()
	{
		int largest = 0;
		
		for (AgentPath path : solution.values())
		{
			if (path.getLength() > largest)
			{
				largest = path.getLength();
			}
		}
		
		// time is indexed from 0
		return largest - 1;
	}
	
	/**
	 * Checks the delta value and returns the direction it is headed
	 * 
	 * @param delta
	 * @return
	 */
	protected int getDirection (int delta)
	{
		return ( (delta < 0) ? -1 : (delta > 0) ? 1 : 0);
	}
	
	protected List<JumpPointConstraint>
			buildJumpPointConstraintsFromPath (AgentPath p)
	{
		ArrayList<JumpPointConstraint> constraints =
				new ArrayList<JumpPointConstraint>();
		
		// no constraints will be added if there is only one vertex in path
		if (p.getLength() <= 1) { return constraints; }
		
		Iterator<GraphVertex> it = p.getAsList().iterator();
		
		LocationVertex currentVertex = (LocationVertex) it.next();
		LocationVertex nextVertex = null;
		
		int lastDirectionX = 0;
		int lastDirectionY = 0;
		
		LocationVertex lastJumpPoint = null;
		
		while (it.hasNext())
		{
			nextVertex = (LocationVertex) it.next();
			
			int dx = nextVertex.getX() - currentVertex.getX();
			int dy = nextVertex.getY() - currentVertex.getY();
			
			// on initialization
			if (lastJumpPoint == null)
			{
				lastJumpPoint = currentVertex;
				
				currentVertex.setT(0);
				
				lastDirectionX = getDirection(dx);
				lastDirectionY = getDirection(dy);
			}
			else if (lastDirectionX == getDirection(dx)
					&& lastDirectionY == getDirection(dy))
			{
				// do nothing
			}
			else
			{
				// we found a new jump point because the direction changed
				JumpPointConstraint con =
						new JumpPointConstraint(lastJumpPoint, currentVertex);
				
				constraints.add(con);
				
				lastDirectionX = dx;
				lastDirectionY = dy;
				
				lastJumpPoint = currentVertex;
			}
			
			currentVertex = nextVertex;
		}
		
		// add one for the goal node
		JumpPointConstraint con =
				new JumpPointConstraint(lastJumpPoint, currentVertex);
		
		constraints.add(con);
		
		// add one for the end of the simulation
		int lastTimestep = getLastTimestep();
		
		LocationVertex end = new LocationVertex(currentVertex.getX(),
				currentVertex.getY(), lastTimestep);
		
		JumpPointConstraint conEnd =
				new JumpPointConstraint(currentVertex, end);
		
		constraints.add(conEnd);
		
		return constraints;
	}
	
	protected void padSolutionToTimestep (int timestep)
	{
		log.logDebug("Padding Solution to timestep: " + timestep);
		
		// check vertices at this timestep
		for (Agent agent : solution.keySet())
		{
			AgentPath path = solution.get(agent);
			
			SearchPerformance perf = null;
			if (performance != null)
			{
				perf = performance.get(agent);
			}
			
			if (path.getLength() <= timestep)
			{
				log.logDebug("Path length " + path.getLength() + " is <= "
						+ timestep);
				
				int count = path.getLength();
				
				while (count < timestep + 1)
				{
					LocationVertex v =
							(LocationVertex) path.vertexAtTime(count - 1);
					
					LocationVertex copy = new LocationVertex(v);
					
					copy.setT(count);
					
					// log.logDebug("Adding : " + copy.toString());
					
					path.getAsList().add(copy);
					
					count++ ;
				}
			}
			
			if (perf != null)
			{
				perf.setSolutionLength(path.getLength());
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo (GraphVertex o)
	{
		// if a tie exists in the cost, the solution with fewer conflicts is
		// selected.
		// if a tie still exists, FIFO
		
		int val = Double.compare(cost, o.getCost());
		
		if (val == 0)
		{
			SolutionValidator sv = new SolutionValidator();
			
			// TODO: Make more efficient
			Queue<Conflict> conflictQ = sv.validateSolution(this);
			Queue<Conflict> conflictQOther =
					sv.validateSolution((ConstraintTreeNode) o);
			
			int tieVal = new Integer(conflictQ.size())
					.compareTo(conflictQOther.size());
			
			return tieVal;
		}
		
		return val;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode ()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ( (nodeConstraint == null) ? 0 : nodeConstraint.hashCode());
		result = prime * result
				+ ( (solution == null) ? 0 : solution.hashCode());
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals (Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ConstraintTreeNode other = (ConstraintTreeNode) obj;
		
		if (nodeConstraint == null)
		{
			if (other.nodeConstraint != null) return false;
		}
		else if (!nodeConstraint.equals(other.nodeConstraint)) return false;
		if (solution == null)
		{
			if (other.solution != null) return false;
		}
		else if (!solution.equals(other.solution)) return false;
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString ()
	{
		return String.format(
				"ConstraintTreeNode [nodeConstraint=%s, solutionExists=%s, cost=%f]",
				nodeConstraint, (solution == null) ? false : true,
				cost.doubleValue());
	}
	
}
