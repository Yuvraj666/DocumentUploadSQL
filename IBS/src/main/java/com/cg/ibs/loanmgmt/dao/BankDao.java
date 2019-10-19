package com.cg.ibs.loanmgmt.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import com.cg.ibs.loanmgmt.bean.LoanMaster;
import com.cg.ibs.loanmgmt.exception.IBSException;

public interface BankDao {
	public boolean saveLoan(LoanMaster loanMaster) throws SQLException;
	public long generateLoanNumber() throws IBSException; 

	public Map<Long, LoanMaster> getLoanDetailsForVerification() throws IOException, ClassNotFoundException, IBSException;

	public LoanMaster getPreClosureDetailsForVerification()
			throws IOException, ClassNotFoundException; /* Fetch loan details pending for verification */

	public LoanMaster updatePreClosure(LoanMaster loanMaster); /* Updates Loan Details */

	public boolean downloadDocument(long applicationNumber) throws IBSException;

}
