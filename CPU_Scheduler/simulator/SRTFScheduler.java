package simulator;

import java.util.*;

public class SRTFScheduler {
    private final List<Process> processList;
    private final int contextSwitchingTime;

    public SRTFScheduler(List<Process> processList, int contextSwitchingTime) {
        this.processList = processList;
        this.contextSwitchingTime = contextSwitchingTime;
    }

    public void execute() {
        List<Process> readyQueue = new ArrayList<>();
        int currentTime = 0;
        int completed = 0;
        int n = processList.size();

        processList.sort(Comparator.comparingInt(p -> p.arrivalTime));
        while (completed < n) {
            for (Process process : processList) {
                if (process.arrivalTime <= currentTime && process.remainingTime > 0 && !readyQueue.contains(process)) {
                    readyQueue.add(process);
                }
            }

            readyQueue.sort(Comparator.comparingInt(p -> p.remainingTime));
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            Process currentProcess = readyQueue.get(0);
            readyQueue.remove(currentProcess);
            int executionTime = Math.min(currentProcess.remainingTime, contextSwitchingTime);
            currentTime += executionTime;
            currentProcess.remainingTime -= executionTime;

            if (currentProcess.remainingTime == 0) {
                currentProcess.calculateMetrics(currentTime);
                completed++;
            } else {
                readyQueue.add(currentProcess);
            }
        }

        printResults("Shortest Remaining Time First Scheduling");
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
