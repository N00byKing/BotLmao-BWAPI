
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
    
    drawOrders(game, eus);
    
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
          } else {
            if (eus.get(i - 1).exists()) {
              eus.get(i).move(new TilePosition(posPlayerUnits.get(i - 1).getX() + 10, posPlayerUnits.get(i - 1).getY() + 16).toPosition());
            } else {
              eus.get(i).move(new TilePosition(posPlayerUnits.get(i).getX(), posPlayerUnits.get(i - 1).getY() + 16).toPosition());
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
        
        for (int j = 0; j < enus.size(); j++) {
          if (!firebats.get(j).exists()) {
            firebats.remove(j);
          }
          if (firebats.size() > 0) {
            for (int i = 0; i < eus.size(); i++) {
              Unit eu = eus.get(i);
              Unit enu = enus.get(j);
              UnitType enuTy = enu.getType();
              
              if (enuTy != UnitType.Terran_Firebat) {
                j = j+1;
                enu = enus.get(j);
              }
              
              int maxRadius = 110;
              int minRadius = 68;
              if (eu.exists()) {
                game.drawCircleMap(eu.getPosition(), minRadius, Color.Red);
                game.drawCircleMap(eu.getPosition(), maxRadius, Color.Green);
                game.drawTextScreen( 50, 110 + 20*i, ("Distance to Firebat ( Unit No." + i + " ) : " + (enu.getY() - eu.getY())));
                game.drawText(bwapi.CoordinateType.Enum.Map, eu.getX(), eu.getY() - 3 , "Unit No. " + i);
              }
              
              if (enu.getDistance(eu) <= maxRadius && enu.getDistance(eu) >= minRadius) {
                if ((i % 2) == 0) {
                  eu.move((new TilePosition(this.posPlayerUnits.get(i).getX() - 3, this.posPlayerUnits.get(i).getY() - 10)).toPosition());
                }
                else {
                  eu.move((new TilePosition(this.posPlayerUnits.get(i).getX() + 3, this.posPlayerUnits.get(i).getY() - 10)).toPosition());
                }
              }
              else if (eu.getLastCommandFrame() >= game.getFrameCount() || eu.isAttackFrame()) {
              }
              else if (eu.getLastCommand().getUnitCommandType() == UnitCommandType.Attack_Unit) {
              }
              else {
                if (enu.isVisible()) {
                  eu.attack(enu);
                }
              }
            }
          }
          else {
            for (int i = 0; i < eus.size(); i++) {
              if (!enus.get(j).isVisible()) {
                eus.get(i).move(new TilePosition(eus.get(i).getX(), eus.get(i).getY() + 10).toPosition());
              }
              else {
                if (eus.get(i).getLastCommandFrame() >= game.getFrameCount() || eus.get(i).isAttacking()) {
                }
                else if (eus.get(i).getLastCommand().getUnitCommandType() == UnitCommandType.Attack_Unit) {
                }
                else {
                  eus.get(i).attack(game.enemy().getUnits().get(j));
                }
              }
            }
          }
        }
      }
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
    for (int i = 0; i < eus.size(); i++) {
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
      else if ((i % 2) == 0) {
        eus.get(i).move(new TilePosition(this.posPlayerUnits.get(i).getX() - 5, this.posPlayerUnits.get(i).getY()).toPosition());
      }
      else {
        if (this.posPlayerUnits.get(i).getX() <= 35) {
          eus.get(i).move(new TilePosition(this.posPlayerUnits.get(i).getX() + 5, this.posPlayerUnits.get(i).getY()).toPosition());
        } else {
          eus.get(i).move(new TilePosition(this.posPlayerUnits.get(i).getX() - 5, this.posPlayerUnits.get(i).getY() + 5).toPosition());
        }
      } 
    }
  }

    // Unveränderbar
  private void drawOrders(Game state, List<Unit> units) {
    for (Unit u : units) {
      if (u.isIdle())
        continue;
      
      Position pos = u.getOrderTargetPosition();
      bwapi.Color c = bwapi.Color.Green;
      
      UnitCommandType ty = u.getLastCommand().getUnitCommandType();
      if (ty == UnitCommandType.Attack_Move || ty == UnitCommandType.Attack_Unit) {
        c = bwapi.Color.Red; 
      }
      game.drawLineMap(u.getPosition(), pos, c);
    }
  }
    
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