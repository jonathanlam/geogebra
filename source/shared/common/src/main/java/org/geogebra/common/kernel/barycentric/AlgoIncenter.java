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
import org.geogebra.common.util.MyMath;

/**
 * Computes the incenter of a triangle.
 * The incenter is the center of the inscribed circle (Kimberling index 1).
 */
public class AlgoIncenter extends AlgoElement {

	private GeoPointND A, B, C;
	private GeoPointND incenter;

	/**
	 * Creates new algorithm for incenter
	 * @param cons construction
	 * @param label label
	 * @param A first vertex
	 * @param B second vertex
	 * @param C third vertex
	 */
	public AlgoIncenter(Construction cons, String label, GeoPointND A,
			GeoPointND B, GeoPointND C) {
		super(cons);
		this.A = A;
		this.B = B;
		this.C = C;
		int dim = MyMath.max(A.getDimension(), B.getDimension(),
				C.getDimension());
		incenter = kernel.getGeoFactory().newPoint(dim, cons);
		setInputOutput();
		compute();
		incenter.setLabel(label);
	}

	@Override
	public Commands getClassName() {
		return Commands.Incenter;
	}

	@Override
	protected void setInputOutput() {
		input = new GeoElement[3];
		input[0] = A.toGeoElement();
		input[1] = B.toGeoElement();
		input[2] = C.toGeoElement();

		setOnlyOutput(incenter);
		setDependencies();
	}

	/**
	 * @return the incenter point
	 */
	public GeoPointND getResult() {
		return incenter;
	}

	@Override
	public final void compute() {
		// Side lengths
		double a = B.distance(C);
		double b = C.distance(A);
		double c = A.distance(B);

		// Incenter weights are proportional to opposite side lengths
		double wA = a;
		double wB = b;
		double wC = c;
		double w = wA + wB + wC;

		if (w == 0) {
			incenter.setUndefined();
		} else {
			GeoPoint.setBarycentric(A, B, C, wA, wB, wC, w, incenter);
		}
	}
}
