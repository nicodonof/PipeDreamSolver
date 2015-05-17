package defaulter;

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
	}

}
