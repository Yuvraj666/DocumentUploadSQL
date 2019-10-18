package com.cg.ibs.loanmgmt.bean;

public class LoanBean {
	private double loanAmount;
	private int loanTenure;
	private LoanType loanType;
	private float interestRate;
	private double emiAmount;

	public double getEmiAmount() {
		return emiAmount;
	}

	public void setEmiAmount(double emiAmount) {
		this.emiAmount = emiAmount;
	}

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

	public LoanType getLoanType() {
		return loanType;
	}

	public void setLoanType(LoanType loanType) {
		this.loanType = loanType;
	}

	public float getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(float interestRate) {
		this.interestRate = interestRate;
	}

	@Override
	public String toString() {
		return "Loan Type\t :" + loanType + "\nLoan Amount\t :Rs " + loanAmount + "\nLoan Tenure\t :" + loanTenure
				+ " months\nInterest Rate\t :" + interestRate + "%\nEMI Amount\t :Rs "
				+ String.format("%.2f", emiAmount);
	}
}
