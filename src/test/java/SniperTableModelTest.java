import org.example.MainWindow;
import org.example.SniperSnapshot;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static org.example.MainWindow.Column.values;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SniperTableModelTest {
    @RegisterExtension
    private final JUnit5Mockery context = new JUnit5Mockery();
    private TableModelListener listener = context.mock(TableModelListener.class);
    private final MainWindow.SnipersTableModel model = new MainWindow.SnipersTableModel();

    @BeforeEach public void attachModelListener() {
        model.addTableModelListener(listener);
    }

    @Test public void hasEnoughColumns() {
        assertEquals(model.getColumnCount(), values().length);
    }

    @Test public void setsSniperValuesInColumns() {
        context.checking(new Expectations() {{
            oneOf(listener).tableChanged(with(aRowChangedEvent()));
        }});

        model.sniperStatusChanged(new SniperSnapshot("item id", 555, 666, SniperSnapshot.SniperState.JOINING), MainWindow.STATUS_BIDDING);

        assertColumnEquals(MainWindow.Column.ITEM_IDENTIFIER, "item id");
        assertColumnEquals(MainWindow.Column.LAST_PRICE, 555);
        assertColumnEquals(MainWindow.Column.LAST_BID, 666);
        assertColumnEquals(MainWindow.Column.SNIPER_STATUS, MainWindow.STATUS_BIDDING);
    }

    private void assertColumnEquals(MainWindow.Column column, Object expected) {
        final int rowIndex = 0;
        final int columIndex = column.ordinal();
        assertEquals(expected, model.getValueAt(rowIndex, columIndex));
    }

    private Matcher<TableModelEvent> aRowChangedEvent() {
        return samePropertyValuesAs(new TableModelEvent(model, 0));
    }
}

