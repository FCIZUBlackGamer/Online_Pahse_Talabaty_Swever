package talabaty.swever.com.online;

import android.net.Uri;

import java.io.Serializable;

public class ImageSource implements Serializable {
    Uri Photo2;
    int Id ;
    String Photo;
    int SampleProductId ;


    public ImageSource(String photo) {
        Photo = photo;
    }

    public ImageSource(int id, String photo, int sampleProductId) {
        Id = id;
        Photo = photo;
        SampleProductId = sampleProductId;
    }

    public int getId() {
        return Id;
    }

    public int getSampleProductId() {
        return SampleProductId;
    }

    public Uri getPhoto2() {
        return Photo2;
    }

    public String getPhoto() {
        return Photo;
    }

    public ImageSource(Uri source) {
        this.Photo2 = source;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setSampleProductId(int sampleProductId) {
        SampleProductId = sampleProductId;
    }

    public Uri getSource() {
        return Photo2;
    }
}
