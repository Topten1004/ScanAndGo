import * as React from 'react'
import Layout from 'components/layouts'
import LoginPage from 'components/pages/auth/login'

const Login = () => {
	return (
		<Layout pageTitle='RFID - LogIn' type='single'>
			<LoginPage />
		</Layout>
	)
}

export default Login