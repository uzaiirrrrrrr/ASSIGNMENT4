package com.example.assignment4;

public class ParkingSlot {
    private int id;
    private String slotName;
    private String location;
    private boolean isOccupied;
    private double pricePerHour;

    public ParkingSlot(int id, String slotName, String location, boolean isOccupied, double pricePerHour) {
        this.id = id;
        this.slotName = slotName;
        this.location = location;
        this.isOccupied = isOccupied;
        this.pricePerHour = pricePerHour;
    }

    public ParkingSlot(String slotName, String location, boolean isOccupied, double pricePerHour) {
        this.slotName = slotName;
        this.location = location;
        this.isOccupied = isOccupied;
        this.pricePerHour = pricePerHour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    // For parsing from generic generic API if needed
    // In real scenario we map JSON fields to these
}
