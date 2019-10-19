package com.cg.ibs.loanmgmt.dao;

public interface QueryMapper {
	public static String SEL_THE_COMMON_ROWS = "select customers.uci, customers.user_id, customers.first_name,customers.last_name,"
			+ "loan.loan_number, loan.loan_amount, loan.loan_tenure, loan.balance,loan.applied_date, "
			+ "loan.total_num_of_emis, loan.num_of_emis_paid, loan.type_id,loan.emi_amount,"
			+ "loan_type.loan_type, loan_type.interest_rate, loan_type.maximum_limit,loan_type.minimum_limit "
			+ "from customers inner join loan on customers.uci=loan.uci inner join loan_type"
			+ " on loan.type_id = loan_type.type_id where customers.user_id=?";
	public static String GENERATE_APPLICANT_NUM = "select applicant_number_seq.nextval from dual";
	public static String GENERATE_LOAN_NUM = "select loan_number_seq.nextval from dual";
	public static String VERIFY_CUSTOMER = "select user_id from customers where user_id=?";
	public static String GET_UCI = "select uci from customers where user_id=?";
	public static String SEND_LOAN_VERIFICATION = "Insert into loan_applicant(applicant_num, uci,status) values(?,?,?)";
	public static String UPLOAD_DOCUMENT = "update loan set document = ? where uci=?";
	public static String DOWNLOAD_DOCUMENT = "select document from loan where applicant_num=?";
	public static String GET_PENDING_LOAN = "Select loan.applicant_num, loan.uci, loan.loan_amount, loan.loan_tenure, "
			+ "loan.balance, loan.applied_date, loan.total_num_of_emis, loan.num_of_emis_paid, loan.type_id,loan.emi_amount, "
			+ "loan.status, loan_type.interest_rate,customers.first_name,customers.last_name,customers.user_id from loan JOIN loan_type on "
			+ "loan.type_id=loan_type.type_id JOIN customers on loan.uci=customers.uci where loan.status='pending'";
	public static String GET_CUSTOMER_DETAILS = "Select uci, user_id, First_name, last_name from customers where user_id=?";
	public static String INS_LOAN = "update loan set status=? where applicant_num =?";
	public static String APPROVE_LOAN="insert into approved_loan values(?,?)";
	public static String INS_APP = "Insert into loan (applicant_num,uci,status,loan_amount,loan_tenure,total_num_of_emis,emi_amount,applied_date,type_id,balance,num_of_emis_paid) values(?,?,?,?,?,?,?,?,?,?,?)";
}
