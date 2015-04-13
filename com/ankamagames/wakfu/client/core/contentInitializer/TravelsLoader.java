package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.travel.infos.*;
import com.ankamagames.wakfu.client.core.*;

public class TravelsLoader implements ContentInitializer
{
    private static final Logger m_logger;
    private static final TravelsLoader m_instance;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        this.loadZaaps();
        this.loadZaapLinks();
        this.loadDragos();
        this.loadBoats();
        this.loadBoatLinks();
        this.loadCannons();
        clientInstance.fireContentInitializerDone(this);
    }
    
    private void loadCannons() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new CannonBinaryData(), new LoadProcedure<CannonBinaryData>() {
            @Override
            public void load(final CannonBinaryData bs) {
                final CannonInfo cannon = new CannonInfo(bs.getCannonId(), bs.getVisualId(), bs.getItemId(), bs.getItemQty(), bs.getUiGfxId(), TravelType.getFromId(bs.getLandmarkTravelType()));
                for (final CannonBinaryData.Link linkData : bs.getLinks()) {
                    final CannonLink link = new CannonLink(linkData.getId(), (short)linkData.getDropWeight(), linkData.getCriteria(), linkData.getExitX(), linkData.getExitY(), linkData.getExitWorldId());
                    cannon.addLink(link);
                    link.setLoading(convertLoading(linkData.getLoading()));
                }
                TravelInfoManager.INSTANCE.addCannon(cannon);
            }
        });
    }
    
    private void loadBoatLinks() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new BoatLinkBinaryData(), new LoadProcedure<BoatLinkBinaryData>() {
            @Override
            public void load(final BoatLinkBinaryData bs) {
                final BoatLink link = new BoatLink(bs.getId(), bs.getStart(), bs.getEnd(), bs.getCost(), bs.getCriteria(), bs.getCriteriaDisplay(), bs.isNeedsToPayEverytime());
                TravelInfoManager.INSTANCE.addBoatLink(link);
                link.setLoading(convertLoading(bs.getLoading()));
            }
        });
    }
    
    private void loadBoats() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new BoatBinaryData(), new LoadProcedure<BoatBinaryData>() {
            @Override
            public void load(final BoatBinaryData bs) {
                final BoatInfo boat = new BoatInfo(bs.getBoatId(), bs.getVisualId(), bs.getExitX(), bs.getExitY(), bs.getExitWorldId(), bs.getUiGfxId(), TravelType.getFromId(bs.getLandmarkTravelType()));
                TravelInfoManager.INSTANCE.addBoat(boat);
            }
        });
    }
    
    private void loadDragos() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new DragoBinaryData(), new LoadProcedure<DragoBinaryData>() {
            @Override
            public void load(final DragoBinaryData bs) {
                final DragoInfo drago = new DragoInfo(bs.getDragoId(), bs.getVisualId(), bs.getExitX(), bs.getExitY(), bs.getUiGfxId(), TravelType.getFromId(bs.getLandmarkTravelType()), bs.getDragoCriterion());
                TravelInfoManager.INSTANCE.addDrago(drago);
                drago.setLoading(convertLoading(bs.getLoading()));
            }
        });
    }
    
    private static TravelLoadingInfo convertLoading(final TravelLoadingBinaryData loading) {
        if (loading == null) {
            return null;
        }
        return new TravelLoadingInfo(loading.getLoadingAnimationName(), loading.getLoadingMinDuration(), loading.getLoadingFadeInDuration(), loading.getLoadingFadeOutDuration());
    }
    
    private void loadZaapLinks() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new ZaapLinkBinaryData(), new LoadProcedure<ZaapLinkBinaryData>() {
            @Override
            public void load(final ZaapLinkBinaryData data) {
                final ZaapLink link = new ZaapLink(data.getId(), data.getStart(), data.getEnd(), data.getCost());
                TravelInfoManager.INSTANCE.addZaapLink(link);
            }
        });
        TravelInfoManager.INSTANCE.checkZaapLinksIntegrity();
    }
    
    private void loadZaaps() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new ZaapBinaryData(), new LoadProcedure<ZaapBinaryData>() {
            @Override
            public void load(final ZaapBinaryData bs) {
                final ZaapInfo zaap = new ZaapInfo(bs.getZaapId(), bs.getVisualId(), bs.getExitX(), bs.getExitY(), bs.getExitWorldId(), bs.getDestinationCriteria(), bs.getUiGfxId(), TravelType.getFromId(bs.getLandmarkTravelType()));
                TravelInfoManager.INSTANCE.addZaap(zaap, bs.isZaapBase());
                zaap.setLoading(convertLoading(bs.getLoading()));
            }
        });
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.zaap");
    }
    
    public static TravelsLoader getInstance() {
        return TravelsLoader.m_instance;
    }
    
    static {
        m_logger = Logger.getLogger((Class)TravelsLoader.class);
        m_instance = new TravelsLoader();
    }
}
