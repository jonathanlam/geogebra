package org.geogebra.web.full.gui.menu;

import com.google.gwt.user.client.ui.Widget;
import org.geogebra.common.gui.menu.Action;
import org.geogebra.common.gui.menu.ActionableItem;
import org.geogebra.common.gui.menu.MenuItem;
import org.geogebra.common.gui.menu.SubmenuItem;
import org.geogebra.web.full.gui.HeaderView;
import org.geogebra.web.full.gui.menu.action.MenuActionHandler;
import org.geogebra.web.html5.gui.FastClickHandler;

import java.util.Arrays;
import java.util.Collections;

class MenuActionRouter {

	private MenuActionHandler menuActionHandler;
	private MenuViewController menuViewController;

	public MenuActionRouter(MenuActionHandler menuActionHandler, MenuViewController menuViewController) {
		this.menuActionHandler = menuActionHandler;
		this.menuViewController = menuViewController;
	}

	void handleMenuItem(MenuItem menuItem) {
		if (menuItem instanceof ActionableItem) {
			handleAction(((ActionableItem) menuItem).getAction());
		} else if (menuItem instanceof SubmenuItem) {
			handleSubmenu((SubmenuItem) menuItem);
		}
	}

	void handleAction(Action action) {
		switch (action) {
			case START_CLASSIC:
				menuActionHandler.startClassic();
				break;
			case START_GRAPHING:
				menuActionHandler.startGraphing();
				break;
			case START_SCIENTIFIC:
				menuActionHandler.startScientific();
				break;
			case START_GEOMETRY:
				menuActionHandler.startGeometry();
				break;
			case START_CAS_CALCULATOR:
				menuActionHandler.startCasCalculator();
				break;
			case START_GRAPHING_3D:
				menuActionHandler.startGraphing3d();
				break;
			case CLEAR_CONSTRUCTION:
				menuActionHandler.clearConstruction();
				break;
			case START_EXAM_MODE:
				menuActionHandler.startExamMode();
				break;
			case SHOW_SETTINGS:
				menuActionHandler.showSettings();
				break;
			case SHOW_SEARCH_VIEW:
				menuActionHandler.showSearchView();
				break;
			case SAVE_FILE:
				menuActionHandler.saveFile();
				break;
			case SHARE_FILE:
				menuActionHandler.shareFile();
				break;
			case EXPORT_IMAGE:
				menuActionHandler.exportImage();
				break;
			case SHOW_EXAM_LOG:
				menuActionHandler.showExamLog();
				break;
			case EXIT_EXAM_MODE:
				menuActionHandler.exitExamMode();
				break;
			case SHOW_TUTORIALS:
				menuActionHandler.showTutorials();
				break;
			case SHOW_FORUM:
				menuActionHandler.showForum();
				break;
			case REPORT_PROBLEM:
				menuActionHandler.reportProblem();
				break;
			case SHOW_LICENSE:
				menuActionHandler.showLicense();
				break;
			case SIGN_IN:
				menuActionHandler.signIn();
				break;
			case SIGN_OUT:
				menuActionHandler.signOut();
				break;
			case OPEN_PROFILE_PAGE:
				menuActionHandler.openProfilePage();
				break;
			case SHOW_DOWNLOAD_AS:
				menuActionHandler.showDownloadAs();
				break;
			case DOWNLOAD_GGB:
				menuActionHandler.downloadGgb();
				break;
			case DOWNLOAD_GGS:
				menuActionHandler.downloadGgs();
				break;
			case DOWNLOAD_PNG:
				menuActionHandler.downloadPng();
				break;
			case DOWNLOAD_SVG:
				menuActionHandler.downloadSvg();
				break;
			case DOWNLOAD_PDF:
				menuActionHandler.downloadPdf();
				break;
			case DOWNLOAD_STL:
				menuActionHandler.downloadStl();
				break;
			case DOWNLOAD_COLLADA_DAE:
				menuActionHandler.downloadColladaDae();
				break;
			case DOWNLOAD_COLLADA_HTML:
				menuActionHandler.downloadColladaHTML();
				break;
			case PREVIEW_PRINT:
				menuActionHandler.previewPrint();
		}
		menuViewController.setMenuVisible(false);
	}

	private void handleSubmenu(SubmenuItem submenuItem) {
		final MenuView menuView = new MenuView();
		menuViewController.setMenuItemGroups(menuView,
				Collections.singletonList(submenuItem.getGroup()));
		HeaderView headerView = new HeaderView();
		headerView.setElevated(false);
		headerView.setCompact(true);
		headerView.setCaption(submenuItem.getLabel());
		headerView.getBackButton().addFastClickHandler(new FastClickHandler() {
			@Override
			public void onClick(Widget source) {
				menuViewController.hideSubmenu();
			}
		});
		HeaderedMenuView submenu = new HeaderedMenuView(menuView);
		submenu.setHeaderView(headerView);
		menuViewController.showSubmenu(submenu);
	}
}
