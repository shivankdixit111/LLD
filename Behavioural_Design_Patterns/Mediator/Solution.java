package Low_Level_Design.Behavioural_Design_Patterns.Mediator;
import java.util.ArrayList;
import java.util.List;

interface AuctionMediator {
    void registerBidder(Bidder bidder);
    void placeBid(Bidder bidder, int amount);
}

class AuctionHouse implements AuctionMediator {
    private List<Bidder> bidders;
    AuctionHouse() {
        this.bidders = new ArrayList<>();
    }
    @Override
    public void registerBidder(Bidder bidder) {
        bidders.add(bidder);
        bidder.setAuctionHouse(this);
    }

    @Override
    public void placeBid(Bidder bidder, int amount) {
        System.out.println(bidder.getName() + " placed a bid of amount " + amount);
        for(Bidder b: bidders) { 
            if(b != bidder) {
                b.receiveBid(bidder, amount);
            }
        } 
        System.out.println();
    }
}


class Bidder {
    private String name;
    private AuctionMediator auctionMediator; //association with AuctionMediator 
    Bidder(String name ) {
        this.name = name; 
    }

    public void placeBid(int amount) {
        auctionMediator.placeBid(this, amount);
    } 
    public void receiveBid(Bidder bidder, int amount) {
        System.out.println(name + " notified :] " + bidder.getName() + " placed a bid of amount " + amount);
    }
    public String getName() { return name; }
    public void setAuctionHouse(AuctionMediator auctionMediator) { this.auctionMediator = auctionMediator; }
}


public class Solution {
    public static void main(String[] args) {
        AuctionMediator auctionMediator = new AuctionHouse();
        Bidder bidder1 = new Bidder("Alice");
        Bidder bidder2 = new Bidder("Bob");
        Bidder bidder3 = new Bidder("John");


        auctionMediator.registerBidder(bidder1);
        auctionMediator.registerBidder(bidder2);
        auctionMediator.registerBidder(bidder3);


        bidder1.placeBid(100);
        bidder2.placeBid(150);
        bidder3.placeBid(200);
        

    }
}