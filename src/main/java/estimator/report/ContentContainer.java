package estimator.report;

import java.awt.image.BufferedImage;

import com.itextpdf.text.Chunk;

public class ContentContainer {
	private String name;
	private BufferedImage bufferedImage;
	private Chunk[] content;
	
	public ContentContainer(String name, BufferedImage bi, Chunk[] content){
		this.name=name;
		this.bufferedImage=bi;
		this.content=content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}
	
	public Chunk[] getContent() {
		return content;
	}

	public void setContent(Chunk[] content) {
		this.content = content;
	}
}
