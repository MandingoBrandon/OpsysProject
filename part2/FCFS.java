
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FCFS
{
	private Map<Character, Process> processes;
	
	private List<Process> endedProcesses;
	private List<Character> queue;
	
	private char currPID;
	
	private int t_cs;
	private int time;
	private int cpuTime;
	private int contextSwitch;
	
	public FCFS(Map<Character, Process> processes, final int t_cs)
	{
		this.processes = processes;
		this.t_cs = t_cs;
		
		this.endedProcesses = new ArrayList<>();
		this.queue = new ArrayList<>();
		
		this.currPID = '\0';
		
		this.time = 0;
		this.cpuTime = 0;
		this.contextSwitch = 0;
	}
	
	public String queueStr()
	{
		if (queue.size() == 0)
		{
			return "";
		}
		
		String queueStr = "";
		for (int i = 0; i < queue.size()-1; ++i)
		{
			queueStr += queue.get(i) + " ";
		}
		queueStr += queue.get(queue.size()-1);
		return queueStr;
	}
	
	public List<Task> findNextTasks()
	{
		List<Task> tmp = new ArrayList<>();
		for (Process p : this.processes.values())
		{
			tmp.add(p.peak());
		}
		
		if (tmp.size() == 0)
		{
			return tmp;
		}
		
		List<Task> tmp2 = new ArrayList<>();
		if (this.currPID != '\0')
		{
			for (Task task : tmp)
			{
				if (task.getPID() == this.currPID ||
					!task.getStatus().equals(Status.CPU))
				{
					tmp2.add(task);
				}
			}
		}
		else
		{
			tmp2 = tmp;
		}

		int maxTime = Integer.MAX_VALUE;
		for (Task task : tmp2)
		{
			final int time = task.getTime();
			if (time < maxTime)
			{
				maxTime = time;
			}
		}

		List<Task> tasks = new ArrayList<>();
		for (Task task : tmp2)
		{
			if (task.getTime() <= maxTime)
			{
				tasks.add(task);
			}
		}
		
		Collections.sort(tasks, new Task.TComparator());
		return tasks;
	}
	
	public void cpuHandler(Task currentCpu)
	{
		Status type = currentCpu.getStatus();
		char pid = currentCpu.getPID();
		int time = currentCpu.getTime();
		
		Process p = this.processes.get(pid);
		
		if (type.equals(Status.CTX_SWITCH))
		{
			Task nextTask = p.peak(1);
			if (nextTask != null && nextTask.getStatus().equals(Status.CPU))
			{
				this.time += time;
				p.decreaseFirstTime(time);
				int nextCpuTime = p.peak().getTime();
				
				if (this.time <= 999)
				{
					System.out.println("time " + this.time + "ms: Process " + pid + " started using the CPU for " +
										nextCpuTime + "ms burst [Q " + queueStr() + "]");
				}
				
				p.inCpu(true);
				this.contextSwitch += 1;
			}
			else
			{
				p.decreaseFirstTime(time);
				p.inCpu(false);
				this.currPID = '\0';
				
				if (p.peak() == null)
				{
					System.out.println("time " + this.time + "ms: Process " + pid + " terminated [Q " + queueStr() + "]");
					p.setTime(this.time);
					this.endedProcesses.add(p);
					this.processes.remove(pid);
				}
				this.time += time;
			}
		}
		else if (type.equals(Status.CPU))
		{
			this.time += time;
			this.cpuTime += time;
			p.decreaseFirstTime(time);
			
			int remainingBursts = p.getRemainingBursts();
			if (remainingBursts > 0)
			{
				if (this.time <= 999)
				{
					if (remainingBursts == 1)
					{
						System.out.println("time " + this.time + "ms: Process " + pid + " completed a CPU burst; " +
											remainingBursts + " burst to go [Q " + queueStr() + "]");
					}
					else
					{
						System.out.println("time " + this.time + "ms: Process " + pid + " completed a CPU burst; " +
											remainingBursts + " bursts to go [Q " + queueStr() + "]");
					}
				}
				
				Task io = this.processes.get(pid).peak();
				if (io != null && this.time <= 999)
				{
					System.out.println("time " + this.time + "ms: Process " + pid + " switching out of CPU;"
							+ "will block on I/O until time " + io.getTime() + "ms [Q " + queueStr() + "]");
				}
			}
			
			p.setPrepend(new Task.ContextSwitch(this.t_cs, pid));
		}
	}
	
	public void run()
	{
		System.out.println("time 0ms: Simulator started for FCFS [Q <empty>]");
		while (this.processes.size() != 0)
		{
			Task currentCpu = (this.currPID != '\0') ? this.processes.get(this.currPID).peak() : null;
			if (currentCpu == null && this.queue.size() > 0)
			{
				Task task = this.processes.get(this.queue.get(0)).peak();
				char pid = task.getPID();
				this.processes.get(pid).setPrepend(new Task.ContextSwitch(this.t_cs, pid));
				this.currPID = pid;
				this.queue.remove(0);
			}
			
			List<Character> affectedPids = new ArrayList<>();
			List<Task> tasks = this.findNextTasks();
			int time = tasks.get(0).getTime();
			
			List<Task> cpuTasks = new ArrayList<>();
			for (Task task : tasks)
			{
				if (task.getStatus().equals(Status.CPU) ||
					task.getStatus().equals(Status.CTX_SWITCH))
				{
					cpuTasks.add(task);
				}
			}
			
			for (Task task : cpuTasks)
			{
				char pid = task.getPID();
				if (pid == this.currPID)
				{
					this.cpuHandler(task);
					affectedPids.add(pid);
				}
			}
			
			if (affectedPids.size() == 0)
			{
				this.time += time;
			}
			
			for (Task task : tasks)
			{
				char pid = task.getPID();
				Status type = task.getStatus();
				if (!affectedPids.contains(pid))
				{
					affectedPids.add(pid);
					if (type.equals(Status.IO))
					{
						this.queue.add(pid);
						if (this.time <= 999)
						{
							// print("time {:.0f}ms: Process {} completed I/O; placed on ready queue [Q {}]".format(
                            // self.time, npid, self.getQueueStr()))
						}
					}
					else if (type.equals(Status.ARRIVING))
					{
						this.queue.add(pid);
						if (this.time <= 999)
						{
							// print("time {:.0f}ms: Process {} arrived; placed on ready queue [Q {}]".format(
                            // self.time, npid, self.getQueueStr()))
						}
					}
					this.processes.get(pid).decreaseFirstTime(time);
				}
			}
			
			for (Entry<Character, Process> entry : this.processes.entrySet())
			{
				char pid = entry.getKey();
				Process process = entry.getValue();
				
				if (!affectedPids.contains(pid))
				{
					process.decreaseWaitTime(time);
				}
			}
		}
		
		// print(
	    //        "time {:.0f}ms: Simulator ended for FCFS [Q <empty>]".format(self.time))
		
		int totalBurstTime = 0;
		int totalWaitTime = 0;
		int totalBurst = 0;
		
		for (Process p : this.endedProcesses)
		{
			totalBurstTime += p.getTotalBurstTime();
			totalWaitTime += p.getWaitTime();
			totalBurst += p.getCpuBursts();
		}

		System.out.println((float) totalBurstTime / totalBurst);
		System.out.println((float) totalWaitTime / totalBurst);
		System.out.println((float) (totalBurstTime + this.contextSwitch * this.t_cs * 2 + totalWaitTime) / totalBurst);
		System.out.println(this.contextSwitch);
		System.out.println((float) totalBurstTime / this.time * 100);
	}
}
