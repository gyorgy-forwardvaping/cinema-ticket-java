package uk.gov.dwp.uc.pairtest;


import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */
    
    private int calculatePrice(int price, int seats) {
        return price * seats;
    }
    
    private boolean makeReservation(long accountId, int totalPrice, int totalSeat) {
        if (totalPrice == 0) {
            throw new InvalidPurchaseException("Wrong Price calculation!");
        }
        
        if (totalSeat == 0) {
            throw new InvalidPurchaseException("Wrong Seat calculation!");
        }
        
        try {
            new TicketPaymentServiceImpl().makePayment(accountId, totalSeat);
            new SeatReservationServiceImpl().reserveSeat(accountId, totalSeat);
            
            return true;   
        } catch(Exception error) {
            throw new InvalidPurchaseException("Ticket purchase failed");
        }
    }
    
    private int totalTicketNumber = 20;
    private int adultTicketPrice = 20;
    private int childTicketPrice = 10;
    private int infantTicketPrice = 0;
    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        int adultTicketsNumber = 0;
        int childTicketsNumber = 0;
        int infantTicketsNumber = 0;
        int totalPrice = 0;
        int seats = 0;
        int tickets = 0;
        
        /**
         * if the ticketTypeRequests is empty then the purchase fail
         */
    	if (ticketTypeRequests.length == 0) {
            throw new InvalidPurchaseException("No request happened");
        }
        
        for (int i = 0; i < ticketTypeRequests.length; i++) {
            if (ticketTypeRequests[i].getTicketType() == TicketTypeRequest.Type.ADULT) {
                adultTicketsNumber += ticketTypeRequests[i].getNoOfTickets();
                totalPrice += this.calculatePrice(this.adultTicketPrice, adultTicketsNumber);
            } else if (ticketTypeRequests[i].getTicketType() == TicketTypeRequest.Type.CHILD) {
                childTicketsNumber += ticketTypeRequests[i].getNoOfTickets();
                totalPrice += this.calculatePrice(this.childTicketPrice, childTicketsNumber);
            } else if (ticketTypeRequests[i].getTicketType() == TicketTypeRequest.Type.INFANT) {
                infantTicketsNumber += ticketTypeRequests[i].getNoOfTickets();
                totalPrice += this.calculatePrice(this.infantTicketPrice, infantTicketsNumber);
            } else {
                throw new InvalidPurchaseException("Ticket Type not valid");
            }
        }
        
        seats = adultTicketsNumber + childTicketsNumber;
        tickets = seats + infantTicketsNumber;
        /**
         * If adult ticket is 0 then the purchase fail
         */
        if (adultTicketsNumber == 0) {
            throw new InvalidPurchaseException("Adult required for ticket purchase");
        }
        /**
         * one adult can hold one infant. If the number of infant greater than adult then the purchase fail
         */
        if (adultTicketsNumber < infantTicketsNumber) {
            throw new InvalidPurchaseException("Can't purchase more Infant ticket than Adult one");
        }
        
        /**
         * if the requested ticket number greater than totalTicketNumber then the purchase fail
         */
        if (this.totalTicketNumber < tickets) {
            throw new InvalidPurchaseException("Can't purchase more ticket than " + this.totalTicketNumber + " at one time!");
        }
        
        this.makeReservation(accountId, totalPrice, seats);
    }

}
