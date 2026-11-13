package it.unibo.mvc.view;

import it.unibo.mvc.api.DrawNumberController;
import it.unibo.mvc.api.DrawNumberView;
import it.unibo.mvc.api.DrawResult;

/**
 * DrawNumberStandardOutputView is a simle view without an a UI.
 */
public final class DrawNumberStandardOutputView implements DrawNumberView {

    /**
     * Builds new view without an a UI.
     */
    public DrawNumberStandardOutputView() {
        // There is only std output, no UI
    }

    @Override
    public void setController(final DrawNumberController observer) {
        // No observer required for std only
    }

    @Override
    public void start() {
        // No UI to display
    }

    @Override
    public void result(final DrawResult res) {
        IO.println(res.getDescription());
    }
}
