import java.util.ArrayList;
import java.util.List;

public class Table {
    private final List<Philosopher> philosophers;  // List to store philosophers at this table
    private final Fork[] forks;  // Array of forks at the table
    private static final Object sixthTableLock = new Object();  // Lock to ensure thread-safe access to the 6th table
    private static int philosophersAtSixthTable = 0;  // Count of philosophers moved to the 6th table
    private static String lastPhilosopherAtSixthTable = null;  // Name of the last philosopher to move to the 6th table

    // Constructor to initialize the table with forks and an empty list of philosophers
    public Table(int tableSize) {
        philosophers = new ArrayList<>();
        forks = new Fork[tableSize];  // Initialize forks for the table
        for (int i = 0; i < tableSize; i++) {
            forks[i] = new Fork();  // Create a fork for each seat at the table
        }
    }

    // Adds a philosopher to this table
    public void addPhilosopher(Philosopher philosopher) {
        philosophers.add(philosopher);  // Add philosopher to the list
    }

    // Start dining by running each philosopher's thread
    public void startDining() {
        for (Philosopher philosopher : philosophers) {
            philosopher.start();  // Start each philosopher thread (they begin thinking, eating, etc.)
        }
    }

    // Check if there's a deadlock and, if so, move a philosopher to the 6th table
    public void checkForDeadlock(Philosopher philosopher) throws InterruptedException {
        if (areAllPhilosophersWaiting()) {  // Check if all philosophers at the table are waiting for forks
            movePhilosopherToSixthTable(philosopher);  // If deadlock detected, move a philosopher to the 6th table
        }
    }

    // Helper method to check if all philosophers at the table are waiting (deadlock detection)
    private boolean areAllPhilosophersWaiting() {
        for (Philosopher philosopher : philosophers) {
            // If any philosopher is not waiting, there is no deadlock
            if (!philosopher.getState().equals(Thread.State.WAITING)) {
                return false;
            }
        }
        // If all philosophers are in the WAITING state, we consider it a deadlock
        return true;
    }

    // Move a philosopher to the 6th table in case of deadlock
    private void movePhilosopherToSixthTable(Philosopher philosopher) throws InterruptedException {
        synchronized (sixthTableLock) {  // Ensure only one philosopher can be moved to the 6th table at a time
            if (philosophersAtSixthTable < 5) {  // Check if the 6th table is not full yet
                System.out.println(philosopher.getPhilosopherName() + " moved to the 6th table.");
                philosopher.moveToSixthTable();  // Mark the philosopher as moved to the 6th table
                philosophersAtSixthTable++;  // Increment the count of philosophers at the 6th table
                lastPhilosopherAtSixthTable = philosopher.getPhilosopherName();  // Record the name of the philosopher moved

                // If the 6th table is now full (5 philosophers), a deadlock has occurred at the 6th table
                if (philosophersAtSixthTable == 5) {
                    System.out.println("Deadlock at 6th table! Last philosopher: " + lastPhilosopherAtSixthTable);
                    System.exit(0);  // End the simulation when deadlock at the 6th table is detected
                }
            }
        }
    }

    // Method to get the fork at a specific index (seat) at the table
    public Fork getFork(int index) {
        return forks[index];  // Return the fork corresponding to the given index
    }
}
