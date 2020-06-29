package org.geogebra.web.full.gui.openfileview;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class CardInfoPanel extends FlowPanel {
	private Widget title;
	private Widget subtitle;

	public CardInfoPanel(Widget title, Widget subtitle) {
		super();
		this.title = title;
		this.subtitle = subtitle;
		styleComponents();
		build();
	}

	private void build() {
		add(title);
		add(subtitle);
	}

	private void styleComponents() {
		setStyleName("cardInfoPanel");
		title.setStyleName("cardTitle");
		subtitle.setStyleName("cardAuthor");
	}
}
