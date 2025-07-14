package me.lyamray.mtwarscocaine.managers;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TextDisplayManager {

    @Getter
    private static final TextDisplayManager instance = new TextDisplayManager();



}
