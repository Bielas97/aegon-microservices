package com.aegon.domain;

import com.aegon.util.lang.SimpleId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationUsername extends SimpleId<String> {

	private ApplicationUsername(String internal) {
		super(internal);
	}

	public static ApplicationUsername valueOf(String username) {
		return new ApplicationUsername(validateUsername(username));
	}

	private static String validateUsername(String email) {
		final Matcher matcher = usernamePattern().matcher(email);
		if (!matcher.matches()) {
			throw UsernameException.notValid();
		}
		return email;
	}

	private static Pattern usernamePattern() {
		final String regex = "[a-zA-Z]+";
		return Pattern.compile(regex);
	}
}
