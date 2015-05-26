package Level;

import java.util.LinkedList;
import java.util.List;

public class LevelResolver {
	private static final long TIME = 100;
	private List<int[]> list;
	private int[][] aux = {{0,1},{0,-1},{-1,0},{1,0}};
	private Level level;
	private int numberOfPieces, total;
	private boolean progress = false;
	private boolean finish = false;
	private boolean ignore = false;
	public int cont = 0;
	private boolean hillClimbing = false;
	
	public LevelResolver(Level level, boolean progress, boolean hillClimbing){
		this.level = level;
		this.progress = progress;
		numberOfPieces = total = level.total();
		list = new LinkedList<int[]>();		
		list.add(new int[]{1,1,0,0,1});// N S E W
		list.add(new int[]{2,1,0,1,0});
		list.add(new int[]{3,0,1,1,0});
		list.add(new int[]{4,0,1,0,1});
		list.add(new int[]{5,1,1,0,0});
		list.add(new int[]{6,0,0,1,1});
		list.add(new int[]{7,1,1,1,1});
		this.hillClimbing = hillClimbing;
	}
	
	public boolean resolv(){
		System.out.println("Start");
		int[] aux = level.getBeggining();
		int prevPos = 0;
		switch(level.mat[aux[0]][aux[1]]){
		case 'N':
			aux[1] -= 1;
			prevPos= 2;
			break;
		case 'S':
			aux[1] += 1;
			prevPos= 1;
			break;
		case 'E':
			aux[0] += 1;
			prevPos= 4;
			break;
		case 'W':
			aux[0] -= 1;
			prevPos= 3;
			break;	
		}
		recurResolv(level.getMat(), aux, prevPos,numberOfPieces);	
		if(hillClimbing){
			finish = false;
			
			int[] sumVector = parser(list.get(level.getSolMat()[aux[0]][aux[1]] - '1').clone(),prevPos);
			
			sumVector[0] += aux[0];
			sumVector[1] += aux[1];
			int[] sumVector2 = parser(list.get(level.getSolMat()[sumVector[0]][sumVector[1]] - '1').clone(),sumVector[2]);
			if(sumVector2[2] % 2 == 0)
				sumVector2[2]-=1;
			else 
				sumVector2[2]+=1;
			getNeighbour(level.getSolMat(),aux,prevPos, sumVector,sumVector2[2]);
			/*List<char[][]> neighbours = new LinkedList<char[][]>();
			fillNeighbours(neighbours);
			for(char[][] neighbour : neighbours){
				//consigo el de mayor peso
			}*/			
		}
		return false;
	}
	
	private void fillNeighbours(List neighbours) {
		
		
	}

//	private void something(char[][] mat,int[] vecPosFinal,int[] posInicial,int[] posFinal,int[] firstPiece,int[] secondPiece,int i){
		
	//}
	
	private void getNeighbour(char[][] mat,int[] posInicial,int prevPos,int[] posFinal,int postPos){
		System.out.println("post pos: " + postPos);
		for(int[] firstPiece : list){
			for(int[] secondPiece : list){
				int[] vecPosInicial = list.get(mat[posInicial[0]][posInicial[1]] - '0');
				int[] vecPosFinal = list.get(mat[posFinal[0]][posFinal[1]] - '0');
				int[][] littleVec = {{0,-1},{0,1},{1,0},{-1,0}};
				if(firstPiece[prevPos] == 1 && firstPiece[0] != mat[posInicial[0]][posInicial[1]] - '0' && firstPiece[0] != 7){
					if(secondPiece[postPos] == 1 && secondPiece[0] != mat[posFinal[0]][posFinal[1]] - '0' && secondPiece[0] != 7){
						char[][] matAux = new char[level.getCols()][level.getRows()];
						copy(mat,matAux);
						matAux[posInicial[0]][posInicial[1]] =(char) ('0' + firstPiece[0]);
						matAux[posFinal[0]][posFinal[1]] =(char) ('0' + secondPiece[0]);
						int[] auxer = null;
						for(int i=1;i<5;i++){
							if(firstPiece[i] == 1 && i!= prevPos);
								auxer = parser(firstPiece.clone(), prevPos);
						}
						auxer[0] += posInicial[0];
						auxer[1] += posInicial[1];
						recurComplete(matAux,auxer,posFinal, auxer[2], numberOfPieces);
						return;
					}
				}							
			}
		}
	}
	
	private void recurComplete(char[][] mat,int[] pos,int[] posFinal,int prevPos, int piecesLeft){		
		if((pos[0] == -1) || (pos[1] == -1)	|| (pos[0] == level.getCols()) || (pos[1] == level.getRows())){
			//Me fui del tablero
			return;
		}
		if(finish){
			return;
		}
		
		if(progress){
			try {
			    Thread.sleep(TIME);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			level.setMat(mat);
		}
		cont+=1;
		if(cont%10000000 == 0){
			level.setMat(mat);
		}
		for(int[] elem : list){
			if(elem[prevPos] == 1){
				int prevPosAux;
				int[] sumVector = parser(elem.clone(),prevPos);				
				sumVector[0] += pos[0];
				sumVector[1] += pos[1];
				prevPosAux = sumVector[2];
				if(elem[0] == 7){
					if(pos[0]>0 && pos[0]<level.getCols()-1 && pos[1]> 0 && pos[1]<level.getRows()-1){
						if((prevPosAux < 3 && ((mat[pos[0]+1][pos[1]] != ' ' && mat[pos[0]+1][pos[1]] != '7') || (mat[pos[0]-1][pos[1]] != ' ' && mat[pos[0]-1][pos[1]] != '7')) && level.getPieces()[4] > 0) ||
								(prevPosAux > 2 && ((mat[pos[0]][pos[1]+1] != ' ' && mat[pos[0]][pos[1]+1] != '7') || (mat[pos[0]][pos[1]-1] != ' ' && mat[pos[0]][pos[1]-1] != '7')) && level.getPieces()[5] > 0))
							ignore = true;
					} else if(pos[0] == 0 || pos[0] == level.getCols() || pos[1] == 0 || pos[1] == level.getRows()){
						ignore = true;
					} 
				}
				
				if((mat[pos[0]][pos[1]] == ' ' || mat[pos[0]][pos[1]] == '7') && level.getPieces()[elem[0]-1] >= 1 && !ignore){
					
					char[][] matb = new char[level.getCols()][level.getRows()];
					copy(mat,matb);
					if(mat[pos[0]][pos[1]]=='7'){
						sumVector = parser(list.get(6).clone(),prevPos);
						sumVector[0] += pos[0];
						sumVector[1] += pos[1];
						prevPosAux = sumVector[2];
						recurComplete(matb,sumVector,posFinal,prevPosAux,piecesLeft - 1);
						break;
					} else {
						matb[pos[0]][pos[1]] = (char) ('0' + (char) elem[0]);
						level.getPieces()[elem[0]-1] -= 1;
						
						int[] yetAnotherVec = parser(elem.clone(),prevPos);
						
						if(list.get(mat[posFinal[0]][posFinal[1]] -  '0' - 1)[yetAnotherVec[2]] == 1 && yetAnotherVec[0] + pos[0]== posFinal[0] && yetAnotherVec[1] + pos[1]== posFinal[1]){
							//Chequeo si me estoy chocando bien
							level.setSolMat(matb);
							finish = true;
							return;
						}
						
						recurComplete(matb,sumVector,posFinal,prevPosAux,piecesLeft - 1);
						level.getPieces()[elem[0]-1] += 1;
					}
				}
				ignore = false;
			}
		}
	}
	
	private void recurResolv(char[][] mat,int[] pos,int prevPos, int piecesLeft){		
		if((pos[0] == -1) || (pos[1] == -1)	|| (pos[0] == level.getCols()) || (pos[1] == level.getRows())){
			if(piecesLeft == 0 || piecesLeft == 1){
		//		level.setMat(mat);
				level.setSolMat(mat);
				finish = true;
				return;
			}else if(piecesLeft < numberOfPieces){
				numberOfPieces = piecesLeft;
	//			level.setMat(mat);
				level.setSolMat(mat);
			}
			if(hillClimbing)
				finish = true;
			return;
		}
		if(finish){
			return;
		}
		if(progress){
			try {
			    Thread.sleep(TIME);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			level.setMat(mat);
		}
		cont+=1;
		if(cont%10000000 == 0){
			level.setMat(mat);
		}
		for(int[] elem : list){
			
			if(elem[prevPos] == 1){
				int prevPosAux;
				int[] sumVector = parser(elem.clone(),prevPos);				
				sumVector[0] += pos[0];
				sumVector[1] += pos[1];
				prevPosAux = sumVector[2];
				if(elem[0] == 7){
					if(pos[0]>0 && pos[0]<level.getCols()-1 && pos[1]> 0 && pos[1]<level.getRows()-1){
						if((prevPosAux < 3 && ((mat[pos[0]+1][pos[1]] != ' ' && mat[pos[0]+1][pos[1]] != '7') || (mat[pos[0]-1][pos[1]] != ' ' && mat[pos[0]-1][pos[1]] != '7')) && level.getPieces()[4] > 0) ||
								(prevPosAux > 2 && ((mat[pos[0]][pos[1]+1] != ' ' && mat[pos[0]][pos[1]+1] != '7') || (mat[pos[0]][pos[1]-1] != ' ' && mat[pos[0]][pos[1]-1] != '7')) && level.getPieces()[5] > 0))
							ignore = true;
					} else if(pos[0] == 0 || pos[0] == level.getCols() || pos[1] == 0 || pos[1] == level.getRows()){
						ignore = true;
					} 
				}
				
				if((mat[pos[0]][pos[1]] == ' ' || mat[pos[0]][pos[1]] == '7') && level.getPieces()[elem[0]-1] >= 1 && !ignore){
					
					char[][] matb = new char[level.getCols()][level.getRows()];
					copy(mat,matb);
					if(mat[pos[0]][pos[1]]=='7'){
						sumVector = parser(list.get(6).clone(),prevPos);
						sumVector[0] += pos[0];
						sumVector[1] += pos[1];
						prevPosAux = sumVector[2];
						recurResolv(matb,sumVector,prevPosAux,piecesLeft - 1);
						break;
					} else {
						matb[pos[0]][pos[1]] = (char) ('0' + (char) elem[0]);
						level.getPieces()[elem[0]-1] -= 1;
						recurResolv(matb,sumVector,prevPosAux,piecesLeft - 1);
						level.getPieces()[elem[0]-1] += 1;
					}
				}
				ignore = false;
			}
		}
	}
	
	

	public void print2(char[][] mat){
		for(int i = 0; i < level.getRows(); i++){
			for (int j = 0; j < level.getCols(); j++)
				System.out.print(mat[j][i]);
			System.out.println();
		}
	}
	
	private void copy(char[][] mat, char[][] matb){
		for(int i = 0; i < level.getCols(); i++){
			for (int j = 0; j < level.getRows(); j++)
				matb[i][j] = mat[i][j];
		}
	}
	
	private int[] parser(int[] vector,int prevPos){
		//
		vector[prevPos] = 0; 
		int[] auxer = new int[3];
		if(vector[0] == 7){
			auxer[0] = aux[prevPos - 1][0];
			auxer[1] = aux[prevPos - 1][1];
		}else{
			auxer[0] = vector[3] - vector[4];
			auxer[1] = -vector[1] + vector[2];
		}
		
		if (auxer[0] == 1)
			auxer[2] = 4;
		else if (auxer[0] == -1)
			auxer[2] = 3;
		else if (auxer[1] == 1)
			auxer[2] = 1;
		else
			auxer[2] = 2;
		return auxer;
	}
}
