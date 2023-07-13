import static org.junit.Assert.assertTrue;

public class Project
{
	private static Rand48 rand;
	
	public static float next_exp(final float lambda, final float ubound)
	{
		float result = 0f;
		do
		{
			double r = rand.drand48();
			result = (float) (-Math.log(r) / lambda);
		}
		while (result > ubound);
		return result;
	}
	
	/**
	 * <p>
	 * args[1]: Define <i>n</i> as the number of processes to simulate.
	 *  Process IDs are assigned in alphabetical order A through Z.
	 *  Therefore, you will have at most 26 processes to simulate.
	 * </p>
	 * 
	 * <p>
	 * args[2]: Define <i>n_cpu</i> as the number of processes that are
	 *  CPU-bound. For this project, we will classify processes as
	 *  I/O-bound or CPU-bound. The <i>n_cpu</i> CPU-bound processes,
	 *  when generated, will have CPU burst times that are longer by a
	 *  factor of 4 and will have I/O burst times that are shorter by
	 *  a factor of 8.
	 * </p>
	 * 
	 * <p>
	 * args[3]: We will use a psuedo-random number generator to determine
	 *  the interarrival times of CPU bursts. This command-line argument,
	 *  i.e. <i>seed</i>, servers as the seed for the psuedo-random
	 *  number sequence. To ensure predictability and repeatability, use
	 *  {@code srand48()} with this given seed before simulating each
	 *  scheduling algorithm and {@code drand48()} to obtain the next
	 *  value in the range [0.0, 1.0). For languages that do not have
	 *  these functions, implement an equivalent 48-bit linear
	 *  congruential generator, as described in the man page for these
	 *  functions in C.
	 * </p>
	 * 
	 * <p>
	 * args[4]: To determine interarrival times, we will use an
	 *  exponential distribution: therefore, this command-line argument
	 *  is parameter lambda. Remember that 1/lambda will be the average
	 *  random value generated, e.g., if lambda = 0.01, then the average
	 *  should be approximately 100. See the exp-random.c example; and
	 *  use the formula shown in the code, i.e., -log(r)/lambda, where
	 *  log is the natural logarithm.
	 * </p>
	 * 
	 * <p>
	 * args[5]: For the exponential distribution, this command-line
	 *  argument represents the upper bound for valid pseudo-random
	 *  numbers. This threshold is used to avoid values far down the
	 *  long tail of the exponential distribution. As an example, if
	 *  this is set to 3000, all generated values above 3000 should be
	 *  skipped. For cases in which this value is used in the ceiling
	 *  function, be sure the ceiling is still valid according to this
	 *  upper bound.
	 * </p>
	 * 
	 * @param args   The command line arguments.
	 */
	public static void main(String[] args)
	{
		final int n = Integer.parseInt(args[1]);
		final int n_cpu = Integer.parseInt(args[2]);
		final long seed = Long.parseLong(args[3]);
		final float lambda = Float.parseFloat(args[4]);
		final int ubound = Integer.parseInt(args[5]);
		
		assertTrue(n >= 0 && n <= 26);
		assertTrue(n_cpu >= 0);
		assertTrue(ubound > 0);

		Project.rand = new Rand48(seed);
		
		// TODO: next_exp()
	}
}
