import java.util.concurrent.locks.ReentrantLock;

public class Fork {
    private final ReentrantLock lock = new ReentrantLock();

    public boolean pickUp() {
        return lock.tryLock();  // Attempt to pick up the fork
    }

    public void putDown() {
        lock.unlock();  // Release the fork
    }
  
}
