import java.util.LinkedList;
import java.util.Queue;

class ProducerConsumer {

    private final Queue<Integer> buffer = new LinkedList<>();
    private final int capacity = 5;

    public void produce() throws InterruptedException {
        int value = 0;
        while (true) {
            synchronized (this) {
                while (buffer.size() == capacity) {
                    System.out.println("Buffer full. Producer waiting...");
                    wait();
                }

                System.out.println("Producing: " + value);
                buffer.add(value++);

                notify();
                Thread.sleep(500);
            }
        }
    }

    public void consume() throws InterruptedException {
        while (true) {
            synchronized (this) {
                while (buffer.isEmpty()) {
                    System.out.println("Buffer empty. Consumer waiting...");
                    wait();
                }

                int value = buffer.poll();
                System.out.println("Consuming: " + value);

                notify();
                Thread.sleep(1000);
            }
        }
    }

    public static void main(String[] args) {
        ProducerConsumer pc = new ProducerConsumer();

        Thread producerThread = new Thread(() -> {
            try {
                pc.produce();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumerThread = new Thread(() -> {
            try {
                pc.consume();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producerThread.start();
        consumerThread.start();
    }
}
