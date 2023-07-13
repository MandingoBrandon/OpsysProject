
public class Rand48
{
	private final long a = 0x5DEECE66DL;
	private final long c = 0xBL;
	private final long m = 1L << 48;
	
	private long seed;
	
	public Rand48(final long seed)
	{
		this.srand48(seed);
	}
	
	public void srand48(final long seed)
	{
		this.seed = (this.seed << 16) | 0x330EL;
	}
	
	public double drand48()
	{
		this.seed = (a * this.seed + c) & (m - 1);
		return (double) this.seed / m;
	}
}
