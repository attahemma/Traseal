package com.pyropy.travelseal;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.auth.data.model.Resource;
import com.squareup.picasso.Picasso;


public class TravelDeal implements Parcelable {
    private String id;
    private String title;
    private String description;
    private String price;
    private String imageUrl;
    private String mImageName;

    public TravelDeal(){}

    public TravelDeal(String title, String description, String price, String imageUrl, String imageName) {
        this.setId(id);
        this.setTitle(title);
        this.setDescription(description);
        this.setPrice(price);
        this.setImageUrl(imageUrl);
        this.setImageName(imageName);
    }

    protected TravelDeal(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        price = in.readString();
        imageUrl = in.readString();
        mImageName = in.readString();
    }

    public static final Creator<TravelDeal> CREATOR = new Creator<TravelDeal>() {
        @Override
        public TravelDeal createFromParcel(Parcel in) {
            return new TravelDeal(in);
        }

        @Override
        public TravelDeal[] newArray(int size) {
            return new TravelDeal[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(price);
        parcel.writeString(imageUrl);
        parcel.writeString(mImageName);
    }


//    public static void loadImage(ImageView view, String imageUrl){
//        if (imageUrl==null)imageUrl="";
//        if (!imageUrl.isEmpty()) {
//            showImage(imageUrl,view);
//        }
//        else{
//
//            view.setBackgroundResource(android.R.drawable.gallery_thumb);
//        }
//
//    }

    public static void showImage(String url, ImageView view){
        int width = Resources.getSystem().getDisplayMetrics().widthPixels - 10;
        Picasso.get().load(url).resize(width, width*2/3).centerCrop().placeholder(android.R.drawable.gallery_thumb).into(view);
    }

    public String getImageName() {
        return mImageName;
    }

    public void setImageName(String imageName) {
        mImageName = imageName;
    }
}
