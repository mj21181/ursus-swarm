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
package pso.async.implementation.plants;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * @author Mike Johnson
 * 
 */
public class LinearPlantParameters
{
	public static LinearPlantParameters generatePsoParameters (
			int numDimensions, double M, double noiseGain)
	{
		int size = numDimensions * 2;
		
		RealMatrix A = new Array2DRowRealMatrix(size, size);
		RealMatrix B = new Array2DRowRealMatrix(size, size);
		RealMatrix D = new Array2DRowRealMatrix(size, size);
		
		RealMatrix topRight =
				MatrixUtils.createRealIdentityMatrix(numDimensions)
						.scalarMultiply(1.0);
		RealMatrix bottomLeft =
				MatrixUtils.createRealIdentityMatrix(numDimensions)
						.scalarMultiply(-M);
		RealMatrix bottomRight =
				MatrixUtils.createRealIdentityMatrix(numDimensions)
						.scalarMultiply(M + 1);
		
		A.setSubMatrix(topRight.getData(), 0, numDimensions);
		A.setSubMatrix(bottomLeft.getData(), numDimensions, 0);
		A.setSubMatrix(bottomRight.getData(), numDimensions, numDimensions);
		
		// System.out.println("A: ");
		// for(int i = 0; i < size; i++)
		// {
		// for(int j = 0; j < size; j++)
		// {
		// System.out.print(A.getEntry(i, j) + ", ");
		// }
		// System.out.println();
		// }
		
		bottomRight =
				MatrixUtils.createRealIdentityMatrix(numDimensions)
						.scalarMultiply(1.0);
		
		B.setSubMatrix(bottomRight.getData(), numDimensions, numDimensions);
		
		// System.out.println("B: ");
		// for(int i = 0; i < size; i++)
		// {
		// for(int j = 0; j < size; j++)
		// {
		// System.out.print(B.getEntry(i, j) + ", ");
		// }
		// System.out.println();
		// }
		
		RealMatrix C = MatrixUtils.createRealIdentityMatrix(size);
		
		LinearPlantParameters params =
				new LinearPlantParameters(size, size, size, noiseGain, A, B, C,
						D);
		
		return params;
	}
	
	private int			stateVectorSize		= -1;
	private int			inputVectorSize		= -1;
	private int			outputVectorSize	= -1;
	
	private double		noiseGain			= Double.NaN;
	
	private RealMatrix	A					= null;
	private RealMatrix	B					= null;
	private RealMatrix	C					= null;
	private RealMatrix	D					= null;
	
	public LinearPlantParameters (int svSize, int ivSize, int ovSize,
			RealMatrix a, RealMatrix b, RealMatrix c, RealMatrix d)
	{
		this(svSize, ivSize, ovSize, Double.NaN, a, b, c, d);
	}
	
	public LinearPlantParameters (int svSize, int ivSize, int ovSize,
			double noiseGain, RealMatrix a, RealMatrix b, RealMatrix c,
			RealMatrix d)
	{
		stateVectorSize = svSize;
		inputVectorSize = ivSize;
		outputVectorSize = ovSize;
		
		this.noiseGain = noiseGain;
		
		A = a;
		B = b;
		C = c;
		D = d;
	}
	
	/**
	 * @return the stateVectorSize
	 */
	public int getStateVectorSize ()
	{
		return stateVectorSize;
	}
	
	/**
	 * @param stateVectorSize the stateVectorSize to set
	 */
	public void setStateVectorSize (int stateVectorSize)
	{
		this.stateVectorSize = stateVectorSize;
	}
	
	/**
	 * @return the inputVectorSize
	 */
	public int getInputVectorSize ()
	{
		return inputVectorSize;
	}
	
	/**
	 * @param inputVectorSize the inputVectorSize to set
	 */
	public void setInputVectorSize (int inputVectorSize)
	{
		this.inputVectorSize = inputVectorSize;
	}
	
	/**
	 * @return the outputVectorSize
	 */
	public int getOutputVectorSize ()
	{
		return outputVectorSize;
	}
	
	/**
	 * @param outputVectorSize the outputVectorSize to set
	 */
	public void setOutputVectorSize (int outputVectorSize)
	{
		this.outputVectorSize = outputVectorSize;
	}
	
	/**
	 * @return the a
	 */
	public RealMatrix getA ()
	{
		return A;
	}
	
	/**
	 * @param a the a to set
	 */
	public void setA (RealMatrix a)
	{
		A = a;
	}
	
	/**
	 * @return the b
	 */
	public RealMatrix getB ()
	{
		return B;
	}
	
	/**
	 * @param b the b to set
	 */
	public void setB (RealMatrix b)
	{
		B = b;
	}
	
	/**
	 * @return the c
	 */
	public RealMatrix getC ()
	{
		return C;
	}
	
	/**
	 * @param c the c to set
	 */
	public void setC (RealMatrix c)
	{
		C = c;
	}
	
	/**
	 * @return the d
	 */
	public RealMatrix getD ()
	{
		return D;
	}
	
	/**
	 * @param d the d to set
	 */
	public void setD (RealMatrix d)
	{
		D = d;
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString ()
	{
		return String
				.format("LinearPlantParameters [stateVectorSize=%s, inputVectorSize=%s, outputVectorSize=%s, noiseGain=%s, A=%s, B=%s, C=%s, D=%s]",
						stateVectorSize, inputVectorSize, outputVectorSize,
						noiseGain, A, B, C, D);
	}
	
}
