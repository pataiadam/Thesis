package estimator.report;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.collections15.Transformer;

import championship.MatchModel;
import championship.TeamModel;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import estimator.EstimateModel;
import estimator.Estimator;
import estimator.IRankingMetodh;

public class EstimatorReport {

	private Estimator estimator;
	private ArrayList<ContentContainer> cc = new ArrayList<ContentContainer>();

	public void printReportToConsole(Estimator estimator, String name) {
		this.estimator = estimator;
		 System.out.println(name);
		for (EstimateModel e : estimator.getEstimations()) {
			System.out.println(e);
		}

		System.out.println("Accuracy rate w/ draw: "
				+ estimator.getCorrectionRateWDraw());
		System.out.println("Accuracy rate w/o draw: "
				+ estimator.getCorrectionRateWODraw());

	}

	public void addToPdfDocument(Estimator estimator, String name) {
		this.estimator = estimator;
		String content = "";
		Chunk[] contents = new Chunk[estimator.getEstimations().size() + 3];
		int i = 0;
		for (EstimateModel e : estimator.getEstimations()) {
			content += e + "\n";
			BaseColor color = BaseColor.BLACK;
			if (e.isItCorrectEstimation(false)) {
				color = BaseColor.GREEN;
			}
			Font font = new Font(FontFamily.TIMES_ROMAN, 11, Font.NORMAL, color);
			contents[i] = new Chunk(e + "", font);
			i++;
		}
		BaseColor color = BaseColor.BLACK;
		contents[i] = new Chunk("Correction rate w/ draw: "
				+ estimator.getCorrectionRateWDraw());
		contents[i + 1] = new Chunk("Correction rate w/o draw: "
				+ estimator.getCorrectionRateWODraw());
		contents[i + 2] = new Chunk("Entropy: " + estimator.getEntropy());

		content += "Accuracy rate w/ draw: "
				+ estimator.getCorrectionRateWDraw() + "\n";
		content += "Accuracy rate w/o draw: "
				+ estimator.getCorrectionRateWODraw() + "\n";
		addContent(estimator.getRanking(), name, contents);
	}

	private void addContent(IRankingMetodh ranking, String name,
			Chunk[] contents) {
		Graph<TeamModel, MatchModel> svg = ranking.getGraph();
		Layout<TeamModel, MatchModel> layout = new CircleLayout(svg);
		layout.setSize(new Dimension(1000, 1000));

		VisualizationImageServer<TeamModel, MatchModel> vis = new VisualizationImageServer<TeamModel, MatchModel>(
				layout, layout.getSize());

		vis.setBackground(Color.WHITE);
		vis.getRenderContext().setEdgeLabelTransformer(
				new Transformer<MatchModel, String>() {

					@Override
					public String transform(MatchModel match) {
						return match.getAbsDiff() + "";
					}
				});
		vis.getRenderContext().setEdgeShapeTransformer(
				new EdgeShape.Line<TeamModel, MatchModel>());
		vis.getRenderContext().setVertexLabelTransformer(
				new ToStringLabeller<TeamModel>());
		vis.getRenderer().getVertexLabelRenderer()
				.setPosition(Renderer.VertexLabel.Position.CNTR);

		Transformer<TeamModel, Paint> vertexColor = new Transformer<TeamModel, Paint>() {
			@Override
			public Paint transform(TeamModel s) {
				double sa = ranking.getRankings().get(s) * 10;
				return Color.getHSBColor(0.33f, sa > 1 ? 1 : (float) sa, 1);
			}
		};

		Transformer<TeamModel, Shape> vertexSize = new Transformer<TeamModel, Shape>() {
			@Override
			public Shape transform(TeamModel s) {
				Ellipse2D circle = new Ellipse2D.Double(-30, -30, 60, 60);
				double size = ranking.getRankings().get(s);
				return AffineTransform.getScaleInstance(size * 30, size * 30)
						.createTransformedShape(circle);

			}
		};
		Transformer<TeamModel, String> vertexLabel = new Transformer<TeamModel, String>() {
			@Override
			public String transform(TeamModel s) {
				return s.getName();
			}
		};
		;
		vis.getRenderContext().setVertexFillPaintTransformer(vertexColor);
		vis.getRenderContext().setVertexShapeTransformer(vertexSize);
		vis.getRenderContext().setVertexLabelTransformer(vertexLabel);

		BufferedImage image = (BufferedImage) vis.getImage(new Point2D.Double(
				vis.getGraphLayout().getSize().getWidth() / 2, vis
						.getGraphLayout().getSize().getHeight() / 2),
				new Dimension((int) vis.getGraphLayout().getSize().getWidth(),
						(int) vis.getGraphLayout().getSize().getWidth()));

		cc.add(new ContentContainer(name, image, contents));

	}

	public void writeToPDF(String filename) {
		try {
			this.createPdf(filename, cc);
		} catch (IOException | DocumentException e) {

			e.printStackTrace();
		}
	}

	private void createPdf(String filename, ArrayList<ContentContainer> content)
			throws IOException, DocumentException {
		Document document = new Document();

		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream(filename + ".pdf"));
		document.open();

		PdfContentByte pdfCB = new PdfContentByte(writer);
		for (ContentContainer c : content) {
			Image image = Image.getInstance(pdfCB, c.getBufferedImage(), 1);
			image.scaleToFit(400, 400);
			document.add(new Paragraph(c.getName()));
			document.add(image);
			for (Chunk ch : c.getContent()) {
				document.add(ch);
				document.add(Chunk.NEWLINE);
			}
			document.newPage();
		}
		document.close();
	}

}
