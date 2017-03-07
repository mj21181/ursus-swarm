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
package pso.async.implementation.fitness;

import java.io.Serializable;
import java.util.Arrays;

public class FitnessState implements Serializable
{
	private static final long	serialVersionUID			=
																	5815858802289052852L;
	
	protected FitFunction		f							= FitFunction.MOLA;
	protected double[][]		molaData					= null;
	protected String			molaFile					= "";
	protected double			lowerBound					= 0.0;
	protected double			upperBound					= 0.0;
	protected double			lowerInitializationBound	= 0.0;
	protected double			upperInitializationBound	= 0.0;
	protected int				axisSize					= -1;
	
	/**
	 * Default constructor.
	 * 
	 * Fitness function == SPHERE Lower Bound == -100 Upper Bound == 100
	 * 
	 */
	public FitnessState ()
	{
		f = FitFunction.SPHERE;
		lowerBound = Double.MIN_VALUE;
		upperBound = Double.MAX_VALUE;
		lowerInitializationBound = Double.MIN_VALUE;
		upperInitializationBound = Double.MAX_VALUE;
		molaFile = "";
		axisSize = 100;
	}
	
	/**
	 * Explicit constructor
	 * 
	 * @param ff fitness function to use
	 * @param lb lower bound
	 * @param ub upper bound
	 */
	public FitnessState (FitFunction ff, double lb, double ub, double lib,
			double uib, int size)
	{
		f = ff;
		lowerBound = lb;
		upperBound = ub;
		lowerInitializationBound = lib;
		upperInitializationBound = uib;
		axisSize = size;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param fit
	 */
	public FitnessState (FitnessState fit)
	{
		f = fit.f;
		lowerBound = fit.lowerBound;
		upperBound = fit.upperBound;
		molaData = fit.molaData;
		lowerInitializationBound = fit.lowerInitializationBound;
		upperInitializationBound = fit.upperInitializationBound;
	}
	
	/**
	 * Sets the new fitness function to use
	 * 
	 * @param ff Enum of fitness function to use
	 */
	public void setFitnessFunction (FitFunction ff)
	{
		f = ff;
	}
	
	public void setFitnessFunction (String fitFunctionName)
	{
		
		if (fitFunctionName.equalsIgnoreCase(FitFunction.SPHERE.getName()))
		{
			f = FitFunction.SPHERE;
		}
		else if (fitFunctionName.equalsIgnoreCase(FitFunction.ROSENBROCK
				.getName()))
		{
			f = FitFunction.ROSENBROCK;
		}
		else if (fitFunctionName.equalsIgnoreCase(FitFunction.RASTRIGIN
				.getName()))
		{
			f = FitFunction.RASTRIGIN;
		}
		else if (fitFunctionName.equalsIgnoreCase(FitFunction.GRIEWANK
				.getName()))
		{
			f = FitFunction.GRIEWANK;
		}
		else if (fitFunctionName.equalsIgnoreCase(FitFunction.ACKLEY.getName()))
		{
			f = FitFunction.ACKLEY;
		}
		else if (fitFunctionName.equalsIgnoreCase(FitFunction.MICHALEWICZ
				.getName()))
		{
			f = FitFunction.MICHALEWICZ;
		}
		else if (fitFunctionName.equalsIgnoreCase(FitFunction.SCHWEFEL
				.getName()))
		{
			f = FitFunction.SCHWEFEL;
		}
		else if (fitFunctionName.equalsIgnoreCase(FitFunction.FLAT.getName()))
		{
			f = FitFunction.FLAT;
		}
		else if (fitFunctionName.equalsIgnoreCase(FitFunction.MOLA.getName()))
		{
			f = FitFunction.MOLA;
		}
		else
		{
			f = FitFunction.SPHERE;
		}
	}
	
	/**
	 * Sets the lower bound of the fitness function
	 * 
	 * @param lb the new lower bound
	 */
	public void setLowerBound (double lb)
	{
		lowerBound = lb;
	}
	
	/**
	 * Sets the upper bound of the fitness function
	 * 
	 * @param ub the new upper bound
	 */
	public void setUpperBound (double ub)
	{
		upperBound = ub;
	}
	
	/**
	 * Returns the Enum of the fitnees function currently in use
	 * 
	 * @return Enum of function
	 */
	public FitFunction getFitnessFunction ()
	{
		return f;
	}
	
	/**
	 * Gets the lower bound of the fitness function
	 * 
	 * @return lower bound
	 */
	public double getLowerBound ()
	{
		return lowerBound;
	}
	
	/**
	 * Gets the upper bound of the fitness function
	 * 
	 * @return upper bound
	 */
	public double getUpperBound ()
	{
		return upperBound;
	}
	
	/**
	 * Sets the MOLA data
	 * 
	 * @param m data
	 */
	public void setMOLAData (double[][] m)
	{
		molaData = m;
	}
	
	/**
	 * Gets the MOLA data
	 * 
	 * @return data
	 */
	public double[][] getMOLAData ()
	{
		return molaData;
	}
	
	public void setMolaFile (String file)
	{
		molaFile = file;
	}
	
	public String getMolaFile ()
	{
		return molaFile;
	}
	
	/**
	 * @return the upperInitializationBound
	 */
	public double getUpperInitializationBound ()
	{
		return upperInitializationBound;
	}
	
	/**
	 * @param upperInitializationBound the upperInitializationBound to set
	 */
	public void setUpperInitializationBound (double upperInitializationBound)
	{
		this.upperInitializationBound = upperInitializationBound;
	}
	
	/**
	 * @return the lowerInitializationBound
	 */
	public double getLowerInitializationBound ()
	{
		return lowerInitializationBound;
	}
	
	/**
	 * @param lowerInitializationBound the lowerInitializationBound to set
	 */
	public void setLowerInitializationBound (double lowerInitializationBound)
	{
		this.lowerInitializationBound = lowerInitializationBound;
	}
	
	/**
	 * @return the axisSize
	 */
	public int getAxisSize ()
	{
		return axisSize;
	}
	
	/**
	 * @param axisSize the axisSize to set
	 */
	public void setAxisSize (int axisSize)
	{
		this.axisSize = axisSize;
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
		result = prime * result + axisSize;
		result = prime * result + ( (f == null) ? 0 : f.hashCode());
		long temp;
		temp = Double.doubleToLongBits(lowerBound);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lowerInitializationBound);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Arrays.hashCode(molaData);
		result =
				prime * result
						+ ( (molaFile == null) ? 0 : molaFile.hashCode());
		temp = Double.doubleToLongBits(upperBound);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(upperInitializationBound);
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
		FitnessState other = (FitnessState) obj;
		if (axisSize != other.axisSize) return false;
		if (f != other.f) return false;
		if (Double.doubleToLongBits(lowerBound) != Double
				.doubleToLongBits(other.lowerBound)) return false;
		if (Double.doubleToLongBits(lowerInitializationBound) != Double
				.doubleToLongBits(other.lowerInitializationBound)) return false;
		if (!Arrays.deepEquals(molaData, other.molaData)) return false;
		if (molaFile == null)
		{
			if (other.molaFile != null) return false;
		}
		else if (!molaFile.equals(other.molaFile)) return false;
		if (Double.doubleToLongBits(upperBound) != Double
				.doubleToLongBits(other.upperBound)) return false;
		if (Double.doubleToLongBits(upperInitializationBound) != Double
				.doubleToLongBits(other.upperInitializationBound)) return false;
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
		return String
				.format("FitnessState [f=%s, molaData=%s, molaFile=%s, lowerBound=%s, upperBound=%s, lowerInitializationBound=%s, upperInitializationBound=%s, axisSize=%s]",
						f, Arrays.toString(molaData), molaFile, lowerBound,
						upperBound, lowerInitializationBound,
						upperInitializationBound, axisSize);
	}
	
}
