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
 * Computes the centroid of a triangle.
 * The centroid is the intersection of the medians (Kimberling index 2).
 */
public class AlgoCentroidTriangle extends AlgoElement {

	private GeoPointND A, B, C;
	private GeoPointND centroid;

	/**
	 * Creates new algorithm for centroid
	 * @param cons construction
	 * @param label label
	 * @param A first vertex
	 * @param B second vertex
	 * @param C third vertex
	 */
	public AlgoCentroidTriangle(Construction cons, String label, GeoPointND A,
			GeoPointND B, GeoPointND C) {
		super(cons);
		this.A = A;
		this.B = B;
		this.C = C;
		int dim = MyMath.max(A.getDimension(), B.getDimension(),
				C.getDimension());
		centroid = kernel.getGeoFactory().newPoint(dim, cons);
		setInputOutput();
		compute();
		centroid.setLabel(label);
	}

	@Override
	public Commands getClassName() {
		return Commands.Centroid;
	}

	@Override
	protected void setInputOutput() {
		input = new GeoElement[3];
		input[0] = A.toGeoElement();
		input[1] = B.toGeoElement();
		input[2] = C.toGeoElement();

		setOnlyOutput(centroid);
		setDependencies();
	}

	/**
	 * @return the centroid point
	 */
	public GeoPointND getResult() {
		return centroid;
	}

	@Override
	public final void compute() {
		// Centroid has equal weights (1:1:1)
		GeoPoint.setBarycentric(A, B, C, 1, 1, 1, 3, centroid);
	}
}
