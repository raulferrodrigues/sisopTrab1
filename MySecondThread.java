
public class MySecondThread extends Thread {
	
	int i;
	int j;
	int k;
	int mres [][];
	int m1 [][];
	int m2 [][];

	public MySecondThread(int mres[][], int m1[][], int m2[][]) {
		i = 1;
		j = 0;
		k = 0;
		this.mres = mres;
		this.m1 = m1;
		this.m2 = m2;
		start();
	}

	public void run() {
		int inc;
		if(mres.length%2 == 0) 
		{inc = mres.length;}
		else {inc = mres.length-1;}
		
		while (i < inc){
			System.out.println("2");
			for (int j=0 ; j<mres[0].length; j++) {
				for (k=0 ; k<m2.length; k++) {
					mres[i][j] += m1[i][k] * m2[k][j];
				}
			}
			i = i+2;
		}

	}

}
