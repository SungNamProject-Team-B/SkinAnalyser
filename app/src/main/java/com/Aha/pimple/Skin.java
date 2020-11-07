package com.Aha.pimple;

import java.io.Serializable;

public class Skin implements Serializable {



    String SkinPictureName;
    String SkinPicturePath;

    public Skin(String skinPictureName, String skinPicturePath){
        SkinPictureName = skinPictureName;
        SkinPicturePath = skinPicturePath;
    }

    public String getSkinPictureName(){
        return SkinPictureName;
    }

    public void setSkinPictureName(String skinPictureName){
        SkinPictureName = skinPictureName;
    }

    public String getSkinPicturePath(){
        return SkinPicturePath;
    }

    public void setSkinPicturePath(String skinPicturePath) {
        SkinPicturePath = skinPicturePath;
    }
}
