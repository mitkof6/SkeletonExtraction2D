package animation;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import main.Main;

import primitives.Bone2D;



/**
 *
 * @author STANEV
 */
public class AnimationPanel extends JPanel{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
	private Graphics2D g2d;//2d graphics
    private Bone2D rootBS;
    
    private Color BONEC = Color.RED, JOINTC = Color.GREEN;
    
    /**
     * Constructor, initialize variables and run the algorithms..
     */
    public AnimationPanel(){
    	
    }

    public void render(){
    	if(Main.boneSystem!=null){
    		rootBS = Main.boneSystem.getRoot();
    		repaint();
    	}
    }
    /**
     * Render method
     */
    public void paintComponent(Graphics g){
    	Graphics2D g2d = (Graphics2D) g;
        boneDraw(rootBS, g2d);
    }
    
    private void boneDraw(Bone2D bone, Graphics2D g2d){
    	if(bone==null){
    		return;
    	}
    	/*
    	g2d.translate(bone.getX(), bone.getY());
    	g2d.rotate(bone.getA(), 0, 0);
    	
    	g2d.setColor(Color.GREEN);
    	g2d.fillRect(0, 0, 3, 3);
    	g2d.fillRect((int)bone.getL(), 0, 3, 3);
    	g2d.setColor(Color.BLACK);
    	g2d.drawLine(0, 0, (int)bone.getL(), 0);
    	
    	g2d.translate(bone.getL(), 0);
    	*/
    	g2d.setColor(JOINTC);
    	g2d.fillRect((int)bone.getX(), (int)bone.getY(), 3, 3);
    	if(bone.getParent()!=null){
    		g2d.setColor(BONEC);
    		g2d.drawLine((int)bone.getX(), (int)bone.getY(),
    				(int)bone.getParent().getX(), (int)bone.getParent().getY());
    	}
    	
    	for(Bone2D child : bone.getChild()){
    		boneDraw(child, g2d);
    	}
    }
}
