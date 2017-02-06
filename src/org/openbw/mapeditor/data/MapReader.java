package org.openbw.mapeditor.data;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import mpq.MPQException;

public class MapReader {

	public int[] readMap() throws IOException, MPQException {

		int[] tiles = new int[128 * 128];

		MPQReader mpqReader = new MPQReader();
		ByteBuffer buffer = mpqReader.extractMpqFile("staredit\\scenario.chk", "fighting_spirit.scx");

		String sectionName = "";
		int sectionLength = 0;
		do {
			buffer.position(buffer.position() + sectionLength);

			byte[] section = new byte[4];
			buffer.get(section, 0, 4);
			sectionName = new String(section, "UTF-8");

			sectionLength = buffer.order(ByteOrder.LITTLE_ENDIAN).getInt();

			System.out.println(sectionName + ": " + sectionLength);

		} while (!"MTXM".equals(sectionName));

		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = buffer.order(ByteOrder.LITTLE_ENDIAN).getShort();
		}

		return tiles;
	}
}
