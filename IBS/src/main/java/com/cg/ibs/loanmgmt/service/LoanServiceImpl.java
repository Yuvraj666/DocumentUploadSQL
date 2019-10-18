package com.cg.ibs.loanmgmt.service;

import com.cg.ibs.loanmgmt.bean.LoanBean;
import com.cg.ibs.loanmgmt.bean.LoanType;

public class LoanServiceImpl implements LoanService {
	public Loan setLoanDetails(LoanBean loanBean) {

		if (loanBean.getLoanType() == LoanType.HOME_LOAN) {
			Loan loan = new HomeLoan();
			loan.setLoanAmount(loanBean.getLoanAmount());
			loan.setLoanTenure(loanBean.getLoanTenure());
			loan.setEmiAmount(loanBean.getEmiAmount());
			return loan;
		} else if (loanBean.getLoanType() == LoanType.EDUCATION_LOAN) {
			Loan loan = new EducationLoan();
			loan.setLoanAmount(loanBean.getLoanAmount());
			loan.setLoanTenure(loanBean.getLoanTenure());
			loan.setEmiAmount(loanBean.getEmiAmount());
			return loan;
		} else if (loanBean.getLoanType() == LoanType.PERSONAL_LOAN) {
			Loan loan = new PersonalLoan();
			loan.setLoanAmount(loanBean.getLoanAmount());
			loan.setLoanTenure(loanBean.getLoanTenure());
			loan.setEmiAmount(loanBean.getEmiAmount());
			return loan;
		} else if (loanBean.getLoanType() == LoanType.VEHICLE_LOAN) {
			Loan loan = new VehicleLoan();
			loan.setLoanAmount(loanBean.getLoanAmount());
			loan.setLoanTenure(loanBean.getLoanTenure());
			loan.setEmiAmount(loanBean.getEmiAmount());
			return loan;
		} else {
			return null;
		}

	}
}