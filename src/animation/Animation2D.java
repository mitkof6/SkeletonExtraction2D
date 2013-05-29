package animation;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import primitives.Bone2D;


import com.jogamp.opengl.util.FPSAnimator;



public class Animation2D extends Frame implements GLEventListener, KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GLProfile glp;
	private GLCapabilities caps;
	private GLCanvas canvas;
	private FPSAnimator animator;
	private GLU glu;
	private GL2 gl;

	private Bone2D rootBS;
	private boolean updateFlag = false;
	private int DISTANCE = 1000, X = 0, Y = 0;


	public Animation2D(Bone2D rootBS){

		super("Animation");
		this.setFocusable(true);
		this.addKeyListener(this);

		//closing
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				canvas.destroy();
				animator.stop();
				System.exit(0);
			}
		});

		this.rootBS = rootBS;

		//intialization
		glp =  GLProfile.getDefault();
		GLProfile.initSingleton();
		caps = new GLCapabilities(glp);

		//canvas
		canvas = new GLCanvas(caps);
		canvas.addGLEventListener(this);

		//glu
		glu = new GLU();

		//frame
		this.add(canvas);

		//animator
		animator = new FPSAnimator(canvas, 60);
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
		//setCamera(gl, glu);
		gl.glViewport(X, Y, 1000, 1000);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glOrtho(-DISTANCE, DISTANCE, -DISTANCE, DISTANCE, -1, 1);
		gl.glMatrixMode(GL2.GL_MODELVIEW);

		//set light
		setLight(gl);

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		render(drawable);
		//update();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);

	}

	private void update() {

	}

	private void render(GLAutoDrawable drawable) {

		gl = drawable.getGL().getGL2();

		// Clear screen.
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glLoadIdentity();

		if(updateFlag==true){
			updateFlag = false;
			gl.glViewport(X, Y, 1000, 1000);
		}

		// draw
		drawBone(rootBS, gl);

		gl.glFlush();

	}

	private void drawBone(Bone2D bone, GL2 gl){
		gl.glPushMatrix();
		gl.glBegin(GL2.GL_LINE_LOOP);
		gl.glColor3f(1, 0, 0);
		gl.glVertex2d(bone.getX(), bone.getY());
		gl.glColor3f(0, 1, 0);
		if(bone.getParent()!=null) gl.glVertex2d(bone.getParent().getX(),
				bone.getParent().getY());
		gl.glEnd();

		/*
		 gl.glPushMatrix();

		 gl.glTranslated(bone.getX(), bone.getY(), 0);
		 gl.glRotated(bone.getA(), 0, 0, 1);

		 gl.glBegin(GL2.GL_LINE_LOOP);
		 gl.glColor3f(1, 0, 0);
		 gl.glVertex2d(0, 0);
		 gl.glColor3f(0, 1, 0);
		 gl.glVertex2d(bone.getL(), 0);
		 gl.glEnd();
		 */
		for(Bone2D child : bone.getChild()){
			drawBone(child, gl);
		}

		gl.glPopMatrix();
	}

	private void setCamera(GL2 gl, GLU glu) {
		// Change to projection matrix.
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		// Perspective.
		float widthHeightRatio = (float) getWidth() / (float) getHeight();
		glu.gluPerspective(90, widthHeightRatio, 1, 2000);
		glu.gluLookAt(0, 0, DISTANCE, X, Y, 0, 0, 1, 0);


		// Change back to model view matrix.
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	private void setLight(GL2 gl){
		// Prepare light parameters.
		float SHINE_ALL_DIRECTIONS = 1;
		float[] lightPos = {0, 0, 150, SHINE_ALL_DIRECTIONS};
		float[] lightColorAmbient = {0.2f, 0.2f, 0.2f, 1f};
		float[] lightColorSpecular = {0.2f, 0.8f, 0.8f, 1f};

		// Set light parameters.
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightColorAmbient, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);

		// Enable lighting in GL.
		gl.glEnable(GL2.GL_LIGHT1);
		gl.glEnable(GL2.GL_LIGHTING);

		// Set material properties.
		float[] rgba = {0.3f, 0.5f, 1f};
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, rgba, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rgba, 0);
		gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 0.5f);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		updateFlag = true;
		if(e.getKeyCode()==KeyEvent.VK_W){
			Y = Y+10;
		}else if(e.getKeyCode()==KeyEvent.VK_S){
			Y = Y-10;
		}else if(e.getKeyCode()==KeyEvent.VK_A){
			X = X-10;
		}else if(e.getKeyCode()==KeyEvent.VK_D){
			X = X+10;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
