package com.committee.security;

import com.committee.security.crypto.SCryptUtil;

public class ScryptPasswordHashingDemo {

	// public static void main(String[] args) {
	// String originalPassword = "password";
	// String generatedSecuredPasswordHash = SCryptUtil.scrypt(originalPassword,
	// 16, 16, 16);
	// System.out.println(generatedSecuredPasswordHash);
	//
	// boolean matched = SCryptUtil.check("password",
	// generatedSecuredPasswordHash);
	// System.out.println(matched);
	//
	// matched = SCryptUtil.check("passwordno", generatedSecuredPasswordHash);
	// System.out.println(matched);
	// }

	public String encoder(String originalPassword) {
		// String originalPassword = "password";
		// String generatedSecuredPasswordHash = SCryptUtil.scrypt(originalPassword,
		// 16, 16, 16);
		// System.out.println(generatedSecuredPasswordHash);

		// boolean matched = SCryptUtil.check("password",
		// generatedSecuredPasswordHash);
		// System.out.println("if password"+matched);
		//
		// matched = SCryptUtil.check("passwordno => ",
		// generatedSecuredPasswordHash);
		// System.out.println("if passwordno => "+matched);
		return SCryptUtil.scrypt(originalPassword, 16, 16, 16);
	}

	public boolean matched(String enterPassword, String passwordFromDB) {
		return SCryptUtil.check(enterPassword, passwordFromDB);
	}
}
