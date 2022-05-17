package com.example.infogames.model;

public class Review {
    private String id;
    private int type;
    private int materialId;
    private int evaluation;

    public Review()  {}
    public Review(String id, int type, int materialId, int evaluation) {
        this.id = id;
        this.type = type;
        this.materialId = materialId;
        this.evaluation = evaluation;
    }

    public Review(int type, int materialId, int evaluation) {
        this.type = type;
        this.materialId = materialId;
        this.evaluation = evaluation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }
}
