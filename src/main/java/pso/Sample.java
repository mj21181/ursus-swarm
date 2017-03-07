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
package pso;

import java.util.Arrays;

/**
 * @author Mike Johnson
 * 
 */
public class Sample
{
	/**
	 * The location in the search domain of this sample
	 */
	protected int[]		sampleLocation	= null;
	
	/**
	 * The value of the fitness function at that location
	 */
	protected double	sampleValue		= Double.NaN;
	
	public Sample (double value, int... location)
	{
		sampleLocation = location;
		
		sampleValue = value;
	}
	
	/**
	 * @return the sampleLocation
	 */
	public int[] getSampleLocation ()
	{
		return sampleLocation;
	}
	
	/**
	 * @param sampleLocation the sampleLocation to set
	 */
	public void setSampleLocation (int[] sampleLocation)
	{
		this.sampleLocation = sampleLocation;
	}
	
	/**
	 * @return the sampleValue
	 */
	public double getSampleValue ()
	{
		return sampleValue;
	}
	
	/**
	 * @param sampleValue the sampleValue to set
	 */
	public void setSampleValue (double sampleValue)
	{
		this.sampleValue = sampleValue;
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
		result = prime * result + Arrays.hashCode(sampleLocation);
		long temp;
		temp = Double.doubleToLongBits(sampleValue);
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
		Sample other = (Sample) obj;
		if (!Arrays.equals(sampleLocation, other.sampleLocation)) return false;
		if (Double.doubleToLongBits(sampleValue) != Double
				.doubleToLongBits(other.sampleValue)) return false;
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
		return String.format("Sample [sampleLocation=%s, sampleValue=%s]",
				Arrays.toString(sampleLocation), sampleValue);
	}
	
}
