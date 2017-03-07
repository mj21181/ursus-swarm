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

public class LocationConfig implements Serializable
{
	/**
	 * Generated
	 */
	private static final long	serialVersionUID	= 2528811761780326114L;
	
	private double				latitude			= 0.0;
	private double				longitude			= 0.0;
	
	public LocationConfig ()
	{
		
	}
	
	/**
	 * @return the latitude
	 */
	public double getLatitude ()
	{
		return latitude;
	}
	
	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude (double latitude)
	{
		this.latitude = latitude;
	}
	
	/**
	 * @return the longitude
	 */
	public double getLongitude ()
	{
		return longitude;
	}
	
	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude (double longitude)
	{
		this.longitude = longitude;
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
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
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
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		LocationConfig other = (LocationConfig) obj;
		if (Double.doubleToLongBits(latitude) != Double
				.doubleToLongBits(other.latitude)) return false;
		if (Double.doubleToLongBits(longitude) != Double
				.doubleToLongBits(other.longitude)) return false;
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
		return "LocationConfig [latitude=" + latitude + ", longitude="
				+ longitude + "]";
	}
}
