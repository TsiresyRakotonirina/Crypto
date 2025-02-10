package com.example.crypto.model;

import com.example.crypto.utils.Column;
import com.example.crypto.utils.Generalisation;

import java.sql.Connection;
import java.util.Vector;

public class Commission {
    @Column
    String type;
    @Column
    double pourcentage;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPourcentage() {
        return pourcentage;
    }

    public Commission() {
    }

    public Commission(String type, double pourcentage) {
        this.type = type;
        this.pourcentage = pourcentage;
    }

    public void setPourcentage(double pourcentage) {
        this.pourcentage = pourcentage;
    }
    public void update(Connection con, Object obj, String type)throws Exception{
        Generalisation.update(con, obj, " type='"+type+"'");
    }
    public Vector<Commission> find(Connection con,String type) throws Exception{
        return Generalisation.select(con,"where type='"+type+"'", Commission.class);
    }
    public Vector<Commission> all(Connection con) throws Exception{
        return Generalisation.select(con," ", Commission.class);
    }
}
