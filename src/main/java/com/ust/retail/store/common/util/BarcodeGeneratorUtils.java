package com.ust.retail.store.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.oned.Code128Writer;
import com.ust.retail.store.bistro.exception.BarcodeGenerationException;

public class BarcodeGeneratorUtils {
	private static final int HEIGHT = 90;
	private static final String DEFAULT_FORMAT = "png";

	private BarcodeGeneratorUtils() {
	}

	public static byte[] generateCode128BarcodeImage(String text) {

		Map<EncodeHintType, Object> hints = Map.of(EncodeHintType.MARGIN, 0);

		int width = new Code128Writer().encode(text).length;

		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			MatrixToImageWriter.writeToStream(
					new MultiFormatWriter().encode(text, BarcodeFormat.CODE_128, width * 2, HEIGHT, hints),
					DEFAULT_FORMAT,
					byteArrayOutputStream);
			return byteArrayOutputStream.toByteArray();
		} catch (WriterException | IOException e) {
			throw new BarcodeGenerationException(e);
		}
	}

}
