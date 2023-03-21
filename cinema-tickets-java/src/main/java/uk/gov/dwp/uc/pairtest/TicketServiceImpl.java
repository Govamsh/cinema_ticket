package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    public final String INVALID_ACCOUNT_ID = "Invalid Account ID";
    public final String SEAT_COUNT_EXCEEDED = "Seat count exceeded";
    public final String NO_ADULT = "No Adult";
    /**
     * Should only have private methods other than the one below.
     */

    TicketPaymentService ticketPaymentService = new TicketPaymentServiceImpl();
    SeatReservationService seatReservationService = new SeatReservationServiceImpl();

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        if (accountId <= 0) {
            System.out.println(INVALID_ACCOUNT_ID);
            throw new InvalidPurchaseException();
        }


        int totalSeats = 0;
        boolean atleastOneAdult = false;
        int price = 0;
        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {
            price += getTicketPrice(ticketTypeRequest.getTicketType());

            if (ticketTypeRequest.getTicketType() == TicketTypeRequest.Type.ADULT
                    || ticketTypeRequest.getTicketType() == TicketTypeRequest.Type.CHILD) {
                if (ticketTypeRequest.getTicketType() == TicketTypeRequest.Type.ADULT)
                    atleastOneAdult = true;
                totalSeats += ticketTypeRequest.getNoOfTickets();
            }
        }
        if (!atleastOneAdult) {
            System.out.println(NO_ADULT);
            throw new InvalidPurchaseException();
        }

        if (totalSeats >= 20) {
            System.out.println(SEAT_COUNT_EXCEEDED);
            throw new InvalidPurchaseException();
        }

        ticketPaymentService.makePayment(accountId, price);
        seatReservationService.reserveSeat(accountId, totalSeats);
    }

    private Integer getTicketPrice(TicketTypeRequest.Type type) {
        return switch (type) {
            case ADULT -> 20;
            case CHILD -> 10;
            default -> 0;
        };
    }

}
