package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

@XulorActionsTag
public class WebShopDialogActions
{
    public static final String PACKAGE = "wakfu.webShop";
    
    public static void first(final Event e, final WebShopSession session) {
        session.firstPage();
    }
    
    public static void previous(final Event e, final WebShopSession session) {
        session.previousPage();
    }
    
    public static void next(final Event e, final WebShopSession session) {
        session.nextPage();
    }
    
    public static void last(final Event e, final WebShopSession session) {
        session.lastPage();
    }
    
    public static void selectCategory(final ListSelectionChangedEvent e, final WebShopSession session) {
        if (e.getSelected()) {
            search(e, session, (Category)e.getValue());
        }
    }
    
    public static void selectCategory(final Event e, final WebShopSession session, final Category category) {
        if (session.getCurrentCategory() != category) {
            search(e, session, category);
        }
    }
    
    public static void selectCategory(final Event e, final WebShopSession session, final GondolaHead gondolaHead) {
        session.setCurrentGondolaHead(gondolaHead);
        session.searchArticles(null);
    }
    
    public static void goToHighlightCategory(final Event e, final WebShopSession session, final Highlight highlight) {
        final Category category = session.getCategoryFromId(highlight.getCategoryId());
        if (category != null) {
            session.setCurrentCategory(category);
            session.searchArticles("");
        }
    }
    
    public static void selectCarrouselHighlight(final Event e, final WebShopSession session, final Highlight h) {
        final int index = session.getCarrouselHighlightIndex(h);
        if (index == -1) {
            return;
        }
        final UIMessage msg = new UIMessage((short)18502);
        msg.setIntValue(index);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void search(final Event e, final WebShopSession session, final Category category) {
        session.setCurrentCategory(category);
        session.searchArticles("");
    }
    
    public static void home(final Event e, final WebShopSession session) {
        final TextWidget textEditor = (TextWidget)e.getCurrentTarget().getElementMap().getElement("textEditor");
        if (textEditor != null) {
            textEditor.setText("");
        }
        session.home();
    }
    
    public static void search2(final SelectionChangedEvent e, final WebShopSession session) {
        if (e.isSelected()) {
            return;
        }
    }
    
    public static void search3(final SelectionChangedEvent e, final WebShopSession session) {
        if (e.isSelected()) {
            return;
        }
    }
    
    public static void search4(final SelectionChangedEvent e, final WebShopSession session) {
        if (e.isSelected()) {
            return;
        }
    }
    
    public static void search(final Event e, final WebShopSession session, final TextWidget te) {
        session.searchArticles(te.getText());
    }
    
    public static void search(final KeyEvent e, final WebShopSession session) {
        final TextEditor te = e.getTarget();
        if (e.getKeyCode() == 10) {
            session.searchArticles(te.getText());
        }
    }
    
    public static void resetSearch(final Event e, final WebShopSession session, final TextWidget te) {
        te.setText("");
        session.searchArticles("");
    }
    
    public static void quickBuy(final Event e, final WebShopSession session, final Article article) {
        if (article.isARental()) {
            final String msgText = WakfuTranslator.getInstance().getString("desc.rent.warning", article.getRentalDescription());
            final MessageBoxData data = new MessageBoxData(102, 0, msgText, null, WakfuMessageBoxConstants.getMessageBoxIconUrl(8), 24L);
            final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
            controler.addEventListener(new MessageBoxEventListener() {
                @Override
                public void messageBoxClosed(final int type, final String userEntry) {
                    if (type == 8) {
                        session.quickBuy(article);
                    }
                }
            });
        }
        else {
            session.quickBuy(article);
        }
    }
    
    public static void openItemDescription(final MouseEvent e, final ReferenceItem item) {
        if (e.getButton() == 3) {
            Actions.sendOpenCloseItemDetailMessage(e.getTarget().getElementMap().getRootId(), item);
        }
    }
    
    public static void openArticleDescription(final Event e, final Article article) {
        UIWebShopFrame.getInstance().openArticleDialog(article);
    }
    
    public static void openStuffPreviewWindow(final Event e, final Article article) {
        article.previewArticle();
    }
    
    public static void buyOgrines(final Event e) {
        Worker.getInstance().pushMessage(new UIMessage((short)18503));
    }
}
