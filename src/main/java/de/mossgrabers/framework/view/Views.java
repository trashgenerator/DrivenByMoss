// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.framework.view;

import de.mossgrabers.framework.daw.IHost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Static view IDs.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public enum Views
{
    /** View for playing notes. */
    PLAY,
    /** View for a session grid with clips. */
    SESSION,
    /** View for a sequencer. */
    SEQUENCER,
    /** View for playing drums and sequencing. */
    DRUM,
    /** View for raindrops sequencer. */
    RAINDROPS,
    /** View for playing in piano keyboard style. */
    PIANO,
    /** View sending program changes. */
    PRG_CHANGE,
    /** View for editing the clip length. */
    CLIP,
    /** View for drum sequencing with 4 sounds. */
    DRUM4,
    /** View for drum sequencing with 8 sounds. */
    DRUM8,
    /** View for drum playing with 64 pads. */
    DRUM64,
    /** View for selecting a color. */
    COLOR,
    /** View for playing scenes. */
    SCENE_PLAY,
    /** View for the poly sequencer. */
    POLY_SEQUENCER,
    /** View for mixing */
    MIXING,

    /** View for browsing. */
    BROWSER,
    /** View for changing track parameters. */
    TRACK,
    /** View for changing track volumes. */
    TRACK_VOLUME,
    /** View for changing track panoramas. */
    TRACK_PAN,
    /** View for changing track sends. */
    TRACK_SENDS,
    /** View for Track selection. */
    TRACK_SELECT,
    /** View for soloing tracks. */
    TRACK_SOLO,
    /** View for muting tracks. */
    TRACK_MUTE,
    /** View for editing remote parameters. */
    DEVICE,

    /** View for controlling values. */
    CONTROL,
    /** View for shift options. */
    SHIFT;


    /** The name of the play view. */
    public static final String              VIEW_NAME_PLAY           = "Play";
    /** The name of the piano view. */
    public static final String              VIEW_NAME_PIANO          = "Piano";
    /** The name of the drum view. */
    public static final String              VIEW_NAME_DRUM           = "Drum";
    /** The name of the drum4 view. */
    public static final String              VIEW_NAME_DRUM4          = "Drum 4";
    /** The name of the drum 8 view. */
    public static final String              VIEW_NAME_DRUM8          = "Drum 8";
    /** The name of the drum 64 view. */
    public static final String              VIEW_NAME_DRUM64         = "Drum 64";
    /** The name of the sequencer view. */
    public static final String              VIEW_NAME_SEQUENCER      = "Sequencer";
    /** The name of the raindrops view. */
    public static final String              VIEW_NAME_RAINDROPS      = "Raindrop";
    /** The name of the poly sequencer view. */
    public static final String              VIEW_NAME_POLY_SEQUENCER = "Poly Seq.";

    private static final Map<String, Views> NOTE_VIEW_NAMES          = new HashMap<> ();
    private static final List<Views>        NOTE_VIEWS               = new ArrayList<> ();
    private static final Set<Views>         SEQUENCER_VIEWS          = new HashSet<> ();
    private static final Set<Views>         SESSION_VIEWS            = new HashSet<> ();

    private static boolean                  isInitialised            = false;


    /**
     * Private due to utility class.
     */
    private Views ()
    {
        // Intentionally empty
    }


    /**
     * Initialise.
     *
     * @param host The DAW host
     */
    public static void init (final IHost host)
    {
        if (isInitialised)
            return;
        isInitialised = true;

        NOTE_VIEWS.add (PLAY);
        NOTE_VIEWS.add (PIANO);
        NOTE_VIEWS.add (DRUM64);
        NOTE_VIEW_NAMES.put (VIEW_NAME_PLAY, PLAY);
        NOTE_VIEW_NAMES.put (VIEW_NAME_PIANO, PIANO);
        NOTE_VIEW_NAMES.put (VIEW_NAME_DRUM64, DRUM64);

        if (host.hasDrumDevice ())
        {
            NOTE_VIEWS.add (DRUM);
            NOTE_VIEWS.add (DRUM4);
            NOTE_VIEWS.add (DRUM8);
            NOTE_VIEW_NAMES.put (VIEW_NAME_DRUM, DRUM);
            NOTE_VIEW_NAMES.put (VIEW_NAME_DRUM4, DRUM4);
            NOTE_VIEW_NAMES.put (VIEW_NAME_DRUM8, DRUM8);

            SEQUENCER_VIEWS.add (DRUM);
            SEQUENCER_VIEWS.add (DRUM4);
            SEQUENCER_VIEWS.add (DRUM8);
        }

        NOTE_VIEWS.add (SEQUENCER);
        NOTE_VIEWS.add (RAINDROPS);
        NOTE_VIEW_NAMES.put (VIEW_NAME_SEQUENCER, SEQUENCER);
        NOTE_VIEW_NAMES.put (VIEW_NAME_RAINDROPS, RAINDROPS);

        SEQUENCER_VIEWS.add (SEQUENCER);
        SEQUENCER_VIEWS.add (RAINDROPS);
        SEQUENCER_VIEWS.add (POLY_SEQUENCER);

        SESSION_VIEWS.add (SESSION);
        SESSION_VIEWS.add (SCENE_PLAY);
    }


    /**
     * Returns true if the given view ID is one of the sequencer views.
     *
     * @param viewId The view ID to test
     * @return True if it is a sequencer view
     */
    public static boolean isSequencerView (final Views viewId)
    {
        return SEQUENCER_VIEWS.contains (viewId);
    }


    /**
     * Returns true if the given view ID is one of the session views.
     *
     * @param viewId The view ID to test
     * @return True if it is a session view
     */
    public static boolean isSessionView (final Views viewId)
    {
        return SESSION_VIEWS.contains (viewId);
    }


    /**
     * Returns true if the given view ID is one of the note views.
     *
     * @param viewId The view ID to test
     * @return True if it is a note view
     */
    public static boolean isNoteView (final Views viewId)
    {
        return NOTE_VIEWS.contains (viewId);
    }


    /**
     * Get the note view names.
     *
     * @return The names
     */
    public static String [] getNoteViewNames ()
    {
        final String [] array = NOTE_VIEW_NAMES.keySet ().toArray (new String [NOTE_VIEW_NAMES.size ()]);
        Arrays.sort (array);
        return array;
    }


    /**
     * Get the note view at the given index.
     *
     * @param name The name of the note view
     * @return The note view
     */
    public static Views getNoteView (final String name)
    {
        return NOTE_VIEW_NAMES.get (name);
    }


    /**
     * Get an offset view.
     *
     * @param view The base view
     * @param offset The offset
     * @return The offset view
     */
    public static Views get (final Views view, final int offset)
    {
        return Views.values ()[view.ordinal () + offset];
    }
}
