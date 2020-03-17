import java.sql.*;
import java.util.*;

/**
 * The class implements methods of the MovieTheater database interface.
 *
 * All methods of the class receive a Connection object through which all
 * communication to the database should be performed. Note: the
 * Connection object should not be closed by any method.
 *
 * Also, no method should throw any exceptions. In particular, in case
 * an error occurs in the database, then the method should print an
 * error message and call System.exit(-1);
 */

public class MovieTheaterApplication {

    private Connection connection;

    /*
     * Constructor
     */
    public MovieTheaterApplication(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection()
    {
        return connection;
    }

    /**
     * getShowingsCount has a string argument called thePriceCode, and returns the number of
     * Showings whose priceCode equals thePriceCode.
     * A value of thePriceCode that’s not ‘A’, B’ or ‘C’ is an error.
     */

    public Integer getShowingsCount(String thePriceCode)
    {
        Integer result = 0;
        try {
            //error check to see if parameter thePriceCode is valid value
            if (thePriceCode == "A" || thePriceCode == "B" || thePriceCode == "C") {

                //make String query and put into PreparedStatement object
                String query = "SELECT COUNT(*) FROM Showings WHERE priceCode=?";
                PreparedStatement st = connection.prepareStatement(query);
                st.setString(1,thePriceCode); //set the ? to the value of thePriceCode
                st.execute();
                ResultSet rs = st.getResultSet();
                if (rs.next()) { //get the result that was returned and store in result
                    result = rs.getInt(1); 
                }
                st.close();
                rs.close();

            } else { //error
                System.out.println("thePriceCode is not A, B, or C!");
                System.exit(-1);
            }

        } catch (SQLException e) {
    		System.out.println("Error with query: " + e);
    		e.printStackTrace();
    	}
        return result;
    }


    /**
     * updateMovieName method has two arguments, an integer argument theMovieID, and a string
     * argument, newMovieName.  For the tuple in the Movies table (if any) whose movieID equals
     * theMovieID, updateMovieName should update its name to be newMovieName.  (Note that there
     * might not be any tuples whose movieID matches theMovieID.)  updateMovieName should return
     * the number of tuples that were updated, which will always be 0 or 1.
     */

    public int updateMovieName(int theMovieID, String newMovieName)
    {
        int updated = 0;
        try {
            //make String query and put into PreparedStatement
            String query ="UPDATE movies SET name=? WHERE movieID=?";
            PreparedStatement st = connection.prepareStatement(query);

            //set the ? to necessary parameter values
            st.setString(1, newMovieName);
            st.setInt(2, theMovieID);
            st.execute();
            
            //check to see if an element in database was updated(1 if it was)
            updated = st.getUpdateCount();
            st.close();
        } catch (SQLException e) {
    		System.out.println("Error with query: " + e);
    		e.printStackTrace();
    	}

        return updated;

    }


    /**
     * reduceSomeTicketPrices has an integer parameter, maxTicketCount.  It invokes a stored
     * function reduceSomeTicketPricesFunction that you will need to implement and store in the
     * database according to the description in Section 5.  reduceSomeTicketPricesFunction should
     * have the same parameter, maxTicketCount.  A value of maxTicketCount that’s not positive is
     * an error.
     *
     * The Tickets table has a ticketPrice attribute, which gives the price (in dollars and cents)
     * of each ticket.  reduceSomeTicketPricesFunction will reduce the ticketPrice for some (but
     * not necessarily all) tickets; Section 5 explains which tickets should have their
     * ticketPrice reduced, and also tells you how much they should be reduced.  The
     * reduceSomeTicketPrices method should return the same integer result that the
     * reduceSomeTicketPricesFunction stored function returns.
     *
     * The reduceSomeTicketPrices method must only invoke the stored function
     * reduceSomeTicketPricesFunction, which does all of the assignment work; do not implement
     * the reduceSomeTicketPrices method using a bunch of SQL statements through JDBC.
     */

    public int reduceSomeTicketPrices (int maxTicketCount)
    {
        // There's nothing special about the name storedFunctionResult
        int storedFunctionResult = 0;       

        //check to see if maxTicketCount is valid
        if (maxTicketCount < 0) {
            System.out.println("maxTicketPrice not a valid number!");
            System.exit(-1);
        }

        try {
            //make String query and put in PreparedStatement
            String query ="SELECT reduceSomeTicketPricesFunction(?)";
            PreparedStatement st = connection.prepareStatement(query);

            //set ? to int parameter
            st.setInt(1, maxTicketCount);
            st.execute();

            //get the ResultSet of the query
            ResultSet rs = st.getResultSet();
            if (rs.next()) { //store the result into storedFunctionResult
                storedFunctionResult = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return storedFunctionResult;
    }

};
