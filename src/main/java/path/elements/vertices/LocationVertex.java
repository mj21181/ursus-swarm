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
import java.util.List;

/**
 * This class is a representation of a location in a uniform cost grid at a
 * specified time. It extends the {@link GraphVertex} parent class.
 * 
 * @author Mike Johnson
 *
 */
public class LocationVertex extends GraphVertex
{
	/**
	 * The X location in the grid
	 */
	protected int	x	= -1;
	
	/**
	 * The Y location in the grid
	 */
	protected int	y	= -1;
	
	/**
	 * The time at which the agent has reached this vertex
	 */
	protected int	t	= -1;
	
	/**
	 * Constructs a {@link LocationVertex} object at a specified x index, y
	 * index, and time
	 * 
	 * @param x
	 * @param y
	 * @param t
	 */
	public LocationVertex (int x, int y, int t)
	{
		this.x = x;
		this.y = y;
		this.t = t;
	}
	
	/**
	 * A Copy Constructor for a given {@link LocationVertex}
	 * 
	 * @param l
	 */
	public LocationVertex (LocationVertex l)
	{
		this.x = l.x;
		this.y = l.y;
		this.t = l.t;
		
		this.parent = l.parent;
		
		if (l.g == null)
		{
			this.g = 0.0;
		}
		else
		{
			this.g = new Double(l.g);
		}
		
		if (l.h == null)
		{
			this.h = 0.0;
		}
		else
		{
			this.h = new Double(l.h);
		}
		setCost();
	}
	
	/**
	 * Gets a {@link List} of the children {@link LocationVertex} objects of
	 * this {@link LocationVertex}
	 * 
	 * @return
	 */
	public List<LocationVertex> getChildren ()
	{
		ArrayList<LocationVertex> kids = new ArrayList<LocationVertex>();
		
		// gets the neighboring locations for an 8-connect grid
		// + 1 for waiting one timestep at the current location
		for (int i = -1; i < 2; i++ )
		{
			for (int j = -1; j < 2; j++ )
			{
				kids.add(new LocationVertex(x + i, y + j, t + 1));
			}
		}
		
		// think of the children!!
		return kids;
	}
	
	/**
	 * Gets the X index of this location
	 * 
	 * @return
	 */
	public int getX ()
	{
		return x;
	}
	
	/**
	 * Sets the X index of this location
	 * 
	 * @param x
	 */
	public void setX (int x)
	{
		this.x = x;
	}
	
	/**
	 * Gets the Y index of this location
	 * 
	 * @return
	 */
	public int getY ()
	{
		return y;
	}
	
	/**
	 * Sets the Y index of this location
	 * 
	 * @param y
	 */
	public void setY (int y)
	{
		this.y = y;
	}
	
	/**
	 * Gets the current timestep of this location
	 * 
	 * @return
	 */
	public int getT ()
	{
		return t;
	}
	
	/**
	 * Sets the current timestep of this location
	 * 
	 * @param t
	 */
	public void setT (int t)
	{
		this.t = t;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.GraphVertex#hashCode()
	 */
	@Override
	public int hashCode ()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + t;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.GraphVertex#equals(java.lang.Object)
	 */
	@Override
	public boolean equals (Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		LocationVertex other = (LocationVertex) obj;
		if (x != other.x) return false;
		if (y != other.y) return false;
		return true;
	}
	
	/**
	 * A .equals method which includes the time field of this
	 * {@link LocationVertex}
	 * 
	 * @param v
	 * @return
	 */
	public boolean equalsWithTime (LocationVertex v)
	{
		if (!this.equals(v)) { return false; }
		
		if (t != v.t) { return false; }
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.GraphVertex#toString()
	 */
	@Override
	public String toString ()
	{
		return String.format("LocationVertex [x=%s, y=%s, t=%s]", x, y, t);
	}
	
}
