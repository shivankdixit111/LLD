package Low_Level_Design.Creational_Design_Patterns.Singleton;

class Logger {
    private static volatile Logger logger = null; //volatile -> ensuring visibility of instance across all threads
    private Logger() {} // Private constructor to prevent instantiation

    public static Logger getLogger(){
        if(logger==null) {
            synchronized (Logger.class) { //synchronized instance creation : ensuring only one thread can access at at time 
                if(logger == null) { // second check : bcoz other threads can enter after their waiting is over and create multiple instance 
                    logger = new Logger();
                }
            }
        }
        return logger;
    }

    public void log(String message) {
        System.out.println(message);
    }
};

public class Main {
    public static void main(String[] args) {
        Logger instance = Logger.getLogger();
        instance.log("Learning Singleton design pattern");
    }
}
/*
 * Double-Checked Locking Singleton (Thread-Safe Lazy Initialization)
 *
 * Why synchronized(Logger.class)?
 * - Logger.class is a single shared Class object in JVM.
 * - It is used as a global lock so only one thread can enter the critical section.
 * - We cannot use Logger (type) or methods/booleans as locks because a lock must be a stable shared object.
 *
 * Flow with 2 threads (A & B):
 *
 * 1. Both Thread A and Thread B call getLogger()
 *    - Both check: if (logger == null)
 *    - Both see null and proceed
 *
 * 2. Thread A enters synchronized block first (gets lock on Logger.class)
 *    - Thread B waits outside the lock
 *
 * 3. Thread A checks again: if (logger == null)
 *    - Still null → creates Logger instance
 *    - logger is now initialized
 *
 * 4. Thread A exits synchronized block and releases lock
 *
 * 5. Thread B acquires lock and enters synchronized block
 *    - It again checks: if (logger == null)
 *    - Now logger is NOT null → skips creation
 *
 * Final Result:
 * - Only one Logger instance is created
 * - All threads return the same shared instance
 *
 * Key Idea:
 * - First null check → performance optimization (avoid locking after initialization)
 * - Synchronized block → ensures only one thread can create instance
 * - Second null check → prevents duplicate creation after lock is acquired
 */