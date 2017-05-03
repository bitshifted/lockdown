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

import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.WizardPane;

/**
 * Wizard page which has access to FXML controller.
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class ControlledWizardPane extends WizardPane {

    private final KeyringCreateController controller;
    private final int page;

    public ControlledWizardPane(KeyringCreateController controller, int page) {
        this.controller = controller;
        this.page = page;
    }

    @Override
    public void onEnteringPage(Wizard wizard) {
        switch (page) {
            case 2:
                controller.generateMnemonicWords();
                wizard.invalidProperty().unbind();
                wizard.invalidProperty().bind(controller.getPasswordMismatchObservable().matchValue());
                break;
            case 3:
                controller.initMnemonicQRCode();
                break;
        }

    }

}
