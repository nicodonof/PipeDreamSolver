package Level;

import java.util.LinkedList;
import java.util.List;

//import jdk.internal.org.objectweb.asm.tree.IntInsnNode;
import defaulter.Piece;

public class LevelResolver {
	private static final long TIME = 100;
	private static final double NANOMULT = 60000000000.0;
	private static int MAXSTEPSHILLCLIMBING = 5;
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
	private long MAXTIMEMINUTES;
	private long startTime;
	
	public LevelResolver(Level level, boolean progress, boolean hillClimbing, long maxtime) {
		this.level = level;
		this.progress = progress;
		MAXLENGTH = level.total();
		pieceTypes = new PieceVector<Character, Piece>();
		pieceTypes.put('7', new Piece(7, new int[] { 1, 1, 1, 1 }));
		pieceTypes.put('1', new Piece(1, new int[] { 1, 0, 0, 1 }));
		pieceTypes.put('2', new Piece(2, new int[] { 1, 0, 1, 0 }));
		pieceTypes.put('3', new Piece(3, new int[] { 0, 1, 1, 0 }));
		pieceTypes.put('4', new Piece(4, new int[] { 0, 1, 0, 1 }));
		pieceTypes.put('5', new Piece(5, new int[] { 1, 1, 0, 0 }));
		pieceTypes.put('6', new Piece(6, new int[] { 0, 0, 1, 1 }));


		this.hillClimbing = hillClimbing;
		if (hillClimbing)
			firstSol = true;
		MAXTIMEMINUTES = maxtime;
	}

	public boolean resolv() {
		startTime = System.nanoTime();
		System.out.println("Start");
		int[] aux = level.getBeggining();
		int prevPos = 0;
		switch (level.mat[aux[0]][aux[1]]) {
		case 'N':
			aux[1] -= 1;
			prevPos = 1;
			break;
		case 'S':
			aux[1] += 1;
			prevPos = 0;
			break;
		case 'E':
			aux[0] += 1;
			prevPos = 3;
			break;
		case 'W':
			aux[0] -= 1;
			prevPos = 2;
			break;
		}
		partialSolutionSteps = 0;
		
		
		if(!hillClimbing){
			recurResolv(level.getMat(), aux, null, prevPos, 0, level.getPieces().clone(), 0);
			System.out.println("Cantidad de pasos: "+ (partialSolutionSteps+1));
		}
		else{

			char[][] matVacia = new char[level.getCols()][level.getRows()];
			int[] initialPieces = level.getPieces().clone();
			copy(level.getMat(),matVacia);
			int bestSolutionSteps = 0;
			char[][] bestSolution = null;
			while((double)(System.nanoTime() - startTime)/NANOMULT <= MAXTIMEMINUTES){
				level.setPieces(initialPieces.clone());
				finish = false;
				firstSol = true;
				recurResolv(matVacia, aux, null, prevPos, 0, level.getPieces(), 0);
				int stepsForCurrentResolv = partialSolutionSteps;
				firstSol = false;
				if(finish){
					int[] positionSecond = pieceTypes.get(level.getSolMat()[aux[0]][aux[1]]).parser(prevPos, aux);				
					hillclimbing(level.getSolMat(), aux, prevPos, positionSecond, pieceTypes.get(level.getSolMat()[positionSecond[0]][positionSecond[1]]).otherEnd(positionSecond[2]));
					int stepsForCurrent = level.getSolutionSize() + stepsForCurrentResolv;
					if(stepsForCurrent > bestSolutionSteps){
						bestSolution = level.getSolMat();
						bestSolutionSteps = stepsForCurrent;
					}
						
				}
				shuffle(pieceTypes);
			}
			
			if(bestSolution != null){
				level.setSolMat(bestSolution);
			}
			System.out.println(bestSolutionSteps);
		}
		return true;
	}

	private void shuffle(PieceVector<Character, Piece> pieceTypes2) {	
		Piece pieceAux = pieceTypes2.remove((char) ('1' +(int)(Math.random() * 7)));
		pieceTypes2.put(pieceAux.getPieceIDChar(),pieceAux);
		
	}

	private void hillclimbing(char[][] solution, int[] firstPos, int prevPos, int[] secondPos, int postPos) {
		if((double)(System.nanoTime() - startTime)/NANOMULT >= MAXTIMEMINUTES)
			return ;
		List<Neighbour> neighbours = new LinkedList<Neighbour>();	
		
		stepHillNeighbour(solution, firstPos, prevPos, secondPos, postPos, neighbours);
		int max = 0;
		Neighbour maxNeighbour = null;
		if (neighbours.isEmpty()) {
			return ;
		}
		for (Neighbour neighbour : neighbours) {
			if (neighbour.length > max) {
				max = neighbour.length;
				maxNeighbour = neighbour;
			}
		}
		level.setSolMat(maxNeighbour.mat);
		level.setPieces(maxNeighbour.pieces);
		level.setSolutionSize(max);
		Piece firstPiece = pieceTypes.get(level.getSolMat()[firstPos[0]][firstPos[1]]);
		int[] auxer = firstPiece.parser(prevPos, firstPos);
		Piece secondPiece = pieceTypes.get(level.getSolMat()[auxer[0]][auxer[1]]);
		hillclimbing(level.getSolMat(), firstPos, prevPos, auxer, secondPiece.otherEnd(auxer[2]));
	}

	private void stepHillNeighbour(char[][] solution, int[] firstPos, int prevPos, int[] secondPos, int postPos, List<Neighbour> neighbours) {
		if((double)(System.nanoTime() - startTime)/NANOMULT >= MAXTIMEMINUTES)
			return ;
		
		Neighbour neighbAux = getNeighbour(solution, firstPos, prevPos, secondPos, postPos);
		if (neighbAux != null) {
			neighbours.add(neighbAux);
		}

		Piece pieceSecondPos = pieceTypes.get(solution[secondPos[0]][secondPos[1]]);
		int secondPosPrevPos = pieceSecondPos.otherEnd(postPos);

		int auxer[] = pieceSecondPos.parser(secondPosPrevPos, secondPos);
		if ((auxer[0] == -1) || (auxer[1] == -1) || (auxer[0] == level.getCols()) || (auxer[1] == level.getRows()))
			return;
		postPos = pieceTypes.get(solution[auxer[0]][auxer[1]]).otherEnd(auxer[2]);
		stepHillNeighbour(solution, secondPos, secondPosPrevPos, auxer, postPos, neighbours);
	}

	private class Neighbour {
		char[][] mat;
		int length;
		int[] pieces;

		public Neighbour(char[][] mat, int length, int[] pieces) {
			this.mat = mat;
			this.length = length;
			this.pieces = pieces;
		}
	}

	private Neighbour getNeighbour(char[][] mat, int[] posInit, int prevPos, int[] posFinal, int postPos) {
		if((double)(System.nanoTime() - startTime)/NANOMULT >= MAXTIMEMINUTES)
			return null;
		Neighbour bestPosibleSwap = null;
		int bestPosibleSwapLength = 0;
		Piece siete = pieceTypes.get('7');
		if (level.getPieces()[6] > 0) {
				if(mat[posInit[0]][posInit[1]] != '7' && mat[posInit[0]][posInit[1]] != '6' && mat[posInit[0]][posInit[1]] != '5'){
					int[] posInitNext = siete.parser(prevPos, posInit);
					if (posInitNext[0] >= 0 && posInitNext[0] < level.getCols() && posInitNext[1] >= 0 && posInitNext[1] < level.getRows()) {
						if (mat[posInitNext[0]][posInitNext[1]] == ' ' || mat[posInitNext[0]][posInitNext[1]] == '7') {
							finish = false;
							char[][] matAux = new char[level.getCols()][level.getRows()];
							copy(mat, matAux);
							int[] piecesAux = level.getPieces().clone();
							piecesAux[matAux[posInit[0]][posInit[1]] - '1'] += 1;
							matAux[posInit[0]][posInit[1]] = siete.getPieceIDChar();
							piecesAux[siete.getPieceID() - 1] -= 1;
							
							recurResolv(matAux, posInitNext, posFinal, posInitNext[2], postPos, piecesAux, 0);
							
							if (finish && partialSolutionSteps > bestPosibleSwapLength){
								bestPosibleSwap =new Neighbour(level.getMat(), partialSolutionSteps, piecesAux);
								bestPosibleSwapLength = partialSolutionSteps;
								return new Neighbour(level.getMat(), partialSolutionSteps, piecesAux);
							}
						}
					}
				}
		}
		
		for (Piece firstPiece : pieceTypes.values()) {
			for (Piece secondPiece : pieceTypes.values()) {
				if (level.getPieces()[firstPiece.getPieceID() - 1] > 0) {
	
					if (firstPiece.getDirections()[prevPos] == 1 && ((firstPiece.getPieceIDChar() != mat[posInit[0]][posInit[1]] && firstPiece.getPieceID() != 7)/* || specialCase*/)) {
						if (secondPiece.getDirections()[postPos] == 1 && ((secondPiece.getPieceIDChar() != mat[posFinal[0]][posFinal[1]] && secondPiece.getPieceID() != 7)/* || specialCase*/)) {

							int[] posFinalPrev = secondPiece.parser(postPos, posFinal);
							int[] posInitNext = firstPiece.parser(prevPos, posInit);
							if(!(posFinalPrev[0] == posInit[0] && posFinalPrev[1] == posInit[1]) && !(posInitNext[0] == posFinal[0] && posInitNext[1] == posFinal[1]))
							if (posFinalPrev[0] >= 0 && posFinalPrev[0] < level.getCols() && posFinalPrev[1] >= 0 && posFinalPrev[1] < level.getRows()) {
								if ((mat[posFinalPrev[0]][posFinalPrev[1]] == ' ' || mat[posFinalPrev[0]][posFinalPrev[1]] == '7')) {
									if (posInitNext[0] >= 0 && posInitNext[0] < level.getCols() && posInitNext[1] >= 0 && posInitNext[1] < level.getRows()) {
										if ((mat[posInitNext[0]][posInitNext[1]] == ' ' || mat[posInitNext[0]][posInitNext[1]] == '7')) {
											boolean[] validSeven = { true, true };
											if (mat[posInit[0]][posInit[1]] == '7')
												if (posInit[0] > 0 && posInit[0] < level.getCols() - 1 && posInit[1] > 0 && posInit[1] < level.getRows() - 1)
													if ((prevPos <= 1 && mat[posInit[0] + 1][posInit[1]] != ' ' && mat[posInit[0] - 1][posInit[1]] != ' ') || (prevPos >= 2 && mat[posInit[0]][posInit[1] + 1] != ' ' && mat[posInit[0]][posInit[1] - 1] != ' '))
														validSeven[0] = false;
											if (mat[posFinal[0]][posFinal[1]] == '7')
												if (posFinal[0] > 0 && posFinal[0] < level.getCols() - 1 && posFinal[1] > 0 && posFinal[1] < level.getRows() - 1)
													if ((postPos <= 1 && mat[posFinal[0] + 1][posFinal[1]] != ' ' && mat[posFinal[0] - 1][posFinal[1]] != ' ') || (postPos >= 2 && mat[posFinal[0]][posFinal[1] + 1] != ' ' && mat[posFinal[0]][posFinal[1] - 1] != ' '))
														validSeven[1] = false;

											if (validSeven[0] && validSeven[1]) {
												finish = false;
												char[][] matAux = new char[level.getCols()][level.getRows()];
												copy(mat, matAux);
												int[] piecesAux = level.getPieces().clone();
												piecesAux[matAux[posInit[0]][posInit[1]] - '1'] += 1;
												piecesAux[matAux[posFinal[0]][posFinal[1]] - '1'] += 1;
												matAux[posInit[0]][posInit[1]] = firstPiece.getPieceIDChar();
												matAux[posFinal[0]][posFinal[1]] = secondPiece.getPieceIDChar();
												piecesAux[firstPiece.getPieceID() - 1] -= 1;
												piecesAux[secondPiece.getPieceID() - 1] -= 1;

												int[] auxer = firstPiece.parser(prevPos, posInit);
												recurResolv(matAux, auxer, posFinal, auxer[2], postPos, piecesAux, 0);
											
												if (finish && partialSolutionSteps > bestPosibleSwapLength){
													bestPosibleSwap =new Neighbour(level.getMat(), partialSolutionSteps, piecesAux);
													bestPosibleSwapLength = partialSolutionSteps;
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
		}
		return bestPosibleSwap;
	}

	private void recurResolv(char[][] mat, int[] pos, int[] posFinal, int prevPos, int finalDir, int[] pieces, int stepsMade) {
		if((double)(System.nanoTime() - startTime)/NANOMULT >= MAXTIMEMINUTES && hillClimbing){
			return ;
		}
		if ((pos[0] == -1) || (pos[1] == -1) || (pos[0] == level.getCols()) || (pos[1] == level.getRows())) {
			if (!hillClimbing) {
				if (stepsMade == MAXLENGTH) {
					finish = true;
					level.setMat(mat);
					level.setSolMat(mat);
					return;
				}
				if (stepsMade > partialSolutionSteps) {
					partialSolutionSteps = stepsMade;
					level.setMat(mat);
					level.setSolMat(mat);
				}
			}
			if (firstSol) {
				level.setPieces(pieces);
				finish = true;
				level.setMat(mat);
				level.setSolMat(mat);
				partialSolutionSteps = stepsMade;
				return;
			}
			return;
		}
		boolean flasg = false;
		if (hillClimbing && !firstSol && pos[0] == posFinal[0] && pos[1] == posFinal[1] && pieceTypes.get(mat[pos[0]][pos[1]]).getDirections()[prevPos] == 1){
			flasg = true;	
			if(mat[pos[0]][pos[1]] == '7' && !(prevPos == pieceTypes.get('7').otherEnd(finalDir))){
				flasg = false;
			}
		}

	
		if (hillClimbing && !firstSol && stepsMade == MAXSTEPSHILLCLIMBING)
			return;

		if (progress){
			try {
				Thread.sleep(TIME);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			level.drawMat(mat);
		}
		for (Piece elem : pieceTypes.values()) {
			if (!finish && elem.getDirections()[prevPos] == 1) {
				int[] sumVector = elem.parser(prevPos, pos);
				int prevPosAux = sumVector[2];
				if (elem.getPieceID() == 7) {
					if (pos[0] > 0 && pos[0] < level.getCols() - 1 && pos[1] > 0 && pos[1] < level.getRows() - 1) {
						if ((prevPosAux <= 1 && ((mat[pos[0] + 1][pos[1]] != ' ' && mat[pos[0] + 1][pos[1]] != '7') || (mat[pos[0] - 1][pos[1]] != ' ' && mat[pos[0] - 1][pos[1]] != '7')) && pieces[4] > 0) || (prevPosAux >= 2 && ((mat[pos[0]][pos[1] + 1] != ' ' && mat[pos[0]][pos[1] + 1] != '7') || (mat[pos[0]][pos[1] - 1] != ' ' && mat[pos[0]][pos[1] - 1] != '7')) && pieces[5] > 0))
							ignore = true;
					} else if (pos[0] == 0 || pos[0] == level.getCols() || pos[1] == 0 || pos[1] == level.getRows()) {
						ignore = true;
					}
				}

				if (flasg || ((mat[pos[0]][pos[1]] == ' ' || mat[pos[0]][pos[1]] == '7') && pieces[elem.getPieceID() - 1] >= 1 && !ignore)) {
					char[][] matb = new char[level.getCols()][level.getRows()];
					copy(mat, matb);
					if (!flasg && mat[pos[0]][pos[1]] == '7') {
						sumVector = pieceTypes.get('7').parser(prevPos, pos);
						prevPosAux = sumVector[2];
						recurResolv(matb, sumVector, posFinal, prevPosAux, finalDir, pieces, stepsMade + 1);
						break;
					} else {
						if(!flasg){
							matb[pos[0]][pos[1]] = elem.getPieceIDChar();
							pieces[elem.getPieceID() - 1] -= 1;// Todo: analizar si convendria mover todo al 0123456
						}
						int[] yetAnotherVec = elem.parser(prevPos, pos);

						if (flasg || (!firstSol && hillClimbing && yetAnotherVec[2] == pieceTypes.get(mat[posFinal[0]][posFinal[1]]).otherEnd(finalDir) && yetAnotherVec[0] == posFinal[0] && yetAnotherVec[1] == posFinal[1])) {
							// Chequeo si me estoy chocando bien
							partialSolutionSteps = stepsMade + 1;
							level.setMat(matb);
							finish = true;
							flasg = false;
							return;
						}
						recurResolv(matb, sumVector, posFinal, prevPosAux, finalDir, pieces, stepsMade + 1);
						if (!finish)
							pieces[elem.getPieceID() - 1] += 1;
					}
				}
				ignore = false;
			}
		}
	}

	public void print2(char[][] mat) {
		for (int i = 0; i < level.getRows(); i++) {
			for (int j = 0; j < level.getCols(); j++)
				System.out.print(mat[j][i]);
			System.out.println();
		}
	}

	private void copy(char[][] mat, char[][] matb) {
		for (int i = 0; i < level.getCols(); i++) {
			for (int j = 0; j < level.getRows(); j++)
				matb[i][j] = mat[i][j];
		}
	}
}
