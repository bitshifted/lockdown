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
package com.vektorsoft.lockdown.app;

import com.vektorsoft.lockdown.app.init.InitialSelectionScreen;
import com.vektorsoft.lockdown.app.init.Initializer;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class Lockdown extends Application {

    private Node initScreen;
    private BorderPane mainScreen;
    private boolean needsInit = false;

    @Override
    public void start(Stage primaryStage) {

        StackPane root = new StackPane();
        if (needsInit) {
            try {
                InitialSelectionScreen initSelectionSCreen = new InitialSelectionScreen();
                initScreen = initSelectionSCreen.getScreen();
                root.getChildren().addAll(initScreen);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else {
            root.getChildren().add(mainScreen);
        }

        Scene scene = new Scene(root, 600, 400);
        Initializer.instance().setScene(scene);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        mainScreen = FXMLLoader.load(this.getClass().getResource("/gui/fxml/main_screen.fxml"));

        if (!Initializer.instance().checkFileStructure()) {
            System.out.println("File structure does not exist");
            needsInit = true;

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
