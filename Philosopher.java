import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Philosopher extends Thread {
    private final String name;        // Name of the philosopher (e.g., "A", "B", etc.)
    private final Fork leftFork;      // Fork to the left of the philosopher
    private final Fork rightFork;     // Fork to the right of the philosopher
    private final Table table;        // The table to which the philosopher belongs
    private volatile boolean movedToSixthTable = false;  // Indicates if the philosopher has moved to the sixth table due to deadlock

    // Constructor to initialize the philosopher with a name, left fork, right fork, and their table
    public Philosopher(String name, Fork leftFork, Fork rightFork, Table table) {
        this.name = name;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.table = table;
    }

    // This method is the entry point for the philosopher thread (called when the thread starts)
    @Override
    public void run() {
        try {
            // The philosopher will continue thinking, trying to pick forks, and eating until moved to the sixth table
            while (!movedToSixthTable) {
                think();  // Simulate thinking
                pickForksAndEat();  // Try to pick up forks and eat
            }
        } catch (InterruptedException e) {
            // Handle the thread interruption (if any) safely
            Thread.currentThread().interrupt();
        }
    }

    // Simulate the thinking process
    private void think() throws InterruptedException {
        System.out.println(name + " is thinking.");
        // Random thinking time between 0 and 10 seconds
        TimeUnit.SECONDS.sleep(new Random().nextInt(10));
    }

    // Try to pick up forks and eat if successful
    private void pickForksAndEat() throws InterruptedException {
        // Attempt to pick up the left fork
        boolean leftForkPicked = leftFork.pickUp();
        if (leftForkPicked) {
            System.out.println(name + " picked up the left fork.");
            // Wait for 4 seconds before attempting to pick up the right fork
            TimeUnit.SECONDS.sleep(4);

            // Attempt to pick up the right fork
            boolean rightForkPicked = rightFork.pickUp();
            if (rightForkPicked) {
                System.out.println(name + " picked up the right fork and starts eating.");
                // Simulate the eating process
                eat();
                // After eating, put down the right fork
                rightFork.putDown();
                System.out.println(name + " put down the right fork.");
            }
            // After attempting to eat, put down the left fork
            leftFork.putDown();
            System.out.println(name + " put down the left fork.");
        } else {
            System.out.println(name + " couldn't pick up the left fork and is waiting.");
        }

        // After attempting to eat or failing, check for deadlock at the current table
        table.checkForDeadlock(this);
    }

    // Simulate the eating process
    private void eat() throws InterruptedException {
        // Random eating time between 0 and 5 seconds
        TimeUnit.SECONDS.sleep(new Random().nextInt(5));
    }

    // Mark the philosopher as moved to the sixth table in case of deadlock
    public void moveToSixthTable() {
        this.movedToSixthTable = true;  // Philosopher moves to the sixth table
    }

    // Get the name of the philosopher (used for logging and deadlock detection)
    public String getPhilosopherName() {
        return name;
    }
}
