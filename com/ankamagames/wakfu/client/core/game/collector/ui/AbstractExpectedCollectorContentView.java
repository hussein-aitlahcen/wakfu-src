package com.ankamagames.wakfu.client.core.game.collector.ui;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.*;

public abstract class AbstractExpectedCollectorContentView extends ImmutableFieldProvider implements CollectorContentView
{
    public static final String MAX_QUANTITY_FIELD = "maxQuantity";
    public static final String QUANTITY_FIELD = "quantity";
    public static final String QUANTITY_RATIO_FIELD = "quantityRatio";
    public static final String CURRENT_PLAYER_QUANTITY_FIELD = "currentPlayerQuantity";
    public static final String MAX_PLAYER_QUANTITY_FIELD = "maxPlayerQuantity";
    public static final String TOTAL_PLAYER_QUANTITY_FIELD = "totalPlayerQuantity";
    public static final String CAN_MAX_FIELD = "canMax";
    public static final String VALID_FIELD = "valid";
    public static final String EDITABLE_FIELD = "editable";
    public static final String CITIZEN_POINTS_FIELD = "citizenPoints";
    public static final String CITIZEN_POINTS_TEXT_FIELD = "citizenPointsText";
    private final String[] FIELDS;
    private int m_currentPlayerQuantity;
    
    public AbstractExpectedCollectorContentView() {
        super();
        this.FIELDS = new String[] { "maxQuantity", "quantity", "quantityRatio", "currentPlayerQuantity", "maxPlayerQuantity", "totalPlayerQuantity", "canMax", "valid", "editable", "citizenPoints", "citizenPointsText" };
        this.m_currentPlayerQuantity = 0;
    }
    
    @Override
    public String[] getFields() {
        return this.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("currentPlayerQuantity")) {
            return this.getCurrentPlayerQuantity();
        }
        if (fieldName.equals("quantity")) {
            return this.getCurrentQuantity();
        }
        if (fieldName.equals("maxQuantity")) {
            return this.getMaxQuantity();
        }
        if (fieldName.equals("maxPlayerQuantity")) {
            return this.getTotalPlayerQuantity();
        }
        if (fieldName.equals("quantityRatio")) {
            return this.getCurrentQuantity() / this.getMaxQuantity();
        }
        if (fieldName.equals("totalPlayerQuantity")) {
            return this.getTotalPlayerQuantity();
        }
        if (fieldName.equals("canMax")) {
            return this.canMax();
        }
        if (fieldName.equals("valid")) {
            return this.isValid();
        }
        if (fieldName.equals("citizenPoints")) {
            return 0;
        }
        if (fieldName.equals("citizenPointsText")) {
            return WakfuTranslator.getInstance().getString("chaos.citizenPointsNeeded", 1, 50);
        }
        if (fieldName.equals("editable")) {
            return this.isEditable();
        }
        return null;
    }
    
    private Boolean isEditable() {
        return this.getMaxPlayerQuantity() > 0 && this.getNeededQuantity() > 0;
    }
    
    private boolean isValid() {
        return this.getCurrentPlayerQuantity() > 0 && this.getNeededQuantity() > 0;
    }
    
    @Override
    public int getMaxPlayerQuantity() {
        return Math.min(this.getNeededQuantity(), this.getTotalPlayerQuantity());
    }
    
    @Override
    public abstract int getTotalPlayerQuantity();
    
    @Override
    public int getNeededQuantity() {
        return this.getMaxQuantity() - this.getCurrentQuantity();
    }
    
    @Override
    public abstract int getMaxQuantity();
    
    @Override
    public abstract int getCurrentQuantity();
    
    @Override
    public int getCurrentPlayerQuantity() {
        return this.m_currentPlayerQuantity;
    }
    
    @Override
    public void setCurrentPlayerQuantity(final int currentPlayerQuantity) {
        this.m_currentPlayerQuantity = currentPlayerQuantity;
    }
    
    @Override
    public boolean canMax() {
        return this.getCurrentPlayerQuantity() != this.getMaxQuantity() && (this.isValid() || (this.getCurrentPlayerQuantity() == 0 && this.getNeededQuantity() > 0 && this.getMaxPlayerQuantity() > 0));
    }
}
