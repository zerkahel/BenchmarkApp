package GUI;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class Window extends Canvas{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2815577872584840591L;

	public Window(){
        JFrame frame=new JFrame();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int ourHeight=screenSize.height;
        int ourWidth=screenSize.width/2;
        frame.setPreferredSize(new Dimension(ourHeight,ourWidth));
        frame.setMaximumSize(new Dimension(ourHeight,ourWidth));
        frame.setMinimumSize(new Dimension(ourHeight,ourWidth));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        
        
        frame.add(new TabbedPaneDemo(ourHeight,ourWidth), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
}
}
