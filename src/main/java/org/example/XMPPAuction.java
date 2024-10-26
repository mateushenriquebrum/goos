package org.example;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import static java.lang.String.format;
import static org.example.Main.BID_COMMAND_FORMAT;
import static org.example.Main.JOIN_COMMAND_FORMAT;

public class XMPPAuction implements Auction{
    private final Chat chat;

    public XMPPAuction(Chat chat) {
        this.chat = chat;
    }

    @Override
    public void bid(int amount) {
        sendMessage(format(BID_COMMAND_FORMAT, amount));
    }

    @Override
    public void join() {
        sendMessage(JOIN_COMMAND_FORMAT);
    }

    private void sendMessage(String message){
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
