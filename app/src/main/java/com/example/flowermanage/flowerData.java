package com.example.flowermanage;

public class flowerData {
    private String probability;
    private String commonName;
    private String scientificName;
    private String imageURL;
    private String detailData;

    public flowerData(String probability, String commonName, String scientificName, String imageURL, String detailData){
        this.probability = probability;
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.imageURL = imageURL;
        this.detailData = detailData;
    }

    public String getProbability()
    {
        return this.probability;
    }

    public String getCommonName()
    {
        return this.commonName;
    }

    public String getScientificName()
    {
        return this.scientificName;
    }

    public String getImageURL()
    {
        return this.imageURL;
    }

    public String getDetailData()
    {
        return this.detailData;
    }
}
