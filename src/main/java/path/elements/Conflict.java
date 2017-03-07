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
package path.elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.geometry.spherical.twod.Edge;

import path.elements.vertices.GraphVertex;
import path.elements.vertices.LocationVertex;

/**
 * This class implements a conflict in {@link AgentPath}s between two or more
 * {@link Agent}s. {@link Conflict}s take two forms, either a vertex
 * {@link Conflict} - where two (or more) {@link Agent}s occupy the same
 * {@link LocationVertex} at the same timestep, or an {@link Edge} conflict -
 * where two (or more) agents occupy the same {@link Edge} at the same timestep.
 * 
 * @author Mike Johnson
 *
 */
public class Conflict implements Comparable<Conflict>
{
	/**
	 * The {@link GraphVertex} or {@link Edge} at which the conflict occurs
	 */
	protected GraphObject					o			= null;
	
	/**
	 * The timestep at which the conflict occurs
	 */
	protected int							timestep	= -1;
	
	/**
	 * The {@link Agent} IDs which are involved with the conflict
	 */
	protected Map<Integer, LocationVertex>	agents		=
			new HashMap<Integer, LocationVertex>();
	
	/**
	 * Constructs the conflict object at a specified timestep and vertex or edge
	 * 
	 * @param time
	 * @param obj
	 */
	public Conflict (int time, GraphObject obj)
	{
		o = obj;
		timestep = time;
	}
	
	/**
	 * Gets the {@link List} of agent IDs involved in the conflict
	 * 
	 * @return
	 */
	public Set<Integer> getAgents ()
	{
		return agents.keySet();
	}
	
	/**
	 * Adds an ID for an agent to the {@link List} of agents involved in the
	 * {@link Conflict}
	 * 
	 * @param a
	 */
	public void addAgent (Integer a, LocationVertex next)
	{
		agents.put(a, next);
	}
	
	public LocationVertex getNextLocation (Integer a)
	{
		return agents.get(a);
	}
	
	/**
	 * Gets the vertex or edge at which the conflict occurs
	 * 
	 * @return
	 */
	public GraphObject getGraphObject ()
	{
		return o;
	}
	
	/**
	 * Sets the vertex or edge at which the conflict occurs
	 * 
	 * @param v
	 */
	public void setGraphObject (GraphObject v)
	{
		this.o = v;
	}
	
	/**
	 * Gets the time that the conflict occurs
	 * 
	 * @return
	 */
	public int getTime ()
	{
		return timestep;
	}
	
	/**
	 * Sets the time that the conflict occurs
	 * 
	 * @param t
	 */
	public void setTime (int t)
	{
		timestep = t;
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
		result = prime * result + ( (o == null) ? 0 : o.hashCode());
		result = prime * result + timestep;
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
		Conflict other = (Conflict) obj;
		if (o == null)
		{
			if (other.o != null) return false;
		}
		else if (!o.equals(other.o)) return false;
		if (timestep != other.timestep) return false;
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo (Conflict other)
	{
		// check the timestep
		int timeVal = Integer.compare(timestep, other.timestep);
		
		if (timeVal == 0)
		{
			boolean isVertex = this.o instanceof GraphVertex;
			boolean otherIsVertex = other.o instanceof GraphVertex;
			
			// if it's the same timestep, process vertex conflicts first
			int vertexVal = Boolean.compare(otherIsVertex, isVertex);
			
			return vertexVal;
		}
		else
		{
			return timeVal;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString ()
	{
		return String.format("Conflict [o=%s, timestep=%s, agents=%s]", o,
				timestep, agents);
	}
	
}
