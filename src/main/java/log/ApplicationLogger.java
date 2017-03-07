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
package log;

import java.util.Observable;

import event.log.LogEvent;


/**
 * @author Mike Johnson
 * 
 */
public class ApplicationLogger extends Observable
{
	private static ApplicationLogger	instance	= null;
	
	private boolean						debugOn		= true;
	private boolean						normalOn	= true;
	private boolean						errorOn		= true;
	
	private ApplicationLogger ()
	{
		// Exists only to defeat instantiation.
	}
	
	public synchronized static ApplicationLogger getInstance ()
	{
		if (instance == null)
		{
			instance = new ApplicationLogger();
			instance.log("created logger: " + instance.toString());
		}
		
		return instance;
	}
	
	public void logDebug (String msg)
	{
		if(debugOn)
		{
			LogEvent evt = new LogEvent(msg, LogLevel.DEBUG);
			setChanged();
			notifyObservers(evt);
		}
	}
	
	public void logDebug (String msg, Object... args)
	{
		logDebug(String.format(msg, args));
	}
	
	public void logError (String msg)
	{
		if(errorOn)
		{
			LogEvent evt = new LogEvent(msg, LogLevel.ERROR);
			setChanged();
			notifyObservers(evt);
		}
	}
	
	public void logError (String msg, Object... args)
	{
		logError(String.format(msg, args));
	}
	
	public void logException (Exception error)
	{
		logError(error.getMessage());
		
		for (StackTraceElement e : error.getStackTrace())
		{
			logError(e.toString());
		}
	}
	
	public void log (String msg)
	{
		if(normalOn)
		{
			LogEvent evt = new LogEvent(msg, LogLevel.NORM);
			setChanged();
			notifyObservers(evt);
		}
	}
	
	public void log (String msg, Object... args)
	{
		log(String.format(msg, args));
	}
	
	public void debugOn()
	{
		debugOn = true;
	}
	
	public void debugOff()
	{
		debugOn = false;
	}
	
	public void normalOn()
	{
		normalOn = true;
	}
	
	public void normalOff()
	{
		normalOn = false;
	}
	
	public void errorOn()
	{
		errorOn = true;
	}
	
	public void errorOff ()
	{
		errorOn = false;
	}
}
