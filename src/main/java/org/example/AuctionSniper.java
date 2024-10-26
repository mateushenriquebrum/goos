package org.example;

import static org.example.AuctionEventListener.PriceSource.FromSniper;

public class AuctionSniper implements AuctionEventListener {
    private final String itemId;
    private final Auction auction;
    private final SniperListenerAuction listener;
    private boolean isWinning = false;

    public AuctionSniper(String itemId, Auction auction, SniperListenerAuction listener) {
        this.itemId = itemId;
        this.auction = auction;
        this.listener = listener;
    }

    @Override
    public void auctionClosed() {
        if (isWinning) {
            listener.sniperWon();
        } else {
            listener.sniperLost();
        }
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource bidder) {
        isWinning = bidder == FromSniper;
        if (isWinning) {
            listener.sniperWinning();
        } else {
            int bid = price + increment;
            auction.bid(bid);
            listener.sniperStateChanged(new SniperSnapshot(itemId, price, bid,  SniperSnapshot.SniperState.BIDDING));
        }
    }
}

