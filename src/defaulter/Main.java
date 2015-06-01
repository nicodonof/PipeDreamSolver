package defaulter;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import defaulter.TimeOut.MyJob;
import Level.Level;
import Level.LevelDrawer;
import Level.LevelReader;
import Level.LevelResolver;
//prevPos = sumVector[0]==0?sumVector[1]==1?3:4:sumVector[0]==1?1:2;
public class Main {
	private static Level level;
	private static JFrame f;
	
	public static void main(String[] args/*agregar q se pase por parametro*/) {
		level = null;
		boolean progress = true;
		level = LevelReader.loadLevel(new Level(),"test3.txt");
		if(level == null){
			System.out.println("Archivo Mal Formado");
			return;
		} else {
			level.print();
		}
		f = new JFrame("level");
		LevelResolver ls = new LevelResolver(level,true,true);
		level.setLd(new LevelDrawer(level,ls));
		
		
		long startTime = System.nanoTime();
	//	Future<String> control = Executors.newSingleThreadExecutor().submit(ls);
	//	String result = new String();
     /*   try {

            result = control.get(200000000, TimeUnit.MILLISECONDS);

        } catch (TimeoutException ex) {

            control.cancel(true);

        } catch (InterruptedException ex) {

        } catch (ExecutionException ex) {

        }*/
		ls.resolv();
		long stopTime = System.nanoTime();
	 	//System.out.println(result);
		System.out.println((stopTime - startTime) / Math.pow(10, 9));
		//level.setMat(level.getSolMat());
		level.drawMat(level.getSolMat());
		System.out.println("termino");
		ls.print2(level.getSolMat());
		System.out.println(ls.cont);
	}
		
	
}


