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
 *
 * @author STANEV
 */
public class Main {
	
	public static Skeleton2D skeleton;
	private static BoneSystem boneSystem;
	public static ArrayList<Vertex> vertices;
	public static ArrayList<Segment> edges;
    public static Bone root;
    
    public static final int HEIGTH = 700, WIDTH = 700;
    
    public static int SKIN_DEPENDENCES = 2;
    
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
    
    public static void generateBoneSystem(){
    	System.out.println(SEP);
    	System.out.println("Generate bone system");
    	boneSystem = new BoneSystem(skeleton.getRoot().get(0), skeleton.getBones());
    	boneSystem.generateBoneSystem();
    	root = boneSystem.getRoot();
    	BoneFunctions.printBoneSystem(root, 1);
    	System.out.println(SEP);
    
    }
    
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
