import * as React from 'react'
import Layout from 'components/layouts'
import InventoryPage from 'components/pages/inventory'

const NameOfItem = () => {
	return (
		<Layout pageTitle='RFID - Inventory' type='base'>
			<InventoryPage />
		</Layout>
	)
}

export default NameOfItem