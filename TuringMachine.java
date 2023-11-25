/*

We need an array which is the tape

Then we read the tape

We need to be able to read a symbol and do something based on the instruction

Need a concept of "state", instruction, instruction table, etc.

State	/ Symbol Read	/ Write Instruction	/ Move Instruction / Next State

*/

////// State Table /////////////

import java.util.ArrayList;
import java.util.Arrays;

class InstructionSet {
	public InstructionSet(Character _symbolRead, Character _writeInstruction, Integer _moveInstruction, Integer _nextState) {
		symbolRead = _symbolRead;
		writeInstruction = _writeInstruction;
		moveInstruction = _moveInstruction;
		nextState = _nextState;
	}

	Character symbolRead = null;
	Character writeInstruction = null;
	Integer moveInstruction = null;
	Integer nextState = null;
}

class State {
	public ArrayList<InstructionSet> instructionSets = new ArrayList<>();

	public State(InstructionSet _instructionSet1, InstructionSet _instructionSet2,
			InstructionSet _instructionSet3) {
		instructionSets.add(_instructionSet1);
		instructionSets.add(_instructionSet2);
		instructionSets.add(_instructionSet3);
	}

}

class StateTable {
	public ArrayList<State> states = new ArrayList<>();
}

///////////// Turing Machine ///////////////////

class Tape {
	public Tape(ArrayList<Character> _symbols) {
		symbols = _symbols;
	}

	public void PrintTape(int tapeIndex) {
		for (int i = 0; i < symbols.size(); i++) {
			if (i == tapeIndex) {
				System.out.print("*");
				System.out.print(symbols.get(i));
				System.out.print("*");
			} else {
				System.out.print(" ");
				System.out.print(symbols.get(i));
				System.out.print(" ");
			}
		}
	}

	public ArrayList<Character> symbols;
}

class TuringMachine {
	// Constructor
	TuringMachine(Tape _tape, StateTable _stateTable) {
		tape = _tape;
		stateTable = _stateTable;
	}

	public void run() {
		int tapeIndex = 1;

		State currentState = stateTable.states.get(0);
		boolean running = true;
		while (running) {
			// Print state
			System.out.print("State: ");
			System.out.print(stateTable.states.indexOf(currentState));
			System.out.print("   ");

			// Print tape
			System.out.print("Tape: ");
			tape.PrintTape(tapeIndex);
			System.out.print("    ");

			// Get symbol
			int tapeSymbol = tape.symbols.get(tapeIndex);

			// Check instructions set for symbol
			for (InstructionSet instructionSet : currentState.instructionSets) {
				if (instructionSet.symbolRead == tapeSymbol) {

					// Execute write instruction
					System.out.print("Output: ");
					System.out.println(instructionSet.writeInstruction);
					tape.symbols.set(tapeIndex, instructionSet.writeInstruction);
					tapeIndex += instructionSet.moveInstruction;

					// State transition
					Integer nextStateIndex = instructionSet.nextState;
					if (nextStateIndex == null) {
						running = false;
					} else {
						currentState = stateTable.states.get(nextStateIndex);
					}

					// Exit
					break;
				}
			}
		}
	}

	Tape tape;
	StateTable stateTable;
}

///////////// Main Code ///////////////////////

class Main {
	public static void main(String[] args) {
		System.out.println("Operation of Addition using Turing Machine");

		//////////////////////////////// States ////////////////////////////

		// Subtraction states
		State readLeftToRightState = new State(
				new InstructionSet('B', 'B', -1, 1),
				new InstructionSet('0', '0', 1, 0),
				new InstructionSet('1', '1', 1, 0));

		State subtractionReplaceState = new State(
				new InstructionSet('B', 'B', 1, 6),
				new InstructionSet('0', '1', -1, 1),
				new InstructionSet('1', '0', -1, 2));

		State jumpLeftToRightState = new State(
				new InstructionSet('B', 'B', 1, 3),
				new InstructionSet('0', '0', 1, 2),
				new InstructionSet('1', '1', 1, 2));

		// Addition states
		State readLeftToRightState2 = new State(
				new InstructionSet('B', 'B', -1, 4),
				new InstructionSet('0', '0', 1, 3),
				new InstructionSet('1', '1', 1, 3));

		State additionReplaceState = new State(
				new InstructionSet('B', '1', 1, 5),
				new InstructionSet('0', '1', -1, 5),
				new InstructionSet('1', '0', -1, 4));

		State jumptoLeftState = new State(
				new InstructionSet('B', 'B', -1, 1),
				new InstructionSet('0', '0', -1, 5),
				new InstructionSet('1', '1', -1, 5));

		// Cleanup state into halt
		State cleanupState = new State(
      new InstructionSet('B', '0', 0, null),
			new InstructionSet('0', '0', 1, 6),
			new InstructionSet('1', '0', 1, 6));

		//////////////////////////////// State table ////////////////////////////

		// State table
		StateTable stateTable = new StateTable();
		stateTable.states.add(readLeftToRightState);
		stateTable.states.add(subtractionReplaceState);
		stateTable.states.add(jumpLeftToRightState);
		stateTable.states.add(readLeftToRightState2);
		stateTable.states.add(additionReplaceState);
		stateTable.states.add(jumptoLeftState);
		stateTable.states.add(cleanupState);

		//////////////////////////////// Turing Machine ////////////////////////////

		// Turing machine
		Tape tape = new Tape(new ArrayList<Character>(Arrays.asList('B', '0', '0', '0', '1', 'B', '0', '0', '1', '0', 'B')));
		TuringMachine turingMachine = new TuringMachine(tape, stateTable);
		turingMachine.run();

		// Print output
		System.out.print("Final output:    ");
		tape.PrintTape(-1);
	}
}

/*
 * 7 + 6 = 13
 * 0111 0110
 * 0110 0111
 * 0101 1000
 * 0100 1001
 * 0011 1010
 * 0010 1011
 * 0001 1100
 * 0000 1101
 */

/* 
* 1 + 2 = 3
0001 + 0010
0011
*/