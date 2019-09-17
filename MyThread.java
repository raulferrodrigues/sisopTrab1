
public class MyThread extends Thread {

	int i;
	int j;
	int k;
	int mres [][];
	int m1 [][];
	int m2 [][];

	public MyThread(int mres[][], int m1[][], int m2[][]) {
		i = 0;
		j = 0;
		k = 0;
		this.mres = mres;
		this.m1 = m1;
		this.m2 = m2;
		start();
	}

	public void run() {
		
		while (i < mres.length){
			System.out.println("1");
			for (int j=0 ; j<mres[0].length; j++) {
				for (k=0 ; k<m2.length; k++) {
					mres[i][j] += m1[i][k] * m2[k][j];
				}
			}
			i = i+2;
		}

	}
}
