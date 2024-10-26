package org.example;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_NAME = "org.example.Auction Sniper Main";
    public static final String SNIPER_STATUS_NAME = "sniper state";
    public static final String STATUS_JOINING = "Joining";
    public static final String STATUS_LOST = "Lost";
    public static final String STATUS_WON = "Won";
    public static final String STATUS_WINNING = "Winning";
    public static final String STATUS_BIDDING = "Bidding";
    private final JLabel sniperStatus = createLabel(STATUS_JOINING);
    private final SnipersTableModel snipers = new SnipersTableModel();

    public enum Column {
        ITEM_IDENTIFIER,
        LAST_PRICE,
        LAST_BID,
        SNIPER_STATUS;

        public static Column at(int offset) {
            return values()[offset];
        }
    }

    public MainWindow() {
        super("Action Sniper");
        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }

    private JTable makeSnipersTable() {
        final JTable sniperTable = new JTable(snipers);
        sniperTable.setName(SNIPER_STATUS_NAME);
        return sniperTable;
    }

    private void fillContentPane(JTable sniperTable) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(sniperTable), BorderLayout.CENTER);
    }

    public void showStatus(String status) {
        sniperStatus.setText(status);
    }

    private static JLabel createLabel(String initialText) {
        JLabel result = new JLabel(initialText);
        result.setName(SNIPER_STATUS_NAME);
        result.setBorder(new LineBorder(Color.BLACK));
        return result;
    }

    public void sniperStatusChanged(SniperSnapshot state, String text) {
        snipers.sniperStatusChanged(state, text);
    }


    public static class SnipersTableModel extends AbstractTableModel {
        private static String[] STATUS_TEXT = {
                MainWindow.STATUS_JOINING,
                MainWindow.STATUS_BIDDING
        };
        private String statusText = STATUS_JOINING;
        private final static SniperSnapshot STARTING_UP = new SniperSnapshot("",0,0, SniperSnapshot.SniperState.JOINING);
        private SniperSnapshot sniperSnapshot = STARTING_UP;
        @Override
        public int getRowCount() {
            return 1;
        }

        @Override
        public int getColumnCount() {
            return Column.values().length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return switch (Column.at(columnIndex)) {
                case ITEM_IDENTIFIER -> sniperSnapshot.itemId();
                case LAST_PRICE -> sniperSnapshot.lastPrice();
                case LAST_BID -> sniperSnapshot.lastBid();
                case SNIPER_STATUS -> statusText;
                case null -> throw new IllegalArgumentException("No column");
            };
        }
        public void sniperStatusChanged(SniperSnapshot newSniperSnapshot, String newStatusText) {
            sniperSnapshot = newSniperSnapshot;
            statusText = STATUS_TEXT[sniperSnapshot.state().ordinal()];
            fireTableRowsUpdated(0, 0);
        }
    }
}
