package geogebra.gui.dialog.options;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class OptionsUtil {

	private static int defaultHgap = 2;
	private static int defaultVgap = 3;

	public static JPanel flowPanel(Component... comps) {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, defaultHgap,
				defaultVgap));
		for (Component comp : comps) {
			p.add(comp);
		}
		return p;
	}

	public static JPanel flowPanel(int tab, Component... comps) {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, defaultHgap,
				defaultVgap));
		p.add(Box.createHorizontalStrut(tab));
		for (Component comp : comps) {
			p.add(comp);
		}
		return p;
	}

	public static JPanel flowPanel(int hgap, int vgap, int tab,
			Component... comps) {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, hgap, vgap));
		p.add(Box.createHorizontalStrut(tab));
		for (Component comp : comps) {
			p.add(comp);
		}
		return p;
	}

	
	public static JPanel flowPanelCenter(int hgap, int vgap, int tab,
			Component... comps) {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, hgap, vgap));
		p.add(Box.createHorizontalStrut(tab));
		for (Component comp : comps) {
			p.add(comp);
		}
		return p;
	}
	
	public static JPanel flowPanelCenter(int hgap, int vgap, int tab, Color bgColor,
			Component... comps) {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, hgap, vgap));
		p.add(Box.createHorizontalStrut(tab));
		for (Component comp : comps) {
			p.add(comp);
		}
		p.setBackground(bgColor);
		return p;
	}
	
	
	
	public static JPanel flowPanelRight(int hgap, int vgap, int tab,
			Component... comps) {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, hgap, vgap));
		p.add(Box.createHorizontalStrut(tab));
		for (Component comp : comps) {
			p.add(comp);
		}
		return p;
	}
	
	
	public static Border titleBorder(String title) {
		Border lineBorder = BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, SystemColor.controlLtHighlight),
				BorderFactory.createMatteBorder(0, 0, 1, 0, SystemColor.controlShadow));
		TitledBorder dummyBorder = BorderFactory.createTitledBorder("");
		//Font f = dummyBorder.getTitleFont();
		//f = f== null ? null : f.deriveFont(Font.ITALIC);
		Border outsideBorder = BorderFactory.createTitledBorder(lineBorder,title,
				TitledBorder.LEADING, TitledBorder.TOP); //,f,SystemColor.DARK_GRAY);
		Border insideBorder = BorderFactory.createEmptyBorder(0, 40, 0, 0);
		return BorderFactory.createCompoundBorder(outsideBorder, insideBorder);
	}

	public static class TitlePanel extends JPanel {

		public TitlePanel() {
			this("");
		}
		
		public TitlePanel(String title) {
			setLayout((new BoxLayout(this, BoxLayout.Y_AXIS)));
			setBorder(titleBorder(title));
		}

		public void setTitle(String title) {
			setBorder(titleBorder(title));
		}
	}

}
