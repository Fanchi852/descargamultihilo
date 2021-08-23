import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.Map;

public class DownloadController{

    final static Logger logger = Logger.getLogger(DownloadController.class);

    public VBox download_view_panel;
    public Label lb_size = new Label();
    public Label download_percentage = new Label();
    public Label file_name = new Label();


    public ProgressBar dowload_progres_bar = new ProgressBar();

    private String link;
    private String path;
    private DownloadTask DT;
    private Stage stage;
    private File file;



    public DownloadController(Map<String, String> linkAndPath) {
        System.out.println("entrada al constructor");
        this.link = linkAndPath.get("link");
        this.path = linkAndPath.get("path");
        this.file = new File(path);
        this.startDownload();
        System.out.println("salida constructor");
    }

    private void startDownload(){

        try{
            URL url = new URL(link);
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Selecciona donde almacenar el archivo");
            //if (path != null){
            System.out.println("0");
                fileChooser.setCurrentDirectory(file);
           // }

            /*
            no consigo hacer que se habra la ventana de guardado, me da error al pasarle el evento ventana y no consigo hacerlo funcionar

            File file = fileChooser.showSaveDialog(currentManager.getActiveWindow());

             */
            if (file == null) {
                System.out.println("cerrando cosa");
                return;
            }
            System.out.println("1");
            String namefile = "nombre: "+file.getName();
            System.out.println(namefile);
            file_name.setText(namefile);

            System.out.println("2");
            URLConnection urlConnection = url.openConnection();
            int fileSize = urlConnection.getContentLength();


            DT = new DownloadTask(file, url);
            DT.progressProperty().addListener((observableValue, oldValue, newValue) -> dowload_progres_bar.setProgress((double)newValue));
            DT.messageProperty().addListener((observableValue, oldValue, newValue) -> download_percentage.setText(newValue));
            DT.messageProperty().addListener((observableValue, oldValue, newValue) -> file_name.setText(namefile));
            DT.valueProperty().addListener((observableValue, oldValue, newValue) -> lb_size.setText(Integer.toString(newValue/1048576) + "MB de " + Integer.toString(fileSize/1048576) + " MB"));
            DT.stateProperty().addListener((observableValue, oldState, newState) -> {
                System.out.println(observableValue.toString());
                if (newState == Worker.State.SUCCEEDED) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("INFORMACION");
                    alert.setHeaderText("Descaga Completada");
                    alert.setContentText("La descarga ha terminado de manera satisfactoria, y a sifo almacenada en la direccion "+ file.getPath());
                    alert.show();
                    logger.info("exito al descargar, " + namefile + " en " + file.getPath() + " link: " + this.link);

                }
            });

            new Thread(DT).start();

        }catch (Exception error){
            System.out.println(error);
            logger.error("error en la descarga del archivo " + file.getName() + " link: " + this.link);
        }
    }

    @FXML
    public void cancelDownload(ActionEvent event) {

        try {

            DT.cancel();
            logger.warn("cancelada la descarga,  " + "nombre: " + file.getName() + " en " + file.getPath() + " link: " + this.link);

        }catch (Exception pauseError){
            System.out.println(pauseError);
            logger.error("error en la descarga del archivo " + file.getName() + " link: " + this.link);
        }
    }

    @FXML
    public void deleteDownload(ActionEvent event) {

        /*error al borrar elementos no consigo hacer funcionar el metodo remove*/

        DT.cancel();
        download_view_panel = (VBox) dowload_progres_bar.getParent().getParent();
        download_view_panel.getChildren().clear();
        download_view_panel.getChildren().remove(download_view_panel.getChildren());

    }
}
