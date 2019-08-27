package com.turner.whit.revstabletv2;

public class Car {

    private String id, make, model, year, thumburl, descr, country, gallery, fact1, fact2, fact3, fact4, fact5, fact6, fact7, fact8, articlesurl, dashurl, imagesurl, tcurl, aacurl, spare;

    //make station and gallery_seq integers so that sort by station works correctly
    private Integer station;
    private Integer gallery_seq;

    Car(String id, String make, String model, String year, String thumburl, String descr, String country, String gallery, Integer gallery_seq, Integer station, String fact1, String fact2, String fact3, String fact4, String fact5, String fact6, String fact7, String fact8, String articlesurl, String dashurl, String imagesurl, String tcurl, String aacurl, String spare) {

        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.thumburl = thumburl;
        this.descr = descr;
        this.country = country;
        this.gallery = gallery;
        this.gallery_seq = gallery_seq;
        this.station = station;
        this.fact1 = fact1;
        this.fact2 = fact2;
        this.fact3 = fact3;
        this.fact4 = fact4;
        this.fact5 = fact5;
        this.fact6 = fact6;
        this.fact7 = fact7;
        this.fact8 = fact8;
        this.articlesurl = articlesurl;
        this.dashurl = dashurl;
        this.imagesurl = imagesurl;
        this.tcurl = tcurl;
        this.aacurl = aacurl;
        this.spare = spare;

    }


    public String getId() {
        return id;
    }

    String getMake() {
        return make;
    }

    String getModel() {
        return model;
    }

    String getYear() {
        return year;
    }

    String getThumburl() {
        return thumburl;
    }

    String getDescr() {
        return descr;
    }

    public String getCountry() {
        return country;
    }

    public String getGallery() {
        return gallery;
    }

    Integer getGallery_seq() {
        return gallery_seq;
    }

    Integer getStation() {
        return station;
    }

    String getFact1() {
        return fact1;
    }

    String getFact2() {
        return fact2;
    }

    String getFact3() {
        return fact3;
    }

    String getFact4() {
        return fact4;
    }

    String getFact5() {
        return fact5;
    }

    String getFact6() {
        return fact6;
    }

    String getFact7() { return fact7; }

    String getFact8() { return fact8; }

    String getArticlesurl() {
        return articlesurl;
    }

    String getDashurl() {
        return dashurl;
    }

    String getImagesurl() {
        return imagesurl;
    }

    public String getTcurl() {
        return tcurl;
    }

    public String getAacurl() {
        return aacurl;
    }

    String getSpare() {
        return spare;
    }
}