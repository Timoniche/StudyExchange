package com.studyexchange.telegramapiutils;

import com.pengrad.telegrambot.model.PhotoSize;

import java.util.Arrays;
import java.util.Collections;

public final class PhotoUtils {

    private PhotoUtils() {
    }

    public static PhotoSize pickBestPhoto(PhotoSize[] photos) {
        return Collections.max(
                Arrays.asList(photos),
                (o1, o2) -> {
                    int fst = o1.width() * o1.height();
                    int snd = o2.width() * o2.height();
                    return Integer.compare(fst, snd);
                }
        );
    }
}
