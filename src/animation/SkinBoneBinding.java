package animation;

import skeleton2D.Bone;
import Jama.Matrix;

public class SkinBoneBinding {
	
	private Matrix b;
	private double weigth;
	private Bone bone;
	
	public SkinBoneBinding(Matrix b, double weigth, Bone bone){
		this.b = b;
		this.weigth = weigth;
		this.bone = bone;
	}

	public Matrix getB() {
		return b;
	}

	public void setB(Matrix b) {
		this.b = b;
	}

	public double getWeigth() {
		return weigth;
	}

	public void setWeigth(double weigth) {
		this.weigth = weigth;
	}

	public Bone getBone() {
		return bone;
	}

	public void setBone(Bone bone) {
		this.bone = bone;
	}
	
	
}
