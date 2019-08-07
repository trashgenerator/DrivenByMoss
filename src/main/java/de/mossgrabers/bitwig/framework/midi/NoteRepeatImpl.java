// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.bitwig.framework.midi;

import de.mossgrabers.framework.daw.data.ITrack;
import de.mossgrabers.framework.daw.midi.INoteRepeat;

import com.bitwig.extension.controller.api.NoteRepeat;


/**
 * Implementation for a note repeat.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class NoteRepeatImpl implements INoteRepeat
{
    private final NoteRepeat noteRepeat;


    /**
     * Constructor.
     *
     * @param noteRepeat The Bitwig note repeat object
     */
    public NoteRepeatImpl (final NoteRepeat noteRepeat)
    {
        this.noteRepeat = noteRepeat;

        this.noteRepeat.isEnabled ().markInterested ();
        this.noteRepeat.period ().markInterested ();
        this.noteRepeat.noteLengthRatio ().markInterested ();
        this.noteRepeat.shuffle ().markInterested ();
        this.noteRepeat.usePressureToVelocity ().markInterested ();
    }


    /** {@inheritDoc} */
    @Override
    public boolean isActive (final ITrack track)
    {
        return this.noteRepeat.isEnabled ().get ();
    }


    /** {@inheritDoc} */
    @Override
    public void toggleActive (final ITrack track)
    {
        this.noteRepeat.isEnabled ().toggle ();
    }


    /** {@inheritDoc} */
    @Override
    public void setPeriod (final ITrack track, final double length)
    {
        this.noteRepeat.period ().set (length);
    }


    /** {@inheritDoc} */
    @Override
    public double getPeriod (final ITrack track)
    {
        return this.noteRepeat.period ().get ();
    }


    /** {@inheritDoc} */
    @Override
    public void setNoteLength (final ITrack track, final double length)
    {
        this.noteRepeat.noteLengthRatio ().set (length);
    }


    /** {@inheritDoc} */
    @Override
    public double getNoteLength (final ITrack track)
    {
        return this.noteRepeat.noteLengthRatio ().get ();
    }


    /** {@inheritDoc} */
    @Override
    public boolean isShuffle (final ITrack track)
    {
        return this.noteRepeat.shuffle ().get ();
    }


    /** {@inheritDoc} */
    @Override
    public void toggleShuffle (final ITrack track)
    {
        this.noteRepeat.shuffle ().toggle ();
    }
}
