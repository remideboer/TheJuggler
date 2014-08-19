package juggler;

/**
 * Pong Panel voor de balk onder in het scherm die geupdatet moet worden
 * 
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

public class JugglePanel extends JPanel {
	private final int LIJNHOOGTE = 30;
	private final String misTekst = "  MISSES: ";
	private final String counterTekst = "HITS: ";
	private final int BALKHOOGTE = 15;
	private final int DELAY = 10;

	private int balkBreedte = 100;
	private int balkY;
	private Timer timer;
	private int hitCounter; // teller voor het aantal gestopte ballen
	private int misCounter; // teller voor het aantal gemiste ballen
	private int balkX;
	private int panelBreedte = 600;
	private int panelHoogte = 400;
	private int lijnLengte;

	private List<Bal> balLijst;

	public JugglePanel() {
		// Setup time!
		setBackground(Color.BLACK);
		// Speelveld veld = new Speelveld(getWidth(), getHeight());
		balLijst = new CopyOnWriteArrayList<>();  // om ava.util.ConcurrentModificationException te voorkomen zie http://www.noppanit.com/how-to-deal-with-java-util-concurrentmodificationexception-with-arraylist/
		balkY = getWidth() / 2;
		// voeg een bal toe		
			balLijst.add(new Bal(this));

	

		// vuurt elke honderste een actionEvent af
		timer = new Timer(DELAY, new stuiterbalListener());
		// componentListener zorgt voor een eerste mis, wordt gerenderd

		// LISTENERS

		// MOUSE
		// bij bewegen van de muis gebruik de x-coordinaat
		// van de muis om door te geven
		// aan de methode beweegBalk
		addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				beweegBalk(e.getX());
			}
		});
		// COMPONENT
		// anonieme componentListener om de hoogte te verkrijgen in eerste in
		// stantie
		// anders geeft deze met getHeight 0
		// en met resize kunnen we nu aanpassen
		// API meeleveren voor vraag
		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {

				balkY = getHeight() - BALKHOOGTE;
				lijnLengte = getWidth();
				panelBreedte = getWidth();
				panelHoogte = getHeight();
				requestFocusInWindow();

			}

		});

		// START TIMER
		timer.start();
	}

	// Hier wordt getekend doh! :)
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.WHITE);
		//balkHoogte lijn  g2d.drawLine(0, balkY, lijnLengte, balkY);
		
		g2d.drawLine(0, LIJNHOOGTE, lijnLengte, LIJNHOOGTE);
		g2d.setColor(Color.BLUE);
		g2d.drawString(counterTekst + hitCounter + misTekst + misCounter, 20,
				20);
		g2d.setColor(Color.GREEN);
		g2d.fillRect(balkX, balkY, balkBreedte, BALKHOOGTE);

		g2d.setColor(Color.RED);

		// itereer door de ballenlijst en roep drawBal aan
		// drawBal uit klasse bal gehaald, tekenen is niet de
		// verantwoordelijkheid van de bal
		
		for (Bal bal : balLijst) {
			g2d.setColor(bal.getKleur());
			g2d.fillRect(bal.getX(), bal.getY(), bal.getDoorsnede(),
					bal.getDoorsnede());
		}
	}

	/**
	 * set de positie van de coordinaten van de balk
	 * 
	 * @param x
	 *            wordt verkregen vanuit de muispositie of anders indien gewenst
	 *            OFFSET is 2 extra pixels vanwege de onzichtbare border bij een
	 *            panel omdat een filled rectangle wordt getekend met 1 pixel
	 *            minder in omtrek
	 */
	private void beweegBalk(int x) {
		int OFFSET = 2; // border en filled is x + 1
		// eigenlijk moet de logica alleen zijn als er bewogen wordt en dan
		// tekenen wanneer binnen in panel
		if ((balkX != x)) {
			if (x > 0 - OFFSET && x < getWidth() - balkBreedte + OFFSET) {
				balkX = x;
				repaint();
			}

		}
	}

	/**
	 * ActionListener voor het updaten van de val Gebruik voor een Timer object
	 * 
	 */
	private class stuiterbalListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// itereer door de balLijst heen, roep beweegBal aan een geef
			// panel+balk eigenschappen door
			// TODO: refactor balkEigenschappen naar speelVeld
			
			
			for (Bal bal : balLijst) {

				bal.beweegBal(panelHoogte, panelBreedte, balkX, balkY,
						balkBreedte, BALKHOOGTE);
			}
			
			repaint();
		}

	}

	// panel
	public void addHit() {
		hitCounter++;
		// verklein de breedte van de balk to 10
		if (hitCounter >= 10) { // wel pas nadat de 10 bereikt is, anders bij start
								// meteen kleiner
			if (hitCounter % 5 == 0) {
				if (balkBreedte > 10) {
					balkBreedte -= 5;
				}
				
			}
			// bij elke 15 een nieuwe bal introduceren
						if(hitCounter % 15 == 0){
							balLijst.add(new Bal(this));
						}
			
		}
	}

	public void addMiss() {
		misCounter++;
		// GAME OVER!!! 
		if(misCounter %  15 == 0){
			// TODO for now grow balk
			if(balkBreedte < getWidth() - 20){
			balkBreedte +=20;
			}
			
		}
		if(misCounter %  10 == 0){ // als er meer dan 1 bal in de lijst zit haal index 0 eruit (is safest)
		if(balLijst.size() > 1){
			balLijst.remove(0);
		}
		}
	}

}
