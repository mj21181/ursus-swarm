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
package pso;

import event.Event;

/**
 * This class is an {@link Event} which updates any listeners on what percentage
 * complete the some task is.
 * 
 * @author Mike Johnson
 * 
 */
public class ProgressEvent extends Event
{
	
	private int			percentage	= -1;
	private TaskType	type;
	
	public ProgressEvent ()
	{
		super(0);
		
		setPercentage(0);
		setTaskType(TaskType.RUN_ITERATION);
	}
	
	/**
	 * @return the percentage
	 */
	public int getPercentage ()
	{
		return percentage;
	}
	
	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage (int percentage)
	{
		this.percentage = percentage;
	}
	
	/**
	 * @return the type
	 */
	public TaskType getTaskType ()
	{
		return type;
	}
	
	/**
	 * @param type the task type to set
	 */
	public void setTaskType (TaskType type)
	{
		this.type = type;
	}
	
}
