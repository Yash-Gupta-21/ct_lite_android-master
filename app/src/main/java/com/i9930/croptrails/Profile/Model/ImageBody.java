package com.i9930.croptrails.Profile.Model;

public class ImageBody {
    String image,person_id,comp_id,user_id,token;
    public ImageBody(String image, String person_id, String comp_id, String user_id, String token) {
        this.image = image;
        this.person_id = person_id;
        this.comp_id = comp_id;
        this.user_id = user_id;
        this.token = token;
    }
}
