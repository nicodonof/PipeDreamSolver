package defaulter;

public class Level {
	private int rows;
	private int cols;
	private char[][] mat;
	private int[] pieces;
	
	public void print(){
		System.out.println(rows + ", " + cols);
		for(int i = 0; i < rows; i++){
			for (int j = 0; j< cols; j++)
				System.out.print(mat[i][j]);
			System.out.println();
		}
		if(pieces != null){
			for(int i = 0; i<7; i++){
				System.out.println(pieces[i]);
			}
		}
	}
	
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getCols() {
		return cols;
	}
	public void setCols(int cols) {
		this.cols = cols;
	}
	public char[][] getMat() {
		return mat;
	}
	public void setMat(char[][] mat) {
		this.mat = mat;
	}
	public int[] getPieces() {
		return pieces;
	}
	public void setPieces(int[] pieces) {
		this.pieces = pieces;
	}
	
	
}
