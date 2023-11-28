import React, {useContext, useState} from 'react'
import { useRouter } from 'next/router'
import useLoginForm from 'components/auth/useLoginForm'
import { LanguageContext } from 'shared/context/language'

interface ILogIn {
  username?: string
  password?: string
}

const LoginPage = () => {

  const classes = {
    label: 'block mb-2 text-sm font-medium text-gray-900 dark:text-white',
    input: 'bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:border-blue-500 block w-80 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:border-blue-500 focus:outline-none',
    button: 'text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:outline-none font-medium rounded-lg text-sm w-32 py-2.5 text-center'
  }
  const { dictionary } = useContext(LanguageContext);
  const router = useRouter();
  const [info, setInfo] = useState<ILogIn>();

  const { login } = useLoginForm();

  const handleChange = (e: any) => {
    setInfo({
      ...info,
      [e.target.name]: e.target.value
    })
  }

  const hanldeLogIn = async (e: any) => {
    if (info?.username && info.password)
      login({username: info?.username, password: info?.password});
  }

  return (
    <div className='flex justify-center'>
      <div className='w-fit px-16 py-8 shadow-custom space-y-8 rounded-xl'>
        <div>
          <label htmlFor="username" className={classes.label}>{dictionary['username']}</label>
          <input type="text" id="username" name="username" className={classes.input} placeholder={dictionary["username_placeholder"]} onChange={handleChange} />
        </div>
        <div>
          <label htmlFor="password" className={classes.label}>{dictionary['password']}</label>
          <input type="password" id="password" name="password" className={classes.input} placeholder={dictionary["password_placeholder"]} onChange={handleChange} />
        </div>
        <div className='flex items-center justify-center gap-1'>
          <p className={classes.label}>{dictionary['login_description']}</p>
          <p className={classes.label + " !text-blue-500 hover:cursor-pointer"} onClick={() => router.push('/register')}>{dictionary['register']}</p>
        </div>
        <div className='flex justify-center'>
          <button type="button" className={classes.button} onClick={hanldeLogIn}>{dictionary['login']}</button>
        </div>
      </div>
    </div>
  )
}

export default LoginPage;