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
package task.runner;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import log.ApplicationLogger;

import event.EventDispatcher;

import pso.interfaces.StateInterface;

import task.Task;
import task.TaskSchedule;

/**
 * @author Mike Johnson
 * 
 */
public class TaskThread implements Runnable
{
	protected TaskSchedule		schedule	= new TaskSchedule();
	
	protected CyclicBarrier		barrier		= null;
	
	protected boolean			running		= true;
	
	protected StateInterface	state		= null;
	
	protected EventDispatcher	ed			= new EventDispatcher();
	
	/**
	 * 
	 */
	public TaskThread (CyclicBarrier cb, StateInterface s)
	{
		barrier = cb;
		state = s;
	}
	
	public void terminate ()
	{
		running = false;
	}
	
	public void setSchedule (TaskSchedule ts)
	{
		schedule = ts;
	}
	
	protected void executeInitialStep ()
	{
		Task initial = schedule.getInitialTask();
		
		initial.execute();
		
		if (state.shouldExit())
		{
			running = false;
		}
	}
	
	/**
	 * Executes one {@link Task} of the {@link TaskSchedule} concurrently with
	 * the other robot threads and the {@link StateTaskThread}
	 */
	protected void executeStep ()
	{
		Task currentStep = schedule.nextScheduledTask();
		
		// execute the current step
		currentStep.execute();
		
		if (state.shouldExit())
		{
			running = false;
		}
	}
	
	protected boolean await ()
	{
		try
		{
			if(this instanceof StateTaskThread)
			{
				ApplicationLogger.getInstance().logDebug("State Awaiting");
			}
			else
			{
				ApplicationLogger.getInstance().logDebug("Worker Awaiting");				
			}
			
			barrier.await();
			
			return false;
		}
		catch (InterruptedException e)
		{
			if (running)
			{
				e.printStackTrace();
			}
		}
		catch (BrokenBarrierException e)
		{
			if (running)
			{
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	public EventDispatcher getEventDispatcher ()
	{
		return ed;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run ()
	{
		executeInitialStep();
		
		if (await())
		{
			running = false;
		}
		
		if(this instanceof StateTaskThread)
		{
			ApplicationLogger.getInstance().logDebug("STATE: Proceeding to Task Schedule");
		}
		else
		{
			ApplicationLogger.getInstance().logDebug("WORKER: Proceeding to Task Schedule");
		}
		
		while (running)
		{
			ApplicationLogger.getInstance().logDebug("Executing Step");
			
			executeStep();
			
			// wait for other task threads. If there was an exception, break
			// from loop
			if (await())
			{
				break;
			}
		}
	}
	
}
