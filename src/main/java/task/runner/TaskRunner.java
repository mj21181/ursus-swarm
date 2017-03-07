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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import log.ApplicationLogger;
import pso.config.PsoConfiguration;
import pso.interfaces.StateInterface;

import task.TaskSchedule;
import task.maker.TaskScheduleMaker;

/**
 * @author Mike Johnson
 * 
 */
public class TaskRunner
{
	
	/**
	 * The thread responsible for synchronizing all of the state information
	 * from all of the robots
	 */
	protected StateTaskThread	stateThread		= null;
	
	/**
	 * A {@link List} of the different threads for each of the robots
	 */
	protected List<TaskThread>	robotThreads	= null;
	
	/**
	 * Used to manage the execution of all of the simulation's threads
	 */
	protected ExecutorService	service			= null;
	
	protected ApplicationLogger	log				= ApplicationLogger
														.getInstance();
	
	/**
	 * Constructs the {@link TaskRunner} for a given number of robots
	 */
	public TaskRunner (PsoConfiguration conf, StateInterface s)
	{
		int numRobots = conf.getNumberOfParticles();
		
		// create the cyclic barrier
		CyclicBarrier cb = new CyclicBarrier(numRobots + 1);
		
		// initialize the state synchronization thread
		stateThread = new StateTaskThread(numRobots, cb, s);
		
		// initialize all of the worker threads
		
		robotThreads = new ArrayList<TaskThread>(numRobots);
		for (int i = 0; i < numRobots; i++ )
		{
			robotThreads.add(new TaskThread(cb, s));
		}
		
		// add event listeners
		for (TaskThread tt : robotThreads)
		{
			tt.getEventDispatcher().addObserver(stateThread);
		}
		
		// create the executor service
		service = Executors.newFixedThreadPool(numRobots + 1);
		
		TaskScheduleMaker maker = new TaskScheduleMaker();
		
		TaskSchedule stateSchedule =
				maker.makePSOStateSchedule(numRobots, stateThread.queue);
		
		ArrayList<TaskSchedule> workerSchedules = new ArrayList<TaskSchedule>();
		
		for (int i = 0; i < numRobots; i++ )
		{
			TaskSchedule workerSchedule =
					maker.makePSOWorkerSchedule(i, robotThreads.get(i)
							.getEventDispatcher(), conf);
			
			workerSchedules.add(workerSchedule);
		}
		
		specifyTaskSchedules(stateSchedule, workerSchedules);
	}
	
	/**
	 * Specifies the {@link TaskSchedule} for each of the threads
	 * 
	 * @param stateSchedule
	 * @param workerSchedule
	 */
	protected void specifyTaskSchedules (TaskSchedule stateSchedule,
			List<TaskSchedule> workerSchedules)
	{
		stateThread.setSchedule(stateSchedule);
		
		for (int i = 0; i < robotThreads.size(); i++ )
		{
			robotThreads.get(i).setSchedule(
					new TaskSchedule(workerSchedules.get(i)));
		}
	}
	
	public void runTasks ()
	{
		service.submit(stateThread);
		
		for (TaskThread t : robotThreads)
		{
			service.submit(t);
		}
	}
	
	public void shutdown ()
	{
		stateThread.terminate();
		
		for (TaskThread t : robotThreads)
		{
			t.terminate();
		}
		
		service.shutdownNow();
	}
}
