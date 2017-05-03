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
package com.vektorsoft.lockdown.app.init;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Observable for matching password entries to make sure that are the same.
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class PasswordMismatchObservable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordMismatchObservable.class);

    private final StringProperty pswdProperty;
    private final StringProperty confirmationProperty;

    private final SimpleBooleanProperty matchProperty;

    public PasswordMismatchObservable(StringProperty password, StringProperty confirmation) {
        this.pswdProperty = password;
        this.confirmationProperty = confirmation;
        matchProperty = new SimpleBooleanProperty();

        pswdProperty.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            matchProperty.setValue(!newValue.equals(confirmationProperty.get()));
            LOGGER.debug("Match value: " + matchProperty.get());
        });

        confirmationProperty.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            matchProperty.setValue(!newValue.equals(pswdProperty.get()));
            LOGGER.debug("Match value: " + matchProperty.get());
        });
    }

    public ObservableValue<Boolean> matchValue() {
        return matchProperty;
    }
}
