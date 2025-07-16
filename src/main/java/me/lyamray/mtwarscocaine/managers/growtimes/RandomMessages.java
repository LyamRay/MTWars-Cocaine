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
                "<gradient:#555856:#555856><i>Denkt na:</i> De <color:#61ffab>regen</color> is eindelijk gestopt. Alles is rustig...</gradient>",
                "<gradient:#555856:#555856><i>Denkt na:</i> De lucht is helder vannacht. De <color:#61ffab>maan</color> geeft rust...</gradient>",
                "<gradient:#555856:#555856><i>Denkt na:</i> Het is ongewoon <color:#61ffab>zacht</color> voor deze tijd van de nacht...</gradient>",
                "<gradient:#555856:#555856><i>Denkt na:</i> De stilte voelt bijna... <color:#61ffab>vruchtbaar</color>.</gradient>"
        ));

        messages.put(TimeBlock.MORNING, List.of(
                "<gradient:#555856:#555856><i>Denkt na:</i> De zon komt op, en de <color:#61ffab>dauw</color> verdampt langzaam...</gradient>",
                "<gradient:#555856:#555856><i>Denkt na:</i> Het voelt als een <color:#61ffab>goede ochtend</color> vandaag...</gradient>",
                "<gradient:#555856:#555856><i>Denkt na:</i> Geen wind, geen regen. De natuur <color:#61ffab>ademt</color> kalmte uit...</gradient>",
                "<gradient:#555856:#555856><i>Denkt na:</i> Het licht valt precies goed... het ruikt naar <color:#61ffab>nieuw begin</color>.</gradient>"
        ));

        messages.put(TimeBlock.NOON, List.of(
                "<gradient:#555856:#555856><i>Denkt na:</i> Het is nu wat <color:#61ffab>warmer</color> geworden...</gradient>",
                "<gradient:#555856:#555856><i>Denkt na:</i> Geen wolk aan de lucht. Een <color:#61ffab>perfecte</color> dag tot nu toe.</gradient>",
                "<gradient:#555856:#555856><i>Denkt na:</i> De zon geeft alles <color:#61ffab>leven</color> vandaag...</gradient>",
                "<gradient:#555856:#555856><i>Denkt na:</i> De temperatuur is precies goed. Het voelt <color:#61ffab>juist</color> aan.</gradient>"
        ));

        messages.put(TimeBlock.EVENING, List.of(
                "<gradient:#555856:#555856><i>Denkt na:</i> De hitte is eindelijk <color:#61ffab>gezakt</color>. Adem in...</gradient>",
                "<gradient:#555856:#555856><i>Denkt na:</i> De lucht is nog steeds warm, maar de <color:#61ffab>rust</color> keert terug...</gradient>",
                "<gradient:#555856:#555856><i>Denkt na:</i> Geen regen, geen wind... de avond is <color:#61ffab>mild</color>.</gradient>",
                "<gradient:#555856:#555856><i>Denkt na:</i> Alles voelt ineens weer <color:#61ffab>stabiel</color> aan...</gradient>"
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