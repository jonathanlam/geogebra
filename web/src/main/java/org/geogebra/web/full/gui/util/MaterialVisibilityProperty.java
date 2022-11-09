package org.geogebra.web.full.gui.util;

import org.geogebra.common.main.Localization;
import org.geogebra.common.main.MaterialVisibility;
import org.geogebra.common.properties.impl.AbstractEnumerableProperty;

public class MaterialVisibilityProperty extends AbstractEnumerableProperty {
	private MaterialVisibility visibility = MaterialVisibility.Private;

	/**
	 * Constructs an AbstractEnumerableProperty
	 * @param localization the localization used
	 */
	public MaterialVisibilityProperty(Localization localization) {
		super(localization, "");
		update(MaterialVisibility.Private);
	}

	@Override
	public int getIndex() {
		return visibility.getIndex();
	}

	@Override
	protected void setValueSafe(String value, int index) {
	}

	public void update(MaterialVisibility visibility) {
		this.visibility = visibility;
		if (visibility == MaterialVisibility.Public) {
			setValuesAndLocalize("Private", "Shared", "Public");
		} else {
			setValuesAndLocalize("Private", "Shared");
		}
	}
}
