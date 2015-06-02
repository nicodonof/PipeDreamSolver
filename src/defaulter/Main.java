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
		boolean progress = false;
		boolean hillClimbing = false;
		boolean notNumber = false;
		long time = 0;
		String file = null;
		int aux = 0;
		if(args.length<1)
			return;
		file=args[0];
		
		try{
			if(args.length>2)
				aux = Integer.parseInt(args[2]);
		} catch (NumberFormatException eoeo){
			notNumber = true;
		}
		
		switch(args.length){
			case(2):
				if(!args[1].equals("exact")){
					System.out.println("Invalid Arguments");
					return;
				}
				break;
			case(3):
				if(notNumber && args[1].equals("exact") && args[2].equals("progress"))
					progress = true;
				else if(args[1].equals("approx") && aux > 0){
					hillClimbing = true;
					time = (long) aux;
				}
			
				else {
					System.out.println("Invalid Arguments");
					return;
				}
				break;
			case(4):
				if(args[1].equals("approx") && aux > 0 && args[3].equals("progress")){
						hillClimbing = true;
						time = (long) aux;
						progress = true;
				} else {
					System.out.println("Invalid Arguments");
					return;
				}
				break;
		}
			
		
		
		level = LevelReader.loadLevel(new Level(),file);
		if(level == null){
			System.out.println("Archivo Mal Formado");
			return;
		}
		f = new JFrame("level");
		LevelResolver ls = new LevelResolver(level,progress,hillClimbing,time);
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


