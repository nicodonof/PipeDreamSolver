package Level;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LevelResolver {
	private List list;
	
	LevelResolver(){
		list = new LinkedList<Integer[]>();		
		list.add(new int[]{1,1,0,0,1});// N S E O
		list.add(new int[]{2,1,0,1,0});
		list.add(new int[]{3,0,1,1,0});
		list.add(new int[]{4,0,1,0,1});
		list.add(new int[]{5,1,1,0,0});
		list.add(new int[]{6,0,0,1,1});
		list.add(new int[]{7,1,1,1,1});
		
	}
	
	public static boolean resolv(Level level){
		//if(beggining[0] == )
			return false;
	}
	
	private static void recurResolv(Level level,int[] pos,int[] prevPos){
		if()
			return;
		for(posible){
			recurResolv(level,posible.algo,pos);
		}
	}
}
