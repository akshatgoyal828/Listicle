package com.example.trackandtrigger;

/* For toDoFragment */

public class Collect {
    private String title;
    private int image_id;
    public static String URL = "https://i.ibb.co/cLkzFTJ/ic-flipkart.png";

    public Collect() {
        //empty constructor needed
    }

    public Collect(String title, int image_id) {
        this.title = title;
        this.image_id = image_id;
    }

    public static String findURL(int image_id) {
        String url="";
        switch (image_id){
            case 1: url="https://i.ibb.co/cLkzFTJ/ic-flipkart.png"; break;
            case 2: url="https://i.ibb.co/bNSPC3d/UPI-Logo-PNG.png"; break;
            case 3: url="https://i.ibb.co/sb50kzW/Paypal-Icon-979x1024.png"; break;
            case 4: url="https://i.ibb.co/mTPRmM3/SBI-Bank-PNG-Icon-1024x1024.png"; break;
            case 5: url="https://i.ibb.co/5x4LFVs/Axis-Bank-PNG-Logo-1024x1024.png"; break;
            default: url="https://i.ibb.co/B3469zs/no-image-found-360x260.png";
        }
        return url;
    }


    public String getTitle() {
        return title;
    }

    public int getImage_id() {
        return image_id;
    }
}