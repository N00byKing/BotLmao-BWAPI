import java.util.*;
import java.util.concurrent.TimeUnit;

import bwapi.*;

public class BotLmao extends DefaultBWListener {
    
    private Mirror mirror = new Mirror();

    private Game game;
    private Player self;
    
    
    @Override
    public void onStart() {
	game = mirror.getGame();
	self = game.self();
	game.sendText("BotLmao has initialised");
    }
    
	
    public void onFrame() {
	// this.useLineFormation();
	this.useAmbushFormation();
    }
    
    public void useLineFormation() {
	
	this.PlayerCheck();
	this.EnemyCheck();
	
	List<Unit> enus = game.enemy().getUnits();
	List<Unit> eus = game.self().getUnits();
	List<TilePosition> posPlayerUnits = new LinkedList<TilePosition>();
	
	for (int i = 0; i < eus.size(); i++) {
	    
	    posPlayerUnits.add(eus.get(i).getTilePosition());
	}
	
	int EnemyCount = this.EnemyCheck();
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
			    eus.get(i).move(new TilePosition(posPlayerUnits.get(i-1).getX() + 15, posPlayerUnits.get(i-1).getY() + 16).toPosition());
			}
			else {
			    eus.get(i).move(new TilePosition(posPlayerUnits.get(i).getX(), posPlayerUnits.get(i-1).getY() + 16).toPosition());
			}
		    }
		}
	    }
	}
    }
    
    public void useAmbushFormation() {

    }
    






    public int EnemyCheck() {
	List<Unit> enus = game.enemy().getUnits();
	game.drawTextScreen( 50, 50, ("Enemies (Visible) Units: " + enus.size()));
	if (enus.size() > 0) {
	    return enus.size();
	}
	else {
	    return 0;
	}
    }
    
    public int PlayerCheck() {
	List<Unit> eus = game.self().getUnits();
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
