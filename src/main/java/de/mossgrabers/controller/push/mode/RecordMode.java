// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.push.mode;

import de.mossgrabers.controller.push.controller.Push1Display;
import de.mossgrabers.controller.push.controller.PushControlSurface;
import de.mossgrabers.framework.controller.color.ColorManager;
import de.mossgrabers.framework.controller.display.ITextDisplay;
import de.mossgrabers.framework.daw.IApplication;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.constants.RecordQuantization;
import de.mossgrabers.framework.graphics.display.DisplayModel;
import de.mossgrabers.framework.mode.AbstractMode;
import de.mossgrabers.framework.utils.ButtonEvent;


/**
 * Mode for editing the recording options.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class RecordMode extends BaseMode
{
    /**
     * Constructor.
     *
     * @param surface The control surface
     * @param model The model
     */
    public RecordMode (final PushControlSurface surface, final IModel model)
    {
        super ("Record", surface, model);
    }


    /** {@inheritDoc} */
    @Override
    public void updateDisplay1 (final ITextDisplay display)
    {
        final IApplication application = this.model.getApplication ();
        final RecordQuantization recQuant = application.getRecordQuantizationGrid ();
        final RecordQuantization [] values = RecordQuantization.values ();
        display.setBlock (1, 0, "Record Quantize:");
        for (int i = 0; i < values.length; i++)
            display.setCell (3, i, (values[i] == recQuant ? Push1Display.SELECT_ARROW : "") + values[i].getName ());
        display.setBlock (1, 3, "Qunat. Note Len:");
        display.setCell (3, 6, application.isRecordQuantizationNoteLength () ? "On" : "Off");
    }


    /** {@inheritDoc} */
    @Override
    public void updateDisplay2 (final DisplayModel message)
    {
        final IApplication application = this.model.getApplication ();
        final RecordQuantization recQuant = application.getRecordQuantizationGrid ();
        final RecordQuantization [] values = RecordQuantization.values ();
        for (int i = 0; i < values.length; i++)
            message.addOptionElement ("", "", false, i == 0 ? "Record Quantization" : "", values[i].getName (), values[i] == recQuant, false);
        message.addEmptyElement ();
        message.addOptionElement ("", "", false, "Quantize Note Length", application.isRecordQuantizationNoteLength () ? "On" : "Off", application.isRecordQuantizationNoteLength (), false);
        message.addEmptyElement ();
    }


    /** {@inheritDoc} */
    @Override
    public void onFirstRow (final int index, final ButtonEvent event)
    {
        if (event != ButtonEvent.UP)
            return;
        if (index == 6)
            this.model.getApplication ().toggleRecordQuantizationNoteLength ();
        else if (index < 5)
            this.model.getApplication ().setRecordQuantizationGrid (RecordQuantization.values ()[index]);
    }


    /** {@inheritDoc} */
    @Override
    public void updateFirstRow ()
    {
        final ColorManager colorManager = this.model.getColorManager ();
        final RecordQuantization [] values = RecordQuantization.values ();
        final IApplication application = this.model.getApplication ();
        final RecordQuantization recQuant = application.getRecordQuantizationGrid ();
        for (int i = 0; i < values.length; i++)
            this.surface.updateTrigger (20 + i, colorManager.getColor (values[i] == recQuant ? AbstractMode.BUTTON_COLOR_HI : AbstractMode.BUTTON_COLOR_ON));
        this.surface.updateTrigger (25, colorManager.getColor (AbstractMode.BUTTON_COLOR_OFF));
        this.surface.updateTrigger (26, colorManager.getColor (application.isRecordQuantizationNoteLength () ? AbstractMode.BUTTON_COLOR_HI : AbstractMode.BUTTON_COLOR_ON));
        this.surface.updateTrigger (27, colorManager.getColor (AbstractMode.BUTTON_COLOR_OFF));
    }
}
