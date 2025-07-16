package me.lyamray.mtwarscocaine.managers.growtimes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lyamray.mtwarscocaine.utils.ChatColor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RandomMessages {

    @Getter
    private static final RandomMessages instance = new RandomMessages();

    private static final Map<TimeBlock, List<String>> messages = new EnumMap<>(TimeBlock.class);

    static {
        messages.put(TimeBlock.NIGHT, List.of(
                "<gray><i>Denkt na:</i> De regen is eindelijk <color:#61ffab>gestopt</color>... ideale nachtrust voor planten.</gray>",
                "<gray><i>Denkt na:</i> Wat een heldere en <color:#61ffab>rustige</color> nacht... alles lijkt mee te zitten.</gray>",
                "<gray><i>Denkt na:</i> De lucht is open en stil... een goede nacht voor <color:#61ffab>groei</color>.</gray>",
                "<gray><i>Denkt na:</i> De omstandigheden zijn zeldzaam <color:#61ffab>gunstig</color> voor dit uur.</gray>"
        ));

        messages.put(TimeBlock.MORNING, List.of(
                "<gray><i>Denkt na:</i> Wat een heerlijke <color:#61ffab>ochtend</color>... alles lijkt in balans.</gray>",
                "<gray><i>Denkt na:</i> De dauw trekt op en het zonnetje komt door. Perfect <color:#61ffab>weer</color>.</gray>",
                "<gray><i>Denkt na:</i> Rustig weer, milde temperatuur... niets staat <color:#61ffab>groei</color> in de weg.</gray>",
                "<gray><i>Denkt na:</i> Het voelt alsof dit een echt <color:#61ffab>groeimoment</color> is.</gray>"
        ));

        messages.put(TimeBlock.NOON, List.of(
                "<gray><i>Denkt na:</i> Het is stil en <color:#61ffab>zonnig</color>... ideaal voor de planten.</gray>",
                "<gray><i>Denkt na:</i> Wat een stabiele <color:#61ffab>middag</color>. Alles lijkt te kloppen.</gray>",
                "<gray><i>Denkt na:</i> De hitte valt mee vandaag. Gunstig weer voor <color:#61ffab>groei</color>.</gray>",
                "<gray><i>Denkt na:</i> De natuur houdt haar adem in... er hangt iets in de <color:#61ffab>lucht</color>.</gray>"
        ));

        messages.put(TimeBlock.EVENING, List.of(
                "<gray><i>Denkt na:</i> De warmte zakt langzaam weg... alles komt tot <color:#61ffab>rust</color>.</gray>",
                "<gray><i>Denkt na:</i> De wind ligt stil en de lucht is <color:#61ffab>zacht</color>... een zeldzaam goed moment.</gray>",
                "<gray><i>Denkt na:</i> De avond is kalm en warm. Dat komt de <color:#61ffab>groei</color> alleen maar ten goede.</gray>",
                "<gray><i>Denkt na:</i> Geen regen, geen wind... gewoon goed <color:#61ffab>weer</color>.</gray>"
        ));

    }

    public void maybeBroadcastGrowTimeMessage() {
        if (!RandomGrowTime.getInstance().isWithinAnyRange(LocalTime.now())) return;

        TimeBlock block = getCurrentTimeBlock();
        List<String> possibleMessages = messages.getOrDefault(block, Collections.emptyList());

        if (!possibleMessages.isEmpty()) {
            String selected = possibleMessages.get(ThreadLocalRandom.current().nextInt(possibleMessages.size()));
            Component component = ChatColor.color(selected);
            Bukkit.broadcast(component);
        }
    }

    private TimeBlock getCurrentTimeBlock() {
        int hour = LocalTime.now().getHour();
        return switch (hour) {
            case 0, 1, 2, 3, 4, 5 -> TimeBlock.NIGHT;
            case 6, 7, 8, 9, 10 -> TimeBlock.MORNING;
            case 11, 12, 13, 14, 15, 16 -> TimeBlock.NOON;
            default -> TimeBlock.EVENING;
        };
    }

    private enum TimeBlock {
        NIGHT,
        MORNING,
        NOON,
        EVENING
    }
}