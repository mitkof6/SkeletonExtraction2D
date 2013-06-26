package main;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import animation.BoneFunctions;

import extract.Load2D;
import gui.InfoScreen;
import gui.MainWindow;
import primitives.Segment;
import primitives.Vertex;
import skeleton2D.Bone;
import skeleton2D.BoneSystem;
import skeleton2D.Skeleton2D;


/**
 * This is the main class
 * 
 * @author Jim Stanev
 */
public class Main {
	
	/**
	 * skeleton instance for the skeleton extraction algorithm
	 */
	public static Skeleton2D skeleton;
	/**
	 * bone system instance for generation of hierarchy tree system
	 */
	private static BoneSystem boneSystem;
	/**
	 * the vertices of the model
	 */
	public static ArrayList<Vertex> vertices;
	/**
	 * the edges of the model
	 */
	public static ArrayList<Segment> edges;
	/**
	 * the root of the bone system
	 */
    public static Bone root;
    /**
     * windows global height and width
     */
    public static final int HEIGHT = 700, WIDTH = 700;
    /**
     * skin attachment index
     */
    public static int SKIN_DEPENDENCES = 2;
    /**
     * printing pattern
     */
    public static final String SEP = "############################";
	
	    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	//info screen
    	InfoScreen infoScreen = new InfoScreen();
    	infoScreen.setVisible(true);
    	
    	//main window
		MainWindow application = new MainWindow();
		application.setVisible(true);

    }

    /**
     * Generates the skeleton of a model
     * 
     * @param path path to file
     * @param zoom multiplies coordinates by zoom
     * @param step the step of convergence toward local minimum
     * @param sampling rerify of the skeleton
     * @param pushingFactor initial pushing toward inner S
     * @param iteration the number of iteration threshold
     * @param distanceTolerance for merging nodes
     * @param rootTolerance for merging final nodes
     * @throws FileNotFoundException if can't open the file
     */
    public static void generateSceleton(String path, int zoom, double step, int sampling,
    		int pushingFactor, int iteration, int distanceTolerance, int rootTolerance)
    				throws FileNotFoundException{
    	
    	System.out.println(SEP);
    	//load model
		Load2D loader = new Load2D(path, zoom);
		vertices = loader.getPolygon();
		edges = loader.getEdges();
		System.out.println("Loaded\nVertices: "+vertices.size()+"\nEdges: "+edges.size());
		
        //get skeleton
        skeleton = new Skeleton2D(step, sampling, pushingFactor, iteration,
        		distanceTolerance, rootTolerance);
        skeleton.getVDS(vertices, edges);
        
        System.out.println(SEP);
    }
    
    /**
     * Generates the hierarchy bone system. If bones are not connected it may be
     * stuck in inf loop
     */
    public static void generateBoneSystem(){
    	System.out.println(SEP);
    	System.out.println("Generate bone system");
    	boneSystem = new BoneSystem(skeleton.getRoot().get(0), skeleton.getBones());
    	boneSystem.generateBoneSystem();
    	root = boneSystem.getRoot();
    	BoneFunctions.printBoneSystem(root, 1);
    	System.out.println(SEP);
    
    }
    
    /**
     * Prints animation controls
     */
    public static void printKeyInfo(){
    	System.out.println(SEP);
    	System.out.println("W S A D: movements");
    	System.out.println("N: select a bone");
    	System.out.println("P: print bone system");
    	System.out.println("E: animation start/stop");
    	System.out.println("R: record key frame");
    	System.out.println("Q: print key frames");
    	//System.out.println("Up Down: root (x, y)");
    	System.out.println(SEP);
    }
}
