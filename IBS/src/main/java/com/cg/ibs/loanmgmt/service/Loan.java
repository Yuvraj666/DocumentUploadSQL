package com.cg.ibs.loanmgmt.service;

import com.cg.ibs.loanmgmt.bean.LoanType;

public abstract class Loan {
	private double loanAmount;
	private int loanTenure;
	private double emiAmount;
	private LoanType loanType = null;
	private float interestRate;
	public final double LOAN_LOWER_LIMIT = 10000;

	

	public Loan() {
		super();
	}

	public Loan(double loanAmount, int loanTenure, double emiAmount, LoanType loanType) {
		super();
		this.loanAmount = loanAmount;
		this.loanTenure = loanTenure;
		this.emiAmount = emiAmount;
		this.loanType = loanType;
	}

	public boolean isValidTenure(int tenure) {
		if (tenure >0 && (tenure%6==0)) {
			return true;
		} else {
			return false;
		}
	}
	public abstract boolean isValidLoanAmount(double loanAmount);

	public abstract float getInterestRate();

	public double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public int getLoanTenure() {
		return loanTenure;
	}

	public void setLoanTenure(int loanTenure) {
		this.loanTenure = loanTenure;
	}

	public double getEmiAmount() {
		return emiAmount;
	}

	public void setEmiAmount(double emiAmount) {
		this.emiAmount = emiAmount;
	}

	public LoanType getLoanType() {
		return loanType;
	}

	public void setLoanType(LoanType loanType) {
		this.loanType = loanType;
	}
}
