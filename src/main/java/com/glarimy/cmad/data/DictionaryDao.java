package com.glarimy.cmad.data;

import com.glarimy.cmad.api.Word;

public interface DictionaryDao {
	
	public String getMeaning(String word);
	public String getSynonym(String word);
	
	public void saveWord(Word word);
	public void updateMeaning(String word, String meaning);
	public void updateSynonym(String word, String synonym);
	
}
