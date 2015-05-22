package Level;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LevelReader {
	
	public static Level loadLevel(Level level,String string){
		String line = null;
		try {
            FileReader fileReader = new FileReader(string);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            String[] split = line.split(",");
            level.setCols(Integer.parseInt(split[0]));
            level.setRows(Integer.parseInt(split[1]));
            char[][] matAux = new char[level.getCols()][level.getRows()];
            for(int i = 0; i < level.getRows(); i++){
            	String aux = bufferedReader.readLine();
            	for (int j = 0; j < level.getCols(); j++){
            		matAux[j][i] = aux.charAt(j);
            	}
            }
            level.setMat(matAux);
            level.setSolMat(matAux);
            if(!level.check()){
            	bufferedReader.close();
            	return null;
            }
            int[] pieAux = new int[7];
            for(int i = 0; i < 7; i++){
            	pieAux[i] = Integer.valueOf(bufferedReader.readLine());
            }
            level.setPieces(pieAux);
            if(bufferedReader.readLine() != null){
            	bufferedReader.close();
            	return null;
            }
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
        }
        catch(IOException ex) {
        }
		catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		return level;
	}
	
}
