package tryPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import twitter4j.TwitterException;

/**
 *
 * @author milind
 */

public class SentimentAnalysisWithCount {

	DoccatModel model;

	// Dogru tahmin edilen negatif tweet'ler
	static ArrayList<Integer> negatifTweetIndisleri = new ArrayList<Integer>();
	// Dogru tahmin edilen pozitif tweet'ler
	static ArrayList<Integer> pozitifTweetIndisleri = new ArrayList<Integer>();
	// doðru tahminler için counter
	static int dogrular = 0;

	public static void main(String[] args)  {
		/*
		 * String line = ""; SentimentAnalysisWithCount twitterCategorizer = new
		 * SentimentAnalysisWithCount(); twitterCategorizer.trainModel(); int
		 * result1 = twitterCategorizer.classifyNewTweet("I hate Faragay");
		 * System.out.println("result1:"+result1);
		 */

		test();
		/*
		 * ConfigurationBuilder cb = new ConfigurationBuilder();
		 * cb.setDebugEnabled(true)
		 * .setOAuthConsumerKey("qHmr0bxZ4HX8c2ABFbt9mDFW4")
		 * .setOAuthConsumerSecret(
		 * "cOD3eXa5w84VNk6FZBwdnZiTA36qP4REQiv4eXJU7gnskxYnWk")
		 * .setOAuthAccessToken(
		 * "970033616804810753-bXxI8496V6zYEEft33CK2ZNBApnf8hr")
		 * .setOAuthAccessTokenSecret(
		 * "vls1DZPaQoZ0kS7VaRafvZojvSuWGqHWbitdeeZ10HBpg"); TwitterFactory tf =
		 * new TwitterFactory(cb.build()); Twitter twitter = tf.getInstance();
		 * Query query = new Query("udta punjab"); QueryResult result =
		 * twitter.search(query); int result1 = 0; for (Tweet status :
		 * result.getTweets()) {
		 * System.out.println("++++++"+status.getText().toString()); result1 =
		 * twitterCategorizer.classifyNewTweet(status.getText()); if (result1 ==
		 * 1) { positive++; } else { negative++; } }
		 * 
		 * BufferedWriter bw = new BufferedWriter(new
		 * FileWriter("C:\\Users\\1finc\\Desktop\\results.csv"));
		 * bw.write("Positive Tweets," + positive); bw.newLine();
		 * bw.write("Negative Tweets," + negative); bw.close();
		 */
	}

	static ArrayList<Integer> testValue = new ArrayList<Integer>();
	static ArrayList<String> testTweet = new ArrayList<String>();
	static int[] results = new int[200];

	public static void test() {
		// test.txt içerisindeki verileri okuyup 2 ayrý arraylist'e atýyoruz.
		oku("C:\\Users\\MBORHAN\\Desktop\\testSet.txt");

		SentimentAnalysisWithCount twitterCategorizer = new SentimentAnalysisWithCount();
		twitterCategorizer.trainModel();
		int result1 = 5555;
		if (!testValue.isEmpty() && !testTweet.isEmpty()) {
			for (int i = 0; i < testTweet.size(); i++) {
				try {
					result1 = twitterCategorizer.classifyNewTweet(testTweet.get(i));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				results[i] = result1;
			}
		}
		sonuc(results);
	}

	private static void oku(String path) {
		BufferedReader reader = null;
		String line = null;
		try {
			File file = new File(path);
			reader = new BufferedReader(new FileReader(file));
			line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();

		}

		// read all contains of file
		while (line != null) {
			testValue.add(Integer.parseInt(line.split("	")[0].trim()));
			testTweet.add(line.split("	")[1].trim());
			try {
				line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void sonuc(int[] dizi) {

		for (int i = 0; i < dizi.length; i++) {

			if (dizi[i] == testValue.get(i)) {
				dogrular++;
				if (testValue.get(i) == 0) {
					negatifTweetIndisleri.add(i);
				}
				if (testValue.get(i) == 1) {
					pozitifTweetIndisleri.add(i);
				}
			}

		}

		System.out.println("Doðru Sayýsý : " + dogrular);

		System.out.println("Doðru Tahmin Edilen Negatif Tweet Sayýsý: " + negatifTweetIndisleri.size());
		System.out.println("Doðru Tahmin Edilen Pozitif Tweet Sayýsý: " + pozitifTweetIndisleri.size());

	}

	public void trainModel() {
		InputStream dataIn = null;
		try {
			dataIn = new FileInputStream("C:\\Users\\MBORHAN\\Desktop\\trainSet.txt");
			ObjectStream lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
			ObjectStream sampleStream = new DocumentSampleStream(lineStream);

			// Specifies the minimum number of times a feature must be seen
			int cutoff = 3;
			int trainingIterations = 600;
			model = DocumentCategorizerME.train("en", sampleStream, cutoff, trainingIterations);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (dataIn != null) {
				try {
					dataIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public int classifyNewTweet(String tweet) throws IOException {
		DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);
		double[] outcomes = myCategorizer.categorize(tweet);
		String category = myCategorizer.getBestCategory(outcomes);

		System.out.print("-----------------------------------------------------\nTWEET :" + tweet + " ===> ");
		if (category.equalsIgnoreCase("1")) {
			System.out.println(" POSITIVE ");
			return 1;
		} else {
			System.out.println(" NEGATIVE ");
			return 0;
		}

	}

}
