package com.ankamagames.wakfu.common.game.descriptionGenerator;

import java.util.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import org.jetbrains.annotations.*;

public interface ContainerWriter
{
    ArrayList<String> writeContainer();
    
    void onContainerBegin(@NotNull ArrayList<String> p0);
    
    void onContainerEnd(@NotNull ArrayList<String> p0);
    
    String onEffectAdded(@NotNull String p0, @NotNull WakfuEffect p1);
    
    int getId();
    
    EffectContainer<WakfuEffect> getContainer();
    
    short getLevel();
    
    boolean isUseAutomaticDescription();
    
    boolean isEffectDisplayable(WakfuEffect p0);
    
    boolean isInMinimalDescriptionMode();
    
    void setMinimalDescriptionMode(boolean p0);
    
    @Nullable
    ArrayList<String> getValidCriterion();
    
    @Nullable
    ArrayList<String> getInvalidCriterion();
    
    CastableDescriptionGenerator.DescriptionMode getDescriptionMode();
    
    int getFreeDescriptionTranslationType();
    
    boolean displayOnlySubEffects();
    
    void setDisplayOnlySubEffects(boolean p0);
}
