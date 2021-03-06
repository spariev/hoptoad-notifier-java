// Modified or written by Luca Marrocco for inclusion with hoptoad.
// Copyright (c) 2009 Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package hoptoad;

import java.util.*;

import org.apache.log4j.*;

public class HoptoadNoticeBuilderUsingFilteredSystemProperties extends HoptoadNoticeBuilder {

	public HoptoadNoticeBuilderUsingFilteredSystemProperties(final String apiKey, final Backtrace backtraceBuilder, final Throwable throwable, final String env, final String host, final String port, final boolean secure) {
		super(apiKey, backtraceBuilder, throwable, env);

		environment(System.getProperties());

		addMDCToSession();

		standardEnvironmentFilters();

		ec2EnvironmentFilters();

        setHost(host);
        setPort(port);
        setSecure(secure);
	}

	private void addMDCToSession() {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = MDC.getContext();

		if (map != null) {
			addSessionKey(":key", Integer.toString(map.hashCode()));
			addSessionKey(":data", map);
		}
	}
}
