package juggler;

import javax.swing.JFrame;


/**
 * Ballenbak vraag klasse ballenbak
 * 
 */

public class Main {

	public static void main(String[] args) {
		//set up frame
		
		JFrame frame = new JFrame("The Juggler");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setLocationRelativeTo(null); // centrum van het scherm
		frame.setSize(600, 400);
		
			
		// voeg panel toe
		frame.add(new JugglePanel());
		frame.setVisible(true);
		
	}
	
	
	
}
