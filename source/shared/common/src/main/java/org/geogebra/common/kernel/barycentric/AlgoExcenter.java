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
import org.geogebra.common.kernel.geos.GeoNumberValue;
import org.geogebra.common.kernel.geos.GeoPoint;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.common.util.MyMath;

/**
 * Computes an excenter of a triangle.
 * The three excenters are the centers of the excircles (circles tangent to one side
 * and the extensions of the other two sides).
 * <ul>
 * <li>Excenter 1 (opposite vertex A): barycentric weights = (-a, b, c)</li>
 * <li>Excenter 2 (opposite vertex B): barycentric weights = (a, -b, c)</li>
 * <li>Excenter 3 (opposite vertex C): barycentric weights = (a, b, -c)</li>
 * </ul>
 * where a = |BC|, b = |CA|, c = |AB| (opposite side lengths).
 */
public class AlgoExcenter extends AlgoElement {

	private GeoPointND A, B, C;
	private GeoNumberValue index;
	private GeoPointND excenter;

	/**
	 * Creates new algorithm for excenter
	 * @param cons construction
	 * @param label label
	 * @param A first vertex
	 * @param B second vertex
	 * @param C third vertex
	 * @param index which excenter (1, 2, or 3)
	 */
	public AlgoExcenter(Construction cons, String label, GeoPointND A,
			GeoPointND B, GeoPointND C, GeoNumberValue index) {
		super(cons);
		this.A = A;
		this.B = B;
		this.C = C;
		this.index = index;
		int dim = MyMath.max(A.getDimension(), B.getDimension(),
				C.getDimension());
		excenter = kernel.getGeoFactory().newPoint(dim, cons);
		setInputOutput();
		compute();
		excenter.setLabel(label);
	}

	@Override
	public Commands getClassName() {
		return Commands.Excenter;
	}

	@Override
	protected void setInputOutput() {
		input = new GeoElement[4];
		input[0] = A.toGeoElement();
		input[1] = B.toGeoElement();
		input[2] = C.toGeoElement();
		input[3] = index.toGeoElement();

		setOnlyOutput(excenter);
		setDependencies();
	}

	/**
	 * @return the excenter point
	 */
	public GeoPointND getResult() {
		return excenter;
	}

	@Override
	public final void compute() {
		// Side lengths (opposite to each vertex)
		double a = B.distance(C); // opposite to A
		double b = C.distance(A); // opposite to B
		double c = A.distance(B); // opposite to C

		int idx = (int) index.getDouble();
		double wA, wB, wC;

		switch (idx) {
		case 1:
			// Excenter opposite to A: (-a, b, c)
			wA = -a;
			wB = b;
			wC = c;
			break;
		case 2:
			// Excenter opposite to B: (a, -b, c)
			wA = a;
			wB = -b;
			wC = c;
			break;
		case 3:
			// Excenter opposite to C: (a, b, -c)
			wA = a;
			wB = b;
			wC = -c;
			break;
		default:
			excenter.setUndefined();
			return;
		}

		double w = wA + wB + wC;

		if (w == 0) {
			excenter.setUndefined();
		} else {
			GeoPoint.setBarycentric(A, B, C, wA, wB, wC, w, excenter);
		}
	}
}
