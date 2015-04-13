package com.ankamagames.wakfu.common.datas;

public class NonPlayerCharacterSerializer extends CharacterSerializer
{
    private final NPCSerializedAppearance m_appearancePart;
    private final NPCSerializedCharacteristics m_characteristicsPart;
    private final NPCSerializedGroup m_groupPart;
    private final NPCSerializedUserTemplate m_templatePart;
    private final NPCSerializedCollect m_collectPart;
    private final NPCCompanionInfo m_controllerId;
    
    public NonPlayerCharacterSerializer() {
        super();
        this.m_appearancePart = new NPCSerializedAppearance();
        this.m_characteristicsPart = new NPCSerializedCharacteristics();
        this.m_groupPart = new NPCSerializedGroup();
        this.m_templatePart = new NPCSerializedUserTemplate();
        this.m_collectPart = new NPCSerializedCollect();
        this.m_controllerId = new NPCCompanionInfo();
    }
    
    @Override
    public NPCSerializedAppearance getAppearancePart() {
        return this.m_appearancePart;
    }
    
    @Override
    public NPCSerializedCharacteristics getPrivateCharacteristicsPart() {
        return this.m_characteristicsPart;
    }
    
    @Override
    public NPCSerializedCharacteristics getPublicCharacteristicsPart() {
        return this.m_characteristicsPart;
    }
    
    @Override
    public NPCSerializedGroup getGroupPart() {
        return this.m_groupPart;
    }
    
    @Override
    public NPCSerializedUserTemplate getTemplatePart() {
        return this.m_templatePart;
    }
    
    @Override
    public NPCSerializedCollect getCollectPart() {
        return this.m_collectPart;
    }
    
    @Override
    public NPCCompanionInfo getCompanionControllerIdPart() {
        return this.m_controllerId;
    }
    
    @Override
    public void clear() {
        super.clear();
        this.m_appearancePart.clear();
        this.m_characteristicsPart.clear();
        this.m_groupPart.clear();
        this.m_templatePart.clear();
        this.m_collectPart.clear();
    }
}
