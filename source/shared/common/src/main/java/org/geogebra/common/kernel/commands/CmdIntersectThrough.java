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

package org.geogebra.common.kernel.commands;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.algos.AlgoIntersectThrough;
import org.geogebra.common.kernel.algos.AlgoIntersectThroughConic;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.geos.GeoConic;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoLine;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.common.main.MyError;

/**
 * IntersectThrough[<Point A>, <Point B>, <Line or Segment l>]
 * IntersectThrough[<Point A>, <Point B>, <Circle c>]
 *
 * Returns the intersection of the line through A and B with line/segment l or circle c.
 * This combines the Line[A,B] and Intersect commands into a single step.
 */
public class CmdIntersectThrough extends CommandProcessor {

	/**
	 * Create new command processor
	 * @param kernel kernel
	 */
	public CmdIntersectThrough(Kernel kernel) {
		super(kernel);
	}

	@Override
	final public GeoElement[] process(Command c, EvalInfo info) throws MyError {
		int n = c.getArgumentNumber();
		boolean[] ok = new boolean[n];
		GeoElement[] arg;

		switch (n) {
		case 3:
			arg = resArgs(c, info);

			// IntersectThrough[<Point>, <Point>, <Line>]
			if ((ok[0] = arg[0].isGeoPoint())
					&& (ok[1] = arg[1].isGeoPoint())
					&& (ok[2] = arg[2].isGeoLine())) {

				AlgoIntersectThrough algo = new AlgoIntersectThrough(cons,
						c.getLabel(),
						(GeoPointND) arg[0],
						(GeoPointND) arg[1],
						(GeoLine) arg[2]);

				GeoElement[] ret = { algo.getPoint() };
				return ret;
			}

			// IntersectThrough[<Point>, <Point>, <Conic>]
			if ((ok[0] = arg[0].isGeoPoint())
					&& (ok[1] = arg[1].isGeoPoint())
					&& (ok[2] = arg[2].isGeoConic())) {

				AlgoIntersectThroughConic algo = new AlgoIntersectThroughConic(cons,
						c.getLabels(),
						(GeoPointND) arg[0],
						(GeoPointND) arg[1],
						(GeoConic) arg[2]);

				return algo.getPoints();
			}

			throw argErr(c, getBadArg(ok, arg));

		default:
			throw argNumErr(c);
		}
	}
}
