package main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import views.GuiView;
import views.TextView;

/**
 * 
 */

/**
 * @author HellwigP
 *
 */
public class NeuronChess {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//0. Read argument: who is on?
		if (args.length != 1) {
            System.out.println("Aufruf: java -jar neuronchess.jar gui|text");
            System.exit(1);
        }
		
		if (args[0].equals("gui")) startSwing();
		else startText();
	}
	
	private static void startSwing() {
		Runnable r = new Runnable() {

			public void run() {
				GuiView cg = new GuiView();

				JFrame f = new JFrame("NeuronChess");
				f.add(cg.getGui());
				// Ensures JVM closes after frame(s) closed and
				// all non-daemon threads are finished
				f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				// See http://stackoverflow.com/a/7143398/418556 for demo.
				f.setLocationByPlatform(true);

				// ensures the frame is the minimum size it needs to be
				// in order display the components within it
				f.pack();
				// ensures the minimum size is enforced.
				f.setMinimumSize(f.getSize());
				f.setVisible(true);
			}
		};
		// Swing GUIs should be created and updated on the EDT
		// http://docs.oracle.com/javase/tutorial/uiswing/concurrency
		SwingUtilities.invokeLater(r);
		
	}
	
	private static void startText() {
		TextView gameView = new TextView();
		gameView.action();
	}
	

}
