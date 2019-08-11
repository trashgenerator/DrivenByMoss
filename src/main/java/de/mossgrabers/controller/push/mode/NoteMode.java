// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.push.mode;

import de.mossgrabers.controller.push.controller.PushControlSurface;
import de.mossgrabers.framework.controller.IValueChanger;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.INoteClip;
import de.mossgrabers.framework.graphics.display.DisplayModel;
import de.mossgrabers.framework.scale.Scales;
import de.mossgrabers.framework.utils.StringUtils;


/**
 * Editing of note parameters.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class NoteMode extends BaseMode
{
    INoteClip clip = null;
    int       step = 0;
    int       note = 60;


    /**
     * Constructor.
     *
     * @param surface The control surface
     * @param model The model
     */
    public NoteMode (final PushControlSurface surface, final IModel model)
    {
        super ("Note", surface, model);
    }


    /**
     * Set the values.
     *
     * @param clip The clip to edit
     * @param step The step to edit
     * @param note The note to edit
     */
    public void setValues (final INoteClip clip, final int step, final int note)
    {
        this.clip = clip;
        this.step = step;
        this.note = note;
    }


    /** {@inheritDoc} */
    @Override
    public void onKnobValue (final int index, final int value)
    {
        final IValueChanger valueChanger = this.model.getValueChanger ();
        switch (index)
        {
            // Note length
            case 0:
                // TODO
                // if (!this.increaseKnobMovement ())
                // return;

                this.clip.changeStepDuration (this.step, this.note, value, 0.1);

                break;

            // Note velocity
            case 1:
                this.clip.changeStepVelocity (this.step, this.note, value);
                // TODO valueChanger.changeValue (value, this.noteVelocity, 1, 128);
                break;

            default:
                return;
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateDisplay1 ()
    {
        // TODO
        // final int quarters = (int) Math.floor (this.noteLength);
        // final int fine = (int) Math.floor (this.noteLength * 100) % 100;
        // final Display d = this.surface.getDisplay ();
        // d.clear ().setCell (0, 0, "Quarters").setCell (1, 0, Integer.toString (quarters));
        // d.setCell (0, 1, "Fine").setCell (1, 1, Integer.toString (fine));
        // d.setCell (0, 2, "Velocity").setCell (1, 2, Integer.toString (this.noteVelocity * 100 /
        // 127) + "%");
        // d.setBlock (3, 0, "Step: " + (this.step + 1));
        // d.setBlock (3, 1, "Selec. Note: " + Scales.formatNoteAndOctave (this.note, -3));
        // d.allDone ();
    }


    /** {@inheritDoc} */
    @Override
    public void updateDisplay2 ()
    {
        // TODO
        double noteLength = clip.getStepDuration (step, note);

        String measures = StringUtils.formatMeasures (this.model.getTransport ().getQuartersPerMeasure (), noteLength, 0);

        final DisplayModel message = this.surface.getDisplay ().getModel ();
        // TODO
        int quarters = 0;
        message.addParameterElement ("Length", quarters, measures, this.isKnobTouched[0], -1);

        double noteVelocity = this.clip.getStepVelocity (this.step, this.note);
        // TODO
        final int parameterValue = (int) (noteVelocity * 1023);
        message.addParameterElement ("Velocity", parameterValue, Integer.toString ((int) (noteVelocity * 100)) + "%", this.isKnobTouched[2], parameterValue);
        message.addOptionElement ("    Step: " + (this.step + 1), "", false, "    Note: " + Scales.formatNoteAndOctave (this.note, -3), "", false, false);
        for (int i = 3; i < 8; i++)
            message.addOptionElement ("", "", false, "", "", false, false);
        message.send ();
    }
}