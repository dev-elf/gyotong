package com.likelion.mountainq.sleepkeeper.data;

/**
 * Created by dnay2 on 2017-05-26.
 */

public class GpsPoint {

    private double latitude;
    private double longitude;

    public GpsPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getVelocity(GpsPoint gpsPoint){
        return Math.sqrt((latitude - gpsPoint.getLatitude()) *(latitude - gpsPoint.getLatitude())
                - (longitude - gpsPoint.getLongitude()) * (longitude - gpsPoint.getLongitude()));
    }
}
