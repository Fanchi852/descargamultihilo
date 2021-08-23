import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import util.R;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppController {

    public TextField field_text_link, field_text_download_path, field_text_file_name;
    public Button btDownload;
    public VBox panel;
    public TextArea text_area_log = new TextArea();

    @FXML
    public void loadHistory(ActionEvent event) {
        text_area_log.setText(readlog());
    }

    private String readlog() {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("download.log")))
        {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
            {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    @FXML
    public void addDownload(ActionEvent event) {
System.out.println("appcontroller");
        if (field_text_link.getText().equals(null) || field_text_link.getText().equals("")){
            System.out.println("a");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            System.out.println("b");
            alert.setContentText("no has introducido un link de descarga");
            System.out.println("c");
            alert.show();
        }else {
            System.out.println("d");
            Map<String, String> linkAndPath = new HashMap<>();
            System.out.println("e");
            linkAndPath.put("link", field_text_link.getText());
            System.out.println("f");
            linkAndPath.put("path", (field_text_download_path.getText()+field_text_file_name.getText()));
            System.out.println("g");

            try {
                System.out.println("link: "+linkAndPath.get("link")+" mas path: "+linkAndPath.get("path"));
                System.out.println("h");
                FXMLLoader loader = new FXMLLoader();
                System.out.println("i");
                loader.setLocation(R.getUI("descarga.fxml"));
                System.out.println("j");
                DownloadController downloadController = new DownloadController(linkAndPath);
                System.out.println("k");
                loader.setController(downloadController);
                System.out.println("l");
                VBox downloadControl = loader.load();
                System.out.println("m");

                panel.getChildren().add(downloadControl);
                System.out.println("n");
            } catch (Exception creationError) {
                creationError.printStackTrace();
                System.out.println(creationError);
            }
        }
    }
}