package com.cg.ibs.loanmgmt.service;

import com.cg.ibs.loanmgmt.bean.LoanType;

public class HomeLoan extends Loan {
	private final float INTEREST_RATE = 8.5f ;
	public final double LOAN_LIMIT = 20000000;
	private final LoanType LOAN_TYPE = LoanType.HOME_LOAN;
	
	public HomeLoan() {
		setLoanType(LoanType.HOME_LOAN);
	}
	public LoanType getLOAN_TYPE() {
		return LOAN_TYPE;
	}
	
	@Override
	public String toString() {
		return "*****\n Loan Type = " + getLoanType() + "\n Interest Rate=" + INTEREST_RATE + "\n Loan Amount= " + getLoanAmount() + ",\n Loan Tenure ="
				+ getLoanTenure() + " Months " + "\n Emi Amount= "+ getEmiAmount() + "\n *****";
	}
	public boolean isValidLoanAmount(double loanAmount) {
		
			if (loanAmount < LOAN_LIMIT && loanAmount > LOAN_LOWER_LIMIT) {
				return true;
			} else {
				return false;
			}
		}
	public float getInterestRate() {
		return INTEREST_RATE;
	}

}
