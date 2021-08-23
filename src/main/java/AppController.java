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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("no has introducido un link de descarga");
            alert.show();
        }else {
            Map<String, String> linkAndPath = new HashMap<>();
            linkAndPath.put("link", field_text_link.getText());
            linkAndPath.put("path", (field_text_download_path.getText()+field_text_file_name.getText()));

            try {
                System.out.println("link: "+linkAndPath.get("link")+" mas path: "+linkAndPath.get("path"));
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(R.getUI("descarga.fxml"));
                DownloadController downloadController = new DownloadController(linkAndPath);
                loader.setController(downloadController);
                VBox downloadControl = loader.load();

                panel.getChildren().add(downloadControl);
            } catch (Exception creationError) {
                creationError.printStackTrace();
                System.out.println(creationError);
            }
        }
    }
}