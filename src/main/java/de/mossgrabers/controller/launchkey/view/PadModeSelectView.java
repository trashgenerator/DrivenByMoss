// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.launchkey.view;

import de.mossgrabers.controller.launchkey.LaunchkeyMiniMk3Configuration;
import de.mossgrabers.controller.launchkey.controller.LaunchkeyMiniMk3Colors;
import de.mossgrabers.controller.launchkey.controller.LaunchkeyMiniMk3ControlSurface;
import de.mossgrabers.framework.controller.grid.PadGrid;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.ISceneBank;
import de.mossgrabers.framework.daw.data.IScene;
import de.mossgrabers.framework.mode.Modes;
import de.mossgrabers.framework.utils.ButtonEvent;
import de.mossgrabers.framework.view.AbstractView;
import de.mossgrabers.framework.view.SceneView;
import de.mossgrabers.framework.view.Views;


/**
 * The pad mode select view.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class PadModeSelectView extends AbstractView<LaunchkeyMiniMk3ControlSurface, LaunchkeyMiniMk3Configuration> implements SceneView
{
    private static final String [] PAD_MODE_NAMES =
    {
        "Clips",
        "Record Arm",
        "Track Select",
        "Mute",
        "Solo",
        "Stop Clips"
    };

    private boolean                isConsumed     = false;


    /**
     * Constructor.
     *
     * @param surface The surface
     * @param model The model
     */
    public PadModeSelectView (final LaunchkeyMiniMk3ControlSurface surface, final IModel model)
    {
        super ("Pad Mode Select", surface, model);
    }


    /** {@inheritDoc} */
    @Override
    public void drawGrid ()
    {
        final PadGrid pads = this.surface.getPadGrid ();
        for (int x = 0; x < 8; x++)
            pads.lightEx (x, 0, LaunchkeyMiniMk3Colors.LAUNCHKEY_COLOR_BLACK);

        final SessionView view = (SessionView) this.surface.getViewManager ().getView (Views.SESSION);
        final Modes padMode = view.getPadMode ();
        pads.lightEx (0, 1, padMode == null ? LaunchkeyMiniMk3Colors.LAUNCHKEY_COLOR_GREEN_HI : LaunchkeyMiniMk3Colors.LAUNCHKEY_COLOR_GREEN_LO);
        pads.lightEx (1, 1, padMode == Modes.REC_ARM ? LaunchkeyMiniMk3Colors.LAUNCHKEY_COLOR_RED_HI : LaunchkeyMiniMk3Colors.LAUNCHKEY_COLOR_RED_LO);
        pads.lightEx (2, 1, padMode == Modes.TRACK_SELECT ? LaunchkeyMiniMk3Colors.LAUNCHKEY_COLOR_WHITE : LaunchkeyMiniMk3Colors.LAUNCHKEY_COLOR_GREY_LO);
        pads.lightEx (3, 1, padMode == Modes.MUTE ? LaunchkeyMiniMk3Colors.LAUNCHKEY_COLOR_AMBER_HI : LaunchkeyMiniMk3Colors.LAUNCHKEY_COLOR_AMBER_LO);
        pads.lightEx (4, 1, padMode == Modes.SOLO ? LaunchkeyMiniMk3Colors.LAUNCHKEY_COLOR_YELLOW_HI : LaunchkeyMiniMk3Colors.LAUNCHKEY_COLOR_YELLOW_LO);
        pads.lightEx (5, 1, padMode == Modes.STOP_CLIP ? LaunchkeyMiniMk3Colors.LAUNCHKEY_COLOR_PINK_HI : LaunchkeyMiniMk3Colors.LAUNCHKEY_COLOR_ROSE);

        pads.lightEx (6, 1, LaunchkeyMiniMk3Colors.LAUNCHKEY_COLOR_BLACK);
        pads.lightEx (7, 1, LaunchkeyMiniMk3Colors.LAUNCHKEY_COLOR_BLACK);
    }


    /** {@inheritDoc} */
    @Override
    public void onGridNote (final int note, final int velocity)
    {
        if (velocity == 0)
            return;

        final int index = note - 36;
        if (index > 5)
            return;

        final SessionView view = (SessionView) this.surface.getViewManager ().getView (Views.SESSION);
        view.setPadMode (index == 0 ? null : SessionView.PAD_MODES[index - 1]);
        this.surface.getDisplay ().notify (PAD_MODE_NAMES[index]);

        this.isConsumed = true;
    }


    /** {@inheritDoc} */
    @Override
    public void onScene (final int sceneIndex, final ButtonEvent event)
    {
        if (event == ButtonEvent.UP)
        {
            this.surface.getViewManager ().restoreView ();

            if (this.isConsumed)
                return;

            final ISceneBank sceneBank = this.model.getCurrentTrackBank ().getSceneBank ();
            final IScene scene = sceneBank.getItem (sceneIndex);
            scene.select ();
            scene.launch ();
        }
        else if (event == ButtonEvent.LONG)
            this.isConsumed = true;
    }


    /** {@inheritDoc} */
    @Override
    public void updateSceneButtons ()
    {
        this.surface.updateTrigger (LaunchkeyMiniMk3ControlSurface.LAUNCHKEY_SCENE1, LaunchkeyMiniMk3Colors.LAUNCHKEY_COLOR_BLACK);
        this.surface.updateTrigger (LaunchkeyMiniMk3ControlSurface.LAUNCHKEY_SCENE2, LaunchkeyMiniMk3Colors.LAUNCHKEY_COLOR_WHITE);
    }


    /** {@inheritDoc} */
    @Override
    public void onActivate ()
    {
        this.isConsumed = false;
    }
}