package com.ankamagames.wakfu.common.game.havenWorld.action;

public interface ActionComputer
{
    void setNextAction(ActionListener p0);
    
    void createTopology(TopologyCreate p0);
    
    void updateTopology(TopologyUpdate p0);
    
    void createBuilding(BuildingCreate p0);
    
    void evolveBuilding(BuildingEvolve p0);
    
    void moveBuilding(BuildingMove p0);
    
    void deleteBuilding(BuildingDelete p0);
    
    void equipBuilding(BuildingEquip p0);
    
    void createInteractiveElement(InteractiveElementCreate p0);
    
    void updateInteractiveElement(InteractiveElementUpdate p0);
    
    void deleteInteractiveElement(InteractiveElementDelete p0);
}
