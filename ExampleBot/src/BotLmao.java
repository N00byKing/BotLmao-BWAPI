import java.util.*;
import bwapi.*;

public class BotLmao extends DefaultBWListener {
    
    private Mirror mirror = new Mirror();

    private Game game;
    
    List<Unit> enus;
    List<Unit> eus;
    
    List<TilePosition> posPlayerUnits;
    
    int EnemyCount;
    
    int AttackStartFrame;
    int WalkTimeCounter;
    @Override
    public void onStart() {
	game = mirror.getGame();
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
	else if (game.mapFileName().equals("03_Zehnkampf.scx")) {
	    game.drawTextScreen( 50, 90, ("Using Backtrackformation"));
	    this.useLineFormation(); 
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
		if ((i % 2) == 0) {
		    eus.get(i).move(new TilePosition(posPlayerUnits.get(i).getX() - 2, posPlayerUnits.get(i).getY() + 3).toPosition());
		}
		else {
		    eus.get(i).move(new TilePosition(posPlayerUnits.get(i).getX() + 2, posPlayerUnits.get(i).getY() + 3).toPosition());
		}
			    
	    }   
	}
	else { 
	    if (AttackStartFrame == 0) {
		AttackStartFrame = game.getFrameCount();
	    }
	    for (int j = 0; j < enus.size(); j++) {
		    if (enus.get(j).getType() == enus.get(j).getType().Terran_Firebat) {

			
			    for (int i = 0; i < eus.size(); i++) {
				if (AttackStartFrame <= (game.getFrameCount() - 25)) {
				    eus.get(i).move((new TilePosition(this.posPlayerUnits.get(i).getX(), this.posPlayerUnits.get(i).getY() - 20)).toPosition());
				    WalkTimeCounter++;
				    if (WalkTimeCounter == 5) {
					AttackStartFrame = 0;
					WalkTimeCounter = 0;
				    }
				}
				else if (eus.get(i).getLastCommandFrame() >= game.getFrameCount() || eus.get(i).isAttackFrame() && WalkTimeCounter < 1) {
				}
				else if (eus.get(i).getLastCommand().getUnitCommandType() == UnitCommandType.Attack_Unit && WalkTimeCounter < 1) {
				}
				else {
				    eus.get(i).attack(enus.get(j));
				}
				
			    }
			
		    }
		    else if (enus.get(j+1).getType() == enus.get(j+1).getType().Terran_Firebat) {

			
			    for (int i = 0; i < eus.size(); i++) {
				if (AttackStartFrame <= (game.getFrameCount() - 25) || WalkTimeCounter < 5) {
				    eus.get(i).move((new TilePosition(this.posPlayerUnits.get(i).getX(), this.posPlayerUnits.get(i).getY() - 20)).toPosition());
				    WalkTimeCounter++;
				    if (WalkTimeCounter == 5) {
					AttackStartFrame = 0;
					WalkTimeCounter = 0;
				    }
				}
				else if (eus.get(i).getLastCommandFrame() >= game.getFrameCount() || eus.get(i).isAttackFrame() || WalkTimeCounter < 1) {
				}
				else if (eus.get(i).getLastCommand().getUnitCommandType() == UnitCommandType.Attack_Unit || WalkTimeCounter < 1) {
				}
				else {
				    eus.get(i).attack(enus.get(j+1));
				}
				
			    }
			
		    }
		    else  {
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
		
	    }
	 } 
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
    
    
    public void useAmbushFormation() {
	int test = 0;
	for(int i = 0; i < eus.size();i++) { 
	    if (EnemyCount > 0) {
		if (eus.get(i).getLastCommandFrame() >= game.getFrameCount() || eus.get(i).isAttackFrame()) {
		}
		else if (eus.get(i).getLastCommand().getUnitCommandType() == UnitCommandType.Attack_Unit) {
		}
		else {
		    if (enus.get(i).isVisible()) {
			eus.get(i).attack(enus.get(i));
		    }
		}
	    }
	    else if ( (i % 2) == 0) {
		eus.get(i).move(new TilePosition(this.posPlayerUnits.get(i).getX() - 5, this.posPlayerUnits.get(i).getY()).toPosition());
	    }
	    else {
		if (this.posPlayerUnits.get(i).getX() <= 35 && test == 0) {
		    eus.get(i).move(new TilePosition(this.posPlayerUnits.get(i).getX() + 5, this.posPlayerUnits.get(i).getY()).toPosition());
		}
		else {
		    eus.get(i).move(new TilePosition(this.posPlayerUnits.get(i).getX() - 5, this.posPlayerUnits.get(i).getY() + 5).toPosition());
		    test = 1;
		}
		
	    }
	    
	}
	    
    }
    
    public int getSide() {
   	if (game.getStartLocations().get(0).getY() == 0) {
   	    return 1;
   	}
   	return 1;
       }
    
    
    
    
    
   
    
    
    //Unveränderbar
    public void useNullFormation() {
	
    }

   
    
    
    public void run() {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    public static void main(String[] args) {
        new BotLmao().run();
    }
}
