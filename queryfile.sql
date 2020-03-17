SELECT T.customerID,T.ticketPrice, T.theaterID, T.showingDate, T.startTime 
FROM Tickets T,Showings S 
WHERE S.priceCode='A' AND T.theaterID=S.theaterID 
	AND T.showingDate=S.showingDate AND T.startTime=S.startTime
ORDER BY T.customerID;

SELECT T.customerID,T.ticketPrice, T.theaterID, T.showingDate, T.startTime
FROM Tickets T,Showings S
WHERE S.priceCode='B' AND T.theaterID=S.theaterID
    AND T.showingDate=S.showingDate AND T.startTime=S.startTime
ORDER BY T.customerID;


SELECT T.customerID,T.ticketPrice, T.theaterID, T.showingDate, T.startTime
FROM Tickets T,Showings S
WHERE S.priceCode='C' AND T.theaterID=S.theaterID
    AND T.showingDate=S.showingDate AND T.startTime=S.startTime
ORDER BY T.customerID;

