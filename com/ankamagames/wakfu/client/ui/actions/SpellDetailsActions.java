package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.component.text.builder.content.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.fight.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.component.text.document.part.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.effectWriter.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

@XulorActionsTag
public class SpellDetailsActions
{
    public static final String PACKAGE = "wakfu.spellDetails";
    
    public static void setSpellLevel(final Event event) {
        if (event instanceof SliderMovedEvent) {
            short levelValue = (short)((SliderMovedEvent)event).getValue();
            if (levelValue > SpellXpTable.getInstance().getMaxLevel()) {
                levelValue = SpellXpTable.getInstance().getMaxLevel();
            }
            final ElementMap map = event.getTarget().getElementMap();
            if (map == null) {
                return;
            }
            setSpellLevel(levelValue, map);
        }
    }
    
    public static void keyType(final Event event, final TextEditor te) {
        if (te.getText().length() == 0) {
            return;
        }
        short levelValue = PrimitiveConverter.getShort(te.getText());
        if (levelValue > SpellXpTable.getInstance().getMaxLevel()) {
            levelValue = SpellXpTable.getInstance().getMaxLevel();
        }
        final ElementMap map = event.getTarget().getElementMap();
        if (map == null) {
            return;
        }
        setSpellLevel(levelValue, map);
    }
    
    public static void processText(final Event event, final Widget target, final PopupElement popup) {
        final TextView textView = event.getTarget();
        final AbstractContentBlock block = textView.getBlockUnderMouse();
        if (block != null && block.getType() == AbstractContentBlock.BlockType.TEXT) {
            final AbstractDocumentPart part = block.getDocumentPart();
            if (part == null) {
                return;
            }
            if (part.getType() == DocumentPartType.TEXT) {
                final String partId = ((TextDocumentPart)part).getId();
                if (partId != null && partId.length() > 0) {
                    final String[] parts = partId.split("_");
                    if (parts.length == 2) {
                        final String type = parts[0];
                        final String id = parts[1];
                        if (type == null || type.length() == 0) {
                            return;
                        }
                        if (type.equals("state")) {
                            final int stateData = Integer.parseInt(id);
                            final short stateBaseId = MathHelper.getFirstShortFromInt(stateData);
                            final short stateLevel = MathHelper.getSecondShortFromInt(stateData);
                            StateClient state = (StateClient)StateManager.getInstance().getState(stateBaseId);
                            if (state.getLevel() != stateLevel) {
                                state = state.instanceAnother(stateLevel);
                            }
                            if (event.getType() == Events.MOUSE_CLICKED) {
                                final UIStateMessage uiStateMessage = new UIStateMessage();
                                uiStateMessage.setState(state);
                                if (target.getElementMap() != null) {
                                    final SpellLevel spellLevel = (SpellLevel)PropertiesProvider.getInstance().getObjectProperty("editableDescribedSpell", target.getElementMap().getId());
                                    if (spellLevel != null) {
                                        uiStateMessage.setIntValue(spellLevel.getSpell().getId());
                                    }
                                }
                                uiStateMessage.setStringValue(target.getElementMap().getId());
                                Worker.getInstance().pushMessage(uiStateMessage);
                            }
                            else {
                                PropertiesProvider.getInstance().setPropertyValue("describedState", state);
                                XulorActions.popup(popup, target);
                                target.addEventListener(Events.POPUP_HIDE, new EventListener() {
                                    @Override
                                    public boolean run(final Event event) {
                                        PropertiesProvider.getInstance().setPropertyValue("describedState", null);
                                        target.removeEventListener(Events.POPUP_HIDE, this, true);
                                        return false;
                                    }
                                }, true);
                            }
                        }
                        else if (type.equals("glyph")) {
                            final int glyphData = Integer.parseInt(id);
                            final short glyphBaseId = MathHelper.getFirstShortFromInt(glyphData);
                            final short glyphLevel = MathHelper.getSecondShortFromInt(glyphData);
                            final AbstractEffectArea glyph = StaticEffectAreaManager.getInstance().getAreaFromId(glyphBaseId);
                            final EffectAreaFieldProvider glyphView = new EffectAreaFieldProvider(glyph, glyphLevel);
                            if (event.getType() == Events.MOUSE_CLICKED) {
                                final UIEffectAreaMessage uiEffectAreaMessage = new UIEffectAreaMessage(glyphView);
                                if (target.getElementMap() != null) {
                                    final SpellLevel spellLevel2 = (SpellLevel)PropertiesProvider.getInstance().getObjectProperty("editableDescribedSpell", target.getElementMap().getId());
                                    if (spellLevel2 != null) {
                                        uiEffectAreaMessage.setIntValue(spellLevel2.getSpell().getId());
                                    }
                                }
                                uiEffectAreaMessage.setStringValue(target.getElementMap().getId());
                                Worker.getInstance().pushMessage(uiEffectAreaMessage);
                            }
                            else {
                                PropertiesProvider.getInstance().setPropertyValue("describedState", glyphView);
                                XulorActions.popup(popup, target);
                                target.addEventListener(Events.POPUP_HIDE, new EventListener() {
                                    @Override
                                    public boolean run(final Event event) {
                                        PropertiesProvider.getInstance().setPropertyValue("describedState", null);
                                        target.removeEventListener(Events.POPUP_HIDE, this, true);
                                        return false;
                                    }
                                }, true);
                            }
                        }
                        else if (type.equals("spell")) {
                            final int spellData = Integer.parseInt(id);
                            final Spell spell = SpellManager.getInstance().getSpell(spellData);
                            if (spell == null) {
                                return;
                            }
                            if (event.getType() == Events.MOUSE_CLICKED) {
                                final SpellLevel spellLevel3 = new SpellLevel(spell, (short)0, -1L);
                                CompanionsEmbeddedActions.openSpellDescription(3, spellLevel3, target.getElementMap().getId());
                            }
                        }
                    }
                }
            }
        }
        if (block != null && block.getType() == AbstractContentBlock.BlockType.IMAGE) {
            final AbstractDocumentPart part = block.getDocumentPart();
            if (part == null) {
                return;
            }
            if (part.getType() == DocumentPartType.IMAGE) {
                PropertiesProvider.getInstance().setPropertyValue("describedState", null);
                final String popupTranslatorKey = ((ImageDocumentPart)part).getPopupTranslatorKey();
                if (popupTranslatorKey == null) {
                    return;
                }
                final String[] translation = popupTranslatorKey.split("-");
                final String translatorKey = translation[0];
                String text;
                if (translation.length > 1) {
                    final String[] params = translation[1].split(",");
                    text = WakfuTranslator.getInstance().getString(translatorKey, (Object[])params);
                }
                else {
                    text = WakfuTranslator.getInstance().getString(translatorKey);
                }
                PropertiesProvider.getInstance().setPropertyValue("describedIcon", text);
                XulorActions.popup(popup, textView);
                target.addEventListener(Events.POPUP_HIDE, new EventListener() {
                    @Override
                    public boolean run(final Event event) {
                        PropertiesProvider.getInstance().setPropertyValue("describedIcon", null);
                        target.removeEventListener(Events.POPUP_HIDE, this, true);
                        return false;
                    }
                }, true);
            }
        }
    }
    
    public static void restore(final Event event) {
        final ElementMap map = event.getCurrentTarget().getElementMap();
        if (map == null) {
            return;
        }
        final SpellLevel spellLevel = (SpellLevel)PropertiesProvider.getInstance().getObjectProperty("describedSpell", map.getId());
        setSpellLevel(spellLevel.getLevel(), map);
    }
    
    private static void setSpellLevel(final short level, final ElementMap map) {
        final SpellLevel spellLevel = (SpellLevel)PropertiesProvider.getInstance().getObjectProperty("editableDescribedSpell", map.getId());
        if (spellLevel == null) {
            return;
        }
        spellLevel.setLevel(level, false);
        final TabbedContainer tc = (TabbedContainer)map.getElement("tabbedContainer");
        if (tc == null) {
            return;
        }
        final boolean standartEffect = tc.getSelectedTabIndex() == 0;
        PropertiesProvider.getInstance().firePropertyValueChanged(spellLevel, "level", "levelTextShort", "ap", "castInterval", "wp", "chrage", "mp", "castOnlyInLine", "range", "areaOfEffectIconURL", "conditionsDescription", "criticalDescription", "shortDescription", "testLineOfSight", "modifiableRange");
        recursiveEffectGroupStateUpdate(spellLevel, spellLevel, standartEffect);
    }
    
    private static void recursiveEffectGroupStateUpdate(final EffectContainer effectContainer, final SpellLevel spellLevel, final boolean standartEffect) {
        for (final WakfuEffect effect : effectContainer) {
            final boolean criticalEffect = effect.checkFlags(1L);
            if (!criticalEffect || !standartEffect) {
                if (!criticalEffect && !standartEffect) {
                    continue;
                }
                if (spellLevel.getLevel() > effect.getContainerMaxLevel()) {
                    continue;
                }
                if (spellLevel.getLevel() < effect.getContainerMinLevel()) {
                    continue;
                }
                final int effectParamsCount = effect.getParamsCount();
                final Object[] params = new Object[effectParamsCount];
                for (int j = 0; j < effectParamsCount; ++j) {
                    params[j] = effect.getParam(j, spellLevel.getLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
                }
                if (RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.contains(effect.getActionId())) {
                    recursiveEffectGroupStateUpdate((EffectContainer)AbstractEffectGroupManager.getInstance().getEffectGroup(effect.getEffectId()), spellLevel, standartEffect);
                }
                else if (effect.getActionId() == RunningEffectConstants.STATE_APPLY.getId() || effect.getActionId() == RunningEffectConstants.APPLY_DEATHTAG.getId() || effect.getActionId() == RunningEffectConstants.APPLY_STATE_PERCENT_FUNCTION_AREA_HP.getId() || effect.getActionId() == RunningEffectConstants.APPLY_STATE_FOR_FECA_ARMOR.getId() || effect.getActionId() == RunningEffectConstants.APPLY_STATE_CAPED_BY_ANOTHER_STATE.getId()) {
                    UIStateDetailFrame.getInstance().updateEffectForSpell(spellLevel.getSpell().getId(), effect.getParam(0, spellLevel.getLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL), (short)effect.getParam(1, spellLevel.getLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL));
                }
                else {
                    final BasicEffectArea<WakfuEffect, EffectAreaParameters> aura = EffectAreaEffectWriter.getEffectArea(effect.getActionId(), params);
                    if (aura == null) {
                        continue;
                    }
                    recursiveEffectGroupStateUpdate(aura, spellLevel, standartEffect);
                }
            }
        }
    }
}
