package pso.interfaces;

import java.util.List;

import event.Event;
import event.events.InitializationEvent;
import event.events.LocationEvent;
import event.events.ValueEvent;
import pso.config.PsoConfiguration;
import pso.implementation.search.SearchDomain;
import pso.implementation.search.UnitIntervalMapper.Mapping;
import pso.interfaces.search.SearchDomainInterface;

public interface StateInterface
{
	/**
	 * Returns true if all threads should exit execution
	 * 
	 * @return
	 */
	public boolean shouldExit ();
	
	public PsoConfiguration getConfiguration ();
	
	public SearchDomainInterface getSearchDomain ();
	
	/**
	 * Gets one of the locations that a given particle is about to sample
	 * 
	 * @param id the id of the particle doing the sampling
	 * @return the location to sample
	 */
	public int[] getSampleLocation (int id);
	
	/**
	 * Gets initial location for a given particle
	 * 
	 * @param id the id of the particle doing the sampling
	 * @return the location to sample
	 */
	public int[] getInitialState (int id);
	
	/**
	 * Generates a random position (location) within the {@link SearchDomain}.
	 * 
	 * Vector RNG used to generate a random position for initialization and for
	 * the noise generator used for particle dynamics
	 * 
	 * This method uses an independent source of random numbers from
	 * {@link StateInterface#generateRandomVectorSecondary()}.
	 * 
	 * @param id The id of the robot
	 * @return
	 */
	public int[] generateRandomVectorPrimary ();
	
	/**
	 * Generates a random position (location) within the {@link SearchDomain}
	 * 
	 * Vector RNG used to generate a random position for initialization and for
	 * the random boundary technique
	 * 
	 * This method uses an independent source of random numbers from
	 * {@link StateInterface#generateRandomVectorPrimary()}.
	 * 
	 * @param id The id of the robot
	 * @return
	 */
	public int[] generateRandomVectorSecondary ();
	
	/**
	 * Scalar RNG used to generate lamda for the random back boundary technique.
	 * Value is uniform randomly distributed between 0 and 1
	 * 
	 * @param id The id of the robot
	 * @return
	 */
	public double generateRandomBackScalar ();
	
	public double generateRandomDuplicateRemovalValue ();
	
	public Mapping selectAlternateLocation (double value);
	
	public void addAlternateLocation (int[] loc);
	
	public int generateTieBreaker (int id);
	
	public int generateGlobalTieBreaker ();
	
	/**
	 * Gets the location that a given particle should be tracking
	 * 
	 * @param id
	 * @return
	 */
	public int[] getDesiredLocation (int id);
	
	public int[] getBestLocation (int id);
	
	public double getBestValue (int id);
	
	public int[] getGlobalBestLocation ();
	
	public double getGlobalBestValue ();
	
	/**
	 * Writes to the buffer of new samples based on the received
	 * {@link LocationEvent}s. If the flag is set to true, the iteration will
	 * increment
	 * 
	 * @param evts
	 * @param psoLevelUpdate
	 */
	public void updateSampleLocations (List<Event> evts,
			boolean psoLevelUpdate);
	
	/**
	 * Writes to the buffer of new samples based on the received
	 * {@link ValueEvent}s.
	 * 
	 * @param evts
	 * @param psoUpdate
	 */
	public void updateSampleValues (List<Event> evts, boolean psoUpdate);
	
	/**
	 * Writes to the buffer of initial values based on the received
	 * {@link InitializationEvent}s
	 * 
	 * @param evts
	 */
	public void updateInitialValues (List<Event> evts);
}
