package pso.implementation.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import log.ApplicationLogger;

public class UnitIntervalMapper
{
	
	public static class Mapping implements Comparable<Mapping>
	{
		public int[] indicies = null;
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString ()
		{
			return String.format("Mapping [i=%s]", Arrays.toString(indicies));
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo (Mapping o)
		{
			for (int i = 0; i < o.indicies.length; i++ )
			{
				// sorting makes least significant index in array increment
				// fastest
				if (indicies[i] < o.indicies[i])
				{
					return 1;
				}
				else if (indicies[i] < o.indicies[i])
				{
					return -1;
				}
				else
				{
					continue;
				}
			}
			
			return 0;
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode ()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(indicies);
			return result;
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals (Object obj)
		{
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			Mapping other = (Mapping) obj;
			if (!Arrays.equals(indicies, other.indicies)) return false;
			return true;
		}
	}
	
	protected ArrayList<Mapping>	mappings	= new ArrayList<Mapping>();
	
	/**
	 * How many mappings in the list have been set to null
	 */
	protected int					nullCount	= 0;
	
	public UnitIntervalMapper (int axisSize, int numDimensions)
	{
		generateMappings(axisSize, numDimensions, new ArrayList<Integer>());
		
		Collections.sort(mappings);
		
		// for (Mapping m : mappings)
		// {
		// ApplicationLogger.getInstance().logDebug(m.toString());
		// }
	}
	
	protected void generateMappings (int axisSize, int numDimensions,
			List<Integer> indices)
	{
		if (numDimensions == 0)
		{
			Mapping m = new Mapping();
			
			// System.out.println("Creating mapping of size: " +
			// indices.size());
			
			m.indicies = new int[indices.size()];
			
			int count = 0;
			for (Integer i : indices)
			{
				m.indicies[count] = i;
				count++ ;
			}
			
			// System.out.println("Mapping: " + Arrays.toString(m.indicies));
			
			mappings.add(m);
			
			return;
		}
		else
		{
			for (int i = 0; i < axisSize; i++ )
			{
				// System.out.println("Adding " + i + " to index");
				indices.add(i);
				
				// System.out.println("Calling down with indicies: "
				// + indices.toString());
				generateMappings(axisSize, numDimensions - 1, indices);
				
				// NB: remove(int), not remove(Object)
				indices.remove(indices.size() - 1);
				// System.out.println("Removing " + i);
			}
			
		}
	}
	
	public void addLocation (int[] loc)
	{
		Mapping m = new Mapping ();
		
		m.indicies = loc;
		
		if(!mappings.contains(m))
		{
			mappings.add(m);
		}
	}
	
	public void removeLocation (int[] loc)
	{
		Mapping m = new Mapping();
		
		m.indicies = loc;
		mappings.remove(m);
//		int index = mappings.indexOf(m);
//		//System.out.println("Location: " + m.toString() + " : indx: " + index);
//		
//		if(index == -1)
//		{
//			return;
//		}
//		
//		mappings.set(index, null);
//		nullCount++ ;
	}
	
	public Mapping retrieveByValue (double d)
	{
		if (d < 0.0 || d > 1.0) { throw new IllegalArgumentException(
				"Value must be between 0 and 1"); }
		
		if (mappings.isEmpty()) { return null; }
		
		if (isNull()) { return null; }
		
		double number = mappings.size() - nullCount;
		
		double delta = 1 / number;
		
		double sum = 0.0;
		
		Mapping m = null;
		
		for (int i = 0; i < number; i++ )
		{
			// System.out.println("i: " + i);
			// System.out.println("Sum: " + sum);
			
			if (sum > d)
			{
				int j = getMappedIndex(i);
				
				// remove the (i-1)th mapping
				 m = mappings.remove(i - 1);
//				m = mappings.set(j, null);
//				nullCount++ ;
				
				break;
			}
			
			sum += delta;
		}
		
		// the last one if it's still null
		if (m == null)
		{
			 m = mappings.remove(mappings.size() - 1);
//			m = mappings.set(getMappedIndex(mappings.size() - nullCount - 1),
//					null);
//			nullCount++ ;
		}
		
		return m;
	}
	
	public int getSize ()
	{
		return mappings.size() - nullCount;
	}
	
	protected boolean isNull ()
	{
		for (Mapping map : mappings)
		{
			if (map != null) { return false; }
		}
		
		return true;
	}
	
	protected int getMappedIndex (int unitIntervalIndex)
	{
		int i = 0;
		int judge = 0;
		
		for (Mapping map : mappings)
		{
			if (map != null)
			{
				if (judge == unitIntervalIndex)
				{
					break;
				}
				
				judge++ ;
			}
			
			i++ ;
		}
		
		return i;
	}
}
