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
package task;

import java.util.LinkedList;

/**
 * @author Mike Johnson
 * 
 */
public class TaskSchedule
{
	protected LinkedList<Task>	queue		= new LinkedList<Task>();
	
	protected Task				initialTask	= null;
	
	protected Task				lastTask	= null;
	
	/**
	 * 
	 */
	public TaskSchedule ()
	{
	}
	
	public TaskSchedule (TaskSchedule ts)
	{
		queue = new LinkedList<Task>();
		queue.addAll(ts.queue);
		
		initialTask = ts.initialTask.copy();
		
		if (ts.lastTask != null)
		{
			lastTask = ts.lastTask.copy();
		}
	}
	
	public void appendTask (Task s)
	{
		queue.addLast(s);
	}
	
	public void prependTask (Task s)
	{
		queue.addFirst(s);
	}
	
	/**
	 * @return the initialTask
	 */
	public Task getInitialTask ()
	{
		return initialTask;
	}
	
	/**
	 * @param initialTask the initialTask to set
	 */
	public void setInitialTask (Task initialTask)
	{
		this.initialTask = initialTask;
	}
	
	public Task nextScheduledTask ()
	{
		// get the task we are about to run
		Task currentTask = queue.poll();
		
		// case when there is only one task
		if (queue.isEmpty())
		{
			queue.add(currentTask);
		}
		else
		{
			// set the last task
			lastTask = currentTask;
			
			// add the last one back into the queue so the tasks cycle
			queue.add(lastTask);
		}
		
		return currentTask;
	}
}
