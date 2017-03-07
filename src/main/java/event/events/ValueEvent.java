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

import event.Event;

/**
 * @author Mike Johnson
 * 
 */
public class ValueEvent extends Event
{
	protected double	value	= Double.NaN;
	
	/**
	 * @param id
	 */
	public ValueEvent (int id, double val)
	{
		super(id);
		
		value = val;
	}
	
	/**
	 * @return the value
	 */
	public double getValue ()
	{
		return value;
	}
	
	/**
	 * @param value the value to set
	 */
	public void setValue (double value)
	{
		this.value = value;
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
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		ValueEvent other = (ValueEvent) obj;
		if (Double.doubleToLongBits(value) != Double
				.doubleToLongBits(other.value)) return false;
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
		return String.format("ValueEvent [value=%s]", value);
	}
	
}
