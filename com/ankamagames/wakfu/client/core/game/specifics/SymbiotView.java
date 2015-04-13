package com.ankamagames.wakfu.client.core.game.specifics;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

public final class SymbiotView implements FieldProvider
{
    private static final Logger m_logger;
    private Symbiot m_symbiot;
    private static SymbiotView m_instance;
    public static final String CAPTURED_CREATURES_FIELD = "capturedCreatures";
    public static final String MAX_SIZE_FIELD = "maxSize";
    public static final String SELECTED_CREATURE_FIELD = "selectedCreature";
    public static final String SELECTED_CREATURE_INDEX_FIELD = "selectedCreatureIndex";
    public static final String CURRENT_LEADERSHIP_CAPACITY_FIELD = "currentLeadershipCapacity";
    public static final String[] FIELDS;
    
    public static SymbiotView getInstance() {
        return SymbiotView.m_instance;
    }
    
    public void setSymbiot(final Symbiot symbiot) {
        this.m_symbiot = symbiot;
    }
    
    public SymbiotView(final Symbiot symbiot) {
        super();
        this.m_symbiot = symbiot;
    }
    
    @Override
    public String[] getFields() {
        return SymbiotView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (this.m_symbiot == null) {
            return null;
        }
        if (fieldName.equals("capturedCreatures")) {
            final BasicInvocationCharacteristics[] creatures = new BasicInvocationCharacteristics[5];
            int index = 0;
            for (byte i = 0; i < (byte)creatures.length; ++i) {
                creatures[index++] = this.m_symbiot.getCreatureParametersFromIndex(i);
            }
            return creatures;
        }
        if (fieldName.equals("maxSize")) {
            return this.m_symbiot.getMaximumCreatures();
        }
        if (fieldName.equals("selectedCreature")) {
            return this.m_symbiot.getCurrentCreatureParameters();
        }
        if (fieldName.equals("selectedCreatureIndex")) {
            return this.m_symbiot.getCurrentCreatureIndex();
        }
        if (fieldName.equals("currentLeadershipCapacity")) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            return Math.max(0, localPlayer.getCharacteristic((CharacteristicType)FighterCharacteristicType.LEADERSHIP).value() - localPlayer.getSummoningsCount());
        }
        return null;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public void updateCreaturesList() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "capturedCreatures");
    }
    
    public void updateSelectedCreature() {
        try {
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "selectedCreature", "selectedCreatureIndex");
        }
        catch (Exception e) {
            SymbiotView.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    public void updateLeaderShipCapacity() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentLeadershipCapacity");
    }
    
    public void onCreatureCaptured() {
        if (!Xulor.getInstance().isLoaded("osamodasSymbiotDialog")) {
            UIControlCenterContainerFrame.getInstance().highLightOsamodasSymbiotButton();
        }
        PropertiesProvider.getInstance().setPropertyValue("osamodasSymbiotCreatureCapturedState", true);
    }
    
    static {
        m_logger = Logger.getLogger((Class)SymbiotView.class);
        SymbiotView.m_instance = new SymbiotView(null);
        FIELDS = new String[] { "capturedCreatures", "maxSize", "selectedCreature", "selectedCreatureIndex", "currentLeadershipCapacity" };
    }
}
