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
package pso.implementation.random;

import java.io.Serializable;

import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.MersenneTwister;

/**
 * 
 * @author Mike Johnson
 * 
 */
public class Random implements Serializable
{
	
	/**
	 * Generated
	 */
	private static final long	serialVersionUID	= 1180217034509103040L;
	
	private MersenneTwister		mt;
	private int					seed;
	private double				lowerBound;
	private double				upperBound;
	
	public Random ()
	{
		
		seed = 1;
		mt = new MersenneTwister(1);
		lowerBound = 0.0;
		upperBound = 1.0;
	}
	
	public Random (int seed, double lb, double ub)
	{
		
		mt = new MersenneTwister(seed);
		this.seed = seed;
		lowerBound = lb;
		upperBound = ub;
	}
	
	public Random (Random rand)
	{
		
		this.mt = rand.mt;
		this.seed = rand.seed;
		this.lowerBound = rand.lowerBound;
		this.upperBound = rand.upperBound;
	}
	
	public MersenneTwister getMT ()
	{
		return mt;
	}
	
	public int getSeed ()
	{
		
		return seed;
	}
	
	public double getLowerBound ()
	{
		return lowerBound;
	}
	
	public double getUpperBound ()
	{
		return upperBound;
	}
	
	public void setLowerBound (double lb)
	{
		lowerBound = lb;
	}
	
	public void setUpperBound (double ub)
	{
		upperBound = ub;
	}
	
	public void setSeed (int seed)
	{
		
		this.seed = seed;
		mt.setSeed(seed);
	}
	
	public void setMT (MersenneTwister mt)
	{
		this.mt = mt;
	}
	
	public double randomDouble ()
	{
		return (upperBound - lowerBound) * mt.nextDouble() + lowerBound;
	}
	
	public int randomInt ()
	{
		// calculate the range that we are remapping our integers to
		double span = upperBound - lowerBound;
		
		// System.out.println("Span: " + span);
		int genRaw = mt.nextInt();
		
//		System.out.println("GenRaw: " + genRaw);
		
		// we are going to remap the generated integer onto the unsigned integer
		// range
		long gen = (long) genRaw;
		
//		System.out.println("Gen: " + gen);
		
		// the maximum of the unsigned integer range
		long max = 2 * (long) Integer.MAX_VALUE;
		
//		System.out.println("Max: " + max);
		
		double frac = gen + (long) Integer.MAX_VALUE + 1;
//		double hgf = Integer.MIN_VALUE;
//		System.out.println("Added: " + (long) Integer.MAX_VALUE + 1);
//		System.out.println("frac: " + frac);
//		System.out.println("GenLong: " + (gen + (long) Integer.MAX_VALUE + 1));
		
		double mult = span * (frac / max);
		
		// System.out.println("Mult: " + mult);
		
		double val = mult + lowerBound;
		
		return (int) Math.floor(val);
	}
	
	public AbstractRealMatrix randomMatrix (int rows, int cols)
	{
		
		AbstractRealMatrix m = new Array2DRowRealMatrix(rows, cols);
		
		for (int i = 0; i < rows; i++ )
		{
			for (int j = 0; j < cols; j++ )
			{
				m.setEntry(i, j, randomDouble());
			}
		}
		
		return m;
	}
	
	public RealVector randomVector (int size)
	{
		RealVector v = new ArrayRealVector(size);
		
		for (int i = 0; i < size; i++ )
		{
			v.setEntry(i, randomDouble());
		}
		
		return v;
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
		temp = Double.doubleToLongBits(lowerBound);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + seed;
		temp = Double.doubleToLongBits(upperBound);
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
		Random other = (Random) obj;
		if (Double.doubleToLongBits(lowerBound) != Double
				.doubleToLongBits(other.lowerBound)) return false;
		if (mt == null)
		{
			if (other.mt != null) return false;
		}
		if (seed != other.seed) return false;
		if (Double.doubleToLongBits(upperBound) != Double
				.doubleToLongBits(other.upperBound)) return false;
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
		return String.format(
				"Random [mt=%s, seed=%s, lowerBound=%s, upperBound=%s]", mt,
				seed, lowerBound, upperBound);
	}
}
