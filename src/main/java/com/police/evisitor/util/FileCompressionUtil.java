package com.police.evisitor.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileCompressionUtil {

	private static final long KB = 1024;
	private static final long MB = 1024 * KB;

	public byte[] compressIfRequired(MultipartFile file) throws IOException {
		long fileSize = file.getSize();
		long targetSize = getTargetSize(fileSize);

		if (targetSize >= fileSize) {
			return file.getBytes();
		}

		String contentType = file.getContentType();

		if (contentType != null && contentType.startsWith("image/")) {
			return compressImage(file.getBytes(), targetSize, contentType);
		}

		if ("application/pdf".equalsIgnoreCase(contentType)) {
			return compressPdf(file.getBytes(), targetSize);
		}

		return file.getBytes();
	}

	private long getTargetSize(long fileSize) {
		if (fileSize >= 5 * MB)
			return 200 * KB;
		if (fileSize > 2 * MB)
			return 180 * KB;
		if (fileSize > 1 * MB)
			return 160 * KB;
		if (fileSize > 300 * KB)
			return 140 * KB;
		return fileSize;
	}

	private byte[] compressImage(byte[] imageBytes, long targetSize, String contentType) throws IOException {

		BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
		if (originalImage == null)
			return imageBytes;

		boolean isPng = "image/png".equalsIgnoreCase(contentType);

		BufferedImage rgbImage = toRgbImage(originalImage);

		float startQuality = isPng ? 0.75f : 0.85f;
		float quality = startQuality;

		while (quality > 0.05f) {
			byte[] compressed = encodeAsJpeg(rgbImage, quality);
			if (compressed.length <= targetSize) {
				return compressed;
			}
			quality -= 0.05f;
		}

		double scale = isPng ? 0.8 : 0.9;
		double minScale = isPng ? 0.1 : 0.2;

		while (scale >= minScale) {
			int newWidth = (int) (rgbImage.getWidth() * scale);
			int newHeight = (int) (rgbImage.getHeight() * scale);

			BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = resized.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, newWidth, newHeight);
			g.drawImage(rgbImage, 0, 0, newWidth, newHeight, null);
			g.dispose();

			float scaleQuality = isPng ? 0.08f : 0.10f;
			byte[] compressed = encodeAsJpeg(resized, scaleQuality);
			if (compressed.length <= targetSize) {
				return compressed;
			}
			scale -= isPng ? 0.1 : 0.1;
		}

		return encodeAsJpeg(rgbImage, 0.05f);
	}

	private BufferedImage toRgbImage(BufferedImage image) {
		if (image.getType() == BufferedImage.TYPE_INT_RGB) {
			return image;
		}

		BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

		Graphics2D g = rgbImage.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return rgbImage;
	}

	private byte[] compressPdf(byte[] pdfBytes, long targetSize) throws IOException {

		try (PDDocument document = PDDocument.load(pdfBytes)) {

			document.setAllSecurityToBeRemoved(true);

			List<ImageEntry> imageEntries = extractAllImages(document);

			float[] qualities = { 0.75f, 0.60f, 0.45f, 0.30f, 0.20f, 0.10f, 0.05f };

			byte[] result = null;

			for (float quality : qualities) {
				replaceAllImagesParallel(document, imageEntries, quality);

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				document.save(outputStream);
				result = outputStream.toByteArray();

				if (result.length <= targetSize) {
					return result;
				}
			}

			return result;
		}
	}

	private void replaceAllImagesParallel(PDDocument document, List<ImageEntry> entries, float quality)
			throws IOException {

		double scale = 1.0;
		if (quality <= 0.30f)
			scale = 0.5;
		else if (quality <= 0.50f)
			scale = 0.7;

		final double finalScale = scale;

		int threadCount = Math.min(entries.size(), Runtime.getRuntime().availableProcessors());
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);

		try {
			List<Future<byte[]>> futures = new ArrayList<>();

			for (ImageEntry entry : entries) {
				futures.add(executor.submit(() -> {
					int newWidth = (int) (entry.originalImage.getWidth() * finalScale);
					int newHeight = (int) (entry.originalImage.getHeight() * finalScale);

					BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
					Graphics2D g = resized.createGraphics();
					g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					g.setColor(Color.WHITE);
					g.fillRect(0, 0, newWidth, newHeight);
					g.drawImage(entry.originalImage, 0, 0, newWidth, newHeight, null);
					g.dispose();

					return encodeAsJpeg(resized, quality);
				}));
			}

			for (int i = 0; i < entries.size(); i++) {
				try {
					byte[] jpegBytes = futures.get(i).get();
					ImageEntry entry = entries.get(i);
					PDResources resources = entry.page.getResources();
					if (resources == null)
						continue;

					PDImageXObject compressed = PDImageXObject.createFromByteArray(document, jpegBytes,
							entry.name.getName());
					resources.put(entry.name, compressed);

				} catch (InterruptedException | ExecutionException e) {
					Thread.currentThread().interrupt();
					throw new IOException("Image compression failed", e);
				}
			}

		} finally {
			executor.shutdown();
			try {
				if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
					executor.shutdownNow();
				}
			} catch (InterruptedException e) {
				executor.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}
	}

	private byte[] encodeAsJpeg(BufferedImage image, float quality) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
		ImageWriteParam param = writer.getDefaultWriteParam();
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(quality);

		try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
			writer.setOutput(ios);
			writer.write(null, new IIOImage(image, null, null), param);
		} finally {
			writer.dispose();
		}

		return baos.toByteArray();
	}

	private static class ImageEntry {
		COSName name;
		PDPage page;
		BufferedImage originalImage;

		ImageEntry(COSName name, PDPage page, BufferedImage originalImage) {
			this.name = name;
			this.page = page;
			this.originalImage = originalImage;
		}
	}

	private List<ImageEntry> extractAllImages(PDDocument document) throws IOException {
		List<ImageEntry> entries = new ArrayList<>();
		for (PDPage page : document.getPages()) {
			PDResources resources = page.getResources();
			if (resources == null)
				continue;
			for (COSName name : resources.getXObjectNames()) {
				try {
					PDXObject xObject = resources.getXObject(name);
					if (!(xObject instanceof PDImageXObject))
						continue;
					BufferedImage image = ((PDImageXObject) xObject).getImage();
					if (image != null) {
						entries.add(new ImageEntry(name, page, image));
					}
				} catch (IOException e) {
				}
			}
		}
		return entries;
	}

}
