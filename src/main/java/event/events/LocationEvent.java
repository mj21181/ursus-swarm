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
package event.events;

import java.util.Arrays;

import event.Event;

/**
 * @author Mike Johnson
 * 
 */
public class LocationEvent extends Event
{
	protected int[]	location	= null;
	
	/**
	 * @param id
	 */
	public LocationEvent (int id, int... loc)
	{
		super(id);
		
		location = loc;
	}
	
	/**
	 * @return the location
	 */
	public int[] getLocation ()
	{
		return location;
	}
	
	/**
	 * @param location the location to set
	 */
	public void setLocation (int[] location)
	{
		this.location = location;
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
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(location);
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
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		LocationEvent other = (LocationEvent) obj;
		if (!Arrays.equals(location, other.location)) return false;
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
		return String.format("LocationEvent [location=%s]",
				Arrays.toString(location));
	}
	
}
