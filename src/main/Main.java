package main;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import primitives.Segment;
import primitives.Vertex2D;
import skeleton2D.BoneSystem;
import skeleton2D.Load2D;
import skeleton2D.Skeleton2D;


/**
 *
 * @author STANEV
 */
public class Main {
	
	public static Skeleton2D skeleton;
	public static BoneSystem boneSystem;
	public static ArrayList<Vertex2D> vertices;
    public static ArrayList<Segment> edges;
	
	    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	
		Window application = new Window();
		application.setVisible(true);

    }

    public static void generateSceleton(String path, int zoom, double step, int sampling,
    		int pushingFactor, int iteration, int distanceTolerance, int rootTolerance)
    				throws FileNotFoundException{
    	
    	//load model
		Load2D loader = new Load2D(path, zoom);
		vertices = loader.getPolygon();
		edges = loader.getEdges();
		System.out.println("Loaded\nVertices: "+vertices.size()+"\nEdges: "+edges.size());
		
        //get skeleton
        skeleton = new Skeleton2D(step, sampling, pushingFactor, iteration,
        		distanceTolerance, rootTolerance);
        skeleton.getVDS(vertices, edges);
    }
    
    public static void generateBoneSystem(){
   	
    	boneSystem = new BoneSystem(skeleton.getRoot().get(0), skeleton.getBones());
    	boneSystem.generateBoneSystem();
    }
}
