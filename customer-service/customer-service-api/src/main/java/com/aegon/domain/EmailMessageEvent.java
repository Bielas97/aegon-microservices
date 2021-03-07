package com.aegon.domain;

import com.aegon.util.lang.Email;
import com.aegon.util.lang.EmailMessage;
import com.aegon.util.lang.Preconditions;
import com.aegon.util.lang.eventsourcing.Event;
import java.util.Objects;
import lombok.Getter;

@Getter
public class EmailMessageEvent extends Event {

	private final Email receiver;

	private final EmailMessage message;

	public EmailMessageEvent(Email receiver, EmailMessage message) {
		this.receiver = Preconditions.requireNonNull(receiver);
		this.message = Preconditions.requireNonNull(message);
	}

	private String prettyPrint() {
		return "Receiver: " + receiver.getInternal() + "\n" +
			   "\n" + "\n" + "\n" + "\n" +
			   "Topic: " + message.getTopic() + "\n" +
			   "Message: " + message.getMsg();
	}

	@Override
	public String toString() {
		return prettyPrint();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EmailMessageEvent that = (EmailMessageEvent) o;
		return Objects.equals(receiver, that.receiver) && Objects.equals(message, that.message);
	}

	@Override
	public int hashCode() {
		return Objects.hash(receiver, message);
	}
}
