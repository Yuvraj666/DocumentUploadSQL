package com.cg.ibs.loanmgmt.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.cg.ibs.loanmgmt.bean.CustomerBean;
import com.cg.ibs.loanmgmt.bean.Document;
import com.cg.ibs.loanmgmt.bean.LoanMaster;
import com.cg.ibs.loanmgmt.bean.LoanType;
import com.cg.ibs.loanmgmt.exception.IBSException;

class CustomerServiceImplTest {
	private static CustomerServiceImpl customerService = new CustomerServiceImpl();
	Loan loanTemp = new HomeLoan();
	LoanMaster loanMasterTemp = new LoanMaster();
	CustomerBean customerTemp = new CustomerBean();

	// HOME_LOAN
	@Test
	void calculateHLoanEmiTest() {

		loanTemp.setLoanAmount(12654);
		loanTemp.setLoanTenure(12);
		double actual = customerService.calculateEmi(loanTemp);
		assertEquals(1103.6846021816305, actual);

	}

	@Test
	void calculateHLoanEmiNegativeTest() {
		Loan loanTemp = new HomeLoan();

		loanTemp.setLoanAmount(12654);
		loanTemp.setLoanTenure(12);
		double actual = customerService.calculateEmi(loanTemp);
		assertNotEquals(89189, actual);
	}

	@Test
	void loanCustomerHLoanVerificationPositiveTest() {
		Loan loanTemp = new EducationLoan();
		loanTemp.setLoanAmount(12654);
		loanTemp.setLoanTenure(12);
		boolean actual = customerService.loanCustomerInputVerificationService(loanTemp);
		assertEquals(true, actual);
	}

	@Test
	void loanCustomerHLoanVerificationNegativeTest() {
		Loan loanTemp = new EducationLoan();
		loanTemp.setLoanAmount(1561899);
		loanTemp.setLoanTenure(13);
		boolean actual = customerService.loanCustomerInputVerificationService(loanTemp);
		assertNotEquals(true, actual);
	}

	// VEHICLE_LOAN
	@Test
	void calculateVLoanEmiPositiveTest() {
		Loan loanTemp = new VehicleLoan();

		loanTemp.setLoanAmount(12654);
		loanTemp.setLoanTenure(12);
		double actual = customerService.calculateEmi(loanTemp);

		assertEquals(1108.0815900950197, actual);

	}

	@Test
	void calculateVLoanEmiNegativeTest() {
		Loan loanTemp = new VehicleLoan();

		loanTemp.setLoanAmount(12654);
		loanTemp.setLoanTenure(12);
		double actual = customerService.calculateEmi(loanTemp);
		assertNotEquals(11058, actual);
	}

	@Test
	void loanCustomerVLoanVerificationPositiveTest() {
		Loan loanTemp = new EducationLoan();
		loanTemp.setLoanAmount(12654);
		loanTemp.setLoanTenure(12);
		boolean actual = customerService.loanCustomerInputVerificationService(loanTemp);
		assertEquals(true, actual);
	}

	@Test
	void loanCustomerVLoanVerificationNegativeTest() {
		Loan loanTemp = new EducationLoan();
		loanTemp.setLoanAmount(12654896);
		loanTemp.setLoanTenure(128);
		boolean actual = customerService.loanCustomerInputVerificationService(loanTemp);
		assertNotEquals(true, actual);
	}

	@Test
	void calculatePLoanEmiPositiveTest() {
		Loan loanTemp = new PersonalLoan();

		loanTemp.setLoanAmount(12654);
		loanTemp.setLoanTenure(12);
		double actual = customerService.calculateEmi(loanTemp);

		assertEquals(1116.9056586284007, actual);

	}

	@Test
	void calculatePLoanEmiNegativeTest() {
		Loan loanTemp = new PersonalLoan();

		loanTemp.setLoanAmount(12654);
		loanTemp.setLoanTenure(12);
		double actual = customerService.calculateEmi(loanTemp);
		assertNotEquals(11058, actual);
	}

	@Test
	void loanCustomerPLoanVerificationPositiveTest() {
		Loan loanTemp = new EducationLoan();
		loanTemp.setLoanAmount(12654);
		loanTemp.setLoanTenure(12);
		boolean actual = customerService.loanCustomerInputVerificationService(loanTemp);
		assertEquals(true, actual);
	}

	@Test
	void loanCustomerPLoanVerificationNegativeTest() {
		Loan loanTemp = new EducationLoan();
		loanTemp.setLoanAmount(1265489654);
		loanTemp.setLoanTenure(13);
		boolean actual = customerService.loanCustomerInputVerificationService(loanTemp);
		assertNotEquals(true, actual);
	}

	// EDUCATION_LOAN
	@Test
	void calculateELoanEmiPositiveTest() {
		Loan loanTemp = new EducationLoan();

		loanTemp.setLoanAmount(12654);
		loanTemp.setLoanTenure(12);
		double actual = customerService.calculateEmi(loanTemp);

		assertEquals(1120.4517435964037, actual);

	}

	@Test
	void calculateELoanEmiNegativeTest() {
		Loan loanTemp = new EducationLoan();

		loanTemp.setLoanAmount(12654);
		loanTemp.setLoanTenure(12);
		double actual = customerService.calculateEmi(loanTemp);
		assertNotEquals("garbage", actual);
	}

	@Test
	void loanCustomerELoanVerificationPositiveTest() {
		Loan loanTemp = new EducationLoan();
		loanTemp.setLoanAmount(12654);
		loanTemp.setLoanTenure(12);
		assertTrue(customerService.loanCustomerInputVerificationService(loanTemp));

	}

	@Test
	void loanCustomerELoanVerificationNegativeTest() {
		Loan loanTemp = new EducationLoan();
		loanTemp.setLoanAmount(99999999);
		loanTemp.setLoanTenure(33);
		assertFalse(customerService.loanCustomerInputVerificationService(loanTemp));

	}

	@Test
	void verifyCustomerPositiveTest() {
		boolean actual = customerService.verifyCustomer("ckohli");
		Assertions.assertEquals(true, actual);
	}

	@Test
	void verifyCustomerNegativeTest() {
		boolean actual = customerService.verifyCustomer("ssinha");
		Assertions.assertNotEquals(true, actual);
	}

	@Test
	void verifyEmiApplicablePositiveTest() {

		loanMasterTemp.setLoanNumber(998);
		// loanMasterTemp.set
		LoanMaster actual = customerService.verifyEmiApplicable(loanMasterTemp.getLoanNumber());
		LoanMaster loanMaster1 = new LoanMaster("998", LoanType.VEHICLE_LOAN, 1000000.00, 24, 45800.0,
				new CustomerBean("Chetan", "Kohli", "12346", "ckohli"), 16, 24, LocalDate.of(2018, 6, 15),
				LocalDate.now());
		assertNotNull(actual);
	}

	@Test
	void verifyLoanNumberPositiveTest() {
		boolean actual = customerService.verifyLoanNumber(loanMasterTemp.setLoanNumber(1000));
		assertEquals(true, actual);
	}

	@Test
	void verifyLoanNumberNegativeTest() {
		boolean actual = customerService.verifyLoanNumber(loanMasterTemp.setLoanNumber(1000));
		assertNotEquals(false, actual);
	}

	@Test
	void checkPreClosure() {
		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
		loanMasterTemp.setLoanType(LoanType.HOME_LOAN);
		loanMasterTemp.setEmiAmount(78954.00);
		loanMasterTemp.setInterestRate(8.5f);
		loanMasterTemp.setLoanAmount(2000000);
		loanMasterTemp.setLoanTenure(24);
		loanMasterTemp.setLoanNumber(1234);
		CustomerBean customerBean = new CustomerBean("Dev", "Goyal", "1234", "dgoyal");
		loanMasterTemp.setCustomerBean(customerBean);
		loanMasterTemp.setNumberOfEmis(13);
		loanMasterTemp.setTotalNumberOfEmis(24);
		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
		double actual = customerService.calculatePreClosure(loanMasterTemp);
		assertEquals(1158400.640604138, actual);
	}

	@Test
	void checkPreClosureNeg() {
		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
		loanMasterTemp.setLoanType(LoanType.HOME_LOAN);
		loanMasterTemp.setEmiAmount(78954.00);
		loanMasterTemp.setInterestRate(8.5f);
		loanMasterTemp.setLoanAmount(2000000);
		loanMasterTemp.setLoanTenure(24);
		loanMasterTemp.setLoanNumber(1234);
		CustomerBean customerBean = new CustomerBean("Dev", "Goyal", "1234", "dgoyal");
		loanMasterTemp.setCustomerBean(customerBean);
		loanMasterTemp.setNumberOfEmis(13);
		loanMasterTemp.setTotalNumberOfEmis(24);
		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
		double actual = customerService.calculatePreClosure(loanMasterTemp);
		assertNotEquals(110.60, actual);
	}

	@Test
	void sendPreClosureForVerificationPos() throws FileNotFoundException, IOException {
		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
		loanMasterTemp.setLoanType(LoanType.HOME_LOAN);
		loanMasterTemp.setEmiAmount(78954.00);
		loanMasterTemp.setInterestRate(8.5f);
		loanMasterTemp.setLoanAmount(2000000);
		loanMasterTemp.setLoanTenure(24);
		loanMasterTemp.setLoanNumber(1234);
		CustomerBean customerBean = new CustomerBean("Dev", "Goyal", "1234", "dgoyal");
		loanMasterTemp.setCustomerBean(customerBean);
		loanMasterTemp.setNumberOfEmis(13);
		loanMasterTemp.setTotalNumberOfEmis(24);
		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
		boolean actual = customerService.sendPreClosureForVerification(loanMasterTemp);
		assertEquals(true, actual);

	}

	@Test
	void sendPreClosureForVerificationNeg() throws FileNotFoundException, IOException {
		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
		loanMasterTemp.setLoanType(LoanType.HOME_LOAN);
		loanMasterTemp.setEmiAmount(78954.00);
		loanMasterTemp.setInterestRate(8.5f);
		loanMasterTemp.setLoanAmount(2000000);
		loanMasterTemp.setLoanTenure(24);
		loanMasterTemp.setLoanNumber(1234);
		CustomerBean customerBean = new CustomerBean("Dev", "Goyal", "1234", "dgoyal");
		loanMasterTemp.setCustomerBean(customerBean);
		loanMasterTemp.setNumberOfEmis(13);
		loanMasterTemp.setTotalNumberOfEmis(24);
		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
		boolean actual = customerService.sendPreClosureForVerification(loanMasterTemp);
		assertNotEquals(false, actual);

	}

//	@Test
//	void sendPreClosureForVerificationNeutral() throws FileNotFoundException, IOException {
//		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
//		loanMasterTemp.setLoanType(LoanType.HOME_LOAN);
//		loanMasterTemp.setEmiAmount(78954.00);
//		loanMasterTemp.setInterestRate(8.5f);
//		loanMasterTemp.setLoanAmount(2000000);
//		loanMasterTemp.setLoanTenure(24);
//		loanMasterTemp.setLoanNumber(1234);
//		CustomerBean customerBean = new CustomerBean("Shreya", "Sinha", "1234", "ssinha");
//		loanMasterTemp.setCustomerBean(customerBean);
//		loanMasterTemp.setNumberOfEmis(13);
//		loanMasterTemp.setTotalNumberOfEmis(24);
//		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
//		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./PreClosureDetailsXYZ.dat"));
//		out.writeObject(loanMasterTemp);
//		out.close();
//		LoanMaster loanMasterActual = null;
//		assertThrows(ClassCastException.class, () -> {
//			customerService.sendPreClosureForVerification(loanMasterTemp);
//		});
//	}

	@Test
	void getPreClosureLoanDetailsPos() {
		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
		loanMasterTemp.setLoanNumber(1000);
		LoanMaster actual = customerService.getPreClosureLoanDetails(loanMasterTemp.getLoanNumber());
		assertNotNull(actual);
	}

	@Test
	void checkHistoryPos() {
		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
		loanMasterTemp.setLoanType(LoanType.HOME_LOAN);
		loanMasterTemp.setEmiAmount(78954.00);
		loanMasterTemp.setInterestRate(8.5f);
		loanMasterTemp.setLoanAmount(2000000);
		loanMasterTemp.setLoanTenure(24);
		loanMasterTemp.setLoanNumber(997);
		CustomerBean customerBean = new CustomerBean("Dev", "Goyal", "1234", "dgoyal");
		loanMasterTemp.setCustomerBean(customerBean);
		loanMasterTemp.setNumberOfEmis(13);
		loanMasterTemp.setTotalNumberOfEmis(24);
		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
		List<LoanMaster> actual = customerService.getHistory(loanMasterTemp.setLoanNumber(997));
		assertNotEquals(true, actual);
	}

	@Test
	void updateEMIpos() {
		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
		loanMasterTemp.setLoanType(LoanType.PERSONAL_LOAN);
		loanMasterTemp.setEmiAmount(23245.0);
		loanMasterTemp.setInterestRate(10.75f);
		loanMasterTemp.setLoanAmount(500000);
		loanMasterTemp.setLoanTenure(24);
		loanMasterTemp.setLoanNumber(999);
		CustomerBean customerBean = new CustomerBean("Dev", "Goyal", "12347", "dgoyal");
		loanMasterTemp.setCustomerBean(customerBean);
		loanMasterTemp.setNumberOfEmis(23);
		loanMasterTemp.setTotalNumberOfEmis(24);
		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
		LoanMaster actual = customerService.updateEMI(23245.0, loanMasterTemp);
		assertNotNull(actual);

	}

	@Test
	void updateEMINeg() {
		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
		loanMasterTemp.setLoanType(LoanType.PERSONAL_LOAN);
		loanMasterTemp.setEmiAmount(23245.0);
		loanMasterTemp.setInterestRate(10.75f);
		loanMasterTemp.setLoanAmount(500000);
		loanMasterTemp.setLoanTenure(24);
		loanMasterTemp.setLoanNumber(999);
		CustomerBean customerBean = new CustomerBean("Dev", "Goyal", "12347", "dgoyal");
		loanMasterTemp.setCustomerBean(customerBean);
		loanMasterTemp.setNumberOfEmis(23);
		loanMasterTemp.setTotalNumberOfEmis(24);
		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
		LoanMaster actual = customerService.updateEMI(8965.0, loanMasterTemp);
		assertNull(actual);
	}

	@Test
	void getCustomerDetailsPos() {
		customerTemp.setUserId("dgoyal");
		CustomerBean actual = customerService.getCustomerDetails(customerTemp.getUserId());
		assertNotNull(actual);
	}

	@Test
	void getCustomerDetailsNeg() {
		customerTemp.setUserId("ssinha");
		CustomerBean actual = customerService.getCustomerDetails(customerTemp.getUserId());
		assertNull(actual);
	}

	@Test
	void getLoanValuesPos() {
		loanTemp.setEmiAmount(21917.0);
		loanTemp.setLoanAmount(1000000.00);
		loanTemp.setLoanTenure(60);
		loanTemp.setLoanType(LoanType.EDUCATION_LOAN);
		LoanMaster actual = customerService.getLoanValues(loanTemp, "ykalra");

		assertNotNull(actual);
	}

	void getLoanValuesNeg() {
		loanTemp.setEmiAmount(96896.0);
		loanTemp.setLoanAmount(1000.00);
		loanTemp.setLoanTenure(60);
		loanTemp.setLoanType(LoanType.EDUCATION_LOAN);
		LoanMaster actual = customerService.getLoanValues(loanTemp, "ssinha");
		assertNull(actual);
	}

	@Test
	void sendLoanforVerificationPos() throws FileNotFoundException, IOException {

		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
		loanMasterTemp.setLoanType(LoanType.PERSONAL_LOAN);
		loanMasterTemp.setEmiAmount(23245.0);
		loanMasterTemp.setInterestRate(10.75f);
		loanMasterTemp.setLoanAmount(500000);
		loanMasterTemp.setLoanTenure(24);
		loanMasterTemp.setLoanNumber(999);
		CustomerBean customerBean = new CustomerBean("Dev", "Goyal", "12347", "dgoyal");
		loanMasterTemp.setCustomerBean(customerBean);
		loanMasterTemp.setNumberOfEmis(23);
		loanMasterTemp.setTotalNumberOfEmis(24);
		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
		boolean actual = customerService.sendLoanForVerification(loanMasterTemp);
		assertTrue(actual);
	}

//	@Test
//	void sendLoanForVerificationNegTest() throws FileNotFoundException, IOException {
//		//LoanMaster loanMasterTemp = null;
//		loanMasterTemp.setAppliedDate(LocalDate.of(2018, Month.SEPTEMBER, 8));
//		loanMasterTemp.setLoanType(LoanType.PERSONAL_LOAN);
//		loanMasterTemp.setEmiAmount(23245.0);
//		loanMasterTemp.setInterestRate(10.75f);
//		loanMasterTemp.setLoanAmount(500000);
//		loanMasterTemp.setLoanTenure(24);
//		loanMasterTemp.setLoanNumber(999);
//		CustomerBean customerBean = new CustomerBean("Dev", "Goyal", "12347", "dgoyal");
//		loanMasterTemp.setCustomerBean(customerBean);
//		loanMasterTemp.setNumberOfEmis(23);
//		loanMasterTemp.setTotalNumberOfEmis(24);
//		loanMasterTemp.setNextEmiDate(LocalDate.of(2019, 10, 14));
//		boolean actual = customerService.sendLoanForVerification(loanMasterTemp);
//		assertTrue(actual);
//	}

	@Test
	void getDocumentTestPositive() {
		Document documentTemp = new Document();
		String voterId = "VoterIDCard";
		String pathOfDocument = "C://Users//DEVGOYAL//Downloads//sample.pdf";
		documentTemp.setNameOfDocument(voterId);
		documentTemp.setPathOfDocument(pathOfDocument);
		try {
			boolean result = customerService.getDocument(documentTemp);
			assertEquals(true, result);
		} catch (IBSException exp) {
			fail("Test Failed: " + exp.getMessage());
		}
	}

	@Test
	void getDocumentTestNegative() {
		Document documentTemp = new Document();
		String voterId = "VoterIDCard";
		String pathOfDocument = "C://Users//Lenovo//Downloads//sample1.pdf";
		documentTemp.setNameOfDocument(voterId);
		documentTemp.setPathOfDocument(pathOfDocument);
		try {
			boolean result = customerService.getDocument(documentTemp);
			assertEquals(false, result);
		} catch (IBSException exp) {
			fail("Test Failed: " + exp.getMessage());
		}
	}

	@Test
	void getDocumentTestForEmptyDocumentDetails() {
		Document documentTemp = new Document();
		assertNull(documentTemp.getNameOfDocument());
	}

	@Test
	void getDocumentTestForEmptyDocument() {
		Document documentTemp = new Document();
		assertNotNull(documentTemp);
	}

}