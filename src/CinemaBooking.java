import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

class Booking {
    private String seatNumber;
    private String dateTime;

    public Booking(String seatNumber, String dateTime) {
        this.seatNumber = seatNumber;
        this.dateTime = dateTime;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "Seat " + seatNumber + ", Booked on: " + dateTime;
    }
}

class Hall {
    private String[][] seats;
    private int rows, cols;
    private ArrayList<Booking> bookingHistory;

    // Constructor to set up the cinema
    public Hall(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.seats = new String[rows][cols];
        this.bookingHistory = new ArrayList<>();
        setupCinema();
        displaySeats();
    }

    // Set up the cinema with available seats
    public void setupCinema() {
        char seatChar = 'A';
        String status = "AV";

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                seats[i][j] = seatChar + "-" + (i + 1) + ": " + status;
                seatChar++;
                if (seatChar > 'Z') seatChar = 'A';
            }
        }
    }

    // Display the cinema layout
    public void displaySeats() {
        System.out.println("\nCinema Layout:");
        System.out.println("AV = Available, BK = Booked");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(seats[i][j] + "  ");
            }
            System.out.println();
        }
    }

    // Book a seat
    public void bookSeat() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter seat to book (e.g., a-1,b-1): ");
        String seatNumbersInput = scanner.nextLine().toLowerCase();
        String[] seatNumbers = seatNumbersInput.split(",");

        for (String seat : seatNumbers) {
            String[] parts = seat.split("-");
            String rowCheck = parts[0];
            String colCheck = parts[1];

            int row = rowCheck.charAt(0) - 'a';
            int col = Integer.parseInt(colCheck) - 1;

            if (isValidSeat(row, col)) {
                if (seats[row][col].endsWith("AV")) {
                    seats[row][col] = rowCheck + "-" + (col + 1) + ": BK";
                    String seatNumber = rowCheck + "-" + (col + 1);
                    String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    bookingHistory.add(new Booking(seatNumber, dateTime));
                    System.out.println("Seat " + seatNumber + " booked successfully!");
                } else {
                    System.out.println("Seat " + seat + " is already booked.");
                }
            } else {
                System.out.println("Invalid seat selection: " + seat);
            }
        }
    }

    // Cancel a booking
    public void cancelBooking() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter row number (1-" + rows + "): ");
        int row = scanner.nextInt() - 1;
        System.out.print("Enter column letter (A-" + (char) ('A' + cols - 1) + "): ");
        char colChar = scanner.next().toUpperCase().charAt(0);
        int col = colChar - 'A';

        if (isValidSeat(row, col)) {
            if (seats[row][col].endsWith("BK")) {
                seats[row][col] = colChar + "-" + (row + 1) + ": AV";
                String seatNumber = colChar + "-" + (row + 1);
                bookingHistory.removeIf(booking -> booking.getSeatNumber().equals(seatNumber));

                System.out.println("Booking for seat " + seatNumber + " cancelled successfully!");
            } else {
                System.out.println("Seat is not booked.");
            }
        } else {
            System.out.println("Invalid seat selection.");
        }
    }

    // View booking history along with current cinema layout
    public void viewBookingHistory() {
        System.out.println("\nCurrent Cinema Layout with Booking History:");
        if (bookingHistory.isEmpty()) {
            System.out.println("No bookings made yet.");
        } else {
            // Display the current layout with the booking status
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (seats[i][j].endsWith("BK")) {
                        System.out.print(seats[i][j] + "  ");
                    } else {
                        System.out.print(seats[i][j] + "  ");
                    }
                }
                System.out.println();
            }

            // Display booking history
            System.out.println("\nBooking History:");
            for (Booking booking : bookingHistory) {
                System.out.println(booking);
            }
            System.out.println("Total booked seats: " + bookingHistory.size());
        }
    }

    // Validate if the seat selection is valid
    private boolean isValidSeat(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }
}

public class CinemaBooking {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hall hall = null;

        while (true) {
            System.out.println("\n--- Cinema Booking System ---");
            System.out.println("1. Set up Cinema");
            System.out.println("2. Book");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View Booking History");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    if(hall == null) {
                        System.out.print("Enter number of rows: ");
                        int rows = scanner.nextInt();
                        System.out.print("Enter number of columns: ");
                        int cols = scanner.nextInt();
                        hall = new Hall(rows, cols);
                    } else {
                        System.out.println("Hall is already exists.");
                    }
                    break;
                case 2:
                    if (hall != null) hall.bookSeat();
                    else System.out.println("Set up the cinema first.");
                    break;
                case 3:
                    if (hall != null) hall.cancelBooking();
                    else System.out.println("Set up the cinema first.");
                    break;
                case 4:
                    if (hall != null) hall.viewBookingHistory();
                    else System.out.println("Set up the cinema first.");
                    break;
                case 5:
                    System.out.println("Exiting system...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
