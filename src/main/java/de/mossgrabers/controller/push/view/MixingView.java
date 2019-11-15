package de.mossgrabers.controller.push.view;

import de.mossgrabers.controller.push.PushConfiguration;
import de.mossgrabers.controller.push.controller.PushControlSurface;
import de.mossgrabers.framework.controller.grid.PadGrid;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.view.AbstractView;

import static de.mossgrabers.framework.daw.DAWColors.*;

enum MixingMode {
    TOTAL,
    TRACK,
    SEND
}

/**
 * The Mixing view. Only for Push 1, because I don't have second edition :)
 *
 * @author Vladimir Utoplov
 */
public class MixingView extends AbstractView<PushControlSurface, PushConfiguration> {

    private MixingMode mode = MixingMode.TOTAL;

    /**
     * Constructor.
     *
     * @param surface The surface
     * @param model   The model
     */
    public MixingView(PushControlSurface surface, IModel model) {
        super("Mixing", surface, model);
    }

    @Override
    public void drawGrid() {
        final String[] colorsPerStage = new String[]{
                DAW_COLOR_RED,
                DAW_COLOR_PINK,
                DAW_COLOR_GREEN,
                DAW_COLOR_LIGHT_GREEN,
                DAW_COLOR_LIGHT_GREEN,
                DAW_COLOR_GREEN,
                DAW_COLOR_PINK,
                DAW_COLOR_RED
        };
        int stage = 0;
        final PadGrid padGrid = this.surface.getPadGrid();
        for (int i = 1; i < 65; i++) {
            padGrid.light(i, colorsPerStage[stage]);
            if (i % 8 == 0) {
                stage++;
            }
        }
    }

    @Override
    public void onGridNote(int note, int velocity) {
        if (this.surface.isPressed(PushControlSurface.PUSH_BUTTON_L))
        if (this.surface.isSelectPressed()) {
            // TODO : Select Track, presented by this column. Set mode
            this.mode = MixingMode.TRACK;
            return;
        }
        if (this.surface.isShiftPressed()) {
            // TODO : Display
            this.mode = MixingMode.SEND;
        }
    }
}
