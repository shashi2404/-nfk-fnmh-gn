import java.util.concurrent.locks.*;

class TicketBookingSystem {
    private boolean[] seats;
    private Lock lock;

    public TicketBookingSystem(int totalSeats) {
        seats = new boolean[totalSeats];
        lock = new ReentrantLock();
    }

    public void bookSeat(String customerType, int seatNumber) {
        lock.lock();
        try {
            if (seatNumber < 0 || seatNumber >= seats.length) {
                System.out.println(customerType + " Booking: Invalid seat number.");
                return;
            }
            if (!seats[seatNumber]) {
                seats[seatNumber] = true;
                System.out.println(customerType + " Booking: Seat " + (seatNumber + 1) + " confirmed.");
            } else {
                System.out.println("Error: Seat " + (seatNumber + 1) + " already booked.");
            }
        } finally {
            lock.unlock();
        }
    }
}

class Customer extends Thread {
    private TicketBookingSystem system;
    private int seatNumber;
    private String customerType;

    public Customer(TicketBookingSystem system, int seatNumber, String customerType, int priority) {
        this.system = system;
        this.seatNumber = seatNumber;
        this.customerType = customerType;
        setPriority(priority);
    }

    @Override
    public void run() {
        system.bookSeat(customerType, seatNumber);
    }
}

public class TicketBooking {
    public static void main(String[] args) {
        TicketBookingSystem system = new TicketBookingSystem(10);

        // Creating multiple users booking tickets
        Customer vip1 = new Customer(system, 0, "VIP", Thread.MAX_PRIORITY);
        Customer reg1 = new Customer(system, 1, "Regular", Thread.NORM_PRIORITY);
        Customer vip2 = new Customer(system, 0, "VIP", Thread.MAX_PRIORITY);
        Customer reg2 = new Customer(system, 1, "Regular", Thread.NORM_PRIORITY);
        Customer reg3 = new Customer(system, 2, "Regular", Thread.NORM_PRIORITY);

        vip1.start();
        reg1.start();
        vip2.start();
        reg2.start();
        reg3.start();
    }
}
