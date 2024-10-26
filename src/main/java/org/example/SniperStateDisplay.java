package org.example;

import javax.swing.*;

class SniperStateDisplay implements SniperListenerAuction {

    private final Main main;

    public SniperStateDisplay(Main main) {
        this.main = main;
    }

    @Override
    public void sniperLost() {
        showStatus(MainWindow.STATUS_LOST);
    }

    @Override
    public void sniperWinning() {
        showStatus(MainWindow.STATUS_WINNING);
    }

    @Override
    public void sniperWon() {
        showStatus(MainWindow.STATUS_WON);
    }

    @Override
    public void sniperStateChanged(SniperSnapshot state) {
        SwingUtilities.invokeLater(() -> main.ui.sniperStatusChanged(state, MainWindow.STATUS_BIDDING));
    }

    private void showStatus(String status) {
        SwingUtilities.invokeLater(() -> main.ui.showStatus(status));
    }
}
