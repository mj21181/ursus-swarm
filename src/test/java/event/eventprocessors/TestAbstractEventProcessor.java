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
package event.eventprocessors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import event.Event;
import event.eventprocessors.AbstractEventProcessor;

import junit.framework.TestCase;

/**
 * @author mike
 * 
 */
public class TestAbstractEventProcessor extends TestCase
{
	private class TestEventProcessor extends AbstractEventProcessor
	{
		public boolean	ran	= false;
		
		public TestEventProcessor (int numRobots)
		{
			super(numRobots);
		}
		
		@Override
		public boolean checkEventType (Event evt)
		{
			return true;
		}
		
		@Override
		public void runBarrierAction ()
		{
			ran = true;
			
			// check events were sorted
			
			assertEquals(new Event(0), receivedEvts.get(0));
			assertEquals(new Event(1), receivedEvts.get(1));
			assertEquals(new Event(2), receivedEvts.get(2));
			assertEquals(new Event(3), receivedEvts.get(3));
		}
		
		@Override
		public boolean shouldRunAction ()
		{
			return (receivedEvts.size() == numRobots ? true : false);
		}
		
		@Override
		public String getType ()
		{
			return null;
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	protected void setUp () throws Exception
	{
		super.setUp();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	protected void tearDown () throws Exception
	{
		super.tearDown();
	}
	
	/**
	 * Test method for
	 * {@link event.eventprocessors.AbstractEventProcessor#AbstractEventProcessor(int, int, java.util.concurrent.CyclicBarrier)}
	 * .
	 */
	@Test
	public void testAbstractEventProcessor ()
	{
		TestEventProcessor tep = new TestEventProcessor(12);
		
		assertEquals(12, tep.getNumberOfRobots());
	}
	
	/**
	 * Test method for
	 * {@link event.eventprocessors.AbstractEventProcessor#processEvent(event.Event)}
	 * .
	 */
	@Test
	public void testProcessEvent ()
	{
		TestEventProcessor tep = new TestEventProcessor(4);
		
		// check process event output
		assertEquals(true, tep.processEvent(new Event(2)));
		assertEquals(true, tep.processEvent(new Event(1)));
		assertEquals(true, tep.processEvent(new Event(0)));
		
		// test outside id range
		assertEquals(true, tep.processEvent(new Event(-1)));
		assertEquals(true, tep.processEvent(new Event(20)));
		
		// test duplicate event
		assertEquals(true, tep.processEvent(new Event(0)));
		
		// check tripping barrier
		assertEquals(false, tep.processEvent(new Event(3)));
		
		// check received events queue cleared
		assertEquals(true, tep.getReceivedEvents().isEmpty());
		
		// check runBarrierAction executed
		
		assertEquals(true, tep.ran);
	}
	
//	/**
//	 * Test method for
//	 * {@link event.eventprocessors.AbstractEventProcessor#getBarrier()}
//	 * .
//	 */
//	@Test
//	public void testGetBarrier ()
//	{
//		CyclicBarrier barrier = new CyclicBarrier(2);
//		
//		TestEventProcessor tep = new TestEventProcessor(5, 12, barrier);
//		
//		assertEquals(barrier, tep.getBarrier());
//	}
	
	/**
	 * Test method for
	 * {@link event.eventprocessors.AbstractEventProcessor#getNumberOfRobots()}
	 * .
	 */
	@Test
	public void testGetNumberOfRobots ()
	{
		TestEventProcessor tep = new TestEventProcessor(12);
		
		assertEquals(12, tep.getNumberOfRobots());
	}
	
	/**
	 * Test method for
	 * {@link event.eventprocessors.AbstractEventProcessor#getReceivedEvents()}
	 * .
	 */
	@Test
	public void testGetReceivedEvents ()
	{
		TestEventProcessor tep = new TestEventProcessor(12);
		
		assertEquals(0, tep.getReceivedEvents().size());
		
		tep.processEvent(new Event(5));
		
		assertEquals(1, tep.getReceivedEvents().size());
	}
	
}
