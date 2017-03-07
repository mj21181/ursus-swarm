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

import path.elements.vertices.LocationVertex;

/**
 * Constructs an {@link Agent} object which is exploring the map. Each has a
 * specified start location on the map and a target location and an id number.
 * When the agent is constructed the time parameter of the target location is
 * ignored since that is not known until the search is complete.
 * 
 * @author Mike Johnson
 * 
 */
public class Agent
{
	/**
	 * The id number of this agent
	 */
	protected int				id		= -1;
	
	/**
	 * The start location of this agent
	 */
	protected LocationVertex	start	= null;
	
	/**
	 * The target location of this agent
	 */
	protected LocationVertex	target	= null;
	
	/**
	 * Constructs an {@link Agent} object from explicit parameters
	 * 
	 * @param id
	 * @param start
	 * @param target
	 */
	public Agent (int id, LocationVertex start, LocationVertex target)
	{
		this.id = id;
		this.start = start;
		this.target = target;
	}
	
	/**
	 * Copy constructor for an {@link Agent}
	 * 
	 * @param a
	 */
	public Agent (Agent a)
	{
		this.id = a.id;
		this.start = new LocationVertex(a.start);
		this.target = new LocationVertex(a.target);
	}
	
	/**
	 * Gets the ID of this agent
	 * 
	 * @return the id
	 */
	public int getId ()
	{
		return id;
	}
	
	/**
	 * Sets the ID of this agent
	 * 
	 * @param id the id to set
	 */
	public void setId (int id)
	{
		this.id = id;
	}
	
	/**
	 * Gets the start {@link LocationVertex} of this agent
	 * 
	 * @return the start
	 */
	public LocationVertex getStart ()
	{
		return start;
	}
	
	/**
	 * Sets the start {@link LocationVertex} of this agent
	 * 
	 * @param start the start to set
	 */
	public void setStart (LocationVertex start)
	{
		this.start = start;
	}
	
	/**
	 * Gets the target {@link LocationVertex} of this agent
	 * 
	 * @return the target
	 */
	public LocationVertex getTarget ()
	{
		return target;
	}
	
	/**
	 * Sets the target {@link LocationVertex} of this agent
	 * 
	 * @param target the target to set
	 */
	public void setTarget (LocationVertex target)
	{
		this.target = target;
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
		result = prime * result + id;
		// result = prime * result + ( (start == null) ? 0 : start.hashCode());
		// result = prime * result + ( (target == null) ? 0 :
		// target.hashCode());
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
		Agent other = (Agent) obj;
		if (id != other.id) return false;
		if (start == null)
		{
			if (other.start != null) return false;
		}
		else if (!start.equals(other.start)) { return false; }
		if (target == null)
		{
			if (other.target != null) return false;
		}
		else if (!target.equals(other.target)) { return false; }
		
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
		return String.format("Agent [id=%s, start=%s, target=%s]", id, start,
				target);
	}
	
}
