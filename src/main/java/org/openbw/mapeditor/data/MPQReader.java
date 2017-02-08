package org.openbw.mapeditor.data;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import mpq.ArchivedFileExtractor;
import mpq.ArchivedFileStream;
import mpq.HashLookup;
import mpq.MPQArchive;
import mpq.MPQException;

public class MPQReader {

	private static final Logger LOGGER = LogManager.getLogger();
	
	private String scdir;
	
	public MPQReader(String scdir) {
		this.scdir = scdir;
	}
	
	public ByteBuffer extractMpqFile(String fileName, String mpqName) throws IOException, MPQException {

		SeekableByteChannel source = FileChannel.open(new File(mpqName).toPath());
		MPQArchive archive = new MPQArchive(source);
		ArchivedFileExtractor extractor = new ArchivedFileExtractor();

		ArchivedFileStream fstream = new ArchivedFileStream(source, extractor, archive.lookupHash2(new HashLookup(fileName)));
		ByteBuffer buffer = ByteBuffer.allocate((int) fstream.size());
		int retVal = fstream.read(buffer);
		fstream.close();
		source.close();
		
		LOGGER.info("extracted file: " + fileName + ". " + retVal + " bytes read.");
		buffer.rewind();
		
		return buffer;
	}
	
	public WritableImage[] readOriginalTextures() throws IOException, MPQException {
		
		ByteBuffer cv5Buffer = extractMpqFile("tileset\\jungle.cv5", scdir);
		ByteBuffer vx4Buffer = extractMpqFile("tileset\\jungle.vx4", scdir);
		ByteBuffer vr4Buffer = extractMpqFile("tileset\\jungle.vr4", scdir);
		ByteBuffer wpeBuffer = extractMpqFile("tileset\\jungle.wpe", scdir);
		
		cv5Buffer.rewind();
		vx4Buffer.rewind();
		vr4Buffer.rewind();
		wpeBuffer.rewind();
		int index = 0;
		WritableImage tilesetImage = new WritableImage(16*32, 28 * 32);
		PixelWriter pixelWriter = tilesetImage.getPixelWriter();
		
		do {
			cv5Buffer.position(cv5Buffer.position() + 20);
			int[] tiles = new int[16];
			
			for (int i = 0; i < 16; i++) {
				tiles[i] = cv5Buffer.order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
				
				for (int j = 0; j < 16; j++) {
					int miniTile = vx4Buffer.order(ByteOrder.LITTLE_ENDIAN).getShort(tiles[i] * 32 + j * 2) & 0xFFFF;
					
					// boolean flipped = miniTile % 2 == 1;
					
					for (int k = 0; k < 64; k++) {
						int wpeReference = (vr4Buffer.get((miniTile >> 1 << 6) + k) & 0xFF) << 2;
						pixelWriter.setArgb(i * 32 + k % 8 + (j % 4) * 8, index * 32 + k / 8 + (j / 4) * 8, wpeBuffer.getInt(wpeReference) >> 8 | 0xFF000000);
					}
				}
			}
			
			index = (cv5Buffer.position() - 52) / 52;
			
		} while (cv5Buffer.remaining() > 0 && index < 28);
		
		return extractTextures(tilesetImage);
	}
	
	private WritableImage[] extractTextures(WritableImage tilesetImage) {
		
		WritableImage[] textures = new WritableImage[14];
		
		WritableImage img1 = new WritableImage(64, 64);
		PixelWriter writer = img1.getPixelWriter();
		PixelReader reader = tilesetImage.getPixelReader();
		writer.setPixels(0, 0, 32, 32, reader, 0, 0);
		writer.setPixels(32, 0, 32, 32, reader, 32, 0);
		writer.setPixels(0, 32, 32, 32, reader, 64, 0);
		writer.setPixels(32, 32, 32, 32, reader, 96, 0);
		textures[13] = img1;
		
		for (int i = 1; i < 14; i++) {
			
			WritableImage img = new WritableImage(64, 64);
			writer = img.getPixelWriter();
			writer.setPixels(0, 0, 32, 32, reader, 0, 32 * (i * 2 - 1));
			writer.setPixels(32, 0, 32, 32, reader, 0, 32 * (i * 2));
			writer.setPixels(0, 32, 32, 32, reader, 32, 32 * (i * 2 - 1));
			writer.setPixels(32, 32, 32, 32, reader, 32, 32 * (i * 2));
			textures[i - 1] = img;
		}
		
		return textures;
	}
}
