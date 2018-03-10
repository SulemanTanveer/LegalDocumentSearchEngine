package wordCloudPackage;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.print.DocFlavor.STRING;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.LayeredWordCloud;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.FontWeight;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import com.kennycason.kumo.palette.LinearGradientColorPalette;

import servletPackage.Servlet;

public class WordCloudGenerator {
	private static FrequencyAnalyzer frequencyAnalyzer;
	private static List<WordFrequency> wordFrequencies;
	private static Dimension dimension;
	private static WordCloud wordCloud;
	private static String textForCloud;

	public WordCloudGenerator() {

		
		try {
			frequencyAnalyzer = new FrequencyAnalyzer();
			//wordFrequencies = frequencyAnalyzer.load("Preprocessing/TextForCloud.txt");
			dimension = new Dimension(600, 600);
			wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	public void generateWordCloud() {
		try {
			System.out.println("wordCloudGenerator Called");
			frequencyAnalyzer.setWordFrequenciesToReturn(200);
			frequencyAnalyzer.setMinWordLength(4);
			List<String> wordCloudTextList = new ArrayList<String>();
			wordCloudTextList.add(textForCloud);
			wordFrequencies = frequencyAnalyzer.load(wordCloudTextList);
			wordCloud.setPadding(2);
			wordCloud.setBackgroundColor(Color.white);
			wordCloud.setBackground(new CircleBackground(300));
			wordCloud.setColorPalette(new LinearGradientColorPalette(Color.BLACK, Color.GRAY, Color.DARK_GRAY, 30, 30));
			wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
			wordCloud.build(wordFrequencies);
			wordCloud.writeToFile("D:/Java Neon/workspace/LegalDocSearchEngine/WebContent/WordCloud/Cloud.png");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void setCloudText(String text){
		textForCloud = text;
	}
	
}
