/*
 * Copyright 2015-2024 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.platform.tests.process;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

record WatchedOutput(Thread thread, ByteArrayOutputStream stream) {

	private static final Charset CHARSET = Charset.forName(System.getProperty("native.encoding"));

	String streamAsString() {
		return stream.toString(CHARSET);
	}
}
