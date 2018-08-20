package org.geogebra.web.shared;

import org.geogebra.common.euclidian.event.PointerEventType;
import org.geogebra.common.move.events.BaseEvent;
import org.geogebra.common.move.ggtapi.events.LogOutEvent;
import org.geogebra.common.move.ggtapi.events.LoginEvent;
import org.geogebra.common.move.views.EventRenderable;
import org.geogebra.web.html5.Browser;
import org.geogebra.web.html5.gui.util.ClickStartHandler;
import org.geogebra.web.html5.gui.util.StandardButton;
import org.geogebra.web.html5.main.AppW;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.animation.client.AnimationScheduler.AnimationCallback;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Singleton representing external header bar of unbundled apps.
 */
public class GlobalHeader implements EventRenderable {
	/**
	 * Singleton instance.
	 */
	public static final GlobalHeader INSTANCE = new GlobalHeader();

	private ProfilePanel profilePanel;
	private RootPanel signIn;
	private AppW app;
	private Label timer;
	private StandardButton examInfoBtn;

	private String oldHref;

	/**
	 * Activate sign in button in external header
	 * 
	 * @param appW
	 *            application
	 */
	public void addSignIn(final AppW appW) {
		this.app = appW;
		signIn = RootPanel.get("signInButton");
		if (signIn == null) {
			return;
		}
		ClickStartHandler.init(signIn, new ClickStartHandler(true, true) {

			@Override
			public void onClickStart(int x, int y, PointerEventType type) {
				new SignInButton(appW, 0, null).login();
			}
		});
		app.getLoginOperation().getView().add(this);
	}

	@Override
	public void renderEvent(BaseEvent event) {
		if (event instanceof LoginEvent
				&& ((LoginEvent) event).isSuccessful()) {
			if (profilePanel == null) {
				profilePanel = new ProfilePanel(app);
			}
			signIn.setVisible(false);
			profilePanel.setVisible(true);
			profilePanel.update(((LoginEvent) event).getUser());
			DivElement profile = DOM.createDiv().cast();
			profile.setId("profileId");
			signIn.getElement().getParentElement().appendChild(profile);

			RootPanel.get("profileId").add(profilePanel);
		}
		if (event instanceof LogOutEvent) {
			profilePanel.setVisible(false);
			signIn.setVisible(true);
		}
	}

	/**
	 * @return share button
	 */
	public static RootPanel getShareButton() {
		return RootPanel.get("shareButton");
	}

	/**
	 * @return button
	 */
	public RootPanel getButtonPanel() {
		return RootPanel.get("buttonsID");
	}

	/**
	 * @return panel of exam timer
	 */
	public RootPanel getExamPanel() {
		return RootPanel.get("examId");
	}

	/**
	 * @return application
	 */
	public AppW getApp() {
		return app;
	}

	/**
	 * @return exam timer
	 */
	public Label getTimer() {
		return timer;
	}

	/**
	 * @return exam info button
	 */
	public StandardButton getExamInfoBtn() {
		return examInfoBtn;
	}

	/**
	 * remove exam timer and put back button panel
	 */
	public void resetAfterExam() {
		getExamPanel().getElement().removeFromParent();
		getButtonPanel().getElement().getStyle()
				.setDisplay(Display.FLEX);
		getHomeLink().setHref(oldHref);
	}

	/**
	 * switch right buttons with exam timer and info button
	 */
	public void addExamTimer() {
		// remove other buttons
		getButtonPanel().getElement().getStyle()
				.setDisplay(Display.NONE);
		// exam panel with timer and info btn
		timer = new Label("0:00");
		timer.setStyleName("examTimer");
		examInfoBtn = new StandardButton(
				SharedResources.INSTANCE.info_black(), null, 24, app);
		examInfoBtn.addStyleName("flatButtonHeader");
		examInfoBtn.addStyleName("examInfoBtn");
		// add exam panel to
		DivElement exam = DOM.createDiv().cast();
		exam.setId("examId");
		getButtonPanel().getElement().getParentElement().appendChild(exam);
		oldHref = getHomeLink().getHref();
		getHomeLink().setHref("#");
		RootPanel.get("examId").addStyleName("examPanel");
		RootPanel.get("examId").add(timer);
		RootPanel.get("examId").add(examInfoBtn);
		// run timer
		AnimationScheduler.get().requestAnimationFrame(new AnimationCallback() {
			@Override
			public void execute(double timestamp) {
				if (getApp().getExam() != null) {
					String os = Browser.getMobileOperatingSystem();
					getApp().getExam().checkCheating(os);
					if (getApp().getExam().isCheating()) {
						getApp().getGuiManager()
								.setUnbundledHeaderStyle("examCheat");
					}
					getTimer().setText(
							getApp().getExam().getElapsedTimeLocalized());
					AnimationScheduler.get().requestAnimationFrame(this);
				}
			}
		});
	}

	private static AnchorElement getHomeLink() {
		return RootPanel.get("headerID").getElement().getElementsByTagName("a")
				.getItem(0).cast();
	}

}
