package Level;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LevelResolver {
	private List<int[]> list;
	private int[][] aux = {{0,1},{0,-1},{-1,0},{1,0}};
	
	
	public LevelResolver(){
		list = new LinkedList<int[]>();		
		list.add(new int[]{1,1,0,0,1});// N S E O
		list.add(new int[]{2,1,0,1,0});
		list.add(new int[]{3,0,1,1,0});
		list.add(new int[]{4,0,1,0,1});
		list.add(new int[]{5,1,1,0,0});
		list.add(new int[]{6,0,0,1,1});
		list.add(new int[]{7,1,1,1,1});
		
	}
	
	public  boolean resolv(Level level){
		//if(beggining[0] == )
		int[] aux = level.getBeggining();
		int prevPos = 0;
		switch(level.mat[aux[0]][aux[1]]){
		case 'N':
			aux[1] -= 1;
			prevPos= 1;
			break;
		case 'S':
			aux[1] += 1;
			prevPos= 2;
			break;
		case 'E':
			aux[0] -= 1;
			prevPos= 3;
			break;
		case 'O':
			aux[0] += 1;
			prevPos= 4;
			break;	
		}
		recurResolv(level, aux, prevPos);
			return false;
			
	}
	
	private void recurResolv(Level level,int[] pos,int prevPos){		
		if(/*termine o estoy trabado*/false)
			return;
		for(int[] elem : list){
			if(elem[prevPos] == 0){
				int[] sumVector = parser(elem,prevPos);
				if (sumVector[0] == 1)
					prevPos = 1;
				else if (sumVector[0] == -1)
					prevPos = 2;
				else if (sumVector[1] == 1)
					prevPos = 3;
				else
					prevPos = 4;
				sumVector[0] += pos[0];
				sumVector[1] += pos[1];
				if(level.mat[pos[0]][pos[1]] == ' '){
					level.mat[pos[0]][pos[1]] = (char) ('0' + (char) elem[0]);
					recurResolv(level,sumVector,prevPos);
				}				
			}
		}
	}
	private int[] parser(int[] vector,int prevPos){
		vector[prevPos] = 0;
		if(vector[0] == 7){
			return aux[prevPos - 1];
		}else{
			int[] auxer = new int[2];
			auxer[0] = -vector[1] + vector[2];
			auxer[1] = -vector[3] + vector[4];
			return auxer;
		}
	}
}
