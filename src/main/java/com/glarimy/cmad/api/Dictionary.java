package com.glarimy.cmad.api;

public interface Dictionary {

	String getMeaning(String word);
	
	String getSynonym(String word);
	
	void saveWord(Word word);
	
	void updateMeaning(Word word);
	
	void updateSynonym(Word word);
}
