package com.cg.ibs.loanmgmt.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.cg.ibs.loanmgmt.bean.CustomerBean;
import com.cg.ibs.loanmgmt.bean.Document;
import com.cg.ibs.loanmgmt.bean.LoanMaster;
import com.cg.ibs.loanmgmt.exception.IBSException;

public interface CustomerService {
	// Apply for Loan
	public boolean loanCustomerInputVerificationService(Loan loan);

	public double calculateEmi(Loan loan);

	public boolean uploadDocument(Document document, LoanMaster loanMaster) throws IBSException;

	public boolean sendLoanForVerification(LoanMaster loanMaster) throws IBSException;

	public LoanMaster getLoanValues(Loan loan, String userId) throws SQLException, IBSException;

	public CustomerBean getCustomerDetails(String userId) throws SQLException, IBSException;

	public boolean verifyCustomer(String userId) throws IBSException;
	// Pay EMI

	public LoanMaster verifyEmiApplicable(String loanNumber);

	public LoanMaster updateEMI(double amountPaid, LoanMaster loanMaster);

	// ViewLoanDetails

	public List<LoanMaster> getHistory(String userId) throws SQLException, IBSException; /*getting collection of all the loans related to given userID*/

	// PreClosure

	public LoanMaster getPreClosureLoanDetails(String loanNumber);// Fetching
																	// Loan
																	// against
																	// Loan
																	// Number

	public boolean verifyLoanNumber(String loanNumber);// Verification of loan
														// existence and
														// preliminary
														// verification

	public double calculatePreClosure(LoanMaster loanMaster);// Calculating
																// PreClosure
																// Amount

	boolean sendPreClosureForVerification(LoanMaster loanMaster) throws FileNotFoundException,
			IOException; /* Send preClosure for verification */

}