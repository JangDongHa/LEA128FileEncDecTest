package kr.re.nsr.crypto.symm;

import kr.re.nsr.crypto.BlockCipher;
import kr.re.nsr.crypto.engine.LeaEngine;
import kr.re.nsr.crypto.mode.CBCMode;

public class LEA {
	private LEA() {
		throw new AssertionError();
	}

	public static final BlockCipher getEngine() {
		return new LeaEngine();
	}

	public static final class CBC extends CBCMode {
		public CBC() {
			super(getEngine());
		}
	}

}
