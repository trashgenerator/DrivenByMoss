// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.framework.graphics.canvas.component;

import de.mossgrabers.framework.controller.color.ColorEx;
import de.mossgrabers.framework.graphics.IBounds;
import de.mossgrabers.framework.graphics.IGraphicsConfiguration;
import de.mossgrabers.framework.graphics.IGraphicsContext;
import de.mossgrabers.framework.graphics.IGraphicsDimensions;
import de.mossgrabers.framework.graphics.IGraphicsInfo;
import de.mossgrabers.framework.graphics.canvas.component.LabelComponent.LabelLayout;


/**
 * An element in the grid which can display on option on top and on the bottom of the element. In
 * the middle two texts can be displayed. The texts are not clipped horizontally and can reach into
 * the next elements.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class OptionsComponent implements IComponent
{
    private final LabelComponent header;
    private final LabelComponent footer;

    private final String         headerBottom;
    private final String         headerTop;
    private boolean              isBottomHeaderSelected;


    /**
     * Constructor.
     *
     * @param headerTop A header for the top menu options (may span multiple grids), may be null
     * @param menuTopName A name for the to menu, may be null
     * @param isMenuTopSelected Is the top menu selected?
     * @param menuTopColor The color to use for the background top menu, may be null
     * @param headerBottom A header for the bottom menu options (may span multiple grids), may be
     *            null
     * @param menuBottomName A name for the bottom menu, may be null
     * @param isMenuBottomSelected Is the bottom menu selected?
     * @param menuBottomColor The color to use for the background bottom menu, may be null
     * @param useSmallTopMenu Draw the small version of the top menu if true
     * @param isBottomHeaderSelected True to draw the lower header selected
     */
    public OptionsComponent (final String headerTop, final String menuTopName, final boolean isMenuTopSelected, final double [] menuTopColor, final String headerBottom, final String menuBottomName, final boolean isMenuBottomSelected, final double [] menuBottomColor, final boolean useSmallTopMenu, final boolean isBottomHeaderSelected)
    {
        final ColorEx topColor = menuTopColor == null ? null : new ColorEx (menuTopColor[0], menuTopColor[1], menuTopColor[2]);
        final ColorEx bottomColor = menuBottomColor == null ? null : new ColorEx (menuBottomColor[0], menuBottomColor[1], menuBottomColor[2]);

        this.header = new LabelComponent (menuTopName, null, topColor, isMenuTopSelected, true, useSmallTopMenu ? LabelLayout.SMALL_HEADER : LabelLayout.PLAIN);
        this.footer = new LabelComponent (menuBottomName, null, bottomColor, isMenuBottomSelected, true, LabelLayout.PLAIN);

        this.headerTop = headerTop;
        this.headerBottom = headerBottom;
        this.isBottomHeaderSelected = isBottomHeaderSelected;
    }


    /** {@inheritDoc} */
    @Override
    public void draw (final IGraphicsInfo info)
    {
        final IGraphicsContext gc = info.getContext ();

        final IGraphicsDimensions dimensions = info.getDimensions ();

        final double menuHeight = 2 * dimensions.getMenuHeight ();
        final IGraphicsConfiguration configuration = info.getConfiguration ();

        this.header.draw (info.withBounds (0, menuHeight));

        final IBounds bounds = info.getBounds ();
        final double left = bounds.getLeft ();
        final double height = bounds.getHeight ();

        this.footer.draw (info.withBounds (height - menuHeight, menuHeight));

        final boolean hasTopHeader = this.headerTop != null && !this.headerTop.isEmpty ();
        final boolean hasBottomHeader = this.headerBottom != null && !this.headerBottom.isEmpty ();
        if (!hasTopHeader && !hasBottomHeader)
            return;

        final double headerHeight = (height - 2 * menuHeight) / 2;
        final ColorEx textColor = configuration.getColorText ();
        if (hasTopHeader)
            gc.drawTextInHeight (this.headerTop, left, menuHeight, headerHeight, textColor, headerHeight / 2.0);
        if (hasBottomHeader)
        {
            if (this.isBottomHeaderSelected)
                gc.drawTextInHeight (this.headerBottom, left, menuHeight + headerHeight, headerHeight, ColorEx.calcContrastColor (textColor), textColor, headerHeight / 2.0);
            else
                gc.drawTextInHeight (this.headerBottom, left, menuHeight + headerHeight, headerHeight, textColor, headerHeight / 2.0);
        }
    }
}
