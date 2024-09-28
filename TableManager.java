import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TableManager {
    private List<List<Philosopher>> tables;
    private List<Philosopher> sixthTable;
    private List<Lock[]> forks;
    private final int numPhilosophersPerTable = 5;

    public TableManager() {
        tables = new ArrayList<>();
        forks = new ArrayList<>();
        sixthTable = new ArrayList<>();

        // Initialize 5 tables with 5 philosophers each
        for (int tableId = 0; tableId < 5; tableId++) {
            Lock[] forkSet = new Lock[numPhilosophersPerTable];
            List<Philosopher> tablePhilosophers = new ArrayList<>();

            for (int i = 0; i < numPhilosophersPerTable; i++) {
                forkSet[i] = new ReentrantLock();
            }

            // Add philosophers to the table
            for (int i = 0; i < numPhilosophersPerTable; i++) {
                String philosopherName = "" + (char) ('A' + tableId * numPhilosophersPerTable + i);
                Lock leftFork = forkSet[i];
                Lock rightFork = forkSet[(i + 1) % numPhilosophersPerTable];
                Philosopher philosopher = new Philosopher(philosopherName, leftFork, rightFork, tableId, this);
                tablePhilosophers.add(philosopher);
            }

            forks.add(forkSet);
            tables.add(tablePhilosophers);
        }
    }

    public void movePhilosopherToSixthTable(Philosopher philosopher) {
        if (sixthTable.size() < 5) {
            System.out.println(philosopher.getPhilosopherName() + " moves to the sixth table.");
            sixthTable.add(philosopher);
        } else {
            System.out.println("Sixth table is now full. Deadlock has occurred at the sixth table.");
            System.out.println("Last philosopher to move: " + philosopher.getPhilosopherName());
            System.exit(0); // End simulation
        }
    }

    public boolean detectDeadlock(int tableId) {
        for (Philosopher philosopher : tables.get(tableId)) {
            if (philosopher.isEating()) {
                return false;
            }
        }
        System.out.println("Deadlock detected at table " + tableId);
        return true;
    }

    public void startSimulation() {
        for (List<Philosopher> table : tables) {
            for (Philosopher philosopher : table) {
                philosopher.start();
            }
        }
    }
}
