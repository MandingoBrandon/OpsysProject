
import java.util.List;

public class Process
{
	private final char pid;
	
	private final int arrivalTime;
	private final int cpuBursts;
	private int time;
	private int waitTurn;
	private int waitTime;
	
	private final List<Task> bursts;
	private Task prepend;
	
	private final boolean ioBound;
	private boolean preempted;
	private boolean inCpu;
	private boolean switching;
	
	public Process(final char pid, final int arrivalTime, final int cpuBursts,
			final List<Task> bursts, final boolean ioBound)
	{
		this.pid = pid;
		this.arrivalTime = arrivalTime;
		this.cpuBursts = cpuBursts;
		this.bursts = bursts;
		this.ioBound = ioBound;
		
		this.prepend = new Task.Arrival(arrivalTime, pid);
		this.preempted = false;

		this.time = 0;
		this.waitTurn = 0;
		this.waitTime = 0;
		
		this.inCpu = false;
		this.switching = false;
	}
	
	public Task peak()
	{
		return this.peak(0);
	}
	
	public Task peak(int level)
	{
		if (this.prepend != null)
		{
			if (level == 0)
			{
				return this.prepend;
			}
			else
			{
				return this.peakBurst(level-1);
			}
		}
		else
		{
			return this.peakBurst(level);
		}
	}
	
	public Task peakBurst(int level)
	{
		if (level < this.bursts.size())
		{
			return this.bursts.get(level);
		}
		return null;
	}
	
	public char getPID()
	{
		return this.pid;
	}
	
	public int getArrivalTime()
	{
		return this.arrivalTime;
	}
	
	public int getCpuBursts()
	{
		return this.cpuBursts;
	}
	
	public int getTime()
	{
		return this.time;
	}
	
	public int getWaitTurn()
	{
		return this.waitTurn;
	}
	
	public int getWaitTime()
	{
		return this.waitTime;
	}
	
	public List<Task> getBursts()
	{
		return this.bursts;
	}
	
	public Task getPrepend()
	{
		return this.prepend;
	}
	
	public boolean isIoBound()
	{
		return this.ioBound;
	}
	
	public boolean isPreempted()
	{
		return this.preempted;
	}
	
	public boolean isInCpu()
	{
		return this.inCpu;
	}
	
	public boolean isSwitching()
	{
		return this.switching;
	}
	
	public int getRemainingBursts()
	{
		return (this.bursts.size() + 1) / 2;
	}
	
	public int getTotalBurstTime()
	{
		int total = 0;
		for (Task task : this.bursts)
		{
			if (task.getStatus().equals(Status.CPU))
			{
				total += task.getTime();
			}
		}
		return total;
	}
	
	public void setTime(final int time)
	{
		this.time = time;
	}
	
	public void setPrepend(final Task task)
	{
		if (this.prepend != null)
		{
			return;
		}
		this.prepend = task;
	}
	
	public void preempted(final boolean flag)
	{
		this.preempted = flag;
	}
	
	public void inCpu(final boolean flag)
	{
		this.inCpu = flag;
	}
	
	public void switching(final boolean flag)
	{
		this.switching = flag;
	}
	
	public Task decreaseWaitTime(final int time)
	{
		Task curr = this.peak();
		if (curr != null)
		{
			Status type = curr.getStatus();
			if (type.equals(Status.ARRIVING) || type.equals(Status.IO) || type.equals(Status.CTX_SWITCH))
			{
				this.decreaseFirstTime(time);
			}
			else if (this.inCpu && type.equals(Status.CPU))
			{
				this.decreaseFirstTime(time);
			}
			else if (!this.inCpu)
			{
				this.waitTime += time;
			}
		}
		return curr;
	}
	
	public void decreaseFirstTime(final int time)
	{
		Task curr = this.peak();
		Status type = curr.getStatus();
		int res = curr.decreaseTime(time);
		
		if (res == 0)
		{
			if (type.equals(Status.ARRIVING) || type.equals(Status.CTX_SWITCH))
			{
				this.prepend = null;
			}
			else
			{
				this.bursts.remove(0);
			}
		}
	}
	
	public void print()
	{
		if (this.ioBound)
		{
			System.out.println("I/O-bound process " + this.pid + ": arrival time " +
					this.arrivalTime + "ms; " + this.cpuBursts + " CPU bursts:");
		}
		else
		{
			System.out.println("CPU-bound process " + this.pid + ": arrival time " + this.arrivalTime
					+ "ms; " + this.cpuBursts + " CPU bursts:");
		}
		
		for (int i = 0; i < this.bursts.size(); ++i)
		{
			if (i % 2 == 0)
			{
				System.out.print("--> CPU burst " + bursts.get(i).getTime() + "ms");
			}
			else
			{
				System.out.println(" --> I/O burst " + bursts.get(i).getTime() + "ms");
			}
		}
		System.out.println();
	}
}
