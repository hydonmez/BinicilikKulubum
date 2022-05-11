package com.huso.binicikulubum;

import java.io.Serializable;

public class EgitmenlerDizisi implements Serializable {
    private String advesoyadarray;
    private String surearray;
    private String cinsiyetarray;

    public EgitmenlerDizisi(String advesoyadarray, String surearray, String cinsiyetarray) {
        this.advesoyadarray = advesoyadarray;
        this.surearray = surearray;
        this.cinsiyetarray = cinsiyetarray;

    }

    public String getAdvesoyadarray() {
        return advesoyadarray;
    }

    public void setAdvesoyadarray(String advesoyadarray) {
        this.advesoyadarray = advesoyadarray;
    }

    public String getSurearray() {
        return surearray;
    }

    public void setSurearray(String surearray) {
        this.surearray = surearray;
    }

    public String getCinsiyetarray() {
        return cinsiyetarray;
    }

    public void setCinsiyetarray(String cinsiyetarray) {
        this.cinsiyetarray = cinsiyetarray;
    }

}
