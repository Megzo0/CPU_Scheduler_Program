package simulator;

import java.util.*;

public class FCAIScheduler {
    private final List<Process> processList;
    private final int timeQuantum;
    private final int contextSwitchingTime;

    public FCAIScheduler(List<Process> processList, int timeQuantum, int contextSwitchingTime) {
        this.processList = processList;
        this.timeQuantum = timeQuantum;
        this.contextSwitchingTime = contextSwitchingTime;
    }

    public void execute() {
        int v1 = (int) Math.ceil(processList.stream().mapToInt(p -> p.arrivalTime).max().orElse(1) / 10.0);
        int v2 = (int) Math.ceil(processList.stream().mapToInt(p -> p.burstTime).max().orElse(1) / 10.0);

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

            readyQueue.sort(Comparator.comparingDouble(p -> calculateFCAIFactor(p, v1, v2)));
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            Process currentProcess = readyQueue.get(0);
            readyQueue.remove(currentProcess);
            int executionTime = Math.min(currentProcess.remainingTime, timeQuantum);
            int quantum40 = (int) Math.ceil(0.4 * timeQuantum);
            if (executionTime > quantum40) {
                executionTime = quantum40;
            }

            currentTime += executionTime + contextSwitchingTime;
            currentProcess.remainingTime -= executionTime;

            if (currentProcess.remainingTime == 0) {
                currentProcess.calculateMetrics(currentTime);
                completed++;
            } else {
                currentProcess.remainingTime += executionTime;
                readyQueue.add(currentProcess);
            }
        }

        printResults("FCAI Scheduling");
    }

    private double calculateFCAIFactor(Process process, int v1, int v2) {
        return (10 - process.priority) + (process.arrivalTime / (double) v1) + (process.remainingTime / (double) v2);
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
