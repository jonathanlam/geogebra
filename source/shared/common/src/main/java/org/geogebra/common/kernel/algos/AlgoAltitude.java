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

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.Path;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoPoint;
import org.geogebra.common.kernel.geos.GeoSegment;
import org.geogebra.common.kernel.geos.GeoVec3D;
import org.geogebra.common.kernel.kernelND.GeoPointND;

/**
 * Computes the altitude (perpendicular segment) from a point to a path.
 * The altitude is the segment from the given point to the foot of the
 * perpendicular on the path (i.e., the closest point on the path).
 */
public class AlgoAltitude extends AlgoElement {

	private GeoPointND point; // input point
	private Path path; // input path (line, segment, etc.)
	private GeoPoint foot; // foot of perpendicular (internal)
	private GeoSegment altitude; // output segment

	/**
	 * Creates altitude from point to path
	 * @param cons construction
	 * @param label label for output segment
	 * @param point the point from which to drop the altitude
	 * @param path the path to drop the altitude onto
	 */
	public AlgoAltitude(Construction cons, String label, GeoPointND point,
			Path path) {
		super(cons);
		this.point = point;
		this.path = path;

		// Create the foot point (not labeled, internal use)
		foot = new GeoPoint(cons);
		foot.setPath(path);

		// Create the output segment
		altitude = new GeoSegment(cons);

		setInputOutput();
		compute();
		altitude.setLabel(label);
	}

	@Override
	public Commands getClassName() {
		return Commands.Altitude;
	}

	@Override
	protected void setInputOutput() {
		input = new GeoElement[2];
		input[0] = point.toGeoElement();
		input[1] = path.toGeoElement();

		setOnlyOutput(altitude);
		setDependencies();
	}

	/**
	 * @return the altitude segment
	 */
	public GeoSegment getSegment() {
		return altitude;
	}

	/**
	 * @return the foot of the perpendicular
	 */
	public GeoPoint getFoot() {
		return foot;
	}

	@Override
	public final void compute() {
		if (!point.isDefined() || !path.toGeoElement().isDefined()) {
			altitude.setUndefined();
			return;
		}

		// Compute foot of perpendicular (closest point on path)
		foot.setCoords((GeoPoint) point);
		path.pointChanged(foot);
		foot.updateCoords();

		if (!foot.isDefined()) {
			altitude.setUndefined();
			return;
		}

		// Set segment endpoints and compute the line equation
		altitude.setPoints((GeoPoint) point, foot);
		GeoVec3D.lineThroughPoints((GeoPoint) point, foot, altitude);
		altitude.calcLength();
	}

	@Override
	final public String toString(StringTemplate tpl) {
		return getLoc().getPlainDefault("AltitudeFromAToB",
				"Altitude from %0 to %1",
				point.getLabel(tpl),
				path.toGeoElement().getLabel(tpl));
	}
}
