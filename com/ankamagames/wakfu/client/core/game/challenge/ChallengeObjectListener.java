package com.ankamagames.wakfu.client.core.game.challenge;

import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.common.game.resource.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.interactiveElements.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;

public class ChallengeObjectListener implements MobileDestructionListener, MobileCreationListener, ResourceDestructionListener, ResourceCreationListener, InteractiveElementCreationListener, InteractiveElementDestructionListener
{
    public static final ChallengeObjectListener INSTANCE;
    private final TLongIntHashMap m_monsterAps;
    private final TLongIntHashMap m_resourceAps;
    private final TLongIntHashMap m_ieAps;
    
    private ChallengeObjectListener() {
        super();
        this.m_monsterAps = new TLongIntHashMap();
        this.m_resourceAps = new TLongIntHashMap();
        this.m_ieAps = new TLongIntHashMap();
    }
    
    @Override
    public void onMobileCreation(final Mobile mobile) {
        if (!isChallengeNPC(mobile)) {
            return;
        }
        onCreation(mobile, this.m_monsterAps, 800163);
    }
    
    @Override
    public void onMobileDestruction(final Mobile mobile) {
        onDestruction(mobile, this.m_monsterAps, true);
    }
    
    private static boolean isChallengeNPC(final Mobile mobile) {
        return mobile instanceof CharacterActor && ((CharacterActor)mobile).getCharacterInfo().isActiveProperty(WorldPropertyType.CHALLENGE_NPC);
    }
    
    @Override
    public void onResourceCreation(final Resource resource) {
        if (!resource.getReferenceResource().hasProperty(ResourcesProperty.CHALLENGE_RESOURCE)) {
            return;
        }
        onCreation(resource, this.m_resourceAps, getChallengeAPSId(resource));
    }
    
    @Override
    public void onResourceDestruction(final Resource resource) {
        onDestruction(resource, this.m_resourceAps, true);
    }
    
    @Override
    public void onInteractiveElementCreation(final ClientInteractiveAnimatedElementSceneView element) {
        if (!element.getInteractiveElement().hasProperty(WakfuInteractiveElementProperty.CHALLENGE_IE)) {
            return;
        }
        onCreation(element, this.m_ieAps, 800163);
    }
    
    @Override
    public void onInteractiveElementDestruction(final ClientInteractiveAnimatedElementSceneView element) {
        onDestruction(element, this.m_ieAps, false);
    }
    
    private static void onCreation(final AnimatedElement ae, final TLongIntHashMap apsMap, final int apsId) {
        if (apsMap.contains(ae.getId())) {
            return;
        }
        final FreeParticleSystem particleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(apsId);
        particleSystem.setTarget(ae);
        IsoParticleSystemManager.getInstance().addParticleSystem(particleSystem);
        apsMap.put(ae.getId(), particleSystem.getId());
    }
    
    private static void onDestruction(final AnimatedElement ae, final TLongIntHashMap apsMap, final boolean createDepopAps) {
        final int apsId = apsMap.remove(ae.getId());
        if (apsId == 0) {
            return;
        }
        IsoParticleSystemManager.getInstance().removeParticleSystem(apsId);
        if (createDepopAps) {
            createDepopAPS(ae);
        }
    }
    
    private static void createDepopAPS(final IsoWorldTarget target) {
        final FreeParticleSystem particleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(800174);
        particleSystem.setWorldPosition(target.getWorldX(), target.getWorldY(), target.getAltitude());
        IsoParticleSystemManager.getInstance().addParticleSystem(particleSystem);
    }
    
    private static int getChallengeAPSId(final Resource resource) {
        return resource.getReferenceResource().isUseBigChallengeAps() ? 800163 : 800164;
    }
    
    static {
        INSTANCE = new ChallengeObjectListener();
    }
}
