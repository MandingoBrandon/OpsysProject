import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class Project
{
    private static final String[] pid = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
        "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
    
    private static Rand48 rand;
    private static int n;
    private static int n_cpu;
    private static float lambda;
    private static float ubound;
    private static List<Process> processes = new ArrayList<>();
    public static float next_exp()
    {
        double result = 0.0;
        do
        {
            double r = rand.drand48();
            result = -Math.log(r) / lambda;
        }
        while (result > ubound);
        return (float) result;
    }
    
    public static Process next_process(final boolean io_bound, final String pid)
    {
        final int initial_arrival_time = (int) Math.floor(next_exp());
        final int cpu_bursts = (int) Math.ceil(rand.drand48() * 64);
        
        List<Integer> bursts = new ArrayList<>();
        for (int i = 0; i < cpu_bursts-1; ++i)
        {
            int cpu_burst_time = (int) Math.ceil(next_exp());
            int io_burst_time = (int) Math.ceil(next_exp()) * 10;
            
            if (!io_bound)
            {
                cpu_burst_time *= 4;
                io_burst_time /= 8;
            }
            
            bursts.add(cpu_burst_time);
            bursts.add(io_burst_time);
        }
        
        int cpu_burst_time = (int) Math.ceil(next_exp());
        if (!io_bound)
        {
            cpu_burst_time *= 4;
        }
        bursts.add(cpu_burst_time);
        return new Process(initial_arrival_time, cpu_bursts, bursts, io_bound, pid);
    }
    
    /**
     * <p>
     * args[0]: Define <i>n</i> as the number of processes to simulate.
     *  Process IDs are assigned in alphabetical order A through Z.
     *  Therefore, you will have at most 26 processes to simulate.
     * </p>
     * 
     * <p>
     * args[1]: Define <i>n_cpu</i> as the number of processes that are
     *  CPU-bound. For this project, we will classify processes as
     *  I/O-bound or CPU-bound. The <i>n_cpu</i> CPU-bound processes,
     *  when generated, will have CPU burst times that are longer by a
     *  factor of 4 and will have I/O burst times that are shorter by
     *  a factor of 8.
     * </p>
     * 
     * <p>
     * args[2]: We will use a psuedo-random number generator to determine
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
     * args[3]: To determine interarrival times, we will use an
     *  exponential distribution: therefore, this command-line argument
     *  is parameter lambda. Remember that 1/lambda will be the average
     *  random value generated, e.g., if lambda = 0.01, then the average
     *  should be approximately 100. See the exp-random.c example; and
     *  use the formula shown in the code, i.e., -log(r)/lambda, where
     *  log is the natural logarithm.
     * </p>
     * 
     * <p>
     * args[4]: For the exponential distribution, this command-line
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
        if (args.length != 5)
        {
            System.out.println("ERROR: Incorrect number of arguments");
            System.exit(1);
        }
        
        long seed = 0;
        try
        {
            n = Integer.parseInt(args[0]);
            n_cpu = Integer.parseInt(args[1]);
            seed = Long.parseLong(args[2]);
            lambda = Float.parseFloat(args[3]);
            ubound = Integer.parseInt(args[4]);
        }
        catch (NumberFormatException e)
        {
            System.out.println("ERROR: thrown NumberFormatException");
            System.exit(1);
        }
        
        if (n_cpu > n)
        {
            System.out.println("ERROR: n_proc >= n_cpu");
            System.exit(1);
        }
        
        Project.rand = new Rand48(seed);
        
        if (n_cpu == 1)
        {
            System.out.println("<<< PROJECT PART I -- process set (n=" + n +
                               ") with 1 CPU-bound process >>>");
        }
        else
        {
            System.out.println("<<< PROJECT PART I -- process set (n=" + n +
                               ") with " + n_cpu + " CPU-bound processes >>>");
        }
        
        for (int i = 0; i < n; ++i)
        {
            final boolean io_bound = i < n - n_cpu;
            final Process p = next_process(io_bound, pid[i]);
            p.print();
            processes.add(p);
        }

        Queue<Process> readyQueue = new LinkedList<>(processes); // Assuming you have a list of processes
        // call the fcfs simulation method for each process in the reeady queue
        for (Process p : readyQueue)
        {
            p.FCFS();
        }
    }
}
