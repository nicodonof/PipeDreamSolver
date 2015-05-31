package defaulter;

public class Piece {
	
	private int idPieza;
	private int[] direcciones;
	private int[][] vectoricitos = {{0,1},{0,-1},{-1,0},{1,0}};
	
	public Piece(int idPieza, int[] direcciones){
		this.idPieza = idPieza;
		this.direcciones = direcciones;
	}
	
	public int otherEnd(int endDirection){
		if(idPieza == 7){
			if(endDirection%2==0)
				return ++endDirection;
			else return --endDirection;
		}
		for(int i=0;i<4;i++){
			if(direcciones[i] == 1 && i!= endDirection){
				return i;
			}
		}
		return -1;//error
	}
	
	public int[] parser(int prevPos,int[] pos){
		//Devuelve un vector con la nueva pos y la nueva prev pos en funcion a donde estabas tu prev pos y la pieza en el lugar
		int[] dirClone = direcciones.clone();
		dirClone[prevPos] = 0; 
		int[] auxer = new int[3];
		if(idPieza == 7){
			auxer[0] = vectoricitos[prevPos][0];
			auxer[1] = vectoricitos[prevPos][1];
		}else{
			auxer[0] = dirClone[2] - dirClone[3];
			auxer[1] = -dirClone[0] + dirClone[1];
		}
		
		if (auxer[0] == 1)
			auxer[2] = 3;
		else if (auxer[0] == -1)
			auxer[2] = 2;
		else if (auxer[1] == 1)
			auxer[2] = 0;
		else
			auxer[2] = 1;
		if(pos!= null){
			auxer[0] += pos[0];
			auxer[1] += pos[1];
		}
		return auxer;
	}
	
	public int getIdPieza() {
		return idPieza;
	}
	
	public char getIdPiezaChar() {
		return (char) (idPieza + '0');
	}

	public int[] getDirecciones() {
		return direcciones;
	}

	public void nextStep(){
		
	}
}
