package org.geogebra.web.full.euclidian;

import org.geogebra.common.awt.GRectangle;
import org.geogebra.common.kernel.geos.GeoInputBox;
import org.geogebra.web.full.gui.components.MathFieldEditor;
import org.geogebra.web.full.gui.components.MathFieldEditorDecorator;

public class SymbolicEditorDecorator extends MathFieldEditorDecorator {
	private int baseline;
	private double fontSize;

	/**
	 *
	 * @param editor to decorate.
	 * @param fontSize to use.
	 */
	SymbolicEditorDecorator(MathFieldEditor editor, int fontSize) {
		super(editor);
		this.fontSize = fontSize;
		editor.addStyleName("evInputEditor");
		editor.setFontSize(fontSize);
	}

	public void update() {
		setTop(baseline - getHeight() / 2);
	}

	/**
	 * Updates editor bounds, colors amd font size
	 * according to the geoInputBox
	 *
	 * @param bounds to set.
	 * @param geoInputBox the currently edited geo.
	 */
	void update(GRectangle bounds, GeoInputBox geoInputBox) {
		updateBounds(bounds);
		setForegroundColor(geoInputBox.getObjectColor());
		setBackgroundColor(geoInputBox.getBackgroundColor());
		setFontSize(fontSize * geoInputBox.getFontSizeMultiplier());
	}

	private void updateBounds(GRectangle bounds) {
		double fieldWidth = bounds.getWidth() - PADDING_LEFT;
		setLeft(bounds.getX());
		setTop(bounds.getY());
		setWidth(fieldWidth);
		setHeight(bounds.getHeight());

		baseline = (int) (bounds.getY() + bounds.getHeight() / 2);
	}
}
