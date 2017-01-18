package com.glarimy.cmad.biz;

import com.glarimy.cmad.api.Dictionary;
import com.glarimy.cmad.api.Word;
import com.glarimy.cmad.data.DictionaryDao;
import com.glarimy.cmad.data.DictionaryMongoDAO;

public class SimpleDictionary implements Dictionary {
	static DictionaryDao dao = new DictionaryMongoDAO();

	@Override
	public String getMeaning(String word) {
		return dao.getMeaning(word);
	}

	@Override
	public String getSynonym(String word) {
		return dao.getSynonym(word);
	}

	@Override
	public void saveWord(Word word) {
		dao.saveWord(word);
	}

	@Override
	public void updateMeaning(Word word) {
		dao.updateMeaning(word.getWord(), word.getMeaning());
	}

	@Override
	public void updateSynonym(Word word) {
		dao.updateSynonym(word.getWord(), word.getSynonym());
	}

}
