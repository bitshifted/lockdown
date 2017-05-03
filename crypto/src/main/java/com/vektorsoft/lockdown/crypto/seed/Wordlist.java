/*
 * Copyright (C) 2017 Vektorsoft Ltd. (http://www.vektorsoft.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.vektorsoft.lockdown.crypto.seed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class Wordlist {

    private static final int WORDLIST_SIZE = 2048;

    private final String[] words = new String[WORDLIST_SIZE];
    private boolean wordsLoaded = false;

    public void loadWordlist(MnemonicLanguage language) throws IOException {
        switch (language) {
            case ENGLISH:
                loadFromFile("/wordlist/english.txt");
                break;
            case SPANISH:
                loadFromFile("/wordlist/spanish.txt");
                break;
        }
        wordsLoaded = true;
    }

    public boolean validateWords(String[] mnemonicWords) {
        if (!wordsLoaded) {
            throw new IllegalStateException("Word list not loaded");
        }
        boolean valid = true;
        for (String word : mnemonicWords) {
            if (Arrays.binarySearch(words, word) < 0) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    private void loadFromFile(String location) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(location)))) {
            String line = null;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                words[count] = line;
                count++;
            }
        }
    }

    public String getWord(int index) {
        if (!wordsLoaded) {
            throw new IllegalStateException("Word list not loaded");
        }
        return words[index];
    }

}
