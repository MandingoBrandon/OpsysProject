
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Project
{
	private Rand48 rand;
	
	private int n;
	private int n_cpu;
	
	private float lambda;
	private float ubound;
	
	private int t_cs;
	private float alpha;
	private int t_slice;
	
	private void checkArguments(String[] args)
	{
		if (args.length != 8)
		{
			System.err.println("ERROR: Incorrect number of arguments");
			System.exit(1);
		}
		
		try
		{
			n = Math.min(Integer.parseInt(args[0]), 26);
			n_cpu = Integer.parseInt(args[1]);
			rand = new Rand48(Long.parseLong(args[2]));
			lambda = Float.parseFloat(args[3]);
			ubound = Integer.parseInt(args[4]);
			t_cs = Integer.parseInt(args[5]);
			alpha = Float.parseFloat(args[6]);
			t_slice = Integer.parseInt(args[7]);
		}
		catch (NumberFormatException e)
		{
			System.err.println("ERROR: thrown NumberFormatException");
			System.exit(1);
		}
	}
	
	private double nextExp()
	{
		double result = Double.MAX_VALUE;
		do
		{
			result = -Math.log(rand.drand()) / lambda;
		}
		while (result > ubound);
		return result;
	}
	
	private Process nextProcess(final char pid, final boolean ioBound)
	{
		final int arrivalTime = (int) Math.floor(nextExp());
		final int cpuBursts = (int) Math.ceil(rand.drand() * 64);
		
		List<Task> bursts = new ArrayList<>();
		for (int i = 0; i < cpuBursts-1;  ++i)
		{
			int cpuBurstTime = (int) Math.ceil(nextExp());
			int ioBurstTime = (int) Math.ceil(nextExp()) * 10;
			
			if (!ioBound)
			{
				cpuBurstTime *= 4;
				ioBurstTime /= 8;
			}
			
			bursts.add(new Task.CPUBurst(cpuBurstTime, pid));
			bursts.add(new Task.IOBurst(ioBurstTime, pid));
		}
		
		int cpuBurstTime = (int) Math.ceil(nextExp());
		cpuBurstTime = (!ioBound) ? cpuBurstTime * 4 : cpuBurstTime;
		bursts.add(new Task.CPUBurst(cpuBurstTime, pid));
		return new Process(pid, arrivalTime, cpuBursts, bursts, ioBound);
	}
	
	public void run(String[] args)
	{
		this.checkArguments(args);
		
		Map<Character, Process> processes = new HashMap<>();
		for (int i = 0; i < n; ++i)
		{
			final boolean ioBound = i < n - n_cpu;
			final char pid = (char) ('A' + i);
			processes.put(pid, nextProcess(pid, ioBound));
		}
		
		for (Process p : processes.values())
		{
			p.print();
		}
		
		FCFS fcfs = new FCFS(processes, t_cs);
		fcfs.run();
	}
	
	public static void main(String[] args)
	{
		 new Project().run(args);
	}
}
