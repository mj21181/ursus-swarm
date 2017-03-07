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
/**
 * 
 */
package pso;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import log.ApplicationLogger;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.jfree.data.xy.XYDataItem;

import event.EventDispatcher;

/**
 * This class is a tool to parse the log files of a PSO simulation looking for
 * data relevant to a certain type of plot. These files are given a (.pso)
 * extension to differentiate them from other files.
 * 
 * This class checks the extension of the filename passed to it. If the log file
 * is a compressed file (.pso.gz) then it will decompress the data as it parses.
 * 
 * This class extends Observable so that any observers can be registered to
 * receive updates on the object's parsing progress.
 * 
 * @author mike
 * 
 */
public class PsoLogParser extends EventDispatcher
{
	public class ParsedParticle
	{
		public int		id				= -1;
		public int[]	bestPosition	= null;
		public int[]	currentPosition	= null;
		public double	bestFitness		= Double.NaN;
	}
	
	public class ParsedIteration
	{
		public int				numParticles		= -1;
		public int[]			globalBestPosition	= null;
		public double			globalBestFitness	= Double.NaN;
		
		public ParsedParticle[]	particles			= null;
	}
	
	public enum PlotType
	{
		FLAT_LANDSCAPE, BENCHMARK, SIMULATED_LANDSCAPE;
	}
	
	protected String			logFileName	= null;
	
	// reads bytes from log
	protected DataInputStream	rdr			= null;
	
	/**
	 * Default Constructor. No compression. Used for unit tests.
	 */
	public PsoLogParser ()
	{
		logFileName =
				"src" + File.separator + "test" + File.separator + "resources"
						+ File.separator + "pso-test-log.pso";
		
		try
		{
			rdr =
					new DataInputStream(new BufferedInputStream(
							new FileInputStream(new File(logFileName))));
		}
		catch (IOException e)
		{
			ApplicationLogger.getInstance().logException(e);
		}
	}
	
	/**
	 * Explicit Constructor. Checks if the log file is compressed.
	 * 
	 * @param logPath
	 */
	public PsoLogParser (String logPath)
	{
		logFileName = logPath;
		
		try
		{
			File log = new File(logFileName);
			
			String extension =
					log.getAbsolutePath().substring(
							log.getAbsolutePath().lastIndexOf("."),
							log.getAbsolutePath().length());
			
			if (extension.equalsIgnoreCase(".pso"))
			{
				rdr =
						new DataInputStream(new BufferedInputStream(
								new FileInputStream(log)));
			}
			else if (extension.equalsIgnoreCase(".gz"))
			{
				rdr =
						new DataInputStream(new GzipCompressorInputStream(
								new BufferedInputStream(
										new FileInputStream(log))));
			}
			else
			{
				ApplicationLogger.getInstance()
						.logError(
								"Given Log File is of the wrong filetype: "
										+ extension);
			}
		}
		catch (IOException e)
		{
			ApplicationLogger.getInstance().logException(e);
		}
	}
	
	/**
	 * Parses a given PSO Log file, assuming that it is a Flat Landscape
	 * simulation.
	 * 
	 * The number of iterations and which axis to plot is specified. Returned is
	 * a {@link LinkedList} of locations along the axis
	 * 
	 * @param dimension which axis to plot
	 * @param iterationsToPlot the number of iterations to plot
	 * @return
	 */
	public LinkedList<Integer> parseFlatLandscape (int dimension,
			long iterationsToPlot)
	{
		LinkedList<Integer> values = new LinkedList<Integer>();
		
		// determine the number of iterations to plot
		long numberOfIterations = startLog();
		
		if (iterationsToPlot < numberOfIterations)
		{
			numberOfIterations = iterationsToPlot;
		}
		
		// parse each iteration
		for (int i = 0; i < numberOfIterations; i++ )
		{
			// dispatch an event so listeners know how long plotting is taking
			ProgressEvent parsePercentEvent = new ProgressEvent();
			parsePercentEvent.setTaskType(TaskType.PLOT_ITERATION);
			parsePercentEvent.setPercentage((int) Math.round(Math.ceil(i
					* 100.0 / numberOfIterations)));
			
			dispatchEvent(parsePercentEvent);
			
			// parse the iteration
			ParsedIteration it = null;
			try
			{
				it = parseIteration();
			}
			catch (PSOParsingException e)
			{
				ApplicationLogger.getInstance().logException(e);
			}
			catch (IOException e)
			{
				ApplicationLogger.getInstance().logException(e);
			}
			
			// add the location to the list
			for (ParsedParticle p : it.particles)
			{
				int[] cp = p.currentPosition;
				
				values.add(cp[dimension]);
			}
		}
		
		return values;
	}
	
	/**
	 * Parses a given PSO Log file, assuming that it is a Benchmark or Simulated
	 * Landscape simulation.
	 * 
	 * The number of iterations to plot is specified. Returned is a
	 * {@link LinkedList} of fitnss values
	 * 
	 * @param iterationsToPlot the number of iterations to plot
	 * @return
	 */
	public LinkedList<XYDataItem> parseFitness (long iterationsToPlot)
	{
		LinkedList<XYDataItem> values = new LinkedList<XYDataItem>();
		
		// determine the number of iterations to plot
		long numberOfIterations = startLog();
		
		if (iterationsToPlot < numberOfIterations)
		{
			numberOfIterations = iterationsToPlot;
		}
		else if(numberOfIterations < iterationsToPlot)
		{
			iterationsToPlot = numberOfIterations;
		}
			
		
		// parse each iteration
		for (int i = 0; i < numberOfIterations; i++ )
		{
			// dispatch an event so listeners know how long plotting is taking
			ProgressEvent parsePercentEvent = new ProgressEvent();
			parsePercentEvent.setTaskType(TaskType.PLOT_ITERATION);
			parsePercentEvent.setPercentage((int) Math.round(Math.ceil(i
					* 100.0 / numberOfIterations)));
			
			dispatchEvent(parsePercentEvent);
			
			// parse the iteration
			ParsedIteration it = null;
			try
			{
				it = parseIteration();
			}
			catch (PSOParsingException e)
			{
				ApplicationLogger.getInstance().logException(e);
			}
			catch (IOException e)
			{
				ApplicationLogger.getInstance().logException(e);
			}
			
			// add the fitness value to the list
			
			XYDataItem item = new XYDataItem(i, it.globalBestFitness);
			
			values.add(item);
		}
		
		return values;
	}
	
	/**
	 * Gets the InputStream in use for log parsing
	 * 
	 * @return the InputStream
	 */
	public DataInputStream getInputStream ()
	{
		return rdr;
	}
	
	/**
	 * Sets the InputStream in use for log parsing
	 * 
	 * @param rdr the InputStream to set
	 */
	public void setInputStream (DataInputStream rdr)
	{
		this.rdr = rdr;
	}
	
	/**
	 * Gets the name of the log file being parsed.
	 * 
	 * @return the name of the file
	 */
	public String getLogFileName ()
	{
		return logFileName;
	}
	
	/**
	 * Filepath of the log being parsed
	 * 
	 * @param path the path to set
	 */
	public void setLogFileName (String path)
	{
		this.logFileName = path;
	}
	
	/**
	 * Parses a Matrix from the log file.
	 * 
	 * @return the Matrix data structure
	 * @throws IOException If there was an error reading from the InputStream
	 * @throws PSOParsingException If the log file is malformed.
	 */
	protected int[] parseArray () throws IOException, PSOParsingException
	{
		
		int one = rdr.readUnsignedByte();
		int two = rdr.readUnsignedByte();
		int three = rdr.readUnsignedByte();
		int four = rdr.readUnsignedByte();
		
		int num = rdr.readInt();
		
		int[] m = new int[num];
		
		if (! (one == 0xDE && two == 0xFE && three == 0xC8 && four == 0xED)) { throw new PSOParsingException(
				"Log Data is not Array"); }
		
		for (int i = 0; i < m.length; i++ )
		{
			m[i] = rdr.readInt();
		}
		
		return m;
	}
	
	/**
	 * Parses a Particle from the log file.
	 * 
	 * @return the Particle data structure
	 * @throws IOException If there was an error reading from the InputStream
	 * @throws PSOParsingException If the log file is malformed.
	 */
	protected ParsedParticle parseParticle () throws IOException,
			PSOParsingException
	{
		int one = rdr.readUnsignedByte();
		int two = rdr.readUnsignedByte();
		int three = rdr.readUnsignedByte();
		int four = rdr.readUnsignedByte();
		
		if (! (one == 0xB1 && two == 0x6B && three == 0x00 && four == 0xB5)) { throw new PSOParsingException(
				"Log Data is not a Particle"); }
		
		// parse the particle data
		int id = rdr.readInt();
		int[] blp = parseArray();
		int[] cp = parseArray();
		double lf = rdr.readDouble();
		
		// store it in the particle object
		ParsedParticle p = new ParsedParticle();
		
		p.id = id;
		p.bestPosition = blp;
		p.currentPosition = cp;
		p.bestFitness = lf;
		
		return p;
	}
	
	/**
	 * Parses an Iteration from the log file.
	 * 
	 * @return the Iteration data structure
	 * @throws IOException If there was an error reading from the InputStream
	 * @throws PSOParsingException If the log file is malformed.
	 */
	protected ParsedIteration parseIteration () throws IOException,
			PSOParsingException
	{
		int one = rdr.readUnsignedByte();
		int two = rdr.readUnsignedByte();
		int three = rdr.readUnsignedByte();
		int four = rdr.readUnsignedByte();
		
		if (! (one == 0x2B && two == 0xAD && three == 0xBA && four == 0xBE)) { throw new PSOParsingException(
				"Log Data is not an Iteration"); }
		
		// parse the number of particles
		int numParticles = rdr.readInt();
		
		// parse the global best position and fitness
		int[] globalBest = parseArray();
		double globalFitness = rdr.readDouble();
		
		// parse each of the particle objects
		ParsedParticle[] particles = new ParsedParticle[numParticles];
		for (int i = 0; i < numParticles; i++ )
		{
			particles[i] = parseParticle();
		}
		
		// create the iteration object
		ParsedIteration it = new ParsedIteration();
		
		it.numParticles = numParticles;
		it.globalBestPosition = globalBest;
		it.globalBestFitness = globalFitness;
		it.particles = particles;
		
		return it;
	}
	
	/**
	 * Parses the start of the PSO log file, returning the number of iterations
	 * contained in the log file
	 * 
	 * @return
	 */
	protected long startLog ()
	{
		
		try
		{
			int one = rdr.readUnsignedByte();
			int two = rdr.readUnsignedByte();
			int three = rdr.readUnsignedByte();
			
			if (! (one == 0x69 && two == 0x69 && three == 0x69))
			{
				rdr.close();
				throw new IllegalArgumentException(
						"File is not a .pso log file.");
			}
			
			return rdr.readLong();
		}
		catch (IOException e)
		{
			ApplicationLogger.getInstance().logException(e);
		}
		
		return -1;
	}
	
	/**
	 * Closes the log file
	 */
	protected void closeLog ()
	{
		try
		{
			rdr.close();
		}
		catch (IOException e)
		{
			ApplicationLogger.getInstance().logException(e);
		}
	}
}
