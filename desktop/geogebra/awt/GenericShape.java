package geogebra.awt;

import java.awt.Shape;
import java.awt.geom.GeneralPath;

import geogebra.common.awt.AffineTransform;
import geogebra.common.awt.PathIterator;
import geogebra.common.awt.Rectangle;
import geogebra.common.awt.Rectangle2D;
import geogebra.common.factories.AwtFactory;

public class GenericShape implements geogebra.awt.Shape{
	private java.awt.Shape impl; 
	
	private GenericShape(){}
	public boolean intersects(int i, int j, int k, int l) {
		return impl.intersects(i, j, k, l);
	}
	public static java.awt.Shape getAwtShape(geogebra.common.awt.Shape s){
		if(!(s instanceof geogebra.awt.Shape))
			return null;
		return ((geogebra.awt.Shape)s).getAwtShape();
	}
	
	public GenericShape(java.awt.Shape s){
		this();
		impl = s;
	}
	
	public boolean contains(int x, int y) {
		return impl.contains(x, y);
	}
	
	public geogebra.awt.Rectangle getBounds() {
		return new geogebra.awt.Rectangle(impl.getBounds());
	}
	public Rectangle2D getBounds2D() {
		return new geogebra.awt.Rectangle2D(impl.getBounds2D());
	}
	public boolean contains(Rectangle rectangle) {
		return impl.contains(geogebra.awt.Rectangle.getAWTRectangle(rectangle));
	}
	public boolean contains(double xTry, double yTry) {
		return impl.contains(xTry, yTry);
	}
	public void setImpl(java.awt.Shape newImpl) {
		impl = newImpl;
	}
	public Shape getAwtShape() {
		return impl;
	}
	public PathIterator getPathIterator(AffineTransform affineTransform) {
		return (PathIterator) impl.getPathIterator(geogebra.awt.AffineTransform.getAwtAffineTransform(affineTransform));
	}
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return (PathIterator) impl.getPathIterator(geogebra.awt.AffineTransform.getAwtAffineTransform(at), flatness);
	}
	public boolean intersects(double x, double y, double w, double h) {
		return impl.intersects(x, y, w, h);
	}
	public boolean intersects(Rectangle2D r) {
		return impl.intersects(geogebra.awt.Rectangle2D.getAWTRectangle(r));
	}
}
