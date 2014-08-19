package juggler;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JPanel;

public class Bal {
	private static int balCounter = 0;
	private int balID;
	private final int LIJNHOOGTE = 30; // for debugging remove!!!!
	int balkX; // for debugging remove!!!!
	int balkY; // for debugging remove!!!!
	int balkBreedte; // for debugging remove!!!!
	private int x;
	private int y;
	private int doorsnede; // is eigenlijk geen doorsnede, maar breedte
	private int moveX;
	private int moveY;
	private Color kleur;
	private JugglePanel veld;
	private int speedLimit;
	private boolean collisionZijkant; 

	public Bal(JugglePanel veld) {
		// gebruik de klassevar hoog op en wijs toe aan dit bal object
		balCounter++;
		balID = balCounter;
		speedLimit = 2;
		this.veld = veld;
		this.doorsnede = 20;
		startBal();
		Random rng = new Random();
		// kleurtje randommen
		switch (rng.nextInt(10)) {
		case 0:
			kleur = Color.BLUE;
			break;
		case 1:
			kleur = Color.RED;
			break;
		case 2:
			kleur = Color.YELLOW;
			break;
		case 3:
			kleur = Color.CYAN;
			break;
		case 4:
			kleur = Color.GREEN;
			break;
		case 5:
			kleur = Color.MAGENTA;
			break;
		case 6:
			kleur = Color.ORANGE;
			break;
		case 7:
			kleur = Color.PINK;
			break;
		case 8:
			kleur = Color.WHITE;
			break;
		case 9:
			kleur = Color.GRAY;
			break;

		}

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getDoorsnede() {
		return doorsnede;
	}

	public int getMoveX() {
		return moveX;
	}

	public int getMoveY() {
		return moveY;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setDoorsnede(int doorsnede) {
		this.doorsnede = doorsnede;
	}

	public void setMoveX(int moveX) {
		this.moveX = moveX;
	}

	public void setMoveY(int moveY) {
		this.moveY = moveY;
	}

	public Color getKleur() {
		return kleur;
	}

	public void setKleur(Color kleur) {
		this.kleur = kleur;
	}

	public void beweegBal(int panelHoogte, int panelBreedte, int balkX,
			int balkY, int balkBreedte, int balkHoogte) {
		// in methode beweegBal
		
		this.x += moveX;
		this.y += moveY;
		// Y-coordinaat checks
		// als de "bal" er aan de onderkant doorgaat dan weer bovenaan
		// beginnen
		if (y > panelHoogte  ) { //plus visuele correctie
				// Geef je falen aan het veld door! Zodat je dat ingewreven kunt
					// worden is miscounter
				veld.addMiss();
				// pas als de bal het veld verlaat opnieuw een bal starten
				if(y > panelHoogte){
					startBal();
				}
				
		} 
			
			// als er een botsing met de balk OF met de bovenkant van het veld is
			// draai om
			if (checkCollisionBalk(balkX, balkY, balkBreedte, balkHoogte)|| y <= LIJNHOOGTE) {
					// wissel van y richting
					moveY = moveY * -1;
					// wissel van x richting als er een botsing is met de zijkant
					if(collisionZijkant){
						moveX = moveX * -1;
					}
			}

			// X coordinaat checks
			if (x <= 0 || x >= panelBreedte - doorsnede) {
				// wissel van richting
				moveX = moveX * -1;
			}
		

	}

	private void startBal() {
		// for debugging purps
		Random rng = new Random();
		int tmp = rng.nextInt(500); // met een temp var want nextInt wil geen
									// nul hebben
		this.x = tmp;
		this.y = 40;
		// varieer startsnelheden
		this.moveX = rng.nextInt(speedLimit) + 2;
		this.moveY = rng.nextInt(speedLimit) + 2;
		this.doorsnede = 20;
		// randomize richting
		if (rng.nextInt(2) == 1) {
			moveX = moveX * -1;
		}
	}

	public boolean checkCollisionBalk(int balkX, int balkY, int balkBreedte,
			int balkHoogte) {
		// // splits checks op in booleans op voor leesbaarheid
		// x groter dan x balk EN x kleiner dan x balk plus breedte en y
		// groter dan y balk EN y kleiner dan y balk dan TRUE
		// nog rekening houden met doorsnede object
		boolean xGroterDanLinkerkantBalk = false;
		boolean xKleinerDanRechterkantBalk = false;
		boolean yGroterDanBovenkantBalk = false;
		collisionZijkant = false; // HACK TODO

		boolean collision = false;

		// werkt
		// check x coordinaat
		// is rechts van coordinaat balk en ook links van einde balk
		
		if (this.x + this.doorsnede >= balkX) {
			xGroterDanLinkerkantBalk = true;
		}

		if (x + this.doorsnede < balkX + balkBreedte) {
			xKleinerDanRechterkantBalk = true;

		}

		if ((this.y + this.doorsnede) > balkY ) {
			yGroterDanBovenkantBalk = true;

		}
		
//		// check linkerzijkant collision
//		if ((xGroterDanLinkerkantBalk  && yGroterDanBovenkantBalk && this.x + this.doorsnede >= balkX + 2) ) {
//			collisionZijkant = true;
//
//		}

		if (xGroterDanLinkerkantBalk && xKleinerDanRechterkantBalk
				&& yGroterDanBovenkantBalk) {
			// geef aan het Speelveld door dat je met succes een bal hebt
			// tegengehouden you Epic SOn of a Bitch!!
			veld.addHit();
			collision = true;

		}
		//
		return collision;
	}

}
