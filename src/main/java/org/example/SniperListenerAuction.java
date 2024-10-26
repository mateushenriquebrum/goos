package org.example;

public interface SniperListenerAuction {
    void sniperLost();

    void sniperWinning();

    void sniperWon();

    void sniperStateChanged(SniperSnapshot status);
}
