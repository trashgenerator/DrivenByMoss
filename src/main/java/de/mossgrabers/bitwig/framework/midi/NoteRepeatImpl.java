// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.bitwig.framework.midi;

import de.mossgrabers.framework.daw.data.ITrack;
import de.mossgrabers.framework.daw.midi.INoteRepeat;

import com.bitwig.extension.controller.api.Arpeggiator;


/**
 * Implementation for a note repeat.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class NoteRepeatImpl implements INoteRepeat
{
    private final Arpeggiator noteRepeat;


    /**
     * Constructor.
     *
     * @param arpeggiator The Bitwig arpeggiator object
     */
    public NoteRepeatImpl (final Arpeggiator arpeggiator)
    {
        this.noteRepeat = arpeggiator;

        this.noteRepeat.isEnabled ().markInterested ();
        this.noteRepeat.period ().markInterested ();
        this.noteRepeat.gateLength ().markInterested ();
        this.noteRepeat.shuffle ().markInterested ();
        this.noteRepeat.usePressureToVelocity ().markInterested ();
        this.noteRepeat.mode ().markInterested ();
        this.noteRepeat.octaves ().markInterested ();
        this.noteRepeat.isFreeRunning ().markInterested ();
    }


    /** {@inheritDoc} */
    @Override
    public void enableObservers (final boolean enable)
    {
        this.noteRepeat.isEnabled ().setIsSubscribed (enable);
        this.noteRepeat.period ().setIsSubscribed (enable);
        this.noteRepeat.gateLength ().setIsSubscribed (enable);
        this.noteRepeat.shuffle ().setIsSubscribed (enable);
        this.noteRepeat.usePressureToVelocity ().setIsSubscribed (enable);
        this.noteRepeat.mode ().setIsSubscribed (enable);
        this.noteRepeat.octaves ().setIsSubscribed (enable);
        this.noteRepeat.isFreeRunning ().setIsSubscribed (enable);
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
        this.noteRepeat.gateLength ().set (length);
    }


    /** {@inheritDoc} */
    @Override
    public double getNoteLength (final ITrack track)
    {
        return this.noteRepeat.gateLength ().get ();
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


    /** {@inheritDoc} */
    @Override
    public boolean usePressure (final ITrack track)
    {
        return this.noteRepeat.usePressureToVelocity ().get ();
    }


    /** {@inheritDoc} */
    @Override
    public void toggleUsePressure (final ITrack track)
    {
        this.noteRepeat.usePressureToVelocity ().toggle ();
    }


    /** {@inheritDoc} */
    @Override
    public int getOctaves (final ITrack track)
    {
        return this.noteRepeat.octaves ().get ();
    }


    /** {@inheritDoc} */
    @Override
    public void setOctaves (final ITrack track, final int octaves)
    {
        if (octaves >= 0 && octaves < 9)
            this.noteRepeat.octaves ().set (octaves);
    }


    /** {@inheritDoc} */
    @Override
    public String getMode (final ITrack track)
    {
        return this.noteRepeat.mode ().get ();
    }


    /** {@inheritDoc} */
    @Override
    public void setMode (final ITrack track, final String mode)
    {
        this.noteRepeat.mode ().set (mode);
    }


    /** {@inheritDoc} */
    @Override
    public boolean isFreeRunning (final ITrack track)
    {
        return this.noteRepeat.isFreeRunning ().get ();
    }


    /** {@inheritDoc} */
    @Override
    public void toggleIsFreeRunning (final ITrack track)
    {
        this.noteRepeat.isFreeRunning ().toggle ();
    }
}
