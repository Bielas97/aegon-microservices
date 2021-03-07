package com.aegon.infrastructure;

import com.aegon.domain.EmailMessageEvent;
import com.aegon.util.email.service.AegonEmailSender;
import com.aegon.util.lang.eventsourcing.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailEventHandler extends EventHandler<EmailMessageEvent> {

	private final AegonEmailSender sender;

	@Override
	public void handle(EmailMessageEvent event) {
		sender.send(event.getReceiver(), event.getMessage());
	}
}
