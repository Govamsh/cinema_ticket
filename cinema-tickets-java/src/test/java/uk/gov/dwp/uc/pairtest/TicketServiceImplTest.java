package uk.gov.dwp.uc.pairtest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceImplTest {

    public final Long INVALID_ACCOUNT_ID = -1L;
    public final Long VALID_ACCOUNT_ID = 1L;
    @InjectMocks
    TicketServiceImpl ticketService;

    @Test(expected = InvalidPurchaseException.class)
    public void purchaseTicketsWithInvalidAccountId() {
        ticketService.purchaseTickets(INVALID_ACCOUNT_ID, getValidAdultTicketRequest());
    }

    @Test(expected = InvalidPurchaseException.class)
    public void purchaseTicketsWithOutOneAdultAndChildrenAlone() {
        ticketService.purchaseTickets(VALID_ACCOUNT_ID, getValidChildTicketRequest());
    }

    @Test(expected = InvalidPurchaseException.class)
    public void purchaseTicketsWithOutOneAdultAndInfantsAlone() {
        ticketService.purchaseTickets(VALID_ACCOUNT_ID, getValidInfantTicketRequest());
    }

    @Test(expected = InvalidPurchaseException.class)
    public void purchaseTicketsWithMoreThen20Seats() {
        ticketService.purchaseTickets(VALID_ACCOUNT_ID, getInValidAdultTicketRequest());
    }

    @Test
    public void purchaseTickets() {
        ticketService.purchaseTickets(VALID_ACCOUNT_ID, getValidAdultTicketRequest());
    }

    private TicketTypeRequest getValidAdultTicketRequest() {
        return new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 10);
    }

    private TicketTypeRequest getInValidAdultTicketRequest() {
        return new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 50);
    }

    private TicketTypeRequest getValidChildTicketRequest() {
        return new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 5);
    }

    private TicketTypeRequest getValidInfantTicketRequest() {
        return new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5);
    }
}