package model;

public class Ticket {
    private int ticketID;
    private int flightID;
    private int userID;
    private String seatNumber;
    private boolean isCheckedIn;

    public Ticket() {}

    public Ticket(int ticketID, int flightID, int userID, String seatNumber, boolean isCheckedIn) {
        this.ticketID = ticketID;
        this.flightID = flightID;
        this.userID = userID;
        this.seatNumber = seatNumber;
        this.isCheckedIn = isCheckedIn;
    }

    public int getTicketID() { return ticketID; }
    public void setTicketID(int ticketID) { this.ticketID = ticketID; }

    public int getFlightID() { return flightID; }
    public void setFlightID(int flightID) { this.flightID = flightID; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public boolean isCheckedIn() { return isCheckedIn; }
    public void setCheckedIn(boolean isCheckedIn) { this.isCheckedIn = isCheckedIn; }
}
