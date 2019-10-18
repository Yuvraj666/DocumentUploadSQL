package com.cg.ibs.loanmgmt.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cg.ibs.loanmgmt.bean.LoanMaster;
import com.cg.ibs.loanmgmt.dao.BankDao;
import com.cg.ibs.loanmgmt.dao.BankDaoImpl;
import com.cg.ibs.loanmgmt.exception.IBSException;

public class BankServiceImpl implements BankService {
	BankDao bankDao = new BankDaoImpl();
	LoanMaster loanMaster = new LoanMaster();
	static final String UPLOADS_LOC = "./uploads";

	public boolean verifyLoan(LoanMaster loanMaster) throws Exception {
		loanMaster.setLoanNumber(loanMaster.generateLoanNumber());
		return bankDao.saveLoan(loanMaster);
	}

	public Map<Long,LoanMaster> getLoanDetailsForVerification() throws IOException, ClassNotFoundException, IBSException {
		return bankDao.getLoanDetailsForVerification();
	}

	public List<String> getFilesAvailable() {
		List<String> files = new ArrayList<String>();
		File upLoc = new File(UPLOADS_LOC);
		for (File f : upLoc.listFiles()) {
			files.add(f.getName());
		}
		return files;
	}

	public boolean downloadDocument(String destPath, String fileName) {
		String srcPath = UPLOADS_LOC + "/" + fileName;
		destPath += "/" + fileName;
		return bankDao.copyDocument(srcPath, destPath);
	}

	public LoanMaster getPreClosureDetailsForVerification() throws IOException, ClassNotFoundException {
		return bankDao.getPreClosureDetailsForVerification();

	}

	public LoanMaster updatePreClosure(LoanMaster loanMaster) {
		return bankDao.updatePreClosure(loanMaster);
	}

}
