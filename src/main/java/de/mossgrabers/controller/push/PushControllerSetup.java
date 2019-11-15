// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.push;

import de.mossgrabers.controller.push.command.continuous.ConfigurePitchbendCommand;
import de.mossgrabers.controller.push.command.continuous.MastertrackTouchCommand;
import de.mossgrabers.controller.push.command.continuous.SmallKnobTouchCommand;
import de.mossgrabers.controller.push.command.pitchbend.PitchbendCommand;
import de.mossgrabers.controller.push.command.pitchbend.PitchbendSessionCommand;
import de.mossgrabers.controller.push.command.trigger.AccentCommand;
import de.mossgrabers.controller.push.command.trigger.AutomationCommand;
import de.mossgrabers.controller.push.command.trigger.ClipCommand;
import de.mossgrabers.controller.push.command.trigger.DeviceCommand;
import de.mossgrabers.controller.push.command.trigger.FixedLengthCommand;
import de.mossgrabers.controller.push.command.trigger.LayoutCommand;
import de.mossgrabers.controller.push.command.trigger.MastertrackCommand;
import de.mossgrabers.controller.push.command.trigger.MuteCommand;
import de.mossgrabers.controller.push.command.trigger.OctaveCommand;
import de.mossgrabers.controller.push.command.trigger.PageLeftCommand;
import de.mossgrabers.controller.push.command.trigger.PageRightCommand;
import de.mossgrabers.controller.push.command.trigger.PanSendCommand;
import de.mossgrabers.controller.push.command.trigger.PushBrowserCommand;
import de.mossgrabers.controller.push.command.trigger.PushQuantizeCommand;
import de.mossgrabers.controller.push.command.trigger.RasteredKnobCommand;
import de.mossgrabers.controller.push.command.trigger.ScalesCommand;
import de.mossgrabers.controller.push.command.trigger.SelectCommand;
import de.mossgrabers.controller.push.command.trigger.SelectPlayViewCommand;
import de.mossgrabers.controller.push.command.trigger.SelectSessionViewCommand;
import de.mossgrabers.controller.push.command.trigger.SetupCommand;
import de.mossgrabers.controller.push.command.trigger.ShiftCommand;
import de.mossgrabers.controller.push.command.trigger.SoloCommand;
import de.mossgrabers.controller.push.command.trigger.TrackCommand;
import de.mossgrabers.controller.push.command.trigger.VolumeCommand;
import de.mossgrabers.controller.push.controller.Push1Display;
import de.mossgrabers.controller.push.controller.Push2Display;
import de.mossgrabers.controller.push.controller.PushColors;
import de.mossgrabers.controller.push.controller.PushControlSurface;
import de.mossgrabers.controller.push.mode.AccentMode;
import de.mossgrabers.controller.push.mode.AutomationMode;
import de.mossgrabers.controller.push.mode.ConfigurationMode;
import de.mossgrabers.controller.push.mode.FixedMode;
import de.mossgrabers.controller.push.mode.FrameMode;
import de.mossgrabers.controller.push.mode.GrooveMode;
import de.mossgrabers.controller.push.mode.InfoMode;
import de.mossgrabers.controller.push.mode.MarkersMode;
import de.mossgrabers.controller.push.mode.NoteMode;
import de.mossgrabers.controller.push.mode.NoteRepeatMode;
import de.mossgrabers.controller.push.mode.NoteViewSelectMode;
import de.mossgrabers.controller.push.mode.RibbonMode;
import de.mossgrabers.controller.push.mode.ScaleLayoutMode;
import de.mossgrabers.controller.push.mode.ScalesMode;
import de.mossgrabers.controller.push.mode.SessionMode;
import de.mossgrabers.controller.push.mode.SessionViewSelectMode;
import de.mossgrabers.controller.push.mode.SetupMode;
import de.mossgrabers.controller.push.mode.TransportMode;
import de.mossgrabers.controller.push.mode.device.DeviceBrowserMode;
import de.mossgrabers.controller.push.mode.device.DeviceChainsMode;
import de.mossgrabers.controller.push.mode.device.DeviceLayerMode;
import de.mossgrabers.controller.push.mode.device.DeviceLayerModePan;
import de.mossgrabers.controller.push.mode.device.DeviceLayerModeSend;
import de.mossgrabers.controller.push.mode.device.DeviceLayerModeVolume;
import de.mossgrabers.controller.push.mode.device.DeviceParamsMode;
import de.mossgrabers.controller.push.mode.device.UserParamsMode;
import de.mossgrabers.controller.push.mode.track.ClipMode;
import de.mossgrabers.controller.push.mode.track.CrossfaderMode;
import de.mossgrabers.controller.push.mode.track.LayerDetailsMode;
import de.mossgrabers.controller.push.mode.track.MasterMode;
import de.mossgrabers.controller.push.mode.track.PanMode;
import de.mossgrabers.controller.push.mode.track.SendMode;
import de.mossgrabers.controller.push.mode.track.TrackDetailsMode;
import de.mossgrabers.controller.push.mode.track.TrackMode;
import de.mossgrabers.controller.push.mode.track.VolumeMode;
import de.mossgrabers.controller.push.view.*;
import de.mossgrabers.framework.command.ContinuousCommandID;
import de.mossgrabers.framework.command.SceneCommand;
import de.mossgrabers.framework.command.TriggerCommandID;
import de.mossgrabers.framework.command.aftertouch.AftertouchAbstractViewCommand;
import de.mossgrabers.framework.command.continuous.FootswitchCommand;
import de.mossgrabers.framework.command.continuous.KnobRowModeCommand;
import de.mossgrabers.framework.command.continuous.MasterVolumeCommand;
import de.mossgrabers.framework.command.continuous.PlayPositionCommand;
import de.mossgrabers.framework.command.core.NopCommand;
import de.mossgrabers.framework.command.trigger.application.DeleteCommand;
import de.mossgrabers.framework.command.trigger.application.DuplicateCommand;
import de.mossgrabers.framework.command.trigger.application.UndoCommand;
import de.mossgrabers.framework.command.trigger.clip.ConvertCommand;
import de.mossgrabers.framework.command.trigger.clip.DoubleCommand;
import de.mossgrabers.framework.command.trigger.clip.NewCommand;
import de.mossgrabers.framework.command.trigger.clip.NoteRepeatCommand;
import de.mossgrabers.framework.command.trigger.clip.StopAllClipsCommand;
import de.mossgrabers.framework.command.trigger.device.AddEffectCommand;
import de.mossgrabers.framework.command.trigger.mode.ButtonRowModeCommand;
import de.mossgrabers.framework.command.trigger.mode.CursorCommand;
import de.mossgrabers.framework.command.trigger.mode.KnobRowTouchModeCommand;
import de.mossgrabers.framework.command.trigger.mode.ModeCursorCommand.Direction;
import de.mossgrabers.framework.command.trigger.mode.ModeSelectCommand;
import de.mossgrabers.framework.command.trigger.track.AddTrackCommand;
import de.mossgrabers.framework.command.trigger.transport.MetronomeCommand;
import de.mossgrabers.framework.command.trigger.transport.PlayCommand;
import de.mossgrabers.framework.command.trigger.transport.RecordCommand;
import de.mossgrabers.framework.command.trigger.transport.TapTempoCommand;
import de.mossgrabers.framework.configuration.AbstractConfiguration;
import de.mossgrabers.framework.configuration.ISettingsUI;
import de.mossgrabers.framework.controller.AbstractControllerSetup;
import de.mossgrabers.framework.controller.DefaultValueChanger;
import de.mossgrabers.framework.controller.ISetupFactory;
import de.mossgrabers.framework.controller.color.ColorManager;
import de.mossgrabers.framework.daw.ICursorDevice;
import de.mossgrabers.framework.daw.IHost;
import de.mossgrabers.framework.daw.INoteClip;
import de.mossgrabers.framework.daw.IParameterBank;
import de.mossgrabers.framework.daw.ISendBank;
import de.mossgrabers.framework.daw.ITrackBank;
import de.mossgrabers.framework.daw.ITransport;
import de.mossgrabers.framework.daw.ModelSetup;
import de.mossgrabers.framework.daw.data.IChannel;
import de.mossgrabers.framework.daw.data.ITrack;
import de.mossgrabers.framework.daw.midi.DeviceInquiry;
import de.mossgrabers.framework.daw.midi.IMidiAccess;
import de.mossgrabers.framework.daw.midi.IMidiInput;
import de.mossgrabers.framework.daw.midi.IMidiOutput;
import de.mossgrabers.framework.mode.ModeManager;
import de.mossgrabers.framework.mode.Modes;
import de.mossgrabers.framework.view.AbstractSequencerView;
import de.mossgrabers.framework.view.AbstractView;
import de.mossgrabers.framework.view.SceneView;
import de.mossgrabers.framework.view.View;
import de.mossgrabers.framework.view.ViewManager;
import de.mossgrabers.framework.view.Views;


/**
 * Support for the Ableton Push 1 and Push 2 controllers.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class PushControllerSetup extends AbstractControllerSetup<PushControlSurface, PushConfiguration>
{
    protected final boolean isPush2;


    /**
     * Constructor.
     *
     * @param host The DAW host
     * @param factory The factory
     * @param globalSettings The global settings
     * @param documentSettings The document (project) specific settings
     * @param isPush2 True if Push 2
     */
    public PushControllerSetup (final IHost host, final ISetupFactory factory, final ISettingsUI globalSettings, final ISettingsUI documentSettings, final boolean isPush2)
    {
        super (factory, host, globalSettings, documentSettings);
        this.isPush2 = isPush2;
        this.colorManager = new ColorManager ();
        PushColors.addColors (this.colorManager, isPush2);
        this.valueChanger = new DefaultValueChanger (1024, 10, 1);
        this.configuration = new PushConfiguration (host, this.valueChanger, isPush2);
    }


    /** {@inheritDoc} */
    @Override
    public void flush ()
    {
        super.flush ();

        final PushControlSurface surface = this.getSurface ();
        this.updateMode (surface.getModeManager ().getActiveOrTempModeId ());

        final View activeView = surface.getViewManager ().getActiveView ();
        if (activeView == null)
            return;
        final de.mossgrabers.framework.command.core.PitchbendCommand pitchbendCommand = activeView.getPitchbendCommand ();
        if (pitchbendCommand != null)
            pitchbendCommand.updateValue ();
    }


    /** {@inheritDoc} */
    @Override
    protected void createModel ()
    {
        final ModelSetup ms = new ModelSetup ();
        if (this.isPush2)
        {
            ms.setNumFilterColumnEntries (48);
            ms.setNumResults (48);
        }
        ms.setNumMarkers (8);
        ms.setHasFlatTrackList (false);
        this.model = this.factory.createModel (this.colorManager, this.valueChanger, this.scales, ms);

        final ITrackBank trackBank = this.model.getTrackBank ();
        trackBank.setIndication (true);
        trackBank.addSelectionObserver ( (index, isSelected) -> this.handleTrackChange (isSelected));
        final ITrackBank effectTrackBank = this.model.getEffectTrackBank ();
        if (effectTrackBank != null)
            effectTrackBank.addSelectionObserver ( (index, isSelected) -> this.handleTrackChange (isSelected));
        this.model.getMasterTrack ().addSelectionObserver ( (index, isSelected) -> {
            final PushControlSurface surface = this.getSurface ();
            final ModeManager modeManager = surface.getModeManager ();
            if (isSelected)
                modeManager.setActiveMode (Modes.MASTER);
            else if (modeManager.isActiveOrTempMode (Modes.MASTER))
                modeManager.restoreMode ();
        });
    }


    /** {@inheritDoc} */
    @Override
    protected void createSurface ()
    {
        final IMidiAccess midiAccess = this.factory.createMidiAccess ();
        final IMidiOutput output = midiAccess.createOutput ();
        final IMidiInput input = midiAccess.createInput ("Pads", "80????" /* Note off */,
                "90????" /* Note on */, "B040??" /* Sustainpedal */);
        final PushControlSurface surface = new PushControlSurface (this.host, this.colorManager, this.configuration, output, input);
        this.surfaces.add (surface);

        if (this.isPush2)
            surface.addGraphicsDisplay (new Push2Display (this.host, this.valueChanger.getUpperBound (), this.configuration));
        else
            surface.addTextDisplay (new Push1Display (this.host, this.valueChanger.getUpperBound (), output, this.configuration));

        surface.getModeManager ().setDefaultMode (Modes.TRACK);
    }


    /** {@inheritDoc} */
    @Override
    protected void createModes ()
    {
        final PushControlSurface surface = this.getSurface ();
        final ModeManager modeManager = surface.getModeManager ();

        modeManager.registerMode (Modes.VOLUME, new VolumeMode (surface, this.model));
        modeManager.registerMode (Modes.PAN, new PanMode (surface, this.model));
        modeManager.registerMode (Modes.CROSSFADER, new CrossfaderMode (surface, this.model));

        final SendMode modeSend = new SendMode (surface, this.model);
        modeManager.registerMode (Modes.SEND1, modeSend);
        modeManager.registerMode (Modes.SEND2, modeSend);
        modeManager.registerMode (Modes.SEND3, modeSend);
        modeManager.registerMode (Modes.SEND4, modeSend);
        modeManager.registerMode (Modes.SEND5, modeSend);
        modeManager.registerMode (Modes.SEND6, modeSend);
        modeManager.registerMode (Modes.SEND7, modeSend);
        modeManager.registerMode (Modes.SEND8, modeSend);

        modeManager.registerMode (Modes.MASTER, new MasterMode (surface, this.model, false));
        modeManager.registerMode (Modes.MASTER_TEMP, new MasterMode (surface, this.model, true));

        modeManager.registerMode (Modes.TRACK, new TrackMode (surface, this.model));
        modeManager.registerMode (Modes.TRACK_DETAILS, new TrackDetailsMode (surface, this.model));
        modeManager.registerMode (Modes.DEVICE_LAYER_DETAILS, new LayerDetailsMode (surface, this.model));
        modeManager.registerMode (Modes.CLIP, new ClipMode (surface, this.model));
        modeManager.registerMode (Modes.NOTE, new NoteMode (surface, this.model));
        modeManager.registerMode (Modes.FRAME, new FrameMode (surface, this.model));
        modeManager.registerMode (Modes.SCALES, new ScalesMode (surface, this.model));
        modeManager.registerMode (Modes.SCALE_LAYOUT, new ScaleLayoutMode (surface, this.model));
        modeManager.registerMode (Modes.ACCENT, new AccentMode (surface, this.model));
        modeManager.registerMode (Modes.FIXED, new FixedMode (surface, this.model));
        modeManager.registerMode (Modes.RIBBON, new RibbonMode (surface, this.model));
        modeManager.registerMode (Modes.GROOVE, new GrooveMode (surface, this.model));
        modeManager.registerMode (Modes.VIEW_SELECT, new NoteViewSelectMode (surface, this.model));
        modeManager.registerMode (Modes.MARKERS, new MarkersMode (surface, this.model));

        modeManager.registerMode (Modes.AUTOMATION, new AutomationMode (surface, this.model));
        modeManager.registerMode (Modes.TRANSPORT, new TransportMode (surface, this.model));

        modeManager.registerMode (Modes.DEVICE_PARAMS, new DeviceParamsMode (surface, this.model));
        modeManager.registerMode (Modes.DEVICE_CHAINS, new DeviceChainsMode (surface, this.model));
        modeManager.registerMode (Modes.DEVICE_LAYER, new DeviceLayerMode ("Layer", surface, this.model));

        modeManager.registerMode (Modes.BROWSER, new DeviceBrowserMode (surface, this.model));

        modeManager.registerMode (Modes.DEVICE_LAYER_VOLUME, new DeviceLayerModeVolume (surface, this.model));
        modeManager.registerMode (Modes.DEVICE_LAYER_PAN, new DeviceLayerModePan (surface, this.model));
        final DeviceLayerModeSend modeLayerSend = new DeviceLayerModeSend (surface, this.model);
        modeManager.registerMode (Modes.DEVICE_LAYER_SEND1, modeLayerSend);
        modeManager.registerMode (Modes.DEVICE_LAYER_SEND2, modeLayerSend);
        modeManager.registerMode (Modes.DEVICE_LAYER_SEND3, modeLayerSend);
        modeManager.registerMode (Modes.DEVICE_LAYER_SEND4, modeLayerSend);
        modeManager.registerMode (Modes.DEVICE_LAYER_SEND5, modeLayerSend);
        modeManager.registerMode (Modes.DEVICE_LAYER_SEND6, modeLayerSend);
        modeManager.registerMode (Modes.DEVICE_LAYER_SEND7, modeLayerSend);
        modeManager.registerMode (Modes.DEVICE_LAYER_SEND8, modeLayerSend);

        if (this.host.hasUserParameters ())
            modeManager.registerMode (Modes.USER, new UserParamsMode (surface, this.model));

        if (this.isPush2)
        {
            modeManager.registerMode (Modes.SETUP, new SetupMode (surface, this.model));
            modeManager.registerMode (Modes.INFO, new InfoMode (surface, this.model));
        }
        else
            modeManager.registerMode (Modes.CONFIGURATION, new ConfigurationMode (surface, this.model));

        modeManager.registerMode (Modes.SESSION, new SessionMode (surface, this.model));
        modeManager.registerMode (Modes.SESSION_VIEW_SELECT, new SessionViewSelectMode (surface, this.model));

        modeManager.registerMode (Modes.REPEAT_NOTE, new NoteRepeatMode (surface, this.model));
    }


    /** {@inheritDoc} */
    @Override
    protected void createObservers ()
    {
        final PushControlSurface surface = this.getSurface ();
        if (this.configuration.isPush2 ())
        {
            this.configuration.addSettingObserver (PushConfiguration.DISPLAY_BRIGHTNESS, surface::sendDisplayBrightness);
            this.configuration.addSettingObserver (PushConfiguration.LED_BRIGHTNESS, surface::sendLEDBrightness);
            this.configuration.addSettingObserver (PushConfiguration.PAD_SENSITIVITY, () -> {
                surface.sendPadVelocityCurve ();
                surface.sendPadThreshold ();
            });
            this.configuration.addSettingObserver (PushConfiguration.PAD_GAIN, () -> {
                surface.sendPadVelocityCurve ();
                surface.sendPadThreshold ();
            });
            this.configuration.addSettingObserver (PushConfiguration.PAD_DYNAMICS, () -> {
                surface.sendPadVelocityCurve ();
                surface.sendPadThreshold ();
            });
        }
        else
        {
            this.configuration.addSettingObserver (PushConfiguration.VELOCITY_CURVE, surface::sendPadSensitivity);
            this.configuration.addSettingObserver (PushConfiguration.PAD_THRESHOLD, surface::sendPadSensitivity);
        }

        surface.getModeManager ().addModeListener ( (oldMode, newMode) -> this.updateMode (newMode));
        surface.getViewManager ().addViewChangeListener ( (previousViewId, activeViewId) -> this.onViewChange ());

        this.configuration.addSettingObserver (PushConfiguration.RIBBON_MODE, this::updateRibbonMode);
        this.configuration.addSettingObserver (PushConfiguration.DEBUG_MODE, () -> {
            final ModeManager modeManager = surface.getModeManager ();
            final Modes debugMode = this.configuration.getDebugMode ();
            if (modeManager.getMode (debugMode) != null)
                modeManager.setActiveMode (debugMode);
            else
                this.host.error ("Mode " + debugMode + " not registered.");
        });

        if (this.isPush2)
            this.configuration.addSettingObserver (PushConfiguration.DEBUG_WINDOW, this.getSurface ().getGraphicsDisplay ()::showDebugWindow);

        this.configuration.addSettingObserver (PushConfiguration.DISPLAY_SCENES_CLIPS, () -> {
            if (Views.isSessionView (this.getSurface ().getViewManager ().getActiveViewId ()))
            {
                final ModeManager modeManager = this.getSurface ().getModeManager ();
                if (modeManager.isActiveMode (Modes.SESSION))
                    modeManager.restoreMode ();
                else
                    modeManager.setActiveMode (Modes.SESSION);
            }
        });

        this.configuration.addSettingObserver (PushConfiguration.SESSION_VIEW, () -> {
            final ViewManager viewManager = this.getSurface ().getViewManager ();
            if (!Views.isSessionView (viewManager.getActiveViewId ()))
                return;
            if (this.configuration.isScenesClipViewSelected ())
                viewManager.setActiveView (Views.SCENE_PLAY);
            else
                viewManager.setActiveView (Views.SESSION);
        });

        this.configuration.addSettingObserver (AbstractConfiguration.KNOB_SPEED_NORMAL, () -> this.valueChanger.setFractionValue (this.configuration.getKnobSpeedNormal ()));
        this.configuration.addSettingObserver (AbstractConfiguration.KNOB_SPEED_SLOW, () -> this.valueChanger.setSlowFractionValue (this.configuration.getKnobSpeedSlow ()));

        this.createScaleObservers (this.configuration);
    }


    /** {@inheritDoc} */
    @Override
    protected void createViews ()
    {
        final PushControlSurface surface = this.getSurface ();
        final ViewManager viewManager = surface.getViewManager ();
        viewManager.registerView (Views.PLAY, new PlayView (surface, this.model));
        viewManager.registerView (Views.PIANO, new PianoView (surface, this.model));
        viewManager.registerView (Views.PRG_CHANGE, new PrgChangeView (surface, this.model));
        viewManager.registerView (Views.CLIP, new ClipView (surface, this.model));
        viewManager.registerView (Views.COLOR, new ColorView (surface, this.model));

        viewManager.registerView (Views.SESSION, new SessionView (surface, this.model));
        viewManager.registerView (Views.SEQUENCER, new SequencerView (surface, this.model));
        viewManager.registerView (Views.POLY_SEQUENCER, new PolySequencerView (surface, this.model, true));
        viewManager.registerView (Views.MIXING, new MixingView(surface, this.model));

        viewManager.registerView (Views.DRUM, new DrumView (surface, this.model));
        viewManager.registerView (Views.DRUM4, new DrumView4 (surface, this.model));
        viewManager.registerView (Views.DRUM8, new DrumView8 (surface, this.model));
        viewManager.registerView (Views.RAINDROPS, new RaindropsView (surface, this.model));
        viewManager.registerView (Views.SCENE_PLAY, new ScenePlayView (surface, this.model));

        viewManager.registerView (Views.DRUM64, new DrumView64 (surface, this.model));
    }


    /** {@inheritDoc} */
    @Override
    protected void registerTriggerCommands ()
    {
        final PushControlSurface surface = this.getSurface ();
        final ViewManager viewManager = surface.getViewManager ();
        this.addTriggerCommand (TriggerCommandID.PLAY, PushControlSurface.PUSH_BUTTON_PLAY, new PlayCommand<> (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.RECORD, PushControlSurface.PUSH_BUTTON_RECORD, new RecordCommand<> (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.NEW, PushControlSurface.PUSH_BUTTON_NEW, new NewCommand<> (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.DUPLICATE, PushControlSurface.PUSH_BUTTON_DUPLICATE, new DuplicateCommand<> (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.AUTOMATION, PushControlSurface.PUSH_BUTTON_AUTOMATION, new AutomationCommand (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.FIXED_LENGTH, PushControlSurface.PUSH_BUTTON_FIXED_LENGTH, new FixedLengthCommand (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.QUANTIZE, PushControlSurface.PUSH_BUTTON_QUANTIZE, new PushQuantizeCommand (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.DELETE, PushControlSurface.PUSH_BUTTON_DELETE, new DeleteCommand<> (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.DOUBLE, PushControlSurface.PUSH_BUTTON_DOUBLE, new DoubleCommand<> (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.UNDO, PushControlSurface.PUSH_BUTTON_UNDO, new UndoCommand<> (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.DEVICE, PushControlSurface.PUSH_BUTTON_DEVICE, new DeviceCommand (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.BROWSE, PushControlSurface.PUSH_BUTTON_BROWSE, new PushBrowserCommand (Modes.BROWSER, this.model, surface));
        this.addTriggerCommand (TriggerCommandID.TRACK, PushControlSurface.PUSH_BUTTON_TRACK, new TrackCommand (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.CLIP, PushControlSurface.PUSH_BUTTON_CLIP, new ClipCommand (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.VOLUME, PushControlSurface.PUSH_BUTTON_VOLUME, new VolumeCommand (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.PAN_SEND, PushControlSurface.PUSH_BUTTON_PAN_SEND, new PanSendCommand (this.model, surface));

        for (int i = 0; i < 8; i++)
        {
            this.addTriggerCommand (TriggerCommandID.get (TriggerCommandID.ROW1_1, i), PushControlSurface.PUSH_BUTTON_ROW1_1 + i, new ButtonRowModeCommand<> (0, i, this.model, surface));
            this.addTriggerCommand (TriggerCommandID.get (TriggerCommandID.ROW2_1, i), PushControlSurface.PUSH_BUTTON_ROW2_1 + i, new ButtonRowModeCommand<> (1, i, this.model, surface));
            this.addTriggerCommand (TriggerCommandID.get (TriggerCommandID.SCENE1, i), PushControlSurface.PUSH_BUTTON_SCENE1 + i, new SceneCommand<> (7 - i, this.model, surface));
        }

        this.addTriggerCommand (TriggerCommandID.SHIFT, PushControlSurface.PUSH_BUTTON_SHIFT, new ShiftCommand (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.LAYOUT, PushControlSurface.PUSH_BUTTON_LAYOUT, new LayoutCommand (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.SELECT, PushControlSurface.PUSH_BUTTON_SELECT, new SelectCommand (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.TAP_TEMPO, PushControlSurface.PUSH_BUTTON_TAP, new TapTempoCommand<> (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.METRONOME, PushControlSurface.PUSH_BUTTON_METRONOME, new MetronomeCommand<> (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.MASTERTRACK, PushControlSurface.PUSH_BUTTON_MASTER, new MastertrackCommand (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.PAGE_LEFT, PushControlSurface.PUSH_BUTTON_DEVICE_LEFT, new PageLeftCommand (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.PAGE_RIGHT, PushControlSurface.PUSH_BUTTON_DEVICE_RIGHT, new PageRightCommand (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.MUTE, PushControlSurface.PUSH_BUTTON_MUTE, new MuteCommand (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.SOLO, PushControlSurface.PUSH_BUTTON_SOLO, new SoloCommand (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.SCALES, PushControlSurface.PUSH_BUTTON_SCALES, new ScalesCommand (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.ACCENT, PushControlSurface.PUSH_BUTTON_ACCENT, new AccentCommand (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.ADD_EFFECT, PushControlSurface.PUSH_BUTTON_ADD_EFFECT, new AddEffectCommand<> (Modes.BROWSER, this.model, surface));
        this.addTriggerCommand (TriggerCommandID.ADD_TRACK, PushControlSurface.PUSH_BUTTON_ADD_TRACK, new AddTrackCommand<> (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.SELECT_PLAY_VIEW, PushControlSurface.PUSH_BUTTON_NOTE, new SelectPlayViewCommand (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.ARROW_DOWN, PushControlSurface.PUSH_BUTTON_DOWN, new CursorCommand<> (Direction.DOWN, this.model, surface));
        this.addTriggerCommand (TriggerCommandID.ARROW_UP, PushControlSurface.PUSH_BUTTON_UP, new CursorCommand<> (Direction.UP, this.model, surface));
        this.addTriggerCommand (TriggerCommandID.ARROW_LEFT, PushControlSurface.PUSH_BUTTON_LEFT, new CursorCommand<> (Direction.LEFT, this.model, surface));
        this.addTriggerCommand (TriggerCommandID.ARROW_RIGHT, PushControlSurface.PUSH_BUTTON_RIGHT, new CursorCommand<> (Direction.RIGHT, this.model, surface));
        this.addTriggerCommand (TriggerCommandID.OCTAVE_DOWN, PushControlSurface.PUSH_BUTTON_OCTAVE_DOWN, new OctaveCommand (false, this.model, surface));
        this.addTriggerCommand (TriggerCommandID.OCTAVE_UP, PushControlSurface.PUSH_BUTTON_OCTAVE_UP, new OctaveCommand (true, this.model, surface));

        viewManager.registerTriggerCommand (TriggerCommandID.SETUP, new SetupCommand (this.isPush2, this.model, surface));
        if (this.isPush2)
        {
            surface.assignTriggerCommand (PushControlSurface.PUSH_BUTTON_SETUP, TriggerCommandID.SETUP);
            this.addTriggerCommand (TriggerCommandID.CONVERT, PushControlSurface.PUSH_BUTTON_CONVERT, new ConvertCommand<> (this.model, surface));
            this.addTriggerCommand (TriggerCommandID.USER, PushControlSurface.PUSH_BUTTON_USER_MODE, this.host.hasUserParameters () ? new ModeSelectCommand<> (this.model, surface, Modes.USER) : NopCommand.INSTANCE);
        }
        else
            surface.assignTriggerCommand (PushControlSurface.PUSH_BUTTON_USER_MODE, TriggerCommandID.SETUP);

        this.addTriggerCommand (TriggerCommandID.STOP_CLIP, PushControlSurface.PUSH_BUTTON_CLIP_STOP, new StopAllClipsCommand<> (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.SELECT_SESSION_VIEW, PushControlSurface.PUSH_BUTTON_SESSION, new SelectSessionViewCommand (this.model, surface));
        this.addTriggerCommand (TriggerCommandID.REPEAT, PushControlSurface.PUSH_BUTTON_REPEAT, new NoteRepeatCommand<> (this.model, surface));
    }


    /** {@inheritDoc} */
    @SuppressWarnings(
    {
        "rawtypes",
        "unchecked"
    })
    @Override
    protected void registerContinuousCommands ()
    {
        final PushControlSurface surface = this.getSurface ();
        for (int i = 0; i < 8; i++)
            this.addContinuousCommand (ContinuousCommandID.get (ContinuousCommandID.KNOB1, i), PushControlSurface.PUSH_KNOB1 + i, new KnobRowModeCommand<> (i, this.model, surface));

        this.addContinuousCommand (ContinuousCommandID.MASTER_KNOB, PushControlSurface.PUSH_KNOB9, new MasterVolumeCommand<> (this.model, surface));
        this.addContinuousCommand (ContinuousCommandID.TEMPO, PushControlSurface.PUSH_SMALL_KNOB1, new RasteredKnobCommand (this.model, surface));
        this.addContinuousCommand (ContinuousCommandID.PLAY_POSITION, PushControlSurface.PUSH_SMALL_KNOB2, new PlayPositionCommand<> (this.model, surface));
        this.addContinuousCommand (ContinuousCommandID.FOOTSWITCH, PushControlSurface.PUSH_FOOTSWITCH2, new FootswitchCommand<> (this.model, surface));

        this.addNoteCommand (TriggerCommandID.KNOB1_TOUCH, PushControlSurface.PUSH_KNOB1_TOUCH, new KnobRowTouchModeCommand<> (0, this.model, surface));
        this.addNoteCommand (TriggerCommandID.KNOB2_TOUCH, PushControlSurface.PUSH_KNOB2_TOUCH, new KnobRowTouchModeCommand<> (1, this.model, surface));
        this.addNoteCommand (TriggerCommandID.KNOB3_TOUCH, PushControlSurface.PUSH_KNOB3_TOUCH, new KnobRowTouchModeCommand<> (2, this.model, surface));
        this.addNoteCommand (TriggerCommandID.KNOB4_TOUCH, PushControlSurface.PUSH_KNOB4_TOUCH, new KnobRowTouchModeCommand<> (3, this.model, surface));
        this.addNoteCommand (TriggerCommandID.KNOB5_TOUCH, PushControlSurface.PUSH_KNOB5_TOUCH, new KnobRowTouchModeCommand<> (4, this.model, surface));
        this.addNoteCommand (TriggerCommandID.KNOB6_TOUCH, PushControlSurface.PUSH_KNOB6_TOUCH, new KnobRowTouchModeCommand<> (5, this.model, surface));
        this.addNoteCommand (TriggerCommandID.KNOB7_TOUCH, PushControlSurface.PUSH_KNOB7_TOUCH, new KnobRowTouchModeCommand<> (6, this.model, surface));
        this.addNoteCommand (TriggerCommandID.KNOB8_TOUCH, PushControlSurface.PUSH_KNOB8_TOUCH, new KnobRowTouchModeCommand<> (7, this.model, surface));
        this.addNoteCommand (TriggerCommandID.TEMPO_TOUCH, PushControlSurface.PUSH_SMALL_KNOB1_TOUCH, new SmallKnobTouchCommand (this.model, surface, true));
        this.addNoteCommand (TriggerCommandID.PLAYCURSOR_TOUCH, PushControlSurface.PUSH_SMALL_KNOB2_TOUCH, new SmallKnobTouchCommand (this.model, surface, false));
        this.addNoteCommand (TriggerCommandID.CONFIGURE_PITCHBEND, PushControlSurface.PUSH_RIBBON_TOUCH, new ConfigurePitchbendCommand (this.model, surface));
        this.addNoteCommand (TriggerCommandID.MASTERTRACK_TOUCH, PushControlSurface.PUSH_KNOB9_TOUCH, new MastertrackTouchCommand (this.model, surface));

        final ViewManager viewManager = surface.getViewManager ();
        viewManager.registerPitchbendCommand (new PitchbendCommand (this.model, surface));

        final Views [] views =
        {
            Views.PLAY,
            Views.PIANO,
            Views.DRUM,
            Views.DRUM64
        };
        for (final Views viewID: views)
        {
            final AbstractView view = (AbstractView) viewManager.getView (viewID);
            view.registerAftertouchCommand (new AftertouchAbstractViewCommand<> (view, this.model, surface));
        }

        viewManager.getView (Views.SESSION).registerPitchbendCommand (new PitchbendSessionCommand (this.model, surface));
    }


    /** {@inheritDoc} */
    @Override
    public void startup ()
    {
        final PushControlSurface surface = this.getSurface ();
        surface.getViewManager ().setActiveView (this.configuration.getDefaultNoteView ());

        surface.sendPressureMode (true);
        surface.getOutput ().sendSysex (DeviceInquiry.createQuery ());

        if (this.isPush2)
            surface.updateColorPalette ();
    }


    /**
     * Called when a new view is selected.
     */
    private void onViewChange ()
    {
        final PushControlSurface surface = this.getSurface ();
        surface.updateButtons ();

        // Update ribbon mode
        if (surface.getViewManager ().isActiveView (Views.SESSION))
            surface.setRibbonMode (PushControlSurface.PUSH_RIBBON_PAN);
        else
            this.updateRibbonMode ();

        this.updateIndication (null);
    }


    /** {@inheritDoc} */
    @Override
    protected void updateButtons ()
    {
        final ITransport t = this.model.getTransport ();
        final PushControlSurface surface = this.getSurface ();
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_METRONOME, t.isMetronomeOn () ? ColorManager.BUTTON_STATE_HI : ColorManager.BUTTON_STATE_ON);
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_PLAY, t.isPlaying () ? PushColors.PUSH_BUTTON_STATE_PLAY_HI : PushColors.PUSH_BUTTON_STATE_PLAY_ON);

        final boolean isShift = surface.isShiftPressed ();
        final boolean isFlipRecord = this.configuration.isFlipRecord ();
        final boolean isRecordShifted = isShift && !isFlipRecord || !isShift && isFlipRecord;
        final boolean isWriting = isRecordShifted ? t.isWritingClipLauncherAutomation () : t.isWritingArrangerAutomation ();
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_AUTOMATION, isWriting ? PushColors.PUSH_BUTTON_STATE_REC_HI : PushColors.PUSH_BUTTON_STATE_REC_ON);
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_RECORD, isRecordShifted ? t.isLauncherOverdub () ? PushColors.PUSH_BUTTON_STATE_OVR_HI : PushColors.PUSH_BUTTON_STATE_OVR_ON : t.isRecording () ? PushColors.PUSH_BUTTON_STATE_REC_HI : PushColors.PUSH_BUTTON_STATE_REC_ON);

        final String repeatState = this.getSurface ().getInput ().getDefaultNoteInput ().getNoteRepeat ().isActive () ? ColorManager.BUTTON_STATE_HI : ColorManager.BUTTON_STATE_ON;
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_REPEAT, repeatState);
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_ACCENT, this.configuration.isAccentActive () ? ColorManager.BUTTON_STATE_HI : ColorManager.BUTTON_STATE_ON);

        final PushConfiguration config = surface.getConfiguration ();
        if (this.isPush2)
        {
            final ModeManager modeManager = surface.getModeManager ();
            if (modeManager.isActiveOrTempMode (Modes.DEVICE_LAYER))
            {
                final ICursorDevice cd = this.model.getCursorDevice ();
                final IChannel layer = cd.getLayerOrDrumPadBank ().getSelectedItem ();
                surface.updateTrigger (PushControlSurface.PUSH_BUTTON_MUTE, layer != null && layer.isMute () ? PushColors.PUSH_BUTTON_STATE_MUTE_HI : PushColors.PUSH_BUTTON_STATE_MUTE_ON);
                surface.updateTrigger (PushControlSurface.PUSH_BUTTON_SOLO, layer != null && layer.isSolo () ? PushColors.PUSH_BUTTON_STATE_SOLO_HI : PushColors.PUSH_BUTTON_STATE_SOLO_ON);
            }
            else
            {
                final ITrack selTrack = modeManager.isActiveOrTempMode (Modes.MASTER) ? this.model.getMasterTrack () : this.model.getSelectedTrack ();
                surface.updateTrigger (PushControlSurface.PUSH_BUTTON_MUTE, selTrack != null && selTrack.isMute () ? PushColors.PUSH_BUTTON_STATE_MUTE_HI : PushColors.PUSH_BUTTON_STATE_MUTE_ON);
                surface.updateTrigger (PushControlSurface.PUSH_BUTTON_SOLO, selTrack != null && selTrack.isSolo () ? PushColors.PUSH_BUTTON_STATE_SOLO_HI : PushColors.PUSH_BUTTON_STATE_SOLO_ON);
            }

            surface.updateTrigger (PushControlSurface.PUSH_BUTTON_CONVERT, this.model.canConvertClip () ? ColorManager.BUTTON_STATE_ON : ColorManager.BUTTON_STATE_OFF);
        }
        else
        {
            final boolean isMuteState = config.isMuteState ();
            surface.updateTrigger (PushControlSurface.PUSH_BUTTON_MUTE, isMuteState ? PushColors.PUSH_BUTTON_STATE_MUTE_HI : PushColors.PUSH_BUTTON_STATE_MUTE_ON);
            surface.updateTrigger (PushControlSurface.PUSH_BUTTON_SOLO, !isMuteState ? PushColors.PUSH_BUTTON_STATE_SOLO_HI : PushColors.PUSH_BUTTON_STATE_SOLO_ON);
        }

        final ViewManager viewManager = surface.getViewManager ();
        final boolean isSessionView = Views.isSessionView (viewManager.getActiveViewId ());
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_NOTE, isSessionView ? ColorManager.BUTTON_STATE_ON : ColorManager.BUTTON_STATE_HI);
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_CLIP_STOP, surface.isPressed (PushControlSurface.PUSH_BUTTON_CLIP_STOP) ? PushColors.PUSH_BUTTON_STATE_STOP_HI : PushColors.PUSH_BUTTON_STATE_STOP_ON);
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_SESSION, isSessionView ? ColorManager.BUTTON_STATE_HI : ColorManager.BUTTON_STATE_ON);
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_ACCENT, config.isAccentActive () ? ColorManager.BUTTON_STATE_HI : ColorManager.BUTTON_STATE_ON);

        final View activeView = viewManager.getActiveView ();
        if (activeView != null)
        {
            ((CursorCommand<?, ?>) activeView.getTriggerCommand (TriggerCommandID.ARROW_DOWN)).updateArrows ();
            ((SceneView) activeView).updateSceneButtons ();
        }

        boolean isDeviceLeftOn = false;
        boolean isDeviceRightOn = false;

        if (viewManager.isActiveView (Views.SESSION))
        {
            final ITrackBank currentTrackBank = this.model.getCurrentTrackBank ();
            isDeviceLeftOn = currentTrackBank.canScrollPageBackwards ();
            isDeviceRightOn = currentTrackBank.canScrollPageForwards ();
        }
        else
        {
            final INoteClip clip = activeView instanceof AbstractSequencerView && !(activeView instanceof ClipView) ? ((AbstractSequencerView<?, ?>) activeView).getClip () : null;
            isDeviceLeftOn = clip != null && clip.doesExist () && clip.canScrollStepsBackwards ();
            isDeviceRightOn = clip != null && clip.doesExist () && clip.canScrollStepsForwards ();
        }

        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_DEVICE_LEFT, isDeviceLeftOn ? ColorManager.BUTTON_STATE_ON : ColorManager.BUTTON_STATE_OFF);
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_DEVICE_RIGHT, isDeviceRightOn ? ColorManager.BUTTON_STATE_ON : ColorManager.BUTTON_STATE_OFF);
    }


    private void updateMode (final Modes mode)
    {
        if (mode == null)
            return;

        this.updateIndication (mode);

        final boolean isMasterOn = Modes.MASTER == mode || Modes.MASTER_TEMP == mode || Modes.FRAME == mode;
        final boolean isVolumeOn = Modes.VOLUME == mode || Modes.CROSSFADER == mode;
        final boolean isPanOn = mode.ordinal () >= Modes.PAN.ordinal () && mode.ordinal () <= Modes.SEND8.ordinal ();
        final boolean isDeviceOn = Modes.isDeviceMode (mode);
        boolean isMixOn = Modes.TRACK == mode;
        if (this.isPush2)
            isMixOn = isMixOn || isVolumeOn || isPanOn;

        final PushControlSurface surface = this.getSurface ();
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_MASTER, isMasterOn ? ColorManager.BUTTON_STATE_HI : ColorManager.BUTTON_STATE_ON);
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_TRACK, isMixOn ? ColorManager.BUTTON_STATE_HI : ColorManager.BUTTON_STATE_ON);
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_VOLUME, isVolumeOn ? ColorManager.BUTTON_STATE_HI : ColorManager.BUTTON_STATE_ON);
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_PAN_SEND, isPanOn ? ColorManager.BUTTON_STATE_HI : ColorManager.BUTTON_STATE_ON);
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_DEVICE, isDeviceOn ? ColorManager.BUTTON_STATE_HI : ColorManager.BUTTON_STATE_ON);
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_SCALES, Modes.SCALES == mode ? ColorManager.BUTTON_STATE_HI : ColorManager.BUTTON_STATE_ON);
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_FIXED_LENGTH, Modes.FIXED == mode ? ColorManager.BUTTON_STATE_HI : ColorManager.BUTTON_STATE_ON);
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_BROWSE, Modes.BROWSER == mode ? ColorManager.BUTTON_STATE_HI : ColorManager.BUTTON_STATE_ON);
        surface.updateTrigger (PushControlSurface.PUSH_BUTTON_CLIP, Modes.CLIP == mode ? ColorManager.BUTTON_STATE_HI : ColorManager.BUTTON_STATE_ON);

        if (this.host.hasUserParameters ())
            surface.updateTrigger (PushControlSurface.PUSH_BUTTON_USER_MODE, Modes.USER == mode ? ColorManager.BUTTON_STATE_HI : ColorManager.BUTTON_STATE_ON);
        else
            surface.updateTrigger (PushControlSurface.PUSH_BUTTON_USER_MODE, this.isPush2 ? ColorManager.BUTTON_STATE_OFF : ColorManager.BUTTON_STATE_ON);

        if (this.isPush2)
            surface.updateTrigger (PushControlSurface.PUSH_BUTTON_SETUP, Modes.SETUP == mode ? ColorManager.BUTTON_STATE_HI : ColorManager.BUTTON_STATE_ON);
    }


    /** {@inheritDoc} */
    @Override
    protected void updateIndication (final Modes mode)
    {
        if (this.currentMode != null && this.currentMode == mode)
            return;

        if (mode != null)
            this.currentMode = mode;

        final ITrackBank tb = this.model.getTrackBank ();
        final ITrackBank tbe = this.model.getEffectTrackBank ();
        final PushControlSurface surface = this.getSurface ();
        final boolean isSession = surface.getViewManager ().isActiveView (Views.SESSION);
        final boolean isEffect = this.model.isEffectTrackBankActive ();
        final boolean isTrackMode = Modes.TRACK == this.currentMode;
        final boolean isVolume = Modes.VOLUME == this.currentMode;
        final boolean isPan = Modes.PAN == this.currentMode;
        final boolean isDevice = Modes.isDeviceMode (this.currentMode) || Modes.isLayerMode (this.currentMode);
        final boolean isUserMode = Modes.USER == this.currentMode;

        tb.setIndication (!isEffect && isSession);
        if (tbe != null)
            tbe.setIndication (isEffect && isSession);

        final ICursorDevice cursorDevice = this.model.getCursorDevice ();
        final ITrack selectedTrack = tb.getSelectedItem ();
        final IParameterBank parameterBank = cursorDevice.getParameterBank ();
        for (int i = 0; i < tb.getPageSize (); i++)
        {
            final boolean hasTrackSel = selectedTrack != null && selectedTrack.getIndex () == i && isTrackMode;
            final ITrack track = tb.getItem (i);
            track.setVolumeIndication (!isEffect && (isVolume || hasTrackSel));
            track.setPanIndication (!isEffect && (isPan || hasTrackSel));

            final ISendBank sendBank = track.getSendBank ();
            for (int j = 0; j < sendBank.getPageSize (); j++)
                sendBank.getItem (j).setIndication (!isEffect && (this.currentMode.ordinal () - Modes.SEND1.ordinal () == j || hasTrackSel));

            if (tbe != null)
            {
                final ITrack fxTrack = tbe.getItem (i);
                fxTrack.setVolumeIndication (isEffect);
                fxTrack.setPanIndication (isEffect && isPan);
            }

            parameterBank.getItem (i).setIndication (isDevice);
        }

        if (this.host.hasUserParameters ())
        {
            final IParameterBank userParameterBank = this.model.getUserParameterBank ();
            for (int i = 0; i < userParameterBank.getPageSize (); i++)
                userParameterBank.getItem (i).setIndication (isUserMode);
        }
    }


    /**
     * Handle a track selection change.
     *
     * @param isSelected Has the track been selected?
     */
    private void handleTrackChange (final boolean isSelected)
    {
        if (!isSelected)
            return;

        final PushControlSurface surface = this.getSurface ();
        final ViewManager viewManager = surface.getViewManager ();
        final ModeManager modeManager = surface.getModeManager ();

        // Recall last used view (if we are not in session mode)
        if (!viewManager.isActiveView (Views.SESSION))
        {
            final ITrack selectedTrack = this.model.getSelectedTrack ();
            if (selectedTrack != null)
            {
                final Views preferredView = viewManager.getPreferredView (selectedTrack.getPosition ());
                viewManager.setActiveView (preferredView == null ? this.configuration.getDefaultNoteView () : preferredView);
            }
        }

        if (modeManager.isActiveOrTempMode (Modes.MASTER))
            modeManager.setActiveMode (Modes.TRACK);

        if (viewManager.isActiveView (Views.PLAY))
            viewManager.getActiveView ().updateNoteMapping ();

        // Reset drum octave because the drum pad bank is also reset
        this.scales.resetDrumOctave ();
        if (viewManager.isActiveView (Views.DRUM))
            viewManager.getView (Views.DRUM).updateNoteMapping ();
    }


    private void updateRibbonMode ()
    {
        final PushControlSurface surface = this.getSurface ();
        surface.setRibbonValue (0);

        switch (this.configuration.getRibbonMode ())
        {
            case PushConfiguration.RIBBON_MODE_CC:
            case PushConfiguration.RIBBON_MODE_FADER:
                surface.setRibbonMode (PushControlSurface.PUSH_RIBBON_VOLUME);
                break;

            default:
                surface.setRibbonMode (PushControlSurface.PUSH_RIBBON_PITCHBEND);
                break;
        }
    }
}
