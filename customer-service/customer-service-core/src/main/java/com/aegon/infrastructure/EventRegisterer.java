package com.aegon.infrastructure;

import com.aegon.util.lang.eventsourcing.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventRegisterer implements CommandLineRunner {

	private final EmailEventHandler emailEventHandler;

	@Override
	public void run(String... args) {
		EventBus.register(emailEventHandler);
	}
}
