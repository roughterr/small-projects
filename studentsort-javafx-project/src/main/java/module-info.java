module org.kk.studentproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens org.kk.studentproject to javafx.fxml;
    exports org.kk.studentproject;
    exports org.kk.studentproject.sort;
    opens org.kk.studentproject.sort to javafx.fxml;
}