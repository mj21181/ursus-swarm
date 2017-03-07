package pso;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import log.ApplicationLogger;

import event.Event;
import event.EventDispatcher;
import event.events.InitializationEvent;
import event.events.LocationEvent;
import event.events.ValueEvent;
import event.log.PsoLogEvent;
import pso.config.PsoConfiguration;
import pso.implementation.optimization.SampleOptimizer;
import pso.implementation.random.RandomStreamer;
import pso.implementation.random.SeedSet;
import pso.implementation.search.SearchDomain;
import pso.implementation.search.SearchDomainParams;
import pso.implementation.search.UnitIntervalMapper;
import pso.implementation.search.UnitIntervalMapper.Mapping;
import pso.interfaces.StateInterface;
import pso.interfaces.search.SearchDomainInterface;

public class StateRepository implements StateInterface
{
	/**
	 * The singleton
	 */
	private static StateRepository	instance	= null;
	
	/**
	 * Logger reference
	 */
	private ApplicationLogger		log			=
			ApplicationLogger.getInstance();
	
	private StateRepository ()
	{
		// Exists only to defeat instantiation.
	}
	
	/**
	 * Gets the singleton instance
	 * 
	 * @return
	 */
	public synchronized static StateRepository getInstance ()
	{
		if (instance == null)
		{
			instance = new StateRepository();
		}
		
		return instance;
	}
	
	protected boolean					isInitialized		= false;
	
	protected boolean					shouldExit			= false;
	
	protected PsoConfiguration			configuration		= null;
	
	protected SearchDomainInterface		domain				= null;
	
	protected UnitIntervalMapper		mapper				= null;
	
	protected RandomStreamer			streamer			= null;
	
	protected Vector<Sample>			lastSampleBuffer	= null;
	protected Vector<Sample>			sampleBuffer		= null;
	
	protected Vector<InitialState>		initial				= null;
	
	protected SampleOptimizer			globalOptimizer		= null;
	
	protected Vector<SampleOptimizer>	localOptimizers		= null;
	
	protected EventDispatcher			dispatcher			= null;
	
	protected long						iterationNumber		= -1;
	
	protected long						globalBestIteration	= -1;
	
	public void initialize (int simulationNumber, PsoConfiguration conf)
	{
		configuration = conf;
		
		dispatcher = new EventDispatcher();
		
		int numParticles = configuration.getNumberOfParticles();
		int numDimensions = configuration.getNumberOfDimensions();
		
		// initialize the SearchDomain
		
		SearchDomainParams sdParams = new SearchDomainParams();
		
		for (int i = 0; i < numDimensions; i++ )
		{
			sdParams.addAxis(configuration.getFitnessState().getLowerBound(),
					configuration.getFitnessState().getUpperBound(),
					configuration.getFitnessState().getAxisSize());
		}
		
		domain = new SearchDomain(sdParams);
		
		mapper = new UnitIntervalMapper(
				configuration.getFitnessState().getAxisSize(), numDimensions);
		
		// initialize sample buffers
		
		lastSampleBuffer = new Vector<Sample>(numParticles);
		sampleBuffer = new Vector<Sample>(numParticles);
		initial = new Vector<InitialState>(numParticles);
		
		// initialize RNG streamers
		
		streamer = new RandomStreamer(
				configuration.getFitnessState().getAxisSize(),
				new SeedSet(simulationNumber, numParticles, numDimensions));
		
		// initialize optimizers
		
		globalOptimizer =
				new SampleOptimizer(-1, configuration.isMaximize(), this);
		
		localOptimizers = new Vector<SampleOptimizer>(numParticles);
		
		for (int i = 0; i < numParticles; i++ )
		{
			localOptimizers.add(
					new SampleOptimizer(i, configuration.isMaximize(), this));
		}
		
		iterationNumber = 0;
		
		isInitialized = true;
		shouldExit = false;
	}
	
	public SearchDomainParams getSearchDomainParameters ()
	{
		checkInitialization();
		
		return domain.getParams();
	}
	
	public long getIterationNumber ()
	{
		return iterationNumber;
	}
	
	public long getGlobalBestIteration ()
	{
		return globalBestIteration;
	}
	
	public EventDispatcher getDispatcher ()
	{
		return dispatcher;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.interfaces.StateInterface#getSearchDomain()
	 */
	@Override
	public SearchDomainInterface getSearchDomain ()
	{
		checkInitialization();
		
		return domain;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.interfaces.StateInterface#getBestValue(int)
	 */
	@Override
	public double getBestValue (int id)
	{
		checkInitialization();
		
		return localOptimizers.get(id).getOptimumValue();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.interfaces.StateInterface#getGlobalBestValue()
	 */
	@Override
	public double getGlobalBestValue ()
	{
		checkInitialization();
		
		return globalOptimizer.getOptimumValue();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.interfaces.StateInterface#getConfiguration()
	 */
	@Override
	public PsoConfiguration getConfiguration ()
	{
		checkInitialization();
		
		return configuration;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.interfaces.StateInterface#shouldExit()
	 */
	@Override
	public boolean shouldExit ()
	{
		return shouldExit;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.interfaces.StateInterface#getSampleLocation(int)
	 */
	@Override
	public int[] getSampleLocation (int id)
	{
		checkInitialization();
		
		return sampleBuffer.get(id).getSampleLocation();
	}
	
	public int[] getLastSampleLocation (int id)
	{
		checkInitialization();
		
		return lastSampleBuffer.get(id).getSampleLocation();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.interfaces.StateInterface#getInitialState(int)
	 */
	@Override
	public int[] getInitialState (int id)
	{
		checkInitialization();
		
		return initial.get(id).getState();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.interfaces.StateInterface#generateRandomVectorPrimary()
	 */
	@Override
	public int[] generateRandomVectorPrimary ()
	{
		checkInitialization();
		
		return streamer.generateVectorPrimary();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.interfaces.StateInterface#generateRandomVectorSecondary()
	 */
	@Override
	public int[] generateRandomVectorSecondary ()
	{
		checkInitialization();
		
		return streamer.generateVectorSecondary();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.interfaces.StateInterface#generateTieBreaker(int)
	 */
	@Override
	public int generateTieBreaker (int id)
	{
		checkInitialization();
		
		return streamer.generateRandomTieBreaker(id);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.interfaces.StateInterface#generateGlobalTieBreaker()
	 */
	@Override
	public int generateGlobalTieBreaker ()
	{
		checkInitialization();
		
		return streamer.generateGlobalTieBreaker();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.interfaces.StateInterface#generateRandomBackScalar()
	 */
	@Override
	public double generateRandomBackScalar ()
	{
		checkInitialization();
		
		return streamer.generateRandomBackScalar();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.interfaces.StateInterface#generateRandomDuplicateRemovalValue()
	 */
	@Override
	public double generateRandomDuplicateRemovalValue ()
	{
		checkInitialization();
		
		return streamer.generateRandomDuplicateRemovalValue();
	}
	
	@Override
	public Mapping selectAlternateLocation (double value)
	{
		return mapper.retrieveByValue(value);
	}
	
	@Override
	public void addAlternateLocation (int[] loc)
	{
		mapper.addLocation(loc);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.interfaces.StateInterface#getDesiredLocation(int)
	 */
	@Override
	public int[] getDesiredLocation (int id)
	{
		checkInitialization();
		
		int[] desired = null;
		
		switch (configuration.getMode())
		{
			case GLOBAL_BEST:
				desired = globalOptimizer.getOptimumLocation();
				break;
			case LOCAL_BEST:
				desired = localOptimizers.get(id).getOptimumLocation();
				break;
			case ORIGINAL_PSO:
				// TODO:
				
				break;
			default:
				break;
		}
		
		return desired;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.interfaces.StateInterface#getBestLocation(int)
	 */
	@Override
	public int[] getBestLocation (int id)
	{
		checkInitialization();
		
		return localOptimizers.get(id).getOptimumLocation();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.interfaces.StateInterface#getGlobalBestLocation()
	 */
	@Override
	public int[] getGlobalBestLocation ()
	{
		checkInitialization();
		
		return globalOptimizer.getOptimumLocation();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.interfaces.StateInterface#updateSampleLocations(java.util.List)
	 */
	@Override
	public void updateSampleLocations (List<Event> evts, boolean psoLevelUpdate)
	{
		checkInitialization();
		
		if (evts.size() != configuration
				.getNumberOfParticles()) { throw new IllegalArgumentException(
						"Number of received LocationEvents: " + evts.size()
								+ " must match number of robots: "
								+ configuration.getNumberOfParticles()); }
		
		// clear old samples
		sampleBuffer.clear();
		
		// set each sample to an unknown value and known location
		for (Event evt : evts)
		{
			LocationEvent levt = (LocationEvent) evt;
			
			int[] location = levt.getLocation();
			
			ApplicationLogger.getInstance()
					.logDebug("Checking if all locations have been sampled.");
			
			// check whether we have sampled the entire search domain
			boolean negative = true;
			for (int i = 0; i < location.length; i++ )
			{
				if (location[i] < 0)
				{
					negative = true;
					break;
				}
				else
				{
					negative = false;
				}
			}
			
			if (negative)
			{
				ApplicationLogger.getInstance()
						.log("Sampled All Locations by iteration "
								+ iterationNumber + " exiting.");
				
				// TODO: don't lose samples
				shouldExit = true;
			}
			
			Sample s = new Sample(Double.NaN, levt.getLocation());
			
			if (!sampleBuffer.contains(s))
			{
				sampleBuffer.add(s);
			}
			else
			{
				double selectionValue = generateRandomDuplicateRemovalValue();
				
				Mapping m = selectAlternateLocation(selectionValue);
				// System.out.println("Remaining: " + uim.getSize());
				
				// check if all of the locations in the SearchDomain have been
				// observed
				if (m == null)
				{
					shouldExit = true;
					continue;
				}
				
				s = new Sample(Double.NaN, m.indicies);
				
				sampleBuffer.add(s);
			}
		}
		
		if (psoLevelUpdate && !shouldExit)
		{
			// increment to the next iteration. happens
			// once per PSO iteration
			iterationNumber++ ;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.interfaces.StateInterface#updateInitialValues(java.util.List)
	 */
	@Override
	public void updateInitialValues (List<Event> evts)
	{
		checkInitialization();
		
		// clear old samples (if there even were any
		sampleBuffer.clear();
		initial.clear();
		
		ApplicationLogger.getInstance()
				.logDebug("STATE: Updating initial values");
		
		// this assumes these events have been sorted according to id ascending
		// order
		for (Event evt : evts)
		{
			InitializationEvent ievt = (InitializationEvent) evt;
			
			ApplicationLogger.getInstance().logDebug(
					"STATE: Getting initial state for event %02d",
					ievt.getID());
			
			int[] initialState = ievt.getInitial();
			
			// now we need to update the dynamics with the initial state and the
			// sample buffer so that the position is move to and sampled
			initial.add(new InitialState(initialState));
			
			// even# * 2 -> even#, odd# * 2 -> even#, therefore int division
			// should always work no matter how many dimensions fitness function
			// has
			sampleBuffer
					.add(new Sample(Double.NaN, Arrays.copyOfRange(initialState,
							initialState.length / 2, initialState.length)));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pso.interfaces.StateInterface#updateSampleValues(java.util.List)
	 */
	@Override
	public void updateSampleValues (List<Event> evts, boolean psoUpdate)
	{
		checkInitialization();
		
		if (evts.size() != configuration
				.getNumberOfParticles()) { throw new IllegalArgumentException(
						"Number of received ValueEvents must match number of robots. Received: "
								+ evts.size() + " events.  There are "
								+ configuration.getNumberOfParticles()
								+ " robots."); }
		
		// copy the values corresponding to each location
		for (Event evt : evts)
		{
			ValueEvent vevt = (ValueEvent) evt;
			
			sampleBuffer.get(evt.getID()).setSampleValue(vevt.getValue());
		}
		
		log.logDebug("Finished updating sample buffer");
		
		// update the search domain
		for (Sample s : sampleBuffer)
		{
			log.logDebug("Observing location: %s",
					Arrays.toString(s.getSampleLocation()));
			domain.observe(s.getSampleValue(), s.getSampleLocation());
			mapper.removeLocation(s.getSampleLocation());
		}
		
		// copy samples into last buffer
		lastSampleBuffer.clear();
		for (Sample s : sampleBuffer)
		{
			lastSampleBuffer.add(s);
		}
		
		log.logDebug("Finished updating search domain");
		
		// update optimal values
		updateOptima();
		
		// update performance metrics
		updatePerformance();
		
		// update exit conditions
		updateExitConditions();
		
		if(psoUpdate)
		{
			dispatcher.dispatchEvent(new PsoLogEvent(0));			
		}
	}
	
	protected void updateOptima ()
	{
		for (SampleOptimizer so : localOptimizers)
		{
			so.updateOptimum(sampleBuffer);
		}
		
		boolean updated = globalOptimizer.updateOptimum(sampleBuffer);
		
		if (updated)
		{
			globalBestIteration = iterationNumber;
		}
		
		log.logDebug("Finished Updating Optima");
	}
	
	protected void updatePerformance ()
	{
		log.logDebug("Finished Updating Performance Metrics");
	}
	
	protected void updateExitConditions ()
	{
		log.logDebug("Finished Updating Exit Conditions");
	}
	
	protected void checkInitialization ()
	{
		if (!isInitialized) { throw new IllegalStateException(
				"StateRepository is not initialized."); }
	}
}
