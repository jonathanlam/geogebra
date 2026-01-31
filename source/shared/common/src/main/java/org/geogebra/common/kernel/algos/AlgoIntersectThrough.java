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

package org.geogebra.common.kernel.algos;

import org.geogebra.common.euclidian.EuclidianConstants;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoLine;
import org.geogebra.common.kernel.geos.GeoPoint;
import org.geogebra.common.kernel.geos.GeoVec3D;
import org.geogebra.common.kernel.kernelND.GeoPointND;

/**
 * Algorithm to compute the intersection of:
 * - The line passing through two points A and B
 * - Another line or segment l
 *
 * This is a one-step tool that combines Line[A,B] and Intersect[line, l]
 * without creating the intermediate line object.
 */
public class AlgoIntersectThrough extends AlgoElement {

	private GeoPointND pointA; // input: first point
	private GeoPointND pointB; // input: second point
	private GeoLine line; // input: line/segment to intersect with
	private GeoLine tempLine; // temporary line through A and B (internal)
	private GeoPoint intersection; // output: intersection point

	/**
	 * Creates intersection of line through A and B with line l.
	 * @param cons construction
	 * @param label label for output point
	 * @param pointA first point defining the line
	 * @param pointB second point defining the line
	 * @param line the line/segment to intersect with
	 */
	public AlgoIntersectThrough(Construction cons, String label,
			GeoPointND pointA, GeoPointND pointB, GeoLine line) {
		super(cons);
		this.pointA = pointA;
		this.pointB = pointB;
		this.line = line;

		// Create temporary line for computation (not added to construction)
		tempLine = new GeoLine(cons);

		// Create output point
		intersection = new GeoPoint(cons);

		setInputOutput();
		compute();
		intersection.setLabel(label);
		addIncidence();
	}

	private void addIncidence() {
		intersection.addIncidence(line, false);
	}

	@Override
	public Commands getClassName() {
		return Commands.IntersectThrough;
	}

	@Override
	public int getRelatedModeID() {
		return EuclidianConstants.MODE_INTERSECT_THROUGH;
	}

	@Override
	protected void setInputOutput() {
		input = new GeoElement[3];
		input[0] = pointA.toGeoElement();
		input[1] = pointB.toGeoElement();
		input[2] = line;

		setOnlyOutput(intersection);
		setDependencies();
	}

	/**
	 * @return the intersection point
	 */
	public GeoPoint getPoint() {
		return intersection;
	}

	@Override
	public final void compute() {
		// Check if all inputs are defined
		if (!pointA.isDefined() || !pointB.isDefined() || !line.isDefined()) {
			intersection.setUndefined();
			return;
		}

		// Compute line through A and B
		GeoVec3D.lineThroughPoints((GeoPoint) pointA, (GeoPoint) pointB, tempLine);

		if (!tempLine.isDefined()) {
			intersection.setUndefined();
			return;
		}

		// Compute intersection of tempLine and line using cross product
		GeoVec3D.cross(tempLine, line, intersection);

		// Check if intersection point is valid
		if (intersection.isDefined()) {
			// For segments/rays, check if the point is on the limited path
			if (!line.isIntersectionPointIncident(intersection, Kernel.MIN_PRECISION)) {
				intersection.setUndefined();
			}
		}
	}

	@Override
	final public String toString(StringTemplate tpl) {
		return getLoc().getPlainDefault("IntersectionOfLineABAndC",
				"Intersection of line %0%1 and %2",
				pointA.getLabel(tpl),
				pointB.getLabel(tpl),
				line.getLabel(tpl));
	}
}
