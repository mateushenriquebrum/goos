package org.example;

public record SniperSnapshot(String itemId, int lastPrice, int lastBid, SniperState state) {
    public enum SniperState {
        JOINING,
        BIDDING,
        WINNING,
        LOST,
        WON;
    }
}