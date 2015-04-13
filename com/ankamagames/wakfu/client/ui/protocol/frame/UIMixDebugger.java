package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.prefs.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.framework.sound.openAL.soundLogger.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import java.util.*;
import com.ankamagames.framework.sound.stream.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.ambience.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.container.*;
import com.ankamagames.framework.sound.openAL.*;
import com.ankamagames.framework.sound.group.defaultSound.*;
import com.ankamagames.framework.sound.group.field.*;
import com.ankamagames.framework.sound.group.music.*;
import gnu.trove.*;

public class UIMixDebugger implements MessageFrame, Runnable
{
    private static UIMixDebugger m_instance;
    private static final String SAVE_KEY = "ankamagames.wakfu.mixDebuggerDirectory";
    private static final Logger m_logger;
    private static final long DEFAULT_REFRESH_COOLDOWN = 5000L;
    private MixDebuggerDisplayer m_mixDebugger;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIMixDebugger getInstance() {
        return UIMixDebugger.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 17720: {
                WakfuGameEntity.getInstance().removeFrame(this);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void pause() {
        this.m_mixDebugger.pause();
    }
    
    public void unPause() {
        this.m_mixDebugger.unPause();
    }
    
    public void selectSaveDirectory() {
        final String currentDirectory = Preferences.userRoot().get("ankamagames.wakfu.mixDebuggerDirectory", null);
        final JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Choisissez le r\u00e9pertoire de sauvegarde des logs.");
        fc.setFileSelectionMode(1);
        fc.setFileFilter(new DirectoryOnlyFilter());
        if (currentDirectory != null) {
            fc.setSelectedFile(new File(currentDirectory));
        }
        final int returnVal = fc.showOpenDialog(null);
        if (returnVal == 0) {
            final File selectedFile = fc.getSelectedFile();
            try {
                Preferences.userRoot().put("ankamagames.wakfu.mixDebuggerDirectory", selectedFile.getCanonicalPath());
            }
            catch (IOException e) {
                UIMixDebugger.m_logger.warn((Object)("Probl\u00e8me \u00e0 la s\u00e9lection du r\u00e9pertoire : " + selectedFile.getAbsolutePath()));
            }
        }
    }
    
    public void saveLogsToFile() {
        final String currentDirectory = Preferences.userRoot().get("ankamagames.wakfu.mixDebuggerDirectory", null);
        final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
        final StringBuilder sb = new StringBuilder();
        final int month = now.getMonth();
        final int day = now.getDay();
        final int hours = now.getHours();
        final int minutes = now.getMinutes();
        final int seconds = now.getSeconds();
        if (currentDirectory != null) {
            sb.append(currentDirectory).append("\\");
        }
        else {
            sb.append(".\\");
        }
        sb.append("mixDebugger-");
        sb.append(now.getYear()).append((month < 10) ? "0" : "").append(month).append((day < 10) ? "0" : "").append(day);
        sb.append("-").append((hours < 10) ? "0" : "").append(hours).append((minutes < 10) ? "0" : "").append(minutes).append((seconds < 10) ? "0" : "").append(seconds);
        sb.append(".txt");
        final String filePath = sb.toString();
        PrintWriter w;
        try {
            w = new PrintWriter(FileHelper.createFileOutputStream(filePath));
        }
        catch (IOException ioe) {
            UIMixDebugger.m_logger.warn((Object)"Probl\u00e8me \u00e0 la sauvegarde des logs");
            return;
        }
        w.println("---=== Crit\u00e8res ===---");
        w.println();
        w.println(this.m_mixDebugger.getCriterionsDescription(false));
        w.println();
        w.println("---=== Ev\u00e8nements ===---");
        w.println();
        w.println(this.m_mixDebugger.getEventMessages(false));
        w.println("---=== Logs ===---");
        w.println();
        w.println(this.m_mixDebugger.getLogs(false));
        w.flush();
        w.close();
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("mixDebuggerDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIMixDebugger.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            this.m_mixDebugger = new MixDebuggerDisplayer();
            PropertiesProvider.getInstance().setPropertyValue("mixDebugger", this.m_mixDebugger);
            Xulor.getInstance().load("mixDebuggerDialog", Dialogs.getDialogPath("mixDebuggerDialog"), 1L, (short)10000);
            ProcessScheduler.getInstance().schedule(this, 5000L, -1);
            Xulor.getInstance().putActionClass("wakfu.mixDebugger", MixDebuggerDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            SoundLogger.unregisterAppender(MixDebuggerDisplayer.class);
            this.m_mixDebugger.cleanUp();
            this.m_mixDebugger = null;
            ProcessScheduler.getInstance().remove(this);
            PropertiesProvider.getInstance().removeProperty("mixDebugger");
            Xulor.getInstance().unload("mixDebuggerDialog");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().removeActionClass("wakfu.mixDebugger");
        }
    }
    
    @Override
    public void run() {
        this.refresh(false);
    }
    
    public void refresh(final boolean force) {
        if (force || this.m_mixDebugger.isRefreshCache()) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this.m_mixDebugger, "cache", "memoryCache", "sourcesTotalSize");
        }
    }
    
    public MixDebuggerDisplayer getMixDebugger() {
        return this.m_mixDebugger;
    }
    
    static {
        UIMixDebugger.m_instance = new UIMixDebugger();
        m_logger = Logger.getLogger((Class)UIMixDebugger.class);
    }
    
    private static class DirectoryOnlyFilter extends FileFilter
    {
        @Override
        public boolean accept(final File f) {
            return f.isDirectory();
        }
        
        @Override
        public String getDescription() {
            return "R\u00e9pertoire";
        }
    }
    
    public class MixDebuggerDisplayer extends ImmutableFieldProvider implements SoundLogAppender, SoundContainerManagerListener, SoundStrataListener
    {
        private static final int LOG_MESSAGES_MAX_SIZE = 1000;
        public static final String CACHE_FIELD = "cache";
        public static final String MEMORY_CACHE_FIELD = "memoryCache";
        public static final String CRITERIONS_FIELD = "criterions";
        public static final String EVENTS_FIELD = "events";
        public static final String REFRESH_FIELD = "refresh";
        public static final String CACHE_SOURCES_TOTAL_SIZE_FIELD = "cacheSourcesTotalSize";
        public static final String SOURCES_TOTAL_SIZE_FIELD = "sourcesTotalSize";
        public static final String STRATA_FIELD = "stratas";
        public static final String LOG_FIELD = "log";
        public final String[] FIELDS;
        private TByteObjectHashMap<SourceGroupDisplayer> m_sourceGroupDisplayers;
        private LinkedList<LogMessage> m_logMessages;
        private LinkedList<EventMessage> m_eventMessages;
        private ArrayList<CriterionMessage> m_criterionMessages;
        private LinkedList<LogMessage> m_logMessagesCopy;
        private LinkedList<EventMessage> m_eventMessagesCopy;
        private ArrayList<CriterionMessage> m_criterionMessagesCopy;
        private IntObjectLightWeightMap<SoundStrataFieldProvider> m_stratas;
        private boolean m_paused;
        private boolean m_refreshCache;
        
        public MixDebuggerDisplayer() {
            super();
            this.FIELDS = new String[] { "cache", "memoryCache", "criterions", "events", "refresh", "cacheSourcesTotalSize", "sourcesTotalSize", "log", "stratas" };
            this.m_sourceGroupDisplayers = new TByteObjectHashMap<SourceGroupDisplayer>();
            this.m_logMessages = new LinkedList<LogMessage>();
            this.m_eventMessages = new LinkedList<EventMessage>();
            this.m_criterionMessages = new ArrayList<CriterionMessage>();
            this.m_logMessagesCopy = new LinkedList<LogMessage>();
            this.m_eventMessagesCopy = new LinkedList<EventMessage>();
            this.m_criterionMessagesCopy = new ArrayList<CriterionMessage>();
            this.m_stratas = new IntObjectLightWeightMap<SoundStrataFieldProvider>();
            this.m_refreshCache = true;
            WakfuSoundManager.getInstance().addSoundContainerManagerListener(this);
            for (final GameSoundGroup g : GameSoundGroup.values()) {
                this.m_sourceGroupDisplayers.put(g.getGroupId(), new SourceGroupDisplayer(g));
            }
            SoundLogger.registerAppender(MixDebuggerDisplayer.class, this);
            final SoundContainerManager scm = WakfuSoundManager.getInstance().getSoundContainerManager();
            final ArrayList<SoundStrata> stratas = scm.getSoundStrata();
            for (int i = 0, size = stratas.size(); i < size; ++i) {
                final SoundStrata strata = stratas.get(i);
                strata.addListener(this);
                this.m_stratas.put(strata.getId(), new SoundStrataFieldProvider(strata));
            }
        }
        
        public void cleanUp() {
            WakfuSoundManager.getInstance().removeSoundContainerManagerListener(this);
            SoundLogger.unregisterAppender(MixDebuggerDisplayer.class);
            final SoundContainerManager scm = WakfuSoundManager.getInstance().getSoundContainerManager();
            final ArrayList<SoundStrata> stratas = scm.getSoundStrata();
            for (int i = 0, size = stratas.size(); i < size; ++i) {
                stratas.get(i).removeListener(this);
            }
            for (int i = 0, size = this.m_stratas.size(); i < size; ++i) {
                this.m_stratas.getQuickValue(i).clear();
            }
            this.m_stratas.clear();
        }
        
        @Override
        public String[] getFields() {
            return this.FIELDS;
        }
        
        public void pause() {
            this.m_logMessagesCopy.addAll(this.m_logMessages);
            this.m_eventMessagesCopy.addAll(this.m_eventMessages);
            this.m_criterionMessagesCopy.addAll(this.m_criterionMessages);
            this.m_paused = true;
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "criterions", "events", "log");
        }
        
        public void unPause() {
            this.m_logMessagesCopy.clear();
            this.m_eventMessagesCopy.clear();
            this.m_criterionMessagesCopy.clear();
            this.m_paused = false;
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "criterions", "events", "log");
        }
        
        public String getLogs(final boolean formatted) {
            final TextWidgetFormater sb = new TextWidgetFormater();
            final LinkedList<LogMessage> list = this.m_paused ? this.m_logMessagesCopy : this.m_logMessages;
            for (final LogMessage message : list) {
                final byte groupId = message.getGroupId();
                if (groupId != -1) {
                    final GameSoundGroup gameSoundGroup = GameSoundGroup.fromId(groupId);
                    if (gameSoundGroup == null) {
                        UIMixDebugger.m_logger.error((Object)"Game sound group inconnu !");
                        continue;
                    }
                    if (this.m_sourceGroupDisplayers.get(gameSoundGroup.getGroupId()).m_logFiltered) {
                        continue;
                    }
                    sb.append(message.getTimeStamp()).append(" ");
                    if (formatted) {
                        sb.b();
                    }
                    sb.append(gameSoundGroup.name());
                    if (formatted) {
                        sb._b();
                    }
                }
                else {
                    sb.append(message.getTimeStamp()).append(" ");
                    if (formatted) {
                        sb.b();
                    }
                    sb.append("Null");
                    if (formatted) {
                        sb._b();
                    }
                }
                String color;
                if (message.getMessage().startsWith("Adding")) {
                    color = Color.DARK_GREEN.getRGBtoHex();
                }
                else {
                    color = Color.RED.getRGBtoHex();
                }
                sb.append(" : ");
                if (formatted) {
                    sb.openText().addColor(color);
                }
                sb.append(message.getMessage());
                if (formatted) {
                    sb.closeText();
                }
                sb.newLine();
            }
            return sb.finishAndToString();
        }
        
        public String getCriterionsDescription(final boolean formatted) {
            final TextWidgetFormater sb = new TextWidgetFormater();
            final ArrayList<CriterionMessage> list = this.m_paused ? this.m_criterionMessagesCopy : this.m_criterionMessages;
            for (int i = 0, size = list.size(); i < size; ++i) {
                final CriterionMessage message = list.get(i);
                if (formatted) {
                    sb.b();
                }
                sb.append(message.getTitle()).append(" = ");
                if (formatted) {
                    sb._b();
                }
                sb.append(message.getValue()).newLine();
            }
            return sb.finishAndToString();
        }
        
        public String getEventMessages(final boolean formatted) {
            final TextWidgetFormater sb = new TextWidgetFormater();
            final LinkedList<EventMessage> list = this.m_paused ? this.m_eventMessagesCopy : this.m_eventMessages;
            for (int i = 0, size = list.size(); i < size; ++i) {
                final EventMessage message = list.get(i);
                sb.append(message.getTimeStamp()).append(" ");
                if (formatted) {
                    sb.b();
                }
                sb.append(message.getEventName()).append(" : ");
                if (formatted) {
                    sb._b();
                }
                sb.append(message.getEventParams()).newLine();
            }
            return sb.finishAndToString();
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("cache")) {
                return this.m_sourceGroupDisplayers.getValues();
            }
            if (fieldName.equals("log")) {
                return this.getLogs(true);
            }
            if (fieldName.equals("cacheSourcesTotalSize")) {
                return JOrbisSoundCache.INSTANCE.size();
            }
            if (fieldName.equals("sourcesTotalSize")) {
                int count = 0;
                final TByteObjectIterator<SourceGroupDisplayer> it = this.m_sourceGroupDisplayers.iterator();
                while (it.hasNext()) {
                    it.advance();
                    count += it.value().getSourcesSize();
                }
                return count;
            }
            if (fieldName.equals("memoryCache")) {
                final long memorySize = JOrbisSoundCache.INSTANCE.getMemorySize();
                if (memorySize < 1024L) {
                    return memorySize + " o";
                }
                if (memorySize < 1048576L) {
                    return String.format("%.2f ko", memorySize / 1024.0f);
                }
                return String.format("%.2f Mo", memorySize / 1024.0f / 1024.0f);
            }
            else {
                if (fieldName.equals("criterions")) {
                    return this.getCriterionsDescription(true);
                }
                if (fieldName.equals("refresh")) {
                    return this.m_refreshCache;
                }
                if (fieldName.equals("events")) {
                    return this.getEventMessages(true);
                }
                if (fieldName.equals("stratas")) {
                    return this.m_stratas;
                }
                return null;
            }
        }
        
        public boolean isRefreshCache() {
            return this.m_refreshCache;
        }
        
        public void setRefreshCache(final boolean refreshCache) {
            this.m_refreshCache = refreshCache;
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "refresh");
        }
        
        @Override
        public void onUpdate(final SoundStrata strata) {
            final SoundStrataFieldProvider strataFP = this.m_stratas.get(strata.getId());
            if (strataFP != null) {
                strataFP.updateContainers();
            }
        }
        
        @Override
        public void onUpdate(final long date) {
            this.m_criterionMessages.clear();
            this.m_criterionMessages.add(new CriterionMessage("Altitude", String.valueOf(ContainerCriterionParameterManager.getInstance().getAltitude())));
            this.m_criterionMessages.add(new CriterionMessage("Num players", String.valueOf(ContainerCriterionParameterManager.getInstance().getNumPlayers())));
            this.m_criterionMessages.add(new CriterionMessage("Season", String.valueOf(ContainerCriterionParameterManager.getInstance().getSeason())));
            this.m_criterionMessages.add(new CriterionMessage("Temperature", String.valueOf(ContainerCriterionParameterManager.getInstance().getTemperature() + "°w")));
            this.m_criterionMessages.add(new CriterionMessage("Time", String.valueOf(ContainerCriterionParameterManager.getInstance().getTimeOfDay() * 100.0f)));
            this.m_criterionMessages.add(new CriterionMessage("Wakfu score", String.valueOf(ContainerCriterionParameterManager.getInstance().getWakfuScore() * 100.0f)));
            this.m_criterionMessages.add(new CriterionMessage("Type de zone", AmbianceZoneType.getFromId((byte)ContainerCriterionParameterManager.getInstance().getZoneTypeId()).getEnumLabel()));
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "criterions");
        }
        
        @Override
        public void onEvent(final SoundEvent event) {
            if (this.m_eventMessages.size() == 50) {
                this.m_eventMessages.removeFirst();
            }
            this.m_eventMessages.add(new EventMessage(this.timeStamp(), event.getEventTitle(), event.getParamDescription()));
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "events");
        }
        
        @Override
        public void append(final ObjectPair<Byte, String> message) {
            if (this.m_logMessages.size() == 1000) {
                this.m_logMessages.removeFirst();
            }
            this.m_logMessages.addLast(new LogMessage(this.timeStamp(), (byte)message.getFirst(), (String)message.getSecond()));
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "log");
        }
        
        private String timeStamp() {
            final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
            final StringBuilder sb = new StringBuilder();
            final int hours = now.getHours();
            final int minutes = now.getMinutes();
            final int seconds = now.getSeconds();
            sb.append("[");
            sb.append((hours < 10) ? "0" : "").append(hours).append(":");
            sb.append((minutes < 10) ? "0" : "").append(minutes).append(":");
            sb.append((seconds < 10) ? "0" : "").append(seconds);
            sb.append("]");
            return sb.toString();
        }
    }
    
    private static class CriterionMessage
    {
        private String m_title;
        private String m_value;
        
        private CriterionMessage(final String title, final String value) {
            super();
            this.m_title = title;
            this.m_value = value;
        }
        
        public String getTitle() {
            return this.m_title;
        }
        
        public String getValue() {
            return this.m_value;
        }
    }
    
    private static class LogMessage
    {
        private String m_timeStamp;
        private byte m_groupId;
        private String m_message;
        
        private LogMessage(final String timeStamp, final byte groupId, final String message) {
            super();
            this.m_timeStamp = timeStamp;
            this.m_groupId = groupId;
            this.m_message = message;
        }
        
        public String getTimeStamp() {
            return this.m_timeStamp;
        }
        
        public byte getGroupId() {
            return this.m_groupId;
        }
        
        public String getMessage() {
            return this.m_message;
        }
    }
    
    private static class EventMessage
    {
        private String m_timeStamp;
        private String m_eventName;
        private String m_eventParams;
        
        private EventMessage(final String timeStamp, final String eventName, final String eventParams) {
            super();
            this.m_timeStamp = timeStamp;
            this.m_eventName = eventName;
            this.m_eventParams = eventParams;
        }
        
        public String getTimeStamp() {
            return this.m_timeStamp;
        }
        
        public String getEventName() {
            return this.m_eventName;
        }
        
        public String getEventParams() {
            return this.m_eventParams;
        }
    }
    
    private static class SoundContainerFieldProvider extends ImmutableFieldProvider implements SoundContainerListener
    {
        public static final String NAME = "name";
        public static final String LAST_ADDED = "lastAdded";
        private final SoundContainer m_container;
        private String m_lastAdded;
        
        private SoundContainerFieldProvider(final SoundContainer container) {
            super();
            (this.m_container = container).setListener(this);
        }
        
        public void clear() {
            this.m_container.setListener(null);
        }
        
        @Override
        public void onSourceCreation(final AudioSource source) {
            this.m_lastAdded = source.getFileId();
        }
        
        @Override
        public String[] getFields() {
            return null;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("name")) {
                return this.m_container.getName();
            }
            if (fieldName.equals("lastAdded")) {
                return this.m_lastAdded;
            }
            return null;
        }
    }
    
    private class SoundStrataFieldProvider extends ImmutableFieldProvider
    {
        public static final String NAME = "name";
        public static final String CONTAINERS = "containers";
        private final SoundStrata m_strata;
        private IntObjectLightWeightMap<SoundContainerFieldProvider> m_containers;
        
        private SoundStrataFieldProvider(final SoundStrata strata) {
            super();
            this.m_containers = new IntObjectLightWeightMap<SoundContainerFieldProvider>();
            this.m_strata = strata;
            this.updateContainers();
        }
        
        public void clear() {
            if (this.m_containers != null) {
                for (int i = 0, size = this.m_containers.size(); i < size; ++i) {
                    this.m_containers.getQuickValue(i).clear();
                }
            }
        }
        
        @Override
        public String[] getFields() {
            return null;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("name")) {
                return this.m_strata.getName();
            }
            if (fieldName.equals("containers")) {
                return this.m_containers;
            }
            return null;
        }
        
        public void updateContainers() {
            final IntObjectLightWeightMap<SoundContainerFieldProvider> toRemoveMap = new IntObjectLightWeightMap<SoundContainerFieldProvider>();
            for (int i = 0, size = this.m_containers.size(); i < size; ++i) {
                toRemoveMap.put(this.m_containers.getQuickKey(i), this.m_containers.getQuickValue(i));
            }
            final ArrayList<SoundContainer> containers = this.m_strata.getCurrentSources();
            for (int j = 0, size2 = containers.size(); j < size2; ++j) {
                final SoundContainer soundContainer = containers.get(j);
                final int id = soundContainer.getId();
                if (this.m_containers.contains(id)) {
                    toRemoveMap.remove(id);
                }
                else {
                    this.m_containers.put(id, new SoundContainerFieldProvider(soundContainer));
                }
            }
            for (int j = 0, size2 = toRemoveMap.size(); j < size2; ++j) {
                final SoundContainerFieldProvider soundContainerFieldProvider = this.m_containers.remove(toRemoveMap.getQuickKey(j));
                soundContainerFieldProvider.clear();
            }
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "containers");
        }
    }
    
    public static class SourceGroupDisplayer extends ImmutableFieldProvider
    {
        public static final String NAME_FIELD = "name";
        public static final String SOURCES_DESCRIPTION_FIELD = "sourcesDescription";
        public static final String LOG_FILTERED_FIELD = "logFiltered";
        public static final String VISIBLE_FIELD = "visible";
        public final String[] FIELDS;
        private GameSoundGroup m_gameSoundGroup;
        private boolean m_visible;
        private boolean m_logFiltered;
        
        public SourceGroupDisplayer(final GameSoundGroup gameSoundGroup) {
            super();
            this.FIELDS = new String[] { "name", "sourcesDescription" };
            this.m_gameSoundGroup = gameSoundGroup;
            this.m_visible = (this.getSourcesSize() > 0);
            this.m_logFiltered = false;
        }
        
        @Override
        public String[] getFields() {
            return this.FIELDS;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("name")) {
                return this.m_gameSoundGroup.name() + "(" + this.getSourcesSize() + ")";
            }
            if (fieldName.equals("logFiltered")) {
                return this.m_logFiltered;
            }
            if (fieldName.equals("visible")) {
                return this.m_visible;
            }
            if (fieldName.equals("sourcesDescription")) {
                final TextWidgetFormater textWidgetFormater = new TextWidgetFormater();
                final DefaultSourceGroup defaultSourceGroup = this.m_gameSoundGroup.getDefaultGroup();
                if (defaultSourceGroup != null) {
                    textWidgetFormater.b().append("-Default- size=").append(defaultSourceGroup.getSources().size())._b().append("\n");
                    for (final AudioSource audioSource : defaultSourceGroup.getSources()) {
                        textWidgetFormater.append(audioSource.getDescription()).append("\n");
                    }
                }
                final FieldSourceGroup fieldSourceGroup = this.m_gameSoundGroup.getFieldSourceGroup();
                if (fieldSourceGroup != null) {
                    textWidgetFormater.b().append("-Field- size=").append(fieldSourceGroup.getSourcesSize())._b().append("\n");
                    textWidgetFormater.append(fieldSourceGroup.getSourcesDescription()).append("\n");
                }
                final MusicGroup musicGroup = this.m_gameSoundGroup.getMusicGroup();
                if (musicGroup != null) {
                    final AudioSource currentMusic = musicGroup.getMainMusic();
                    textWidgetFormater.b().append("-Music-")._b().append("\n");
                    if (currentMusic != null) {
                        textWidgetFormater.append(currentMusic.getDescription()).append("\n");
                    }
                }
                return textWidgetFormater.finishAndToString();
            }
            return null;
        }
        
        public void setLogFiltered(final boolean logFiltered) {
            this.m_logFiltered = logFiltered;
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "logFiltered");
        }
        
        public byte getGroupId() {
            return this.m_gameSoundGroup.getGroupId();
        }
        
        public void setVisible(final boolean visible) {
            this.m_visible = visible;
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "visible");
        }
        
        public int getSourcesSize() {
            int count = 0;
            if (this.m_gameSoundGroup.getDefaultGroup() != null) {
                count += this.m_gameSoundGroup.getDefaultGroup().getSources().size();
            }
            if (this.m_gameSoundGroup.getFieldSourceGroup() != null) {
                count += this.m_gameSoundGroup.getFieldSourceGroup().getSourcesSize();
            }
            return count;
        }
    }
}
