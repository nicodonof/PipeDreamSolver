package defaulter;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LevelReader lr = new LevelReader(new Level());
		Level level = null;
		level = lr.loadLevel("test.txt");
		level.print();
	}

}
