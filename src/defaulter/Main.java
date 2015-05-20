package defaulter;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import Level.Level;
import Level.LevelDrawer;
import Level.LevelReader;

public class Main {
	private static Level level;
	
	public static void main(String[] args/*agregar q se pase por parametro*/) {
		level = null;
		level = LevelReader.loadLevel(new Level(),"test.txt");
		if(level == null){
			System.out.println("Archivo Mal Formado");
		} else {
			level.print();
		}
		level.setLd(new LevelDrawer(level));
		pres();
	}
		
	public static void pres(){
		Runnable r = new Runnable() {
			@Override
            public void run() {
                
                JFrame f = new JFrame("level");
                f.add(level.getLd().getGui());
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLocationByPlatform(true);

                f.pack();
                f.setMinimumSize(f.getSize());
                f.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
	}
}


