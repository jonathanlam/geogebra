package org.geogebra.web.full.gui.pagecontrolpanel;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class PreviewTitlePanel extends FlowPanel {
		private final Label mainTitle;
		private final Label subTitle;

	public PreviewTitlePanel() {
		FlowPanel titles = new FlowPanel();
		mainTitle = new Label();
		subTitle = new Label();
		titles.add(mainTitle);
		titles.add(subTitle);
		add(titles);
		addStyleName("mowTitlePanel");
	}

	public void setMainTitle(String title) {
		mainTitle.setText(title);
	}

	public void setSubTitle(String title) {
		subTitle.setText(title);
	}
}
