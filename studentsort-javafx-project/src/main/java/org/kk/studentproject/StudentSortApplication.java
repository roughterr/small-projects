package org.kk.studentproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.kk.studentproject.sort.SortingAlgorithm;
import org.kk.studentproject.sort.StudentSort;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class StudentSortApplication extends Application {
    private StudentSort studentSort = new StudentSort();
    private StudentFileUtils studentFileUtils = new StudentFileUtils();
    private List<Student> students = new ArrayList<>();
    private Text numberOfStudentsText = new Text("no file selected");
    private Text amountOfTimeSpentText = new Text("");
    private FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
    private ComboBox<SortingAlgorithm> cbxSortingAlgorithm = new ComboBox<>();

    @Override
    public void start(Stage stage) throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(extFilter);

        FileChooser fileSaveChooser = new FileChooser();
        fileSaveChooser.setTitle("Save");
        fileSaveChooser.getExtensionFilters().add(extFilter);

        Button buttonSelectFile = new Button();
        buttonSelectFile.setText("Select file");
        buttonSelectFile.setPrefSize(100, 20);
        buttonSelectFile.setOnAction(e -> {
                    File file = fileChooser.showOpenDialog(stage);
                    students = studentFileUtils.studentFileToList(file);
                    numberOfStudentsText.setText("Number of students: " + students.size());
                }
        );

        // create a alert
        Alert alertSelectAlgo = new Alert(Alert.AlertType.INFORMATION, "Please select sorting algorithm");
        Alert alertSelectFile = new Alert(Alert.AlertType.INFORMATION, "Please select a valid file");

        Button buttonSort = new Button();
        buttonSort.setText("Sort");
        buttonSort.setPrefSize(100, 20);
        buttonSort.setOnAction(e -> {
                    System.out.println("Selected sorting algorithm: " + cbxSortingAlgorithm.getSelectionModel().getSelectedItem());
                    if (cbxSortingAlgorithm.getSelectionModel().getSelectedItem() == null) {
                        alertSelectAlgo.show();
                        return;
                    }
                    if (students.isEmpty()) {
                        alertSelectFile.show();
                        return;
                    }
                    long timeSpent = studentSort.sort(students, cbxSortingAlgorithm.getSelectionModel().getSelectedItem());
                    amountOfTimeSpentText.setText("Amount of time spent for sorting (in nanoseconds): " + timeSpent);
                    File file = fileSaveChooser.showSaveDialog(stage);
                    String text = studentFileUtils.studentListToStr(students);
                    if (file != null) {
                        saveTextToFile(text, file);
                    }
                }
        );


        cbxSortingAlgorithm.getItems().setAll(Arrays.asList(SortingAlgorithm.values()));
        // Create a tile pane
        TilePane tile_pane = new TilePane(cbxSortingAlgorithm);

        Group root = new Group();

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(65, 12, 15, 72));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");
        hbox.getChildren().addAll(numberOfStudentsText, amountOfTimeSpentText, buttonSort, buttonSelectFile, tile_pane);
        root.getChildren().add(hbox);

        Scene scene = new Scene(root, 600, 300);
        stage.setTitle("Sorting students application");
        stage.setScene(scene);
        stage.show();
    }

    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}