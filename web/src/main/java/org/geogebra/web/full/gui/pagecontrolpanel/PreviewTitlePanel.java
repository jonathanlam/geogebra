package org.geogebra.web.full.gui.pagecontrolpanel;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class PreviewTitlePanel extends FlowPanel {
		private final Label mainTitle;
		private final Label subTitle;

	public PreviewTitlePanel() {
		FlowPanel titles = new FlowPanel();
		mainTitle = new Label();
		mainTitle.setStyleName("mainTitle");
		subTitle = new Label();
		subTitle.setStyleName("subTitle");
		titles.add(mainTitle);
		titles.add(subTitle);
		add(titles);
		addStyleName("mowTitlePanel");
	}

	public void setMainTitle(String title) {
		mainTitle.setText(title);
	}

	public void setSubtitle(String title) {
		subTitle.setText(title);
	}

	public String getSubtitle() {
		return subTitle.getText();
	}
}
