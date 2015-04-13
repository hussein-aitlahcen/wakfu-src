package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.game.chaos.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class GenericActivableParameter extends IEParameter
{
    public static final String GENERIC_VAR_NAME = "elementId";
    public static final String USER_VAR_NAME = "userId";
    private final Visual[] m_visuals;
    
    public GenericActivableParameter(final int paramId, final ChaosInteractiveCategory chaosCategory, final int chaosCollectorParamId, final Visual[] visuals) {
        super(paramId, 0, chaosCategory, chaosCollectorParamId);
        this.m_visuals = visuals;
    }
    
    public Visual[] getVisuals() {
        return this.m_visuals;
    }
    
    private short getTotalActionCount() {
        int count = 0;
        for (final Visual v : this.m_visuals) {
            count += v.getGroupActionCount();
        }
        return (short)count;
    }
    
    public GroupAction getGroupAction(final short actionIndex) {
        for (final Visual v : this.m_visuals) {
            for (final GroupAction group : v.m_groupActions) {
                if (group.getGroupIndex() == actionIndex) {
                    return group;
                }
            }
        }
        return null;
    }
    
    public Visual getVisual(final short actionIndex) {
        for (final Visual v : this.m_visuals) {
            for (final GroupAction group : v.m_groupActions) {
                if (group.getGroupIndex() == actionIndex) {
                    return v;
                }
            }
        }
        return null;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.valueOf((short)0);
    }
    
    public InteractiveElementAction[] getInteractiveUsableActions() {
        final InteractiveElementAction[] actions = new InteractiveElementAction[this.getTotalActionCount()];
        System.arraycopy(InteractiveElementAction.values(), 0, actions, 0, actions.length);
        return actions;
    }
    
    @Override
    public String toString() {
        return "GenericActivableParameter{} " + super.toString();
    }
    
    public static class Action
    {
        private final int m_actionId;
        private final int m_actionTypeId;
        private final String m_criteria;
        private final String[] m_actionParams;
        
        public Action(final int actionId, final int actionTypeId, final String criteria, final String[] actionParams) {
            super();
            this.m_actionId = actionId;
            this.m_actionTypeId = actionTypeId;
            this.m_criteria = criteria;
            this.m_actionParams = actionParams;
        }
        
        public int getActionId() {
            return this.m_actionId;
        }
        
        public int getActionTypeId() {
            return this.m_actionTypeId;
        }
        
        public String getCriteria() {
            return this.m_criteria;
        }
        
        public String[] getActionParams() {
            return this.m_actionParams;
        }
        
        public GenericActionConstants getActionConstant() {
            return GenericActionConstants.getFromId(this.getActionTypeId());
        }
    }
    
    public static class GroupAction
    {
        private final int m_actionId;
        private final int m_groupIndex;
        private final float m_weight;
        private final String m_criteria;
        private final Action[] m_actions;
        
        public GroupAction(final int actionId, final int groupIndex, final float weight, final String criteria, final Action[] actions) {
            super();
            this.m_actionId = actionId;
            this.m_groupIndex = groupIndex;
            this.m_weight = weight;
            this.m_criteria = criteria;
            this.m_actions = actions;
        }
        
        public int getActionId() {
            return this.m_actionId;
        }
        
        public int getGroupIndex() {
            return this.m_groupIndex;
        }
        
        public float getWeight() {
            return this.m_weight;
        }
        
        public String getCriteria() {
            return this.m_criteria;
        }
        
        public Action[] getActions() {
            return this.m_actions;
        }
    }
    
    public static class Visual
    {
        private final int m_id;
        private final int m_visualId;
        private final int m_itemConsumed;
        private final int m_quantity;
        private final boolean m_consumeItem;
        private final int m_kamaCost;
        private final GroupAction[] m_groupActions;
        private final int m_distributionDuration;
        
        public Visual(final int id, final int visualId, final int itemConsumed, final int quantity, final boolean consumeItem, final int kamaCost, final GroupAction[] groupActions, final int distributionDuration) {
            super();
            this.m_id = id;
            this.m_visualId = visualId;
            this.m_itemConsumed = itemConsumed;
            this.m_quantity = quantity;
            this.m_consumeItem = consumeItem;
            this.m_kamaCost = kamaCost;
            this.m_groupActions = groupActions;
            this.m_distributionDuration = distributionDuration;
        }
        
        public int getId() {
            return this.m_id;
        }
        
        public int getItemConsumed() {
            return this.m_itemConsumed;
        }
        
        public int getQuantity() {
            return this.m_quantity;
        }
        
        public boolean isConsumeItem() {
            return this.m_consumeItem;
        }
        
        public int getKamaCost() {
            return this.m_kamaCost;
        }
        
        public int getVisualId() {
            return this.m_visualId;
        }
        
        public GroupAction[] getGroupActions() {
            return this.m_groupActions;
        }
        
        public GroupAction getRandomGroupAction() {
            assert this.m_groupActions.length > 0;
            float w = 0.0f;
            final float rand = MathHelper.random(0.0f, this.getTotalWeight());
            for (int i = 0, size = this.m_groupActions.length; i < size; ++i) {
                w += this.m_groupActions[i].getWeight();
                if (rand <= w) {
                    return this.m_groupActions[i];
                }
            }
            return this.m_groupActions[this.m_groupActions.length - 1];
        }
        
        public short getRandomGroupActionIndex() {
            return (short)this.getRandomGroupAction().getGroupIndex();
        }
        
        private float getTotalWeight() {
            float total = 0.0f;
            for (final GroupAction action : this.m_groupActions) {
                total += action.getWeight();
            }
            return total;
        }
        
        public int getDistributionDuration() {
            return this.m_distributionDuration;
        }
        
        public int getGroupActionCount() {
            return this.m_groupActions.length;
        }
        
        public boolean executeOnSpawn() {
            return this.m_visualId == 0;
        }
    }
}
