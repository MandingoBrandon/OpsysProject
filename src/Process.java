import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

	public void FCFS()
	{
		System.out.println("time 0ms: Simulator started for FCFS [Q <empty>]");
		int currentTime = 0;
		Queue<Process> readyQueue = new LinkedList<>();
		readyQueue.add(this); // add the current process to the ready queue
		while(!readyQueue.isEmpty())
		{
			Process currentProcess = readyQueue.poll();
			System.out.println("time " + currentTime + "ms: Process " + currentProcess.pid + " arrived; added to ready queue [Q " + currentProcess.pid + "]");
			// process start using cpu
			int cpuBurstTime = currentProcess.bursts.get(0);
			currentTime += cpuBurstTime;
			System.out.println("time " + currentTime + "ms: Process " + currentProcess.pid + " started using the CPU for " + cpuBurstTime + "ms burst [Q <empty>]");

			// remove 
			currentProcess.bursts.remove(0);
			
			// process complete i/o burst
			// Process completes I/O burst if remaining bursts exist
            if (!currentProcess.bursts.isEmpty()) {
                int ioBurstTime = currentProcess.bursts.get(0);
                currentTime += ioBurstTime;
                System.out.println("time " + currentTime + "ms: Process " + currentProcess.pid + " completed a CPU burst; " + currentProcess.bursts.size() + " bursts to go [Q <empty>]");
                System.out.println("time " + currentTime + "ms: Process " + currentProcess.pid + " switching out of CPU; blocking on I/O until time " + (currentTime + ioBurstTime) + "ms [Q <empty>]");

                // Remove the completed burst
                currentProcess.bursts.remove(0);

                // Update the arrival time for the next process (if available)
                if (!readyQueue.isEmpty()) {
                    Process nextProcess = readyQueue.peek();
					int newArrivalTime = Math.max(currentTime,nextProcess.initial_arrival_time);
					nextProcess.initial_arrival_time = newArrivalTime;
				}

                // Add the process back to the queue for the next burst
                readyQueue.add(currentProcess);
            } else {
                System.out.println("time " + currentTime + "ms: Process " + currentProcess.pid + " terminated [Q <empty>]");
            }
        }

        // Simulation ends
        System.out.println("time " + currentTime + "ms: Simulator ended for FCFS [Q <empty>]");
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
				System.out.println(" --> I/O burst " + bursts.get(i) + "ms");
			}
		}
		System.out.println();
	}
}
