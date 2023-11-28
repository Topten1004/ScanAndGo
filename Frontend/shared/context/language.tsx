import React, { useState, createContext , useContext } from 'react';

import { languageOptions, dictionaryList } from 'static/languages';

// create the language context with default selected language
export const LanguageContext = createContext({
  userLanguage: 'en',
  dictionary: dictionaryList.en,
  userLanguageChange: (params: any) => {}
});

// it provides the language context to app
export function LanguageProvider({ children }: any) {
  
  const [userLanguage, setUserLanguage] = useState('en');

  const provider = {
    
    userLanguage,
    
    dictionary: dictionaryList[userLanguage],
    
    userLanguageChange  : (selected: any) => {

      const newLanguage = languageOptions[selected] ? selected : 'en'
      setUserLanguage(newLanguage);
      window.localStorage.setItem('rcml-lang', newLanguage);
    
    }
  
  };

  return (
    <LanguageContext.Provider value={provider}>
      {children}
    </LanguageContext.Provider>
  );
};

// get text according to id & current language
export function Text({ tid }: any) {
  const languageContext = useContext(LanguageContext);

  return languageContext.dictionary[tid] || tid;
};
