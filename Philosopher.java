import java.util.Random;
import java.util.concurrent.locks.Lock;

public class Philosopher extends Thread {
    private String name;
    private Lock leftFork;
    private Lock rightFork;
    private int tableId;
    private boolean eating = false;
    private TableManager tableManager;

    public Philosopher(String name, Lock leftFork, Lock rightFork, int tableId, TableManager tableManager) {
        this.name = name;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.tableId = tableId;
        this.tableManager = tableManager;
    }

    private void think() throws InterruptedException {
        int thinkTime = new Random().nextInt(10) * 1000; // 0 to 10 seconds
        System.out.println(name + " is thinking.");
        Thread.sleep(thinkTime);
    }

    private void eat() throws InterruptedException {
        int eatTime = new Random().nextInt(5) * 1000; // 0 to 5 seconds
        System.out.println(name + " is eating.");
        Thread.sleep(eatTime);
    }

    @Override
    public void run() {
        try {
            while (true) {
                think();

                // Try to pick up left fork
                if (leftFork.tryLock()) {
                    try {
                        System.out.println(name + " picked up the left fork.");
                        Thread.sleep(4000); // Wait 4 seconds before trying the right fork

                        // Try to pick up right fork
                        if (rightFork.tryLock()) {
                            try {
                                System.out.println(name + " picked up the right fork and is now eating.");
                                eating = true;
                                eat();
                                eating = false;
                            } finally {
                                rightFork.unlock();
                            }
                        } else {
                            System.out.println(name + " couldn't pick up the right fork.");
                        }
                    } finally {
                        leftFork.unlock();
                    }
                } else {
                    System.out.println(name + " couldn't pick up the left fork.");
                }

                // Check for deadlock and handle moving to the sixth table
                if (tableManager.detectDeadlock(tableId)) {
                    tableManager.movePhilosopherToSixthTable(this);
                    return;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isEating() {
        return eating;
    }

    public String getPhilosopherName() {
        return name;
    }
}
