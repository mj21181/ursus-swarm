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
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Observable;

import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import event.Event;
import event.EventListener;
import event.log.PsoLogEvent;

import pso.interfaces.StateInterface;

import log.ApplicationLogger;

/**
 * This class is a tool to convert the data structures of a PSO object into a
 * binary log file. These files are given a (.pso) extension to differentiate
 * them from other files.
 * 
 * This class keeps track of the number of bytes written into a given log file.
 * This number can be queried using a getter method.
 * 
 * If the explicit constructor is used, the object can be configured to compress
 * the log file using Gzip. In this case, the extension will be (.pso.gz)
 * 
 * @author Mike Johnson
 * 
 */
public class PsoLogWriter implements EventListener
{
	private String					logFileName		= null;
	private File					logFile			= null;
	// counts bytes
	private CountingOutputStream	cout			=
			new CountingOutputStream();
	
	// wrapped around the counter to count bytes
	private DataOutputStream		counterWrapper	=
			new DataOutputStream(cout);
	
	// writes bytes to the log
	private DataOutputStream		out				= null;
	
	protected StateInterface		state			= null;
	
	/**
	 * Default Constructor. No compression. Used for unit tests.
	 */
	public PsoLogWriter (StateInterface s)
	{
		state = s;
		
		logFileName = "src" + File.separator + "test" + File.separator
				+ "resources" + File.separator + "pso-test-log";
		
		try
		{
			logFile = new File(logFileName + ".pso");
			
			if (!logFile.exists())
			{
				logFile.getParentFile().mkdirs();
				logFile.createNewFile();
			}
			
			out = new DataOutputStream(
					new BufferedOutputStream(new FileOutputStream(logFile)));
			
		}
		catch (FileNotFoundException e)
		{
			ApplicationLogger.getInstance().logException(e);
		}
		catch (IOException e)
		{
			ApplicationLogger.getInstance().logException(e);
		}
	}
	
	/**
	 * Explicit Constructor.
	 * 
	 * @param logName Name of the log file.
	 * @param compress Whether to compress the log file.
	 */
	public PsoLogWriter (String logName, StateInterface s, boolean compress)
	{
		logFileName = logName;
		state = s;
		
		try
		{
			if (compress)
			{
				File f = new File(logFileName + ".pso.gz");
				
				if (!f.exists())
				{
					f.getParentFile().mkdirs();
					f.createNewFile();
				}
				
				logFile = f;
				out = new DataOutputStream(new GzipCompressorOutputStream(
						new FileOutputStream(f)));
			}
			else
			{
				File f = new File(logFileName + ".pso");
				
				if (!f.exists())
				{
					f.getParentFile().mkdirs();
					f.createNewFile();
				}
				
				logFile = f;
				out = new DataOutputStream(
						new BufferedOutputStream(new FileOutputStream(f)));
			}
			
			counterWrapper = new DataOutputStream(cout);
		}
		catch (FileNotFoundException e)
		{
			ApplicationLogger.getInstance().logException(e);
		}
		catch (IOException e)
		{
			ApplicationLogger.getInstance().logException(e);
		}
	}
	
	/**
	 * Copy Constructor
	 * 
	 * @param lw The LogWriter to copy
	 */
	public PsoLogWriter (PsoLogWriter lw)
	{
		logFileName = lw.logFileName;
		logFile = lw.logFile;
		
		out = lw.out;
	}
	
	/**
	 * Returns the number of bytes written to the log file.
	 * 
	 * @return long The Number of bytes
	 */
	public long getNumberOfBytesWritten ()
	{
		return cout.getCount();
	}
	
	/**
	 * Gets the name of the log file.
	 * 
	 * @return String logFileName
	 */
	public String getLogFileName ()
	{
		return logFileName;
	}
	
	/**
	 * Sets the name of the log file.
	 * 
	 * @param logFileName the logFileName to set
	 */
	public void setLogFileName (String logFileName)
	{
		this.logFileName = logFileName;
	}
	
	/**
	 * Gets the log file.
	 * 
	 * @return File logFile
	 */
	public File getLogFile ()
	{
		return logFile;
	}
	
	/**
	 * Sets the log file.
	 * 
	 * @param logFile the logFile to set
	 */
	public void setLogFile (File logFile)
	{
		this.logFile = logFile;
	}
	
	/**
	 * Gets the Output Stream in use
	 * 
	 * @return the out
	 */
	public DataOutputStream getOutputStream ()
	{
		return out;
	}
	
	/**
	 * @param out the out to set
	 */
	public void setOutputStream (DataOutputStream out)
	{
		this.out = out;
	}
	
	// /**
	// * Calculates the number of bytes that will be written to a log file given
	// a
	// * PSO object's configuration and the number of iterations that a
	// simulation
	// * will be run for.
	// *
	// * @param p The PSO object
	// * @param iterations The number of iterations
	// * @return The number of bytes as a long
	// */
	// public long calculateSize (int dimension, int numberOfParticles,
	// int iterations)
	// {
	// /*
	// * start of log delimiter 0x696969 plus 4 bytes for int containing
	// * numberOfIterations
	// */
	// long size = 7;
	//
	// int blpSize = 0;
	// int bnpSize = 0;
	// int cpSize = 0;
	// int cvSize = 0;
	// int npSize = 0;
	// int nvSize = 0;
	// int nilSize = 0;
	//
	// /*
	// * 4 bytes for start of vector delimiter, 4 bytes for dimension int, 8
	// * bytes per double
	// */
	// int vectorSize = 4 + 4 + (8 * dimension);
	//
	// blpSize += vectorSize;
	//
	// bnpSize += vectorSize;
	//
	// cpSize += vectorSize;
	//
	// cvSize += vectorSize;
	//
	// npSize += vectorSize;
	//
	// nvSize += vectorSize;
	//
	// int nilDimension = -1;
	//
	// switch (nt)
	// {
	// case CIRCULAR:
	// nilDimension = 3;
	// break;
	// case GLOBAL_BEST:
	// nilDimension = numberOfParticles;
	// break;
	// case PERSONAL_BEST:
	// nilDimension = 1;
	// break;
	// case VON_NEUMANN:
	// nilDimension = 5;
	// break;
	// case WHEEL:
	// nilDimension = 4;
	// break;
	// default:
	// throw new IllegalStateException(
	// "Unknown Neighborhood Topology, cannot calculate log file size");
	// }
	//
	// nilSize += 4 + 4 + (8 * nilDimension);
	//
	// int particleSize = 0;
	//
	// /*
	// * 4 bytes of start of particle delimiter, 4 bytes for id int, 8 bytes
	// * for local fitness double, 8 bytes for neighbor fitness double
	// */
	// particleSize +=
	// 4 + 4 + 8 + 8 + blpSize + bnpSize + cpSize + cvSize + npSize
	// + nvSize + nilSize;
	//
	// int iterationSize = 0;
	//
	// /*
	// * 4 bytes for start of iteration delimiter, 4 bytes for id int
	// * (remove?), 4 bytes for iteration int (make long?)
	// */
	// iterationSize += 12 + numberOfParticles * particleSize;
	//
	// size += iterationSize * iterations;
	//
	// // ApplicationLogger.getInstance().logDbg("File Size in Bytes: "+
	// // (fileSize + 1));
	//
	// // simulationSize += 1; //end of tar entry byte
	//
	// return size;
	// }
	
	/**
	 * Closes the resoures associated with writing the log file.
	 */
	public void closeLog (boolean compress, long actualIterationNumber)
	{
		try
		{
			out.flush();
			out.close();
			counterWrapper.close();
			
			// here we need to replace the numberOfIterations parameter in the
			// log file with the actual number that was performed during the
			// simulation. Otherwise, when the plotting is performed exceptions
			// will be thrown. This opens the log file and rewrites the field.
			
			File tmp = new File("tmp.pso");
			tmp.createNewFile();
			
			out = new DataOutputStream(
					new BufferedOutputStream(new FileOutputStream(tmp)));
			
			DataInputStream in = new DataInputStream(
					new BufferedInputStream(new FileInputStream(logFile)));
			
			out.writeByte(in.readByte());
			out.writeByte(in.readByte());
			out.writeByte(in.readByte());
			
			out.writeLong(actualIterationNumber);
			
			// toss out the old value
			in.readLong();
			
			while (in.available() > 0)
			{
				out.writeByte(in.readByte());
			}
			
			in.close();
			out.flush();
			out.close();
			
			tmp.renameTo(logFile);
			
			// ApplicationLogger.getInstance().logDbg("Total byte count:
			// "+cout.getCount());
			
			if (compress)
			{
				File uncompressedLog = new File(logFileName + ".pso");
				
				GzipCompressorOutputStream zipStream =
						new GzipCompressorOutputStream(new FileOutputStream(
								new File(logFileName + ".pso.gz")));
				BufferedInputStream tmpIn = new BufferedInputStream(
						new FileInputStream(uncompressedLog));
				
				IOUtils.copy(tmpIn, zipStream);
				
				zipStream.flush();
				zipStream.close();
				tmpIn.close();
				
				uncompressedLog.delete();
			}
		}
		catch (IOException e)
		{
			ApplicationLogger.getInstance().logException(e);
		}
	}
	
	/**
	 * Begins a PSO simulation log file for a given number of iterations
	 * 
	 * @param numberOfIterations
	 */
	public void openLog (long numberOfIterations)
	{
		try
		{
			out.writeByte(0x69);
			out.writeByte(0x69);
			out.writeByte(0x69);
			
			out.writeLong(numberOfIterations);
			
			counterWrapper.writeByte(0x69);
			counterWrapper.writeByte(0x69);
			counterWrapper.writeByte(0x69);
			
			counterWrapper.writeLong(numberOfIterations);
			
			// ApplicationLogger.getInstance().logDbg("Start of Log Size: " +
			// counter.getCount());
			
		}
		catch (IOException e)
		{
			ApplicationLogger.getInstance().logException(e);
		}
	}
	
	/**
	 * Writes a vector data structure to the log file.
	 * 
	 * @param m The matrix of data
	 */
	public void arrayToBytes (int[] m)
	{
		try
		{
			// write the start-of-matrix-bytes to file
			
			out.writeByte(0xDE);
			out.writeByte(0xFE);
			out.writeByte(0xC8);
			out.writeByte(0xED);
			
			out.writeInt(m.length);
			
			counterWrapper.writeByte(0xDE);
			counterWrapper.writeByte(0xFE);
			counterWrapper.writeByte(0xC8);
			counterWrapper.writeByte(0xED);
			
			counterWrapper.writeInt(m.length);
			
			// write the double data
			for (int i = 0; i < m.length; i++ )
			{
				out.writeInt(m[i]);
				counterWrapper.writeInt(m[i]);
			}
			
			// ApplicationLogger.getInstance().logDbg("Matrix Count: " +
			// counter.getCount());
		}
		catch (IOException e)
		{
			ApplicationLogger.getInstance().logException(e);
		}
	}
	
	/**
	 * Writes a Particle data structure to a file
	 * 
	 * @param p The Particle data
	 */
	public void particleToBytes (int id, int[] blp, int[] cp, double lf)
	{
		try
		{
			// write the particle-start-of-bytes to file
			out.writeByte(0xB1);
			out.writeByte(0x6B);
			out.writeByte(0x00);
			out.writeByte(0xB5);
			
			// write out id
			out.writeInt(id);
			
			counterWrapper.writeByte(0xB1);
			counterWrapper.writeByte(0x6B);
			counterWrapper.writeByte(0x00);
			counterWrapper.writeByte(0xB5);
			
			// write out id
			counterWrapper.writeInt(id);
			
			arrayToBytes(blp);
			arrayToBytes(cp);
			out.writeDouble(lf);
			counterWrapper.writeDouble(lf);
			
			// ApplicationLogger.getInstance().logDbg("Particle Size: " +
			// counter.getCount());
		}
		catch (IOException e)
		{
			ApplicationLogger.getInstance().logException(e);
		}
	}
	
	/**
	 * Writes an iteration data structure to the file.
	 * 
	 * @param it The Iteration data
	 */
	public void iterationToBytes ()
	{
		try
		{
			// write the iteration-start-of-bytes to file
			out.writeByte(0x2B);
			out.writeByte(0xAD);
			out.writeByte(0xBA);
			out.writeByte(0xBE);
			
			counterWrapper.writeByte(0x2B);
			counterWrapper.writeByte(0xAD);
			counterWrapper.writeByte(0xBA);
			counterWrapper.writeByte(0xBE);
			
			// write out number of particles
			int num = state.getConfiguration().getNumberOfParticles();
			
			out.writeInt(num);
			counterWrapper.writeInt(num);
			
			// write global best location
			arrayToBytes(state.getGlobalBestLocation());
			
			// write global best value
			double nf = state.getGlobalBestValue();
			
			out.writeDouble(nf);
			counterWrapper.writeDouble(nf);
			
			// write out each particle
			for (int i = 0; i < num; i++ )
			{
				int id = i;
				int[] blp = state.getBestLocation(id);
				int[] cp = state.getSampleLocation(id);
				double lf = state.getBestValue(id);
				
				particleToBytes(id, blp, cp, lf);
			}
		}
		catch (IOException e)
		{
			ApplicationLogger.getInstance().logException(e);
		}
	}
	
	/**
	 * This is a private class that simply records the number of bytes that are
	 * written to a certain output stream
	 * 
	 * @author mike
	 * 
	 */
	private class CountingOutputStream extends OutputStream
	{
		private long count = 0;
		
		@Override
		public void write (int b) throws IOException
		{
			count++ ;
		}
		
		public long getCount ()
		{
			return count;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update (Observable o, Object arg)
	{
		if (arg instanceof PsoLogEvent)
		{
			receiveEvent((PsoLogEvent) arg);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see event.EventListener#receiveEvent(event.Event)
	 */
	@Override
	public void receiveEvent (Event evt)
	{
		// PsoLogEvent pevt = (PsoLogEvent) evt;
		
		iterationToBytes();
	}
}
