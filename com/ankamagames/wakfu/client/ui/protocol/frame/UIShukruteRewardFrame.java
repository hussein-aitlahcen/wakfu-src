package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.core.dialogclose.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;

public class UIShukruteRewardFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UIShukruteRewardFrame m_instance;
    private static final int DURATION = 20000;
    private ShukruteReward m_reward;
    private Widget m_dialog;
    private Runnable m_closeRunnable;
    private DialogCloseRequestListener m_dialogCloseRequestListener;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIShukruteRewardFrame getInstance() {
        return UIShukruteRewardFrame.m_instance;
    }
    
    public void loadShukruteRewards(final ShukruteReward reward) {
        this.m_reward = reward;
        if (!WakfuGameEntity.getInstance().hasFrame(this)) {
            WakfuGameEntity.getInstance().pushFrame(this);
        }
        else {
            PropertiesProvider.getInstance().setLocalPropertyValue("shukruteReward", this.m_reward, "shukruteRewardDialog");
        }
    }
    
    public void addReward(final ShukruteRewardItem rewardItem) {
        this.m_reward.addReward(rewardItem);
    }
    
    public void closeDialog() {
        if (this.m_closeRunnable != null) {
            ProcessScheduler.getInstance().remove(this.m_closeRunnable);
            this.m_closeRunnable = null;
        }
        this.m_dialog.removeTweensOfType(PositionTween.class);
        this.m_dialog.addTween(new PositionTween(this.m_dialog.getX(), this.m_dialog.getY(), this.m_dialog.getX(), MasterRootContainer.getInstance().getHeight(), this.m_dialog, 0, 1000, TweenFunction.PROGRESSIVE));
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                WakfuGameEntity.getInstance().removeFrame(UIShukruteRewardFrame.this);
            }
        }, 1000L, 1);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        message.getId();
        return true;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("shukruteRewardDialog")) {
                        UIShukruteRewardFrame.this.closeDialog();
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            this.m_dialogCloseRequestListener = new DialogCloseRequestListener() {
                @Override
                public int onDialogCloseRequest(final String id) {
                    if (id.equals("shukruteRewardDialog")) {
                        UIShukruteRewardFrame.this.closeDialog();
                        return 2;
                    }
                    return 0;
                }
            };
            DialogClosesManager.getInstance().addDialogCloseRequestListener(this.m_dialogCloseRequestListener);
            (this.m_dialog = (Widget)Xulor.getInstance().load("shukruteRewardDialog", Dialogs.getDialogPath("shukruteRewardDialog"), 1L, (short)10000)).setVisible(false);
            this.m_dialog.setY(MasterRootContainer.getInstance().getHeight());
            PropertiesProvider.getInstance().setLocalPropertyValue("shukruteReward", this.m_reward, "shukruteRewardDialog");
            ProcessScheduler.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    UIShukruteRewardFrame.this.m_dialog.setX(Alignment9.CENTER.getX(UIShukruteRewardFrame.this.m_dialog.getWidth(), MasterRootContainer.getInstance().getWidth()));
                    UIShukruteRewardFrame.this.m_dialog.removeTweensOfType(PositionTween.class);
                    UIShukruteRewardFrame.this.m_dialog.addTween(new PositionTween(UIShukruteRewardFrame.this.m_dialog.getX(), UIShukruteRewardFrame.this.m_dialog.getY(), UIShukruteRewardFrame.this.m_dialog.getX(), MasterRootContainer.getInstance().getHeight() - UIShukruteRewardFrame.this.m_dialog.getHeight() - 50, UIShukruteRewardFrame.this.m_dialog, 0, 1000, TweenFunction.PROGRESSIVE));
                    UIShukruteRewardFrame.this.m_dialog.setVisible(true);
                }
            }, 1000L, 1);
            this.m_closeRunnable = new Runnable() {
                @Override
                public void run() {
                    UIShukruteRewardFrame.this.closeDialog();
                }
            };
            ProcessScheduler.getInstance().schedule(this.m_closeRunnable, 20000L, 1);
            Xulor.getInstance().putActionClass("wakfu.shukrute", ShukruteActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            DialogClosesManager.getInstance().removeDialogCloseRequestListener(this.m_dialogCloseRequestListener);
            Xulor.getInstance().unload("shukruteRewardDialog");
            Xulor.getInstance().removeActionClass("wakfu.shukrute");
            if (this.m_closeRunnable != null) {
                ProcessScheduler.getInstance().remove(this.m_closeRunnable);
                this.m_closeRunnable = null;
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIShukruteRewardFrame.class);
        UIShukruteRewardFrame.m_instance = new UIShukruteRewardFrame();
    }
    
    public static class ShukruteReward extends ImmutableFieldProvider
    {
        public static final String DESCRIPTION = "description";
        public static final String FIRST_RANK_REWARD = "firstRankReward";
        public static final String SECOND_RANK_REWARD = "secondRankReward";
        public static final String THIRD_RANK_REWARD = "thirdRankReward";
        private final ArrayList<ShukruteRewardItem> m_rewardItems;
        private String m_description;
        
        public ShukruteReward(final String description) {
            super();
            this.m_rewardItems = new ArrayList<ShukruteRewardItem>();
            this.m_description = description;
        }
        
        @Override
        public String[] getFields() {
            return null;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("description")) {
                return this.m_description;
            }
            if (fieldName.equals("firstRankReward")) {
                if (this.m_rewardItems.size() >= 1) {
                    return this.m_rewardItems.get(0);
                }
            }
            else if (fieldName.equals("secondRankReward")) {
                if (this.m_rewardItems.size() >= 2) {
                    return this.m_rewardItems.get(1);
                }
            }
            else if (fieldName.equals("thirdRankReward") && this.m_rewardItems.size() >= 3) {
                return this.m_rewardItems.get(2);
            }
            return null;
        }
        
        public void addReward(final ShukruteRewardItem rewardItem) {
            this.m_rewardItems.add(rewardItem);
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "firstRankReward", "secondRankReward", "thirdRankReward");
        }
    }
    
    public static class ShukruteRewardItem extends ImmutableFieldProvider
    {
        public static final String ITEM = "item";
        public static final String TEXT = "text";
        private ReferenceItem m_item;
        private String m_text;
        
        public ShukruteRewardItem(final int refId, final String text) {
            super();
            this.m_item = ReferenceItemManager.getInstance().getReferenceItem(refId);
            this.m_text = text;
        }
        
        @Override
        public String[] getFields() {
            return null;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("item")) {
                return this.m_item;
            }
            if (fieldName.equals("text")) {
                return this.m_text;
            }
            return null;
        }
    }
}
