package ru.xander.swissknife.controller.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Control;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.springframework.util.StringUtils;
import ru.xander.swissknife.controller.MainController;
import ru.xander.swissknife.dto.JavaProperty;
import ru.xander.swissknife.util.FxUtil;

import java.util.Comparator;

/**
 * @author Alexander Shakhov
 */
public class MainTabProperties {

    private final MainController main;

    private MainTabProperties(MainController main) {
        this.main = main;
    }

    private void initializeTab() {
        FxUtil.makeCopyable(main.tableJavaProperties);
        main.tableJavaProperties.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        main.tableJavaProperties.getSelectionModel().setCellSelectionEnabled(true);

        main.columnPropName.prefWidthProperty().bind(main.tableJavaProperties.widthProperty().multiply(0.16));
        main.columnPropName.setCellValueFactory(new PropertyValueFactory<>("name"));

        main.columnPropValue.prefWidthProperty().bind(main.tableJavaProperties.widthProperty().multiply(0.83));
        main.columnPropValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        main.columnPropValue.setCellFactory(param -> {
            TableCell<JavaProperty, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(cell.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });


        ObservableList<JavaProperty> masterData = prepareMasterData();
        FilteredList<JavaProperty> filteredData = new FilteredList<>(masterData, property -> true);
        SortedList<JavaProperty> sortedData = new SortedList<>(filteredData, Comparator.comparing(JavaProperty::getName).thenComparing(JavaProperty::getValue));
        sortedData.comparatorProperty().bind(main.tableJavaProperties.comparatorProperty());
        main.tableJavaProperties.setItems(sortedData);


        main.textJavaPropFilter.textProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(property -> {
                    if (StringUtils.isEmpty(newValue)) {
                        return true;
                    }
                    String filter = newValue.toLowerCase();
                    if ((property.getName() != null) && property.getName().toLowerCase().contains(filter)) {
                        return true;
                    }
                    return (property.getValue() != null) && property.getValue().toLowerCase().contains(filter);
                }));

        main.textJavaPropFilter.textProperty().bindBidirectional(main.state.javaPropFilterProperty());
    }

    private ObservableList<JavaProperty> prepareMasterData() {
        ObservableList<JavaProperty> masterData = FXCollections.observableArrayList();
        System.getProperties().forEach((name, value) -> masterData.add(new JavaProperty(String.valueOf(name), String.valueOf(value))));
        return masterData;
    }

    public static void initialize(MainController main) {
        new MainTabProperties(main).initializeTab();
    }
}
