package com.cg.ibs.loanmgmt.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.cg.ibs.loanmgmt.bean.CustomerBean;
import com.cg.ibs.loanmgmt.bean.Document;
import com.cg.ibs.loanmgmt.bean.LoanMaster;
import com.cg.ibs.loanmgmt.exception.IBSException;

public interface CustomerDao {

	public CustomerBean getCustomerDetails(String userId) throws SQLException, IBSException;

	public boolean verifyCustomer(String userId) throws IBSException;

	public boolean sendLoanForVerification(LoanMaster loanMaster) throws IBSException;

	public LoanMaster updateEMI(LoanMaster loanMaster);

	public LoanMaster getEMIDetails(String loanNumber);

	public List<LoanMaster> getHistory(String userId) throws SQLException, IBSException;

	public LoanMaster getPreClosureLoanDetails(String loanNumber);

	public boolean verifyLoanNumber(String loanNumber);

	public boolean sendPreClosureForVerification(LoanMaster loanMaster) throws FileNotFoundException, IOException;

	public boolean uploadDocument(Document document, LoanMaster loanMaster) throws IBSException;
}