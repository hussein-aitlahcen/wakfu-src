package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.framework.sound.openAL.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.sound.*;
import java.util.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class UIBlindBoxFrame implements MessageFrame
{
    private static final Logger m_logger;
    private static final UIBlindBoxFrame m_instance;
    public static final int ROLL_DELAY = 2000;
    private DialogUnloadListener m_dialogUnloadListener;
    private BlindBoxView m_blindBoxView;
    private ArrayList<RollingListRunnable> m_rollingListRunnables;
    private static final int UPDATE_TIME = 25;
    private AudioSource m_rollSound;
    private GiveRandomItemInListItemAction m_itemAction;
    private long m_itemId;
    
    public UIBlindBoxFrame() {
        super();
        this.m_rollingListRunnables = new ArrayList<RollingListRunnable>();
    }
    
    public static UIBlindBoxFrame getInstance() {
        return UIBlindBoxFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
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
            if (this.m_blindBoxView == null || this.m_itemAction == null) {
                return;
            }
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("blindBoxDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIBlindBoxFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().putActionClass("wakfu.blindBox", BlindBoxDialogActions.class);
            final Window window = (Window)Xulor.getInstance().load("blindBoxDialog", Dialogs.getDialogPath("blindBoxDialog"), 8448L, (short)10005);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("blindBoxDialog");
            final List rollingItemList1 = (List)map.getElement("rollingItemList1");
            rollingItemList1.addListContentListener(new EditableRenderableCollection.CollectionContentLoadedListener() {
                @Override
                public void onContentLoaded() {
                    UIBlindBoxFrame.this.startRoll();
                    rollingItemList1.removeListContentLoadListener(this);
                }
            });
            this.m_rollingListRunnables.add(new RollingListRunnable(rollingItemList1));
            this.m_rollingListRunnables.add(new RollingListRunnable((List)map.getElement("rollingItemList2")));
            this.m_rollingListRunnables.add(new RollingListRunnable((List)map.getElement("rollingItemList3")));
            PropertiesProvider.getInstance().setPropertyValue("blindBox", this.m_blindBoxView);
        }
    }
    
    private void startRoll() {
        for (final RollingListRunnable rlr : this.m_rollingListRunnables) {
            rlr.scrollTo(false, 0.9f);
        }
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                UIBlindBoxFrame.this.m_itemAction.delayedRequest(UIBlindBoxFrame.this.m_itemId);
            }
        }, 2000L, 1);
        this.m_rollSound = WakfuSoundManager.getInstance().playGUISound(600152L, true);
        if (this.m_rollSound != null) {
            this.m_rollSound.setGain(0.0f);
            this.m_rollSound.fade(1.0f, 100.0f);
        }
    }
    
    private void stopRoll(final short index) {
        for (final RollingListRunnable rlr : this.m_rollingListRunnables) {
            rlr.scrollTo(index, false, 0.9f);
        }
    }
    
    private int setListOffset(final List list, final float offset) {
        if (list == null) {
            return -1;
        }
        list.setListOffset(offset);
        this.m_blindBoxView.setCurrentOffset(offset);
        return -1;
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            if (this.m_rollSound != null) {
                this.m_rollSound.setStopOnNullGain(true);
                this.m_rollSound.fade(0.0f, 100.0f);
                this.m_rollSound = null;
            }
            for (final RollingListRunnable rlr : this.m_rollingListRunnables) {
                ProcessScheduler.getInstance().remove(rlr);
            }
            this.m_rollingListRunnables.clear();
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().removeProperty("blindBox");
            Xulor.getInstance().unload("blindBoxDialog");
            Xulor.getInstance().removeActionClass("wakfu.blindBox");
        }
    }
    
    public void setBlindBoxView(final BlindBoxView blindBoxView) {
        this.m_blindBoxView = blindBoxView;
    }
    
    private ArrayList<FakeItem> getItemsForLootList(final int lootListId) {
        final ArrayList<FakeItem> items = new ArrayList<FakeItem>();
        try {
            final LightLootListBinaryData data = new LightLootListBinaryData();
            if (BinaryDocumentManager.getInstance().getId(lootListId, data)) {
                for (final LightLootListBinaryData.LootEntry entry : data.getEntries()) {
                    final int itemId = entry.getItemId();
                    final FakeItem fakeItem = new FakeItem(ReferenceItemManager.getInstance().getReferenceItem(itemId));
                    fakeItem.setQuantity(entry.getQuantity());
                    items.add(fakeItem);
                }
            }
        }
        catch (Exception e) {
            UIBlindBoxFrame.m_logger.error((Object)("Priobl\u00e8me de r\u00e9cup\u00e9ration des info pour la loot List id=" + lootListId));
        }
        return items;
    }
    
    public void onRandomItemResult(final int referenceId) {
        final int index = this.m_blindBoxView.indexOf(referenceId);
        this.stopRoll((short)index);
    }
    
    public void initialize(final GiveRandomItemInListItemAction giveRandomItemInListItemAction, final Item item) {
        this.m_itemId = item.getUniqueId();
        this.m_itemAction = giveRandomItemInListItemAction;
        this.m_blindBoxView = new BlindBoxView(item.getName(), this.getItemsForLootList(giveRandomItemInListItemAction.getLootListId()));
    }
    
    public byte countFinishedRoll() {
        byte count = 0;
        for (final RollingListRunnable rlr : this.m_rollingListRunnables) {
            if (rlr.isFinished()) {
                ++count;
            }
        }
        return count;
    }
    
    private void checkAllFinished() {
        int soundId = -1;
        final byte count = this.countFinishedRoll();
        switch (count) {
            case 1: {
                soundId = 600153;
                break;
            }
            case 2: {
                soundId = 600154;
                break;
            }
            case 3: {
                soundId = 600155;
                break;
            }
            default: {
                return;
            }
        }
        WakfuSoundManager.getInstance().playGUISound(soundId);
        if (count < 3) {
            return;
        }
        this.m_blindBoxView.setFinished(true);
        WakfuSoundManager.getInstance().playGUISound(600156L);
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("blindBoxDialog");
        final Container cont = (Container)map.getElement("containerParticle");
        final ParticleDecorator particleDecorator = new ParticleDecorator();
        particleDecorator.onCheckOut();
        particleDecorator.setUseParentScissor(true);
        particleDecorator.setFile("6001051.xps");
        particleDecorator.setAlignment(Alignment9.CENTER);
        cont.getAppearance().add(particleDecorator);
        if (this.m_rollSound != null) {
            this.m_rollSound.setStopOnNullGain(true);
            this.m_rollSound.fade(0.0f, 100.0f);
            this.m_rollSound = null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIBlindBoxFrame.class);
        m_instance = new UIBlindBoxFrame();
    }
    
    private class RollingListRunnable implements Runnable
    {
        private List m_rollingItemList;
        private boolean m_forward;
        private float m_movementSpeed;
        private float m_startOffset;
        private float m_count;
        private boolean m_finished;
        
        public RollingListRunnable(final List rollingItemList) {
            super();
            this.m_rollingItemList = rollingItemList;
        }
        
        @Override
        public void run() {
            final float offset = this.m_rollingItemList.getOffset();
            final float inc = this.m_forward ? this.m_movementSpeed : (-this.m_movementSpeed);
            if (this.m_count != 32767.0f) {
                boolean end;
                if (this.m_forward) {
                    end = (this.m_rollingItemList.getOffset() - this.m_startOffset + inc >= this.m_count);
                }
                else {
                    end = (this.m_rollingItemList.getOffset() - this.m_startOffset + inc <= this.m_count);
                }
                if (end) {
                    this.tryStop(offset, inc, this.m_forward);
                    return;
                }
            }
            UIBlindBoxFrame.this.setListOffset(this.m_rollingItemList, offset + inc);
        }
        
        private void scrollTo(final short targetedIndex, final boolean forward, final float movementSpeed) {
            this.m_finished = false;
            this.m_startOffset = ((targetedIndex == 32767) ? (-(float)(Math.random() * UIBlindBoxFrame.this.m_blindBoxView.lootListSize())) : ((float)Math.floor(this.m_rollingItemList.getOffset())));
            UIBlindBoxFrame.this.setListOffset(this.m_rollingItemList, this.m_startOffset);
            if (targetedIndex == 32767) {
                this.m_count = targetedIndex;
            }
            else {
                final int size = this.m_rollingItemList.size();
                if (size - targetedIndex > Math.abs(this.m_startOffset) % size) {
                    if (forward) {
                        this.m_count = MathHelper.fastFloor(targetedIndex - this.m_startOffset % size);
                    }
                    else {
                        this.m_count = -MathHelper.fastFloor(size - targetedIndex - Math.abs(this.m_startOffset % size));
                    }
                }
                else if (forward) {
                    this.m_count = MathHelper.fastFloor(size - this.m_startOffset % size + targetedIndex);
                }
                else {
                    this.m_count = -MathHelper.fastFloor(size + size - targetedIndex - Math.abs(this.m_startOffset) % size);
                }
            }
            this.m_forward = forward;
            this.m_movementSpeed = movementSpeed;
            ProcessScheduler.getInstance().remove(this);
            ProcessScheduler.getInstance().schedule(this, 25L);
        }
        
        private boolean tryStop(final float offset, final float inc, final boolean forward) {
            int bound;
            boolean stop;
            if (forward) {
                bound = MathHelper.fastCeil(offset);
                stop = (offset + inc >= bound);
            }
            else {
                bound = MathHelper.fastFloor(offset);
                stop = (offset + inc <= bound);
            }
            if (stop) {
                UIBlindBoxFrame.this.setListOffset(this.m_rollingItemList, bound);
                ProcessScheduler.getInstance().remove(this);
                this.m_finished = true;
                UIBlindBoxFrame.this.checkAllFinished();
                return true;
            }
            return false;
        }
        
        public boolean isFinished() {
            return this.m_finished;
        }
        
        public void scrollTo(final boolean forward, final float movementSpeed) {
            this.scrollTo((short)32767, forward, movementSpeed);
        }
    }
}
