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

import path.elements.GraphObject;

/**
 * This is an abstract class which is the parent class of all types of verticies
 * in a given graph. It implements all of the common methods for such a vertex
 * and required child classes to implement methods necessary for use in the
 * search libraries.
 * 
 * @author Mike Johnson
 *
 */
public abstract class GraphVertex
		implements GraphObject, Comparable<GraphVertex>
{
	/**
	 * The travel cost for this {@link GraphVertex}
	 */
	protected Double		g		= null;
	
	/**
	 * The heuristic cost for this {@link GraphVertex}
	 */
	protected Double		h		= null;
	
	/**
	 * The cost of this {@link GraphVertex} for sorting purposes
	 */
	protected Double		cost	= null;
	
	/**
	 * The parent node of this {@link GraphVertex}. Null if this vertex is the
	 * root node
	 */
	protected GraphVertex	parent	= null;
	
	/**
	 * Gets the G Score of this vertex
	 * 
	 * @return
	 */
	public Double getG ()
	{
		return g;
	}
	
	/**
	 * Sets the G Score of this vertex
	 * 
	 * @param g
	 */
	public void setG (double g)
	{
		this.g = g;
	}
	
	/**
	 * Gets the H Score of this vertex
	 * 
	 * @return
	 */
	public Double getH ()
	{
		return h;
	}
	
	/**
	 * Sets the H Score of this vertex
	 * 
	 * @param h
	 */
	public void setH (double h)
	{
		this.h = h;
	}
	
	/**
	 * Returns the cost for this {@link GraphVertex}
	 * 
	 * @return cost
	 */
	public Double getCost ()
	{
		return cost;
	}
	
	/**
	 * Calculates the F Score of this vertex by summing the G and H scores
	 */
	public void setCost ()
	{
		if (g == null)
		{
			g = 0.0;
		}
		
		if (h == null)
		{
			h = 0.0;
		}
		
		cost = g + h;
	}
	
	/**
	 * Returns the parent {@link GraphVertex} of this {@link GraphVertex}, null
	 * if this vertex is the root node
	 * 
	 * @return parent
	 */
	public GraphVertex getParent ()
	{
		return parent;
	}
	
	/**
	 * Sets the parent {@link GraphVertex} of this {@link GraphVertex}
	 * 
	 * @param p
	 */
	public void setParent (GraphVertex p)
	{
		parent = p;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo (GraphVertex o)
	{
		if (cost == null || o.cost == null) { throw new IllegalStateException(
				"The cost must be set on a " + GraphVertex.class.getSimpleName()
						+ " before it can be compared."); }
		
		return Double.compare(cost, o.getCost());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public abstract int hashCode ();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public abstract boolean equals (Object o);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString ();
}
