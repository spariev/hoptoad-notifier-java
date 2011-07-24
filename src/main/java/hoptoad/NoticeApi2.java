// Modified or written by Luca Marrocco for inclusion with hoptoad.
// Copyright (c) 2009 Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package hoptoad;

import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang.*;

public class NoticeApi2 {

	private final StringBuilder stringBuilder = new StringBuilder();

	public NoticeApi2(HoptoadNotice notice) {
		notice("2.0.0");
		{
			apikey(notice);

			notifier();
			{
				name("hoptoad");
				version("1.7-socrata-SNAPSHOT");
				url("http://hoptoad.googlecode.com");
			}
			end("notifier");

			error();
			{
				tag("class", notice.errorClass());
				tag("message", notice.errorMessage());

				backtrace();
				{
					for (final String backtrace : notice.backtrace()) {
						line(backtrace);
					}
				}
				end("backtrace");
			}
			end("error");

			if (notice.hasRequest()) {
				addRequest(notice);
			}

			server_environment();
			{
				tag("project-root", notice.projectRoot());
				tag("environment-name", notice.env());
			}
			end("server-environment");
		}
		end("notice");
	}

	private void addRequest(HoptoadNotice notice) {
		request();
		{
			tag("url", notice.url());
			tag("component", notice.component());
			vars("params", notice.request());
			vars("session", notice.session());
			vars("cgi-data", notice.environment());
		}
		end("request");
	}

	private void vars(String sectionName, Map<String, Object> vars) {
		if (vars.isEmpty()) {
			return;
		}

		tag(sectionName);
		for (Entry<String, Object> var : vars.entrySet()) {
			append("<var key=\"" + var.getKey() + "\">");
			text(var.getValue().toString());
			append("</var>");
		}
		end(sectionName);
	}

	private void request() {
		tag("request");
	}

	private void apikey(HoptoadNotice notice) {
		tag("api-key");
		{
			append(notice.apiKey());
		}
		end("api-key");
	}

	private void append(String str) {
		stringBuilder.append(str);
	}

	private void backtrace() {
		tag("backtrace");
	}

	private void end(String string) {
		append("</" + string + ">");
	}

	private void error() {
		tag("error");
	}

	private void server_environment() {
		tag("server-environment");
	}

	private void line(String backtrace) {
		append(new BacktraceLine(backtrace).toXml());
	}

	private void name(String name) {
		tag("name", name);
	}

	private void notice(String string) {
		append("<?xml version=\"1.0\"?>");
		append("<notice version=\"" + string + "\">");
	}

	private void notifier() {
		tag("notifier");
	}

	private NoticeApi2 tag(String string) {
		append("<" + string + ">");
		return this;
	}

	private void tag(String string, String contents) {
		tag(string).text(contents).end(string);
	}

	private NoticeApi2 text(String string) {
		append(StringEscapeUtils.escapeXml(string));
		return this;
	}

	public String toString() {
		return stringBuilder.toString();
	}

	private void url(String url) {
		tag("url", url);
	}

	private void version(String version) {
		tag("version", version);
	}
}
