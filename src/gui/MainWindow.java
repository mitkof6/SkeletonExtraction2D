package gui;


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

import main.Main;

import exceptions.FileDialogException;
import extract.Load2D;
import extract.OpenFileDialog;

import animation.Animator2D;
import animation.BoneFunctions;

/**
 * Main window class
 * 
 * @author Jim Stanev
 */
public class MainWindow extends JFrame{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
    public MainWindow(){
        super("Model");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(10, 10, Main.WIDTH, Main.HEIGHT);
        
        this.setLayout(new BorderLayout());
       
        final JTextField pathTF = new JTextField("model/Model_2d_test.poly", 25);//Model_2d_test.poly
        //cube
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
					Main.generateBoneSystem();
				} catch (NumberFormatException | FileNotFoundException e) {
					@SuppressWarnings("unused")
					JOptionPane pane = new JOptionPane("Can't open file",
							JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
					return;
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
				if(Main.root==null) return;
				Main.SKIN_DEPENDENCES = 
					Integer.parseInt(JOptionPane.showInputDialog(null, "Skin Dependencies?", "2"));
				Animator2D animator = new Animator2D();
				animator.setBounds(100, 20, Main.WIDTH, Main.HEIGHT);
				animator.setVisible(true);
				Main.printKeyInfo();
			}
		});
        animationToolBar.add(animateB);
        
        JButton loadB = new JButton("Load Bone System");
		loadB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					System.out.println(Main.SEP);
					OpenFileDialog fileHandler = new OpenFileDialog();
					Main.root = BoneFunctions.loadBoneSystem(fileHandler.getFile());
					System.out.println("Bone system loaded");
					BoneFunctions.printBoneSystem(Main.root, 1);
					System.out.println(Main.SEP);
				} catch (FileDialogException | FileNotFoundException e) {
					return;
				}
			}
		});
		animationToolBar.add(loadB);
		
		JButton loadSkin = new JButton("Load Model");
		loadSkin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					System.out.println(Main.SEP);
					OpenFileDialog fileHandler = new OpenFileDialog();
					Load2D loadSkin = new Load2D(fileHandler.getFile(), 1);
					Main.vertices = loadSkin.getPolygon();
					System.out.println("Model loaded");
					System.out.println(Main.SEP);
				} catch (FileDialogException | FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		animationToolBar.add(loadSkin);
        
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
