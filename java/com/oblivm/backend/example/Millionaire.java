package com.oblivm.backend.example;

import com.oblivm.backend.circuits.arithmetic.IntegerLib;
import com.oblivm.backend.flexsc.CompEnv;
import com.oblivm.backend.gc.BadLabelException;
import com.oblivm.backend.util.EvaRunnable;
import com.oblivm.backend.util.GenRunnable;
import com.oblivm.backend.util.Utils;

public class Millionaire {


	static public<T> T compute(CompEnv<T> gen, T[] inputA, T[] inputB){
		return new IntegerLib<T>(gen).geq(inputA, inputB);
	}

	public static class Generator<T> extends GenRunnable<T> {

		T[] inputA;
		T[] inputB;
		T scResult;

		@Override
		public void prepareInput(CompEnv<T> gen) {
			inputA = gen.inputOfAlice(Utils.fromInt(new Integer(args[1]), 32));
			gen.flush();
			inputB = gen.inputOfBob(new boolean[32]);
		}

		@Override
		public void secureCompute(CompEnv<T> gen) {

			scResult = compute(gen, inputA, inputB);
		}

		@Override
		public void prepareOutput(CompEnv<T> gen) throws BadLabelException {
			System.out.println(gen.outputToAlice(scResult));
			gen.outputToBob(scResult);
		}
	}

	public static class Evaluator<T> extends EvaRunnable<T> {
		T[] inputA;
		T[] inputB;
		T scResult;

		@Override
		public void prepareInput(CompEnv<T> eva) {
			inputA = eva.inputOfAlice(new boolean[32]);
			eva.flush();
			inputB = eva.inputOfBob(Utils.fromInt(new Integer(args[1]), 32));
		}

		@Override
		public void secureCompute(CompEnv<T> eva) {

			scResult = compute(eva, inputA, inputB);
		}

		@Override
		public void prepareOutput(CompEnv<T> eva) throws BadLabelException {

			eva.outputToAlice(scResult);
			System.out.println(eva.outputToBob(scResult));

		}
	}
}
