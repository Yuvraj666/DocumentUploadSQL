package com.cg.ibs.loanmgmt.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cg.ibs.loanmgmt.bean.LoanMaster;
import com.cg.ibs.loanmgmt.exception.IBSException;

public interface BankService {

	public Map<Long, LoanMaster> getLoanDetailsForVerification() throws IOException, ClassNotFoundException, IBSException;

	public LoanMaster getPreClosureDetailsForVerification()
			throws IOException, ClassNotFoundException; /* Getting Loan Details */

	public LoanMaster updatePreClosure(LoanMaster loanMaster);

	public boolean verifyLoan(LoanMaster loanMaster) throws Exception;

	public List<String> getFilesAvailable();

	public boolean downloadDocument(String destPath, String fileName);

}
