package Level;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import com.sun.org.apache.bcel.internal.generic.GOTO;

import defaulter.Piece;



public class LevelResolver /*implements Callable<String>*/{
	private static final long TIME = 100;
	private static final int MAXSTEPSHILLCLIMBING = 6;
	private static int MAXLENGTH;
	private PieceVector<Character, Piece> pieceTypes;
	private Level level;
	public int partialSolutionSteps;
	private boolean progress = false;
	private boolean finish = false;
	private boolean ignore = false;
	public int cont = 0;
	private boolean hillClimbing = false;
	private boolean firstSol = false;
	
	public LevelResolver(Level level, boolean progress, boolean hillClimbing){
		this.level = level;
		this.progress = progress;
		MAXLENGTH = level.total();
		pieceTypes = new PieceVector<Character, Piece>();		
		pieceTypes.put('1',new Piece(1,new int[]{1,0,0,1}));// N S E W
		pieceTypes.put('2',new Piece(2,new int[]{1,0,1,0}));
		pieceTypes.put('3',new Piece(3,new int[]{0,1,1,0}));
		pieceTypes.put('4',new Piece(4,new int[]{0,1,0,1}));
		pieceTypes.put('5',new Piece(5,new int[]{1,1,0,0}));
		pieceTypes.put('6',new Piece(6,new int[]{0,0,1,1}));
		pieceTypes.put('7',new Piece(7,new int[]{1,1,1,1}));
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
		partialSolutionSteps = 0;
		recurResolv(level.getMat(), aux, null, prevPos, level.getPieces().clone(),0);
		System.out.println("Encontre una sol de : " + partialSolutionSteps + " pasos");
		System.out.println(level.getPieces()[0]);
		firstSol = false;
		if(hillClimbing){
			finish = false;
			int[] sumVector = pieceTypes.get(level.getSolMat()[aux[0]][aux[1]]).parser(prevPos,aux);
												
			hillclimbing(level.getSolMat(),aux,prevPos,sumVector,pieceTypes.get(level.getSolMat()[sumVector[0]][sumVector[1]]).otherEnd(sumVector[2]));
			
		}
		return false;
	}
	
	private void hillclimbing(char[][] solution,int[] firstPos,int prevPos,int[] secondPos, int postPos ) {
		// TODO Auto-generated method stub
		List<Neighbour> neighbours = new LinkedList<Neighbour>();
		
		
		stepHillNeighbour(solution,firstPos,prevPos,secondPos,postPos,neighbours);			
		int max = 0;
		Neighbour maxNeighbour = null;
		if(neighbours.isEmpty()){
			//level.setSolMat(level.getMat());
			return;
		}
		for(Neighbour neighbour : neighbours){
			System.out.println("TENGO UN NEIGH DE PESO:" + neighbour.length);
			if(neighbour.length > max){
				max = neighbour.length;
				maxNeighbour = neighbour;
			}
		}
		level.setSolMat(maxNeighbour.mat);
		level.setPieces(maxNeighbour.pieces);
		Piece firstPiece = pieceTypes.get(level.getMat()[firstPos[0]][firstPos[1]]);
		int[] auxer = firstPiece.parser(prevPos, firstPos);
		Piece secondPiece = pieceTypes.get(level.getMat()[auxer[0]][auxer[1]]);
		hillclimbing(level.getMat(), firstPos, prevPos, auxer, secondPiece.otherEnd(auxer[2]));
	}

	private void stepHillNeighbour(char[][] solution,int[] firstPos,int prevPos,int[] secondPos, int postPos,List<Neighbour> neighbours){		
		Neighbour neighbAux = getNeighbour(solution, firstPos,prevPos, secondPos,postPos);
		if(neighbAux != null){
			System.out.println("Encontre un neighbour saliendo de: " + firstPos[0] + " " + firstPos[1] + ", y llegando a: "  + secondPos[0] + " " + secondPos[1]);
			System.out.println("Peso: " + neighbAux.length);
			print2(neighbAux.mat);
			neighbours.add(neighbAux);
		}
		
		Piece pieceSecondPos = pieceTypes.get(solution[secondPos[0]][secondPos[1]]);
		int secondPosPrevPos = pieceSecondPos.otherEnd(postPos);

		int auxer[] = pieceSecondPos.parser(secondPosPrevPos, secondPos);
		if((auxer[0] == -1) || (auxer[1]  == -1)	|| (auxer[0] == level.getCols()) || (auxer[1] == level.getRows()))
			return; // Todo: esto se podria hacer en menos comparaciones*/

		postPos = pieceTypes.get(solution[auxer[0]][auxer[1]]).otherEnd(auxer[2]);
		stepHillNeighbour(solution, secondPos, secondPosPrevPos , auxer ,postPos,neighbours);
	}
	
	private class Neighbour{
		char[][] mat;
		int length;
		int[] pieces;
		
		public Neighbour(char[][] mat,int length, int[] pieces){
			this.mat = mat;
			this.length = length;
			this.pieces = pieces;
		}
	}

	
	private Neighbour getNeighbour(char[][] mat,int[] posInit,int prevPos,int[] posFinal,int postPos){	
		//System.out.println("Entro a testear neighbours " + posInicial[0] + " " + posInicial[1] + " " + prevPos + " , y final " + posFinal[0] + " " + posFinal[1] + " " +postPos);
		
		for(Piece firstPiece : pieceTypes.values()){
			for(Piece secondPiece : pieceTypes.values()){
				boolean specialCase = false;
				if(level.getPieces()[firstPiece.getIdPieza()-1]>0){
					if((secondPiece.getIdPiezaChar() == mat[posFinal[0]][posFinal[1]] && level.getPieces()[secondPiece.getIdPieza()-1]>=0) 
							|| (secondPiece.getIdPiezaChar() != mat[posFinal[0]][posFinal[1]] && level.getPieces()[secondPiece.getIdPieza()-1]>0))
	
						if (firstPiece.getIdPieza() == 7 && secondPiece.getIdPiezaChar() == mat[posFinal[0]][posFinal[1]])
								specialCase = true;
	
						if (firstPiece.getDirecciones()[prevPos] == 1 && ((firstPiece.getIdPiezaChar() != mat[posInit[0]][posInit[1]] && firstPiece.getIdPieza() != 7) || specialCase)) {
							if (secondPiece.getDirecciones()[postPos] == 1 && ((secondPiece.getIdPiezaChar() != mat[posFinal[0]][posFinal[1]] && secondPiece.getIdPieza() != 7) || specialCase)) {
	
								int[] posFinalPrev = secondPiece.parser(postPos, posFinal);
								int[] posInitNext = firstPiece.parser(prevPos, posInit);
	
								if (posFinalPrev[0] >= 0 && posFinalPrev[0] < level.getCols() && posFinalPrev[1] >= 0 && posFinalPrev[1] < level.getRows()) {
									if (specialCase || (mat[posFinalPrev[0]][posFinalPrev[1]] == ' ' || mat[posFinalPrev[0]][posFinalPrev[1]] == '7')) {
										if (posInitNext[0] >= 0 && posInitNext[0] < level.getCols() && posInitNext[1] >= 0 && posInitNext[1] < level.getRows()) {
											if (specialCase || (mat[posInitNext[0]][posInitNext[1]] == ' ' || mat[posInitNext[0]][posInitNext[1]] == '7')) {
												boolean[] validSeven = { true, true };
												if (mat[posInit[0]][posInit[1]] == '7')
													if (posInit[0] > 0 && posInit[0] < level.getCols() - 1 && posInit[1] > 0 && posInit[1] < level.getRows() - 1)
														if ((prevPos <= 1 && mat[posInit[0] + 1][posInit[1]] != ' ' && mat[posInit[0] - 1][posInit[1]] != ' ') || (prevPos >= 2 && mat[posInit[0]][posInit[1] + 1] != ' ' && mat[posInit[0]][posInit[1] - 1] != ' '))
															validSeven[0] = false;
												if (mat[posFinal[0]][posFinal[1]] == '7')
													if (posFinal[0] > 0 && posFinal[0] < level.getCols() - 1 && posFinal[1] > 0 && posFinal[1] < level.getRows() - 1)
														if ((postPos <= 1 && mat[posFinal[0] + 1][posFinal[1]] != ' ' && mat[posFinal[0] - 1][posFinal[1]] != ' ') || (postPos >= 2 && mat[posFinal[0]][posFinal[1] + 1] != ' ' && mat[posFinal[0]][posFinal[1] - 1] != ' '))
															validSeven[1] = false;
	
												if (validSeven[0] && validSeven[1] || (specialCase && mat[posFinal[0]][posFinal[1]] == '7')) {
													finish = false;
													char[][] matAux = new char[level.getCols()][level.getRows()];
													copy(mat, matAux);
													int[] piecesAux = level.getPieces().clone();
													piecesAux[matAux[posInit[0]][posInit[1]] - '1'] += 1;
													piecesAux[matAux[posFinal[0]][posFinal[1]] - '1'] += 1;
													matAux[posInit[0]][posInit[1]] = firstPiece.getIdPiezaChar();
													matAux[posFinal[0]][posFinal[1]] = secondPiece.getIdPiezaChar();
													piecesAux[firstPiece.getIdPieza() - 1] -= 1;
													piecesAux[secondPiece.getIdPieza() - 1] -= 1;
	
													int[] auxer = firstPiece.parser(prevPos, posInit);
													if (specialCase && mat[posFinal[0]][posFinal[1]] == '7')
														recurResolv(matAux, auxer, posInit, auxer[2], piecesAux, 0);
													else
														recurResolv(matAux, auxer, posFinal, auxer[2], piecesAux, 0);
													if (finish)
														return new Neighbour(level.getMat(), partialSolutionSteps, piecesAux);
												}
											}
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
	
	
	private void recurResolv(char[][] mat,int[] pos, int[] posFinal, int prevPos, int[] pieces, int stepsMade){
		//System.out.println("Entro al resolv, pasos dados hasta ahora: " + stepsMade);
		//System.out.println(pieces[0]);
		if((pos[0] == -1) || (pos[1] == -1)	|| (pos[0] == level.getCols()) || (pos[1] == level.getRows())){
			if(!hillClimbing){
				if(stepsMade == MAXLENGTH /*|| piecesLeft == 1 fijarse caso mas 1 salida*/){
					finish = true;
					level.setMat(mat);
					level.setSolMat(mat);
					return;
				}
				if(stepsMade > partialSolutionSteps){
					partialSolutionSteps = stepsMade;
					level.setMat(mat);
					level.setSolMat(mat);
					System.out.println("Encontre solucion mejor de: " + stepsMade +" steps.");
				}
			}
			if(firstSol){
				level.setPieces(pieces);
				finish = true;
				System.out.println("Encontre una Solucion Normal");
				print2(mat);
				level.setMat(mat);
				level.setSolMat(mat);
				partialSolutionSteps = stepsMade;
				return ;
			}
			return ;
		}
		boolean flasg = false;
		if(hillClimbing && !firstSol && pos[0] == posFinal[0] && pos[1] == posFinal[1] && pieceTypes.get(mat[pos[0]][pos[1]]).getDirecciones()[prevPos] == 1)
			flasg = true;
		
		if(hillClimbing && !firstSol && (stepsMade - partialSolutionSteps) == MAXSTEPSHILLCLIMBING)
			return ;
		
		/*if(finish){
			return piecesLeft;
		}*/
		if(progress){
			try {
			    Thread.sleep(TIME);
			}catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			//level.setMat(mat);
			level.drawMat(mat);
		}
		/*cont+=1;
		if(cont%10000000 == 0){
			//level.setMat(mat);
			System.out.println("Sigo funcando" + stepsMade);
		}*/
		for(Piece elem : pieceTypes.values()){
			if(!finish && elem.getDirecciones()[prevPos] == 1){
				int[] sumVector = elem.parser(prevPos,pos);				
				int prevPosAux = sumVector[2];
				if(elem.getIdPieza() == 7){
					if(pos[0]>0 && pos[0]<level.getCols()-1 && pos[1]> 0 && pos[1]<level.getRows()-1){
						if((prevPosAux <= 1 && ((mat[pos[0]+1][pos[1]] != ' ' && mat[pos[0]+1][pos[1]] != '7') || (mat[pos[0]-1][pos[1]] != ' ' && mat[pos[0]-1][pos[1]] != '7')) && pieces[4] > 0) ||
							(prevPosAux >= 2 && ((mat[pos[0]][pos[1]+1] != ' ' && mat[pos[0]][pos[1]+1] != '7') || (mat[pos[0]][pos[1]-1] != ' ' && mat[pos[0]][pos[1]-1] != '7')) && pieces[5] > 0))
							ignore = true;
					} else if(pos[0] == 0 || pos[0] == level.getCols() || pos[1] == 0 || pos[1] == level.getRows()){
						ignore = true;
					}
				}
				
				if(flasg || ((mat[pos[0]][pos[1]] == ' ' || mat[pos[0]][pos[1]] == '7') && pieces[elem.getIdPieza()-1] >= 1 && !ignore)){
					char[][] matb = new char[level.getCols()][level.getRows()];
					copy(mat,matb);
					if(mat[pos[0]][pos[1]]=='7'){
						sumVector = pieceTypes.get('7').parser(prevPos,pos);
						prevPosAux = sumVector[2];
						recurResolv(matb,sumVector,posFinal,prevPosAux,pieces,stepsMade + 1); 
						break;
					} else {
						matb[pos[0]][pos[1]] = elem.getIdPiezaChar();
						pieces[elem.getIdPieza()-1] -= 1;//Todo: analizar si convendria mover todo al 0123456
						
						int[] yetAnotherVec = elem.parser(prevPos,pos);//TODO: meter esto adentro del if de abajo
						
						if(flasg || (!firstSol && hillClimbing && pieceTypes.get(mat[posFinal[0]][posFinal[1]]).getDirecciones()[yetAnotherVec[2]] == 1 && 
								yetAnotherVec[0]== posFinal[0] && yetAnotherVec[1]== posFinal[1])){
							//Chequeo si me estoy chocando bien
							partialSolutionSteps = stepsMade + 1;
							System.out.println("Encontre forma de juntarlos");
							level.setMat(matb);
							finish = true;
							return ;
						}
						recurResolv(matb,sumVector,posFinal,prevPosAux,pieces,stepsMade + 1);
						if(!finish)
							pieces[elem.getIdPieza()-1] += 1;
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



	/*public String call() throws Exception {
		resolv();
		return "asd";
	}*/
}
