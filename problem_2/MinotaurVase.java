package problem_2;

public class MinotaurVase {
    static SharedSwitch sign = new SharedSwitch(true);

    public static void main(String[] args) throws Exception {
        final long startTime = System.nanoTime();

        int guests_num;
        if (args.length > 0) {
            try {
                guests_num = Integer.parseInt(args[0]);
                if (guests_num <= 0) {
                    throw new IllegalArgumentException("Number of guests must be greater than 0 !");
                }
            } catch (NumberFormatException nbfe) {
                throw new IllegalArgumentException("First argument (guests) must be an integer !", nbfe);
            }
        } else {
            throw new IllegalArgumentException("Number of guests is required !");
        }

        Object syncObj = new Object();
        Thread[] guests = new Thread[guests_num];
        Thread fate = new Thread(new HandleFate(guests_num));
        for (int i = 0; i < guests_num; i++) {
            guests[i] = new Thread(
                    new SeeVase(i));
            guests[i].start();
        }

        fate.start();

        for (int i = 0; i < guests_num; i++) {
            guests[i].join();
        }
        fate.join();

        final double seconds = (double) (System.nanoTime() - startTime) / 1_000_000_000;

        System.out.printf("It took %f seconds for all guests to visit showroom !\n", seconds);
    }
}

class HandleFate implements Runnable {
    int guests_num;

    public HandleFate(int guests_num) {
        this.guests_num = guests_num;
    }

    @Override
    public void run() {

    }

}

class SeeVase implements Runnable {
    int ID;

    public SeeVase(int ID) {
        this.ID = ID;
    }

    @Override
    public void run() {

    }
}

class SharedCounter {
    private int count;

    public SharedCounter() {
        this.count = 0;
    }

    public synchronized int getAndIncrement() {
        int temp = count;
        count = temp + 1;
        return temp;
    }

    public synchronized int get() {
        return count;
    }

    public synchronized void increment() {
        count += 1;
    }
}

class SharedSwitch {
    private boolean bool;

    public SharedSwitch() {
        this(false);
    }

    public SharedSwitch(boolean start) {
        this.bool = start;
    }

    public synchronized boolean getAndFlip() {
        boolean res = bool;
        bool = !bool;
        return res;
    }

    public synchronized boolean get() {
        return bool;
    }

    public synchronized void flip() {
        bool = !bool;
    }
}

class SharedInt {
    private int num;

    public SharedInt() {
        this.num = 0;
    }

    public synchronized int get() {
        return num;
    }

    public synchronized void set(int new_num) {
        num = new_num;
    }
}