import java.util.Arrays;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Phaser;
import java.util.concurrent.Semaphore;

// =================== ЧАСТИНА 1: Семафори ===================
class CommonResource {
    int[] numbers;

    CommonResource(int size) {
        numbers = new int[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = i + 1; // Ініціалізація числами 1,2,3...
        }
    }

    void print() {
        System.out.println(Arrays.toString(numbers));
    }
}

class IncrementThread implements Runnable {
    private CommonResource res;
    private Semaphore sem;

    IncrementThread(CommonResource res, Semaphore sem) {
        this.res = res;
        this.sem = sem;
    }

    public void run() {
        try {
            sem.acquire();
            for (int i = 0; i < res.numbers.length; i++) {
                res.numbers[i] += 1;
            }
            System.out.print("IncrementThread: ");
            res.print();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            sem.release();
        }
    }
}

class MultiplyThread implements Runnable {
    private CommonResource res;
    private Semaphore sem;

    MultiplyThread(CommonResource res, Semaphore sem) {
        this.res = res;
        this.sem = sem;
    }

    public void run() {
        try {
            sem.acquire();
            for (int i = 0; i < res.numbers.length; i++) {
                res.numbers[i] *= 3;
            }
            System.out.print("MultiplyThread: ");
            res.print();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            sem.release();
        }
    }
}

class DecrementThread implements Runnable {
    private CommonResource res;
    private Semaphore sem;

    DecrementThread(CommonResource res, Semaphore sem) {
        this.res = res;
        this.sem = sem;
    }

    public void run() {
        try {
            sem.acquire();
            for (int i = 0; i < res.numbers.length; i++) {
                res.numbers[i] -= 2;
            }
            System.out.print("DecrementThread: ");
            res.print();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            sem.release();
        }
    }
}

// =================== ЧАСТИНА 2: Exchanger ===================
class ExchangerThread1 implements Runnable {
    private Exchanger<String> exchanger;
    private String message;

    ExchangerThread1(Exchanger<String> exchanger) {
        this.exchanger = exchanger;
        this.message = "Слава Україні!";
    }

    public void run() {
        try {
            String received = exchanger.exchange(message);
            System.out.println("Thread1 sent: " + message + ", received: " + received);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ExchangerThread2 implements Runnable {
    private Exchanger<String> exchanger;
    private String message;

    ExchangerThread2(Exchanger<String> exchanger) {
        this.exchanger = exchanger;
        this.message = "Героям Слава!";
    }

    public void run() {
        try {
            String received = exchanger.exchange(message);
            System.out.println("Thread2 sent: " + message + ", received: " + received);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// =================== ЧАСТИНА 3: Phaser ===================
class Programmer implements Runnable {
    private Phaser phaser;
    private String name;

    Programmer(Phaser phaser, String name) {
        this.phaser = phaser;
        this.name = name;
        phaser.register();
    }

    public void run() {
        try {
            System.out.println(name + " збір бізнес-вимог");
            Thread.sleep(500);
            phaser.arriveAndAwaitAdvance();

            System.out.println(name + " розробка програмного забезпечення");
            Thread.sleep(500);
            phaser.arriveAndAwaitAdvance();

            System.out.println(name + " тестування програми");
            Thread.sleep(500);
            phaser.arriveAndDeregister();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// =================== Головний клас ===================
public class MainProgram {
    public static void main(String[] args) throws InterruptedException {
        // ==== Частина 1: Семафори ====
        CommonResource resource = new CommonResource(5);
        Semaphore semaphore = new Semaphore(1);

        Thread t1 = new Thread(new IncrementThread(resource, semaphore));
        Thread t2 = new Thread(new MultiplyThread(resource, semaphore));
        Thread t3 = new Thread(new DecrementThread(resource, semaphore));

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("\n==== Частина 2: Exchanger ====");
        Exchanger<String> exchanger = new Exchanger<>();
        Thread ex1 = new Thread(new ExchangerThread1(exchanger));
        Thread ex2 = new Thread(new ExchangerThread2(exchanger));

        ex1.start();
        ex2.start();

        ex1.join();
        ex2.join();

        System.out.println("\n==== Частина 3: Phaser ====");
        Phaser phaser = new Phaser(1); // головний потік
        Thread prog1 = new Thread(new Programmer(phaser, "Програміст 1"));
        Thread prog2 = new Thread(new Programmer(phaser, "Програміст 2"));

        prog1.start();
        prog2.start();

        // Чекати завершення фаз
        for (int i = 0; i < 3; i++) {
            int phase = phaser.getPhase();
            phaser.arriveAndAwaitAdvance();
            System.out.println("Фаза " + phase + " завершена");
        }

        phaser.arriveAndDeregister();
    }
}
