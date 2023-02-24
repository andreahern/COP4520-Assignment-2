package problem_1;

public class MinotaurParty {
    public static final int LEADER = 0;
    static SharedCounter guests_entered = new SharedCounter();
    static SharedSwitch game_over = new SharedSwitch();
    static SharedSwitch cupcake_state = new SharedSwitch(true);
    static SharedInt selected_guest = new SharedInt();
    static SharedCounter guests_selected = new SharedCounter();
    static SharedSwitch start_count = new SharedSwitch();

    public static void main(String[] args) throws Exception {
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
        Thread minotaur = new Thread(new SelectGuests(guests_num, game_over, selected_guest, guests_selected, syncObj));

        for (int i = 0; i < guests_num; i++) {
            guests[i] = new Thread(
                    new PlayGame(i, guests_num, guests_entered, game_over, cupcake_state, selected_guest,
                            guests_selected,
                            start_count, syncObj));
            guests[i].start();
        }

        minotaur.start();

        for (int i = 0; i < guests_num; i++) {
            guests[i].join();
        }
        minotaur.join();

        System.out.printf("It took %d guest selections until the claim was made!\n", guests_selected.get());
    }
}

class SelectGuests implements Runnable {
    private int guests_num;
    private SharedSwitch game_over;
    private SharedInt selected_guest;
    private SharedCounter guests_selected;
    private Object syncObj;

    public SelectGuests(int guests_num, SharedSwitch game_over, SharedInt selected_guest,
            SharedCounter guests_selected, Object syncObj) {
        this.guests_num = guests_num;
        this.game_over = game_over;
        this.selected_guest = selected_guest;
        this.guests_selected = guests_selected;
        this.syncObj = syncObj;
    }

    @Override
    public void run() {
        while (!game_over.get()) {
            selected_guest.set((int) (Math.random() * guests_num));
            guests_selected.increment();
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class PlayGame implements Runnable {
    private int ID;
    private int guests_num;
    private SharedCounter guests_entered;
    private SharedSwitch game_over;
    private SharedSwitch cupcake_state;
    private SharedInt selected_guest;
    private SharedCounter guests_selected;
    private SharedSwitch start_count;
    private Object syncObj;

    public PlayGame(int ID, int guests_num, SharedCounter guests_entered, SharedSwitch game_over,
            SharedSwitch cupcake_state,
            SharedInt selected_guest,
            SharedCounter guests_selected, SharedSwitch start_count, Object syncObj) {
        this.ID = ID;
        this.guests_num = guests_num;
        this.guests_entered = guests_entered;
        this.game_over = game_over;
        this.cupcake_state = cupcake_state;
        this.selected_guest = selected_guest;
        this.guests_selected = guests_selected;
        this.start_count = start_count;
        this.syncObj = syncObj;
    }

    @Override
    public void run() {
        boolean has_eaten_cupcake = false;
        int current_guests_selected = guests_selected.get();

        while (!game_over.get()) {
            if (current_guests_selected != guests_selected.get()) {
                current_guests_selected = guests_selected.get();

                if (ID == selected_guest.get()) {
                    if (ID == MinotaurParty.LEADER && !has_eaten_cupcake && cupcake_state.get()) {
                        cupcake_state.flip();
                        has_eaten_cupcake = true;
                        guests_entered.increment();
                        start_count.flip();

                        if (guests_entered.get() == guests_num) {
                            game_over.flip();
                        }

                    } else if (ID == MinotaurParty.LEADER && cupcake_state.get() && start_count.get()) {
                        guests_entered.increment();

                        if (guests_entered.get() == guests_num) {
                            game_over.flip();
                        }
                    } else if (!cupcake_state.get() && !has_eaten_cupcake) {
                        has_eaten_cupcake = true;
                        cupcake_state.flip();
                    }
                }
            }
            synchronized (syncObj) {
                syncObj.notify();
            }
        }
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