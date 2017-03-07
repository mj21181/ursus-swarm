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
package log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Observable;
import java.util.Observer;

import event.log.LogEvent;

/**
 * @author Mike Johnson
 *
 */
public class FileLogger implements Observer
{
	protected File			f	= null;
	
	protected PrintStream	ps	= null;
	
	/**
	 * 
	 */
	public FileLogger (String path)
	{
		f = new File(path);
		
		f.getParentFile().mkdirs();
		
		try
		{
			f.createNewFile();
			ps = new PrintStream(
					new BufferedOutputStream(new FileOutputStream(f)));
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
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
		if (arg instanceof LogEvent)
		{
			LogEvent evt = (LogEvent) arg;
			
			switch (evt.getLogLevel())
			{
				case DEBUG:
					ps.println("DEBUG: " + evt.getMessage());
					break;
				case ERROR:
					ps.println("ERROR: " + evt.getMessage());
					break;
				case NORM:
					ps.println(evt.getMessage());
					break;
				default:
					break;
			}
			
			ps.flush();
		}
	}
	
	public void close ()
	{
		ps.close();
	}
	
}
