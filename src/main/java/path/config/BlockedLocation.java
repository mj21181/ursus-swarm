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
package path.config;

import java.io.Serializable;

public class BlockedLocation implements Serializable
{
	/**
	 * Generated
	 */
	private static final long	serialVersionUID	= -3160904365831043359L;
	
	private int					x					= 0;
	private int					y					= 0;
	
	public BlockedLocation ()
	{
		
	}
	
	/**
	 * @return the x
	 */
	public int getX ()
	{
		return x;
	}
	
	/**
	 * @param x
	 *            the x to set
	 */
	public void setX (int x)
	{
		this.x = x;
	}
	
	/**
	 * @return the y
	 */
	public int getY ()
	{
		return y;
	}
	
	/**
	 * @param y
	 *            the y to set
	 */
	public void setY (int y)
	{
		this.y = y;
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
		result = prime * result + x;
		result = prime * result + y;
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
		BlockedLocation other = (BlockedLocation) obj;
		if (x != other.x) return false;
		if (y != other.y) return false;
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
		return "BlockedLocation [x=" + x + ", y=" + y + "]";
	}
}
