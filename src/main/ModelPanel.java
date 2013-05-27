package main;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import primitives.Point;
import primitives.Vertex2D;
import primitives.Segment;


/**
 *
 * @author STANEV
 */
public class ModelPanel extends JPanel{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private ArrayList<Vertex2D> vertices;
	private ArrayList<Point> joints, root;
    private ArrayList<Segment> edges, bones;
    
    @SuppressWarnings("unused")
	private Graphics2D g2d;//2d graphics
    
    private Color BONEC = Color.black, VERTEXC = Color.GREEN, ROOTC = Color.RED,
    	EDGEC = Color.MAGENTA, JOINTC = Color.BLUE;	
    
    /**
     * Constructor, initialize variables and run the algorithms..
     * 
     */
    public ModelPanel(){
        //init
        edges = new ArrayList<>();
        vertices = new ArrayList<>();
       
        joints = new ArrayList<>();
        bones = new ArrayList<Segment>();
        root = new ArrayList<>();
        
        g2d = (Graphics2D) this.getGraphics();

    }
    
    public void render(){
    	edges = Main.edges;
    	vertices = Main.vertices;
    	
    	joints = Main.skeleton.getJoints();
    	bones = Main.skeleton.getBones();
    	root = Main.skeleton.getRoot();
    	
        repaint();
    }
    
    /**
     * Render method
     */
    public void paintComponent(Graphics g){
    	Graphics2D g2d = (Graphics2D) g;
    	//g2d.translate(10, 100);
    	
        //draw points
    	g2d.setColor(VERTEXC);
    	for(Point p : vertices){
    		g2d.fillRect((int)p.getX(), 
        			(int)p.getY(), 3, 3);
    	}
       
        
        //draw lines
        g2d.setColor(EDGEC);
        for(Segment s : edges){
        	g2d.drawLine((int)s.getLeft().getX(), (int)s.getLeft().getY(),
        			(int)s.getRight().getX(), (int)s.getRight().getY());
        }
        
        
        //draw joints
        g2d.setColor(JOINTC);
        for(Point p : joints){
        	g2d.fillRect((int)p.getX(), 
        			(int)p.getY(), 3, 3);
        }
        
        //draw root
        g2d.setColor(ROOTC);
        for(Point p : root){
        	g2d.fillRect((int)p.getX(), 
        			(int)p.getY(), 5, 5);
        }
        
        //draw bones
        g2d.setColor(BONEC);
        for(Segment r : bones){
        	g2d.drawLine((int)r.getLeft().getX(), (int)r.getLeft().getY(),
        			(int)r.getRight().getX(), (int)r.getRight().getY());
        }
    }   

}
