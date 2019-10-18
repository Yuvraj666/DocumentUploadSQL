package com.cg.ibs.loanmgmt.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.cg.ibs.loanmgmt.bean.CustomerBean;
import com.cg.ibs.loanmgmt.bean.Document;
import com.cg.ibs.loanmgmt.bean.LoanBean;
import com.cg.ibs.loanmgmt.bean.LoanMaster;
import com.cg.ibs.loanmgmt.bean.LoanType;
import com.cg.ibs.loanmgmt.dao.CustomerDao;
import com.cg.ibs.loanmgmt.dao.CustomerDaoImpl;
import com.cg.ibs.loanmgmt.exception.IBSException;

public class CustomerServiceImpl implements CustomerService {
	Loan loan;
	Document document = new Document();
	LoanMaster loanMaster = new LoanMaster();
	CustomerDao customerDao = new CustomerDaoImpl();
	public static final String UPLOADS_LOC = "./uploads";

	public boolean loanCustomerInputVerificationService(Loan loan) {
		boolean amountValid = loan.isValidLoanAmount(loan.getLoanAmount());
		boolean tenureValid = loan.isValidTenure(loan.getLoanTenure());
		boolean check = false;
		if (amountValid && tenureValid) {
			check = true;
		}
		return check;
	}

	@Override
	public double calculateEmi(Loan loan) {
		LoanBean loanBeanTemp = new LoanBean();
		float rate = loan.getInterestRate() / (12 * 100);
		loanBeanTemp.setEmiAmount(((loan.getLoanAmount() * rate * Math.pow((rate + 1), loan.getLoanTenure()))
				/ (Math.pow((rate + 1), loan.getLoanTenure()) - 1)));
		return loanBeanTemp.getEmiAmount();

	}

	public LoanMaster getLoanValues(Loan loan, String userId)
			throws SQLException, IBSException { /* makes a master copy of the loan to be sent */
		loanMaster.setLoanAmount(loan.getLoanAmount());
		loanMaster.setEmiAmount(loan.getEmiAmount());
		loanMaster.setLoanTenure(loan.getLoanTenure());
		loanMaster.setNumberOfEmis(0);
		loanMaster.setAppliedDate(LocalDate.now());
		loanMaster.setNextEmiDate(loanMaster.getAppliedDate().plusMonths(1));
		loanMaster.setCustomerBean(getCustomerDetails(userId));
		loanMaster.setTotalNumberOfEmis(loanMaster.getLoanTenure());
		loanMaster.setApplicationNumber(loanMaster.generateApplicationNumber()); /* Generates Application Number */
		loanMaster.setLoanType(loan.getLoanType());
		loanMaster.setInterestRate(loan.getInterestRate());
		loanMaster.setBalance(0);
		return loanMaster;
	}

	public CustomerBean getCustomerDetails(String userId) throws SQLException, IBSException {
		return customerDao.getCustomerDetails(userId);
	}

	public boolean uploadDocument(Document document , LoanMaster loanMaster) throws IBSException { /* Document Upload */
		return customerDao.uploadDocument(document,loanMaster);
	}

	public boolean sendLoanForVerification(LoanMaster loanMaster) throws IBSException{
		return customerDao.sendLoanForVerification(loanMaster);

	}

	private LoanMaster getEMIDetails(String loanNumber) {
		return customerDao.getEMIDetails(loanNumber);

	}

	private boolean verifyEmi(LoanMaster loanMaster) {
		boolean check = false;
		if (loanMaster.getTotalNumberOfEmis() > loanMaster.getNumberOfEmis()) {
			check = true;
		}
		return check;
	}

	public LoanMaster verifyEmiApplicable(String loanNumber) {

		if (verifyEmi(getEMIDetails(loanNumber)) == true) {
			return getEMIDetails(loanNumber);
		} else {
			return null;
		}
	}

	private boolean verifyTransaction(double presentAmount, LoanMaster loanMaster) {

		boolean result = false;
		if (presentAmount == loanMaster.getEmiAmount()) {
			result = true;
		}
		return result;
	}

	public LoanMaster updateEMI(double amountPaid, LoanMaster loanMaster) {
		if (verifyTransaction(amountPaid, loanMaster)) {
			return customerDao.updateEMI(loanMaster);
		} else {
			return null;
		}
	}

	// LoanDetails

	public List<LoanMaster> getHistory(String userId) throws SQLException, IBSException {
		return customerDao.getHistory(userId); /*
												 * Getting collection of all the loans related to given userId
												 */
	}

	// PreClosure Customer
	public LoanMaster getPreClosureLoanDetails(String loanNumber) {
		return getInterestRate(customerDao.getPreClosureLoanDetails(loanNumber));
	}

	private boolean verifyPreclosure(LoanMaster loanMaster) { /* Preliminary Verification */
		boolean check = false;
		if (loanMaster.getNumberOfEmis() > (loanMaster.getTotalNumberOfEmis() / 4)) {
			check = true;
		}
		return check;
	}

	public boolean verifyLoanNumber(String loanNumber) {
		boolean check = false;
		if (customerDao.verifyLoanNumber(loanNumber)) { /* Verification of Loan */
			check = verifyPreclosure(getPreClosureLoanDetails(loanNumber)); /* Preliminary Verification */
		}
		return check;
	}

	public double calculatePreClosure(LoanMaster loanMaster) {
		double paidAmount = loanMaster.getNumberOfEmis() * loanMaster.getEmiAmount();
		double paidInterest = loanMaster.getLoanAmount() * (Math.pow(1 + (double) loanMaster.getInterestRate() / 100.0,
				(double) loanMaster.getNumberOfEmis() / 12.0)) - loanMaster.getLoanAmount();
		double paidPrincipal = (paidAmount - paidInterest);

		return (loanMaster.getLoanAmount() - paidPrincipal);
	}

	public boolean sendPreClosureForVerification(LoanMaster loanMaster) throws FileNotFoundException, IOException {

		return customerDao.sendPreClosureForVerification(loanMaster); /* Sending data */
	}

	private LoanMaster getInterestRate(LoanMaster loanMaster) {
		if (loanMaster.getLoanType() == LoanType.HOME_LOAN) {
			loanMaster.setInterestRate(8.5f);
		} else if (loanMaster.getLoanType() == LoanType.EDUCATION_LOAN) {
			loanMaster.setInterestRate(11.35f);
		} else if (loanMaster.getLoanType() == LoanType.PERSONAL_LOAN) {
			loanMaster.setInterestRate(10.75f);
		} else if (loanMaster.getLoanType() == LoanType.VEHICLE_LOAN) {
			loanMaster.setInterestRate(9.25f);
		}
		return loanMaster;
	}

	@Override
	public boolean verifyCustomer(String userId) throws IBSException {
		return customerDao.verifyCustomer(userId);
	}

}
