package com.ankamagames.wakfu.common.game.tax;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.*;

public class TaxManager
{
    private static final Logger m_logger;
    public static final TaxManager INSTANCE;
    private final ArrayList<TaxHandler> m_taxHandlers;
    
    private TaxManager() {
        super();
        this.m_taxHandlers = new ArrayList<TaxHandler>();
    }
    
    public void addTaxHandler(final TaxHandler taxHandler) {
        if (this.m_taxHandlers.contains(taxHandler)) {
            TaxManager.m_logger.error((Object)("Tentative d'ajout multiple du TaxHandler=" + taxHandler.toString() + " \u00e0 la liste des percepteurs de taxe globaux"));
            return;
        }
        this.m_taxHandlers.add(taxHandler);
    }
    
    public int getTaxesAmount(final MerchantClient taxSource, final TaxContext taxContext, final int kamasCount) {
        return this.applyTaxes(taxSource, taxContext, kamasCount, false);
    }
    
    public int applyTaxes(final MerchantClient taxSource, final TaxContext taxContext, final int kamasCount) {
        return this.applyTaxes(taxSource, taxContext, kamasCount, true);
    }
    
    private int applyTaxes(final MerchantClient taxSource, final TaxContext taxContext, final int kamasCount, final boolean checkInTaxes) {
        int totalTaxAmount = 0;
        for (int i = 0, size = this.m_taxHandlers.size(); i < size; ++i) {
            final TaxHandler taxHandler = this.m_taxHandlers.get(i);
            if (taxHandler.mustTax(taxSource)) {
                final int taxAmount = taxHandler.getTaxAmount(taxSource, taxContext, kamasCount);
                if (taxAmount > 0 && checkInTaxes) {
                    taxHandler.checkInTax(taxContext, taxAmount);
                }
                totalTaxAmount += taxAmount;
            }
        }
        if (checkInTaxes) {
            taxSource.getWallet().substractAmount(totalTaxAmount);
        }
        return totalTaxAmount;
    }
    
    public void removeTaxHandler(final TaxHandler taxHandler) {
        if (this.m_taxHandlers.contains(taxHandler)) {
            this.m_taxHandlers.remove(taxHandler);
        }
        else {
            TaxManager.m_logger.error((Object)("Tentative de suppression du TaxHandler=" + taxHandler.toString() + " alors qu'il n'est pas pr\u00e9sent dans la liste"));
        }
    }
    
    @Override
    public String toString() {
        return "TaxManager{m_taxHandlers=" + this.m_taxHandlers.size() + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)TaxManager.class);
        INSTANCE = new TaxManager();
    }
}
