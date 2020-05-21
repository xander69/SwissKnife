package ru.xander.swissknife.util;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;

/**
 * @author Alexander Shakhov
 */
public final class FxUtil {
    private FxUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void writeInputStream(BlockingQueue<String> textQueue, InputStream inputStream) throws IOException, InterruptedException {
        int len;
        byte[] buffer = new byte[4096];
        while ((len = inputStream.read(buffer)) != -1) {
            textQueue.put(new String(buffer, 0, len));
        }
    }

    public static void makeCopyable(TableView<?> tableView) {
        KeyCodeCombination keyCodeCopy = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);
        tableView.setOnKeyPressed(event -> {
            if (keyCodeCopy.match(event)) {
                copySelectionToClipboard(tableView);
            }
        });
    }

    private static void copySelectionToClipboard(final TableView<?> tableView) {
        final StringBuilder clipboardString = new StringBuilder();
        int prevRow = -1;
        for (TablePosition selectedCell : tableView.getSelectionModel().getSelectedCells()) {
            if ((prevRow != -1) && (prevRow != selectedCell.getRow())) {
                clipboardString.append('\n');
            }
            TableColumn tableColumn = selectedCell.getTableColumn();
            Object value = tableColumn.getCellData(selectedCell.getRow());
            clipboardString.append(value).append('\t');
            prevRow = selectedCell.getRow();
        }
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(clipboardString.toString().trim());
        Clipboard.getSystemClipboard().setContent(clipboardContent);
    }
}
