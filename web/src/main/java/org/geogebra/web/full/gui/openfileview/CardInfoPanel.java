package org.geogebra.web.full.gui.openfileview;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class CardInfoPanel extends FlowPanel {
	private Label title;
	private Widget row;

	public CardInfoPanel(Label title, Widget row) {
		super();
		this.title = title;
		this.row = row;
		add(title);
		add(row);
		setStyleName("cardInfoPanel");
		title.setStyleName("cardTitle");
	}
}
