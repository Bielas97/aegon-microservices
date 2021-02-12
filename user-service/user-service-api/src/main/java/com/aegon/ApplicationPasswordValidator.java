package com.aegon;

import com.aegon.domain.ApplicationUserPassword;
import com.aegon.domain.ApplicationUserPasswordException;
import com.aegon.util.lang.Preconditions;
import com.aegon.util.lang.Validator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationPasswordValidator implements Validator {

	private final ApplicationUserPassword password;

	private final ApplicationUserPassword passwordConfirmation;

	private ApplicationPasswordValidator(ApplicationUserPassword password, ApplicationUserPassword passwordConfirmation) {
		this.password = Preconditions.requireNonNull(password);
		this.passwordConfirmation = Preconditions.requireNonNull(passwordConfirmation);
	}

	public static ApplicationPasswordValidator with(ApplicationUserPassword password, ApplicationUserPassword passwordConfirmation) {
		return new ApplicationPasswordValidator(password, passwordConfirmation);
	}

	public void validate() {
		validateRegexPassword();
		validatePasswordAreTheSame();
	}

	public void validatePasswordAreTheSame() {
		if (!password.sameAs(passwordConfirmation)) {
			throw ApplicationUserPasswordException.of("Confirmation password is different");
		}
	}

	private void validateRegexPassword() {
		final Matcher matcher = passwordPattern().matcher(password.getInternal());
		if (!matcher.matches()) {
			throw ApplicationUserPasswordException.notValid();
		}
	}

	private Pattern passwordPattern() {
		final String regex = "[a-zA-Z0-9]+";
		return Pattern.compile(regex);
	}

}
