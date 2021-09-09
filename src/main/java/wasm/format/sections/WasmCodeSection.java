package wasm.format.sections;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ghidra.app.util.bin.BinaryReader;
import ghidra.app.util.bin.format.dwarf4.LEB128;
import ghidra.util.exception.DuplicateNameException;
import wasm.format.StructureBuilder;
import wasm.format.sections.structures.WasmFunctionBody;

public class WasmCodeSection extends WasmSection {

	private LEB128 count;
	private List<WasmFunctionBody> functions = new ArrayList<WasmFunctionBody>();

	public WasmCodeSection(BinaryReader reader) throws IOException {
		super(reader);
		count = LEB128.readUnsignedValue(reader);
		for (int i = 0; i < count.asLong(); ++i) {
			functions.add(new WasmFunctionBody(reader));
		}
	}

	public List<WasmFunctionBody> getFunctions() {
		return Collections.unmodifiableList(functions);
	}

	@Override
	protected void addToStructure(StructureBuilder builder) throws DuplicateNameException, IOException {
		builder.add(count, "count");
		for (int i = 0; i < functions.size(); i++) {
			builder.add(functions.get(i), "function_" + i);
		}
	}

	@Override
	public String getName() {
		return ".code";
	}
}
