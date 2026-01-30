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
import org.geogebra.common.kernel.algos.AlgoElement;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoPoint;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.common.util.DoubleUtil;
import org.geogebra.common.util.MyMath;

/**
 * Computes the orthocenter of a triangle.
 * The orthocenter is the intersection of the altitudes (Kimberling index 4).
 */
public class AlgoOrthocenter extends AlgoElement {

	private GeoPointND A, B, C;
	private GeoPointND orthocenter;

	/**
	 * Creates new algorithm for orthocenter
	 * @param cons construction
	 * @param label label
	 * @param A first vertex
	 * @param B second vertex
	 * @param C third vertex
	 */
	public AlgoOrthocenter(Construction cons, String label, GeoPointND A,
			GeoPointND B, GeoPointND C) {
		super(cons);
		this.A = A;
		this.B = B;
		this.C = C;
		int dim = MyMath.max(A.getDimension(), B.getDimension(),
				C.getDimension());
		orthocenter = kernel.getGeoFactory().newPoint(dim, cons);
		setInputOutput();
		compute();
		orthocenter.setLabel(label);
	}

	@Override
	public Commands getClassName() {
		return Commands.Orthocenter;
	}

	@Override
	protected void setInputOutput() {
		input = new GeoElement[3];
		input[0] = A.toGeoElement();
		input[1] = B.toGeoElement();
		input[2] = C.toGeoElement();

		setOnlyOutput(orthocenter);
		setDependencies();
	}

	/**
	 * @return the orthocenter point
	 */
	public GeoPointND getResult() {
		return orthocenter;
	}

	@Override
	public final void compute() {
		// Side lengths
		double a = B.distance(C);
		double b = C.distance(A);
		double c = A.distance(B);

		double a2 = a * a;
		double a4 = a2 * a2;
		double b2 = b * b;
		double b4 = b2 * b2;
		double c2 = c * c;
		double c4 = c2 * c2;

		// Orthocenter weights: -a⁴ + (b² - c²)² for Kimberling index 4
		// Q = (b² - c²)² (with cyclic permutation)
		double wA = -a4 + (b2 - c2) * (b2 - c2);
		double wB = -b4 + (c2 - a2) * (c2 - a2);
		double wC = -c4 + (a2 - b2) * (a2 - b2);
		double w = wA + wB + wC;

		if (Double.isNaN(w) || DoubleUtil.isZero(w)) {
			orthocenter.setUndefined();
		} else {
			GeoPoint.setBarycentric(A, B, C, wA, wB, wC, w, orthocenter);
		}
	}
}
