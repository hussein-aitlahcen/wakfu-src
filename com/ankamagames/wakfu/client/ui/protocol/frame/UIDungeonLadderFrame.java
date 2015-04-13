package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.appearance.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.background.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.dungeon.*;
import com.ankamagames.wakfu.common.game.dungeon.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.framework.reflect.*;

public class UIDungeonLadderFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UIDungeonLadderFrame m_instance;
    private DungeonLadderView m_dungeonLadderView;
    private DungeonStatutView m_currentDungeonStatutView;
    private DungeonTimer m_dungeonTimer;
    private ParticleDecorator m_particleDecorator;
    private Container m_splashMessageDialogContainer;
    private ArrayList<ModulationColorClient> m_appL;
    private final LinkedList<String> m_waitingSplashTexts;
    private static final int SPLASH_TEXT_DISPLAY_TIME = 2500;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public UIDungeonLadderFrame() {
        super();
        this.m_waitingSplashTexts = new LinkedList<String>();
    }
    
    public static UIDungeonLadderFrame getInstance() {
        return UIDungeonLadderFrame.m_instance;
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
            if (this.m_dungeonLadderView == null) {
                UIDungeonLadderFrame.m_logger.error((Object)"Impossible de charger l'affichage du ladder, il manque les donn\u00e9es !");
                return;
            }
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("dungeonLadderDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIDungeonLadderFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().setPropertyValue("dungeonLadder", this.m_dungeonLadderView);
            PropertiesProvider.getInstance().setPropertyValue("dungeonLadderBackgroundImage", WakfuConfiguration.getInstance().getDisplayBackgroundBackgroundImage(BackgroundDisplayType.POSTER.getId()));
            Xulor.getInstance().load("dungeonLadderDialog", Dialogs.getDialogPath("dungeonLadderDialog"), 256L, (short)30000);
            this.fadeOut();
            Xulor.getInstance().putActionClass("wakfu.dungeonLadder", DungeonLadderDialogActions.class);
            WakfuSoundManager.getInstance().playGUISound(600120L);
        }
    }
    
    private void fadeOut() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("dungeonLadderDialog");
        final ArrayList<ModulationColorClient> mcc = new ArrayList<ModulationColorClient>();
        Widget w = (Widget)map.getElement("background");
        if (w != null) {
            mcc.add(w.getAppearance());
        }
        w = (Widget)map.getElement("closeButton");
        if (w != null) {
            mcc.add(w.getAppearance());
        }
        if (w != null) {
            final Color c1 = new Color(Color.WHITE_ALPHA.get());
            final Color c2 = new Color(Color.WHITE.get());
            w.addTween(new ModulationColorListTween(c1, c2, mcc, 0, 500, 1, TweenFunction.PROGRESSIVE));
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.clean();
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().removeProperty("dungeonLadder");
            PropertiesProvider.getInstance().removeProperty("dungeonLadderBackgroundImage");
            Xulor.getInstance().unload("dungeonLadderDialog");
            Xulor.getInstance().removeActionClass("wakfu.dungeonLadder");
            WakfuSoundManager.getInstance().playGUISound(600121L);
        }
    }
    
    public void loadDungeonLadder(final DungeonLadder ladder) {
        this.m_dungeonLadderView = new DungeonLadderView(ladder);
        WakfuGameEntity.getInstance().pushFrame(getInstance());
    }
    
    public void displayDungeonLadderTimer() {
        this.stopDungeonLadderTimer();
        this.m_currentDungeonStatutView = new DungeonStatutView(DungeonLadderType.TIME_ATTACK);
        PropertiesProvider.getInstance().setPropertyValue("dungeonStatut", this.m_currentDungeonStatutView);
        this.m_dungeonTimer = new DungeonTimer();
        ProcessScheduler.getInstance().schedule(this.m_dungeonTimer, 1000L);
        Xulor.getInstance().load("dungeonStatutDialog", Dialogs.getDialogPath("dungeonStatutDialog"), 139264L, (short)10000);
    }
    
    public static String getChronoString(final int secondsTotal) {
        int minuteCount = 0;
        int seconds = secondsTotal;
        if (seconds / 3600 > 0) {
            return "> 1 h ";
        }
        if (secondsTotal / 60 > 0) {
            minuteCount = secondsTotal / 60;
            seconds = secondsTotal % 60;
        }
        return ((minuteCount < 10) ? ("0" + minuteCount) : minuteCount) + ":" + ((seconds < 10) ? ("0" + seconds) : seconds);
    }
    
    public void unloadDungeonStatut() {
        Xulor.getInstance().unload("dungeonStatutDialog");
        this.m_currentDungeonStatutView = null;
        PropertiesProvider.getInstance().removeProperty("dungeonStatut");
    }
    
    public void stopDungeonLadderTimer() {
        if (this.m_dungeonTimer == null) {
            return;
        }
        ProcessScheduler.getInstance().remove(this.m_dungeonTimer);
        this.m_dungeonTimer = null;
    }
    
    public void displayDungeonLadderMonsterCount() {
        (this.m_currentDungeonStatutView = new DungeonStatutView(DungeonLadderType.SURVIVAL)).setText("0");
        PropertiesProvider.getInstance().setPropertyValue("dungeonStatut", this.m_currentDungeonStatutView);
        Xulor.getInstance().load("dungeonStatutDialog", Dialogs.getDialogPath("dungeonStatutDialog"), 139264L, (short)10000);
    }
    
    public void incrementDungeonLadderMonsterCount() {
        final int count = Integer.parseInt(this.m_currentDungeonStatutView.getText());
        this.m_currentDungeonStatutView.setText(String.valueOf(count + 1));
    }
    
    public void displayDungeonResultSplashText(final short position) {
        if (this.m_currentDungeonStatutView == null) {
            UIDungeonLadderFrame.m_logger.error((Object)"on tente d'afficher le r\u00e9sultat d'un donjon alors qu'il n'existe pas d'interface de statut");
            return;
        }
        this.m_currentDungeonStatutView.setResult(position);
    }
    
    public void displaySplashText(final String text) {
        if (PropertiesProvider.getInstance().getObjectProperty("splashText") != null) {
            this.m_waitingSplashTexts.addLast(text);
        }
        else {
            PropertiesProvider.getInstance().setPropertyValue("splashText", text);
            if (!Xulor.getInstance().isLoaded("splashMessageDialog") || this.m_splashMessageDialogContainer == null) {
                this.m_splashMessageDialogContainer = (Container)Xulor.getInstance().load("splashMessageDialog", Dialogs.getDialogPath("splashMessageDialog"), 8210L, (short)10000);
            }
            if (this.m_splashMessageDialogContainer != null) {
                if (this.m_particleDecorator != null) {
                    this.m_splashMessageDialogContainer.getAppearance().destroyDecorator(this.m_particleDecorator);
                }
                (this.m_particleDecorator = new ParticleDecorator()).onCheckOut();
                this.m_particleDecorator.setFile("6001037.xps");
                this.m_particleDecorator.setAlignment(Alignment9.CENTER);
                this.m_splashMessageDialogContainer.getAppearance().add(this.m_particleDecorator);
            }
            this.fade(true);
        }
    }
    
    private void initializeAppList() {
        this.m_appL = new ArrayList<ModulationColorClient>();
        final ElementMap map = this.m_splashMessageDialogContainer.getElementMap();
        this.m_appL.add(((Container)map.getElement("mainContainer")).getAppearance());
        this.m_appL.add(((TextView)map.getElement("text")).getAppearance());
    }
    
    private void setWidgetsNonBlocking(final boolean nonBlocking) {
        final ElementMap map = this.m_splashMessageDialogContainer.getElementMap();
        ((Container)map.getElement("mainContainer")).setNonBlocking(nonBlocking);
    }
    
    public void fade(final boolean fadeIn) {
        if (this.m_splashMessageDialogContainer == null) {
            UIDungeonLadderFrame.m_logger.warn((Object)"on tente de fade un splashScreen alors qu'il n'est pas charg\u00e9");
            return;
        }
        if (this.m_appL == null) {
            this.initializeAppList();
        }
        this.setWidgetsNonBlocking(!fadeIn);
        final ElementMap map = this.m_splashMessageDialogContainer.getElementMap();
        final Container mainContainer = (Container)map.getElement("mainContainer");
        final ModulationColorClient app = this.m_appL.get(0);
        int aValue;
        int bValue;
        if (fadeIn) {
            aValue = Color.WHITE_ALPHA.get();
            bValue = Color.WHITE.get();
        }
        else {
            aValue = Color.WHITE.get();
            bValue = Color.WHITE_ALPHA.get();
        }
        if (aValue != bValue) {
            if (fadeIn) {
                WakfuSoundManager.getInstance().windowFadeIn();
            }
            else {
                WakfuSoundManager.getInstance().windowFadeOut();
            }
            final Color a = new Color(aValue);
            final Color b = new Color(bValue);
            mainContainer.removeTweensOfType(ModulationColorListTween.class);
            final ModulationColorListTween tw = new ModulationColorListTween(a, b, this.m_appL, 0, 500, 1, TweenFunction.PROGRESSIVE);
            tw.addTweenEventListener(new TweenEventListener() {
                @Override
                public void onTweenEvent(final AbstractTween tw, final TweenEvent e) {
                    switch (e) {
                        case TWEEN_ENDED: {
                            tw.removeTweenEventListener(this);
                            if (fadeIn) {
                                ProcessScheduler.getInstance().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        UIDungeonLadderFrame.this.fade(false);
                                    }
                                }, 2500L, 1);
                                break;
                            }
                            if (UIDungeonLadderFrame.this.m_waitingSplashTexts.size() > 0) {
                                ProcessScheduler.getInstance().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        PropertiesProvider.getInstance().setPropertyValue("splashText", null);
                                        UIDungeonLadderFrame.this.displaySplashText(UIDungeonLadderFrame.this.m_waitingSplashTexts.poll());
                                    }
                                }, 2000L, 1);
                                break;
                            }
                            if (UIDungeonLadderFrame.this.m_particleDecorator != null) {
                                UIDungeonLadderFrame.this.m_splashMessageDialogContainer.getAppearance().destroyDecorator(UIDungeonLadderFrame.this.m_particleDecorator);
                                UIDungeonLadderFrame.this.m_particleDecorator = null;
                            }
                            UIDungeonLadderFrame.this.m_splashMessageDialogContainer = null;
                            UIDungeonLadderFrame.this.m_appL = null;
                            PropertiesProvider.getInstance().setPropertyValue("splashText", null);
                            break;
                        }
                    }
                }
            });
            mainContainer.addTween(tw);
        }
    }
    
    public DungeonLadderView getDungeonLadderView() {
        return this.m_dungeonLadderView;
    }
    
    public void clean() {
        this.stopDungeonLadderTimer();
        this.m_dungeonLadderView = null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIDungeonLadderFrame.class);
        UIDungeonLadderFrame.m_instance = new UIDungeonLadderFrame();
    }
    
    private class DungeonTimer implements Runnable
    {
        private int m_secondsCount;
        
        @Override
        public void run() {
            ++this.m_secondsCount;
            if (UIDungeonLadderFrame.this.m_currentDungeonStatutView != null) {
                UIDungeonLadderFrame.this.m_currentDungeonStatutView.setText(UIDungeonLadderFrame.getChronoString(this.m_secondsCount));
            }
        }
        
        @Override
        public String toString() {
            return "DungeonTimer{m_secondsCount=" + this.m_secondsCount + '}' + super.toString();
        }
    }
    
    private static class DungeonStatutView extends ImmutableFieldProvider
    {
        public static final String TEXT_FIELD = "text";
        public static final String DUNGEON_TYPE_FIELD = "dungeonType";
        public static final String POSITION_RESULT_FIELD = "positionResult";
        public static final String BUFFS_FIELD = "buffs";
        public static final String MEDAL_STYLE_FIELD = "medalStyle";
        public final String[] FIELDS;
        private final DungeonLadderType m_dungeonLadderType;
        private String m_text;
        private short m_positionResult;
        
        public DungeonStatutView(final DungeonLadderType dungeonLadderType) {
            super();
            this.FIELDS = new String[] { "text", "dungeonType", "positionResult", "buffs", "medalStyle" };
            this.m_positionResult = -1;
            this.m_dungeonLadderType = dungeonLadderType;
        }
        
        public void setText(final String text) {
            this.m_text = text;
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "text");
        }
        
        public String getText() {
            return this.m_text;
        }
        
        @Override
        public String[] getFields() {
            return this.FIELDS;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("text")) {
                return this.m_text;
            }
            if (fieldName.equals("dungeonType")) {
                return this.m_dungeonLadderType.ordinal();
            }
            if (fieldName.equals("positionResult")) {
                return this.m_positionResult;
            }
            if (fieldName.equals("medalStyle")) {
                switch (this.m_positionResult) {
                    case 0: {
                        return "GoldMedal";
                    }
                    case 1: {
                        return "SilverMedal";
                    }
                    case 2: {
                        return "BronzeMedal";
                    }
                    default: {
                        return "BronzeMedal";
                    }
                }
            }
            else {
                if (!fieldName.equals("buffs")) {
                    return null;
                }
                if (this.m_positionResult != 0) {
                    return "";
                }
                return null;
            }
        }
        
        public void setResult(final short position) {
            this.m_positionResult = position;
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "medalStyle", "positionResult", "buffs");
        }
    }
}
