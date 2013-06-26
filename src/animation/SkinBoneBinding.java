package animation;

import skeleton2D.Bone;
import Jama.Matrix;

/**
 * Represents a bone skin binding
 * 
 * @author Jim Stanev
 */
public class SkinBoneBinding {
	
	private Matrix bindingMatrix;
	private double weight;
	private Bone bone;
	
	/**
	 * Constructor
	 * 
	 * @param bindingMatrix the binding matrix
	 * @param weight the weight of the binding
	 * @param bone the attached bone
	 */
	public SkinBoneBinding(Matrix bindingMatrix, double weight, Bone bone){
		this.bindingMatrix = bindingMatrix;
		this.weight = weight;
		this.bone = bone;
	}

	public Matrix getBindingMatrix() {
		return bindingMatrix;
	}


	public double getWeigth() {
		return weight;
	}

	public Bone getBone() {
		return bone;
	}

}
