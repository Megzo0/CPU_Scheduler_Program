package simulator;

import java.util.*;

public class Scheduler {
    private List<Process> processList = new ArrayList<>();

    public void run() {
        Scanner scanner = new Scanner(System.in);

        // Input number of processes
        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();

        // Input time quantum for Round Robin
        System.out.print("Enter the Round Robin time quantum: ");
        int timeQuantum = scanner.nextInt();

        // Input context switching time
        System.out.print("Enter the context switching time: ");
        int contextSwitchingTime = scanner.nextInt();

        // Input process details
        for (int i = 0; i < numProcesses; i++) {
            System.out.println("Enter details for process " + (i + 1) + ":");
            System.out.print("Process Name: ");
            String name = scanner.next();

            System.out.print("Process Color: ");
            String color = scanner.next();

            System.out.print("Process Arrival Time: ");
            int arrivalTime = scanner.nextInt();

            System.out.print("Process Burst Time: ");
            int burstTime = scanner.nextInt();

            System.out.print("Process Priority: ");
            int priority = scanner.nextInt();

            processList.add(new Process(name, color, arrivalTime, burstTime, priority));
        }

        // Display scheduling options
        System.out.println("Select Scheduling Algorithm:");
        System.out.println("1. Non-preemptive Priority Scheduling");
        System.out.println("2. Non-preemptive Shortest Job First (SJF)");
        System.out.println("3. Shortest Remaining Time First (SRTF)");
        System.out.println("4. FCAI Scheduling");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> runPriorityScheduling();
            case 2 -> runSJFScheduling();
            case 3 -> runSRTFScheduling();
            case 4 -> runFCAIScheduling(timeQuantum, contextSwitchingTime);
            default -> System.out.println("Invalid choice.");
        }

        scanner.close();
    }

    private void runPriorityScheduling() {
        // === Non-preemptive Priority Scheduling ===
        System.out.println("=== Non-preemptive Priority Scheduling ===");

        // Sort processes by arrival time first, then priority
        processList.sort(Comparator.comparingInt((Process p) -> p.arrivalTime)
                .thenComparingInt(p -> p.priority));

        int currentTime = 0;

        // Execute processes in the order determined by the sorted list
        for (Process process : processList) {
            // Check if the process arrives after the current time
            if (process.arrivalTime > currentTime) {
                currentTime = process.arrivalTime;
            }

            // Process the current task
            currentTime += process.burstTime; // Update current time after process completion
            process.calculateMetrics(currentTime);
        }

        // Print process details
        for (Process process : processList) {
            System.out.println(process);
        }

        // Calculate averages
        double avgWaitingTime = processList.stream().mapToInt(p -> p.waitingTime).average().orElse(0);
        double avgTurnaroundTime = processList.stream().mapToInt(p -> p.turnaroundTime).average().orElse(0);

        System.out.printf("Average Waiting Time: %.2f\n", avgWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", avgTurnaroundTime);
    }

    private void runSJFScheduling() {
        // Implement non-preemptive SJF scheduling logic here
        System.out.println("Non-preemptive Shortest Job First Scheduling is not yet implemented.");
    }

    private void runSRTFScheduling() {
        // Implement SRTF scheduling logic here
        System.out.println("Shortest Remaining Time First Scheduling is not yet implemented.");
    }

    private void runFCAIScheduling(int timeQuantum, int contextSwitchingTime) {
        // Implement FCAI scheduling logic here
        System.out.println("FCAI Scheduling is not yet implemented.");
    }
}
