package com.ankamagames.wakfu.common.game.nation.protector;

import com.ankamagames.wakfu.common.game.tax.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.nation.*;

public class NationProtectorInfo
{
    private final int m_id;
    private Nation m_nation;
    private int m_cash;
    private float m_fleaTaxValue;
    private float m_marketTaxValue;
    private int m_currentSatisfaction;
    private final int m_totalSatisfaction;
    private boolean m_inChaos;
    private String m_serverIdString;
    
    protected NationProtectorInfo(final int id, final Nation nation, final boolean isChaos, final int cash, final float fleaTaxValue, final float marketTaxValue, final int currentSatisfaction, final int totalSatisfaction) {
        this(id, nation, isChaos, cash, fleaTaxValue, marketTaxValue, currentSatisfaction, totalSatisfaction, null);
    }
    
    protected NationProtectorInfo(final int id, final Nation nation, final boolean isChaos, final int cash, final float fleaTaxValue, final float marketTaxValue, final int currentSatisfaction, final int totalSatisfaction, final String serverIdString) {
        super();
        this.m_id = id;
        this.m_nation = nation;
        this.m_inChaos = isChaos;
        this.m_cash = cash;
        this.m_fleaTaxValue = fleaTaxValue;
        this.m_marketTaxValue = marketTaxValue;
        this.m_currentSatisfaction = currentSatisfaction;
        this.m_totalSatisfaction = totalSatisfaction;
        this.m_serverIdString = serverIdString;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public Nation getNation() {
        return this.m_nation;
    }
    
    public int getNationId() {
        return this.m_nation.getNationId();
    }
    
    public void setNation(final Nation nation) {
        this.m_nation = nation;
    }
    
    public boolean isInChaos() {
        return this.m_inChaos;
    }
    
    public void setInChaos(final boolean inChaos) {
        this.m_inChaos = inChaos;
    }
    
    public int getCash() {
        return this.m_cash;
    }
    
    public void adjustCash(final int delta) {
        this.m_cash += delta;
    }
    
    public void setSatisfaction(final int currentSatisfaction) {
        this.m_currentSatisfaction = currentSatisfaction;
    }
    
    public float getFleaTaxValue() {
        return this.m_fleaTaxValue;
    }
    
    public float getMarketTaxValue() {
        return this.m_marketTaxValue;
    }
    
    public void setTaxValue(final TaxContext context, final float tax) {
        switch (context) {
            case FLEA_ADD_ITEM_CONTEXT: {
                this.m_fleaTaxValue = tax;
                break;
            }
            case MARKET_ADD_ITEM_CONTEXT: {
                this.m_marketTaxValue = tax;
                break;
            }
            default: {
                throw new UnsupportedOperationException("Context de taxe non g\u00e9r\u00e9 : " + context);
            }
        }
    }
    
    public int getCurrentSatisfaction() {
        return this.m_currentSatisfaction;
    }
    
    public int getTotalSatisfaction() {
        return this.m_totalSatisfaction;
    }
    
    public String getServerIdString() {
        return this.m_serverIdString;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("NationProtectorInfo");
        sb.append("{m_id=").append(this.m_id);
        sb.append(", m_nation=").append(this.m_nation);
        sb.append(", m_isChaos=").append(this.m_inChaos);
        sb.append(", m_cash=").append(this.m_cash);
        sb.append(", m_fleaTaxValue=").append(this.m_fleaTaxValue);
        sb.append(", m_marketTaxValue=").append(this.m_marketTaxValue);
        sb.append(", m_currentSatisfaction=").append(this.m_currentSatisfaction).append(" / ").append(this.m_totalSatisfaction);
        sb.append(", m_serverIdString=").append(this.m_serverIdString);
        sb.append('}');
        return sb.toString();
    }
    
    public void serialize(final ByteBuffer buffer) {
        buffer.putInt(this.m_id);
        buffer.putInt(this.m_nation.getNationId());
        buffer.put((byte)(this.m_inChaos ? 1 : 0));
        buffer.putInt(this.m_cash);
        buffer.putFloat(this.m_fleaTaxValue);
        buffer.putFloat(this.m_marketTaxValue);
        buffer.putInt(this.m_currentSatisfaction);
        buffer.putInt(this.m_totalSatisfaction);
    }
    
    public static NationProtectorInfo fromBuild(final ByteBuffer buffer) {
        final int protectorId = buffer.getInt();
        final Nation nation = NationManager.INSTANCE.getNationById(buffer.getInt());
        return new NationProtectorInfo(protectorId, nation, buffer.get() != 0, buffer.getInt(), buffer.getFloat(), buffer.getFloat(), buffer.getInt(), buffer.getInt());
    }
    
    public static int serializedSize() {
        return 29;
    }
}
