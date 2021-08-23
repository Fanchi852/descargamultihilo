import javafx.concurrent.Task;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadTask extends Task<Integer> {

    private URL URL;
    private File File;
    private Boolean pauseFlag = false;

    public DownloadTask(File file, URL url) {

        this.File = file;
        this.URL = url;

    }

    @Override
    protected Integer call() throws Exception {

        byte dataBuffer[] = new byte[1024];
        int bytesRead;
        int totalRead = 0;
        double downloadProgress = 0;


        URLConnection urlConnection = URL.openConnection();
        double fileSize = urlConnection.getContentLength();

        BufferedInputStream bufferedInputStream = new BufferedInputStream(URL.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(File);

        System.out.println("entrada del while 1 "+pauseFlag);
        while ((bytesRead = bufferedInputStream.read(dataBuffer, 0, 1024)) != -1) {

            downloadProgress = ((double) totalRead / fileSize);
            //System.out.println(downloadProgress);
            updateProgress(downloadProgress, 1);
            updateMessage( Math.ceil(downloadProgress * 100) + " %");

            fileOutputStream.write(dataBuffer, 0, bytesRead);
            totalRead += bytesRead;
            updateValue(totalRead);
            if (isCancelled()) {
                return null;
            }
        }

        updateProgress(1, 1);

        fileOutputStream.close();
        return null;
    }
/*
    public void pause(){
    }
    public void resume(){
    }
*/
}
