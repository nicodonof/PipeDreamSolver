package Level;

import java.util.LinkedList;
import java.util.List;

import defaulter.Piece;

public class LevelResolver {
	private static final long TIME = 100;
	private List<Piece> list;
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
		list = new LinkedList<Piece>();		
		list.add(new Piece(1,new int[]{1,0,0,1}));// N S E W
		list.add(new Piece(2,new int[]{1,0,1,0}));
		list.add(new Piece(3,new int[]{0,1,1,0}));
		list.add(new Piece(4,new int[]{0,1,0,1}));
		list.add(new Piece(5,new int[]{1,1,0,0}));
		list.add(new Piece(6,new int[]{0,0,1,1}));
		list.add(new Piece(7,new int[]{1,1,1,1}));
		this.hillClimbing = hillClimbing;
		if(hillClimbing)
			firstSol = true;
	}
	
	public boolean resolv(){
		System.out.println("Start");
		int[] aux = level.getBeggining(); // TODO : analizar si esto se puede hacer en O(1)
		int prevPos = 0;
		switch(level.mat[aux[0]][aux[1]]){
		case 'N':
			aux[1] -= 1;
			prevPos= 1;
			break;
		case 'S':
			aux[1] += 1;
			prevPos= 0;
			break;
		case 'E':
			aux[0] += 1;
			prevPos= 3;
			break;
		case 'W':
			aux[0] -= 1;
			prevPos= 2;
			break;	
		}
		recurResolv(level.getMat(), aux, null, prevPos, level.getPieces(),numberOfPieces,-2);	
		firstSol = false;
		if(hillClimbing){
			finish = false;
			int[] sumVector = list.get(level.getSolMat()[aux[0]][aux[1]] - '1').parser(prevPos,aux);
						
						
			List<Neighbour> neighbours = new LinkedList<Neighbour>();
			stepHillNeighbour(level.getSolMat(),aux,prevPos,sumVector,list.get(level.getSolMat()[sumVector[0]][sumVector[1]] - '1').otherEnd(sumVector[2]),neighbours);			
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
			System.out.println("Encontre un neighbour saliendo de: " + firstPos[0] + " " + firstPos[1] + ", y llegando a: "  + secondPos[0] + " " + secondPos[1]);
			print2(neighbAux.mat);
			neighbours.add(neighbAux);
		}
		if((secondPos[0]  == 0) || (secondPos[1]  == 0)	|| (secondPos[0] == level.getCols()) || (secondPos[1] == level.getRows()))
			return; // Todo: esto se podria hacer en menos comparaciones
		Piece pieceSecondPos = list.get(solution[secondPos[0]][secondPos[1]] - '1');
		int secondPosPrevPos = pieceSecondPos.otherEnd(postPos);

		int auxer[] = pieceSecondPos.parser(secondPosPrevPos, secondPos);
		postPos = list.get(solution[auxer[0]][auxer[1]] - '1').otherEnd(auxer[2]);
		
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
		System.out.println("Entro a testear neighbours " + posInicial[0] + " " + posInicial[1] + " " + prevPos + " , y final " + posFinal[0] + " " + posFinal[1] + " " +postPos);
		for(Piece firstPiece : list){
			for(Piece secondPiece : list){
				if(level.getPieces()[firstPiece.getIdPieza()-1]>0 && level.getPieces()[secondPiece.getIdPieza()-1]>0){
					if(firstPiece.getDirecciones()[prevPos] == 1 && firstPiece.getIdPiezaChar() != mat[posInicial[0]][posInicial[1]] && firstPiece.getIdPieza() != 7){
						if(secondPiece.getDirecciones()[postPos] == 1 && secondPiece.getIdPiezaChar() != mat[posFinal[0]][posFinal[1]] && secondPiece.getIdPieza() != 7){
							int[] rexua  = secondPiece.parser(postPos,posFinal);
							if(rexua[0]>0 && rexua[0]<level.getCols()-1 && rexua[1]> 0 && rexua[1]<level.getRows()-1){
								if(level.getMat()[rexua[0]][rexua[1]] == ' ' || level.getMat()[rexua[0]][rexua[1]] == '7'){
									char[][] matAux = new char[level.getCols()][level.getRows()];
									copy(mat,matAux);
									level.getPieces()[matAux[posInicial[0]][posInicial[1]] - '1'] += 1;
									level.getPieces()[matAux[posFinal[0]][posFinal[1]] - '1'] += 1;		
									matAux[posInicial[0]][posInicial[1]] =firstPiece.getIdPiezaChar();
									matAux[posFinal[0]][posFinal[1]] =secondPiece.getIdPiezaChar();
									level.getPieces()[firstPiece.getIdPieza()-1] -= 1;
									level.getPieces()[secondPiece.getIdPieza()-1] -= 1;
									
									int[] auxer = firstPiece.parser(prevPos, posInicial);
																
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
		System.out.println("SALGO DE NEIGHBOUR");
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
				System.out.println("Encontre una Solucion Normal");
				print2(mat);
				level.setMat(mat);
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
		for(Piece elem : list){			
			if(!finish && elem.getDirecciones()[prevPos] == 1){
				int prevPosAux;
				int[] sumVector = elem.parser(prevPos,pos);				
				prevPosAux = sumVector[2];
				if(elem.getIdPieza() == 7){
					if(pos[0]>0 && pos[0]<level.getCols()-1 && pos[1]> 0 && pos[1]<level.getRows()-1){
						if((prevPosAux < 3 && ((mat[pos[0]+1][pos[1]] != ' ' && mat[pos[0]+1][pos[1]] != '7') || (mat[pos[0]-1][pos[1]] != ' ' && mat[pos[0]-1][pos[1]] != '7')) && level.getPieces()[4] > 0) ||
								(prevPosAux > 2 && ((mat[pos[0]][pos[1]+1] != ' ' && mat[pos[0]][pos[1]+1] != '7') || (mat[pos[0]][pos[1]-1] != ' ' && mat[pos[0]][pos[1]-1] != '7')) && level.getPieces()[5] > 0))
							ignore = true;
					} else if(pos[0] == 0 || pos[0] == level.getCols() || pos[1] == 0 || pos[1] == level.getRows()){
						ignore = true;
					} 
				}
				
				if((mat[pos[0]][pos[1]] == ' ' || mat[pos[0]][pos[1]] == '7') && level.getPieces()[elem.getIdPieza()-1] >= 1 && !ignore){
					
					char[][] matb = new char[level.getCols()][level.getRows()];
					copy(mat,matb);
					if(mat[pos[0]][pos[1]]=='7'){
						sumVector = list.get(6).parser(prevPos,pos);
						prevPosAux = sumVector[2];
						chain = recurResolv(matb,sumVector,posFinal,prevPosAux,pieces,piecesLeft - 1,maxSteps); 
						break;
					} else {
						matb[pos[0]][pos[1]] = (char) ('0' + (char) elem.getIdPieza());//Todo: hacer funcion getIdPiezaChar
						level.getPieces()[elem.getIdPieza()-1] -= 1;//Todo: analizar si convendria mover todo al 0123456
						
						int[] yetAnotherVec = elem.parser(prevPos,pos);
						
						if(!firstSol && hillClimbing && list.get(mat[posFinal[0]][posFinal[1]] - '1').getDirecciones()[yetAnotherVec[2]] == 1 && yetAnotherVec[0]== posFinal[0] && yetAnotherVec[1]== posFinal[1]){
							//Chequeo si me estoy chocando bien
							level.setMat(matb);
							finish = true;
							return true;
						}
						chain = recurResolv(matb,sumVector,posFinal,prevPosAux,pieces,piecesLeft - 1,maxSteps-1);
						level.getPieces()[elem.getIdPieza()-1] += 1;
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
}
