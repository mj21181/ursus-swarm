/*******************************************************************************
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
 *******************************************************************************/
package event;

/**
 * This class serves as the base class from which all other {@link Event}s can
 * extend.
 * 
 * @author Mike Johnson
 * 
 */
public class Event implements Comparable<Event>
{
	/**
	 * The id number of the robot dispatching this event
	 */
	protected int	id	= -1;
	
	/**
	 * Constructs this {@link Event} for a given id
	 * 
	 * @param id
	 */
	public Event (int id)
	{
		this.id = id;
	}
	
	/**
	 * Gets the id of the robot which dispatched this {@link Event}
	 * 
	 * @return int the id
	 */
	public int getID ()
	{
		return id;
	}
	
	/**
	 * Sets the id of the robot which dispatched this {@link Event}
	 * 
	 * @param id The id to set
	 */
	public void setID (int id)
	{
		this.id = id;
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
		Event other = (Event) obj;
		if (id != other.id) return false;
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo (Event o)
	{
		return Integer.compare(id, o.getID());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString ()
	{
		return String.format("Event [id=%s]", id);
	}
}
