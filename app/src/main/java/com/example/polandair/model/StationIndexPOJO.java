package com.example.polandair.model;


public class StationIndexPOJO {


    private int id;

    private IndexLevel stIndexLevel;
    private IndexLevel no2IndexLevel;
    private IndexLevel coIndexLevel;
    private IndexLevel pm10IndexLevel;
    private IndexLevel pm25IndexLevel;
    private IndexLevel c6h6IndexLevel;
    private IndexLevel o3IndexLevel;


    public StationIndexPOJO() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public IndexLevel getStIndexLevel() {
        return stIndexLevel;
    }

    public void setStIndexLevel(IndexLevel stIndexLevel) {
        this.stIndexLevel = stIndexLevel;
    }

    public IndexLevel getNo2IndexLevel() {
        return no2IndexLevel;
    }

    public void setNo2IndexLevel(IndexLevel no2IndexLevel) {
        this.no2IndexLevel = no2IndexLevel;
    }

    public IndexLevel getCoIndexLevel() {
        return coIndexLevel;
    }

    public void setCoIndexLevel(IndexLevel coIndexLevel) {
        this.coIndexLevel = coIndexLevel;
    }

    public IndexLevel getPm10IndexLevel() {
        return pm10IndexLevel;
    }

    public void setPm10IndexLevel(IndexLevel pm10IndexLevel) {
        this.pm10IndexLevel = pm10IndexLevel;
    }

    public IndexLevel getPm25IndexLevel() {
        return pm25IndexLevel;
    }

    public void setPm25IndexLevel(IndexLevel pm25IndexLevel) {
        this.pm25IndexLevel = pm25IndexLevel;
    }

    public IndexLevel getC6h6IndexLevel() {
        return c6h6IndexLevel;
    }

    public void setC6h6IndexLevel(IndexLevel c6h6IndexLevel) {
        this.c6h6IndexLevel = c6h6IndexLevel;
    }

    public IndexLevel getO3IndexLevel() {
        return o3IndexLevel;
    }

    public void setO3IndexLevel(IndexLevel o3IndexLevel) {
        this.o3IndexLevel = o3IndexLevel;
    }

    public static class IndexLevel {
        public IndexLevel() {}

        private int id;
        private String indexLevelName;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIndexLevelName() {
            return indexLevelName;
        }

        public void setIndexLevelName(String indexLevelName) {
            this.indexLevelName = indexLevelName;
        }

    }
}

