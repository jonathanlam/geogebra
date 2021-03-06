/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

package org.geogebra.common.kernel.algos;

import java.math.BigInteger;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoNumberValue;
import org.geogebra.common.util.DoubleUtil;

/**
 * Computes GCD[a, b]
 * 
 * @author Michael Borcherds
 */
public class AlgoGCD extends AlgoTwoNumFunction {

	public AlgoGCD(Construction cons, String label, GeoNumberValue a,
			GeoNumberValue b) {
		super(cons, label, a, b);
	}

	@Override
	public Commands getClassName() {
		return Commands.GCD;
	}

	@Override
	public final double computeValue(double aVal, double bVal) {
		if (aVal > Long.MAX_VALUE || bVal > Long.MAX_VALUE || aVal < -Long.MAX_VALUE
				|| bVal < -Long.MAX_VALUE) {
			return Double.NaN;
		}

		if (DoubleUtil.isInteger(aVal) && DoubleUtil.isInteger(bVal)) {
			BigInteger i1 = BigInteger.valueOf(Math.round(aVal));
			BigInteger i2 = BigInteger.valueOf(Math.round(bVal));

			i1 = i1.gcd(i2);

			double result = Math.abs(i1.doubleValue());

			// can't store integers greater than this in a double accurately
			if (result < 1e15) {
				return result;
			}
		}
		return Double.NaN;
	}
}
