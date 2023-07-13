import java.util.List;

public class Process
{
	private final int initial_arrival_time;
	private final int cpu_bursts;
	
	private final List<Integer> bursts;
	
	private final boolean io_bound;
	
	private final String pid;
	
	public Process(final int initial_arrival_time, final int cpu_bursts, final List<Integer> bursts,
			final boolean io_bound, final String pid)
	{
		this.initial_arrival_time = initial_arrival_time;
		this.cpu_bursts = cpu_bursts;
		this.bursts = bursts;
		this.io_bound = io_bound;
		this.pid = pid;
	}
	
	public void print()
	{
		if (this.io_bound)
		{
			System.out.println("I/O-bound process " + this.pid + ": arrival time " +
					this.initial_arrival_time + "ms; " + this.cpu_bursts + " CPU bursts:");
		}
		else
		{
			System.out.println("CPU-bound process " + this.pid + ": arrival time " + this.initial_arrival_time
					+ "ms; " + this.cpu_bursts + " CPU bursts:");
		}
		
		for (int i = 0; i < this.bursts.size(); ++i)
		{
			if (i % 2 == 0)
			{
				System.out.print("--> CPU burst " + bursts.get(i) + "ms");
			}
			else
			{
				System.out.println("--> I/O burst " + bursts.get(i) + "ms");
			}
		}
		System.out.println();
	}
}
