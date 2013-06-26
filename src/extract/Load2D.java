package extract;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Scanner;

import primitives.Segment;
import primitives.Vector2D;
import primitives.Vertex;



public class Load2D {
	
	private ArrayList<Vertex> vertices;
	private ArrayList<Vertex> polygon;
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
				vertices.add(new Vertex(zoom*Double.parseDouble(line[1]), 
						zoom*Double.parseDouble(line[2])));
				
			}else if(line[0].startsWith("pl")){
				polygon.add(vertices.get(Integer.parseInt(line[1])-1));
				
			}

		}
		inputStream.close();
		
		//generate edges and normals
		generateEdges(this.polygon, edges);
	}

	private void computeNormalVector(Vertex p1, Vertex p2){
    	//variables
    	double dy = p2.getY()-p1.getY();
    	double dx = p2.getX()-p1.getX();
    	
    	Vector2D n = new Vector2D(-dy, dx);
    	n.normalize();
    	
    	p1.setN1(n);
    	p2.setN2(n);
    	
    }
    
  
    private void generateEdges(ArrayList<Vertex> points, ArrayList<Segment> edges){
    	
    	for(int i = 0;i<points.size()-1;i++){
    		edges.add(new Segment(points.get(i).getPosition(), points.get(i+1).getPosition()));
    		computeNormalVector(points.get(i), points.get(i+1));
    		
    	}
    	
    	//last
    	edges.add(new Segment(points.get(points.size()-1).getPosition(),
    			points.get(0).getPosition()));
    	computeNormalVector(points.get(points.size()-1), points.get(0));
    }
    
	public ArrayList<Vertex> getVertices() {
		return vertices;
	}

	public ArrayList<Vertex> getPolygon() {
		return polygon;
	}
	
	public ArrayList<Segment> getEdges(){
		return edges;
	}
}
