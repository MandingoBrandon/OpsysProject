
import java.util.Comparator;

public abstract class Task
{
	private int time;
	private int enterAt;
	
	private final char pid;
	
	private boolean preempted;
	
	public Task(final int time, final char pid)
	{
		this.time = time;
		this.pid = pid;
		this.enterAt = 0;
		this.preempted = false;
	}
	
	public Integer decreaseTime(final int time)
	{
		this.time -= time;
		if (this.time < 0)
		{
			System.err.println("ERROR: time < 0");
			System.exit(1);
		}
		return this.time;
	}
	
	public Integer getTime()
	{
		return this.time;
	}
	
	public Integer enteredAt()
	{
		return this.enterAt;
	}
	
	public char getPID()
	{
		return this.pid;
	}
	
	public boolean isPreempted()
	{
		return this.preempted;
	}
	
	public abstract Status getStatus();
	
	public static class CPUBurst extends Task
	{
		public CPUBurst(final int time, final char pid)
		{
			super(time, pid);
		}

		@Override
		public Status getStatus()
		{
			return Status.CPU;
		}
	}
	
	public static class IOBurst extends Task
	{
		public IOBurst(final int time, final char pid)
		{
			super(time, pid);
		}
		
		@Override
		public Status getStatus()
		{
			return Status.IO;
		}
	}
	
	public static class Arrival extends Task
	{
		public Arrival(final int time, final char pid)
		{
			super(time, pid);
		}
		
		@Override
		public Status getStatus()
		{
			return Status.ARRIVING;
		}
	}
	
	public static class ContextSwitch extends Task
	{
		public ContextSwitch(final int time, final char pid)
		{
			super(time, pid);
		}
		
		@Override
		public Status getStatus()
		{
			return Status.CTX_SWITCH;
		}
	}
	
	public static class TComparator implements Comparator<Task>
	{
		public int compare(Task a, Task b)
		{
			if (a.getTime() == b.getTime())
			{
				if (a.getStatus().equals(b.getStatus()))
				{
					return a.getPID() - b.getPID();
				}
				else
				{
					return a.getStatus().compareTo(b.getStatus());
				}
			}
			return a.getTime() - b.getTime();
		}
	}
}
