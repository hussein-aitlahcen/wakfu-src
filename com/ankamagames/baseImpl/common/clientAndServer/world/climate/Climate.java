package com.ankamagames.baseImpl.common.clientAndServer.world.climate;

import java.util.*;

public class Climate
{
    private static final int DEFAULT_DURATION = 92;
    private final DailyWeather[] m_weatherPerDay;
    private float[] m_rainDistributionKernel;
    private final Random m_random;
    private int m_randomSeed;
    private ClimateModel m_model;
    
    public Climate() {
        this(92);
    }
    
    public Climate(final int duration) {
        super();
        this.m_random = new Random(0L);
        this.m_randomSeed = 0;
        this.m_model = new ClimateModel();
        this.m_weatherPerDay = new DailyWeather[duration];
        for (int i = 0; i < duration; ++i) {
            this.m_weatherPerDay[i] = new DailyWeather(i);
        }
        this.createSmoothingKernel(this.m_model.getRaindDistribution());
    }
    
    public int getDuration() {
        return this.m_weatherPerDay.length;
    }
    
    public DailyWeather getLastDay() {
        return this.m_weatherPerDay[this.m_weatherPerDay.length - 1];
    }
    
    public DailyWeather getFirstDay() {
        return this.m_weatherPerDay[0];
    }
    
    public void setModel(final ClimateModel model) {
        this.m_model = model;
    }
    
    public ClimateModel getModel() {
        return this.m_model;
    }
    
    public int getRandomSeed() {
        return this.m_randomSeed;
    }
    
    public void setRandomSeed(final int randomSeed) {
        this.m_randomSeed = randomSeed;
        for (final DailyWeather weather : this.m_weatherPerDay) {
            weather.setRandomSeed(randomSeed + weather.getDayOfMonth() * 1000000);
        }
    }
    
    public float getRainEventProba() {
        return this.m_model.getRainEventProba();
    }
    
    public void setRainEventProba(final float rainEventProba) {
        this.m_model.setRainEventProba(rainEventProba);
    }
    
    public float getRainHeight() {
        return this.m_model.getRainHeight();
    }
    
    public void setRainHeight(final float rainHeight) {
        this.m_model.setRainHeight(rainHeight);
    }
    
    public float getTMin() {
        return this.m_model.getTMin();
    }
    
    public void setTMin(final float tMin) {
        this.m_model.setTMin(tMin);
    }
    
    public float getTMax() {
        return this.m_model.getTMax();
    }
    
    public void setTMax(final float tMax) {
        this.m_model.setTMax(tMax);
    }
    
    public float getKMin() {
        return this.m_model.getKMin();
    }
    
    public void setKMin(final float kMin) {
        this.m_model.setKMin(kMin);
    }
    
    public float getKMax() {
        return this.m_model.getKMax();
    }
    
    public void setKMax(final float kMax) {
        this.m_model.setKMax(kMax);
    }
    
    public float getWMin() {
        return this.m_model.getWMin();
    }
    
    public void setWMin(final float wMin) {
        this.m_model.setWMin(wMin);
    }
    
    public float getWMax() {
        return this.m_model.getWMax();
    }
    
    public void setWMax(final float wMax) {
        this.m_model.setWMax(wMax);
    }
    
    public DailyWeather[] getWeatherPerDay() {
        return this.m_weatherPerDay;
    }
    
    public DailyWeather getWeather(final int day) {
        return this.m_weatherPerDay[day];
    }
    
    public void createSmoothingKernel(final int size) {
        this.m_model.setRaindDistribution(size);
        this.m_rainDistributionKernel = new float[size];
        final float k = (float)(1.0 / Math.sqrt(6.283185307179586));
        final float h = size / 15.0f;
        for (int i = 0; i < size; ++i) {
            this.m_rainDistributionKernel[i] = (float)(k * Math.exp(-(i * i) / (2.0f * h * h)));
        }
    }
    
    private void smooth() {
        final float[] p = new float[this.m_weatherPerDay.length];
        if (this.m_rainDistributionKernel.length > 0) {
            for (final DailyWeather weather : this.m_weatherPerDay) {
                final int d = weather.getDayOfMonth();
                p[d] = this.m_rainDistributionKernel[0] * this.m_weatherPerDay[d].getPrecipitations();
                for (int i = 1; i < this.m_rainDistributionKernel.length; ++i) {
                    if (d + i < this.m_weatherPerDay.length) {
                        final float[] array = p;
                        final int n = d;
                        array[n] += this.m_rainDistributionKernel[i] * this.m_weatherPerDay[d + i].getPrecipitations();
                    }
                    if (d - i >= 0) {
                        final float[] array2 = p;
                        final int n2 = d;
                        array2[n2] += this.m_rainDistributionKernel[i] * this.m_weatherPerDay[d - i].getPrecipitations();
                    }
                }
            }
        }
        for (final DailyWeather weather : this.m_weatherPerDay) {
            weather.setPrecipitations(p[weather.getDayOfMonth()]);
        }
    }
    
    private void clearPrecipitations() {
        for (final DailyWeather weather : this.m_weatherPerDay) {
            weather.setPrecipitations(0.0f);
        }
    }
    
    private void rollRainEvents() {
        final ArrayList<DailyWeather> candidates = new ArrayList<DailyWeather>();
        candidates.addAll(Arrays.asList(this.m_weatherPerDay));
        for (int count = (int)(this.m_weatherPerDay.length * this.m_model.getRainEventProba()), i = 0; i < count; ++i) {
            final DailyWeather weather = candidates.remove((int)(this.m_random.nextFloat() * candidates.size()));
            weather.setPrecipitations(1.0f);
        }
    }
    
    private void normalize() {
        float s = 0.0f;
        for (final DailyWeather weather : this.m_weatherPerDay) {
            s += weather.getPrecipitations();
        }
        if (s > 0.0f) {
            final float n = this.m_model.getRainHeight() / s;
            for (final DailyWeather weather2 : this.m_weatherPerDay) {
                final float p = weather2.getPrecipitations() * n;
                if (p > 0.0f) {
                    final int k = 0;
                }
                weather2.setPrecipitations(p);
            }
        }
        for (final DailyWeather weather : this.m_weatherPerDay) {
            weather.generateRainEvents();
        }
    }
    
    public void clearTemperatures() {
        for (final DailyWeather weather : this.m_weatherPerDay) {
            weather.setTMin(0.0f);
            weather.setTMax(0.0f);
        }
    }
    
    public void computeTemperatures() {
        final float tMin = this.m_model.getTMin();
        final float tMax = this.m_model.getTMax();
        final float kMin = this.m_model.getKMin();
        final float kMax = this.m_model.getKMax();
        for (final DailyWeather weather : this.m_weatherPerDay) {
            if (weather.getDayOfMonth() == 0) {
                weather.setTMin(tMin + this.m_random.nextFloat() * (tMax - tMin));
            }
            else {
                final float pMax = this.m_weatherPerDay[weather.getDayOfMonth() - 1].getTMax();
                weather.setTMin(Math.max(tMin, pMax - (int)(kMin + this.m_random.nextFloat() * (kMax - kMin))));
            }
            weather.setTMax(Math.min(tMax, weather.getTMin() + (int)(kMin + this.m_random.nextFloat() * (kMax - kMin))));
        }
    }
    
    private void clearWinds() {
        for (final DailyWeather weather : this.m_weatherPerDay) {
            weather.setWindForce(0.0f);
        }
    }
    
    private void computeWinds() {
        final float wMin = this.m_model.getWMin();
        final float wMax = this.m_model.getWMax();
        for (final DailyWeather weather : this.m_weatherPerDay) {
            weather.setWindForce(wMin + this.m_random.nextFloat() * (wMax - wMin));
            weather.generateWinds(wMin, wMax);
        }
    }
    
    public void update() {
        this.createSmoothingKernel(this.m_model.getRaindDistribution());
        this.updatePrecipitations();
        this.updateTemperatures();
    }
    
    public void updatePrecipitations() {
        this.m_random.setSeed(this.m_randomSeed);
        this.clearPrecipitations();
        this.rollRainEvents();
        this.smooth();
        this.normalize();
        this.updateWinds();
    }
    
    public void updateTemperatures() {
        this.m_random.setSeed(this.m_randomSeed);
        this.clearTemperatures();
        this.computeTemperatures();
    }
    
    public void updateWinds() {
        this.m_random.setSeed(this.m_randomSeed);
        this.clearWinds();
        this.computeWinds();
    }
}
