package main;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import animation.Animation2D;
import animation.AnimationWindow;

/**
 *
 * @author STANEV
 */
public class Window extends JFrame{//
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int HEIGTH = 700, WIDTH = 700;
    
    public Window(){
        super("Model");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(10, 10, WIDTH, HEIGTH);
        
        this.setLayout(new BorderLayout());
       
        final JTextField pathTF = new JTextField("Model_2d_test.poly", 25);
        final JTextField zoomTF = new JTextField("2", 3);
        final JTextField stepTF = new JTextField("1", 3);
        final JTextField samplingTF = new JTextField("20", 3);
        final JTextField pushingFactorTF = new JTextField("30", 3);
        final JTextField iterationTF = new JTextField("1000", 5);
        final JTextField distanceTTF = new JTextField("20", 3);
        final JTextField rootTTF = new JTextField("2", 3);
        
        final ModelPanel panel = new ModelPanel();
        
        JButton generateB = new JButton("Generate");
        generateB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Main.generateSceleton(pathTF.getText(),
							Integer.parseInt(zoomTF.getText()),
							Double.parseDouble(stepTF.getText()),
							Integer.parseInt(samplingTF.getText()),
							Integer.parseInt(pushingFactorTF.getText()),
							Integer.parseInt(iterationTF.getText()),
							Integer.parseInt(distanceTTF.getText()), 
							Integer.parseInt(rootTTF.getText()));
				} catch (NumberFormatException | FileNotFoundException e) {
					@SuppressWarnings("unused")
					JOptionPane pane = new JOptionPane("Can't open file",
							JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
					return;
					//TODO
				}
				panel.render();
				repaint();
			}
		});
        
        //model tool bar
        JToolBar modelToolBar = new JToolBar();
        modelToolBar.add(generateB);
        modelToolBar.add(createVerticalSeparator());
        modelToolBar.add(new JLabel("PA: "));
        modelToolBar.add(pathTF);
        modelToolBar.add(createVerticalSeparator());
        modelToolBar.add(new JLabel("ZO: "));
        modelToolBar.add(zoomTF);
        modelToolBar.add(createVerticalSeparator());
        modelToolBar.add(new JLabel("ST: "));
        modelToolBar.add(stepTF);
        modelToolBar.add(createVerticalSeparator());
        modelToolBar.add(new JLabel("SA: "));
        modelToolBar.add(samplingTF);
        modelToolBar.add(createVerticalSeparator());
        modelToolBar.add(new JLabel("PF: "));
        modelToolBar.add(pushingFactorTF);
        modelToolBar.add(createVerticalSeparator());
        modelToolBar.add(new JLabel("IT: "));
        modelToolBar.add(iterationTF);
        modelToolBar.add(createVerticalSeparator());
        modelToolBar.add(new JLabel("DT: "));
        modelToolBar.add(distanceTTF);
        modelToolBar.add(createVerticalSeparator());
        modelToolBar.add(new JLabel("RT: "));
        modelToolBar.add(rootTTF);

        //animation tool bar
        JToolBar animationToolBar = new JToolBar();
        
        JButton animateB = new JButton("Animate");
        animateB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Main.skeleton==null) return;
				Main.generateBoneSystem();
				Animation2D animator = new Animation2D(Main.boneSystem.getRoot());
				animator.setBounds(100, 20, WIDTH, HEIGTH);
				animator.setVisible(true);
			}
		});
        animationToolBar.add(animateB);
        
        this.add(panel, BorderLayout.CENTER);
        this.add(modelToolBar, BorderLayout.NORTH);
        this.add(animationToolBar, BorderLayout.SOUTH);
        
    }
    
    private JComponent createVerticalSeparator() {  
        JSeparator x = new JSeparator(SwingConstants.VERTICAL);  
        x.setPreferredSize(new Dimension(3,20));  
        return x;  
    }
}
