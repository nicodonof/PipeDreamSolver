package Level;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;


public class LevelDrawer {
		private final JPanel gui;
	    private JPanel levelBoard;
	    private ImageIcon[] imgs;
	    
	    public LevelDrawer(Level level) {
	    	gui = new JPanel(new GridLayout(level.getRows(), level.getCols()));
	    	imgs = new ImageIcon[19];
	    	try {
	            BufferedImage sprite = ImageIO.read(new File("Pipes01.png"));

	            final int width = 48;
	            final int height = 48;

	            int x = 0;
	            int y = 0;
	            
	  		    for(int i = 0; i < 6; i++, x+=48){
	            	imgs[i] = new ImageIcon(sprite.getSubimage(x, y, width, height));
	            	imgs[i+6] = new ImageIcon(sprite.getSubimage(x, y+48, width, height));
	            	imgs[i+12] = new ImageIcon(sprite.getSubimage(x, y+96, width, height));
	    	    }
	  		    imgs[18] = new ImageIcon("Nada.png");
	    
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		    draw(level);
		}
	    
	    public void draw(Level level){
	    	char[][] mat = level.getMat();
		    int aux = 0;
	    	for(int i = 0; i < level.getRows();i++){
	    		for(int j = 0; j < level.getCols(); j++){
	    			switch(mat[i][j]){
	    			case '#': 
	    				aux = 17;
	    				break;
	    			case 'N':
	    				aux = 14;
	    				break;
	    			case 'S':
	    				aux = 13;
	    				break;
	    			case 'W':
	    				aux = 12;
	    				break;
	    			case 'E':
	    				aux = 11;
	    				break;
	    			case '6':
	    				aux = 10;
	    				break;
	    			case '5':
	    				aux = 9;
	    				break;
	    			case '1':
	    				aux = 8;
	    				break;
	    			case '2':
	    				aux = 7;
	    				break;
	    			case '4':
	    				aux = 6;
	    				break;
	    			case '3':
	    				aux = 5;
	    				break;
	    			case '7':
	    				aux = 0;
	    				break;
	    			case ' ':
	    				aux = 18;
	    				break;
	    			}
	    				gui.add(new JLabel(imgs[aux]));
	    		}
	        }    	
	    }

	        
	    		        
	    public final JComponent getLevelBoard() {
	        return levelBoard;
	    }

	    public final JComponent getGui() {
	        return gui;
	    }

}
