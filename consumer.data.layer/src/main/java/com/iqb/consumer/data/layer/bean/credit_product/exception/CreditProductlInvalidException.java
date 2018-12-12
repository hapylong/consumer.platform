package com.iqb.consumer.data.layer.bean.credit_product.exception;

public class CreditProductlInvalidException extends Exception {
    private static final long serialVersionUID = 1L;
    private Reason reason;
    private Location location;
    private Layer layer;

    public static enum Reason {
        INVALID_REQUEST_PARAMS, DB_NOT_FOUND, INVALID_ENTITY, UNKNOW_TYPE, DB_ERROR, UNKNOWN_ERROR, REPEATED_INJECTION

    }

    public static enum Layer {
        CONTROLLER, SERVICE, MANAGER, REPOSITORY
    }

    public static enum Location {
        A, B, C, D, E
    }

    public CreditProductlInvalidException(Reason reason, Layer layer,
            Location location) {
        this.reason = reason;
        this.location = location;
        this.layer = layer;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
