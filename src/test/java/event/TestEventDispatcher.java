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
package event;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * @author Mike Johnson
 *
 */
public class TestEventDispatcher extends TestCase
{
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	protected void setUp () throws Exception
	{
		super.setUp();
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	protected void tearDown () throws Exception
	{
		super.tearDown();
	}
	
	/**
	 * Test method for {@link event.EventDispatcher#dispatchEvent(event.Event)}.
	 */
	@Test
	public void testDispatchEvent ()
	{
		fail("Not yet implemented");
	}
	
}
