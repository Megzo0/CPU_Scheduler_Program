package simulator;

import java.util.*;

public class PriorityScheduler {
    private final List<Process> processList;
    private final int contextSwitchingTime;

    public PriorityScheduler(List<Process> processList, int contextSwitchingTime) {
        this.processList = processList;
        this.contextSwitchingTime = contextSwitchingTime;
    }

    public void execute() {
        // Step 1: Create a queue sorted by priority and arrival time
        List<Process> readyQueue = new ArrayList<>(processList);
        readyQueue.sort(Comparator.comparingInt((Process p) -> p.priority)
                .thenComparingInt(p -> p.arrivalTime));

        int currentTime = 0;
        int totalWaitingTime = 0, totalTurnaroundTime = 0;

        System.out.println("=== Non-preemptive Priority Scheduling ===");

        for (Process process : readyQueue) {
            // Step 2: Wait for the process to arrive if CPU is idle
            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime;
            }

            // Step 3: Calculate process metrics
            process.waitingTime = currentTime - process.arrivalTime;
            currentTime += process.burstTime; // Process execution
            process.turnaroundTime = currentTime - process.arrivalTime;

            // Step 4: Add context switching time after each process (except the last one)
            if (!process.equals(readyQueue.get(readyQueue.size() - 1))) {
                currentTime += contextSwitchingTime;
            }

            // Step 5: Accumulate total metrics
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnaroundTime;

            // Step 6: Print process details
            System.out.println(process);
        }

        // Step 7: Calculate and print average metrics
        System.out.printf("Average Waiting Time: %.2f\n", (double) totalWaitingTime / processList.size());
        System.out.printf("Average Turnaround Time: %.2f\n", (double) totalTurnaroundTime / processList.size());
    }
}
