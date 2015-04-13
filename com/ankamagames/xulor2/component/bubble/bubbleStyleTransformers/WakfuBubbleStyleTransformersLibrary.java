package com.ankamagames.xulor2.component.bubble.bubbleStyleTransformers;

import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.alignment.*;

public class WakfuBubbleStyleTransformersLibrary
{
    private abstract static class AbstractBubbleStyleTransformer implements WakfuBubbleStyleTransformer
    {
        private ElementMap m_elementMap;
        
        @Override
        public void transform(final WakfuBubbleWidget wakfuBubbleWidget) {
            this.m_elementMap = Xulor.getInstance().getEnvironment().getElementMap(wakfuBubbleWidget.getElementMap().getId());
            assert this.m_elementMap != null : "Impossible de charger une bulle";
        }
        
        protected void transformText(final String textStyle) {
            if (this.m_elementMap == null) {
                return;
            }
            final TextWidget text = (TextWidget)this.m_elementMap.getElement("text");
            if (text == null) {
                return;
            }
            text.setStyle(textStyle);
        }
        
        protected void transformContainer(final String containerStyle, final Dimension prefSize) {
            if (this.m_elementMap == null) {
                return;
            }
            final Container container = (Container)this.m_elementMap.getElement("container");
            if (container == null) {
                return;
            }
            container.setStyle(containerStyle);
            container.setPrefSize(prefSize);
            container.getAppearance().setModulationColor(null);
            final Container coloredContainer = (Container)this.m_elementMap.getElement("coloredContainer");
            if (coloredContainer == null) {
                return;
            }
            coloredContainer.setPrefSize(new Dimension(prefSize.getSize().width, prefSize.getSize().height + 20));
        }
        
        protected void transformImage(final String imageStyle, final int xOffset, final int yOffset) {
            if (this.m_elementMap == null) {
                return;
            }
            final Image image = (Image)this.m_elementMap.getElement("image");
            if (image == null) {
                return;
            }
            image.setStyle(imageStyle);
            image.getAppearance().setModulationColor(null);
            final StaticLayoutData data = (StaticLayoutData)image.getLayoutData();
            data.setXOffset(xOffset);
            data.setYOffset(yOffset);
        }
        
        @Override
        public void turn(final WakfuBubbleWidget wakfuBubbleWidget) {
            this.m_elementMap = Xulor.getInstance().getEnvironment().getElementMap(wakfuBubbleWidget.getElementMap().getId());
            final Image image = (Image)this.m_elementMap.getElement("image");
            final boolean toRight = wakfuBubbleWidget.isToRight();
            image.setStyle(toRight ? "BubbleArrowLeft" : "BubbleArrowRight");
            final StaticLayoutData sld = (StaticLayoutData)image.getLayoutData();
            sld.setAlign(toRight ? Alignment17.SOUTH_WEST : Alignment17.SOUTH_EAST);
            this.transform(wakfuBubbleWidget);
        }
    }
    
    public static class StandartBubbleStyleTransformer extends AbstractBubbleStyleTransformer
    {
        private static final String STANDART_TEXT_STYLE = "DefaultDarkBold14";
        private static final String STANDART_CONTAINER_STYLE = "chatBubble";
        private static final String STANDART_IMAGE_STYLE_LEFT = "BubbleArrowLeft";
        private static final String STANDART_IMAGE_STYLE_RIGHT = "BubbleArrowRight";
        
        @Override
        public void transform(final WakfuBubbleWidget wakfuBubbleWidget) {
            super.transform(wakfuBubbleWidget);
            this.transformText("DefaultDarkBold14");
            this.transformContainer("chatBubble", new Dimension(12, 20));
            this.transformImage(wakfuBubbleWidget.isToRight() ? "BubbleArrowLeft" : "BubbleArrowRight", wakfuBubbleWidget.isToRight() ? 15 : -15, 0);
            wakfuBubbleWidget.setXOffset(wakfuBubbleWidget.isToRight() ? -5 : 5);
        }
    }
    
    public static class ScreamingBubbleStyleTransformer extends AbstractBubbleStyleTransformer
    {
        private static final String SCREAMING_TEXT_STYLE = "DefaultDarkBold14";
        private static final String SCREAMING_CONTAINER_STYLE = "chatScreamingBubble";
        private static final String SCREAMING_IMAGE_STYLE_LEFT = "BubbleArrowLeft";
        private static final String SCREAMING_IMAGE_STYLE_RIGHT = "BubbleArrowRight";
        
        @Override
        public void transform(final WakfuBubbleWidget wakfuBubbleWidget) {
            super.transform(wakfuBubbleWidget);
            this.transformText("DefaultDarkBold14");
            this.transformContainer("chatScreamingBubble", new Dimension(40, 40));
            this.transformImage(wakfuBubbleWidget.isToRight() ? "BubbleArrowLeft" : "BubbleArrowRight", wakfuBubbleWidget.isToRight() ? 35 : -35, 3);
            wakfuBubbleWidget.setXOffset(wakfuBubbleWidget.isToRight() ? -5 : 5);
        }
    }
    
    public static class ThinkingBubbleStyleTransformer extends AbstractBubbleStyleTransformer
    {
        private static final String THINKING_TEXT_STYLE = "grey";
        private static final String THINKING_CONTAINER_STYLE = "chatThinkingBubble";
        private static final String THINKING_IMAGE_STYLE_LEFT = "BubbleThinkingArrowLeft";
        private static final String THINKING_IMAGE_STYLE_RIGHT = "BubbleThinkingArrowRight";
        
        @Override
        public void transform(final WakfuBubbleWidget wakfuBubbleWidget) {
            super.transform(wakfuBubbleWidget);
            this.transformText("grey");
            this.transformContainer("chatThinkingBubble", new Dimension(30, 40));
            this.transformImage(wakfuBubbleWidget.isToRight() ? "BubbleThinkingArrowLeft" : "BubbleThinkingArrowRight", wakfuBubbleWidget.isToRight() ? 25 : -25, 5);
            wakfuBubbleWidget.setXOffset(wakfuBubbleWidget.isToRight() ? -5 : 5);
        }
    }
}
