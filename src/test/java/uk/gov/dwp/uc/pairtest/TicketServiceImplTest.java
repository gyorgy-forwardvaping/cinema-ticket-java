package uk.gov.dwp.uc.pairtest;

import org.junit.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImplTest {
       
	@Test(expected = InvalidPurchaseException.class)
	public void purchseRequestWithoutTickets() {
            TicketServiceImpl ticketService = new TicketServiceImpl();
            long userID = 100;
            ticketService.purchaseTickets(userID);

	}
        
        @Test(expected = InvalidPurchaseException.class)
	public void purchseRequestWithoutAdult() {
            TicketServiceImpl ticketService = new TicketServiceImpl();
            long userID = 100;
            ticketService.purchaseTickets(userID, new TicketTypeRequest(TicketTypeRequest.Type.INFANT,4), new TicketTypeRequest(TicketTypeRequest.Type.CHILD,4));
	}
        
        @Test(expected = InvalidPurchaseException.class)
        public void requestingMoreTicketThanTheMaximum() {
            TicketServiceImpl ticketService = new TicketServiceImpl();
            long userID = 100;
            ticketService.purchaseTickets(userID, new TicketTypeRequest(TicketTypeRequest.Type.ADULT,10), new TicketTypeRequest(TicketTypeRequest.Type.INFANT,5), new TicketTypeRequest(TicketTypeRequest.Type.CHILD,10));
        }
        
        @Test(expected = InvalidPurchaseException.class)
        public void requestingMoreInfantTicketThanAdultOne() {
            TicketServiceImpl ticketService = new TicketServiceImpl();
            long userID = 100;
            ticketService.purchaseTickets(userID, new TicketTypeRequest(TicketTypeRequest.Type.ADULT,5), new TicketTypeRequest(TicketTypeRequest.Type.INFANT,7));
        }
        
        /**
         * successful request tests
         */
        
        @Test
        public void successfulTicketPurchaseJustAdult(){
            TicketServiceImpl ticketService = new TicketServiceImpl();
            long userID = 100;
            ticketService.purchaseTickets(userID, new TicketTypeRequest(TicketTypeRequest.Type.ADULT,15));
        }
        
        @Test
        public void successfulTicketPurchaseAdultAndChild(){
            TicketServiceImpl ticketService = new TicketServiceImpl();
            long userID = 100;
            ticketService.purchaseTickets(userID, new TicketTypeRequest(TicketTypeRequest.Type.ADULT,15), new TicketTypeRequest(TicketTypeRequest.Type.CHILD,5));
        }
        
        @Test
        public void successfulTicketPurchaseAdultChildAndInfant(){
            TicketServiceImpl ticketService = new TicketServiceImpl();
            long userID = 100;
            ticketService.purchaseTickets(userID, new TicketTypeRequest(TicketTypeRequest.Type.ADULT,12), new TicketTypeRequest(TicketTypeRequest.Type.CHILD,5), new TicketTypeRequest(TicketTypeRequest.Type.INFANT,3));
        }
}
