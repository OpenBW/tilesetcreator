package org.openbw.mapeditor.data;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;

import mpq.ArchivedFileExtractor;
import mpq.ArchivedFileStream;
import mpq.HashLookup;
import mpq.MPQArchive;
import mpq.MPQException;

public class MPQReader {

	public ByteBuffer extractMpqFile(String fileName, String mpqName) throws IOException, MPQException {

		SeekableByteChannel source = FileChannel.open(new File(mpqName).toPath());
		MPQArchive archive = new MPQArchive(source);
		ArchivedFileExtractor extractor = new ArchivedFileExtractor();

		ArchivedFileStream fstream = new ArchivedFileStream(source, extractor, archive.lookupHash2(new HashLookup(fileName)));
		ByteBuffer buffer = ByteBuffer.allocate((int) fstream.size());
		int retVal = fstream.read(buffer);
		fstream.close();
		source.close();
		
		System.out.println("extracted file: " + fileName + ". " + retVal + " bytes read.");
		buffer.rewind();
		
		return buffer;
	}
	
	public void readOriginalTextures(Tileset tileset) throws IOException, MPQException {
		
		ByteBuffer buffer = extractMpqFile("tileset\\jungle.cv5", "C:\\Users\\Andreas\\Documents\\starcraft\\StarDat.mpq");
		
		boolean done = false;
		
		do {
			buffer.position(buffer.position() + 20);
			short[] tiles = new short[16];
			for (int i = 0; i < 16; i++) {
				tiles[i] = buffer.order(ByteOrder.LITTLE_ENDIAN).getShort();
				System.out.println(tiles[i]);
			}
			
		} while (!done);
	}
	
	public static void main(String[] args) throws Exception {
		
		MPQReader reader = new MPQReader();
		reader.readOriginalTextures(new Tileset());
	}
}
