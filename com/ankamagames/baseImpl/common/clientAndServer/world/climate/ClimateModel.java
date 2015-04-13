package com.ankamagames.baseImpl.common.clientAndServer.world.climate;

public class ClimateModel
{
    private int m_id;
    private float m_rainEventProba;
    private float m_rainHeight;
    private int m_raindDistribution;
    private float m_tMin;
    private float m_tMax;
    private float m_kMin;
    private float m_kMax;
    private float m_wMin;
    private float m_wMax;
    
    public ClimateModel() {
        super();
        this.m_id = 0;
        this.m_rainEventProba = 0.5f;
        this.m_rainHeight = 50.0f;
        this.m_raindDistribution = 2;
        this.m_tMin = 5.0f;
        this.m_tMax = 5.0f;
        this.m_kMin = 3.0f;
        this.m_kMax = 10.0f;
        this.m_wMin = 0.0f;
        this.m_wMax = 0.5f;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public void setId(final int id) {
        this.m_id = id;
    }
    
    public float getRainEventProba() {
        return this.m_rainEventProba;
    }
    
    public void setRainEventProba(final float rainEventProba) {
        this.m_rainEventProba = rainEventProba;
    }
    
    public int getRaindDistribution() {
        return this.m_raindDistribution;
    }
    
    public void setRaindDistribution(final int raindDistribution) {
        this.m_raindDistribution = raindDistribution;
    }
    
    public float getRainHeight() {
        return this.m_rainHeight;
    }
    
    public void setRainHeight(final float rainHeight) {
        this.m_rainHeight = rainHeight;
    }
    
    public float getTMin() {
        return this.m_tMin;
    }
    
    public void setTMin(final float tMin) {
        this.m_tMin = tMin;
    }
    
    public float getTMax() {
        return this.m_tMax;
    }
    
    public void setTMax(final float tMax) {
        this.m_tMax = tMax;
    }
    
    public float getKMin() {
        return this.m_kMin;
    }
    
    public void setKMin(final float kMin) {
        this.m_kMin = kMin;
    }
    
    public float getKMax() {
        return this.m_kMax;
    }
    
    public void setKMax(final float kMax) {
        this.m_kMax = kMax;
    }
    
    public float getWMin() {
        return this.m_wMin;
    }
    
    public void setWMin(final float wMin) {
        this.m_wMin = wMin;
    }
    
    public float getWMax() {
        return this.m_wMax;
    }
    
    public void setWMax(final float wMax) {
        this.m_wMax = wMax;
    }
}
