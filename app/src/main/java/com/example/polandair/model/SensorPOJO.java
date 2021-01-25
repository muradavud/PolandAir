package com.example.polandair.model;

import androidx.room.Embedded;

public class SensorPOJO {

    private int id;
    private int stationId;
    @Embedded
    private Param param;


    public SensorPOJO() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public Param getParam() {
        return param;
    }

    public void setParam(Param param) {
        this.param = param;
    }


    public static class Param {

        public Param() {}


        private String paramFormula;
        private String paramCode;
        private int idParam;
        private String paramName;

        public String getParamName() {
            return paramName;
        }

        public void setParamName(String paramName) {
            this.paramName = paramName;
        }

        public String getParamFormula() {
            return paramFormula;
        }

        public void setParamFormula(String paramFormula) {
            this.paramFormula = paramFormula;
        }

        public String getParamCode() {
            return paramCode;
        }

        public void setParamCode(String paramCode) {
            this.paramCode = paramCode;
        }

        public int getIdParam() {
            return idParam;
        }

        public void setIdParam(int idParam) {
            this.idParam = idParam;
        }

    }
}

