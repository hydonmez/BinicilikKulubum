package com.huso.binicikulubum;

import java.io.Serializable;

public class rezarvasyonlardizisi implements Serializable {
    private String baslangicdatearray;
    private String bitisdatearray;
    private String saatarray;
    private String basvurankisiarray;
    private String basvurulanhocaarray;
    private String rezarvasyonidarray;

    public rezarvasyonlardizisi(String baslangicdatearray, String bitisdatearray, String saatarray, String basvurankisiarray, String basvurulanhocaarray, String rezarvasyonidarray) {
        this.baslangicdatearray = baslangicdatearray;
        this.bitisdatearray = bitisdatearray;
        this.saatarray = saatarray;
        this.basvurankisiarray = basvurankisiarray;
        this.basvurulanhocaarray = basvurulanhocaarray;
        this.rezarvasyonidarray = rezarvasyonidarray;
    }

    public String getBaslangicdatearray() {
        return baslangicdatearray;
    }

    public void setBaslangicdatearray(String baslangicdatearray) {
        this.baslangicdatearray = baslangicdatearray;
    }

    public String getBitisdatearray() {
        return bitisdatearray;
    }

    public void setBitisdatearray(String bitisdatearray) {
        this.bitisdatearray = bitisdatearray;
    }

    public String getSaatarray() {
        return saatarray;
    }

    public void setSaatarray(String saatarray) {
        this.saatarray = saatarray;
    }

    public String getBasvurankisiarray() {
        return basvurankisiarray;
    }

    public void setBasvurankisiarray(String basvurankisiarray) {
        this.basvurankisiarray = basvurankisiarray;
    }

    public String getBasvurulanhocaarray() {
        return basvurulanhocaarray;
    }

    public void setBasvurulanhocaarray(String basvurulanhocaarray) {
        this.basvurulanhocaarray = basvurulanhocaarray;
    }

    public String getRezarvasyonidarray() {
        return rezarvasyonidarray;
    }

    public void setRezarvasyonidarray(String rezarvasyonidarray) {
        this.rezarvasyonidarray = rezarvasyonidarray;
    }
}
