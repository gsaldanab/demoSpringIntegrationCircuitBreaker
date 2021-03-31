package com.geo.demospringintegrationcircuitbreaker.util;

import java.util.Objects;

public class ExceptionUtil {

	public static Throwable getRootCause(Throwable throwable) {
		Throwable rootCause = Objects.requireNonNull(throwable);
		while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
			rootCause = rootCause.getCause();
		}
		return rootCause;
	}
}
