package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.framework.sound.openAL.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.ui.providers.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.wakfu.client.alea.graphics.tacticalView.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.console.command.display.*;
import com.ankamagames.framework.graphics.engine.test.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.wakfu.client.core.weather.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.console.*;
import com.ankamagames.wakfu.client.ui.protocol.message.options.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.chat.*;
import java.util.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.baseImpl.graphics.ui.shortcuts.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.game.region.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import gnu.trove.*;

@XulorActionsTag
public class OptionsDialogActions
{
    public static final String PACKAGE = "wakfu.options";
    private static final Logger m_logger;
    private static boolean m_binding;
    private static boolean m_resolutionIsDirty;
    private static int m_fontOrdinal;
    public static TIntObjectHashMap<OptionAction> m_optionActions;
    private static int m_previousIndex;
    private static AudioSource m_testSource;
    
    public static void setSelectedTabIndex(final int index) {
        if (OptionsDialogActions.m_previousIndex == index) {
            return;
        }
        if (index != 2) {
            WakfuSoundManager.getInstance().fadeMusic(0.2f, 1000);
            WakfuSoundManager.getInstance().fadeAmbiance(0.2f, 1000);
        }
        else {
            WakfuSoundManager.getInstance().fadeMusic(1.0f, 1000);
            WakfuSoundManager.getInstance().fadeAmbiance(1.0f, 1000);
        }
        OptionsDialogActions.m_previousIndex = index;
    }
    
    public static void tabClick(final MouseEvent e, final TabbedContainer tc) {
        setSelectedTabIndex(tc.getSelectedTabIndex());
    }
    
    public static void setMusicVolume(final SliderMovedEvent event) {
        final WakfuGamePreferences wakfuGamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
        final KeyPreferenceStoreEnum soundsVolumePreferenceKey = KeyPreferenceStoreEnum.MUSIC_VOLUME_PREFERENCE_KEY;
        final float newMusicSoundsVolume = event.getValue();
        WakfuSoundManager.getInstance().setMusicVolume(newMusicSoundsVolume);
        OptionsDialogActions.m_optionActions.put(soundsVolumePreferenceKey.hashCode(), new OptionAction() {
            @Override
            public void valid() {
                wakfuGamePreferences.setValue(soundsVolumePreferenceKey, newMusicSoundsVolume);
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(wakfuGamePreferences, soundsVolumePreferenceKey.getKey());
            }
            
            @Override
            public void cancel() {
                WakfuSoundManager.getInstance().setMusicVolume(wakfuGamePreferences.getFloatValue(soundsVolumePreferenceKey));
            }
        });
    }
    
    public static void setAmbianceSoundsVolume(final SliderMovedEvent event) {
        final WakfuGamePreferences wakfuGamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
        final KeyPreferenceStoreEnum soundsVolumePreferenceKey = KeyPreferenceStoreEnum.AMBIANCE_SOUNDS_VOLUME_PREFERENCE_KEY;
        final float newAmbianceSoundsVolume = event.getValue();
        WakfuSoundManager.getInstance().setAmbianceSoundsVolume(newAmbianceSoundsVolume);
        OptionsDialogActions.m_optionActions.put(soundsVolumePreferenceKey.hashCode(), new OptionAction() {
            @Override
            public void valid() {
                wakfuGamePreferences.setValue(soundsVolumePreferenceKey, newAmbianceSoundsVolume);
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(wakfuGamePreferences, soundsVolumePreferenceKey.getKey());
            }
            
            @Override
            public void cancel() {
                WakfuSoundManager.getInstance().setAmbianceSoundsVolume(wakfuGamePreferences.getFloatValue(soundsVolumePreferenceKey));
            }
        });
    }
    
    public static void setUiSoundsVolume(final SliderMovedEvent event) {
        final WakfuGamePreferences wakfuGamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
        final KeyPreferenceStoreEnum soundsVolumePreferenceKey = KeyPreferenceStoreEnum.UI_SOUNDS_VOLUME_PREFERENCE_KEY;
        final float newUiSoundsVolume = event.getValue();
        WakfuSoundManager.getInstance().setUiSoundsVolume(newUiSoundsVolume);
        OptionsDialogActions.m_optionActions.put(soundsVolumePreferenceKey.hashCode(), new OptionAction() {
            @Override
            public void valid() {
                wakfuGamePreferences.setValue(soundsVolumePreferenceKey, newUiSoundsVolume);
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(wakfuGamePreferences, soundsVolumePreferenceKey.getKey());
            }
            
            @Override
            public void cancel() {
                WakfuSoundManager.getInstance().setUiSoundsVolume(wakfuGamePreferences.getFloatValue(soundsVolumePreferenceKey));
            }
        });
    }
    
    public static void setMusicMute(final SelectionChangedEvent event) {
        final KeyPreferenceStoreEnum muteKey = KeyPreferenceStoreEnum.MUSIC_MUTE_PREFERENCE_KEY;
        final int key = muteKey.hashCode();
        final boolean selected = event.isSelected();
        WakfuSoundManager.getInstance().setMusicMute(selected);
        OptionsDialogActions.m_optionActions.put(key, new OptionAction() {
            @Override
            public void valid() {
                WakfuClientInstance.getInstance().getGamePreferences().setValue(muteKey, selected);
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), muteKey.getKey());
            }
            
            @Override
            public void cancel() {
                final boolean mute = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(muteKey);
                WakfuSoundManager.getInstance().setMusicMute(mute);
            }
        });
    }
    
    public static void setMusicContinuousMode(final SelectionChangedEvent event) {
        final KeyPreferenceStoreEnum continousKey = KeyPreferenceStoreEnum.MUSIC_CONTINUOUS_MODE_PREFERENCE_KEY;
        final int key = continousKey.hashCode();
        final boolean selected = event.isSelected();
        WakfuSoundManager.getInstance().setMusicContinuousMode(selected);
        OptionsDialogActions.m_optionActions.put(key, new OptionAction() {
            @Override
            public void valid() {
                WakfuClientInstance.getInstance().getGamePreferences().setValue(continousKey, selected);
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), continousKey.getKey());
            }
            
            @Override
            public void cancel() {
                final boolean continuous = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(continousKey);
                WakfuSoundManager.getInstance().setMusicContinuousMode(continuous);
            }
        });
    }
    
    public static void setAmbianceSoundsMute(final SelectionChangedEvent event) {
        final KeyPreferenceStoreEnum muteKey = KeyPreferenceStoreEnum.AMBIANCE_SOUNDS_MUTE_PREFERENCE_KEY;
        final int key = muteKey.hashCode();
        final boolean selected = event.isSelected();
        WakfuSoundManager.getInstance().setAmbianceSoundsMute(selected);
        OptionsDialogActions.m_optionActions.put(key, new OptionAction() {
            @Override
            public void valid() {
                WakfuClientInstance.getInstance().getGamePreferences().setValue(muteKey, selected);
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), muteKey.getKey());
            }
            
            @Override
            public void cancel() {
                final boolean mute = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(muteKey);
                WakfuSoundManager.getInstance().setAmbianceSoundsMute(mute);
            }
        });
    }
    
    public static void setUiSoundsMute(final SelectionChangedEvent event) {
        final KeyPreferenceStoreEnum muteKey = KeyPreferenceStoreEnum.UI_SOUNDS_MUTE_PREFERENCE_KEY;
        final int key = muteKey.hashCode();
        final boolean selected = event.isSelected();
        WakfuSoundManager.getInstance().setUiSoundsMute(selected);
        OptionsDialogActions.m_optionActions.put(key, new OptionAction() {
            @Override
            public void valid() {
                WakfuClientInstance.getInstance().getGamePreferences().setValue(muteKey, selected);
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), muteKey.getKey());
            }
            
            @Override
            public void cancel() {
                final boolean mute = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(muteKey);
                WakfuSoundManager.getInstance().setUiSoundsMute(mute);
            }
        });
    }
    
    public static void startAmbianceSoundTest(final Event e) {
        OptionsDialogActions.m_testSource = WakfuSoundManager.getInstance().playSFXSound(600025L, true);
    }
    
    public static void startUiSoundTest(final Event e) {
        OptionsDialogActions.m_testSource = WakfuSoundManager.getInstance().playGUISound(600025L, true);
    }
    
    public static void stopSoundTest(final Event e) {
        if (OptionsDialogActions.m_testSource != null) {
            OptionsDialogActions.m_testSource.fadeOutAndStop(0.2f);
            OptionsDialogActions.m_testSource = null;
        }
    }
    
    public static void closeOptionsDialog(final Event event) {
        UIMessage.send((short)16403);
    }
    
    public static void closeOptionsAndMenuDialogs(final Event event) {
        UIMessage.send((short)16403);
        UIMessage.send((short)16401);
    }
    
    public static void onScreenModeChanged(final ListSelectionChangedEvent event) throws Exception {
        OptionsDialogActions.m_resolutionIsDirty = true;
        UIOptionsFrame.getInstance().getUIFieldProvider().onModeSelected((GLApplicationUIOptionsFieldProvider.ModeView)event.getValue());
    }
    
    public static void onScreenSizeChanged(final ListSelectionChangedEvent event) throws Exception {
        OptionsDialogActions.m_resolutionIsDirty = true;
        UIOptionsFrame.getInstance().getUIFieldProvider().onResolutionSelected(event.getValue());
    }
    
    public static void onGraphicalPresetsChanged(final ListSelectionChangedEvent event) {
        if (!event.getSelected()) {
            return;
        }
        final WakfuKeyPreferenceStoreEnum key = WakfuKeyPreferenceStoreEnum.GRAPHICAL_PRESETS_KEY;
        final WakfuGraphicalPresets.Level currentLevel = WakfuGraphicalPresets.Level.getFromId((byte)WakfuClientInstance.getInstance().getGamePreferences().getIntValue(key));
        final WakfuGraphicalPresets.Level level = (WakfuGraphicalPresets.Level)event.getValue();
        if (currentLevel == level) {
            return;
        }
        switch (level) {
            case LOW: {
                setLODLevel((byte)0);
                setFightLODLevel((byte)0);
                activateVSync(true);
                enableRunningRadius(false);
                activateShaders(false);
                activateMeteoEffect(false);
                break;
            }
            case MEDIUM: {
                setLODLevel((byte)1);
                setFightLODLevel((byte)1);
                activateVSync(true);
                enableRunningRadius(false);
                activateShaders(true);
                activateMeteoEffect(true);
                break;
            }
            case HIGH: {
                setLODLevel((byte)2);
                setFightLODLevel((byte)2);
                activateVSync(true);
                enableRunningRadius(true);
                activateShaders(true);
                activateMeteoEffect(true);
                break;
            }
        }
        WakfuClientInstance.getInstance().getGamePreferences().setValue(key, level.getId());
    }
    
    public static void setLODLevel(final SliderMovedEvent event) {
        if (setLODLevel((byte)event.getValue())) {
            WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.GRAPHICAL_PRESETS_KEY, WakfuGraphicalPresets.Level.CUSTOM.getId());
        }
    }
    
    public static boolean setLODLevel(final byte level) {
        final KeyPreferenceStoreEnum lodLevelKey = KeyPreferenceStoreEnum.LOD_LEVEL_KEY;
        final int oldValue = WakfuClientInstance.getInstance().getGamePreferences().getIntValue(lodLevelKey);
        if (oldValue == level) {
            return false;
        }
        WakfuClientInstance.getInstance().setLODLevel(level);
        WakfuClientInstance.getInstance().getGamePreferences().setValue(lodLevelKey, level);
        OptionsDialogActions.m_optionActions.put(lodLevelKey.hashCode(), new OptionAction() {
            @Override
            public void valid() {
            }
            
            @Override
            public void cancel() {
                WakfuClientInstance.getInstance().setLODLevel((byte)oldValue);
                WakfuClientInstance.getInstance().getGamePreferences().setValue(lodLevelKey, oldValue);
            }
        });
        return true;
    }
    
    public static void setFightLODLevel(final SliderMovedEvent event) {
        if (setFightLODLevel((byte)event.getValue())) {
            WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.GRAPHICAL_PRESETS_KEY, WakfuGraphicalPresets.Level.CUSTOM.getId());
        }
    }
    
    public static boolean setFightLODLevel(final byte level) {
        final KeyPreferenceStoreEnum lodLevelKey = KeyPreferenceStoreEnum.FIGHT_LOD_LEVEL_KEY;
        final int oldValue = WakfuClientInstance.getInstance().getGamePreferences().getIntValue(lodLevelKey);
        if (oldValue == level) {
            return false;
        }
        FightVisibilityManager.getInstance().setFightLODLevel(level);
        WakfuClientInstance.getInstance().getGamePreferences().setValue(lodLevelKey, level);
        OptionsDialogActions.m_optionActions.put(lodLevelKey.hashCode(), new OptionAction() {
            @Override
            public void valid() {
            }
            
            @Override
            public void cancel() {
                FightVisibilityManager.getInstance().setFightLODLevel((byte)oldValue);
                WakfuClientInstance.getInstance().getGamePreferences().setValue(lodLevelKey, oldValue);
            }
        });
        return true;
    }
    
    public static void setActivateAreaChallenges(final SelectionChangedEvent event) {
        final WakfuKeyPreferenceStoreEnum areaChallengesActivatedKey = WakfuKeyPreferenceStoreEnum.AREA_CHALLENGES_ACTIVATED_KEY;
        final int key = areaChallengesActivatedKey.hashCode();
        final boolean selected = event.isSelected();
        OptionsDialogActions.m_optionActions.put(areaChallengesActivatedKey.hashCode(), new OptionAction() {
            @Override
            public void valid() {
                WakfuClientInstance.getInstance().getGamePreferences().setValue(areaChallengesActivatedKey, selected);
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), areaChallengesActivatedKey.getKey());
            }
            
            @Override
            public void cancel() {
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), areaChallengesActivatedKey.getKey());
            }
        });
    }
    
    public static void activateVSync(final SelectionChangedEvent event) {
        if (activateVSync(event.isSelected())) {
            WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.GRAPHICAL_PRESETS_KEY, WakfuGraphicalPresets.Level.CUSTOM.getId());
        }
    }
    
    public static boolean activateVSync(final boolean activate) {
        final WakfuKeyPreferenceStoreEnum vsyncActivated = WakfuKeyPreferenceStoreEnum.VSYNC_ACTIVATED_KEY;
        final int key = vsyncActivated.hashCode();
        final boolean oldValue = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(vsyncActivated);
        if (oldValue == activate) {
            return false;
        }
        WakfuClientInstance.getInstance().getGamePreferences().setValue(vsyncActivated, activate);
        WakfuClientInstance.getInstance().getRenderer().requestVSync(activate);
        OptionsDialogActions.m_optionActions.put(key, new OptionAction() {
            @Override
            public void valid() {
            }
            
            @Override
            public void cancel() {
                WakfuClientInstance.getInstance().getRenderer().requestVSync(oldValue);
                WakfuClientInstance.getInstance().getGamePreferences().setValue(vsyncActivated, oldValue);
            }
        });
        return true;
    }
    
    public static void enableRunningRadius(final SelectionChangedEvent event) {
        if (enableRunningRadius(event.isSelected())) {
            WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.GRAPHICAL_PRESETS_KEY, WakfuGraphicalPresets.Level.CUSTOM.getId());
        }
    }
    
    public static boolean enableRunningRadius(final boolean enable) {
        final WakfuKeyPreferenceStoreEnum enableRadius = WakfuKeyPreferenceStoreEnum.ENABLE_RUNNING_RADIUS;
        final int key = enableRadius.hashCode();
        final boolean oldValue = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(enableRadius);
        if (oldValue == enable) {
            return false;
        }
        AnimatedElement.setEnableRunningRadius(enable);
        WakfuClientInstance.getInstance().getGamePreferences().setValue(enableRadius, enable);
        OptionsDialogActions.m_optionActions.put(key, new OptionAction() {
            @Override
            public void valid() {
            }
            
            @Override
            public void cancel() {
                AnimatedElement.setEnableRunningRadius(oldValue);
                WakfuClientInstance.getInstance().getGamePreferences().setValue(enableRadius, oldValue);
            }
        });
        return true;
    }
    
    public static void setAutoLockFights(final SelectionChangedEvent event) {
        final WakfuKeyPreferenceStoreEnum autoLockFightsKey = WakfuKeyPreferenceStoreEnum.AUTO_LOCK_FIGHTS_KEY;
        final int key = autoLockFightsKey.hashCode();
        final boolean selected = event.isSelected();
        OptionsDialogActions.m_optionActions.put(autoLockFightsKey.hashCode(), new OptionAction() {
            @Override
            public void valid() {
                final boolean currentValue = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(autoLockFightsKey);
                WakfuClientInstance.getInstance().getGamePreferences().setValue(autoLockFightsKey, !currentValue);
            }
            
            @Override
            public void cancel() {
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), autoLockFightsKey.getKey());
            }
        });
    }
    
    public static void setTacticalView(final SelectionChangedEvent event) {
        final WakfuKeyPreferenceStoreEnum tacticalViewKey = WakfuKeyPreferenceStoreEnum.TACTICAL_VIEW_KEY;
        final int key = tacticalViewKey.hashCode();
        final boolean oldValue = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(tacticalViewKey);
        final boolean activate = event.isSelected();
        if (oldValue == activate) {
            return;
        }
        OptionsDialogActions.m_optionActions.put(tacticalViewKey.hashCode(), new OptionAction() {
            @Override
            public void valid() {
            }
            
            @Override
            public void cancel() {
                TacticalViewManager.getInstance().activate(oldValue);
                WakfuClientInstance.getInstance().getGamePreferences().setValue(tacticalViewKey, oldValue);
            }
        });
        TacticalViewManager.getInstance().activate(activate);
        WakfuClientInstance.getInstance().getGamePreferences().setValue(tacticalViewKey, activate);
    }
    
    public static void setMaskWorld(final SelectionChangedEvent event) {
        final WakfuKeyPreferenceStoreEnum alphaMaskActivated = WakfuKeyPreferenceStoreEnum.ALPHA_MASK_ACTIVATED_KEY;
        final int key = alphaMaskActivated.hashCode();
        final boolean selected = event.isSelected();
        AlphaMaskCommand.applyAlphaMasks(selected);
        OptionsDialogActions.m_optionActions.put(alphaMaskActivated.hashCode(), new OptionAction() {
            @Override
            public void valid() {
                WakfuClientInstance.getInstance().getGamePreferences().setValue(alphaMaskActivated, selected);
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), alphaMaskActivated.getKey());
            }
            
            @Override
            public void cancel() {
                final boolean value = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(alphaMaskActivated);
                AlphaMaskCommand.applyAlphaMasks(value);
            }
        });
    }
    
    public static void setDisplayHPBar(final SelectionChangedEvent event) {
        final WakfuKeyPreferenceStoreEnum displayHPBar = WakfuKeyPreferenceStoreEnum.DISPLAY_HP_BAR_KEY;
        final int key = displayHPBar.hashCode();
        final boolean selected = event.isSelected();
        DisplayHpBarCommand.displayHpBar(selected);
        OptionsDialogActions.m_optionActions.put(key, new OptionAction() {
            @Override
            public void valid() {
                WakfuClientInstance.getInstance().getGamePreferences().setValue(displayHPBar, selected);
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), displayHPBar.getKey());
            }
            
            @Override
            public void cancel() {
                final boolean value = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(displayHPBar);
                DisplayHpBarCommand.displayHpBar(value);
            }
        });
    }
    
    public static void setHideFightOccluders(final SelectionChangedEvent event) {
        final WakfuKeyPreferenceStoreEnum hideFightOccluders = WakfuKeyPreferenceStoreEnum.HIDE_FIGHT_OCCLUDERS_ACTIVATED_KEY;
        final int key = hideFightOccluders.hashCode();
        final boolean selected = event.isSelected();
        proceedHideFightOccluders(selected);
        OptionsDialogActions.m_optionActions.put(hideFightOccluders.hashCode(), new OptionAction() {
            @Override
            public void valid() {
                WakfuClientInstance.getInstance().getGamePreferences().setValue(hideFightOccluders, selected);
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), hideFightOccluders.getKey());
            }
            
            @Override
            public void cancel() {
                final boolean selected = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(hideFightOccluders);
                OptionsDialogActions.proceedHideFightOccluders(selected);
            }
        });
    }
    
    public static void proceedHideFightOccluders(final boolean selected) {
        final float alpha = selected ? 0.3f : 1.0f;
        if (WakfuGameEntity.getInstance().hasFrame(UIFightFrame.getInstance())) {
            UIFightFrame.getInstance().hideFighters(selected);
            if (selected) {
                AlphaMaskCommand.applyAlphaMasks(true);
            }
            else {
                final boolean value = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.ALPHA_MASK_ACTIVATED_KEY);
                AlphaMaskCommand.applyAlphaMasks(value);
            }
        }
        final int state = HideFightOccluders.getStateFromAlpha(alpha);
        HideFightOccluders.hideFightOccluders(state, alpha, 1000);
    }
    
    public static void activateShaders(final SelectionChangedEvent event) {
        if (activateShaders(event.isSelected())) {
            WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.GRAPHICAL_PRESETS_KEY, WakfuGraphicalPresets.Level.CUSTOM.getId());
        }
    }
    
    private static boolean activateShaders(final boolean activate) {
        if (!HardwareFeatureManager.INSTANCE.isFeatureSupported(HardwareFeature.GL_FRAGMENT_SHADERS)) {
            return false;
        }
        final WakfuKeyPreferenceStoreEnum shadersActivated = WakfuKeyPreferenceStoreEnum.SHADERS_ACTIVATED_KEY;
        final boolean oldValue = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(shadersActivated);
        if (oldValue == activate) {
            return false;
        }
        doActivateShaders(activate);
        WakfuClientInstance.getInstance().getGamePreferences().setValue(shadersActivated, activate);
        OptionsDialogActions.m_optionActions.put(shadersActivated.hashCode(), new OptionAction() {
            @Override
            public void valid() {
            }
            
            @Override
            public void cancel() {
                doActivateShaders(oldValue);
                WakfuClientInstance.getInstance().getGamePreferences().setValue(shadersActivated, oldValue);
            }
        });
        return true;
    }
    
    private static void doActivateShaders(final boolean activate) {
        EffectManager.getInstance().enablePostProcess(activate);
        IsoSceneLightManager.INSTANCE.forceUpdate();
        MobileManager.getInstance().forceReload();
        ResourceManager.getInstance().forceReload();
        SimpleAnimatedElementManager.getInstance().forceReload();
        AnimatedElementSceneViewManager.getInstance().forceReload();
        final WakfuWorldScene worldScene = (WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene();
        if (worldScene != null) {
            final DisplayedScreenWorld screenWorld = worldScene.getDisplayedScreenWorld();
            if (screenWorld != null) {
                screenWorld.reloadDisplayedScreenMaps();
            }
        }
    }
    
    public static void activateMeteoEffect(final SelectionChangedEvent event) {
        if (activateMeteoEffect(event.isSelected())) {
            WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.GRAPHICAL_PRESETS_KEY, WakfuGraphicalPresets.Level.CUSTOM.getId());
        }
    }
    
    private static boolean activateMeteoEffect(final boolean activate) {
        final WakfuKeyPreferenceStoreEnum activated = WakfuKeyPreferenceStoreEnum.METEO_EFFECT_ACTIVATED_KEY;
        final boolean oldValue = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(activated);
        if (oldValue == activate) {
            return false;
        }
        WeatherEffectManager.INSTANCE.setActivated(activate);
        WakfuClientInstance.getInstance().getGamePreferences().setValue(activated, activate);
        OptionsDialogActions.m_optionActions.put(activated.hashCode(), new OptionAction() {
            @Override
            public void valid() {
            }
            
            @Override
            public void cancel() {
                WeatherEffectManager.INSTANCE.setActivated(oldValue);
                WakfuClientInstance.getInstance().getGamePreferences().setValue(activated, oldValue);
            }
        });
        return true;
    }
    
    public static void bindKey(final ItemEvent event) {
        if (OptionsDialogActions.m_binding) {
            return;
        }
        bindKey((ShortcutFieldProvider)event.getItemValue(), 17731);
    }
    
    public static void bindKey(final Event event, final TextEditor textEditor) {
        final String s = textEditor.getText();
        if (s == null || s.length() == 0) {
            Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("notification.emptyField"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 4099L, 102, 1);
            return;
        }
        final Shortcut shortcut = ShortcutsFieldProvider.getInstance().generateNewShortcutForBinding(s);
        bindKey(new ShortcutFieldProvider(shortcut), 17738);
    }
    
    public static void bindKey(final ShortcutFieldProvider shortcutFieldProvider, final int messageId) {
        final UIBindShortcutMessage uiBindShortcutMessage = new UIBindShortcutMessage(shortcutFieldProvider);
        uiBindShortcutMessage.setId(messageId);
        Worker.getInstance().pushMessage(uiBindShortcutMessage);
    }
    
    public static void selectCurrentBind(final ListSelectionChangedEvent event, final TextEditor textEditor) {
        final ShortcutFieldProvider shortcut = (ShortcutFieldProvider)event.getRenderableContainer().getItemValue();
        textEditor.setText(shortcut.getText());
        ShortcutsFieldProvider.getInstance().setCurrentBind(shortcut);
    }
    
    public static void changeBindText(final Event event, final TextEditor textEditor, final ShortcutFieldProvider shortcutFieldProvider) {
        final String text = textEditor.getText();
        if (text == null || text.length() == 0) {
            Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("notification.emptyField"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 4099L, 102, 1);
            return;
        }
        if (!text.equals(shortcutFieldProvider.getText())) {
            final UIBindShortcutMessage uiBindShortcutMessage = new UIBindShortcutMessage(shortcutFieldProvider);
            uiBindShortcutMessage.setStringValue(text);
            uiBindShortcutMessage.setId(17732);
            Worker.getInstance().pushMessage(uiBindShortcutMessage);
        }
    }
    
    public static void activateBindTextEdition(final Event event) {
        final ToggleButton button = event.getCurrentTarget();
        if (button.getSelected()) {
            ShortcutsFieldProvider.getInstance().activateBindTextEdition(false);
        }
        else {
            ShortcutsFieldProvider.getInstance().activateBindTextEdition(true);
        }
    }
    
    public static void deleteCurrentBind(final Event event) {
        UIMessage.send((short)17739);
    }
    
    public static void onShortcutOver(final ItemEvent itemEvent) {
        PropertiesProvider.getInstance().setPropertyValue("shortcutOver", itemEvent.getItemValue());
    }
    
    public static void onShortcutOut(final ItemEvent itemEvent) {
        PropertiesProvider.getInstance().setPropertyValue("shortcutOver", null);
    }
    
    public static void restore(final Event event) {
        UIMessage.send((short)17733);
    }
    
    public static void setInteractionOnLeftClick(final SelectionChangedEvent event) {
        final WakfuKeyPreferenceStoreEnum leftClickKey = WakfuKeyPreferenceStoreEnum.INTERACTION_ON_LEFT_CLICK_KEY;
        final int key = leftClickKey.hashCode();
        final boolean selected = event.isSelected();
        OptionsDialogActions.m_optionActions.put(leftClickKey.hashCode(), new OptionAction() {
            @Override
            public void valid() {
                WakfuClientInstance.getInstance().getGamePreferences().setValue(leftClickKey, selected);
            }
            
            @Override
            public void cancel() {
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), leftClickKey.getKey());
            }
        });
    }
    
    public static void setDefaultSplitMode(final SelectionChangedEvent event) {
        final WakfuKeyPreferenceStoreEnum defaultSplitKey = WakfuKeyPreferenceStoreEnum.DEFAULT_SPLIT_MODE_KEY;
        final int key = defaultSplitKey.hashCode();
        final boolean selected = event.isSelected();
        OptionsDialogActions.m_optionActions.put(defaultSplitKey.hashCode(), new OptionAction() {
            @Override
            public void valid() {
                WakfuClientInstance.getInstance().getGamePreferences().setValue(defaultSplitKey, selected);
            }
            
            @Override
            public void cancel() {
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), defaultSplitKey.getKey());
            }
        });
    }
    
    public static void setOverHeadDelay(final Event event) {
        if (event != null && event instanceof SliderMovedEvent) {
            final WakfuKeyPreferenceStoreEnum overHeadDelayKey = WakfuKeyPreferenceStoreEnum.OVER_HEAD_DELAY_KEY;
            OptionsDialogActions.m_optionActions.put(overHeadDelayKey.hashCode(), new OptionAction() {
                @Override
                public void valid() {
                    final float delay = ((SliderMovedEvent)event).getValue() / 2.0f;
                    WakfuClientInstance.getInstance().getGamePreferences().setValue(overHeadDelayKey, delay);
                }
                
                @Override
                public void cancel() {
                    GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), overHeadDelayKey.getKey());
                }
            });
        }
    }
    
    public static void setAutoSwitchBarsMode(final SelectionChangedEvent event) {
        final WakfuKeyPreferenceStoreEnum autoSwitchBarsModeKey = WakfuKeyPreferenceStoreEnum.AUTO_SWITCH_BARS_MODE_KEY;
        final int key = autoSwitchBarsModeKey.hashCode();
        final boolean selected = event.isSelected();
        OptionsDialogActions.m_optionActions.put(autoSwitchBarsModeKey.hashCode(), new OptionAction() {
            @Override
            public void valid() {
                WakfuClientInstance.getInstance().getGamePreferences().setValue(autoSwitchBarsModeKey, selected);
            }
            
            @Override
            public void cancel() {
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), autoSwitchBarsModeKey.getKey());
            }
        });
    }
    
    public static void setEmoteIconsActivated(final SelectionChangedEvent event) {
        final WakfuKeyPreferenceStoreEnum emoteIconsActivated = WakfuKeyPreferenceStoreEnum.EMOTE_ICONS_ACTIVATED;
        final int key = emoteIconsActivated.hashCode();
        final boolean selected = event.isSelected();
        OptionsDialogActions.m_optionActions.put(emoteIconsActivated.hashCode(), new OptionAction() {
            @Override
            public void valid() {
                WakfuClientInstance.getInstance().getGamePreferences().setValue(emoteIconsActivated, selected);
            }
            
            @Override
            public void cancel() {
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), emoteIconsActivated.getKey());
            }
        });
    }
    
    public static void setDisplayXPBar(final SelectionChangedEvent event) {
        final WakfuKeyPreferenceStoreEnum displayXpBarActivated = WakfuKeyPreferenceStoreEnum.DISPLAY_XP_BAR;
        final int key = displayXpBarActivated.hashCode();
        final boolean selected = event.isSelected();
        UIXPGainFrame.getInstance().loadDialog(selected);
        OptionsDialogActions.m_optionActions.put(displayXpBarActivated.hashCode(), new OptionAction() {
            @Override
            public void valid() {
                WakfuClientInstance.getInstance().getGamePreferences().setValue(displayXpBarActivated, selected);
            }
            
            @Override
            public void cancel() {
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), displayXpBarActivated.getKey());
                UIXPGainFrame.getInstance().loadDialog(selected);
            }
        });
    }
    
    public static void setCensorActivated(final SelectionChangedEvent event) {
        final WakfuKeyPreferenceStoreEnum censorActivated = WakfuKeyPreferenceStoreEnum.CENSOR_ACTIVATED;
        final int key = censorActivated.hashCode();
        final boolean selected = event.isSelected();
        OptionsDialogActions.m_optionActions.put(censorActivated.hashCode(), new OptionAction() {
            @Override
            public void valid() {
                WakfuClientInstance.getInstance().getGamePreferences().setValue(censorActivated, selected);
            }
            
            @Override
            public void cancel() {
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), censorActivated.getKey());
            }
        });
    }
    
    public static void setTipsActivated(final SelectionChangedEvent event) {
        final WakfuKeyPreferenceStoreEnum tipsActivated = WakfuKeyPreferenceStoreEnum.TIPS_ACTIVATED;
        final int key = tipsActivated.hashCode();
        final boolean selected = event.isSelected();
        OptionsDialogActions.m_optionActions.put(tipsActivated.hashCode(), new OptionAction() {
            @Override
            public void valid() {
                WakfuClientInstance.getInstance().getGamePreferences().setValue(tipsActivated, selected);
            }
            
            @Override
            public void cancel() {
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), tipsActivated.getKey());
            }
        });
    }
    
    public static void setAddSeedsToShortcutBar(final SelectionChangedEvent event) {
        final WakfuKeyPreferenceStoreEnum addSeedsToShortcutBar = WakfuKeyPreferenceStoreEnum.ADD_SEEDS_TO_SHORTCUT_BAR;
        final int key = addSeedsToShortcutBar.hashCode();
        final boolean selected = event.isSelected();
        OptionsDialogActions.m_optionActions.put(addSeedsToShortcutBar.hashCode(), new OptionAction() {
            @Override
            public void valid() {
                WakfuClientInstance.getInstance().getGamePreferences().setValue(addSeedsToShortcutBar, selected);
            }
            
            @Override
            public void cancel() {
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), addSeedsToShortcutBar.getKey());
            }
        });
    }
    
    public static void setChatAutomaticDisactivation(final SelectionChangedEvent event) {
        final WakfuKeyPreferenceStoreEnum disactivation = WakfuKeyPreferenceStoreEnum.CHAT_AUTOMATIC_DISACTIVATION;
        final int key = disactivation.hashCode();
        final boolean selected = event.isSelected();
        OptionsDialogActions.m_optionActions.put(disactivation.hashCode(), new OptionAction() {
            @Override
            public void valid() {
                WakfuClientInstance.getInstance().getGamePreferences().setValue(disactivation, selected);
            }
            
            @Override
            public void cancel() {
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), disactivation.getKey());
            }
        });
    }
    
    public static void setChatLockFade(final SelectionChangedEvent event) {
        final WakfuKeyPreferenceStoreEnum chatFadeKey = WakfuKeyPreferenceStoreEnum.CHAT_FADE_KEY;
        final int key = chatFadeKey.hashCode();
        final boolean selected = event.isSelected();
        chatLockFade(selected);
        OptionsDialogActions.m_optionActions.put(chatFadeKey.hashCode(), new OptionAction() {
            @Override
            public void valid() {
                WakfuClientInstance.getInstance().getGamePreferences().setValue(chatFadeKey, selected);
            }
            
            @Override
            public void cancel() {
                chatLockFade(WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(chatFadeKey));
            }
        });
    }
    
    public static void setChatTime(final SelectionChangedEvent event) {
        final WakfuKeyPreferenceStoreEnum chatTimeKey = WakfuKeyPreferenceStoreEnum.CHAT_TIME;
        final int key = chatTimeKey.hashCode();
        final boolean selected = event.isSelected();
        OptionsDialogActions.m_optionActions.put(chatTimeKey.hashCode(), new OptionAction() {
            @Override
            public void valid() {
                WakfuClientInstance.getInstance().getGamePreferences().setValue(chatTimeKey, selected);
            }
            
            @Override
            public void cancel() {
                GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), chatTimeKey.getKey());
            }
        });
    }
    
    private static void chatLockFade(final boolean fade) {
        final TIntObjectIterator<ChatViewManager> it = ChatWindowManager.getInstance().getWindowIterator();
        while (it.hasNext()) {
            it.advance();
            final int windowId = it.value().getWindowId();
            if (fade) {
                final UIMessage msg = new UIMessage();
                msg.setId(19026);
                msg.setBooleanValue(true);
                msg.setIntValue(windowId);
                msg.setLongValue(5000L);
                Worker.getInstance().pushMessage(msg);
            }
            else {
                final UIMessage msg = new UIMessage();
                msg.setId(19025);
                msg.setIntValue(windowId);
                msg.setBooleanValue(true);
                Worker.getInstance().pushMessage(msg);
            }
        }
        final UIMessage msg2 = new UIMessage();
        msg2.setId(19027);
        msg2.setBooleanValue(!fade);
        Worker.getInstance().pushMessage(msg2);
    }
    
    public static void setFontSize(final SliderMovedEvent e) {
        final WakfuKeyPreferenceStoreEnum textFontSizeKey = WakfuKeyPreferenceStoreEnum.CHAT_TEXT_FONT_SIZE_KEY;
        final int value = (int)e.getValue();
        if (OptionsDialogActions.m_fontOrdinal == -1) {
            OptionsDialogActions.m_fontOrdinal = WakfuClientInstance.getInstance().getGamePreferences().getIntValue(textFontSizeKey);
        }
        setFontSize(value);
        OptionsDialogActions.m_optionActions.put(textFontSizeKey.hashCode(), new OptionAction() {
            @Override
            public void valid() {
                OptionsDialogActions.m_fontOrdinal = -1;
            }
            
            @Override
            public void cancel() {
                setFontSize(OptionsDialogActions.m_fontOrdinal);
                OptionsDialogActions.m_fontOrdinal = -1;
            }
        });
    }
    
    private static void setFontSize(final int value) {
        final WakfuKeyPreferenceStoreEnum textFontSizeKey = WakfuKeyPreferenceStoreEnum.CHAT_TEXT_FONT_SIZE_KEY;
        final ChatWindowManager.FontSize fontSize = ChatWindowManager.FontSize.values()[value];
        WakfuClientInstance.getInstance().getGamePreferences().setValue(textFontSizeKey, fontSize.ordinal());
        PropertiesProvider.getInstance().firePropertyValueChanged(ChatWindowManager.getInstance(), "chatTextStyle");
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), textFontSizeKey.getKey());
        for (final ChatView view : ChatWindowManager.getInstance().getAllViews()) {
            PropertiesProvider.getInstance().firePropertyValueChanged(view, "history");
        }
    }
    
    public static void cancel(final Event event) {
        if (OptionsDialogActions.m_optionActions.isEmpty()) {
            closeOptionsDialog(event);
            return;
        }
        final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.confirmCloseOptionsWithChanges"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
        messageBoxControler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 8) {
                    OptionsDialogActions.cancel();
                    OptionsDialogActions.closeOptionsDialog(event);
                }
            }
        });
    }
    
    public static void cancel() {
        final TIntObjectIterator<OptionAction> it = OptionsDialogActions.m_optionActions.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().cancel();
        }
        OptionsDialogActions.m_optionActions.clear();
        final TIntObjectIterator<Shortcut> it2 = UIOptionsFrame.getInstance().getModifiedShortcuts();
        while (it2.hasNext()) {
            it2.advance();
            final Shortcut shortcut = it2.value();
            final Shortcut originalShortcut = ShortcutManager.getInstance().getShortcut(shortcut.getId());
            try {
                UIOptionsFrame.getInstance().changeShortcut(shortcut.getId(), shortcut.getKeyCode(), shortcut.getModiferMask());
                final String params = shortcut.getParamString();
                UIOptionsFrame.getInstance().changeBindText(originalShortcut, params);
            }
            catch (Exception e) {
                OptionsDialogActions.m_logger.error((Object)"Exception", (Throwable)e);
            }
        }
    }
    
    public static void apply(final Event e) {
        final TIntObjectIterator<OptionAction> it = OptionsDialogActions.m_optionActions.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().valid();
        }
        OptionsDialogActions.m_optionActions.clear();
        if (OptionsDialogActions.m_resolutionIsDirty) {
            final GLApplicationUIOptionsFieldProvider glApplicationFieldProvider = UIOptionsFrame.getInstance().getUIFieldProvider();
            glApplicationFieldProvider.applyResolution(glApplicationFieldProvider.getSelectedMode(), glApplicationFieldProvider.getSelectedResolution(), glApplicationFieldProvider.getSelectedFrequency());
            OptionsDialogActions.m_resolutionIsDirty = false;
        }
        closeOptionsDialog(e);
    }
    
    public static void setBindText(final Event e, final TextEditor textEditor, final ShortcutFieldProvider shortcutFieldProvider) {
        if (e instanceof FocusChangedEvent && ((FocusChangedEvent)e).getFocused()) {
            OptionsDialogActions.m_binding = true;
            return;
        }
        if ((e instanceof FocusChangedEvent && !((FocusChangedEvent)e).getFocused()) || (e instanceof KeyEvent && ((KeyEvent)e).getKeyCode() == 10)) {
            final String text = textEditor.getText();
            if (text == null) {
                Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("notification.emptyField"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 4099L, 102, 1);
                return;
            }
            if (!text.equals(shortcutFieldProvider.getText())) {
                final UIBindShortcutMessage uiBindShortcutMessage = new UIBindShortcutMessage(shortcutFieldProvider);
                uiBindShortcutMessage.setStringValue(text);
                uiBindShortcutMessage.setId(17732);
                Worker.getInstance().pushMessage(uiBindShortcutMessage);
            }
            FocusManager.getInstance().setFocused(null);
            OptionsDialogActions.m_binding = false;
        }
    }
    
    public static void onRegionChanged(final ListSelectionChangedEvent e, final RegionsView regions) {
        if (e.getSelected()) {
            final AbstractUIMessage message = new UIMessage((short)17740);
            message.setObjectValue(new ObjectPair(regions, e.getValue()));
            Worker.getInstance().pushMessage(message);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)OptionsDialogActions.class);
        OptionsDialogActions.m_fontOrdinal = -1;
        OptionsDialogActions.m_optionActions = new TIntObjectHashMap<OptionAction>();
        OptionsDialogActions.m_previousIndex = -1;
        OptionsDialogActions.m_testSource = null;
    }
    
    public static class OptionActionImpl implements OptionAction
    {
        KeyInterface m_keyInterface;
        
        @Override
        public void valid() {
        }
        
        @Override
        public void cancel() {
        }
    }
    
    public interface OptionAction
    {
        void valid();
        
        void cancel();
    }
}
