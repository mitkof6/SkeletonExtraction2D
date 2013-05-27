package skeleton2D;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Scanner;

import primitives.Segment;
import primitives.Vector2D;
import primitives.Vertex2D;



public class Load2D {
	
	private ArrayList<Vertex2D> vertices;
	private ArrayList<Vertex2D> polygon;
	private ArrayList<Segment> edges;
	
	public Load2D(String path, int zoom) throws FileNotFoundException{
		
		//initialize
		Scanner inputStream = new Scanner(new File(path));
		
		vertices = new ArrayList<>();
		polygon = new ArrayList<>();
		edges = new ArrayList<>();

		//read file
		while(inputStream.hasNext()){
			
			String[] line = inputStream.nextLine().split(" ");
			if(line.length==1){
				continue;
			}
			
			if(line[0].charAt(0)=='v'){
				vertices.add(new Vertex2D(zoom*Double.parseDouble(line[1]), 
						zoom*Double.parseDouble(line[2])));
				
			}else if(line[0].startsWith("pl")){
				polygon.add(vertices.get(Integer.parseInt(line[1])-1));
				
			}

		}
		inputStream.close();
		
		//generate edges and normals
		generateEdges(this.polygon, edges);
	}

	private void computeNormalVector(Vertex2D p1, Vertex2D p2){
    	//variables
    	double dy = p2.getY()-p1.getY();
    	double dx = p2.getX()-p1.getX();
    	
    	Vector2D n = new Vector2D(-dy, dx);
    	n.normalize();
    	
    	p1.setN1(n);
    	p2.setN2(n);
    	
    }
    
  
    private void generateEdges(ArrayList<Vertex2D> points, ArrayList<Segment> edges){
    	
    	for(int i = 0;i<points.size()-1;i++){
    		edges.add(new Segment(points.get(i), points.get(i+1)));
    		computeNormalVector(points.get(i), points.get(i+1));
    		
    	}
    	
    	//last
    	edges.add(new Segment(points.get(points.size()-1),
    			points.get(0)));
    	computeNormalVector(points.get(points.size()-1), points.get(0));
    }
    
	public ArrayList<Vertex2D> getVertices() {
		return vertices;
	}

	public ArrayList<Vertex2D> getPolygon() {
		return polygon;
	}
	
	public ArrayList<Segment> getEdges(){
		return edges;
	}
}
