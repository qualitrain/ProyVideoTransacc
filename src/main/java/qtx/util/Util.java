package qtx.util;

public class Util {

	public static void hacerPausaAleatoria() {
		long num = (long) (Math.random() * 100000L);
		int milis = (int) (num % 1000);
		try {
			Thread.sleep(milis);
		}
		catch(InterruptedException iex) {}
		
	}

}
