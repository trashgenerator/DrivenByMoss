// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.push.mode;

import de.mossgrabers.controller.push.controller.PushControlSurface;
import de.mossgrabers.controller.push.controller.PushDisplay;
import de.mossgrabers.framework.Resolution;
import de.mossgrabers.framework.controller.IValueChanger;
import de.mossgrabers.framework.controller.display.Display;
import de.mossgrabers.framework.controller.display.Format;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.data.ITrack;
import de.mossgrabers.framework.daw.midi.INoteInput;
import de.mossgrabers.framework.daw.midi.INoteRepeat;
import de.mossgrabers.framework.graphics.display.DisplayModel;
import de.mossgrabers.framework.mode.AbstractMode;
import de.mossgrabers.framework.utils.ButtonEvent;
import de.mossgrabers.framework.utils.Pair;


/**
 * Editing the length of note repeat notes.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class NoteRepeatMode extends BaseMode
{
    private final INoteRepeat noteRepeat;


    /**
     * Constructor.
     *
     * @param surface The control surface
     * @param model The model
     */
    public NoteRepeatMode (final PushControlSurface surface, final IModel model)
    {
        super ("Note Repeat", surface, model);

        this.isTemporary = true;

        final INoteInput defaultNoteInput = surface.getInput ().getDefaultNoteInput ();
        this.noteRepeat = defaultNoteInput == null ? null : defaultNoteInput.getNoteRepeat ();
    }


    /** {@inheritDoc} */
    @Override
    public void onKnobValue (final int index, final int value)
    {
        if (!this.increaseKnobMovement ())
            return;

        final ITrack selectedTrack = this.model.getCurrentTrackBank ().getSelectedItem ();

        switch (index)
        {
            case 0:
            case 1:
                final int sel = Resolution.change (Resolution.getMatch (this.noteRepeat.getPeriod (selectedTrack)), this.model.getValueChanger ().calcKnobSpeed (value) > 0);
                this.noteRepeat.setPeriod (selectedTrack, Resolution.getValueAt (sel));
                break;

            case 2:
            case 3:
                final int sel2 = Resolution.change (Resolution.getMatch (this.noteRepeat.getNoteLength (selectedTrack)), this.model.getValueChanger ().calcKnobSpeed (value) > 0);
                this.noteRepeat.setNoteLength (selectedTrack, Resolution.getValueAt (sel2));
                break;

            case 7:
                this.changeVelocityRamp (selectedTrack, value);
                break;

            default:
                // Not used
                break;
        }
    }


    /** {@inheritDoc} */
    @Override
    public void onKnobTouch (final int index, final boolean isTouched)
    {
        this.isKnobTouched[index] = isTouched;

        if (isTouched && index == 7 && this.surface.isDeletePressed ())
        {
            this.surface.setTriggerConsumed (this.surface.getDeleteTriggerId ());
            this.noteRepeat.setVelocityRamp (this.model.getCurrentTrackBank ().getSelectedItem (), 0);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void onFirstRow (final int index, final ButtonEvent event)
    {
        if (event != ButtonEvent.UP || this.noteRepeat == null)
            return;

        final ITrack selectedTrack = this.model.getCurrentTrackBank ().getSelectedItem ();
        switch (index)
        {
            case 0:
            case 1:
                final int sel = Resolution.change (Resolution.getMatch (this.noteRepeat.getPeriod (selectedTrack)), index == 1);
                this.noteRepeat.setPeriod (selectedTrack, Resolution.getValueAt (sel));
                break;

            case 2:
            case 3:
                final int sel2 = Resolution.change (Resolution.getMatch (this.noteRepeat.getNoteLength (selectedTrack)), index == 3);
                this.noteRepeat.setNoteLength (selectedTrack, Resolution.getValueAt (sel2));
                break;

            case 6:
                this.noteRepeat.toggleShuffle (selectedTrack);
                break;

            case 7:
                this.noteRepeat.toggleUsePressure (selectedTrack);
                break;

            default:
                // Unused
                break;
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateFirstRow ()
    {
        this.surface.updateTrigger (20, AbstractMode.BUTTON_COLOR_ON);
        this.surface.updateTrigger (21, AbstractMode.BUTTON_COLOR_ON);
        this.surface.updateTrigger (22, AbstractMode.BUTTON_COLOR_ON);
        this.surface.updateTrigger (23, AbstractMode.BUTTON_COLOR_ON);

        this.surface.updateTrigger (24, AbstractMode.BUTTON_COLOR_OFF);
        this.surface.updateTrigger (25, AbstractMode.BUTTON_COLOR_OFF);
        final ITrack selectedTrack = this.model.getCurrentTrackBank ().getSelectedItem ();
        this.surface.updateTrigger (26, this.noteRepeat != null && this.noteRepeat.isShuffle (selectedTrack) ? AbstractMode.BUTTON_COLOR_HI : AbstractMode.BUTTON_COLOR_ON);
        this.surface.updateTrigger (27, this.noteRepeat != null && this.noteRepeat.usePressure (selectedTrack) ? AbstractMode.BUTTON_COLOR_HI : AbstractMode.BUTTON_COLOR_ON);
    }


    /** {@inheritDoc} */
    @Override
    public void updateDisplay1 ()
    {
        final Display d = this.surface.getDisplay ().clear ();

        final ITrack selectedTrack = this.model.getCurrentTrackBank ().getSelectedItem ();

        d.setCell (0, 0, "Period:");
        final int selPeriodIndex = this.getSelectedPeriodIndex (selectedTrack);
        int pos = 0;
        for (final Pair<String, Boolean> p: PushDisplay.createMenuList (4, Resolution.getNames (), selPeriodIndex))
        {
            d.setCell (pos, 1, (p.getValue ().booleanValue () ? PushDisplay.SELECT_ARROW : " ") + p.getKey ());
            pos++;
        }

        d.setCell (0, 2, "Length:");
        final int selLengthIndex = this.getSelectedNoteLengthIndex (selectedTrack);
        pos = 0;
        for (final Pair<String, Boolean> p: PushDisplay.createMenuList (4, Resolution.getNames (), selLengthIndex))
        {
            d.setCell (pos, 3, (p.getValue ().booleanValue () ? PushDisplay.SELECT_ARROW : " ") + p.getKey ());
            pos++;
        }

        d.setCell (0, 7, "Accent");
        d.setCell (1, 7, this.noteRepeat.getVelocityRampStr (selectedTrack));
        d.setCell (2, 7, getRampDisplayValue (selectedTrack), Format.FORMAT_VALUE);

        d.setCell (3, 6, "Shuffle");
        d.setCell (3, 7, "Pressure");

        d.allDone ();
    }


    /** {@inheritDoc} */
    @Override
    public void updateDisplay2 ()
    {
        if (this.noteRepeat == null)
            return;

        final DisplayModel message = this.surface.getDisplay ().getModel ();
        final ITrack selectedTrack = this.model.getCurrentTrackBank ().getSelectedItem ();

        message.addOptionElement ("Period", "", false, "", "", false, false);
        final int selPeriodIndex = this.getSelectedPeriodIndex (selectedTrack);
        message.addListElement (6, Resolution.getNames (), selPeriodIndex);

        message.addOptionElement ("  Length", "", false, "", "", false, false);
        final int selLengthIndex = this.getSelectedNoteLengthIndex (selectedTrack);
        message.addListElement (6, Resolution.getNames (), selLengthIndex);

        message.addEmptyElement ();
        message.addEmptyElement ();

        message.addOptionElement ("", "", false, "", "Shuffle", this.noteRepeat.isShuffle (selectedTrack), false);

        message.addParameterElementWithPlainMenu ("", false, "Pressure", null, this.noteRepeat.usePressure (selectedTrack), "Vel. Ramp", getRampDisplayValue (selectedTrack), this.noteRepeat.getVelocityRampStr (selectedTrack), this.isKnobTouched[5], -1);

        message.send ();
    }


    private int getRampDisplayValue (final ITrack selectedTrack)
    {
        final double ramp = this.noteRepeat.getVelocityRamp (selectedTrack);
        return this.model.getValueChanger ().fromNormalizedValue ((ramp + 1.0) / 2.0);
    }


    /**
     * Get the index of the selected period.
     *
     * @param selectedTrack The currently selected track
     * @return The selected period index
     */
    private int getSelectedPeriodIndex (final ITrack selectedTrack)
    {
        return this.noteRepeat == null ? -1 : Resolution.getMatch (this.noteRepeat.getPeriod (selectedTrack));
    }


    /**
     * Get the index of the selected length.
     *
     * @param selectedTrack The currently selected track
     * @return The selected lenth index
     */
    private int getSelectedNoteLengthIndex (final ITrack selectedTrack)
    {
        return this.noteRepeat == null ? -1 : Resolution.getMatch (this.noteRepeat.getNoteLength (selectedTrack));
    }


    private void changeVelocityRamp (final ITrack selectedTrack, final int control)
    {
        final IValueChanger valueChanger = this.model.getValueChanger ();
        final double inc = valueChanger.toNormalizedValue ((int) valueChanger.calcKnobSpeed (control));
        final double value = Math.max (-1.0, Math.min (1.0, this.noteRepeat.getVelocityRamp (selectedTrack) + inc));
        this.noteRepeat.setVelocityRamp (selectedTrack, value);
    }
}
