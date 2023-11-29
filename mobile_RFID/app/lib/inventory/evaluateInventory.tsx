import axios from "axios"
import handleError from "../handleError"
import { BACKEND_URL } from "../consts"

const evaluateInventory = async (
  inventoryId: number,
  status: string,
  barcode: string,
  photo: string,
) => {
  try {
    const response = await axios.put(`${BACKEND_URL}/inventory/update?id=${inventoryId}`, {
      barcode,
      status,
      photo,
    })
    return response.data
  } catch (error) {
    handleError(error)
    return { error }
  }
}

export default evaluateInventory
