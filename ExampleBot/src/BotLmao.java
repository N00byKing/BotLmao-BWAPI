import java.util.*;
import bwapi.*;

public class BotLmao extends DefaultBWListener {

    private Mirror mirror = new Mirror();

    private Game game;

    List<Unit> enus;
    List<Unit> eus;

    List<TilePosition> posPlayerUnits;

    int EnemyCount;
    int WalkTimeCounter;

    @Override
    public void onStart() {
	game = mirror.getGame();
	eus = new ArrayList<Unit>();
	enus = new ArrayList<Unit>();
	game.sendText("BotLmao has initialised");
    }
    
    public void onUnitDiscover(Unit unit) {
	if (unit.getPlayer() == game.self()) {
	    eus.add(unit);
	} else if (unit.getPlayer() == game.enemy()) {
	    enus.add(unit);
	}
	enus.sort(new Comparator<Unit>() {
	    @Override
	    public int compare(Unit o1, Unit o2) {
		int compare = 0;
		compare = o1.getInitialHitPoints() - o2.getInitialHitPoints();
		return compare;
	    }
	});
    }

    public void onFrame() {

	EnemyCount = EnemyCheck();

	this.PlayerCheck();
	this.EnemyCheck();

	posPlayerUnits = new LinkedList<TilePosition>();

	for (int i = 0; i < eus.size(); i++) {
	    posPlayerUnits.add(eus.get(i).getTilePosition());
	}

	if (game.mapFileName().equals("04_Zehnkampf_Multi.scx")) {
	    game.drawTextScreen(50, 90, ("Using Nullformation"));
	    this.useNullFormation();
	} else if (game.mapFileName().equals("01_BattleRoyale.scx")) {
	    game.drawTextScreen(50, 90, ("Using Lineformation"));
	    this.useLineFormation();
	} else if (game.mapFileName().equals("02_Zweikampf.scx")) {
	    game.drawTextScreen(50, 90, ("Using Backtrackformation"));
	    this.useBacktrackFormation();
	} else if (game.mapFileName().equals("03_Zehnkampf.scx")) {
	    game.drawTextScreen(50, 90, ("Using Backtrackformation"));
	    this.useBacktrackFormation();
	} else {
	    game.drawTextScreen(50, 90, ("No specific Map formation. Defaulting to Lineformation"));
	    this.useLineFormation();
	}

    }

    public void useLineFormation() {
	for (int j = 0; j < EnemyCount + 1; j++) {
	    if (game.enemy().getUnits().size() > 0) {
		for (int i = 0; i < eus.size(); i++) {
		    if (eus.get(i).getLastCommandFrame() >= game.getFrameCount()
			    || eus.get(i).isAttackFrame()) {
		    } else if (eus.get(i).getLastCommand()
			    .getUnitCommandType() == UnitCommandType.Attack_Unit) {
		    } else {
			if (enus.get(j).isVisible()) {
			    eus.get(i).attack(enus.get(j));
			}
		    }
		}
	    }

	    else {
		for (int i = 0; i < eus.size(); i++) {

		    if (i == 0) {
			eus.get(i)
				.move(new TilePosition(
					posPlayerUnits.get(i).getX(),
					posPlayerUnits.get(i).getY() + 16)
						.toPosition());
		    } else {
			if (eus.get(i - 1).exists()) {
			    eus.get(i).move(new TilePosition(
				    posPlayerUnits.get(i - 1).getX() + 10,
				    posPlayerUnits.get(i - 1).getY() + 16)
					    .toPosition());
			} else {
			    eus.get(i).move(new TilePosition(
				    posPlayerUnits.get(i).getX(),
				    posPlayerUnits.get(i - 1).getY() + 16)
					    .toPosition());
			}
		    }
		}
	    }
	}
    }

    public void useBacktrackFormation() {
	try {
	    
	
	
	if (game.enemy().getUnits().size() <= 0) {
	    for (int i = 0; i < eus.size(); i++) {
		if ((i % 2) == 0) {
		    eus.get(i).move(new TilePosition(posPlayerUnits.get(i).getX() - 2, posPlayerUnits.get(i).getY() + 10).toPosition());
		}
		else {
		    eus.get(i).move(new TilePosition(posPlayerUnits.get(i).getX() + 5, posPlayerUnits.get(i).getY() + 10).toPosition());
		}
			     
	    }   
	}
	else { 
	    List<Unit> firebats = new LinkedList<Unit>();
	    for (Unit u : enus) {
		if (u.getType() == UnitType.Terran_Firebat) {
		    firebats.add(u);
		}
	    }
	    
	    for (Unit f : firebats) {
		    for (int i = 0; i < eus.size(); i++) {
					game.drawTextScreen( 50, 110 + 20*i, ("Distance to Firebat: " + (f.getY() - eus.get(i).getY())));
					if (f.getDistance(eus.get(i)) <= 110 && f.getDistance(eus.get(i)) >= 68  && !f.isAttacking()) {
					    if ((i % 2) == 0) {
						eus.get(i).move((new TilePosition(this.posPlayerUnits.get(i).getX() - 3, this.posPlayerUnits.get(i).getY() - 10)).toPosition());
					    }
					    else {
						eus.get(i).move((new TilePosition(this.posPlayerUnits.get(i).getX() + 3, this.posPlayerUnits.get(i).getY() - 10)).toPosition());
					    }
					    
					}
					else if (eus.get(i).getLastCommandFrame() >= game.getFrameCount() || eus.get(i).isAttackFrame()) {
					}
					else if (eus.get(i).getLastCommand().getUnitCommandType() == UnitCommandType.Attack_Unit) {
					}
					else {
					    if (f.isVisible()) {
						eus.get(i).attack(f);
					    }
					}
				    }
	    }
	    
	    // Kein Firebat mehr da.
	    if (firebats.size() == 0 && enus.size() == 1) {
		for (int j = 0; j < enus.size(); j++) {
            		for (int i = 0; i < eus.size(); i++) {
                    	    eus.get(i).move(new TilePosition(eus.get(i).getX(), eus.get(i).getY() - 10).toPosition());
                    	    game.sendText("AyyAdadfwDAWD");
                    	    if (!enus.get(j).isVisible()) {
                    		game.sendText("Where are yuuu :3");
                    		eus.get(i).move(new TilePosition(eus.get(i).getX(), eus.get(i).getY() + 10).toPosition());
                    	    }
                    	    else {
                    		game.sendText("Imma getcha :3");
                    		if (eus.get(i).getLastCommandFrame() >= game.getFrameCount() || eus.get(i).isAttackFrame()) {
                    		    game.sendText("Ayy");
                    		}
                    		else if (eus.get(i).getLastCommand().getUnitCommandType() == UnitCommandType.Attack_Unit) {
                    		    game.sendText("Ayy2");
                    		}
                    		else {
                    		    eus.get(i).attack(game.enemy().getUnits().get(j));
                    		}
                    	    }
                    	    
                    	}
		}
	    }
	}
//	    for (int j = 0; j < enus.size(); j++) {
//		    if (enus.get(j).getType() == UnitType.Terran_Firebat) {
//			    for (int i = 0; i < eus.size(); i++) {
//				game.drawTextScreen( 50, 110 + 20*i, ("Distance to Firebat: " + (enus.get(j).getY() - eus.get(i).getY())));
//				if (enus.get(j).getDistance(eus.get(i)) <= 110 && enus.get(j).getDistance(eus.get(i)) >= 68  && !enus.get(j).isAttacking()) {
//				    if ((i % 2) == 0) {
//					eus.get(i).move((new TilePosition(this.posPlayerUnits.get(i).getX() - 3, this.posPlayerUnits.get(i).getY() - 10)).toPosition());
//				    }
//				    else {
//					eus.get(i).move((new TilePosition(this.posPlayerUnits.get(i).getX() + 3, this.posPlayerUnits.get(i).getY() - 10)).toPosition());
//				    }
//				    
//				}
//				else if (eus.get(i).getLastCommandFrame() >= game.getFrameCount() || eus.get(i).isAttackFrame()) {
//				}
//				else if (eus.get(i).getLastCommand().getUnitCommandType() == UnitCommandType.Attack_Unit) {
//				}
//				else {
//				    if (enus.get(j).isVisible()) {
//					eus.get(i).attack(enus.get(j));
//				    }
//				}
//			    }
//		    }
//		    else  {
//			for (int i = 0; i < eus.size(); i++) {
//			    eus.get(i).move(new TilePosition(eus.get(i).getX(), eus.get(i).getY() - 10).toPosition());
//			    game.sendText("AyyAdadfwDAWD");
//			    if (!enus.get(j).isVisible()) {
//				game.sendText("Where are yuuu :3");
//				eus.get(i).move(new TilePosition(eus.get(i).getX(), eus.get(i).getY() + 10).toPosition());
//			    }
//			    else {
//				game.sendText("Imma getcha :3");
//				if (eus.get(i).getLastCommandFrame() >= game.getFrameCount() || eus.get(i).isAttackFrame()) {
//				    game.sendText("Ayy");
//				}
//				else if (eus.get(i).getLastCommand().getUnitCommandType() == UnitCommandType.Attack_Unit) {
//				    game.sendText("Ayy2");
//				}
//				else {
//				    eus.get(i).attack(game.enemy().getUnits().get(j));
//				}
//			    }
//			    
//			}
//		    }
//	    }
//	 }
	}
	catch (Throwable e) {
	    e.printStackTrace();
	}
    }

    public int EnemyCheck() {
	game.drawTextScreen(50, 50,
		("Enemies (Visible) Units: " + game.enemy().getUnits().size()));
	if (enus.size() > 0) {
	    return enus.size();
	} else {
	    return 0;
	}
    }

    public int PlayerCheck() {
	game.drawTextScreen(50, 70,
		("BotLmao's Units: " + game.self().getUnits().size()));
	if (eus.size() > 0) {
	    return eus.size();
	} else {
	    return 0;
	}
    }

    public void useAmbushFormation() {
	int test = 0;
	for (int i = 0; i < eus.size(); i++) {
	    if (EnemyCount > 0) {
		if (eus.get(i).getLastCommandFrame() >= game.getFrameCount()
			|| eus.get(i).isAttackFrame()) {
		} else if (eus.get(i).getLastCommand()
			.getUnitCommandType() == UnitCommandType.Attack_Unit) {
		} else {
		    if (enus.get(i).isVisible()) {
			eus.get(i).attack(enus.get(i));
		    }
		}
	    } else if ((i % 2) == 0) {
		eus.get(i)
			.move(new TilePosition(
				this.posPlayerUnits.get(i).getX() - 5,
				this.posPlayerUnits.get(i).getY())
					.toPosition());
	    } else {
		if (this.posPlayerUnits.get(i).getX() <= 35 && test == 0) {
		    eus.get(i)
			    .move(new TilePosition(
				    this.posPlayerUnits.get(i).getX() + 5,
				    this.posPlayerUnits.get(i).getY())
					    .toPosition());
		} else {
		    eus.get(i)
			    .move(new TilePosition(
				    this.posPlayerUnits.get(i).getX() - 5,
				    this.posPlayerUnits.get(i).getY() + 5)
					    .toPosition());
		    test = 1;
		}

	    }

	}

    }

    // Unveränderbar
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
