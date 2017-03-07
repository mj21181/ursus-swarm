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
package task.maker;

import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import pso.StateRepository;
import pso.async.implementation.PSOPositionTracker;
import pso.async.implementation.boundary.BoundaryHandler;
import pso.async.implementation.duplicate.DuplicateDetector;
import pso.async.implementation.fitness.FitnessCalculator;
import pso.async.implementation.plants.LinearPlantParameters;
import pso.config.PsoConfiguration;
import pso.interfaces.StateInterface;

import event.Event;
import event.EventDispatcher;
import event.eventprocessors.InitializationEventProcessor;
import event.eventprocessors.LocationEventProcessor;
import event.eventprocessors.ValueEventProcessor;
import task.TaskSchedule;
import task.tasks.WaitForEventTask;
import task.tasks.init.ParticleInitializationTask;
import task.tasks.location.LocationTask;
import task.tasks.value.ValueTask;

/**
 * @author Mike Johnson
 * 
 */
public class TaskScheduleMaker
{
	
	/**
	 * 
	 */
	public TaskScheduleMaker ()
	{
	}
	
	public TaskSchedule makePSOWorkerSchedule (int id, EventDispatcher ed,
			PsoConfiguration conf)
	{
		TaskSchedule ts = new TaskSchedule();
		StateInterface s = StateRepository.getInstance();
		
		RealMatrix K =
				MatrixUtils.createRealIdentityMatrix(conf
						.getNumberOfDimensions() * 2);
		
		K.scalarMultiply(conf.getK());
		
		LinearPlantParameters params =
				LinearPlantParameters.generatePsoParameters(
						conf.getNumberOfDimensions(), conf.getM(),
						conf.getNoiseGain());
		
		FitnessCalculator fit =
				new FitnessCalculator(StateRepository.getInstance()
						.getSearchDomainParameters(), conf.getFitnessState()
						.getFitnessFunction());
		
		PSOPositionTracker fcs = new PSOPositionTracker(K, params);
		
		BoundaryHandler bh =
				new BoundaryHandler(id, conf.getFitnessState().getAxisSize(),
						conf.getBoundaryTechnique(), s);
		
		DuplicateDetector dd = new DuplicateDetector(conf.getDuplicateDetectionMode(), s);
		
		// set the initialization task
		ts.setInitialTask(new ParticleInitializationTask(ed, s, id));
		
		// create the cycling schedule of tasks
		ts.appendTask(new ValueTask(ed, fit, s, id));
		ts.appendTask(new LocationTask(fcs, bh, ed, s, dd, id));
		
		return ts;
	}
	
	public TaskSchedule makePSOStateSchedule (int numRobots,
			ArrayBlockingQueue<Event> eventQueue)
	{
		TaskSchedule ts = new TaskSchedule();
		StateInterface s = StateRepository.getInstance();
		
		ts.setInitialTask(new WaitForEventTask(
				new InitializationEventProcessor(numRobots, s), eventQueue));
		
		ts.appendTask(new WaitForEventTask(
				new ValueEventProcessor(numRobots, s), eventQueue));
		ts.appendTask(new WaitForEventTask(new LocationEventProcessor(
				numRobots, s), eventQueue));
		
		return ts;
	}
	
	public TaskSchedule makePSOWorkerWithPathPlannerSchedule ()
	{
		return null;
	}
	
	public TaskSchedule makePSOStateWithPathPlannerSchedule ()
	{
		return null;
	}
	
	public TaskSchedule makePathPlannerWorkerSchedule ()
	{
		return null;
	}
	
	public TaskSchedule makePathPlannerStateSchedule ()
	{
		return null;
	}
}
