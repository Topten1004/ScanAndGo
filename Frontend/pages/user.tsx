import * as React from 'react'
import Layout from 'components/layouts'
import UserPage from 'components/pages/user'

const User = () => {
	return (
		<Layout pageTitle='RFID - User' type='base'>
            <UserPage />
		</Layout>
	)
}

export default User