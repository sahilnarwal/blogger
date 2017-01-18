package com.glarimy.cmad.data;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

import com.glarimy.cmad.api.Word;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DictionaryMongoDAO implements DictionaryDao {
	
	@Override
	public void saveWord(Word word) {
		MongoClient mongo = new MongoClient("localhost", 27017);
		MongoDatabase db = mongo.getDatabase("dictionary-mongo");
		MongoCollection<Document> collection = db.getCollection("dictionary");
		Document document = new Document();
		document.put("word", word.getWord());
		document.put("meaning", word.getMeaning());
		document.put("synonym", word.getSynonym());
		collection.insertOne(document);
		mongo.close();
	}

	@Override
	public void updateMeaning(String word, String meaning) {
		MongoClient mongo = new MongoClient("localhost", 27017);
		MongoDatabase db = mongo.getDatabase("dictionary-mongo");
		MongoCollection<Document> collection = db.getCollection("dictionary");
		Document document = collection.find(eq("word", word)).first();
		if(document == null){
			Document newDocument = new Document();
			newDocument.put("word", word);
			newDocument.put("meaning", meaning);
			collection.insertOne(document);
		} else {
			document.put("meaning", meaning);
			//collection.insertOne(document);
		}
		mongo.close();
	}

	@Override
	public void updateSynonym(String word, String synonym) {
		MongoClient mongo = new MongoClient("localhost", 27017);
		MongoDatabase db = mongo.getDatabase("dictionary-mongo");
		MongoCollection<Document> collection = db.getCollection("dictionary");
		Document document = collection.find(eq("word", word)).first();
		if(document == null){
			Document newDocument = new Document();
			newDocument.put("word", word);
			newDocument.put("synonym", synonym);
			collection.insertOne(document);
		} else {
			document.put("synonym", synonym);
			//collection.insertOne(document);
		}
		mongo.close();
	}

	@Override
	public String getMeaning(String word) {
		MongoClient mongo = new MongoClient("localhost", 27017);
		MongoDatabase db = mongo.getDatabase("dictionary-mongo");
		MongoCollection<Document> collection = db.getCollection("dictionary");
		Document document = collection.find(eq("word", word)).first();
		String meaning = document == null ? "Not Found" :document.getString("meaning");
		mongo.close();
		return meaning;
	}

	@Override
	public String getSynonym(String word) {
		MongoClient mongo = new MongoClient("localhost", 27017);
		MongoDatabase db = mongo.getDatabase("dictionary-mongo");
		MongoCollection<Document> collection = db.getCollection("dictionary");
		Document document = collection.find(eq("word", word)).first();
		String synonym = document == null ? "Not Found" : document.getString("synonym");
		mongo.close();
		return synonym;
	}

}
