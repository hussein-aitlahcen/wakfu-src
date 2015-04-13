package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.antiAddiction.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.fight.time.buff.*;
import com.ankamagames.framework.fileFormat.properties.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.effect.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class RunningEffectFieldProvider extends ImmutableFieldProvider implements Releasable, Comparable<RunningEffectFieldProvider>
{
    private static Logger m_logger;
    private final ArrayList<RunningEffect> m_runningEffects;
    private long m_remainingSec;
    private RelativeFightTime m_latestEndTime;
    private ArrayList<EffectContainer> m_containers;
    private ObjectPool m_pool;
    public static final String NAME_FIELD = "name";
    public static final String NAME_AND_LEVEL_FIELD = "nameAndLevel";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String TABLE_TURN_DURATION_FIELD = "tableTurnDuration";
    public static final String PLAIN_REMAINING_DURATION_FIELD = "plainRemainingDuration";
    public static final String REMAINING_DURATION_FIELD = "remainingDuration";
    public static final String REMAINING_DURATION_TEXT_FIELD = "remainingDurationText";
    public static final String IS_INFINITE_DURATION_FIELD = "isInfiniteDuration";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String EFFECTS_FIELD = "effects";
    public static final String STACK_SIZE = "stackSize";
    public static final String[] FIELDS;
    private static final ObjectPool m_staticPool;
    
    public static RunningEffectFieldProvider checkOut(final EffectContainer container) {
        RunningEffectFieldProvider refp;
        try {
            refp = (RunningEffectFieldProvider)RunningEffectFieldProvider.m_staticPool.borrowObject();
            refp.m_pool = RunningEffectFieldProvider.m_staticPool;
        }
        catch (Exception e) {
            refp = new RunningEffectFieldProvider();
            refp.m_pool = null;
            RunningEffectFieldProvider.m_logger.error((Object)("Erreur lors d'un checkOut sur un RunningEffectFieldProvider : " + e.getMessage()));
        }
        refp.m_containers.add(container);
        refp.m_remainingSec = -1L;
        return refp;
    }
    
    private RunningEffectFieldProvider() {
        super();
        this.m_runningEffects = new ArrayList<RunningEffect>();
        this.m_containers = new ArrayList<EffectContainer>();
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equalsIgnoreCase("nameAndLevel")) {
            final String name = this.getName();
            final int stackSize = this.getStackSize();
            if (stackSize < 2) {
                return name;
            }
            return name + " (" + WakfuTranslator.getInstance().getString("levelShort.custom", stackSize) + ")";
        }
        else {
            if (fieldName.equalsIgnoreCase("name")) {
                return this.getName();
            }
            if (fieldName.equalsIgnoreCase("description")) {
                return this.getDescription();
            }
            if (fieldName.equalsIgnoreCase("plainRemainingDuration")) {
                return this.getRemainingDuration(false, true);
            }
            if (fieldName.equalsIgnoreCase("remainingDuration")) {
                return this.getRemainingDuration(true, true);
            }
            if (fieldName.equalsIgnoreCase("remainingDurationText")) {
                return this.getRemainingDuration(true, false);
            }
            if (fieldName.equals("isInfiniteDuration")) {
                final Fight fight = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight();
                if (fight != null && this.m_latestEndTime != null) {
                    return this.m_latestEndTime.isInfinite();
                }
                return this.m_remainingSec == -1L;
            }
            else {
                if (fieldName.equalsIgnoreCase("iconUrl")) {
                    return this.getIconUrl();
                }
                if (fieldName.equalsIgnoreCase("stackSize")) {
                    return this.getStackSize();
                }
                if (fieldName.equalsIgnoreCase("effects")) {
                    final String effects = this.getEffects();
                    return effects.isEmpty() ? null : effects;
                }
                return null;
            }
        }
    }
    
    private String getDescription() {
        final EffectContainer container = this.getCommonEffectContainer();
        switch (container.getContainerType()) {
            case 1: {
                final StateClient stateClient = (StateClient)container;
                final String description = stateClient.getDescription();
                if (description != null && !description.isEmpty()) {
                    return description;
                }
                break;
            }
            case 34: {
                if (this.m_runningEffects.isEmpty()) {
                    return null;
                }
                final RunningEffect firstEffect = this.m_runningEffects.get(0);
                final AntiAddictionLevel level = AntiAddictionLevel.getForEffectId(firstEffect.getGenericEffect().getEffectId());
                return WakfuTranslator.getInstance().getString("antiAddictionLevel.desc." + level.getId());
            }
        }
        return null;
    }
    
    private String getRemainingDuration(final boolean colored, final boolean shortDescription) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Fight fight = localPlayer.getCurrentFight();
        if (fight != null && this.m_latestEndTime != null) {
            if (this.m_latestEndTime.isInfinite()) {
                return null;
            }
            final short remainingTurns = fight.getTimeline().howLongInTurnsUntil(this.m_latestEndTime);
            if (remainingTurns == 0) {
                final String desc = shortDescription ? WakfuTranslator.getInstance().getString("remaining.duration.turn.short", "&lt;1") : WakfuTranslator.getInstance().getString("duration.last.turn");
                return colored ? new TextWidgetFormater().openText().addColor(Color.RED.getRGBtoHex()).append(desc).finishAndToString() : desc;
            }
            final String desc = shortDescription ? WakfuTranslator.getInstance().getString("remaining.duration.turn.short", remainingTurns) : WakfuTranslator.getInstance().getString("remaining.turns", remainingTurns);
            if (remainingTurns <= 2 && colored) {
                return new TextWidgetFormater().openText().addColor(Color.RED.getRGBtoHex()).append(desc).closeText().finishAndToString();
            }
            return desc;
        }
        else {
            long remainingSec = this.m_remainingSec;
            final EffectContainer commonEffectContainer = this.getCommonEffectContainer();
            if (commonEffectContainer != null && commonEffectContainer.getContainerType() == 34) {
                final AntiAddictionDataHandler handler = localPlayer.getAntiAddictionDataHandler();
                final GameInterval onlineDuration = handler.getLastConnectionDate().timeTo(WakfuGameCalendar.getInstance().getDate());
                onlineDuration.add(handler.getCurrentUsedQuota());
                remainingSec = (int)onlineDuration.toSeconds();
            }
            if (remainingSec != -1L) {
                final GameInterval interval = GameInterval.fromSeconds(remainingSec);
                String desc2 = shortDescription ? TimeUtils.getVeryShortDescription(interval) : TimeUtils.getShortDescription(interval);
                if (colored && remainingSec <= 60L) {
                    desc2 = new TextWidgetFormater().openText().addColor(Color.RED.getRGBtoHex()).append(desc2).closeText().finishAndToString();
                }
                return desc2;
            }
            return null;
        }
    }
    
    private String getEffects() {
        final ArrayList<String> stackedEffects = RunningEffectHelper.getStackedEffects(this.m_runningEffects, this.getCommonEffectContainer());
        final TextWidgetFormater sb = new TextWidgetFormater();
        for (int i = 0, size = stackedEffects.size(); i < size; ++i) {
            if (i != 0) {
                sb.newLine();
            }
            sb.append(stackedEffects.get(i));
        }
        return sb.finishAndToString();
    }
    
    private int getStackSize() {
        final EffectContainer commonEffectContainer = this.getCommonEffectContainer();
        if (commonEffectContainer instanceof State) {
            final State state = (State)commonEffectContainer;
            return state.getLevel();
        }
        if (commonEffectContainer instanceof AbstractEffectArea) {
            final AbstractEffectArea abstractEffectArea = (AbstractEffectArea)commonEffectContainer;
            return abstractEffectArea.getLevel();
        }
        if (commonEffectContainer != null) {
            final int type = commonEffectContainer.getContainerType();
            if (type == 28 || type == 32) {
                return 0;
            }
        }
        if (this.m_containers.size() == 1) {
            int lastId = -1;
            boolean valid = true;
            for (int i = 0, size = this.m_runningEffects.size(); i < size; ++i) {
                final RunningEffect runningEffect = this.m_runningEffects.get(i);
                if (runningEffect.mustBeTriggered() && runningEffect.getGenericEffect() != null) {
                    valid = false;
                    break;
                }
                if (lastId != -1 && lastId != runningEffect.getId()) {
                    valid = false;
                    break;
                }
                lastId = runningEffect.getId();
            }
            if (valid) {
                return this.m_runningEffects.size();
            }
        }
        return 0;
    }
    
    public String getIconUrl() {
        final EffectContainer commonContainer = this.getCommonEffectContainer();
        try {
            if (commonContainer != null) {
                switch (commonContainer.getContainerType()) {
                    case 12: {
                        ReferenceItem referenceItem;
                        if (!(commonContainer instanceof Item)) {
                            final int itemReferenceId = (int)commonContainer.getEffectContainerId();
                            referenceItem = ReferenceItemManager.getInstance().getReferenceItem(itemReferenceId);
                        }
                        else {
                            referenceItem = (ReferenceItem)((Item)commonContainer).getReferenceItem();
                        }
                        return (String)referenceItem.getFieldValue("iconUrl");
                    }
                    case 11:
                    case 25: {
                        return (String)((SpellLevel)commonContainer).getFieldValue("smallIconUrl");
                    }
                    case 1: {
                        final StateClient state = (StateClient)commonContainer;
                        return state.getIconUrl();
                    }
                    case 3: {
                        final BasicEffectArea area = (BasicEffectArea)commonContainer;
                        return String.format(WakfuConfiguration.getInstance().getString("effectAreasIconsPath"), area.getBaseId());
                    }
                    case 19:
                    case 33: {
                        return WakfuConfiguration.getInstance().getIconUrl("protectorBuffsIconsPath", "defaultIconPath", -1);
                    }
                    case 32: {
                        return WakfuConfiguration.getInstance().getIconUrl("protectorBuffsIconsPath", "defaultIconPath", -2);
                    }
                    case 28: {
                        return WakfuConfiguration.getInstance().getIconUrl("protectorBuffsIconsPath", "defaultIconPath", -2);
                    }
                    case 21: {
                        return WakfuConfiguration.getInstance().getIconUrl("timePointBonusIconsPath", "defaultIconPath", TimelineBuffListManager.INSTANCE.getGfx((int)commonContainer.getEffectContainerId()));
                    }
                    case 34: {
                        if (this.m_runningEffects.isEmpty()) {
                            return null;
                        }
                        final RunningEffect firstEffect = this.m_runningEffects.get(0);
                        final AntiAddictionLevel level = AntiAddictionLevel.getForEffectId(firstEffect.getGenericEffect().getEffectId());
                        return WakfuConfiguration.getInstance().getIconUrl("antiAddictionIconsPath", "defaultIconPath", level.getId());
                    }
                }
            }
        }
        catch (PropertyException e) {
            RunningEffectFieldProvider.m_logger.error((Object)"PropertyException pendant l'acc\u00e8s \u00e0 l'url de l'icone du RunningEffect");
            return null;
        }
        return null;
    }
    
    @Override
    public String[] getFields() {
        return RunningEffectFieldProvider.FIELDS;
    }
    
    public void addEffectContainer(final EffectContainer ec) {
        if (!this.m_containers.contains(ec)) {
            this.m_containers.add(ec);
        }
    }
    
    public void removeEffectContainer(final EffectContainer ec) {
        this.m_containers.remove(ec);
    }
    
    public boolean hasEffectContainers() {
        return !this.m_containers.isEmpty();
    }
    
    @Nullable
    public EffectContainer getCommonEffectContainer() {
        if (!this.m_containers.isEmpty()) {
            return this.m_containers.get(0);
        }
        return null;
    }
    
    private void updateEnd(final RunningEffect re) {
        final EffectContainer commonEffectContainer = this.getCommonEffectContainer();
        if (commonEffectContainer == null || commonEffectContainer.getContainerType() == 1 || !re.isInfinite()) {
            final RelativeFightTime endTime = re.getEndTime();
            if (endTime != null && endTime.compareTo(this.m_latestEndTime) > 0) {
                this.m_latestEndTime = endTime;
            }
        }
    }
    
    public String getName() {
        final EffectContainer commonEffectContainer = this.getCommonEffectContainer();
        if (commonEffectContainer != null) {
            switch (commonEffectContainer.getContainerType()) {
                case 12: {
                    return ((Item)commonEffectContainer).getName();
                }
                case 11:
                case 25: {
                    return (String)((SpellLevel)commonEffectContainer).getFieldValue("name");
                }
                case 1: {
                    final StateClient state = (StateClient)commonEffectContainer;
                    String name = state.getTypeColoredName();
                    if (state.isDisplayCasterName() && !this.m_runningEffects.isEmpty()) {
                        final RunningEffect re = this.m_runningEffects.get(0);
                        final String casterName = ((CharacterInfo)re.getCaster()).getName();
                        name = name + " - " + casterName;
                    }
                    return name;
                }
                case 3: {
                    final BasicEffectArea area = (BasicEffectArea)commonEffectContainer;
                    return WakfuTranslator.getInstance().getString(6, (int)area.getBaseId(), new Object[0]);
                }
                case 19:
                case 33: {
                    return WakfuTranslator.getInstance().getString("protector.buff.generic.name");
                }
                case 28: {
                    return WakfuTranslator.getInstance().getString("haven.world.buff");
                }
                case 32: {
                    return WakfuTranslator.getInstance().getString("guild.buff");
                }
                case 21: {
                    return WakfuTranslator.getInstance().getString("timePoint.bonus");
                }
                case 34: {
                    if (this.m_runningEffects.isEmpty()) {
                        return null;
                    }
                    final RunningEffect firstEffect = this.m_runningEffects.get(0);
                    final AntiAddictionLevel level = AntiAddictionLevel.getForEffectId(firstEffect.getGenericEffect().getEffectId());
                    return WakfuTranslator.getInstance().getString("antiAddictionLevel.name." + level.getId());
                }
            }
        }
        return "";
    }
    
    @Override
    public void release() {
        if (this.m_pool != null) {
            try {
                this.m_pool.returnObject(this);
            }
            catch (Exception e) {
                RunningEffectFieldProvider.m_logger.error((Object)("Exception dans le release de " + this.getClass().toString() + " normalement impossible"));
            }
            this.m_pool = null;
        }
        else {
            this.onCheckIn();
        }
    }
    
    @Override
    public void onCheckIn() {
        this.m_containers.clear();
        this.m_runningEffects.clear();
        this.m_pool = null;
        this.m_remainingSec = -1L;
        this.m_latestEndTime = null;
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public int compareTo(final RunningEffectFieldProvider re) {
        final Fight fight = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight();
        long deltaTime;
        if (fight != null && this.m_latestEndTime != null && re.m_latestEndTime != null) {
            if (this.m_latestEndTime.isInfinite() && re.m_latestEndTime.isInfinite()) {
                deltaTime = 0L;
            }
            else if (this.m_latestEndTime.isInfinite()) {
                deltaTime = 1L;
            }
            else if (re.m_latestEndTime.isInfinite()) {
                deltaTime = -1L;
            }
            else {
                deltaTime = fight.getTimeline().howLongInTurnsUntil(this.m_latestEndTime) - fight.getTimeline().howLongInTurnsUntil(re.m_latestEndTime);
            }
        }
        else if (this.m_remainingSec == re.m_remainingSec) {
            deltaTime = 0L;
        }
        else if (this.m_remainingSec == -1L) {
            deltaTime = 1L;
        }
        else if (re.m_remainingSec == -1L) {
            deltaTime = -1L;
        }
        else {
            deltaTime = this.m_remainingSec - re.m_remainingSec;
        }
        if (deltaTime == 0L) {
            return (int)(this.getCommonEffectContainer().getEffectContainerId() - re.getCommonEffectContainer().getEffectContainerId());
        }
        return (int)deltaTime;
    }
    
    public void setRemainingSec(final long remainingSec) {
        this.m_remainingSec = remainingSec;
    }
    
    public void addRunningEffect(final RunningEffect re, final boolean display) {
        if (display && !this.m_runningEffects.contains(re)) {
            boolean added = false;
            final ArrayList<WakfuEffect> effects = new ArrayList<WakfuEffect>();
            for (final WakfuEffect wakfuEffect : this.m_containers.get(0)) {
                effects.add(wakfuEffect);
            }
            for (int i = this.m_runningEffects.size() - 1; i >= 0; --i) {
                final RunningEffect runningEffect = this.m_runningEffects.get(i);
                if (runningEffect.mustBeTriggered() && runningEffect.getGenericEffect() != null && re.mustBeTriggered() && re.getGenericEffect() != null && runningEffect.getGenericEffect().getActionId() == re.getGenericEffect().getActionId()) {
                    final int index = this.m_runningEffects.indexOf(runningEffect);
                    if (!this.m_runningEffects.contains(re)) {
                        this.m_runningEffects.add(index, re);
                        added = true;
                    }
                }
            }
            if (!added) {
                final int index2 = effects.indexOf(re.getGenericEffect());
                if (index2 >= 0 && index2 < this.m_runningEffects.size()) {
                    if (!this.m_runningEffects.contains(re)) {
                        this.m_runningEffects.add(index2, re);
                    }
                }
                else if (!this.m_runningEffects.contains(re)) {
                    this.m_runningEffects.add(re);
                }
            }
        }
        this.updateEnd(re);
    }
    
    public int getEffectsCount() {
        return this.m_runningEffects.size();
    }
    
    public void updateDuration() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "tableTurnDuration", "remainingDuration", "plainRemainingDuration", "remainingDurationText", "isInfiniteDuration");
    }
    
    public RunningEffectFieldProvider getCopy() {
        final RunningEffectFieldProvider re = new RunningEffectFieldProvider();
        for (int i = 0, size = this.m_runningEffects.size(); i < size; ++i) {
            re.m_runningEffects.add(this.m_runningEffects.get(i));
        }
        re.m_remainingSec = this.m_remainingSec;
        re.m_latestEndTime = this.m_latestEndTime;
        for (int i = 0, size = this.m_containers.size(); i < size; ++i) {
            re.m_containers.add(this.m_containers.get(i));
        }
        return re;
    }
    
    static {
        RunningEffectFieldProvider.m_logger = Logger.getLogger((Class)RunningEffectFieldProvider.class);
        FIELDS = new String[] { "name", "tableTurnDuration", "remainingDuration", "remainingDurationText", "iconUrl", "effects", "stackSize" };
        m_staticPool = new MonitoredPool(new ObjectFactory<RunningEffectFieldProvider>() {
            @Override
            public RunningEffectFieldProvider makeObject() {
                return new RunningEffectFieldProvider(null);
            }
        });
    }
}
