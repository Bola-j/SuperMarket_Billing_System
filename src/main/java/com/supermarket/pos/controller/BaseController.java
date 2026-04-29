package com.supermarket.pos.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * BaseController abstract class for application controllers.
 * Provides common controller methods for handling user interactions.
 */
public abstract class BaseController {

    protected void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    protected void showInfo(String title, String content) {
        showAlert(AlertType.INFORMATION, title, null, content);
    }

    protected void showWarning(String title, String content) {
        showAlert(AlertType.WARNING, title, null, content);
    }

    protected void showError(String title, String content) {
        showAlert(AlertType.ERROR, title, null, content);
    }
}
