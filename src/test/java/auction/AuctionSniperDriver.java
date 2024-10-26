package auction;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;
import org.example.MainWindow;

import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.allOf;

public class AuctionSniperDriver extends JFrameDriver {
    public AuctionSniperDriver(int timeoutMiles) {
        super(new GesturePerformer(), JFrameDriver.topLevelFrame(named(MainWindow.MAIN_WINDOW_NAME), showingOnScreen()), new AWTEventQueueProber(timeoutMiles, 100));
    }

    public void showsSniperStatus(String statusText) {
        new JTableDriver(this).hasCell(withLabelText(statusText));
    }

    public void showsSniperStatus(String itemId, int lastPrice, int lastBid, String statusBidding) {
        new JTableDriver(this).hasRow(
                matching(withLabelText(itemId), withLabelText(valueOf(lastPrice)), withLabelText(valueOf(lastBid)), withLabelText(valueOf(statusBidding))));
    }

    public void showsSniperStatus(String itemId, int lastPrice, String statusWinning) {

    }
}
