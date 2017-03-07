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
package event.log;

import log.ApplicationLogger;
import log.LogLevel;
import event.Event;


/**
 * This {@link Event} is used to dispatch log messages from the
 * {@link ApplicationLogger}.
 * 
 * A listener is free to log the {@link Event}s in whatever method it chooses.
 * This allows the logging method to be logically separate from the logging
 * implementation.
 * 
 * @author Mike Johnson
 * 
 */
public class LogEvent extends Event
{
	/**
	 * The log message
	 */
	private String		msg	= null;
	
	/**
	 * The {@link LogLevel}, either a Normal, Debug, or Error log message
	 */
	private LogLevel	lvl	= null;
	
	/**
	 * Default constructs a {@link LogEvent}
	 */
	public LogEvent ()
	{
		super(0);
		
		msg = "Default Log Message";
		lvl = LogLevel.NORM;
	}
	
	/**
	 * Constructs a {@link LogEvent} from a given message and {@link LogLevel}
	 * 
	 * @param msg The log message
	 * @param lvl The log level
	 */
	public LogEvent (String msg, LogLevel lvl)
	{
		super(0);
		
		this.msg = msg;
		this.lvl = lvl;
	}
	
	/**
	 * Gets the log message
	 * 
	 * @return the msg
	 */
	public String getMessage ()
	{
		return msg;
	}
	
	/**
	 * @param msg the msg to set
	 */
	public void setMessage (String msg)
	{
		this.msg = msg;
	}
	
	/**
	 * @return the lvl
	 */
	public LogLevel getLogLevel ()
	{
		return lvl;
	}
	
	/**
	 * @param lvl the lvl to set
	 */
	public void setLogLevel (LogLevel lvl)
	{
		this.lvl = lvl;
	}
}
