package defaulter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LevelReader {
	private Level level;
	
	public LevelReader(Level level){
		this.level = level;
	}
	
	public Level loadLevel(String string){
		String line = null;
		try {
            FileReader fileReader = new FileReader(string);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            String[] split = line.split(",");
            level.setRows(Integer.parseInt(split[0]));
            level.setCols(Integer.parseInt(split[1]));
            char[][] matAux = new char[level.getRows()][level.getCols()];
            for(int i = 0; i < level.getRows(); i++){
            	String aux = bufferedReader.readLine();
            	for (int j = 0; j < level.getCols(); j++){
            		matAux[i][j] = aux.charAt(j);
            	}
            }
            level.setMat(matAux);
            int[] pieAux = new int[7];
            for(int i = 0; i < 7; i++){
            	pieAux[i] = Integer.valueOf(bufferedReader.readLine());
            }
            level.setPieces(pieAux);
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
        }
        catch(IOException ex) {
        }
		return level;
	}
}
