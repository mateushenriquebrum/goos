import org.example.*;
import org.hamcrest.FeatureMatcher;
import static org.hamcrest.Matchers.*;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.States;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.example.AuctionEventListener.PriceSource.FromOtherBidder;
import static org.example.AuctionEventListener.PriceSource.FromSniper;

public class AuctionSniperTest {
    @RegisterExtension
    public final JUnit5Mockery context = new JUnit5Mockery();
    private final SniperListenerAuction listener = context.mock(SniperListenerAuction.class);
    private final Auction auction = context.mock(Auction.class);
    private final States state = context.states("sniper");
    private final String ITEM_ID = "item-54321";

    private final AuctionSniper sniper = new AuctionSniper(ITEM_ID, auction, listener);
    @Test
    public void reportsLostWhenAuctionClosesImmediately() {
        context.checking(new Expectations() {{
            one(listener).sniperLost();
        }});

        sniper.auctionClosed();
    }

    @Test
    public void reportsWonIfAuctionClosesWhenWinning() {
        context.checking(new Expectations() {{
            ignoring(auction);
            allowing(listener).sniperWinning();
            then(state.is("winning"));

            atLeast(1).of(listener).sniperWon();
            when(state.is("winning"));
        }});
        sniper.currentPrice(123, 45, FromSniper);
        sniper.auctionClosed();
    }

    @Test
    public void reportsLostIfAuctionClosesWhenBidding() {
        context.checking(new Expectations() {{
            ignoring(auction);
            allowing(listener).sniperStateChanged(with(aSniperThatIs(SniperSnapshot.SniperState.BIDDING))); then(state.is("binding"));
            then(state.is("bidding"));
            atLeast(1).of(listener).sniperLost();
            when(state.is("bidding"));
        }});

        sniper.currentPrice(123, 45, FromOtherBidder);
        sniper.auctionClosed();
    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;
        final int bid = price + increment;

        context.checking(new Expectations() {{
            one(auction).bid(bid);
            atLeast(1).of(listener).sniperStateChanged(new SniperSnapshot(ITEM_ID, price, bid, SniperSnapshot.SniperState.BIDDING));
        }});

        sniper.currentPrice(price, increment, FromOtherBidder);
    }

    @Test
    public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
        context.checking(new Expectations() {{
            atLeast(1).of(listener).sniperWinning();
        }});
        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromSniper);
    }

    private Matcher<SniperSnapshot> aSniperThatIs(final SniperSnapshot.SniperState state) {
        return new FeatureMatcher<>(equalTo(state), "sniper that is", "was") {

            @Override
            protected SniperSnapshot.SniperState featureValueOf(SniperSnapshot sniperSnapshot) {
                return sniperSnapshot.state();
            }
        };
    }

}
