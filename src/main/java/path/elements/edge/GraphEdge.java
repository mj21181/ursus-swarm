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
package path.elements.edge;

import path.elements.GraphObject;
import path.elements.vertices.GraphVertex;

/**
 * This class represents an edge on a given Graph, a transition between two
 * graph verticies which are represented by {@link GraphVertex} objects.
 * 
 * @author Mike Johnson
 *
 */
public class GraphEdge implements GraphObject
{
	/**
	 * The first {@link GraphVertex} of the {@link GraphEdge}
	 */
	protected GraphVertex	v1	= null;
	
	/**
	 * The second {@link GraphVertex} of the {@link GraphEdge}
	 */
	protected GraphVertex	v2	= null;
	
	/**
	 * Constructs a {@link GraphEdge} between the two specified vertices
	 * 
	 * @param one
	 * @param two
	 */
	public GraphEdge (GraphVertex one, GraphVertex two)
	{
		v1 = one;
		v2 = two;
	}
	
	/**
	 * Gets the first {@link GraphVertex} of the edge
	 * 
	 * @return
	 */
	public GraphVertex getV1 ()
	{
		return v1;
	}
	
	/**
	 * Sets the first {@link GraphVertex} of the edge
	 * 
	 * @param v1
	 */
	public void setV1 (GraphVertex v1)
	{
		this.v1 = v1;
	}
	
	/**
	 * Gets the second {@link GraphVertex} of the edge
	 * 
	 * @return
	 */
	public GraphVertex getV2 ()
	{
		return v2;
	}
	
	/**
	 * Sets the second {@link GraphVertex} of the edge
	 * 
	 * @param v2
	 */
	public void setV2 (GraphVertex v2)
	{
		this.v2 = v2;
	}
	
	/**
	 * Swaps the first and second {@link GraphVertex} objects
	 */
	public void swap ()
	{
		GraphVertex tmp = v1;
		v1 = v2;
		v2 = tmp;
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
		result = prime * result + ( (v1 == null) ? 0 : v1.hashCode());
		result = prime * result + ( (v2 == null) ? 0 : v2.hashCode());
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
		GraphEdge other = (GraphEdge) obj;
		
		if (v1 == null)
		{
			if (other.v1 != null) return false;
			
			if (v2 == null) { return true; }
			
			if (v2.equals(other.v2))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		
		if (v2 == null)
		{
			if (other.v2 != null) return false;
			
			if (v1.equals(other.v1))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		
		if (v1.equals(other.v1) && v2.equals(other.v2))
		{
			return true;
		}
		else if (v1.equals(other.v2) && v2.equals(other.v1))
		{
			return true;
		}
		else
		{
			return false;
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
		return String.format("GraphEdge [v1=%s, v2=%s]", v1, v2);
	}
}
