CREATE FUNCTION reduceSomeTicketPricesFunction(IN maxTicketCount INT)
RETURNS INT AS $$
	
	DECLARE 
		returnValue INT := 0;
		numChangedA INT := 0;
		numChangedB INT := 0;
		numChangedC INT := 0;
		fetchDataID INT := -100;
		fetchDataDate DATE := NULL;
		fetchDataTime TIME := NULL;
		fetchDataCustID INT := -100;
	
	DECLARE cursorA CURSOR FOR
		SELECT T.customerID,T.theaterID,T.showingDate,T.startTime 
		FROM Tickets T, Showings S
		WHERE T.theaterID=S.theaterID AND T.showingDate=S.showingDate
			AND T.startTime=S.startTime AND S.priceCode='A' 
			AND T.ticketPrice IS NOT NULL
		ORDER BY T.customerID;

	DECLARE cursorB CURSOR FOR
        SELECT T.customerID,T.theaterID,T.showingDate,T.startTime
        FROM Tickets T, Showings S
        WHERE T.theaterID=S.theaterID AND T.showingDate=S.showingDate
            AND T.startTime=S.startTime AND S.priceCode='B' 
            AND T.ticketPrice IS NOT NULL
        ORDER BY T.customerID;

	DECLARE cursorC CURSOR FOR
        SELECT T.customerID,T.theaterID,T.showingDate,T.startTime
        FROM Tickets T, Showings S
        WHERE T.theaterID=S.theaterID AND T.showingDate=S.showingDate
            AND T.startTime=S.startTime AND S.priceCode='C' 
            AND T.ticketPrice IS NOT NULL
        ORDER BY T.customerID;


	BEGIN
		OPEN cursorA;
		LOOP
			EXIT WHEN (numChangedA+numChangedB+numChangedC) >= maxTicketCount;
			FETCH FROM cursorA INTO fetchDataCustID,fetchDataID,fetchDataDate,fetchDataTime;
			EXIT WHEN NOT FOUND;
			
			UPDATE Tickets 
			SET ticketPrice=ticketPrice-3 
			WHERE theaterID=fetchDataID AND showingDate=fetchDataDate 
                    AND startTime=fetchDataTime AND customerID=fetchDataCustID;
			
			numChangedA:=numChangedA+1;
		END LOOP;
		
		returnValue:=returnValue+(numChangedA*3);

		CLOSE cursorA;


		OPEN cursorB;
        LOOP
            EXIT WHEN (numChangedA+numChangedB+numChangedC) >= maxTicketCount;
            FETCH FROM cursorB INTO fetchDataCustID,fetchDataID,fetchDataDate,fetchDataTime;
            EXIT WHEN NOT FOUND;

            UPDATE Tickets
            SET ticketPrice=ticketPrice-2
            WHERE theaterID=fetchDataID AND showingDate=fetchDataDate 
                    AND startTime=fetchDataTime AND customerID=fetchDataCustID;

            numChangedB:=numChangedB+1;
        END LOOP;
		
		returnValue:=returnValue+(numChangedB*2);		

        CLOSE cursorB;

		OPEN cursorC;
        LOOP
            EXIT WHEN (numChangedA+numChangedB+numChangedC) >= maxTicketCount;
            FETCH FROM cursorC INTO fetchDataCustID,fetchDataID,fetchDataDate,fetchDataTime;
            EXIT WHEN NOT FOUND;

            UPDATE Tickets
            SET ticketPrice=ticketPrice-1
            WHERE theaterID=fetchDataID AND showingDate=fetchDataDate 
					AND startTime=fetchDataTime AND customerID=fetchDataCustID;

            numChangedC:=numChangedC+1;
        END LOOP;
	
		returnValue:=returnValue+(numChangedC*1);
        
		CLOSE cursorC;

		RETURN returnValue;
	
	END;
$$ LANGUAGE plpgsql; 
