package defaulter;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

	public static void main(String[] args/*agregar q se pase por parametro*/) {
		LevelReader lr = new LevelReader(new Level());
		Level level = null;
		level = lr.loadLevel("test.txt");
		if(level == null){
			System.out.println("Archivo Mal Formado");
		} else {
			level.print();
		}
		pres(level);
	}
		
	public static void pres(Level level){
		final Level level2 = level;
		Runnable r = new Runnable() {
			@Override
            public void run() {
                LevelDrawer cb =
                        new LevelDrawer(level2);

                JFrame f = new JFrame("level");
                f.add(cb.getGui());
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


