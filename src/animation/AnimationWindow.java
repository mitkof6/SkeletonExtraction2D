package animation;

import java.awt.BorderLayout;
import java.awt.Color;


import javax.swing.JFrame;

public class AnimationWindow extends JFrame{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AnimationPanel panel;
	
	public AnimationWindow(){
		super("Animation");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setBackground(Color.black);
        this.setLayout(new BorderLayout());
        
        panel = new AnimationPanel();
        
        this.add(panel);
        this.setVisible(true);
	}
}
