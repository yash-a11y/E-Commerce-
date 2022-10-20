package com.example.e_commarce;

public class category_datamodel {

    private String name;
    private String catid;
    private String img;


    public void setImg(String img) {
        this.img = img;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getCatid() {
        return catid;
    }
}
