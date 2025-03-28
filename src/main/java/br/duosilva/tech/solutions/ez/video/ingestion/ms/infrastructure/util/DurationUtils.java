package br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.util;

import java.time.Duration;

public class DurationUtils {
	
	public static String formatDuration(long millis) {
		Duration duration = Duration.ofMillis(millis);
		long hours = duration.toHours();
		long minutes = duration.toMinutesPart();
		long seconds = duration.toSecondsPart();
		long milliseconds = duration.toMillisPart();

		return String.format("%02dh %02dm %02ds %03dms", hours, minutes, seconds, milliseconds);
	}

}
