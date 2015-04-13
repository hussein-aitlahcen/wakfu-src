package com.ankamagames.wakfu.client.core.game.weather;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import java.util.*;
import com.ankamagames.xulor2.component.mesh.*;
import com.ankamagames.wakfu.client.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.characteristics.skill.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class WeatherInfoManager extends ImmutableFieldProvider
{
    public static final String WEATHER_OF_DAYS_LIST = "weatherOfDays";
    public static final String WEATHER_TODAY_LIST = "weatherToday";
    public static final String SEASONS_LIST = "seasons";
    public static final String CURRENT_WEATHER = "currentWeather";
    public static final String TEMPERATURE_GRAPH = "temperatureGraph";
    public static final String MIN_TEMPERATURE_GRAPH = "minTemperatureGraph";
    public static final String MAX_TEMPERATURE_GRAPH = "maxTemperatureGraph";
    public static final String RESOURCES = "resources";
    public static final String DISPLAY_CULTURES = "displayCultures";
    public static final String DISPLAY_PLANTS = "displayPlants";
    public static final String DISPLAY_TREES = "displayTrees";
    private static final Color TEMPERATURE_LINE_COLOR_ABOVE_ZERO;
    private static final Color TEMPERATURE_LINE_COLOR_BELOW_ZERO;
    private static final Color TEMPERATURE_ZONE_COLOR_ABOVE_ZERO;
    private static final Color TEMPERATURE_ZONE_COLOR_BELOW_ZERO;
    private static final Color TEMPERATURE_ZONE_COLOR;
    private static final Color[] SEASON_TEMPERATURE_LINE_COLOR;
    private static final Color[] TEMPERATURE_LINE_COLORS;
    private static final Color[] TEMPERATURE_ZONE_COLORS;
    private ArrayList<WeatherInfoFieldProvider> m_seasonMinMaxTemperatures;
    private WeatherInfoFieldProvider m_currentWeather;
    private ArrayList<WeatherInfoFieldProvider> m_list;
    private Graph.GraphData m_temperatureGraphData;
    private float m_graphMinTemperature;
    private float m_graphMaxTemperature;
    private static final WeatherInfoManager m_instance;
    
    private WeatherInfoManager() {
        super();
        this.m_seasonMinMaxTemperatures = new ArrayList<WeatherInfoFieldProvider>();
        this.m_currentWeather = new WeatherInfoFieldProvider();
        this.m_list = new ArrayList<WeatherInfoFieldProvider>();
        this.m_temperatureGraphData = null;
        this.m_list.add(new WeatherInfoFieldProvider());
    }
    
    public static WeatherInfoManager getInstance() {
        return WeatherInfoManager.m_instance;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("currentWeather")) {
            return this.m_currentWeather;
        }
        if (fieldName.equals("seasons")) {
            return this.m_seasonMinMaxTemperatures;
        }
        if (fieldName.equals("weatherOfDays")) {
            return this.m_list;
        }
        if (fieldName.equals("weatherToday")) {
            if (this.m_list == null || this.m_list.size() == 0) {
                return null;
            }
            return this.m_list.get(0);
        }
        else {
            if (fieldName.equals("minTemperatureGraph")) {
                return WakfuTranslator.getInstance().getString("weather.info.temperatureValue.short", this.m_graphMinTemperature);
            }
            if (fieldName.equals("maxTemperatureGraph")) {
                return WakfuTranslator.getInstance().getString("weather.info.temperatureValue.short", this.m_graphMaxTemperature);
            }
            if (fieldName.equals("temperatureGraph")) {
                return this.m_temperatureGraphData;
            }
            if (fieldName.equals("resources")) {
                return this.getResourceList();
            }
            if (fieldName.equals("displayPlants")) {
                return ResourceListFiller.INSTANCE.isDisplayPlants();
            }
            if (fieldName.equals("displayTrees")) {
                return ResourceListFiller.INSTANCE.isDisplayTrees();
            }
            if (fieldName.equals("displayCultures")) {
                return ResourceListFiller.INSTANCE.isDisplayCultures();
            }
            return null;
        }
    }
    
    public boolean isDisplayCultures() {
        return ResourceListFiller.INSTANCE.isDisplayCultures();
    }
    
    public void setDisplayCultures(final boolean displayCultures) {
        ResourceListFiller.INSTANCE.setDisplayCultures(displayCultures);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "displayCultures", "resources");
    }
    
    public boolean isDisplayPlants() {
        return ResourceListFiller.INSTANCE.isDisplayPlants();
    }
    
    public void setDisplayPlants(final boolean displayPlants) {
        ResourceListFiller.INSTANCE.setDisplayPlants(displayPlants);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "displayPlants", "resources");
    }
    
    public boolean isDisplayTrees() {
        return ResourceListFiller.INSTANCE.isDisplayTrees();
    }
    
    public void setDisplayTrees(final boolean displayTrees) {
        ResourceListFiller.INSTANCE.setDisplayTrees(displayTrees);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "displayTrees", "resources");
    }
    
    public void resetResourceFilters() {
        ResourceListFiller.INSTANCE.setDisplayCultures(true);
        ResourceListFiller.INSTANCE.setDisplayPlants(true);
        ResourceListFiller.INSTANCE.setDisplayTrees(true);
    }
    
    public void updateCurrentWeather(final WeatherInfo info) {
        this.m_currentWeather.setWeatherInfo(info);
    }
    
    public WeatherInfoFieldProvider getCurrentWeather() {
        return this.m_currentWeather;
    }
    
    public void clear() {
        this.m_list.clear();
        this.m_seasonMinMaxTemperatures.clear();
    }
    
    private ArrayList<ResourceFieldProvider> getResourceList() {
        final ArrayList<ResourceFieldProvider> resources = new ArrayList<ResourceFieldProvider>();
        ResourceListFiller.INSTANCE.fillList(resources);
        Collections.sort(resources, ResourceLevelComparator.INSTANCE);
        Collections.sort(resources, ResourceTypeComparator.INSTANCE);
        return resources;
    }
    
    public void addWeather(final Season season, final int minTemp, final int maxTemp, final int modTemp, final float[] seasonMinMaxTemperatures, final float avgRain, final float modRain, final float avgWind, final float modWind) {
        final WeatherInfoFieldProvider wi = new WeatherInfoFieldProvider();
        final WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setWeather(minTemp, minTemp, maxTemp, modTemp, seasonMinMaxTemperatures, avgRain, 0, modRain, avgWind, modWind);
        wi.setSeason(season);
        wi.setWeatherInfo(weatherInfo);
        final int maxDaysInWeek = Calendar.getInstance().getActualMaximum(7);
        final int dayOfWeek = Calendar.getInstance().get(7) - 1;
        wi.setDayInWeek((dayOfWeek + this.m_list.size()) % maxDaysInWeek);
        this.m_list.add(wi);
    }
    
    private void addSeason(final Season season, final int minTemp, final int maxTemp, final int modTemp, final float[] seasonMinMaxTemperatures, final float avgRain, final float modRain, final float avgWind, final float modWind) {
        final WeatherInfoFieldProvider wi = new WeatherInfoFieldProvider();
        final WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setWeather(minTemp, minTemp, maxTemp, modTemp, seasonMinMaxTemperatures, avgRain, 0, modRain, avgWind, modWind);
        wi.setSeason(season);
        wi.setWeatherInfo(weatherInfo);
        this.m_seasonMinMaxTemperatures.add(wi);
    }
    
    public void createGraphData(final WeatherHistory weatherHistory) {
        this.m_temperatureGraphData = new Graph.GraphData();
        this.m_graphMinTemperature = Float.MAX_VALUE;
        this.m_graphMaxTemperature = Float.MIN_VALUE;
        final int[] seasonTempColorIndices = new int[weatherHistory.getHistoryLength()];
        final int[] minTempColorIndices = new int[weatherHistory.getHistoryLength()];
        final int[] maxTempColorIndices = new int[weatherHistory.getHistoryLength()];
        final int[] zoneTempColorIndices = new int[weatherHistory.getHistoryLength() * 2];
        final float[] minTempCurve = new float[weatherHistory.getHistoryLength()];
        final float[] maxTempCurve = new float[weatherHistory.getHistoryLength()];
        final float[] seasonMinTempCurve = new float[weatherHistory.getHistoryLength()];
        final float[] seasonMaxTempCurve = new float[weatherHistory.getHistoryLength()];
        for (int i = 0; i < weatherHistory.getHistoryLength(); ++i) {
            final Season season = weatherHistory.getSeasons(i);
            final GraphMesh.GraphElement elem = new GraphMesh.GraphElement();
            switch (season) {
                case SPRING: {
                    elem.setModulationColor(WakfuClientConstants.SPRING_COLOR);
                    break;
                }
                case SUMMER: {
                    elem.setModulationColor(WakfuClientConstants.SUMMER_COLOR);
                    break;
                }
                case FALL: {
                    elem.setModulationColor(WakfuClientConstants.FALL_COLOR);
                    break;
                }
                case WINTER: {
                    elem.setModulationColor(WakfuClientConstants.WINTER_COLOR);
                    break;
                }
            }
            this.m_temperatureGraphData.addGraphElement(elem);
            minTempCurve[i] = weatherHistory.getMinTemperatures(i) + weatherHistory.getModTemperatures(i);
            maxTempCurve[i] = weatherHistory.getMaxTemperatures(i) + weatherHistory.getModTemperatures(i);
            if (minTempCurve[i] < 0.0f) {
                zoneTempColorIndices[i * 2] = (minTempColorIndices[i] = 1);
            }
            if (maxTempCurve[i] < 0.0f) {
                zoneTempColorIndices[i * 2 + 1] = (maxTempColorIndices[i] = 1);
            }
            seasonMinTempCurve[i] = weatherHistory.getSeasonMinMaxTemperatures(season)[0];
            seasonMaxTempCurve[i] = weatherHistory.getSeasonMinMaxTemperatures(season)[1];
            this.m_graphMinTemperature = Math.min(this.m_graphMinTemperature, seasonMinTempCurve[i]);
            this.m_graphMaxTemperature = Math.max(this.m_graphMaxTemperature, seasonMaxTempCurve[i]);
        }
        final GraphMesh.GraphLine minT = new GraphMesh.GraphLine();
        minT.setValues(minTempCurve);
        minT.setColors(WeatherInfoManager.TEMPERATURE_LINE_COLORS, minTempColorIndices);
        final GraphMesh.GraphLine maxT = new GraphMesh.GraphLine();
        maxT.setValues(maxTempCurve);
        maxT.setColors(WeatherInfoManager.TEMPERATURE_LINE_COLORS, maxTempColorIndices);
        final GraphMesh.GraphLine seasonMinT = new GraphMesh.GraphLine();
        seasonMinT.setValues(seasonMinTempCurve);
        seasonMinT.setColors(WeatherInfoManager.SEASON_TEMPERATURE_LINE_COLOR, seasonTempColorIndices);
        final GraphMesh.GraphLine seasonMaxT = new GraphMesh.GraphLine();
        seasonMaxT.setValues(seasonMaxTempCurve);
        seasonMaxT.setColors(WeatherInfoManager.SEASON_TEMPERATURE_LINE_COLOR, seasonTempColorIndices);
        this.m_temperatureGraphData.addGraphLine(minT);
        this.m_temperatureGraphData.addGraphLine(maxT);
        this.m_temperatureGraphData.addGraphLine(seasonMinT);
        this.m_temperatureGraphData.addGraphLine(seasonMaxT);
        final GraphMesh.GraphZone tZone = new GraphMesh.GraphZone();
        tZone.addGraphLine(minT);
        tZone.addGraphLine(maxT);
        tZone.setColors(WeatherInfoManager.TEMPERATURE_ZONE_COLORS, zoneTempColorIndices);
        this.m_temperatureGraphData.addGraphZone(tZone);
        this.m_temperatureGraphData.setMinValue(this.m_graphMinTemperature - 2.0f);
        this.m_temperatureGraphData.setMaxValue(this.m_graphMaxTemperature + 2.0f);
    }
    
    public void updateView() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentWeather", "weatherOfDays");
    }
    
    public void updateFromWeatherHistory(final WeatherHistory weatherHistory) {
        this.clear();
        Season lastSeason = null;
        for (int i = 0; i < weatherHistory.getHistoryLength(); ++i) {
            final Season season = weatherHistory.getSeasons(i);
            if (season != lastSeason) {
                this.addSeason(season, (int)weatherHistory.getSeasonMinMaxTemperatures(season)[0], (int)weatherHistory.getSeasonMinMaxTemperatures(season)[1], 0, null, 0.0f, 0.0f, 0.0f, 0.0f);
                lastSeason = season;
            }
        }
        for (int i = 0; i < weatherHistory.getHistoryLength(); ++i) {
            this.addWeather(weatherHistory.getSeasons(i), weatherHistory.getMinTemperatures(i), weatherHistory.getMaxTemperatures(i), weatherHistory.getModTemperatures(i), weatherHistory.getSeasonMinMaxTemperatures(weatherHistory.getSeasons(i)), weatherHistory.getAvgPrecipitations(i), weatherHistory.getModPrecipitations(i), weatherHistory.getAvgWind(i), weatherHistory.getModWind(i));
        }
        this.createGraphData(weatherHistory);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentWeather", "weatherOfDays", "seasons", "temperatureGraph", "minTemperatureGraph", "maxTemperatureGraph");
    }
    
    static {
        TEMPERATURE_LINE_COLOR_ABOVE_ZERO = new Color(1.0f, 0.8f, 0.0f, 1.0f);
        TEMPERATURE_LINE_COLOR_BELOW_ZERO = new Color(0.0f, 0.8f, 1.0f, 1.0f);
        TEMPERATURE_ZONE_COLOR_ABOVE_ZERO = new Color(1.0f, 0.8f, 0.0f, 0.3f);
        TEMPERATURE_ZONE_COLOR_BELOW_ZERO = new Color(0.0f, 0.8f, 1.0f, 0.3f);
        TEMPERATURE_ZONE_COLOR = new Color(0.0f, 0.8f, 1.0f, 0.3f);
        SEASON_TEMPERATURE_LINE_COLOR = new Color[] { Color.BLACK };
        TEMPERATURE_LINE_COLORS = new Color[] { WeatherInfoManager.TEMPERATURE_LINE_COLOR_ABOVE_ZERO, WeatherInfoManager.TEMPERATURE_LINE_COLOR_BELOW_ZERO };
        TEMPERATURE_ZONE_COLORS = new Color[] { WeatherInfoManager.TEMPERATURE_ZONE_COLOR_ABOVE_ZERO, WeatherInfoManager.TEMPERATURE_ZONE_COLOR_BELOW_ZERO };
        m_instance = new WeatherInfoManager();
    }
    
    private static class ResourceListFiller implements TObjectProcedure<ReferenceResource>
    {
        public static final ResourceListFiller INSTANCE;
        private ArrayList<ResourceFieldProvider> m_resources;
        private boolean m_displayCultures;
        private boolean m_displayPlants;
        private boolean m_displayTrees;
        
        private ResourceListFiller() {
            super();
            this.m_displayCultures = true;
            this.m_displayPlants = true;
            this.m_displayTrees = true;
        }
        
        public void fillList(final ArrayList<ResourceFieldProvider> list) {
            this.m_resources = list;
            ReferenceResourceManager.getInstance().forEachResource(this);
            this.m_resources = null;
        }
        
        @Override
        public boolean execute(final ReferenceResource resource) {
            final ResourceType type = ResourceType.getByAgtIdOrHWCategory(resource.getResourceType());
            if (type == null) {
                return true;
            }
            switch (type) {
                case CULTIVATION: {
                    if (!this.m_displayCultures) {
                        return true;
                    }
                    break;
                }
                case PLANT: {
                    if (!this.m_displayPlants) {
                        return true;
                    }
                    break;
                }
                case TREE: {
                    if (!this.m_displayTrees) {
                        return true;
                    }
                    break;
                }
                default: {
                    return true;
                }
            }
            int level = Integer.MAX_VALUE;
            final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
            for (int i = 0, stepCount = resource.getEvolutionStepCount(); i < stepCount; ++i) {
                final ResourceEvolutionStep step = resource.getQuickEvolutionStep(i);
                for (int j = 0, collectCount = step.getCollectsCount(); j < collectCount; ++j) {
                    final CollectAction collect = step.getQuickCollect(j);
                    final int levelMin = collect.getLevelMin();
                    if (player.getCraftHandler().contains(collect.getCraftId()) && levelMin < level && levelMin <= player.getCraftHandler().getLevel(collect.getCraftId())) {
                        level = levelMin;
                    }
                }
            }
            if (level != Integer.MAX_VALUE) {
                this.m_resources.add(new ResourceFieldProvider(resource, level));
            }
            return true;
        }
        
        public boolean isDisplayCultures() {
            return this.m_displayCultures;
        }
        
        public void setDisplayCultures(final boolean displayCultures) {
            this.m_displayCultures = displayCultures;
        }
        
        public boolean isDisplayPlants() {
            return this.m_displayPlants;
        }
        
        public void setDisplayPlants(final boolean displayPlants) {
            this.m_displayPlants = displayPlants;
        }
        
        public boolean isDisplayTrees() {
            return this.m_displayTrees;
        }
        
        public void setDisplayTrees(final boolean displayTrees) {
            this.m_displayTrees = displayTrees;
        }
        
        static {
            INSTANCE = new ResourceListFiller();
        }
    }
    
    public static class ResourceLevelComparator implements Comparator<ResourceFieldProvider>
    {
        private static ResourceLevelComparator INSTANCE;
        
        @Override
        public int compare(final ResourceFieldProvider o1, final ResourceFieldProvider o2) {
            return o1.getLevel() - o2.getLevel();
        }
        
        static {
            ResourceLevelComparator.INSTANCE = new ResourceLevelComparator();
        }
    }
    
    public static class ResourceTypeComparator implements Comparator<ResourceFieldProvider>
    {
        private static ResourceTypeComparator INSTANCE;
        
        @Override
        public int compare(final ResourceFieldProvider o1, final ResourceFieldProvider o2) {
            return o1.getResource().getResourceType() - o2.getResource().getResourceType();
        }
        
        static {
            ResourceTypeComparator.INSTANCE = new ResourceTypeComparator();
        }
    }
}
