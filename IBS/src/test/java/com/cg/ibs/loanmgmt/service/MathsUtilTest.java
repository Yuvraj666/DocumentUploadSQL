package com.cg.ibs.loanmgmt.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MathsUtilTest {
	private MathsUtil maths = new MathsUtil();

	@Test
	void test1() {
		Assertions.assertEquals(16, maths.add(11, 5));
	}
	@DisplayName("bow bow")
	@Test
	void test2() {
		Assertions.assertNotEquals(16, maths.add(21, 32));
	}

}
