package com.ankamagames.wakfu.common.game.descriptionGenerator.effectWriter;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.spell.*;
import java.util.regex.*;

public class StateEffectWriter extends DefaultEffectWriter
{
    @Override
    protected String formatEffectParams(final WakfuEffect effect, String effectText, final Object[] params, final boolean customFilled, final ContainerWriter writer) {
        short stateLevel = 0;
        State state = null;
        if (effect.getActionId() == CastableDescriptionGenerator.STATE_ACTION_ID || effect.getActionId() == CastableDescriptionGenerator.STATE_APPLICATION_BONUS_ACTION_ID || effect.getActionId() == CastableDescriptionGenerator.STATE_RESIST_ACTION_ID || effect.getActionId() == CastableDescriptionGenerator.APPLY_STATE_PERCENT_FUNCTION_AREA_HP_ID || effect.getActionId() == CastableDescriptionGenerator.UNAPPLY_STATE_ACTION_ID) {
            final State currentState = StateManager.getInstance().getState(((Number)params[0]).intValue());
            if (currentState != null) {
                if (effect.getActionId() == CastableDescriptionGenerator.STATE_ACTION_ID || effect.getActionId() == CastableDescriptionGenerator.APPLY_STATE_PERCENT_FUNCTION_AREA_HP_ID) {
                    stateLevel = ((Number)params[1]).shortValue();
                    state = currentState;
                    params[0] = CastableDescriptionGenerator.getStateNameLink(currentState, stateLevel, currentState.getMaxlevel(), true);
                }
                else if (effect.getActionId() == CastableDescriptionGenerator.STATE_APPLICATION_BONUS_ACTION_ID || effect.getActionId() == CastableDescriptionGenerator.STATE_RESIST_ACTION_ID) {
                    stateLevel = 1;
                    state = currentState;
                    params[0] = CastableDescriptionGenerator.getStateNameLink(currentState, stateLevel, (short)1, false);
                }
                else {
                    params[0] = CastableDescriptionGenerator.getStateNameLink(currentState, stateLevel, (short)1, false);
                }
            }
        }
        else if (effect.getActionId() == CastableDescriptionGenerator.APPLY_DEATHTAG_ACTION_ID) {
            final State currentState = StateManager.getInstance().getState(((Number)params[0]).intValue());
            stateLevel = ((Number)params[1]).shortValue();
            state = currentState;
            params[0] = CastableDescriptionGenerator.getStateNameLink(currentState, stateLevel, (short)effect.getContainerMaxLevel(), true);
        }
        else if (effect.getActionId() == CastableDescriptionGenerator.APPLY_FECA_ARMOR_ACTION_ID) {
            final State currentState = StateManager.getInstance().getState(((Number)params[0]).intValue());
            stateLevel = ((Number)params[1]).shortValue();
            state = currentState;
            params[0] = CastableDescriptionGenerator.getStateNameLink(currentState, stateLevel, (short)effect.getContainerMaxLevel(), true);
        }
        else if (effect.getActionId() == CastableDescriptionGenerator.BLITZKRIEK_ACTION_ID && params.length == 3) {
            state = StateManager.getInstance().getState(((Number)params[1]).intValue());
            if (state != null) {
                stateLevel = ((Number)params[2]).shortValue();
                params[1] = CastableDescriptionGenerator.getStateNameLink(state, stateLevel, state.getMaxlevel(), true);
            }
        }
        if (state != null && (effect.getActionId() == CastableDescriptionGenerator.STATE_ACTION_ID || effect.getActionId() == CastableDescriptionGenerator.APPLY_DEATHTAG_ACTION_ID || effect.getActionId() == CastableDescriptionGenerator.APPLY_STATE_PERCENT_FUNCTION_AREA_HP_ID || effect.getActionId() == CastableDescriptionGenerator.APPLY_FECA_ARMOR_ACTION_ID)) {
            Matcher liMatcher = CastableDescriptionGenerator.LISTED_CONTAINER_PATTERN.matcher(effectText);
            while (liMatcher.find()) {
                final String value = liMatcher.group(1);
                final char charAt0 = value.charAt(0);
                switch (charAt0) {
                    case 'l': {
                        final char charAt = value.charAt(1);
                        switch (charAt) {
                            case 'i': {
                                final String subDesc = CastableDescriptionGenerator.describeSubContainerDescription(state, stateLevel, false, 0, false, false);
                                if (subDesc != null) {
                                    effectText = effectText.substring(0, liMatcher.start()) + subDesc + effectText.substring(liMatcher.end());
                                    liMatcher = CastableDescriptionGenerator.LISTED_CONTAINER_PATTERN.matcher(effectText.trim());
                                    continue;
                                }
                                continue;
                            }
                            case 'p': {
                                final String subDesc = CastableDescriptionGenerator.describeSubContainerDescription(state, stateLevel, false, 0, false, true);
                                if (subDesc != null) {
                                    effectText = effectText.substring(0, liMatcher.start()) + subDesc + effectText.substring(liMatcher.end());
                                    liMatcher = CastableDescriptionGenerator.LISTED_CONTAINER_PATTERN.matcher(effectText.trim());
                                    continue;
                                }
                                continue;
                            }
                        }
                        continue;
                    }
                }
            }
        }
        if (effect.isUsableInWorld() && !effect.isUsableInFight()) {
            final int duration = state.getMsDuration();
            if (duration != -1000) {
                effectText = effectText + '(' + TimeUtils.getLongDescription(GameInterval.fromSeconds(duration / 1000)) + ')';
            }
        }
        return StringFormatter.format(effectText, params);
    }
}
