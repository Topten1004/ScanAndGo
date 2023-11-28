import * as React from 'react'
import Layout from 'components/layouts'
import RegisterPage from 'components/pages/auth/register'

const Register = () => {
	return (
		<Layout pageTitle='RFID - LogIn' type='single'>
			<RegisterPage />
		</Layout>
	)
}

export default Register