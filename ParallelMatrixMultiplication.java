/*
    https://abitofcs.blogspot.com/2015/12/parallel-matrix-multiplication-in-java.html
    https://akarshbolar.wordpress.com/2017/04/02/matrix-multiplication-in-java/
    https://www.javaworld.com/article/2074217/java-101--understanding-java-threads--part-1--introducing-threads-and-runnables.html
*/


import java.util.Random;
import java.util.concurrent.*;
public class ParallelMatrixMultiplication {

    private int[][] a;
    private int[][] b;
    private int[][] c;
    private int size;
    private int threshold = 8;

    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private final ExecutorService exec = Executors.newFixedThreadPool(POOL_SIZE);

    public int[][] getResult() {
        return c;
    }

    ParallelMatrixMultiplication(int matrixSize) {
        this.size = matrixSize;

        a = new int[size][size];
        b = new int[size][size];
        c = new int[size][size];

        Random rand = new Random();

        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a.length; ++j) {
                a[i][j] = rand.nextInt(10);
                b[i][j] = rand.nextInt(10);;
            }
        }
    }

    public void multiplySync() {
        multiplySyncInternal();
    }

    private void multiplySyncInternal() {
        multipltySyncInternal(a, b, c, 0, 0, 0, 0, 0, 0, size);
    }

    private void multipltySyncInternal(int[][] a, int[][] b, int[][] c, int a_i, int a_j, int b_i, int b_j, int c_i, int c_j, int size) {
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                for (int k = 0; k < size; ++k) {
                    c[c_i + i][c_j + j] += a[a_i + i][a_j + k] * b[b_i + k][b_j + j];
                }
            }
        }
    }

    public void multiplyAsyncThreads() {
        MultiplyTaskThread thread1 = new MultiplyTaskThread("thread1", a, b, c, 0, 0, 0, 0, 0, 0, size);
        thread1.start();
        MultiplyTaskThread thread2 = new MultiplyTaskThread("thread2", a, b, c, 0, 0, 0, 0, 0, 0, size);
        thread2.start();
        MultiplyTaskThread thread3 = new MultiplyTaskThread("thread3", a, b, c, 0, 0, 0, 0, 0, 0, size);
        thread3.start();
        MultiplyTaskThread thread4 = new MultiplyTaskThread("thread4", a, b, c, 0, 0, 0, 0, 0, 0, size);
        thread4.start();

        for (Thread t : new Thread[] { thread1.thread, thread2.thread, thread3.thread, thread4.thread }) {
            try {
                t.join();
            } catch(Exception e) {
                System.out.println(e);
            }
        }
    }

    class MultiplyTaskThread implements Runnable {
        private int[][] a;
        private int[][] b;
        private int[][] c;
        private int a_i, a_j, b_i, b_j, c_i, c_j, size;

        private Thread thread;
        private String threadName;

        MultiplyTaskThread(String name, int[][] a, int[][] b, int[][] c, int a_i, int a_j, int b_i, int b_j, int c_i, int c_j, int size) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.a_i = a_i;
            this.a_j = a_j;
            this.b_i = b_i;
            this.b_j = b_j;
            this.c_i = c_i;
            this.c_j = c_j;
            this.size = size;
            threadName = name;
            thread = new Thread(this,"thread");
        }

        public void run() {
            int h = size/2;
            if (size <= threshold) {
                multipltySyncInternal(a, b, c, a_i, a_j, b_i, b_j, c_i, c_j, size);
            } else {
                if(threadName.equals("thread1")) {
                    //First quarter
                    multipltySyncInternal(a, b, c, a_i, a_j, b_i, b_j, c_i, c_j, h);
                    multipltySyncInternal(a, b, c, a_i, a_j + h, b_i + h, b_j, c_i, c_j, h);
                }
                else if(threadName.equals("thread2")) {
                    //Second quarter
                    multipltySyncInternal(a, b, c, a_i, a_j, b_i, b_j+h, c_i, c_j+h, h);
                    multipltySyncInternal(a, b, c, a_i, a_j + h, b_i+h, b_j + h, c_i, c_j + h, h);
                }
                else if(threadName.equals("thread3")) {
                    //Third quarter
                    multipltySyncInternal(a, b, c, a_i + h, a_j, b_i, b_j, c_i + h, c_j, h);
                    multipltySyncInternal(a, b, c, a_i + h, a_j + h, b_i + h, b_j, c_i + h, c_j, h);
                }
                else if(threadName.equals("thread4")) {
                    //Fourth quarter
                    multipltySyncInternal(a, b, c, a_i + h, a_j, b_i, b_j + h, c_i + h, c_j + h, h);
                    multipltySyncInternal(a, b, c, a_i + h, a_j + h, b_i + h, b_j + h, c_i + h, c_j + h, h);
                }
            }
        }

        public void start() {
            thread.start();
        }
    }

    public void multiplyAsync() {
        Future f = exec.submit(new MultiplyTask(a, b, c, 0, 0, 0, 0, 0, 0, a.length));
        try {
            f.get();
            exec.shutdown();
        } catch (Exception e) {
            System.exit(1);
        }
    }

    class MultiplyTask implements Runnable {
        private int[][] a;
        private int[][] b;
        private int[][] c;
        private int a_i, a_j, b_i, b_j, c_i, c_j, size;

        MultiplyTask(int[][] a, int[][] b, int[][] c, int a_i, int a_j, int b_i, int b_j, int c_i, int c_j, int size) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.a_i = a_i;
            this.a_j = a_j;
            this.b_i = b_i;
            this.b_j = b_j;
            this.c_i = c_i;
            this.c_j = c_j;
            this.size = size;
        }

        public void run() {
            int h = size/2;
            if (size <= threshold) {
                multipltySyncInternal(a, b, c, a_i, a_j, b_i, b_j, c_i, c_j, size);
            } else {
                MultiplyTask[] tasks = {
                    new MultiplyTask(a, b, c, a_i, a_j, b_i, b_j, c_i, c_j, h),
                    new MultiplyTask(a, b, c, a_i, a_j+h, b_i+h, b_j, c_i, c_j, h),

                    new MultiplyTask(a, b, c, a_i, a_j, b_i, b_j+h, c_i, c_j+h, h),
                    new MultiplyTask(a, b, c, a_i, a_j+h, b_i+h, b_j+h, c_i, c_j+h, h),

                    new MultiplyTask(a, b, c, a_i+h, a_j, b_i, b_j, c_i+h, c_j, h),
                    new MultiplyTask(a, b, c, a_i+h, a_j+h, b_i+h, b_j, c_i+h, c_j, h),

                    new MultiplyTask(a, b, c, a_i+h, a_j, b_i, b_j+h, c_i+h, c_j+h, h),
                    new MultiplyTask(a, b, c, a_i+h, a_j+h, b_i+h, b_j+h, c_i+h, c_j+h, h)
                };

                FutureTask[] fs = new FutureTask[tasks.length/2];

                for (int i = 0; i < tasks.length; i+=2) {
                    fs[i/2] = new FutureTask(new Serializer(tasks[i], tasks[i+1]), null);
                    exec.execute(fs[i/2]);
                }
                for (int i = 0; i < fs.length; ++i) {
                    fs[i].run();
                }
                try {
                    for (int i = 0; i < fs.length; ++i) {
                        fs[i].get();
                    }
                } catch (Exception e) {

                }
            }
        }
    }

    class Serializer implements Runnable {
        private MultiplyTask first, second;
        Serializer(MultiplyTask first, MultiplyTask second) {
            this.first = first;
            this.second = second;
        }
        public void run() {
            first.run();
            second.run();
        }
    }

    public boolean check(int[][] a, int[][] b, int size) {
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if(a[j][i] != b[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    public long[] benchmark() {
        long startSync = System.nanoTime();
        this.multiplySync();
        long endSync = System.nanoTime();
        long timeSync = endSync - startSync;
        int[][] res = this.getResult().clone();

        long startAsync = System.nanoTime();
        this.multiplyAsync();
        long endAsync = System.nanoTime();
        long timeAsync = endAsync - startAsync;
        int[][] resAsync = this.getResult().clone();
        if (!this.check(res, resAsync, size)) {
            System.exit(3);
        }

        long startThreading = System.nanoTime();
        this.multiplyAsyncThreads();
        long endThreading = System.nanoTime();
        long timeThreading = endThreading - startThreading;
        int[][] resAsyncThreads = this.getResult().clone();
        if (!this.check(res, resAsyncThreads, size)) {
            System.exit(3);
        }

        return new long[] { timeSync, timeAsync, timeThreading };
    }


}