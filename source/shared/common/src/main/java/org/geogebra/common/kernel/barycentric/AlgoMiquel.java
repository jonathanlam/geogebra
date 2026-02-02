/*
 * GeoGebra - Dynamic Mathematics for Everyone
 * Copyright (c) GeoGebra GmbH, Altenbergerstr. 69, 4040 Linz, Austria
 * https://www.geogebra.org
 *
 * This file is licensed by GeoGebra GmbH under the EUPL 1.2 licence and
 * may be used under the EUPL 1.2 in compatible projects (see Article 5
 * and the Appendix of EUPL 1.2 for details).
 * You may obtain a copy of the licence at:
 * https://interoperable-europe.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Note: The overall GeoGebra software package is free to use for
 * non-commercial purposes only.
 * See https://www.geogebra.org/license for full licensing details
 */

package org.geogebra.common.kernel.barycentric;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.algos.AlgoElement;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoLine;
import org.geogebra.common.kernel.geos.GeoPoint;
import org.geogebra.common.kernel.geos.GeoVec3D;
import org.geogebra.common.kernel.matrix.Coords;
import org.geogebra.common.util.DoubleUtil;

/**
 * Computes the Miquel point of a complete quadrilateral defined by 4 lines.
 * The Miquel point is the unique point that lies on all 4 circumcircles
 * of the triangles formed by taking 3 lines at a time.
 */
public class AlgoMiquel extends AlgoElement {

	private GeoLine lineA, lineB, lineC, lineD;
	private GeoPoint miquelPoint;

	/**
	 * Creates new algorithm for Miquel point
	 * @param cons construction
	 * @param label label
	 * @param a first line
	 * @param b second line
	 * @param c third line
	 * @param d fourth line
	 */
	public AlgoMiquel(Construction cons, String label, GeoLine a,
			GeoLine b, GeoLine c, GeoLine d) {
		super(cons);
		this.lineA = a;
		this.lineB = b;
		this.lineC = c;
		this.lineD = d;
		miquelPoint = new GeoPoint(cons);
		setInputOutput();
		compute();
		miquelPoint.setLabel(label);
	}

	@Override
	public Commands getClassName() {
		return Commands.Miquel;
	}

	@Override
	protected void setInputOutput() {
		input = new GeoElement[4];
		input[0] = lineA;
		input[1] = lineB;
		input[2] = lineC;
		input[3] = lineD;

		setOnlyOutput(miquelPoint);
		setDependencies();
	}

	/**
	 * @return the Miquel point
	 */
	public GeoPoint getResult() {
		return miquelPoint;
	}

	@Override
	public final void compute() {
		// Compute 6 intersection points from pairs of lines
		// Using homogeneous coordinates: intersection = cross product
		Coords pAB = GeoVec3D.cross(lineA, lineB);
		Coords pAC = GeoVec3D.cross(lineA, lineC);
		Coords pAD = GeoVec3D.cross(lineA, lineD);
		Coords pBC = GeoVec3D.cross(lineB, lineC);
		Coords pBD = GeoVec3D.cross(lineB, lineD);
		Coords pCD = GeoVec3D.cross(lineC, lineD);

		// Check for points at infinity (parallel lines)
		if (isAtInfinity(pAB) || isAtInfinity(pAC) || isAtInfinity(pAD)
				|| isAtInfinity(pBC) || isAtInfinity(pBD) || isAtInfinity(pCD)) {
			miquelPoint.setUndefined();
			return;
		}

		// Convert to inhomogeneous coordinates
		double[] ab = toInhomogeneous(pAB);
		double[] ac = toInhomogeneous(pAC);
		double[] ad = toInhomogeneous(pAD);
		double[] bc = toInhomogeneous(pBC);
		double[] bd = toInhomogeneous(pBD);
		double[] cd = toInhomogeneous(pCD);

		// Triangle T1 (omit d): pAB, pAC, pBC
		// Triangle T2 (omit c): pAB, pAD, pBD
		// Both triangles share vertex pAB
		// The Miquel point is the second intersection of their circumcircles

		// Check for degenerate triangles (collinear points)
		if (areCollinear(ab, ac, bc) || areCollinear(ab, ad, bd)) {
			miquelPoint.setUndefined();
			return;
		}

		// Compute circumcenters and radii
		double[] c1 = circumcenter(ab, ac, bc);
		double[] c2 = circumcenter(ab, ad, bd);

		if (c1 == null || c2 == null) {
			miquelPoint.setUndefined();
			return;
		}

		double r1 = distance(c1, ab);
		double r2 = distance(c2, ab);

		// Find circle-circle intersection
		// Both circles pass through pAB, so we need the other intersection point
		double[] miquel = circleCircleIntersection(c1, r1, c2, r2, ab);

		if (miquel == null) {
			miquelPoint.setUndefined();
			return;
		}

		miquelPoint.setCoords(miquel[0], miquel[1], 1.0);
	}

	private boolean isAtInfinity(Coords p) {
		return DoubleUtil.isZero(p.getZ());
	}

	private double[] toInhomogeneous(Coords p) {
		double w = p.getZ();
		return new double[] { p.getX() / w, p.getY() / w };
	}

	private boolean areCollinear(double[] p1, double[] p2, double[] p3) {
		// Using cross product to check collinearity
		double area = (p2[0] - p1[0]) * (p3[1] - p1[1])
				- (p3[0] - p1[0]) * (p2[1] - p1[1]);
		return DoubleUtil.isZero(area);
	}

	private double[] circumcenter(double[] p1, double[] p2, double[] p3) {
		// Circumcenter using the formula:
		// The circumcenter is equidistant from all three vertices
		double ax = p1[0], ay = p1[1];
		double bx = p2[0], by = p2[1];
		double cx = p3[0], cy = p3[1];

		double d = 2 * (ax * (by - cy) + bx * (cy - ay) + cx * (ay - by));
		if (DoubleUtil.isZero(d)) {
			return null;
		}

		double ax2ay2 = ax * ax + ay * ay;
		double bx2by2 = bx * bx + by * by;
		double cx2cy2 = cx * cx + cy * cy;

		double ux = (ax2ay2 * (by - cy) + bx2by2 * (cy - ay) + cx2cy2 * (ay - by)) / d;
		double uy = (ax2ay2 * (cx - bx) + bx2by2 * (ax - cx) + cx2cy2 * (bx - ax)) / d;

		return new double[] { ux, uy };
	}

	private double distance(double[] p1, double[] p2) {
		double dx = p1[0] - p2[0];
		double dy = p1[1] - p2[1];
		return Math.sqrt(dx * dx + dy * dy);
	}

	private double[] circleCircleIntersection(double[] c1, double r1,
			double[] c2, double r2, double[] knownPoint) {
		// Find the intersection of two circles, returning the point
		// that is NOT the known point (knownPoint is on both circles)

		double dx = c2[0] - c1[0];
		double dy = c2[1] - c1[1];
		double d = Math.sqrt(dx * dx + dy * dy);

		if (DoubleUtil.isZero(d)) {
			// Circles are concentric - either identical or no intersection
			return null;
		}

		// Using the radical line approach:
		// The radical line is perpendicular to the line connecting centers
		// a = (d² + r1² - r2²) / (2d)
		double a = (d * d + r1 * r1 - r2 * r2) / (2 * d);

		// h² = r1² - a²
		double h2 = r1 * r1 - a * a;
		if (h2 < -Kernel.STANDARD_PRECISION) {
			// Circles don't intersect
			return null;
		}
		double h = Math.sqrt(Math.max(0, h2));

		// Point on the line connecting centers, at distance a from c1
		double px = c1[0] + a * dx / d;
		double py = c1[1] + a * dy / d;

		// Two intersection points are offset by h perpendicular to the center line
		double[] int1 = new double[] { px + h * dy / d, py - h * dx / d };
		double[] int2 = new double[] { px - h * dy / d, py + h * dx / d };

		// Return the intersection point that is NOT the known point
		double dist1 = distance(int1, knownPoint);
		double dist2 = distance(int2, knownPoint);

		if (dist1 > dist2) {
			return int1;
		} else {
			return int2;
		}
	}
}
