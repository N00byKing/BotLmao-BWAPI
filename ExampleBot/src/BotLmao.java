import java.util.*;
import java.util.concurrent.TimeUnit;

import bwapi.*;

public class BotLmao extends DefaultBWListener {
    
    private Mirror mirror = new Mirror();

    private Game game;
    private Player self;
    
    List<Unit> enus;
    List<Unit> eus;
    
    List<TilePosition> posPlayerUnits;
    
    int EnemyCount;
    
    int AttackStartFrame;
    int WalkTimeCounter;
    @Override
    public void onStart() {
	game = mirror.getGame();
	self = game.self();
	game.sendText("BotLmao has initialised");
    }
    
	
    public void onFrame() {
	enus = game.enemy().getUnits();
	eus = game.self().getUnits();
	
	EnemyCount = EnemyCheck();
	
	this.PlayerCheck();
	this.EnemyCheck();
	
	posPlayerUnits = new LinkedList<TilePosition>();
	
	for (int i = 0; i < eus.size(); i++) {
	    posPlayerUnits.add(eus.get(i).getTilePosition());
	}
	
	if (game.mapFileName().equals("04_Zehnkampf_Multi.scx")) {
	    game.drawTextScreen( 50, 90, ("Using Nullformation"));
	    this.useNullFormation();
	}
	else if (game.mapFileName().equals("01_BattleRoyale.scx")) {
	    game.drawTextScreen( 50, 90, ("Using Lineformation"));
	    this.useLineFormation();
	}
	else if (game.mapFileName().equals("02_Zweikampf.scx")) {
	    game.drawTextScreen( 50, 90, ("Using Backtrackformation"));
	    this.useBacktrackFormation();
	}
	else {
	    game.drawTextScreen( 50, 90, ("No specific Map formation. Defaulting to Lineformation"));
	    this.useLineFormation();
	}
    }
    
    public void useLineFormation() {
	for (int j = 0; j < EnemyCount + 1; j++) {
	    if (EnemyCount > 0 ) {
		for (int i = 0; i < eus.size(); i++) {
		    if (eus.get(i).getLastCommandFrame() >= game.getFrameCount() || eus.get(i).isAttackFrame()) {
		    }
		    else if (eus.get(i).getLastCommand().getUnitCommandType() == UnitCommandType.Attack_Unit) {
		    }
		    else {
			if (enus.get(j).isVisible()) {
			    eus.get(i).attack(enus.get(j));
			}
		    }
		}
	    }
	    
	    else {
		for (int i = 0; i < eus.size(); i++) {
		    
		    if (i == 0) {
			eus.get(i).move(new TilePosition(posPlayerUnits.get(i).getX(), posPlayerUnits.get(i).getY() + 16).toPosition());
		    }
		    else {
			if (eus.get(i-1).exists()) {
			    eus.get(i).move(new TilePosition(posPlayerUnits.get(i-1).getX() + 10, posPlayerUnits.get(i-1).getY() + 16).toPosition());
			}
			else {
			    eus.get(i).move(new TilePosition(posPlayerUnits.get(i).getX(), posPlayerUnits.get(i-1).getY() + 16).toPosition());
			}
		    }
		}
	    }
	}
    }
    
    public void useBacktrackFormation() {
	if (EnemyCount <= 0) {
	    for (int i = 0; i < eus.size(); i++) {
		    if (i == 0) {
			eus.get(i).move(new TilePosition(posPlayerUnits.get(i).getX(), posPlayerUnits.get(i).getY() + 16).toPosition());
		    }
		    else {
			if (eus.get(i-1).exists()) {
			    eus.get(i).move(new TilePosition(posPlayerUnits.get(i-1).getX() + 10, posPlayerUnits.get(i-1).getY() + 16).toPosition());
			}
			else {
			    eus.get(i).move(new TilePosition(posPlayerUnits.get(i).getX(), posPlayerUnits.get(i-1).getY() + 16).toPosition());
			}
		    }
	    }   
	}
	else {
	    if (AttackStartFrame == 0) {
		game.sendText("null");
		AttackStartFrame = game.getFrameCount();
	    }
	    for (int j = 0; j < enus.size(); j++) {
		if (enus.get(j).isVisible()) {
		    if (enus.get(j).getType() == enus.get(j).getType().Terran_Firebat) {

			for (int k = 0; k == 0; ) {
			    for (int i = 0; i < eus.size(); i++) {
				if (AttackStartFrame <= (game.getFrameCount()-40)) {
				    game.sendText(":3");
				    if ((i % 2) == 0) {
				    eus.get(i).move((new TilePosition(this.posPlayerUnits.get(i).getX() - 5, this.posPlayerUnits.get(i).getY())).toPosition());
				    }
				    else {
					eus.get(i).move((new TilePosition(this.posPlayerUnits.get(i).getX() + 5, this.posPlayerUnits.get(i).getY())).toPosition());
				    }
				    WalkTimeCounter++;
				    if (WalkTimeCounter == 40) {
					AttackStartFrame = 0;
					WalkTimeCounter = 0;
				    }
				}
				else if (eus.get(i).getLastCommandFrame() >= game.getFrameCount() || eus.get(i).isAttackFrame() && WalkTimeCounter <= 80) {
				}
				else if (eus.get(i).getLastCommand().getUnitCommandType() == UnitCommandType.Attack_Unit) {
				}
				else {
				    eus.get(i).attack(enus.get(j));
				}
				k = 1;
			    }
			}
		    }
		}
	    }
	}
	
	
    }
    

    public void useNullFormation() {
	
    }
    



    public int EnemyCheck() {
	enus = game.enemy().getUnits();
	game.drawTextScreen( 50, 50, ("Enemies (Visible) Units: " + enus.size()));
	if (enus.size() > 0) {
	    return enus.size();
	}
	else {
	    return 0;
	}
    }
    
    public int PlayerCheck() {
	eus = game.self().getUnits();
	game.drawTextScreen( 50, 70, ("BotLmao's Units: " + eus.size()));
	if (eus.size() > 0) {
	    return eus.size();
	}
	else {
	    return 0;
	}
    }
    
    
    
    
    
    
    
   
    
    
    //Unveränderbar
    public void run() {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    public static void main(String[] args) {
        new BotLmao().run();
    }
}
