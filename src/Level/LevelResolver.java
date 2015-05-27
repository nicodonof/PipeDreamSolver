package Level;

import java.util.LinkedList;
import java.util.List;

public class LevelResolver {
	private static final long TIME = 80;
	private List<int[]> list;
	private int[][] aux = {{0,1},{0,-1},{-1,0},{1,0}};
	private Level level;
	private int numberOfPieces, total;
	private boolean progress = false;
	private boolean finish = false;
	private boolean ignore = false;
	public int cont = 0;
	private boolean hillClimbing = false;
	private boolean firstSol = false;
	
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
		if(hillClimbing)
			firstSol = true;
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
		recurResolv(level.getMat(), aux, null, prevPos, level.getPieces(),numberOfPieces,-2);	
		firstSol = false;
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
						
			List<Neighbour> neighbours = new LinkedList<Neighbour>();
			stepHillNeighbour(level.getSolMat(),aux,prevPos,sumVector,sumVector2[2],neighbours);			
			int max = 0;
			Neighbour maxNeighbour;
			for(Neighbour neighbour : neighbours){
				if(neighbour.length > max){
					max = neighbour.length;
					maxNeighbour = neighbour;
				}
			}
		}
		return false;
	}
	
	private void stepHillNeighbour(char[][] solution,int[] firstPos,int prevPos,int[] secondPos, int postPos,List<Neighbour> neighbours){
		Neighbour neighbAux = getNeighbour(solution, firstPos,prevPos, secondPos,postPos);
		if(neighbAux != null){
			System.out.println("Encontre un neighbour");
			print2(neighbAux.mat);
			neighbours.add(neighbAux);
		}				
		if((secondPos[0]  == 0) || (secondPos[1]  == 0)	|| (secondPos[0] == level.getCols()) || (secondPos[1] == level.getRows()))
			return; // Todo: esto se podria hacer en menos comparaciones
		int secondPosPrevPos = 0;
		int[] vectorSecondPos = list.get(solution[secondPos[0]][secondPos[1]] - '1');
		for(int i=1;i<5;i++){
			if(vectorSecondPos[i] == 1 && i!= postPos){
				secondPosPrevPos = i;
			}				
		}
		int auxer[] = parser(vectorSecondPos.clone(),secondPosPrevPos);
		
		auxer[0] += secondPos[0];
		auxer[1] += secondPos[1];
		System.out.println("auxer del neighbour: " + auxer[0] + " " + auxer[1]);
		for(int i=1;i<5;i++){
			if(list.get(solution[auxer[0]][auxer[1]] - '1')[i] == 1 && i!= auxer[2]){
				postPos = i;
			}
		}
		System.out.println("Entro al prox con, pp: " + auxer[2]);
		stepHillNeighbour(solution, secondPos, secondPosPrevPos , auxer ,postPos,neighbours);
	}
	
	private class Neighbour{
		char[][] mat;
		int length;
		
		public Neighbour(char[][] mat,int length){
			this.mat = mat;
			this.length = length;
		}
	}

	
	private Neighbour getNeighbour(char[][] mat,int[] posInicial,int prevPos,int[] posFinal,int postPos){
		finish = false;
		System.out.println("Entro a testear neighbours " + posInicial[0] + " " + posInicial[1]);
		for(int[] firstPiece : list){
			for(int[] secondPiece : list){
				if(level.getPieces()[firstPiece[0]-1]>0 && level.getPieces()[secondPiece[0]-1]>0){
					if(firstPiece[prevPos] == 1 && firstPiece[0] != mat[posInicial[0]][posInicial[1]] - '0' && firstPiece[0] != 7){
						if(secondPiece[postPos] == 1 && secondPiece[0] != mat[posFinal[0]][posFinal[1]] - '0' && secondPiece[0] != 7){
							int[] rexua  = parser(secondPiece.clone(), postPos);
							rexua[0]+= posFinal[0];
							rexua[1]+= posFinal[1];
							if(rexua[0]>0 && rexua[0]<level.getCols()-1 && rexua[1]> 0 && rexua[1]<level.getRows()-1){
								if(level.getMat()[rexua[0]][rexua[1]] == ' ' || level.getMat()[rexua[0]][rexua[1]] == '7'){
									char[][] matAux = new char[level.getCols()][level.getRows()];
									copy(mat,matAux);
									level.getPieces()[matAux[posInicial[0]][posInicial[1]] - '1'] += 1;
									level.getPieces()[matAux[posFinal[0]][posFinal[1]] - '1'] += 1;		
									matAux[posInicial[0]][posInicial[1]] =(char) ('0' + firstPiece[0]);
									matAux[posFinal[0]][posFinal[1]] =(char) ('0' + secondPiece[0]);
									level.getPieces()[firstPiece[0]] -= 1;
									level.getPieces()[secondPiece[0]] -= 1;
									int[] auxer = null;
									for(int i=1;i<5;i++){
										if(firstPiece[i] == 1 && i!= prevPos);
											auxer = parser(firstPiece.clone(), prevPos);
									}
									auxer[0] += posInicial[0];
									auxer[1] += posInicial[1];
																
									
									if(recurResolv(matAux,auxer,posFinal, auxer[2], level.getPieces(),numberOfPieces,7)){
										return new Neighbour(level.getMat(),4);
									}																	
								}
							}
						}
					}
				}										
			}
		}
		return null;
	}
	
	
	private boolean recurResolv(char[][] mat,int[] pos, int[] posFinal, int prevPos, int[] pieces, int piecesLeft, int maxSteps){		
		if((pos[0] == -1) || (pos[1] == -1)	|| (pos[0] == level.getCols()) || (pos[1] == level.getRows())){
			if(!hillClimbing){
				if(piecesLeft == 0 /*|| piecesLeft == 1 fijarse caso mas 1 salida*/){
					finish = true;
					numberOfPieces = piecesLeft;
					level.setMat(mat);
					level.setSolMat(mat);
					return true;
				} 
				if(piecesLeft < numberOfPieces){
					numberOfPieces = piecesLeft;
					level.setMat(mat);
					level.setSolMat(mat);
				}
			}
			if(firstSol){
				finish = true;
				print2(mat);
				level.setSolMat(mat);
				return true;
			}
			return false;
		}
		
		if(hillClimbing && maxSteps <= 0 && !firstSol)
			return false;
		
		if(finish){
			return true;
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
		boolean chain = false;
		for(int[] elem : list){
			
			if(!finish && elem[prevPos] == 1){
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
						chain = recurResolv(matb,sumVector,posFinal,prevPosAux,pieces,piecesLeft - 1,maxSteps);
						break;
					} else {
						matb[pos[0]][pos[1]] = (char) ('0' + (char) elem[0]);
						level.getPieces()[elem[0]-1] -= 1;
						
						int[] yetAnotherVec = parser(elem.clone(),prevPos);
						
						if(!firstSol && hillClimbing && list.get(mat[posFinal[0]][posFinal[1]] - '1')[yetAnotherVec[2]] == 1 && yetAnotherVec[0] + pos[0]== posFinal[0] && yetAnotherVec[1] + pos[1]== posFinal[1]){
							//Chequeo si me estoy chocando bien
							level.setMat(matb);
							finish = true;
							return true;
						}
						chain = recurResolv(matb,sumVector,posFinal,prevPosAux,pieces,piecesLeft - 1,maxSteps-1);
						level.getPieces()[elem[0]-1] += 1;
					}
				}
				ignore = false;
			}
		}
		return chain;
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
