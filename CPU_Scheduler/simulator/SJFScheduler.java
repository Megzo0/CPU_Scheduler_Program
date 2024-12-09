package simulator;

import java.util.*;

public class SJFScheduler {
    private final List<Process> processList;
    private final int contextSwitchingTime;

    public SJFScheduler(List<Process> processList, int contextSwitchingTime) {
        this.processList = processList;
        this.contextSwitchingTime = contextSwitchingTime;
    }

    public void execute() {
        processList.sort(Comparator.comparingInt(p -> p.burstTime));
        int currentTime = 0;

        for (Process process : processList) {
            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime;
            }
            currentTime += process.burstTime + contextSwitchingTime;
            process.calculateMetrics(currentTime);
        }

        printResults("Non-preemptive Shortest Job First Scheduling");
    }

    private void printResults(String algorithm) {
        System.out.println("=== " + algorithm + " ===");
        int totalWaitingTime = 0, totalTurnaroundTime = 0;
        for (Process process : processList) {
            System.out.println(process);
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnaroundTime;
        }
        System.out.printf("Average Waiting Time: %.2f\n", (double) totalWaitingTime / processList.size());
        System.out.printf("Average Turnaround Time: %.2f\n", (double) totalTurnaroundTime / processList.size());
    }
}
