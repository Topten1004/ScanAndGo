import * as React from 'react'
import Layout from 'components/layouts'
import LocationPage from 'components/pages/location'

const Location = () => {
	return (
		<Layout pageTitle='RFID - Location' type='base'>
			<LocationPage />
		</Layout>
	)
}

export default Location