package animation;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import main.Main;


import primitives.Point;
import primitives.Vertex;
import skeleton2D.Bone;


import Jama.Matrix;

import com.jogamp.opengl.util.FPSAnimator;


/**
 * This class is responsible for viewing the animation with jogl
 * 
 * @author Jim Stanev
 */
public class Animator2D extends Frame implements GLEventListener, KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GLCanvas canvas;
	private FPSAnimator animator;
	private GL2 gl;

	private Bone root;
	private ArrayList<Vertex> skin;
	private ArrayList<Point> skinPosition;
	
	private boolean animate = false, weightsInitialized = false;
	private int DISTANCE = 1000;
	private int boneIndex = 1, keyFrameIndex = 0, FPS = 30, time; 
	

	public Animator2D(){
		super("Animation");

		//closing
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				canvas.destroy();
				animator.stop();
				setVisible(false);
			}
		});

		//data
		this.root = Main.root;
		this.skin = Main.vertices;
		skinPosition = new ArrayList<>();
		
		//intialization
		GLProfile glp =  GLProfile.getDefault();
		GLProfile.initSingleton();
		GLCapabilities caps = new GLCapabilities(glp);

		//canvas
		canvas = new GLCanvas(caps);
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		canvas.setFocusable(true);

		//frame
		this.add(canvas);

		//animator
		animator = new FPSAnimator(canvas, FPS);
		animator.add(canvas);
		animator.start();
	}

	
	@Override
	public void init(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2();

		// Enable z- (depth) buffer for hidden surface removal. 
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LEQUAL);

		// Enable smooth shading.
		gl.glShadeModel(GL2.GL_SMOOTH);

		// Define "clear" color.
		gl.glClearColor(0f, 0f, 0f, 0f);

		// We want a nice perspective.
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

		//set camera
		gl.glViewport(0, 0, 1000, 1000);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glOrtho(-DISTANCE, DISTANCE, -DISTANCE, DISTANCE, -1, 1);
		gl.glMatrixMode(GL2.GL_MODELVIEW);


	}

	@Override
	public void dispose(GLAutoDrawable drawable) {}

	@Override
	public void display(GLAutoDrawable drawable) {
		render(drawable);
		if(weightsInitialized==false){
			weightsInitialized = true;
			BoneFunctions.initializeSkinBoneRelation(root, skin, Main.SKIN_DEPENDENCES);
		}
		if(animate){
			BoneFunctions.interpolate(root, time);
			time++;
			if(time==root.getKeyFrame().size()*FPS){
				time = 0;
				BoneFunctions.initialPose(root);
			}
		}
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);

	}


	/**
	 * The rendering method
	 * 
	 * @param drawable gl
	 */
	private void render(GLAutoDrawable drawable) {

		gl = drawable.getGL().getGL2();

		// Clear screen.
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glLoadIdentity();

		//#########################
		drawBone(root, gl);

		updateSkin();
		
		drawSkin(gl);
		//#########################
		
		gl.glFlush();
	}

	
	/**
	 * This method updates the new skin position with linear blend skinning
	 * using the binding matrix, the weight and the dependences bone's absolute
	 * matrix
	 */
	private void updateSkin(){
		skinPosition.clear();
		for(Vertex v : skin){
			double x = 0, y = 0;
			Matrix result = null;
			ArrayList<SkinBoneBinding> attached = v.getAttached();
			for(int i = 0;i<attached.size();i++){
				result = attached.get(i).getBindingMatrix().times(attached.get(i).getBone().getAbsoluteMatrix());
				result = result.times(new Matrix(new double[]{0, 0 , 0, 1},1).transpose());
				result = result.times(attached.get(i).getWeigth());
				x += result.get(0, 0);
				y += result.get(1, 0);
				//System.out.println(Arrays.deepToString(result.getArray()));
			}
			
			//System.out.println(Arrays.deepToString(result.getArray()));
			skinPosition.add(new Point(x, y));
		}
	}
	
	/**
	 * This method draws a bone in recursive way
	 * 
	 * @param bone the bone to draw
	 * @param gl gl
	 */
	private void drawBone(Bone bone, GL2 gl){
		
		gl.glPushMatrix();
		double m[] = new double[16];
	
		if(bone.getParent()==null){
			gl.glTranslated(bone.getX(), bone.getY(), 0);
		}
		
		gl.glRotated(Math.toDegrees(bone.getAngle()), 0, 0, 1);
		
		gl.glColor3f(1, 0, 0);
		if(bone.getName()==boneIndex){//bone selection
			gl.glColor3f(0, 1, 0);
		}
		
		gl.glBegin(GL2.GL_LINE_LOOP);
		gl.glVertex2d(0, 0);
		gl.glVertex2d(bone.getLength(), 0);
		gl.glEnd();
		
		gl.glTranslated(bone.getLength(), 0, 0);
		
		gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, m, 0);
		
		bone.setAbsoluteMatrix(m);
		if(weightsInitialized==false){
			bone.setX(m[12]);
			bone.setY(m[13]);
		}
		
		for(Bone child : bone.getChild()){
			drawBone(child, gl);
		}

		gl.glPopMatrix();
	}

	/**
	 * This method draws the skin
	 * 
	 * @param gl gl
	 */
	private void drawSkin(GL2 gl){
		gl.glPushMatrix();
		
		gl.glColor3f(0, 0, 1);
		gl.glBegin(GL2.GL_LINE_LOOP);
		for(Point p : skinPosition){
			gl.glVertex2d(p.getX(), p.getY());
		}
		gl.glEnd();
		
		gl.glPopMatrix();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode()==KeyEvent.VK_W){
			root.setY(root.getY()+10);
		}else if(e.getKeyCode()==KeyEvent.VK_S){
			root.setY(root.getY()-10);
		}else if(e.getKeyCode()==KeyEvent.VK_A){
			root.setX(root.getX()-10);
		}else if(e.getKeyCode()==KeyEvent.VK_D){
			root.setX(root.getX()+10);
		}else if(e.getKeyCode()==KeyEvent.VK_P){
			boneIndex = boneIndex==0 ? 0 : --boneIndex;
		}else if(e.getKeyCode()==KeyEvent.VK_N){
			boneIndex = boneIndex==BoneFunctions.totalBones ? 0 : ++boneIndex;
		}else if(e.getKeyCode()==KeyEvent.VK_LEFT){
			Bone bone = BoneFunctions.findBoneByName(root, boneIndex);
			if(bone!=null){
				bone.setAngle(bone.getAngle()+0.1);
			}
		}else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
			Bone bone = BoneFunctions.findBoneByName(root, boneIndex);
			if(bone!=null){
				bone.setAngle(bone.getAngle()-0.1);
			}
		}else if(e.getKeyCode()==KeyEvent.VK_Q){
			BoneFunctions.printKeyFrame(root, keyFrameIndex);
			keyFrameIndex = keyFrameIndex + FPS;
		}else if(e.getKeyCode()==KeyEvent.VK_R){
			BoneFunctions.addKeyFrame(root, keyFrameIndex);
			keyFrameIndex = keyFrameIndex + FPS;
		}else if(e.getKeyCode()==KeyEvent.VK_E){//animate
			time = 0;
			animate = !animate;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
}
