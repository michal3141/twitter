package config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import enums.Relation;
import enums.Strategy;
import exceptions.InvalidConfigException;

/**
 * Parses XML configuration to get CrawlerConfiguration object and vice versa
 * @author Micha³
 *
 */
public class ConfigParser {
	public static CrawlerConfiguration fromXML(String filepath) {
		
		CrawlerConfiguration config = new CrawlerConfiguration();
		try {
			File fXmlFile = new File(filepath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
			String seed = doc.getElementsByTagName("seed").item(0).getFirstChild().getNodeValue();
			config.setSeed(seed);
			
			int depth = 
					Integer.parseInt(doc.getElementsByTagName("depth").item(0).getFirstChild().getNodeValue());
			config.setDepth(depth);
			
			int hitsPerHour = 
					Integer.parseInt(doc.getElementsByTagName("hitsPerHour").item(0).getFirstChild().getNodeValue());
			config.setHitsPerHour(hitsPerHour);
			
			int crawlTime = 
					Integer.parseInt(doc.getElementsByTagName("crawlTime").item(0).getFirstChild().getNodeValue());
			config.setCrawlTime(crawlTime);
			
			String strategy = doc.getElementsByTagName("strategy").item(0).getFirstChild().getNodeValue();
			if (strategy.equals("BREADTH_FIRST")) {
				config.setStrategy(Strategy.BREADTH_FIRST);
			} else if (strategy.equals("DEPTH_FIRST")) {
				config.setStrategy(Strategy.DEPTH_FIRST);
			} else if (strategy.equals("KEYWORDS")) {
				config.setStrategy(Strategy.KEYWORDS);
			} else {
				throw new InvalidConfigException("Unknown strategy type: " + strategy);
			}
			
			Element relationsElem = (Element) doc.getElementsByTagName("relations").item(0);
			String follows = relationsElem.getElementsByTagName("follows").item(0).getFirstChild().getNodeValue();
			String followedBy = relationsElem.getElementsByTagName("followedBy").item(0).getFirstChild().getNodeValue();
			String repliesTo = relationsElem.getElementsByTagName("repliesTo").item(0).getFirstChild().getNodeValue();
			String mentions = relationsElem.getElementsByTagName("mentions").item(0).getFirstChild().getNodeValue();
			String hasTweets = relationsElem.getElementsByTagName("hasTweets").item(0).getFirstChild().getNodeValue();
			String retweets = relationsElem.getElementsByTagName("retweets").item(0).getFirstChild().getNodeValue();
			
			Set<Relation> relations = new HashSet<>();
			if (follows.equals("true")) {
				relations.add(Relation.FOLLOWS);
			} else if (! follows.equals("false")) {
				throw new InvalidConfigException("Invalid boolean value : " + follows + ". Has to be true or false.");
			}
			
			if (followedBy.equals("true")) {
				relations.add(Relation.FOLLOWED_BY);
			} else if (! followedBy.equals("false")) {
				throw new InvalidConfigException("Invalid boolean value : " + followedBy + ". Has to be true or false.");
			}
			
			if (repliesTo.equals("true")) {
				relations.add(Relation.REPLIES_TO);
			} else if (! repliesTo.equals("false")) {
				throw new InvalidConfigException("Invalid boolean value : " + repliesTo + ". Has to be true or false.");
			}
			
			if (mentions.equals("true")) {
				relations.add(Relation.MENTIONS);
			} else if (! mentions.equals("false")) {
				throw new InvalidConfigException("Invalid boolean value : " + mentions + ". Has to be true or false.");
			}
			
			if (hasTweets.equals("true")) {
				relations.add(Relation.HAS_TWEETS);
			} else if (! hasTweets.equals("false")) {
				throw new InvalidConfigException("Invalid boolean value : " + hasTweets + ". Has to be true or false.");
			}
			
			if (retweets.equals("true")) {
				relations.add(Relation.RETWEETS);
			} else if (! retweets.equals("false")) {
				throw new InvalidConfigException("Invalid boolean value : " + retweets + ". Has to be true or false.");
			}
			
			config.setRelations(relations);
					
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigException e) {
			e.printStackTrace();
		}
		return config;
	}
	
	public static void toXML(CrawlerConfiguration config, String filepath) {
		try {
			File fXmlFile = new File(filepath);
			initializeXMLFile(fXmlFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
			Node root = doc.getFirstChild();
			
			Element seedElem = doc.createElement("seed");
			seedElem.appendChild(doc.createTextNode(config.getSeed()));
			root.appendChild(seedElem);
			
			Element depthElem = doc.createElement("depth");
			depthElem.appendChild(doc.createTextNode(String.valueOf(config.getDepth())));
			root.appendChild(depthElem);
			
			Element hitsPerHourElem = doc.createElement("hitsPerHour");
			hitsPerHourElem.appendChild(doc.createTextNode(String.valueOf(config.getHitsPerHour())));
			root.appendChild(hitsPerHourElem);

			Element crawlTimeElem = doc.createElement("crawlTime");
			crawlTimeElem.appendChild(doc.createTextNode(String.valueOf(config.getCrawlTime())));
			root.appendChild(crawlTimeElem);
			
			Element strategyElem = doc.createElement("strategy");
			strategyElem.appendChild(doc.createTextNode(config.getStrategy().toString()));
			root.appendChild(strategyElem);
			
			Element relationsElem = doc.createElement("relations");
			
			Element followsElem = doc.createElement("follows");
			if (config.getRelations().contains(Relation.FOLLOWS)) {
				followsElem.appendChild(doc.createTextNode("true"));
			} else {
				followsElem.appendChild(doc.createTextNode("false"));
			}
			relationsElem.appendChild(followsElem);
			
			Element followedByElem = doc.createElement("followedBy");
			if (config.getRelations().contains(Relation.FOLLOWED_BY)) {
				followedByElem.appendChild(doc.createTextNode("true"));
			} else {
				followedByElem.appendChild(doc.createTextNode("false"));
			}
			relationsElem.appendChild(followedByElem);
			
			Element repliesToElem = doc.createElement("repliesTo");
			if (config.getRelations().contains(Relation.REPLIES_TO)) {
				repliesToElem.appendChild(doc.createTextNode("true"));
			} else {
				repliesToElem.appendChild(doc.createTextNode("false"));
			}
			relationsElem.appendChild(repliesToElem);
			
			Element mentionsElem = doc.createElement("mentions");
			if (config.getRelations().contains(Relation.MENTIONS)) {
				mentionsElem.appendChild(doc.createTextNode("true"));
			} else {
				mentionsElem.appendChild(doc.createTextNode("false"));
			}
			relationsElem.appendChild(mentionsElem);
			
			Element hasTweetsElem = doc.createElement("hasTweets");
			if (config.getRelations().contains(Relation.HAS_TWEETS)) {
				hasTweetsElem.appendChild(doc.createTextNode("true"));
			} else {
				hasTweetsElem.appendChild(doc.createTextNode("false"));
			}
			relationsElem.appendChild(hasTweetsElem);
			
			root.appendChild(relationsElem);
			
			Element retweetsElem = doc.createElement("retweets");
			if (config.getRelations().contains(Relation.RETWEETS)) {
				retweetsElem.appendChild(doc.createTextNode("true"));
			} else {
				retweetsElem.appendChild(doc.createTextNode("false"));
			}
			relationsElem.appendChild(retweetsElem);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(fXmlFile);
			transformer.transform(source, result);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	private static void initializeXMLFile(File f) throws IOException {
		System.out.println("Creating XML Configuration File.");	
		FileWriter fw = new FileWriter(f.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n");
		bw.write("<config>" + "\n");
		bw.write("</config>" + "\n");
		bw.close();
		fw.close();				
	}
}
