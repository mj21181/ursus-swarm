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
import java.util.Collections;

import path.elements.vertices.GraphVertex;

/**
 * This class is responsible for handling common methods associated with the
 * path of a given {@link Agent}. This path is a series of {@link GraphVertex}
 * objects from the start vertex to the goal vertex.
 * 
 * Each index in the {@link AgentPath} corresponds to a specified time in the
 * {@link Agent}'s movement. Index 0 is the start {@link GraphVertex}, and index
 * size-1 is the goal vertex.
 * 
 * @author Mike Johnson
 *
 */
public class AgentPath
{
	/**
	 * The data structure containing the series of {@link GraphVertex} objects.
	 */
	protected ArrayList<GraphVertex> path = new ArrayList<GraphVertex>();
	
	/**
	 * Copies this {@link AgentPath} and returns an identical object
	 * 
	 * @return
	 */
	public AgentPath copy ()
	{
		AgentPath copy = new AgentPath();
		
		for (GraphVertex v : path)
		{
			copy.path.add(v);
		}
		
		return copy;
	}
	
	/**
	 * Gets a specified {@link GraphVertex} at a certain time. Returns null if
	 * no vertex exists at that timestep
	 * 
	 * @param time
	 * @return
	 */
	public GraphVertex vertexAtTime (int time)
	{
		if (time < 0 || time >= path.size()) { return null; }
		
		return path.get(time);
	}
	
	/**
	 * Gets the number of verticies in the {@link AgentPath}
	 * 
	 * @return
	 */
	public int getLength ()
	{
		return path.size();
	}
	
	/**
	 * This method takes a goal {@link GraphVertex} and reconstructs the path by
	 * calling getParent repeatedly until the start {@link GraphVertex} is
	 * found.
	 * 
	 * The resulting chain is the {@link AgentPath}.
	 * 
	 * @param v
	 */
	public void reconstruct (GraphVertex v)
	{
		GraphVertex parent = v.getParent();
		
		path.add(v);
		
		while (parent != null)
		{
			path.add(parent);
			
			parent = parent.getParent();
		}
		
		Collections.reverse(path);
	}
	
	/**
	 * Returns the {@link AgentPath} as an {@link ArrayList}.
	 * 
	 * @return
	 */
	public ArrayList<GraphVertex> getAsList ()
	{
		return path;
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
		result = prime * result + ( (path == null) ? 0 : path.hashCode());
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
		AgentPath other = (AgentPath) obj;
		if (path == null)
		{
			if (other.path != null)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		
		if (path.size() != other.path.size()) return false;
		
		for (int i = 0; i < path.size(); i++ )
		{
			if (!path.get(i).equals(other.path.get(i))) { return false; }
		}
		
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
		StringBuilder sb = new StringBuilder();
		
		sb.append("Agent Path: ");
		sb.append(System.lineSeparator());
		
		for (GraphVertex v : path)
		{
			sb.append(v.toString());
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
}
