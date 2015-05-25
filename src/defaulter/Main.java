package defaulter;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

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
		level = LevelReader.loadLevel(new Level(),"test2.txt");
		if(level == null){
			System.out.println("Archivo Mal Formado");
			return;
		} else {
			level.print();
		}
		f = new JFrame("level");
		f.setVisible(true);
		level.setLd(new LevelDrawer(level));
		level.getLd().draw(level);
		pres();
		LevelResolver ls = new LevelResolver(level,progress);
		long startTime = System.nanoTime();
		ls.resolv();
		long stopTime = System.nanoTime();
		System.out.println((stopTime - startTime) / Math.pow(10, 9));
		level.getLd().draw(level);
		level.setMat(level.getSolMat());
		System.out.println("termino");
		ls.print2(level.getSolMat());
		System.out.println(ls.cont);
	}
		
	public static void pres(){
		Runnable r = new Runnable() {
			public void run() {
                f.add(level.getLd().getGui());
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.pack();
                f.setMinimumSize(f.getSize());
                
            }
        };
        SwingUtilities.invokeLater(r);
	}
}


