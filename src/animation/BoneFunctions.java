package animation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import primitives.Vertex;

import skeleton2D.Bone;
import animation.SkinBoneBinding;
import Jama.Matrix;

/**
 * Some static method used in the project
 * 
 * @author Jim Stanev
 */
public class BoneFunctions {

	public static int totalBones;
	/**
	 * used to find minimum distanced bones
	 */
	private static HashMap<Double, Bone> weightedBones;
	
	@SuppressWarnings("resource")
	/**
	 * This static method loads a bone system from a file
	 * 
	 * @param path to the file
	 * @return the root of the bone system
	 * @throws FileNotFoundException if can't open the file
	 */
	public static Bone loadBoneSystem(String path) throws FileNotFoundException{
		
		Scanner inputStream = null;
		inputStream = new Scanner(new File(path));
		
		int depth, actualLevel = 0;
		String[] tempArray;
		String line;
		Bone temp = null, root = null;
		totalBones = 0;
		
		while(inputStream.hasNext()){
			line = inputStream.nextLine();
			//System.out.println(line);
			tempArray = line.split(" ");
			
			depth = tempArray[0].length()-1;
			//System.out.println(depth);
			for(;actualLevel>depth;actualLevel--){
				temp = temp.getParent();
			}
			
			if(root==null&&depth==0){
				root = new Bone(Double.parseDouble(tempArray[1]),
						Double.parseDouble(tempArray[2]),
						Double.parseDouble(tempArray[3]), 
						Double.parseDouble(tempArray[4]),
						Integer.parseInt(tempArray[5]), null);
				temp = root;
				actualLevel++;
			}else{
				Bone child = new Bone(Double.parseDouble(tempArray[1]),
						Double.parseDouble(tempArray[2]),
						Double.parseDouble(tempArray[3]), 
						Double.parseDouble(tempArray[4]),
						Integer.parseInt(tempArray[5]), temp);
				temp.addChild(child);
				temp = temp.getChild().get(temp.getChild().size()-1);
				actualLevel++;
			}
			
			totalBones++;
			
		}
		return root;
	}

	@SuppressWarnings("resource")
	/**
	 * Loads key frames from file. Not used in this project
	 * 
	 * @param root the root to be attached
	 * @param path file name
	 * @throws FileNotFoundException if can't open the file
	 */
	public static void loadKeyFrames(Bone root, String path)
			throws FileNotFoundException{
		
		Scanner inputStream = null;
		inputStream = new Scanner(new File(path));
		
		String[] tempArray;
		String line;
		
		while(inputStream.hasNext()){
			line = inputStream.nextLine();
			//System.out.println(line);
			tempArray = line.split(" ");
			
			Bone bone = findBoneByName(root, Integer.parseInt(tempArray[5]));
			if(bone!=null){
				bone.addKeyFrames(new KeyFrame(Integer.parseInt(tempArray[0]),
						Double.parseDouble(tempArray[1]),
						Double.parseDouble(tempArray[2]),
						Double.parseDouble(tempArray[3]),
						Double.parseDouble(tempArray[4])));
			}
			
		}
	}
	
	/**
	 * Finds a bone by its name
	 * 
	 * @param root the root of the bone system
	 * @param name the name of the bone to find
	 * @return the bone if found else null
	 */
	public static Bone findBoneByName(Bone root, int name){
		
		if(root==null) return null;
		
		if(root.getName() == name) return root;
		
		for(int i = 0;i<root.getChild().size();i++){
			Bone result = findBoneByName(root.getChild().get(i), name);
			
			if(result!=null) return result;
		}
		
		return null;
	}
	
	/**
	 * Prints the bone system in a readable and saveable format
	 * 
	 * @param bone the bone to be printed (give root to start)
	 * @param level the level of the bone (give 1 to start)
	 */
	public static void printBoneSystem(Bone bone, int level){
		
		if(bone==null) return;
		
		for(int i = 0;i<level;i++){
			System.out.print("#");
		}
		
		System.out.println(" "+bone.getX()+" "+bone.getY()+" "+bone.getAngle()+
				" "+bone.getLength()+" "+bone.getName());
		
		for(int i = 0;i<bone.getChild().size();i++){
			printBoneSystem(bone.getChild().get(i), level+1);
		}
	}
	
	/**
	 * Prints a key frame
	 * 
	 * @param bone the bone (give root to start)
	 * @param time the time (give 0 to start)
	 */
	public static void printKeyFrame(Bone bone, int time){
		
		if(bone==null) return;
		
		System.out.println(time+" "+bone.getX()+" "+bone.getY()+" "+bone.getAngle()+
				" "+bone.getLength()+" "+bone.getName());
		
		for(int i = 0;i<bone.getChild().size();i++){
			printKeyFrame(bone.getChild().get(i), time);
		}
	}
	
	/**
	 * Adds key frame to bone system
	 * 
	 * @param bone give root to start
	 * @param time give the current time index
	 */
	public static void addKeyFrame(Bone bone, int time){
		
		if(bone==null) return;
		
		System.out.println("Bone: "+bone.getName()+" time: "+time+" angle: "+bone.getAngle());
		
		bone.addKeyFrames(new KeyFrame(time, bone.getX(), bone.getY(),
				bone.getAngle(), bone.getLength()));
		
		for(int i = 0;i<bone.getChild().size();i++){
			addKeyFrame(bone.getChild().get(i), time);
		}
	}
	
	/**
	 * Used to find the angle of the bones between key frames
	 * 
	 * @param bone the bone 
	 * @param time the time
	 */
	public static void interpolate(Bone bone, int time){
		
		if(bone==null) return;
		
		double angle, length, tim;
		
		for(int i = 0;i<bone.getKeyFrame().size();i++){
			if(bone.getKeyFrame().get(i).getTime()==time){
				if(i!=bone.getKeyFrame().size()-1){
					tim = bone.getKeyFrame().get(i+1).getTime()-
							bone.getKeyFrame().get(i).getTime();
					angle = bone.getKeyFrame().get(i+1).getAngle()-
							bone.getKeyFrame().get(i).getAngle();
					length = bone.getKeyFrame().get(i+1).getLength()-
							bone.getKeyFrame().get(i).getLength();
					
					bone.setAngleOffset(angle/tim);
					bone.setLengthOffset(length/tim);
				}else{
					bone.setAngleOffset(0);
					bone.setLengthOffset(0);
				}
			}
		}
		
		bone.setAngle(bone.getAngle()+bone.getAngleOffset());
		bone.setLength(bone.getLength()+bone.getLengthOffset());
		
		for(int i = 0;i<bone.getChild().size();i++){
			interpolate(bone.getChild().get(i), time);
		}
	}

	/**
	 * Initializes the skin-bone relation
	 * 
	 * @param root root of the bone system
	 * @param skin the skin
	 * @param dependencies the maximum number of attached bones to a node in the skin
	 */
	public static void initializeSkinBoneRelation(Bone root, ArrayList<Vertex> skin, int dependencies){
		
		ArrayList<Bone> bones = new ArrayList<>();
		getBones(root, bones);
		System.out.println("Bones: "+bones.size());

		for(Vertex v: skin){
			
			weightedBones = new HashMap<Double, Bone>();
			
			for(int i = 0;i<bones.size();i++){
				double dist = bones.get(i).getInitialPosition().dist(v.getPosition());
				weightedBones.put(dist, bones.get(i));
			}
			
			List<Double> distance = new ArrayList<Double>(weightedBones.keySet());
			Collections.sort(distance);

			for(int i = 0;i<dependencies;i++){
				Bone b = weightedBones.get(distance.get(i));
				if(distance.get(i)==0){
					v.addBoneSkinBinding(new SkinBoneBinding(
									getBindingMatrix(v, b), 1.0, b));
					break;
				}else{
					v.addBoneSkinBinding(new SkinBoneBinding(
									getBindingMatrix(v, b), 1.0/dependencies, b));
				}
				
			}
		}
	}
	
	/**
	 * Not used in this project
	 * 
	 * @param root
	 * @param skin
	 */
	public static void generateWeights(Bone root, ArrayList<Vertex> skin){
		
		ArrayList<Bone> bones = new ArrayList<>();
		getBones(root, bones);
		
		/*
		for(Vertex2D v : skin){
			
			double[] w = new double[bones.size()];
			double total = 0;
			
			for(int i = 0;i<bones.size();i++){
				double dist = bones.get(i).getAbsolutePosition().dist(v.getPoint());
				//System.out.println(dist+" "+total);
				total += dist;
				w[i] = dist;
			}
			for(int i = 0;i<bones.size();i++){
				System.out.println("Weigth: "+(total -w[i])/total);
				
				v.getAttached().add(new SkinBoneBinding(getBindingMatrix(v, bones.get(i)), (total -w[i])/total, bones.get(i)));
			}
			
		}
		*/
		
		for(Vertex v : skin){
			Bone closest = bones.get(0);
			double minDistance = Double.MAX_VALUE;
			
			for(int i = 1;i<bones.size();i++){
				double dist = v.getPosition().dist(bones.get(i).getAbsolutePosition());
				if(dist<minDistance){
					closest = bones.get(i);
					minDistance = dist;
				}
			}
			
			v.getAttached().add(new SkinBoneBinding(getBindingMatrix(v, closest), 0.7, closest));
			v.getAttached().add(new SkinBoneBinding(getBindingMatrix(v, closest.getParent()),
					0.3, closest.getParent()));
		}
		
		/*
		System.out.println("Bones: "+absolutePosition.size());
		
		for(int i = 0;i<skin.size();i++){
			double w[] = new double[absolutePosition.size()];
			double total = 0.0;
			
			for(int j = 0;j<absolutePosition.size();j++){
				double m[] = absolutePosition.get(j);
				double dist = (new Point(m[12], m[13])).dist(new Point(skin.get(i).getX(),
						skin.get(i).getY()));
				System.out.println(dist);
				if(dist<250){
					total +=dist;
					w[j] = dist;
				}else{
					w[j] = 0;
				}
				
			}
			
			System.out.println("Bone: "+i);
			for(int j = 0;j<absolutePosition.size();j++){
				w[j] = w[j]/total;
				System.out.print(w[j]+", ");
			}
			System.out.println();
			skin.get(i).setW(w);
		}
		*/
	}
	
	/**
	 * Used to get an array list of bone form a tree structure
	 * 
	 * @param root give root to start
	 * @param bones the return list
	 */
	private static void getBones(Bone root, ArrayList<Bone> bones){
		bones.add(root);
		for(int i = 0;i<root.getChild().size();i++){
			bones.add(root.getChild().get(i));
			getBones(root.getChild().get(i), bones);
		}
	}
	
	/**
	 * Calculates the binding matrix
	 * 
	 * @param vertex the vertex position
	 * @param bone the bone
	 * @return the binding matrix
	 */
	private static Matrix getBindingMatrix(Vertex vertex, Bone bone){

		double thi = 0;
		double dx = vertex.getX() - bone.getAbsolutePosition().getX();
		double dy = vertex.getY() - bone.getAbsolutePosition().getY();
		//System.out.println("Thi: "+Math.toDegrees(thi));
		double[][] array = {{Math.cos(thi), Math.sin(thi), 0, dx},
							{-Math.sin(thi), Math.cos(thi), 0, dy},
							{0, 0, 1, 0},
							{0, 0, 0, 1}};
		
		//System.out.println(Arrays.deepToString(array));
		//System.out.println(v.getPoint().toString()+" -> "+b.getAbsolutePosition().toString());
		return new Matrix(array);
	}
	
	/**
	 * Resets bone initial position
	 * 
	 * @param bone give root to start
	 */
	public static void initialPose(Bone bone){
		bone.setInitialAngle();
		
		for(Bone child: bone.getChild()){
			initialPose(child);
		}
	}
}
