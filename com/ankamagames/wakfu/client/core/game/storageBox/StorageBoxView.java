package com.ankamagames.wakfu.client.core.game.storageBox;

public interface StorageBoxView
{
    CompartmentView getSelectedCompartment();
    
    CompartmentView getCompartmentByIndex(byte p0);
    
    void setSelectedCompartment(CompartmentView p0);
    
    void updateFields();
    
    int getSize();
    
    void setMoney(int p0);
    
    int getMoney();
    
    void depositMoney(int p0);
    
    void withdrawMoney(int p0);
    
    void clear();
}
