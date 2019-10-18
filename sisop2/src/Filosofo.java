import java.util.concurrent.Semaphore;

public class Filosofo extends Thread {
    public final String nome;
    private Semaphore garfo_1;
    private Semaphore garfo_2;

    public Filosofo(String nome, Semaphore garfo_1, Semaphore garfo_2) {
        this.nome = nome;
        this.garfo_1 = garfo_1;
        this.garfo_2 = garfo_2;
    }

    public void pensar() {
        try {
            System.out.println(nome + " esta pensando.");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        comerV0();
    }

    public void comerV0() {
        try {
            System.out.println(nome + " esta tentando comer.");
            garfo_1.acquire();
            garfo_2.acquire();
            System.out.println(nome + " esta comendo.");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        garfo_1.release();
        garfo_2.release();
        pensar();
    }

    public void inverte() {
        Semaphore aux = garfo_1;
        garfo_2 = garfo_1;
        garfo_2 = aux;
    }

    @Override
    public void run() {
        super.run();
        pensar();
    }
}
