package main;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JPanel;

import primitives.Point;
import primitives.Vertex2D;
import primitives.Segment;
import skeleton2D.Load2D;
import skeleton2D.Skeleton2D;


/**
 *
 * @author STANEV
 */
public class Panel extends JPanel{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    ArrayList<Vertex2D> vertices;
	private ArrayList<Point> joints, root;
    private ArrayList<Segment> edges, bones;
    @SuppressWarnings("unused")
	private Graphics2D g2d;//2d graphics
    
    
    
    /**
     * Constructor, initialize variables and run the algorithms..
     * 
     * @param width of window
     * @param height of window
     */
    public Panel(int width, int height){

    	
    	
        //init
        edges = new ArrayList<>();
        vertices = new ArrayList<>();
        g2d = (Graphics2D) this.getGraphics();
        joints = new ArrayList<>();
        bones = new ArrayList<Segment>();
        root = new ArrayList<>();
        
        /*
        vertices.add(new Vertex2D(140, 120));
        vertices.add(new Vertex2D(300, 150));
        vertices.add(new Vertex2D(400, 90));
        vertices.add(new Vertex2D(300, 300));
        vertices.add(new Vertex2D(300, 200));
        vertices.add(new Vertex2D(200, 200));
        vertices.add(new Vertex2D(150, 150));
        vertices.add(new Vertex2D(90, 120));
        //vertex.add(new Point(300, 150));
        generateEdges(vertices, edges);
        */
    }
    
    public void generateSceleton(String path, int zoom, double step, int sampling,
    		int pushingFactor, int iteration, int distanceTolerance, int rootTolerance){
    	
    	//load model
        try {
			Load2D loader = new Load2D(path, zoom);
			vertices = loader.getPolygon();
			edges = loader.getEdges();
			//generateEdges(vertices, edges);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
        //get skeleton
        Skeleton2D skeleton = new Skeleton2D(step, sampling, pushingFactor, iteration,
        		distanceTolerance, rootTolerance);
        skeleton.getVDS(vertices, edges);
        bones = skeleton.getBones();
        joints = skeleton.getJoints();
        root = skeleton.getRoot();
        repaint();
    }
    
    /**
     * Render method
     */
    public void paintComponent(Graphics g){
    	Graphics2D g2d = (Graphics2D) g;
    	//g2d.translate(10, 100);
    	
        //draw points
    	g2d.setColor(Color.red);
    	for(Point p : vertices){
    		g2d.fillRect((int)p.getX(), 
        			(int)p.getY(), 3, 3);
    	}
       
        
        //draw lines
        g2d.setColor(Color.blue);
        for(Segment s : edges){
        	g2d.drawLine((int)s.getLeft().getX(), (int)s.getLeft().getY(),
        			(int)s.getRight().getX(), (int)s.getRight().getY());
        }
        
        //draw joints
        g2d.setColor(Color.green);
        for(Point p : joints){
        	g2d.fillRect((int)p.getX(), 
        			(int)p.getY(), 3, 3);
        }
        
      //draw root
        g2d.setColor(Color.RED);
        for(Point p : root){
        	g2d.fillRect((int)p.getX(), 
        			(int)p.getY(), 5, 5);
        }
        
        //draw bones
        g2d.setColor(Color.yellow);
        for(Segment r : bones){
        	g2d.drawLine((int)r.getLeft().getX(), (int)r.getLeft().getY(),
        			(int)r.getRight().getX(), (int)r.getRight().getY());
        }

    } 
}
