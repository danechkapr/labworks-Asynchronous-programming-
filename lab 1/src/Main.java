// Лабораторна робота №1
// Тема: Створення і керування потоком в Java

// Клас, що наслідує Thread
class MyThread extends Thread {
    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        System.out.println("Привіт! Я потік успадкований від класу Thread — " + getName());
        try {
            Thread.sleep(5000); // сон 5 секунд
        } catch (InterruptedException e) {
            System.out.println(getName() + " був перерваний.");
        }
        System.out.println(getName() + " завершив роботу.");
    }
}

// Клас, що імплементує Runnable
class MyRunnable implements Runnable {
    private String name;

    public MyRunnable(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("Привіт! Я потік який імплементує інтерфейс Runnable — " + name);
        try {
            Thread.sleep(10000); // сон 10 секунд
        } catch (InterruptedException e) {
            System.out.println(name + " був перерваний.");
        }
        System.out.println(name + " завершив роботу.");
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Головний потік розпочато...");

        // 5 потоків, які наслідують Thread
        MyThread[] threads1 = new MyThread[5];
        for (int i = 0; i < 5; i++) {
            threads1[i] = new MyThread("Thread-" + (i + 1));
            threads1[i].start();
        }

        // 5 потоків, які реалізують Runnable
        Thread[] threads2 = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads2[i] = new Thread(new MyRunnable("Runnable-" + (i + 1)));
            threads2[i].start();
        }

        // Очікуємо завершення всіх потоків
        try {
            for (MyThread t : threads1) t.join();
            for (Thread t : threads2) t.join();
        } catch (InterruptedException e) {
            System.out.println("Головний потік був перерваний.");
        }

        System.out.println("Головний потік завершено.");
    }
}
