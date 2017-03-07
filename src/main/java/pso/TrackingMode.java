package pso;

public enum TrackingMode
{
	ORIGINAL_PSO(0), LOCAL_BEST(1), GLOBAL_BEST(2);
	
	private final int	val;
	
	private TrackingMode (int v)
	{
		val = v;
	}
	
	public int getModeAsInt ()
	{
		return val;
	}
}
