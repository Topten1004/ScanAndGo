import * as React from 'react'
import Image from 'next/image';
import { INavItem } from 'types/interface';
import { useRouter } from 'next/router';
import { useAppDispatch, useAppSelector } from 'store/hooks';
import { logout } from 'store/slices/auth.slice';
import { LanguageContext } from 'shared/context/language';

const SideBar = () => {

	const { userLanguage, userLanguageChange, dictionary } = React.useContext(LanguageContext);
	const navList: Array<INavItem> = [
		{
			label: dictionary['cloud_words'],
			link: '/category'
		},
		{
			label: dictionary['location'],
			link: '/location'
		},
		{
			label: dictionary['name_of_item'],
			link: '/name-of-item'
		}
	]
	const router = useRouter();
	const { pathname } = router;
	const dispatch = useAppDispatch();
	const { account } = useAppSelector(state => state.auth);

	const handleSignOut = () => {
		dispatch(logout());
		router.push("/login")
	}

	return (
		<div className='flex flex-col py-4 h-screen bg-gray-300'>
			<div className='px-8'>
				<Image src={'/assets/logo.png'} alt='LOGO' width={248} height={152} />
			</div>
			<div className='flex-1 pt-8'>
				{
					account?.role == 1 && <div className={'py-2 px-8 hover:cursor-pointer hover:bg-gray-400 ' + (pathname.indexOf('/user') >= 0 ? 'bg-gray-500 text-gray-200' : '')} onClick={() => router.push('/user')}>
						{dictionary['user']}
					</div>
				}
				{
					navList.map((nav: INavItem, index: number) => (
						<div key={index} className={'py-2 px-8 hover:cursor-pointer hover:bg-gray-400 ' + (pathname.indexOf(nav.link) >= 0 ? 'bg-gray-500 text-gray-200' : '')} onClick={() => router.push(nav.link)}>
							{nav.label}
						</div>
					))
				}
			</div>
			<div className='flex justify-between px-8'>
				<label className="relative inline-flex items-start cursor-pointer">
					<input type="checkbox" className="sr-only peer" onChange={() => userLanguageChange(userLanguage == 'en' ? 'fr' : 'en')} defaultChecked={userLanguage != 'en'} />
					<div className="min-w-[3.75rem] h-8 bg-red-500 peer-focus:outline-none rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-7 after:w-7 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600"></div>
					<span className="absolute font-medium text-xs uppercase right-2 text-white top-2"> Fr </span>
					<span className="absolute font-medium text-xs uppercase right-8 text-white top-2"> En </span>
				</label>
				<p className='p-2 hover:cursor-pointer rounded-md hover:bg-red-100' onClick={handleSignOut}>{dictionary['logout']}</p>
			</div>
		</div>
	)
}

export default SideBar