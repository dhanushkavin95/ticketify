import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/seatBooking")
public class SeatBookingSystem extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Initialize the seat booking data
    private Map<String, Boolean> seats = new HashMap<>();

    @Override
    public void init() throws ServletException {
        // Initialize seats (A1 to J15)
        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        for (String row : rows) {
            for (int i = 1; i <= 15; i++) {
                seats.put(row + i, false); // false indicates available
            }
        }
    }

    // Handle GET requests (Render the HTML interface)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Embed HTML/CSS/JavaScript in the servlet response
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Enhanced Seat Booking System</title>");
        out.println("<style>");
        out.println("/* Your existing styles */");
        out.println("body { font-family: Arial, sans-serif; background-color: #282c34; color: white; text-align: center; padding: 20px; }");
        out.println(".seat { display: inline-block; width: 30px; height: 30px; background-color: green; margin: 5px; cursor: pointer; }");
        out.println(".seat.occupied { background-color: gray; cursor: not-allowed; }");
        out.println(".seat.selected { background-color: orange; }");
        out.println(".screen { width: 100%; height: 20px; background: black; margin: 20px auto; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Book Your Seats</h1>");
        out.println("<div class='screen'>Screen</div>");
        out.println("<div id='seat-container'>");

        // Render seats dynamically
        for (String row : seats.keySet()) {
            if (row.endsWith("1")) out.println("<div class='row'>");
            out.println("<div class='seat' id='" + row + "' " +
                    (seats.get(row) ? "class='occupied'" : "") +
                    " onclick='selectSeat(\"" + row + "\")'></div>");
            if (row.endsWith("15")) out.println("</div>");
        }

        out.println("</div>");
        out.println("<button onclick='bookSeats()'>Book Now</button>");
        out.println("<script>");
        out.println("let selectedSeats = [];");

        // JavaScript for selecting seats
        out.println("function selectSeat(seatId) {");
        out.println("   const seatElement = document.getElementById(seatId);");
        out.println("   if (seatElement.classList.contains('occupied')) return;");
        out.println("   if (seatElement.classList.contains('selected')) {");
        out.println("       seatElement.classList.remove('selected');");
        out.println("       selectedSeats = selectedSeats.filter(s => s !== seatId);");
        out.println("   } else {");
        out.println("       seatElement.classList.add('selected');");
        out.println("       selectedSeats.push(seatId);");
        out.println("   }");
        out.println("}");

        // JavaScript for booking seats
        out.println("function bookSeats() {");
        out.println("   if (selectedSeats.length === 0) {");
        out.println("       alert('Please select at least one seat.');");
        out.println("       return;");
        out.println("   }");
        out.println("   fetch('/seatBooking', {");
        out.println("       method: 'POST',");
        out.println("       headers: { 'Content-Type': 'application/json' },");
        out.println("       body: JSON.stringify({ seats: selectedSeats })");
        out.println("   }).then(res => res.json()).then(data => {");
        out.println("       if (data.success) {");
        out.println("           alert('Seats booked successfully!');");
        out.println("           window.location.reload();");
        out.println("       } else {");
        out.println("           alert('Failed to book seats!');");
        out.println("       }");
        out.println("   });");
        out.println("}");
        out.println("</script>");
        out.println("</body>");
        out.println("</html>");
    }

    // Handle POST requests (Book the selected seats)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Parse the JSON request
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        String json = jsonBuffer.toString();
        String[] selectedSeats = json.replace("{\"seats\":[", "").replace("]}", "").replace("\"", "").split(",");

        // Mark seats as occupied
        for (String seat : selectedSeats) {
            seats.put(seat, true);
        }

        // Send a JSON response
        response.setContentType("application/json");
        response.getWriter().write("{\"success\": true}");
    }
}
