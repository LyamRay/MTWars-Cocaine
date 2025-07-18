package me.lyamray.mtwarscocaine.managers.growtimes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RandomGrowTime {

    @Getter
    private static final RandomGrowTime instance = new RandomGrowTime();

    private final int maxRanges = 3;
    private final int timeBetweenRanges = 1;

    @Getter
    @Setter
    private List<TimeRange> ranges = new ArrayList<>();

    private static final int MIN_HOUR = 0;
    private static final int MAX_HOUR = 22;

    public void generate() {
        ranges.clear();

        List<Integer> availableHours = new ArrayList<>();
        for (int i = MIN_HOUR; i <= MAX_HOUR; i++) {
            availableHours.add(i);
        }

        Collections.shuffle(availableHours, new Random());

        for (int hour : availableHours) {
            LocalTime start = LocalTime.of(hour, 0);
            LocalTime end = start.plusHours(timeBetweenRanges);
            TimeRange newRange = new TimeRange(start, end);

            if (ranges.stream().noneMatch(existing -> existing.overlaps(newRange))) {
                ranges.add(newRange);
            }

            if (ranges.size() == maxRanges) break;
        }

        ranges.sort(Comparator.comparing(TimeRange::start));
    }


    public boolean isWithinAnyRange(LocalTime now) {
        return ranges.stream().anyMatch(r -> r.isWithin(now));
    }

    public record TimeRange(LocalTime start, LocalTime end) {
        public boolean isWithin(LocalTime time) {
            return !time.isBefore(start) && time.isBefore(end);
        }

        public boolean overlaps(TimeRange other) {
            return !end.isBefore(other.start) && !start.isAfter(other.end);
        }
    }
}
