package com.ankamagames.wakfu.common.game.havenWorld.action;

public interface ActionChecker
{
    void setNextAction(ActionListener p0);
    
    void checkCreateTopology(TopologyCreate p0);
    
    void checkUpdateTopology(TopologyUpdate p0);
    
    void checkCreateBuilding(BuildingCreate p0);
    
    void checkEvolveBuilding(BuildingEvolve p0);
    
    void checkMoveBuilding(BuildingMove p0);
    
    void checkDeleteBuilding(BuildingDelete p0);
    
    void checkEquipBuilding(BuildingEquip p0);
    
    void checkCreateInteractiveElement(InteractiveElementCreate p0);
    
    void checkUpdateInteractiveElement(InteractiveElementUpdate p0);
    
    void checkDeleteInteractiveElement(InteractiveElementDelete p0);
}
