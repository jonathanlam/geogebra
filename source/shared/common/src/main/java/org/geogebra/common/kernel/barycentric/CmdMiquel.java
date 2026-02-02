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

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.commands.CommandProcessor;
import org.geogebra.common.kernel.commands.EvalInfo;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoLine;
import org.geogebra.common.main.MyError;

/**
 * Miquel[&lt;Line&gt;, &lt;Line&gt;, &lt;Line&gt;, &lt;Line&gt;]
 */
public class CmdMiquel extends CommandProcessor {

	/**
	 * Create new command processor
	 * @param kernel kernel
	 */
	public CmdMiquel(Kernel kernel) {
		super(kernel);
	}

	@Override
	final public GeoElement[] process(Command c, EvalInfo info) throws MyError {
		int n = c.getArgumentNumber();
		boolean[] ok = new boolean[n];
		GeoElement[] arg;

		switch (n) {
		case 4:
			arg = resArgs(c, info);
			if ((ok[0] = arg[0] instanceof GeoLine)
					&& (ok[1] = arg[1] instanceof GeoLine)
					&& (ok[2] = arg[2] instanceof GeoLine)
					&& (ok[3] = arg[3] instanceof GeoLine)) {

				AlgoMiquel algo = new AlgoMiquel(cons, c.getLabel(),
						(GeoLine) arg[0], (GeoLine) arg[1],
						(GeoLine) arg[2], (GeoLine) arg[3]);

				GeoElement[] ret = { algo.getResult() };
				return ret;
			}
			throw argErr(c, getBadArg(ok, arg));
		default:
			throw argNumErr(c);
		}
	}
}
