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
package pso.config;

import java.io.Serializable;

import pso.DuplicateDetectionMode;
import pso.TrackingMode;
import pso.async.implementation.boundary.BoundaryTechnique;
import pso.async.implementation.fitness.FitnessState;

public class PsoConfiguration implements Serializable
{
	/**
	 * Generated
	 */
	private static final long			serialVersionUID	=
																	8747499288671026954L;
	
	// pso parameters
	protected int						numberOfParticles	= -1;
	protected int						numberOfDimensions	= -1;
	
	protected boolean					maximize			= false;
	
	protected double					M					= Double.NaN;
	
	protected double					K					= Double.NaN;
	
	protected double					noiseGain			= Double.NaN;
	
	// fitness calculations
	protected FitnessState				fitnessFunction		=
																	new FitnessState();
	
	// neighbor topology
	protected NeighborhoodTopology		nh					=
																	NeighborhoodTopology.GLOBAL_BEST;
	
	// boundary handling technique
	protected BoundaryTechnique			b					=
																	BoundaryTechnique.NONE;
	
	protected TrackingMode				mode				=
																	TrackingMode.ORIGINAL_PSO;
	
	protected DuplicateDetectionMode	ddm					=
																	DuplicateDetectionMode.NONE;
	
	/**
	 * Gets the number of particles being used
	 * 
	 * @return the numberOfParticles
	 */
	public int getNumberOfParticles ()
	{
		return numberOfParticles;
	}
	
	/**
	 * Sets the number of particles being used
	 * 
	 * @param numberOfParticles the numberOfParticles to set
	 */
	public void setNumberOfParticles (int numberOfParticles)
	{
		this.numberOfParticles = numberOfParticles;
	}
	
	/**
	 * Gets the number of fitness dimensions being used
	 * 
	 * @return the numberOfDimensions
	 */
	public int getNumberOfDimensions ()
	{
		return numberOfDimensions;
	}
	
	/**
	 * Sets the number of fitness dimensions to use
	 * 
	 * @param numberOfDimensions the numberOfDimensions to set
	 */
	public void setNumberOfDimensions (int numberOfDimensions)
	{
		this.numberOfDimensions = numberOfDimensions;
	}
	
	/**
	 * Gets the fitness being used
	 * 
	 * @return the fitnessFunction
	 */
	public FitnessState getFitnessState ()
	{
		return fitnessFunction;
	}
	
	/**
	 * Sets the fitness being used
	 * 
	 * @param fitnessFunction the fitnessFunction to set
	 */
	public void setFitnessState (FitnessState fitnessFunction)
	{
		this.fitnessFunction = fitnessFunction;
	}
	
	/**
	 * Gets the Neighborhood in use
	 * 
	 * @return the neighborhood
	 */
	public NeighborhoodTopology getNeighborhoodTopology ()
	{
		return nh;
	}
	
	/**
	 * Sets the Neighborhood in use
	 * 
	 * @param nh the neighborhood to set
	 */
	public void setNeighborhoodTopology (String nh)
	{
		this.nh = NeighborhoodTopology.getNeighborhoodTopology(nh);
	}
	
	/**
	 * Gets the BoundaryHandler in use
	 * 
	 * @return the boundary handler
	 */
	public BoundaryTechnique getBoundaryTechnique ()
	{
		return b;
	}
	
	/**
	 * Sets the BoundaryHandler in use
	 * 
	 * @param bh the bh to set
	 */
	public void setBoundaryTechnique (String name)
	{
		this.b = BoundaryTechnique.getBoundaryTechnique(name);
	}
	
	/**
	 * @return the maximize
	 */
	public boolean isMaximize ()
	{
		return maximize;
	}
	
	/**
	 * @param maximize the maximize to set
	 */
	public void setMaximize (boolean maximize)
	{
		this.maximize = maximize;
	}
	
	/**
	 * @return the mode
	 */
	public TrackingMode getMode ()
	{
		return mode;
	}
	
	/**
	 * @param mode the mode to set
	 */
	public void setMode (TrackingMode mode)
	{
		this.mode = mode;
	}
	
	/**
	 * @return the m
	 */
	public double getM ()
	{
		return M;
	}
	
	/**
	 * @param m the m to set
	 */
	public void setM (double m)
	{
		M = m;
	}
	
	/**
	 * @return the k
	 */
	public double getK ()
	{
		return K;
	}
	
	/**
	 * @param k the k to set
	 */
	public void setK (double k)
	{
		K = k;
	}
	
	/**
	 * @return the noiseGain
	 */
	public double getNoiseGain ()
	{
		return noiseGain;
	}
	
	/**
	 * @param noiseGain the noiseGain to set
	 */
	public void setNoiseGain (double noiseGain)
	{
		this.noiseGain = noiseGain;
	}

	/**
	 * @return the ddm
	 */
	public DuplicateDetectionMode getDuplicateDetectionMode ()
	{
		return ddm;
	}

	/**
	 * @param ddm the ddm to set
	 */
	public void setDuplicateDetectionMode (DuplicateDetectionMode ddm)
	{
		this.ddm = ddm;
	}
	
}
