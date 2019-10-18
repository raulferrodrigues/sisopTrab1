import com.sun.tools.javac.util.FatalError;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.Scanner;
import java.io.File;


public class Jantar {
    private ArrayList<Filosofo> filosofos;
    private ArrayList<Semaphore> garfos;
    public final int tamanho;

    public Jantar(int tamanho) {
        this.tamanho = tamanho;
        filosofos = new ArrayList<>();
        garfos = new ArrayList<>();

        File names = new File("resources/names.txt");
        Scanner sc = null;

        try {
          sc = new Scanner(names);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for(int n = 0; n < tamanho; n++) {
            garfos.add(new Semaphore(1));
        }

        for(int n = 0; n < tamanho - 1; n++) {
            filosofos.add(new Filosofo(sc.next(), garfos.get(n), garfos.get(n + 1)));
        }

        filosofos.add(new Filosofo(sc.next(), garfos.get(0), garfos.get(garfos.size() - 1)));
    }

    public void start() {
        for (Filosofo fil: filosofos) {
            fil.start();
        }
    }
}
