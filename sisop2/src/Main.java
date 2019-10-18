import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) throws InterruptedException {
//        Semaphore garfo1 = new Semaphore(1);
//        Semaphore garfo2 = new Semaphore(1);
//        Filosofo fil1 = new Filosofo("Raul", garfo1, garfo2);
//        Filosofo fil2 = new Filosofo("Lucas", garfo1, garfo2);
//        fil1.start();
//        fil2.start();

        Jantar mesa = new Jantar(2);
        mesa.start();
    }
}
