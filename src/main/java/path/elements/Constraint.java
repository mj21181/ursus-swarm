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

import java.util.ArrayList;
import java.util.List;

import path.elements.vertices.LocationVertex;

/**
 * This class specifies a constraint on the search of a uniform cost grid by
 * another {@link Agent}. This specifies which {@link Agent} cannot occupy a
 * certain {@link LocationVertex} at a certain time.
 * 
 * @author Mike Johnson
 *
 */
public class Constraint
{
	/**
	 * The ID number of the {@link Agent} being constrained
	 */
	protected Integer			agent	= null;
	
	/**
	 * The {@link LocationVertex} where said {@link Agent} cannot travel at a
	 * certain time.
	 */
	protected LocationVertex	v		= null;
	
	/**
	 * The next {@link LocationVertex} where the {@link Agent} cannot travel at
	 * a certain time
	 */
	protected List<LocationVertex>	nv		= null;
	
	/**
	 * Constructor for constraints based on other agents
	 * 
	 * @param agent
	 * @param v
	 * @param nv
	 */
	public Constraint (int agent, LocationVertex v)
	{
		this.agent = agent;
		this.v = v;
		this.nv = new ArrayList<LocationVertex>();
	}
	
	public void addNextVertices (List<LocationVertex> nvs)
	{
		nv.addAll(nvs);
	}
	
	/**
	 * Returns true if this constraint applies to the specified
	 * {@link LocationVertex}. False if it does not
	 * 
	 * @param ver
	 * @return
	 */
	public boolean applies (LocationVertex ver)
	{
		// check the vertex is the one in question
		// if it's an agent constraint it applies to a specific timestep
		if (v.equalsWithTime(ver)) { return true; }
		
		// otherwise constraint does not apply
		return false;
	}
	
	/**
	 * @return the agent
	 */
	public Integer getAgent ()
	{
		return agent;
	}
	
	/**
	 * @param agent the agent to set
	 */
	public void setAgent (Integer agent)
	{
		this.agent = agent;
	}
	
	/**
	 * @return the v
	 */
	public LocationVertex getVertex ()
	{
		return v;
	}
	
	/**
	 * @param v the v to set
	 */
	public void setVertex (LocationVertex v)
	{
		this.v = v;
	}
	
	public List<LocationVertex> getNextVertices ()
	{
		return nv;
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
		result = prime * result + ( (agent == null) ? 0 : agent.hashCode());
		result = prime * result + ( (v == null) ? 0 : v.hashCode());
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
		Constraint other = (Constraint) obj;
		if (agent == null)
		{
			if (other.agent != null) return false;
		}
		else if (!agent.equals(other.agent)) return false;
		if (v == null)
		{
			if (other.v != null) return false;
		}
		else if (!v.equalsWithTime(other.v)) return false;
		
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
		return String.format("Constraint [agent=%s, v=%s, nv=%s]", agent, v,
				nv);
	}
	
}
