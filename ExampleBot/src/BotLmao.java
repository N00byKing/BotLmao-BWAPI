import java.util.LinkedList;
import java.util.List;

import bwapi.*;

public class BotLmao extends DefaultBWListener {
    
    private Mirror mirror = new Mirror();

    private Game game;
    private Player self;
    
    
    @Override
    public void onStart() {
	game = mirror.getGame();
	self = game.self();
	game.sendText("Ayy");
    }
    
	
    public void onFrame() {
	this.PlayerCheck();
	
	List<Unit> enus = game.enemy().getUnits();
	List<Unit> eus = game.self().getUnits();
	List<TilePosition> posPlayerUnits = new LinkedList<TilePosition>();
	
	for (int i = 0; i < eus.size(); i++) {
	    
	    posPlayerUnits.add(eus.get(i).getTilePosition());
	}
	
	
	if (this.EnemyCheck() > 0 ) {
	    for (int i = 0; i < eus.size(); i++) {
		if (eus.get(i).getLastCommandFrame() >= game.getFrameCount() || eus.get(i).isAttackFrame()) {
		    game.sendText("SNOOP");
		}
		else if (eus.get(i).getLastCommand().getUnitCommandType() == UnitCommandType.Attack_Unit) {
		    game.sendText("WOOP");
		}
		else {
		    if (this.EnemyCheck() > 1) {
			eus.get(i).attack(enus.get(0));
		    }
		    else {
			eus.get(i).attack(enus.get(1));
		    }
		    
		}
	    }
	    
	}
	else {
	    eus.get(0).move(new TilePosition(posPlayerUnits.get(0).getX(), posPlayerUnits.get(0).getY() + 16).toPosition());
	    eus.get(1).move(new TilePosition(posPlayerUnits.get(0).getX() + 15, posPlayerUnits.get(0).getY() + 16).toPosition());
	}
    }
    
    public int EnemyCheck() {
	List<Unit> enus = game.enemy().getUnits();
	
	if (enus.size() > 0) {
	    game.sendText("Enemies (Visible) Units: " + enus.size());
	    return enus.size();
	}
	else {
	    return 0;
	}
    }
    
    public int PlayerCheck() {
	List<Unit> eus = game.self().getUnits();
	
	if (eus.size() > 0) {
	    game.sendText("BotLmao's Units: " + eus.size());
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
