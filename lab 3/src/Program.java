// Загальний клас-ресурс
class CommonResource {
    // Єдине спільне поле — масив цілих чисел
    public int[] numbers;

    // Конструктор для ініціалізації масиву
    public CommonResource(int size) {
        numbers = new int[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = i + 1; // Приклад заповнення
        }
    }

    // Синхронізований метод для збільшення всіх чисел на 1
    public synchronized void incrementAll() {
        for (int i = 0; i < numbers.length; i++) {
            numbers[i]++;
        }
        System.out.print("Після інкременту: ");
        printArray();
    }

    // Синхронізований метод для множення всіх чисел на 2
    public synchronized void multiplyAll() {
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] *= 2;
        }
        System.out.print("Після множення на 2: ");
        printArray();
    }

    // Синхронізований метод для обчислення середнього арифметичного
    public synchronized void printAverage() {
        int sum = 0;
        for (int num : numbers) {
            sum += num;
        }
        double avg = (double) sum / numbers.length;
        System.out.println("Середнє арифметичне: " + avg);
    }

    // Допоміжний метод для виведення масиву
    private void printArray() {
        for (int num : numbers) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
}

// Перший клас — збільшення чисел
class IncrementThread implements Runnable {
    private CommonResource resource;

    public IncrementThread(CommonResource resource) {
        this.resource = resource;
    }

    public void run() {
        resource.incrementAll();
    }
}

// Другий клас — множення чисел на 2
class MultiplyThread implements Runnable {
    private CommonResource resource;

    public MultiplyThread(CommonResource resource) {
        this.resource = resource;
    }

    public void run() {
        resource.multiplyAll();
    }
}

// Третій клас — обчислення середнього арифметичного
class AverageThread implements Runnable {
    private CommonResource resource;

    public AverageThread(CommonResource resource) {
        this.resource = resource;
    }

    public void run() {
        resource.printAverage();
    }
}

// Головний клас програми
public class Program {
    public static void main(String[] args) {
        // Створюємо лише один об'єкт ресурсу
        CommonResource resource = new CommonResource(5);

        // Створюємо потоки
        Thread t1 = new Thread(new IncrementThread(resource));
        Thread t2 = new Thread(new MultiplyThread(resource));
        Thread t3 = new Thread(new AverageThread(resource));

        // Запускаємо потоки
        t1.start();
        t2.start();
        t3.start();

        // Очікуємо завершення потоків
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
