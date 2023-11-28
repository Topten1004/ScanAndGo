import * as React from 'react'
import Image from 'next/image';
import { LanguageContext } from 'shared/context/language';

const Header = () => {

	const { userLanguage, userLanguageChange } = React.useContext(LanguageContext);

	return (
		<div className='w-full flex justify-between items-center pl-8 pr-16 py-4'>
			<Image src={'/assets/logo.png'} alt='LOGO' width={248} height={152} />
			<label className="relative inline-flex items-start cursor-pointer">
				<input type="checkbox" className="sr-only peer" onChange={() => userLanguageChange(userLanguage == 'en' ? 'fr' : 'en')} defaultChecked={userLanguage != 'en'} />
				<div className="min-w-[3.75rem] h-8 bg-red-500 peer-focus:outline-none rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-7 after:w-7 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600"></div>
				<span className="absolute font-medium text-xs uppercase right-2 text-white top-2"> Fr </span>
				<span className="absolute font-medium text-xs uppercase right-8 text-white top-2"> En </span>
			</label>
		</div>
	)
}

export default Header;