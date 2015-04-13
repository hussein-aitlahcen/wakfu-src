package com.ankamagames.baseImpl.common.clientAndServer.game.inventory;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public interface InventoryContentChecker<ContentType extends InventoryContent>
{
    public static final int OK_STACKED = 1;
    public static final int OK = 0;
    public static final int INVENTORY_FULL = -1;
    public static final int ITEM_NOT_PRESENT = -2;
    public static final int WRONG_ITEM_TYPE = -3;
    public static final int INVALID_CRITERION = -4;
    public static final int POSITION_INVALID = -5;
    public static final int WRONG_INVENTORY_TYPE = -6;
    public static final int EXCLUSIVE_EQUIPMENT_ALREADY_PRESENT = -7;
    public static final int UNAUTHORIZED_OPERATION = -8;
    public static final int POSITION_ALREADY_OCCUPIED = -9;
    public static final int WRONG_PACK_SIZE = -10;
    public static final int ITEM_NOT_IDENTIFIED = -11;
    
    int canAddItem(Inventory<ContentType> p0, ContentType p1);
    
    int canAddItem(Inventory<ContentType> p0, ContentType p1, short p2);
    
    int canReplaceItem(Inventory<ContentType> p0, ContentType p1, ContentType p2);
    
    int canRemoveItem(Inventory<ContentType> p0, ContentType p1);
    
    boolean checkCriterion(ContentType p0, EffectUser p1, EffectContext p2);
    
    boolean checkCriterion(Inventory<ContentType> p0, EffectUser p1, EffectContext p2);
}
