package defaulter;

import javax.swing.JFrame;
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
		/*if(args.length<1)
		*	return;
		*file=args[0];
		*if(args[1].equals("progress"))
		*	
		*
		*
		*
		*/
		level = LevelReader.loadLevel(new Level(),"test2.txt");
		if(level == null){
			System.out.println("Archivo Mal Formado");
			return;
		}
		f = new JFrame("level");
		LevelResolver ls = new LevelResolver(level,false,true,10);
		level.setLd(new LevelDrawer(level,ls));
		
		
		long startTime = System.nanoTime();
	
        ls.resolv();
		long stopTime = System.nanoTime();
		System.out.println("Termino en " + ((double)(stopTime - startTime) / 1000000000.0));
		level.setMat(level.getSolMat());
		level.drawMat(level.getSolMat());
		
		ls.print2(level.getSolMat());
	}
		
	
}


