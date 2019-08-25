// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.bitwig.framework.daw;

import de.mossgrabers.framework.controller.IValueChanger;
import de.mossgrabers.framework.daw.INoteClip;
import de.mossgrabers.framework.daw.IStepInfo;
import de.mossgrabers.framework.daw.constants.TransportConstants;

import com.bitwig.extension.controller.api.Clip;
import com.bitwig.extension.controller.api.Clip.StepInfo;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.SettableColorValue;


/**
 * Proxy to the Bitwig Cursor clip.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class CursorClipImpl implements INoteClip
{
    private final ControllerHost     host;
    private IValueChanger            valueChanger;
    private int                      numSteps;
    private int                      numRows;

    private final StepInfoImpl [] [] launcherData;
    private final StepInfoImpl [] [] arrangerData;
    private Clip                     launcherClip;
    private Clip                     arrangerClip;
    private int                      editPage = 0;
    private double                   stepLength;


    /**
     * Constructor.
     *
     * @param host The host
     * @param valueChanger The value changer
     * @param numSteps The number of steps of the clip to monitor
     * @param numRows The number of note rows of the clip to monitor
     */
    public CursorClipImpl (final ControllerHost host, final IValueChanger valueChanger, final int numSteps, final int numRows)
    {
        this.host = host;
        this.valueChanger = valueChanger;

        this.numSteps = numSteps;
        this.numRows = numRows;
        this.stepLength = 1.0 / 4.0; // 16th

        this.launcherData = new StepInfoImpl [this.numSteps] [];
        for (int step = 0; step < this.numSteps; step++)
        {
            this.launcherData[step] = new StepInfoImpl [this.numRows];
            for (int row = 0; row < this.numRows; row++)
                this.launcherData[step][row] = new StepInfoImpl ();
        }

        this.arrangerData = new StepInfoImpl [this.numSteps] [];
        for (int step = 0; step < this.numSteps; step++)
        {
            this.arrangerData[step] = new StepInfoImpl [this.numRows];
            for (int row = 0; row < this.numRows; row++)
                this.arrangerData[step][row] = new StepInfoImpl ();
        }

        // TODO Bugfix required: https://github.com/teotigraphix/Framework4Bitwig/issues/140
        this.launcherClip = host.createLauncherCursorClip (this.numSteps, this.numRows);
        this.launcherClip.exists ().markInterested ();

        this.launcherClip.addStepDataObserverV2 (this::handleStepData);

        this.launcherClip.playingStep ().markInterested ();
        this.launcherClip.getPlayStart ().markInterested ();
        this.launcherClip.getPlayStop ().markInterested ();
        this.launcherClip.getLoopStart ().markInterested ();
        this.launcherClip.getLoopLength ().markInterested ();
        this.launcherClip.isLoopEnabled ().markInterested ();
        this.launcherClip.getShuffle ().markInterested ();
        this.launcherClip.getAccent ().markInterested ();
        this.launcherClip.canScrollStepsBackwards ().markInterested ();
        this.launcherClip.canScrollStepsForwards ().markInterested ();
        this.launcherClip.color ().markInterested ();

        this.arrangerClip = host.createLauncherCursorClip (this.numSteps, this.numRows);

        this.arrangerClip.addStepDataObserverV2 (this::handleStepData);

        this.arrangerClip.playingStep ().markInterested ();
        this.arrangerClip.getPlayStart ().markInterested ();
        this.arrangerClip.getPlayStop ().markInterested ();
        this.arrangerClip.getLoopStart ().markInterested ();
        this.arrangerClip.getLoopLength ().markInterested ();
        this.arrangerClip.isLoopEnabled ().markInterested ();
        this.arrangerClip.getShuffle ().markInterested ();
        this.arrangerClip.getAccent ().markInterested ();
        this.arrangerClip.canScrollStepsBackwards ().markInterested ();
        this.arrangerClip.canScrollStepsForwards ().markInterested ();
        this.arrangerClip.color ().markInterested ();
    }


    /** {@inheritDoc} */
    @Override
    public void enableObservers (final boolean enable)
    {
        this.launcherClip.exists ().setIsSubscribed (enable);

        this.launcherClip.playingStep ().setIsSubscribed (enable);
        this.launcherClip.getPlayStart ().setIsSubscribed (enable);
        this.launcherClip.getPlayStop ().setIsSubscribed (enable);
        this.launcherClip.getLoopStart ().setIsSubscribed (enable);
        this.launcherClip.getLoopLength ().setIsSubscribed (enable);
        this.launcherClip.isLoopEnabled ().setIsSubscribed (enable);
        this.launcherClip.getShuffle ().setIsSubscribed (enable);
        this.launcherClip.getAccent ().setIsSubscribed (enable);
        this.launcherClip.canScrollStepsBackwards ().setIsSubscribed (enable);
        this.launcherClip.canScrollStepsForwards ().setIsSubscribed (enable);
        this.launcherClip.color ().setIsSubscribed (enable);

        this.arrangerClip.playingStep ().setIsSubscribed (enable);
        this.arrangerClip.getPlayStart ().setIsSubscribed (enable);
        this.arrangerClip.getPlayStop ().setIsSubscribed (enable);
        this.arrangerClip.getLoopStart ().setIsSubscribed (enable);
        this.arrangerClip.getLoopLength ().setIsSubscribed (enable);
        this.arrangerClip.isLoopEnabled ().setIsSubscribed (enable);
        this.arrangerClip.getShuffle ().setIsSubscribed (enable);
        this.arrangerClip.getAccent ().setIsSubscribed (enable);
        this.arrangerClip.canScrollStepsBackwards ().setIsSubscribed (enable);
        this.arrangerClip.canScrollStepsForwards ().setIsSubscribed (enable);
        this.arrangerClip.color ().setIsSubscribed (enable);
    }


    /** {@inheritDoc} */
    @Override
    public void setColor (final double red, final double green, final double blue)
    {
        this.getClip ().color ().set ((float) red, (float) green, (float) blue);
    }


    /** {@inheritDoc} */
    @Override
    public double [] getColor ()
    {
        final SettableColorValue color = this.getClip ().color ();
        return new double []
        {
            color.red (),
            color.green (),
            color.blue ()
        };
    }


    /** {@inheritDoc} */
    @Override
    public double getPlayStart ()
    {
        return this.getClip ().getPlayStart ().get ();
    }


    /** {@inheritDoc} */
    @Override
    public void setPlayStart (final double start)
    {
        this.getClip ().getPlayStart ().set (start);
    }


    /** {@inheritDoc} */
    @Override
    public void changePlayStart (final int control)
    {
        this.getClip ().getPlayStart ().inc (this.valueChanger.calcKnobSpeed (control, this.valueChanger.isSlow () ? 0.1 : 1));
    }


    /** {@inheritDoc} */
    @Override
    public double getPlayEnd ()
    {
        return this.getClip ().getPlayStop ().get ();
    }


    /** {@inheritDoc} */
    @Override
    public void setPlayEnd (final double end)
    {
        this.getClip ().getPlayStop ().set (end);
    }


    /** {@inheritDoc} */
    @Override
    public void changePlayEnd (final int control)
    {
        this.getClip ().getPlayStop ().inc (this.valueChanger.calcKnobSpeed (control, this.valueChanger.isSlow () ? 0.1 : 1));
    }


    /** {@inheritDoc} */
    @Override
    public void setPlayRange (final double start, final double end)
    {
        // Need to distinguish if we move left or right since the start and
        // end cannot be the same value
        if (this.getPlayStart () < start)
        {
            this.setPlayEnd (end);
            this.setPlayStart (start);
        }
        else
        {
            this.setPlayStart (start);
            this.setPlayEnd (end);
        }
    }


    /** {@inheritDoc} */
    @Override
    public double getLoopStart ()
    {
        return this.getClip ().getLoopStart ().get ();
    }


    /** {@inheritDoc} */
    @Override
    public void setLoopStart (final double start)
    {
        this.getClip ().getLoopStart ().set (start);
    }


    /** {@inheritDoc} */
    @Override
    public void changeLoopStart (final int control)
    {
        this.getClip ().getLoopStart ().inc (this.valueChanger.calcKnobSpeed (control, this.valueChanger.isSlow () ? 0.1 : 1));
    }


    /** {@inheritDoc} */
    @Override
    public double getLoopLength ()
    {
        return this.getClip ().getLoopLength ().get ();
    }


    /** {@inheritDoc} */
    @Override
    public void setLoopLength (final int length)
    {
        this.getClip ().getLoopLength ().set (length);
    }


    /** {@inheritDoc} */
    @Override
    public void changeLoopLength (final int control)
    {
        this.getClip ().getLoopLength ().inc (this.valueChanger.calcKnobSpeed (control, this.valueChanger.isSlow () ? 0.1 : 1));
    }


    /** {@inheritDoc} */
    @Override
    public boolean isLoopEnabled ()
    {
        return this.getClip ().isLoopEnabled ().get ();
    }


    /** {@inheritDoc} */
    @Override
    public void setLoopEnabled (final boolean enable)
    {
        this.getClip ().isLoopEnabled ().set (enable);
    }


    /** {@inheritDoc} */
    @Override
    public boolean isShuffleEnabled ()
    {
        return this.getClip ().getShuffle ().get ();
    }


    /** {@inheritDoc} */
    @Override
    public void setShuffleEnabled (final boolean enable)
    {
        this.getClip ().getShuffle ().set (enable);
    }


    /** {@inheritDoc} */
    @Override
    public String getFormattedAccent ()
    {
        return Math.round (this.getAccent () * 200 - 100) + "%";
    }


    /** {@inheritDoc} */
    @Override
    public double getAccent ()
    {
        return this.getClip ().getAccent ().get ();
    }


    /** {@inheritDoc} */
    @Override
    public void resetAccent ()
    {
        this.getClip ().getAccent ().setImmediately (0.5);
    }


    /** {@inheritDoc} */
    @Override
    public void changeAccent (final int control)
    {
        final double speed = this.valueChanger.calcKnobSpeed (control, this.valueChanger.getFractionValue () / 100.0);
        this.getClip ().getAccent ().inc (speed);
    }


    /** {@inheritDoc} */
    @Override
    public int getNumSteps ()
    {
        return this.numSteps;
    }


    /** {@inheritDoc} */
    @Override
    public int getNumRows ()
    {
        return this.numRows;
    }


    /** {@inheritDoc} */
    @Override
    public int getCurrentStep ()
    {
        return this.getClip ().playingStep ().get ();
    }


    /** {@inheritDoc} */
    @Override
    public void setStepLength (final double length)
    {
        this.stepLength = length;
        this.getClip ().setStepSize (length);
    }


    /** {@inheritDoc} */
    @Override
    public double getStepLength ()
    {
        return this.stepLength;
    }


    /** {@inheritDoc} */
    @Override
    public IStepInfo getStep (final int step, final int row)
    {
        return this.getData ()[step][row];
    }


    /** {@inheritDoc} */
    @Override
    public void toggleStep (final int step, final int row, final int velocity)
    {
        this.getClip ().toggleStep (step, row, velocity);
    }


    /** {@inheritDoc} */
    @Override
    public void setStep (final int step, final int row, final int velocity, final double duration)
    {
        this.getClip ().setStep (step, row, velocity, duration);
    }


    /** {@inheritDoc} */
    @Override
    public void clearStep (final int step, final int row)
    {
        this.getClip ().clearStep (step, row);
    }


    /** {@inheritDoc} */
    @Override
    public void updateStepDuration (final int step, final int row, final double duration)
    {
        final StepInfoImpl stepInfo = this.getData ()[step][row];
        stepInfo.setDuration (duration);
        if (!stepInfo.isEditing ())
            this.getClip ().updateStepDuration (step, row, duration);
    }


    /** {@inheritDoc} */
    @Override
    public void changeStepDuration (final int step, final int row, final int control)
    {
        final IStepInfo info = this.getStep (step, row);
        final double frac = this.valueChanger.isSlow () ? TransportConstants.INC_FRACTION_TIME_SLOW / 16.0 : TransportConstants.INC_FRACTION_TIME_SLOW;
        this.updateStepDuration (step, row, Math.max (0, info.getDuration () + this.valueChanger.calcKnobSpeed (control, frac)));
    }


    /** {@inheritDoc} */
    @Override
    public void updateStepVelocity (final int step, final int row, final double velocity)
    {
        final StepInfoImpl stepInfo = this.getData ()[step][row];
        stepInfo.setVelocity (velocity);
        if (!stepInfo.isEditing ())
            this.getClip ().updateStepVelocity (step, row, velocity);
    }


    /** {@inheritDoc} */
    @Override
    public void changeStepVelocity (final int step, final int row, final int control)
    {
        final IStepInfo info = this.getStep (step, row);
        final double velocity = info.getVelocity () + this.valueChanger.toNormalizedValue ((int) this.valueChanger.calcKnobSpeed (control));
        this.updateStepVelocity (step, row, Math.min (1.0, Math.max (0, velocity)));
    }


    /** {@inheritDoc} */
    @Override
    public void updateStepReleaseVelocity (final int step, final int row, final double releaseVelocity)
    {
        final StepInfoImpl stepInfo = this.getData ()[step][row];
        stepInfo.setReleaseVelocity (releaseVelocity);
        if (!stepInfo.isEditing ())
            this.getClip ().updateStepReleaseVelocity (step, row, releaseVelocity);
    }


    /** {@inheritDoc} */
    @Override
    public void changeStepReleaseVelocity (final int step, final int row, final int control)
    {
        final IStepInfo info = this.getStep (step, row);
        final double velocity = info.getReleaseVelocity () + this.valueChanger.toNormalizedValue ((int) this.valueChanger.calcKnobSpeed (control));
        this.updateStepReleaseVelocity (step, row, Math.min (1.0, Math.max (0, velocity)));
    }


    /** {@inheritDoc} */
    @Override
    public void updateStepPressure (final int step, final int row, final double pressure)
    {
        final StepInfoImpl stepInfo = this.getData ()[step][row];
        stepInfo.setPressure (pressure);
        if (!stepInfo.isEditing ())
            this.getClip ().updateStepPressure (step, row, pressure);
    }


    /** {@inheritDoc} */
    @Override
    public void changeStepPressure (final int step, final int row, final int control)
    {
        final IStepInfo info = this.getStep (step, row);
        final double pressure = info.getPressure () + this.valueChanger.toNormalizedValue ((int) this.valueChanger.calcKnobSpeed (control));
        this.updateStepPressure (step, row, Math.min (1.0, Math.max (0, pressure)));
    }


    /** {@inheritDoc} */
    @Override
    public void updateStepTimbre (final int step, final int row, final double timbre)
    {
        final StepInfoImpl stepInfo = this.getData ()[step][row];
        stepInfo.setTimbre (timbre);
        if (!stepInfo.isEditing ())
            this.getClip ().updateStepTimbre (step, row, timbre);
    }


    /** {@inheritDoc} */
    @Override
    public void changeStepTimbre (final int step, final int row, final int control)
    {
        final IStepInfo info = this.getStep (step, row);
        final double timbre = info.getTimbre () + 2.0 * this.valueChanger.toNormalizedValue ((int) this.valueChanger.calcKnobSpeed (control));
        this.updateStepTimbre (step, row, Math.min (1.0, Math.max (-1.0, timbre)));
    }


    /** {@inheritDoc} */
    @Override
    public void updateStepPan (final int step, final int row, final double pan)
    {
        final StepInfoImpl stepInfo = this.getData ()[step][row];
        stepInfo.setPan (pan);
        if (!stepInfo.isEditing ())
            this.getClip ().updateStepPan (step, row, pan);
    }


    /** {@inheritDoc} */
    @Override
    public void changeStepPan (final int step, final int row, final int control)
    {
        final IStepInfo info = this.getStep (step, row);
        final double pan = info.getPan () + 2.0 * this.valueChanger.toNormalizedValue ((int) this.valueChanger.calcKnobSpeed (control));
        this.updateStepPan (step, row, Math.min (1.0, Math.max (-1.0, pan)));
    }


    /** {@inheritDoc} */
    @Override
    public void updateStepTranspose (final int step, final int row, final double transpose)
    {
        final StepInfoImpl stepInfo = this.getData ()[step][row];
        stepInfo.setTranspose (transpose);
        if (!stepInfo.isEditing ())
            this.getClip ().updateStepTranspose (step, row, transpose);
    }


    /** {@inheritDoc} */
    @Override
    public void changeStepTranspose (final int step, final int row, final int control)
    {
        final IStepInfo info = this.getStep (step, row);
        final double transpose = info.getTranspose () + this.valueChanger.calcKnobSpeed (control) / 8.0;
        this.updateStepTranspose (step, row, Math.min (24.0, Math.max (-24.0, transpose)));
    }


    /** {@inheritDoc} */
    @Override
    public void clearRow (final int row)
    {
        this.getClip ().clearSteps (row);
    }


    /** {@inheritDoc} */
    @Override
    public boolean hasRowData (final int row)
    {
        final IStepInfo [] [] data = this.getData ();
        for (int step = 0; step < this.numSteps; step++)
        {
            if (data[step][row].getState () > 0)
                return true;
        }
        return false;
    }


    /** {@inheritDoc} */
    @Override
    public int getLowerRowWithData ()
    {
        for (int row = 0; row < this.numRows; row++)
            if (this.hasRowData (row))
                return row;
        return -1;
    }


    /** {@inheritDoc} */
    @Override
    public int getUpperRowWithData ()
    {
        for (int row = this.numRows - 1; row >= 0; row--)
            if (this.hasRowData (row))
                return row;
        return -1;
    }


    /** {@inheritDoc} */
    @Override
    public void scrollToPage (final int page)
    {
        this.getClip ().scrollToStep (page * this.numSteps);
        this.editPage = page;
    }


    /** {@inheritDoc} */
    @Override
    public int getEditPage ()
    {
        return this.editPage;
    }


    /** {@inheritDoc} */
    @Override
    public void scrollStepsPageBackwards ()
    {
        if (this.editPage <= 0)
            return;
        this.getClip ().scrollStepsPageBackwards ();
        this.editPage--;
    }


    /** {@inheritDoc} */
    @Override
    public void scrollStepsPageForward ()
    {
        this.getClip ().scrollStepsPageForward ();
        this.editPage++;
    }


    /** {@inheritDoc} */
    @Override
    public boolean canScrollStepsBackwards ()
    {
        // TODO Bugfix required: https://github.com/teotigraphix/Framework4Bitwig/issues/217
        return this.getEditPage () > 0;
    }


    /** {@inheritDoc} */
    @Override
    public boolean canScrollStepsForwards ()
    {
        // TODO Bugfix required: https://github.com/teotigraphix/Framework4Bitwig/issues/217
        return true;
    }


    /** {@inheritDoc} */
    @Override
    public void duplicate ()
    {
        this.getClip ().duplicate ();
    }


    /** {@inheritDoc} */
    @Override
    public void duplicateContent ()
    {
        this.getClip ().duplicateContent ();
    }


    /** {@inheritDoc} */
    @Override
    public void quantize (final double amount)
    {
        if (amount < 0.000001 || amount > 1)
            return;
        this.getClip ().quantize (amount);
    }


    /** {@inheritDoc} */
    @Override
    public void transpose (final int semitones)
    {
        this.getClip ().transpose (semitones);
    }


    /** {@inheritDoc} */
    @Override
    public void edit (final int step, final int row, final boolean enable)
    {
        final StepInfoImpl stepInfo = this.getData ()[step][row];
        if (enable)
        {
            stepInfo.setEditing (true);
            this.delayedUpdate (step, row);
            return;
        }

        this.sendClipData (step, row);
        stepInfo.setEditing (false);
    }


    private void delayedUpdate (final int step, final int row)
    {
        final StepInfoImpl stepInfo = this.getData ()[step][row];
        if (!stepInfo.isEditing ())
            return;
        this.sendClipData (step, row);
        this.host.scheduleTask ( () -> delayedUpdate (step, row), 100);
    }


    /**
     * Update the locally changed step data in Bitwig.
     *
     * @param step The step of the clip
     * @param row The row of the clip
     */
    private void sendClipData (final int step, final int row)
    {
        final IStepInfo stepInfo = this.getData ()[step][row];
        final Clip clip = this.getClip ();
        clip.updateStepDuration (step, row, stepInfo.getDuration ());
        clip.updateStepVelocity (step, row, stepInfo.getVelocity ());
        clip.updateStepReleaseVelocity (step, row, stepInfo.getReleaseVelocity ());
        clip.updateStepPressure (step, row, stepInfo.getPressure ());
        clip.updateStepTimbre (step, row, stepInfo.getTimbre ());
        clip.updateStepPan (step, row, stepInfo.getPan ());
        clip.updateStepTranspose (step, row, stepInfo.getTranspose ());
    }


    private void handleStepData (final StepInfo stepInfo)
    {
        final int step = stepInfo.getX ();
        final int row = stepInfo.getY ();
        final StepInfoImpl sinfo = this.getData ()[step][row];
        if (!sinfo.isEditing ())
            sinfo.updateData (stepInfo);
    }


    private Clip getClip ()
    {
        return this.launcherClip.exists ().get () ? this.launcherClip : this.arrangerClip;
    }


    private StepInfoImpl [] [] getData ()
    {
        return this.launcherClip.exists ().get () ? this.launcherData : this.arrangerData;
    }
}